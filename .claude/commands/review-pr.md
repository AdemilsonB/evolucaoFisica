# Review de PR — EvolucaoFisica

Revisa as mudanças do PR atual seguindo os padrões do projeto.

## Uso

```
/project:review-pr
```

## O que este comando faz

Execute os passos abaixo em sequência:

### 1. Obter o diff atual

```bash
git diff main...HEAD --stat
git diff main...HEAD
```

### 2. Analisar o diff com foco em

Leia `.claude/rules/java-conventions.md` e `.claude/rules/api-design.md` antes de analisar.

**Segurança**
- [ ] Ownership validado no Service para recursos do usuário
- [ ] Dados sensíveis não logados (senha, token, peso, dados pessoais)
- [ ] `@PreAuthorize` ou verificação explícita em endpoints sensíveis
- [ ] Stack trace nunca exposto ao cliente

**Arquitetura**
- [ ] Regra de negócio no Service, não no Controller
- [ ] `@Transactional` presente em operações com múltiplas escritas
- [ ] Repository sem lógica de negócio
- [ ] Entidades JPA nunca expostas diretamente no Controller (sempre DTOs)

**Banco de dados**
- [ ] Migration Flyway criada para toda alteração de schema
- [ ] Índices incluídos na migration (não deixar para depois)
- [ ] Nenhum `ddl-auto=update` introduzido

**Qualidade de código**
- [ ] Sem `System.out.println` — apenas SLF4J
- [ ] `Optional.get()` sem verificação ausente
- [ ] `BigDecimal` em cálculos de macros/nutrição
- [ ] `FetchType.LAZY` nos relacionamentos JPA
- [ ] Paginação nas listagens

**Gamificação**
- [ ] Idempotência verificada em eventos de XP
- [ ] `XpTransacao` nunca editada — apenas append
- [ ] Medalha única checada antes de conceder

**Testes**
- [ ] Testes incluídos para a lógica adicionada
- [ ] Cenários de erro cobertos (not found, unauthorized)

### 3. Gerar relatório

Formato de saída:

```
## Review PR — <branch>

### ✅ Aprovado sem ressalvas
<lista de pontos positivos>

### ⚠️ Sugestões de melhoria
<lista numerada com arquivo:linha e sugestão>

### 🔴 Bloqueadores (deve corrigir antes do merge)
<lista numerada com arquivo:linha e motivo>

### Resumo
<parecer geral em 2-3 linhas>
```
