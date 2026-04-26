# Padrões de API REST — EvolucaoFisica

Leia este arquivo ao criar ou revisar Controllers e DTOs.

## Versionamento

- Todos os endpoints em `/api/v1/`
- Nunca criar endpoint fora desse prefixo

## Padrão de response de erro (obrigatório)

```json
{
  "timestamp": "2025-01-15T10:30:00Z",
  "status": 400,
  "error": "BusinessRuleException",
  "message": "Descrição clara do erro para o cliente",
  "path": "/api/v1/treinos/123"
}
```

- `timestamp`: ISO 8601 UTC
- `status`: código HTTP numérico
- `error`: nome da exceção customizada
- `message`: mensagem legível — nunca stack trace
- `path`: endpoint que gerou o erro

## Códigos HTTP

| Situação | Código |
|---|---|
| Criação bem-sucedida | 201 Created |
| Leitura / atualização | 200 OK |
| Deleção (soft delete) | 204 No Content |
| Validação / regra de negócio | 400 Bad Request |
| Não autenticado | 401 Unauthorized |
| Sem permissão / ownership | 403 Forbidden |
| Recurso não encontrado | 404 Not Found |
| Duplicidade | 409 Conflict |

## Paginação

Toda listagem deve usar `Pageable` e retornar `Page<T>`:

```java
@GetMapping
public ResponseEntity<Page<TreinoResponse>> listar(
    @AuthenticationPrincipal UserDetails user,
    Pageable pageable
) { ... }
```

Parâmetros padrão na URL: `?page=0&size=20&sort=criadoEm,desc`

## DTOs

- Sempre usar DTOs — nunca expor entidades JPA diretamente no Controller.
- Nomenclatura: `<Entidade>Request` (entrada), `<Entidade>Response` (saída).
- Validações de formato no `Request` com Bean Validation (`@NotNull`, `@NotBlank`, `@Positive`).
- Validações de regra de negócio no **Service** — nunca depender só do Controller.

```java
// Request
public record TreinoRequest(
    @NotBlank String nome,
    @NotNull Long usuarioId
) {}

// Response
public record TreinoResponse(
    Long id,
    String nome,
    LocalDateTime criadoEm
) {}
```

## Segurança nos Controllers

- Obter usuário autenticado via `@AuthenticationPrincipal` — nunca confiar em campo `usuarioId` no body.
- Ownership validado no Service, nunca no Controller.
- `ROLE_ADMIN` em endpoints administrativos com `@PreAuthorize("hasRole('ADMIN')")`.

## Nomenclatura de endpoints

```
GET    /api/v1/treinos              → listar (paginado)
GET    /api/v1/treinos/{id}         → buscar por id
POST   /api/v1/treinos              → criar
PUT    /api/v1/treinos/{id}         → atualizar completo
PATCH  /api/v1/treinos/{id}/status  → atualizar parcial / mudança de estado
DELETE /api/v1/treinos/{id}         → soft delete (204)
```

## Autenticação

- JWT: `Authorization: Bearer <token>` no header
- Access token: 15 minutos
- Refresh token: 7 dias
- Endpoint de refresh: `POST /api/v1/auth/refresh`
- Endpoint de logout revoga o refresh token: `POST /api/v1/auth/logout`
