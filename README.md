# 🎣 Fisher — Motor de Scraping de Redes Sociais

> Um microsserviço de scraping de alta performance construído como uma camada de abstração limpa sobre os actors da Apify.

Fisher é um serviço Spring Boot especializado que gerencia toda a coleta de dados de redes sociais (Instagram, Facebook, TikTok). Pense nele como um cliente inteligente que esconde a complexidade da API assíncrona da Apify por trás de endpoints REST simples.

---

## 🎯 O Que o Fisher Faz

Fisher transforma esta complexidade:
```
1. Iniciar run do actor Apify
2. Fazer polling a cada 5 segundos até SUCCEEDED
3. Extrair ID do dataset
4. Buscar resultados brutos
5. Mapear JSON aninhado para objetos de domínio
6. Tratar 5+ cenários de erro diferentes
```

Nesta simplicidade:
```bash
GET /fisher/api/v1/instagram/comment?postUrl=https://instagram.com/p/xyz
→ Retorna dados de comentários limpos e tipados
```

---

## 🏗️ Destaques da Arquitetura

### Padrão Gateway
Cada rede social tem gateways dedicados (`IgCommentsGateway`, `FbPostGateway`, etc.) que:
- Encapsulam configuração dos actors Apify
- Gerenciam polling assíncrono até conclusão
- Transformam o caos `Map<String, Object>` em DTOs limpos
- Registram tempo de execução e contagem de itens

### Tradução Inteligente de Erros
`ErrorDecoder` customizado intercepta exceções Feign e traduz:
- `401` → `InvalidTokenException`
- `429` → `RateLimitExceededException`
- `404` → `ResourceNotFoundException`

### Polling Assíncrono com Resiliência
Faz polling das runs Apify a cada 5 segundos, registrando progresso sem bloquear a aplicação. Suporta abort gracioso para scraping delta (buscar apenas comentários novos desde a última verificação).

---

## 🚀 Funcionalidades Implementadas

✅ **Instagram:** Posts, Comentários (completo & delta), Perfis  
✅ **Facebook:** Posts, Comentários, Perfis  
✅ **TikTok:** Posts, Comentários, Perfis  
✅ **Tratamento de Erros:** `@ControllerAdvice` global + decoder específico Apify  
✅ **Validação:** Bean validation em todas as entradas  
✅ **Logging:** SLF4J com timing de execução e progresso detalhado  
✅ **Mapeamento DTO:** Objetos de resposta type-safe com suporte `@JsonAlias`  
✅ **Detecção de Payload Vazio:** Interface `ValidatablePayload` previne retorno de dados vazios  

---

## 🔧 Stack Tecnológica

- **Spring Boot 4.0.0** — Framework de última geração
- **OpenFeign** — Cliente HTTP declarativo para API Apify
- **Lombok** — Redução de boilerplate
- **Java 21** — Features modernas da linguagem
- **SLF4J + Logback** — Logging estruturado

---

## 📋 Próxima mudanças

🔲 **Spring Security + JWT** — Camada de autenticação/autorização  
🔲 **Rate Limiting** — Prevenir abuso com Bucket4j  
🔲 **Cache** — Camada Redis para requisições repetidas  
🔲 **Métricas** — Actuator + Prometheus para monitoramento  
🔲 **Circuit Breaker** — Resilience4j para downtime da Apify  
🔲 **Documentação API** — Springdoc OpenAPI (Swagger UI)  
🔲 **Docker** — Containerização para deploy fácil  
🔲 **Testes de Integração** — WireMock para mock da Apify  

---

## ⚙️ Configuração & Execução

### Pré-requisitos
- Java 21+
- Maven 3.9+
- Token API da Apify

### Configuração
Crie `src/main/resources/application-prod.yml`:
```yaml
apify:
  token: seu_token_apify_aqui
```

### Executar Localmente
```bash
./mvnw spring-boot:run
```

Serviço inicia em `http://localhost:8080`

### Exemplo de Requisição
```bash
curl "http://localhost:8080/fisher/api/v1/instagram/comment?postUrl=https://instagram.com/p/ABC123"
```

---

## 📊 Endpoints da API

### Instagram
- `GET /fisher/api/v1/instagram/comment?postUrl={url}` — Todos os comentários
- `GET /fisher/api/v1/instagram/post?postUrl={url}` — Detalhes do post
- `GET /fisher/api/v1/instagram/profile?profile={username}` — Info do perfil

### Facebook
- `GET /fisher/api/v1/facebook/comment?postUrl={url}`
- `GET /fisher/api/v1/facebook/post?postUrl={url}`
- `GET /fisher/api/v1/facebook/profile?profile={id}`

### TikTok
- `GET /fisher/api/v1/tiktok/comment?postUrl={url}`
- `GET /fisher/api/v1/tiktok/post?postUrl={url}`
- `GET /fisher/api/v1/tiktok/profile?profile={username}`

Todas as respostas seguem estrutura DTO consistente com campos tipados e mapeamento `@JsonAlias`.

---

## 🛡️ Filosofia de Tratamento de Erros

Fisher nunca expõe complexidade interna aos consumidores:
- Erros de API externa → Traduzidos para exceções de domínio
- Inputs inválidos → Bean validation com erros por campo
- Respostas vazias → `InvalidResponseDataException` com dicas de retry
- Falhas de rede → Logadas e encapsuladas em `DefaultIntegrationException`

Clientes recebem códigos HTTP limpos e mensagens de erro legíveis.

---

## 📝 Licença

Este é um projeto privado. Não licenciado para uso público.

---

**Desenvolvido com ☕ por Lucas Pimentel**  
*Parte do ecossistema Hawk Audit Platform*
