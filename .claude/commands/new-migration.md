# Nova Migration Flyway — EvolucaoFisica

Cria o próximo arquivo de migration versionado corretamente.

## Uso

```
/project:new-migration <descricao>
```

Exemplo: `/project:new-migration add_tabela_registro_treino`

## O que este comando faz

Leia `.claude/rules/flyway-migrations.md` antes de começar.

### 1. Descobrir o próximo número de versão

```bash
ls src/main/resources/db/migration/ | sort | tail -1
```

Incrementar o número encontrado.

### 2. Criar o arquivo

Nome: `V<N>__<descricao>.sql`
Local: `src/main/resources/db/migration/`

### 3. Estrutura do arquivo

```sql
-- V<N>__<descricao>.sql
-- <Descrição do que esta migration faz>
-- Rollback: <comando SQL para reverter>

<SQL aqui>

-- Índices
<CREATE INDEX aqui>
```

### 4. Validar e rodar

```bash
./mvnw flyway:validate
./mvnw flyway:migrate
```

### 5. Checklist

- [ ] Número de versão sequencial (sem pular)
- [ ] Descrição clara em snake_case
- [ ] Índices obrigatórios incluídos
- [ ] Comentário de rollback presente
- [ ] Migration validou sem erros
