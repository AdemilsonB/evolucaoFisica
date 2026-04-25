ALTER TABLE usuarios
    ALTER COLUMN senha DROP NOT NULL;

ALTER TABLE usuarios
    ALTER COLUMN objetivo DROP NOT NULL;

ALTER TABLE usuarios
    ADD COLUMN IF NOT EXISTS nivel_experiencia VARCHAR(20),
    ADD COLUMN IF NOT EXISTS onboarding_concluido BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS ultimo_login_em TIMESTAMP;

CREATE INDEX IF NOT EXISTS idx_usuarios_ultimo_login_em
    ON usuarios (ultimo_login_em DESC);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'ck_usuarios_nivel_experiencia'
    ) THEN
        ALTER TABLE usuarios
            ADD CONSTRAINT ck_usuarios_nivel_experiencia
            CHECK (nivel_experiencia IN ('INICIANTE', 'INTERMEDIARIO', 'AVANCADO'));
    END IF;
END $$;
