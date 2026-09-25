# AGENTS.md

Orientações para agentes de IA (Claude Code, Codex, Cursor, etc.) trabalhando neste repositório.

## Propósito

`fisher` (artefato `scraper`) é um microsserviço de scraping. Ele existe para responder uma única pergunta: **"o
que está publicado/comentado em Facebook, Instagram e TikTok agora?"** — usando o [Apify](https://apify.com) como
fonte, sem raspagem própria.

Ele é um componente isolado dentro de um sistema maior chamado **Hawk**, uma espécie de CRM de gerenciamento de
redes sociais: Hawk cadastra contribuintes/membros, define publicações e regras de monitoramento (palavra-chave
esperada, prazo, grupo) e valida se os comentários desses contribuintes atendem a essas regras
(`member_comment.validation_status`). O `fisher` **não sabe nada disso** — ele não conhece `member`,
`monitored_entity`, `post_monitoring` nem regras de validação. Sua única responsabilidade é: dado uma URL de
post/perfil, disparar a raspagem no Apify e devolver os dados brutos mapeados em DTOs. Hawk consome esses DTOs e
faz o trabalho de cruzamento/validação do lado dele.

Essa separação é deliberada — **não introduza no `fisher` conceitos do domínio do Hawk** (membros, grupos,
validação de comentário, keywords esperadas). Se uma funcionalidade exige saber "quem é contribuinte" ou "qual é a
keyword esperada", ela pertence ao Hawk, não aqui.

## Arquitetura

Pipeline de três camadas, repetido por plataforma (Facebook, Instagram, TikTok) × tipo de dado (`post`,
`profile`, `comment`):

```
Controller  →  Service  →  Gateway  →  ApifyGenericFeign  →  API do Apify
```

- **Controller** (`controller/`): um `@RestController` por plataforma em `/fisher/api/v1/{platform}`. Fino: só loga
  tempo de execução (`StopWatch`) e delega ao service.
- **Service** (`service/`): valida entrada (a URL precisa conter o nome da plataforma, ver `InvalidURLException`),
  monta o DTO `*In` do actor, chama o gateway e valida a resposta via `ValidationsUtils.parseDataVality` antes de
  devolver.
- **Gateway** (`gateway/`): um por plataforma+tipo de dado. Concentra a interação real com o Apify — ver "Fluxo
  Apify" abaixo. Mapeia manualmente `List<Map<String, Object>>` bruto para o record `*Out` (sem
  (de)serialização genérica, campo a campo, inclusive objetos aninhados como `owner`).
- **`ApifyGenericFeign`** (`utils/`): único `@FeignClient` para todas as chamadas ao Apify (start run, get run
  details, abort run, get dataset items). Todo gateway reutiliza esta mesma interface.

Não existe abstração genérica de mapeamento — é repetição intencional. Adicionar um mapper genérico
reflexivo/baseado em Jackson quebraria o padrão do projeto; siga a convenção de map manual mesmo que pareça
verboso.

### Fluxo Apify (o coração do projeto)

1. `apifyClient.startRun(actorId, input, token)` — dispara a run do actor no Apify.
2. `ApifyPolling.waitForSucceeded(runId)` (`utils/ApifyPolling.java`) — **bloqueia a thread** em loop de
   `Thread.sleep(5000)` até o status da run virar `SUCCEEDED`. Não há timeout nem backoff: se o Apify travar em
   `RUNNING`, a requisição HTTP fica pendurada indefinidamente (só há tratamento dedicado para rate limit via
   exception, não para runs que nunca terminam).
3. `apifyClient.getDatasetItems(runId, token)` — busca o resultado bruto.
4. Mapeamento manual map→record no gateway.

### Scraping parcial ("delta") de comentários — a função mais complexa do projeto

`IgCommentsGateway.getDeltaInstagramComments` (`POST /fisher/api/v1/instagram/comment/delta`) foge do fluxo padrão
acima. Em vez de esperar a run terminar, ele faz **polling do dataset enquanto a run ainda está `RUNNING`**:

- A cada ~4s (`parseDeltaComments`), busca os itens já raspados até o momento e verifica se algum
  `lastCommentsIds` (IDs de comentários que o chamador já conhece) já apareceu.
- Assim que um ID conhecido aparece, a run é **abortada antecipadamente** (`apifyClient.abortRun`) e só os
  comentários raspados *antes* desse ID são retornados (`subList(0, i)`) — ou seja, só o que é novo desde o
  último comentário conhecido.
- Se a run terminar sozinha (`SUCCEEDED`/`ABORTED`) sem nunca achar o delta, devolve tudo que foi raspado com
  `deltaFound = false`.

Existe para evitar reraspar uma thread de comentários inteira quando só os mais recentes interessam (ex.: Hawk
checando comentários novos numa publicação já monitorada há dias).

## Tratamento de erros

- `GlobalFeignDecoder` (`decoder/`) — único `ErrorDecoder` Feign, mapeia status HTTP do Apify (400/401/403/404/
  405/429/default) para exceptions dedicadas em `exception/apify/`, todas estendendo `GenericCoreException`
  (mensagem + status code).
- `ExternalApifyExceptionHandler` (`handler/`) — `@ControllerAdvice` que converte essas exceptions em
  `GenericMessageOut`.
- `GlobalExceptionHandler` (`handler/`) — trata `MethodArgumentNotValidException` (bean validation → campo a
  campo em `BeansValidationOut`) e as exceptions próprias (`InvalidURLException`, `InvalidResponseDataException`).
- `ValidatablePayload.isBlankPayload()` — interface implementada por todo DTO `*Out`; `ValidationsUtils.
  parseDataVality` a usa para checar centralizadamente que a resposta não é nula/vazia/"em branco" antes do
  service devolver dado ao chamador.

## Convenções ao adicionar uma nova combinação plataforma/tipo de dado

Siga o padrão existente à risca:

1. Entrada em `apify.actor-id.*` (`application.yml`).
2. DTO `*In` (entrada do actor) em `dto/in/apify/`.
3. DTO/record `*Out` implementando `ValidatablePayload` em `dto/out/`.
4. `*Gateway` em `gateway/` que chama `ApifyGenericFeign` + `ApifyPolling` e mapeia manualmente os itens brutos.
5. Método de `Service` que valida entrada/saída.
6. Endpoint de `Controller` em `/fisher/api/v1/{platform}`.

Não pule etapas nem colapse camadas "para simplificar" — a uniformidade entre as 9 combinações (3 plataformas × 3
tipos) é o que torna o projeto previsível.

## Pontos de atenção conhecidos (TODOs no código)

- `IgCommentsGateway.getDeltaInstagramComments`: monta o `IgCommentsIn` na própria gateway em vez de receber pronto
  do `InstagramService` — quebra a separação de responsabilidade Service/Gateway usada no resto do projeto.
- `InstagramService`: instanciação de input do client Feign ainda vive fora do lugar esperado; e há dúvida em
  aberto sobre se filtros de comentário deveriam ficar no Hawk em vez do `fisher`.
- `ValidationsUtils`: falta validação de URL reutilizável (hoje cada `Service` reimplementa o check "contém nome
  da plataforma" à mão, repetido por método dentro da mesma classe).
- Mensagens de erro com copy-paste: `FacebookService.java:42` e `TiktokService.java:37` lançam
  `InvalidURLException` com o texto "objeto do Instagram" mesmo validando URL de Facebook/TikTok.
