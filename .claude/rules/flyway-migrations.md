# Padrões de Migration — Flyway

Leia este arquivo ao criar qualquer alteração de schema do banco.

## Regra absoluta

`ddl-auto` nunca pode ser `update` ou `create` em staging/produção.
Toda alteração de schema passa obrigatoriamente por migration Flyway.

## Nomenclatura de arquivos

```
V<versao>__<descricao_snake_case>.sql
```

Exemplos:
```
V1__baseline_schema.sql
V2__add_tabela_exercicio.sql
V3__add_index_registro_treino.sql
V4__add_coluna_status_exercicio.sql
```

- Versão sempre incremental — nunca reutilizar número.
- Descrição clara e específica — nunca `V5__update.sql`.
- Underscore duplo `__` entre versão e descrição (obrigatório pelo Flyway).

## Local dos arquivos

```
src/main/resources/db/migration/
```

## Estrutura padrão de migration

```sql
-- V2__add_tabela_exercicio.sql
-- Criação da tabela de exercícios com índices

CREATE TABLE exercicio (
    id                BIGSERIAL PRIMARY KEY,
    nome              VARCHAR(100)  NOT NULL,
    descricao         TEXT,
    grupo_muscular    VARCHAR(50)   NOT NULL,
    equipamento       VARCHAR(50),
    dificuldade       VARCHAR(20)   NOT NULL,
    tipo_origem       VARCHAR(20)   NOT NULL DEFAULT 'GLOBAL',
    status            VARCHAR(30)   NOT NULL DEFAULT 'ATIVO',
    usuario_id        BIGINT        REFERENCES usuario(id),
    aprovado_por      BIGINT        REFERENCES usuario(id),
    midia_url         VARCHAR(500),
    criado_em         TIMESTAMP     NOT NULL DEFAULT NOW(),
    atualizado_em     TIMESTAMP     NOT NULL DEFAULT NOW()
);

-- Índices obrigatórios junto com a entidade
CREATE INDEX idx_exercicio_tipo_status_grupo
    ON exercicio(tipo_origem, status, grupo_muscular);

CREATE INDEX idx_exercicio_usuario_tipo
    ON exercicio(usuario_id, tipo_origem);

CREATE INDEX idx_exercicio_pendente
    ON exercicio(status)
    WHERE status = 'PENDENTE_REVISAO';
```

## Regras

1. Criar índices **junto com a tabela** na mesma migration — nunca depois em migration separada sem motivo.
2. Migrations são **imutáveis** após executadas em staging/produção — nunca editar um arquivo já aplicado.
3. Migrations devem ser **reversíveis quando possível** — adicionar comentário `-- Rollback:` com o comando inverso.
4. Nunca usar `DROP TABLE` ou `DROP COLUMN` sem migration de rollback planejada.
5. Soft delete: usar `status` — nunca excluir fisicamente `AtividadeXp`, `MedalhaDefinicao` ou `Exercicio`.

## Checklist antes de criar migration

- [ ] O número de versão é o próximo na sequência?
- [ ] A descrição é específica e clara?
- [ ] Os índices obrigatórios estão incluídos?
- [ ] Tem comentário de rollback?
- [ ] Testou localmente com `./mvnw flyway:migrate`?
- [ ] A migration é idempotente ou usa `IF NOT EXISTS`?
