# Novo Feature — EvolucaoFisica

Cria a estrutura completa de um novo recurso seguindo a arquitetura do projeto.

## Uso

```
/project:new-feature <modulo> <entidade>
```

Exemplo: `/project:new-feature treino Exercicio`

## O que este comando faz

Leia as regras em `.claude/rules/java-conventions.md` e `.claude/rules/api-design.md` antes de começar.

Ao executar, crie na ordem:

1. **Entidade JPA** em `src/main/java/com/evolucaofisica/<modulo>/domain/<Entidade>.java`
   - Anotações: `@Entity`, `@Table`, `@Id`, `@GeneratedValue`
   - Campos: `criadoEm`, `atualizadoEm` com `@CreationTimestamp` / `@UpdateTimestamp`
   - Relacionamentos com `FetchType.LAZY` por padrão

2. **Repository** em `.../repository/<Entidade>Repository.java`
   - Extende `JpaRepository<Entidade, Long>`
   - Adicionar queries customizadas com `@Query` se necessário

3. **DTOs** em `.../dto/`
   - `<Entidade>Request.java` com Bean Validation
   - `<Entidade>Response.java` como record

4. **Service** em `.../service/<Entidade>Service.java`
   - `@Service`, `@RequiredArgsConstructor`
   - `@Transactional` em operações de escrita
   - `@Transactional(readOnly = true)` em leituras
   - Validação de ownership do usuário autenticado
   - Lançar exceções customizadas corretas

5. **Controller** em `.../controller/<Entidade>Controller.java`
   - `@RestController`, `@RequestMapping("/api/v1/<recurso>")`
   - Paginação em listagens (`Pageable`)
   - `@AuthenticationPrincipal` para obter usuário autenticado

6. **Migration Flyway** em `src/main/resources/db/migration/V<N>__add_tabela_<entidade>.sql`
   - Tabela com todos os campos
   - Índices obrigatórios junto com a tabela
   - Comentário de rollback

7. **Teste unitário** em `src/test/java/.../<Entidade>ServiceTest.java`
   - Cenários do caminho feliz
   - Cenário de recurso não encontrado
   - Cenário de acesso não autorizado (ownership)

## Checklist de validação

Antes de finalizar, verifique:
- [ ] `System.out.println` ausente em todo o código gerado
- [ ] Todos os relacionamentos JPA com `FetchType.LAZY`
- [ ] Ownership validado no Service
- [ ] `BigDecimal` usado onde há cálculo numérico de nutrição/macros
- [ ] Migration com índices incluídos
- [ ] Testes com padrão Arrange/Act/Assert
