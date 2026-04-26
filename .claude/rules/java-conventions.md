# Convenções Java — EvolucaoFisica

Leia este arquivo antes de gerar ou revisar qualquer código Java do projeto.

## Nomenclatura

- Variáveis e métodos: `camelCase`
- Classes e interfaces: `PascalCase`
- Constantes: `UPPER_SNAKE_CASE`
- Pacotes: `com.evolucaofisica.<modulo>.<camada>` (ex: `com.evolucaofisica.treino.service`)

## Arquitetura em camadas

```
Controller → Service → Repository
```

- **Controller:** valida formato da requisição, delega ao Service, nunca aplica regra de negócio.
- **Service:** aplica regras de negócio, lança exceções customizadas, gerencia transações com `@Transactional`.
- **Repository:** apenas acesso ao banco via JPA/Hibernate. Sem lógica de negócio.

## Transações

- `@Transactional` explícito em **toda** operação com múltiplas escritas no Service.
- Nunca colocar `@Transactional` no Controller.
- Métodos de leitura: `@Transactional(readOnly = true)`.

## Optional e nulos

```java
// CORRETO
usuario.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado: " + id));

// PROIBIDO
usuario.findById(id).get(); // nunca .get() sem verificação
```

## Tipos numéricos

- `BigDecimal` para **todos** os cálculos de macros, calorias, nutrição e valores monetários.
- Nunca `double` ou `float` para esses domínios.

## JPA e N+1

- `LAZY` loading por padrão em todos os relacionamentos.
- Usar `JOIN FETCH` ou `@EntityGraph` em queries que precisam dos relacionamentos.
- Nunca deixar N+1 silencioso — detectar com Hibernate statistics se necessário.

## Paginação

- Toda listagem deve aceitar e retornar `Pageable` / `Page<T>`.
- Nunca retornar `List<T>` em endpoint de listagem sem paginação.

## Logs (SLF4J)

```java
log.info("Treino iniciado. usuarioId={}, treinoId={}", usuarioId, treinoId);   // fluxo normal
log.warn("Tentativa de acesso negada. usuarioId={}", usuarioId);               // desvio de regra
log.error("Erro ao processar pagamento. pedidoId={}", pedidoId, e);            // exceção real
```

- `System.out.println` é **proibido** em qualquer camada.
- **Nunca** logar: senha, token JWT, peso, dados pessoais ou dados sensíveis.

## Exceções customizadas

| Exceção | Quando usar |
|---|---|
| `ResourceNotFoundException` | Entidade não encontrada no banco |
| `BusinessRuleException` | Violação de regra de negócio |
| `DuplicateEntryException` | Tentativa de duplicidade (ex: mesmo treino no mesmo dia) |
| `UnauthorizedAccessException` | Usuário tentando acessar recurso de outro usuário |

- Sempre usar mensagem descritiva na exceção.
- Nunca expor stack trace ao cliente — o `@ControllerAdvice` cuida disso.

## Segurança e ownership

- Validar **propriedade do recurso** no Service: o usuário autenticado só pode acessar/modificar o próprio dado.
- Usar `@PreAuthorize` ou verificação explícita de ownership em todo endpoint sensível.
- `ROLE_ADMIN` e `ROLE_USER` nunca se misturam em lógica de negócio.

## Soft delete

- `AtividadeXp`, `MedalhaDefinicao` e `Exercicio` nunca são excluídos fisicamente.
- Usar campo `status` com enum: `ATIVO | INATIVO | DRAFT | PENDENTE_REVISAO`.

## Imutabilidade de histórico

- `XpTransacao` é append-only — nunca editar, nunca excluir.
- `EventoAtividade` é imutável após criação.
- Gravar snapshot do valor no momento da concessão.
