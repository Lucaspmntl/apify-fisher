# CLAUDE.md

Este arquivo fornece orientações ao Claude Code (claude.ai/code) ao trabalhar com o código deste repositório.

## O que é

`fisher` (artefato `scraper`) é uma API REST em Spring Boot 4 / Java 21 que raspa dados do Facebook, Instagram e
TikTok (posts, perfis, comentários) acionando actors do [Apify](https://apify.com) e fazendo polling das runs até a
conclusão. Os resultados são expostos como DTOs JSON próprios da aplicação.

## Comandos

```
./mvnw spring-boot:run          # roda localmente (profile padrão: prod, porta 8080)
./mvnw test                     # roda todos os testes
./mvnw test -Dtest=ClassName    # roda uma única classe de teste
./mvnw compile                  # apenas compila
```

Não há `application-*.yml` dedicado para `dev`/`test` — `application.yml` fixa `spring.profiles.active: prod`, e
`application-prod.yml` fornece o `apify.token`. Os IDs dos actors de cada plataforma/tipo de dado ficam em
`application.yml`, em `apify.actor-id.*`.

## Arquitetura

Cada plataforma (Facebook, Instagram, TikTok) segue o mesmo pipeline de três camadas, repetido para cada tipo de
dado (`comment`, `post`, `profile`):

```
Controller  -->  Service  -->  Gateway  -->  ApifyGenericFeign (Feign client)  -->  API do Apify
```

- **Controller** (`controller/`): fino, um `@RestController` por plataforma, mapeado em `/fisher/api/v1/{platform}`.
  Loga o tempo de execução via `StopWatch` e delega diretamente ao service.
- **Service** (`service/`): valida a entrada (ex.: a URL precisa conter o nome da plataforma — ver
  `InvalidURLException`), monta o DTO de entrada do actor, chama o gateway e valida se a resposta não está
  vazia/em branco via `ValidationsUtils.parseDataVality` antes de retornar.
- **Gateway** (`gateway/`): um por plataforma+tipo de dado (ex.: `FbCommentsGateway`, `IgPostGateway`). Concentra a
  interação de fato com o Apify:
  1. `apifyClient.startRun(actorId, input, token)` inicia a run do actor.
  2. `ApifyPolling.waitForSucceeded(runId)` bloqueia (loop de sleep fixo de 5s) até o status da run ficar
     `SUCCEEDED`.
  3. `apifyClient.getDatasetItems(runId, token)` busca o resultado bruto em `List<Map<String, Object>>`.
  4. Os maps brutos são mapeados manualmente para o record `*Out` da plataforma (sem (de)serialização genérica —
     cada gateway extrai os campos do map à mão, inclusive objetos aninhados como `owner`).
- **`ApifyGenericFeign`** (`utils/`): o único `@FeignClient` compartilhado para todas as chamadas ao Apify (start
  run, get run details, abort run, get dataset items). Todos os gateways o reutilizam em vez de declarar seus
  próprios Feign clients.

### Raspagem parcial ("delta") de comentários

`IgCommentsGateway.getDeltaInstagramComments` (exposto via `POST /fisher/api/v1/instagram/comment/delta` no
`InstagramController`) é um fluxo alternativo: em vez de fazer polling até a run terminar, ele faz polling do
*dataset* enquanto a run ainda está `RUNNING`, verificando a cada tentativa se algum dos `lastCommentsIds`
informados pelo chamador já apareceu nos itens raspados (`parseDeltaComments`). Assim que um ID de comentário
conhecido aparece, a run é abortada antecipadamente e são retornados apenas os comentários raspados *antes* desse
ID — ou seja, só o que é novo desde o último comentário conhecido pelo chamador. Isso existe para evitar reraspar
uma thread de comentários inteira quando só os comentários mais recentes são necessários.

### Tratamento de erros

- `GlobalFeignDecoder` (`decoder/`) é o `ErrorDecoder` de todas as chamadas Feign ao Apify: mapeia os status HTTP do
  Apify (400/401/403/404/405/429/outros) para uma exception dedicada por caso, em `exception/apify/`, todas
  estendendo `GenericCoreException` (mensagem + status code).
- `ExternalApifyExceptionHandler` (`handler/`) é o `@ControllerAdvice` que transforma essas exceptions do Apify em
  respostas `GenericMessageOut`.
- `GlobalExceptionHandler` (`handler/`) trata falhas de bean validation (`MethodArgumentNotValidException` →
  `BeansValidationOut`, campo a campo) além de `InvalidURLException` / `InvalidResponseDataException` próprias da
  aplicação.
- A validade da resposta (não nula, não vazia, não "em branco") é checada via a interface `ValidatablePayload`
  (`isBlankPayload()`), implementada pelos DTOs `*Out` e verificada de forma centralizada em
  `ValidationsUtils.parseDataVality`.

### Adicionando uma nova combinação plataforma/tipo de dado

Siga o padrão existente: adicione uma entrada em `apify.actor-id.*`, um DTO `*In` (entrada do actor) e um
DTO/record `*Out` (implementando `ValidatablePayload`), um `*Gateway` que chama `ApifyGenericFeign` +
`ApifyPolling` e mapeia manualmente os itens brutos do dataset, um método de `Service` que valida entrada/saída, e
um endpoint de `Controller` em `/fisher/api/v1/{platform}`.
