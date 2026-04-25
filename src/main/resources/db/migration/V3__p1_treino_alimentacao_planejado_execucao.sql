ALTER TABLE exercicios
    ADD COLUMN IF NOT EXISTS equipamento VARCHAR(80),
    ADD COLUMN IF NOT EXISTS ativo BOOLEAN NOT NULL DEFAULT TRUE;

CREATE INDEX IF NOT EXISTS idx_exercicios_equipamento
    ON exercicios (equipamento);

ALTER TABLE treino_exercicios
    ADD COLUMN IF NOT EXISTS ordem INTEGER;

WITH ordenados AS (
    SELECT id,
           ROW_NUMBER() OVER (PARTITION BY treino_id ORDER BY id) AS nova_ordem
    FROM treino_exercicios
)
UPDATE treino_exercicios te
SET ordem = ordenados.nova_ordem
FROM ordenados
WHERE te.id = ordenados.id
  AND te.ordem IS NULL;

ALTER TABLE treino_exercicios
    ALTER COLUMN ordem SET NOT NULL;

CREATE INDEX IF NOT EXISTS idx_treino_exercicios_treino_ordem
    ON treino_exercicios (treino_id, ordem, id);

ALTER TABLE registros_treino
    ADD COLUMN IF NOT EXISTS planejado_para TIMESTAMP,
    ADD COLUMN IF NOT EXISTS iniciado_em TIMESTAMP,
    ADD COLUMN IF NOT EXISTS abortado_em TIMESTAMP,
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);

UPDATE registros_treino
SET planejado_para = COALESCE(planejado_para, data_registro),
    iniciado_em = CASE
        WHEN iniciado_em IS NULL AND data_registro IS NOT NULL THEN data_registro
        ELSE iniciado_em
    END,
    status = CASE
        WHEN status IS NOT NULL THEN status
        WHEN concluido = TRUE THEN 'CONCLUIDO'
        ELSE 'INICIADO'
    END;

ALTER TABLE registros_treino
    ALTER COLUMN planejado_para SET NOT NULL,
    ALTER COLUMN status SET NOT NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'ck_registros_treino_status'
    ) THEN
        ALTER TABLE registros_treino
            ADD CONSTRAINT ck_registros_treino_status
            CHECK (status IN ('PLANEJADO', 'INICIADO', 'CONCLUIDO', 'ABORTADO'));
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_registros_treino_usuario_status_data
    ON registros_treino (usuario_id, status, planejado_para DESC);

ALTER TABLE registro_exercicios
    ADD COLUMN IF NOT EXISTS treino_exercicio_id BIGINT;

ALTER TABLE registro_exercicios
    ALTER COLUMN repeticoes_real DROP NOT NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_registro_exercicios_treino_exercicio'
    ) THEN
        ALTER TABLE registro_exercicios
            ADD CONSTRAINT fk_registro_exercicios_treino_exercicio
            FOREIGN KEY (treino_exercicio_id) REFERENCES treino_exercicios (id);
    END IF;
END $$;

CREATE UNIQUE INDEX IF NOT EXISTS uk_registro_exercicios_registro_treino_exercicio
    ON registro_exercicios (registro_treino_id, treino_exercicio_id)
    WHERE treino_exercicio_id IS NOT NULL;

ALTER TABLE planos_alimentares
    ADD COLUMN IF NOT EXISTS principal BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS data_inicio DATE,
    ADD COLUMN IF NOT EXISTS data_fim DATE;

CREATE INDEX IF NOT EXISTS idx_planos_alimentares_usuario_principal
    ON planos_alimentares (usuario_id, principal, ativo);

ALTER TABLE registros_diarios
    ADD COLUMN IF NOT EXISTS plano_alimentar_id BIGINT;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_registros_diarios_plano_alimentar'
    ) THEN
        ALTER TABLE registros_diarios
            ADD CONSTRAINT fk_registros_diarios_plano_alimentar
            FOREIGN KEY (plano_alimentar_id) REFERENCES planos_alimentares (id);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_registros_diarios_usuario_data_plano
    ON registros_diarios (usuario_id, data_referencia DESC, plano_alimentar_id);
