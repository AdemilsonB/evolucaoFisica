-- Uso manual em PostgreSQL.
-- Execute este arquivo quando quiser limpar o schema atual antes de recriar as tabelas.

DROP TABLE IF EXISTS postagens_curtidas CASCADE;
DROP TABLE IF EXISTS postagens_comentarios CASCADE;
DROP TABLE IF EXISTS grupos_membros CASCADE;
DROP TABLE IF EXISTS refeicao_alimentos CASCADE;
DROP TABLE IF EXISTS registro_exercicios CASCADE;
DROP TABLE IF EXISTS planos_alimentares_refeicoes_alimentos CASCADE;
DROP TABLE IF EXISTS planos_alimentares_refeicoes CASCADE;
DROP TABLE IF EXISTS postagens CASCADE;
DROP TABLE IF EXISTS refeicoes CASCADE;
DROP TABLE IF EXISTS registros_treino CASCADE;
DROP TABLE IF EXISTS usuarios_missoes_semanais CASCADE;
DROP TABLE IF EXISTS usuarios_medalhas CASCADE;
DROP TABLE IF EXISTS registros_diarios CASCADE;
DROP TABLE IF EXISTS planos_alimentares_dias CASCADE;
DROP TABLE IF EXISTS treino_exercicios CASCADE;
DROP TABLE IF EXISTS grupos CASCADE;
DROP TABLE IF EXISTS seguidores CASCADE;
DROP TABLE IF EXISTS usuarios_identidades_externas CASCADE;
DROP TABLE IF EXISTS perfis_gamificacao_usuarios CASCADE;
DROP TABLE IF EXISTS metas_atletas CASCADE;
DROP TABLE IF EXISTS evolucoes_fisicas CASCADE;
DROP TABLE IF EXISTS planos_alimentares CASCADE;
DROP TABLE IF EXISTS treinos CASCADE;
DROP TABLE IF EXISTS exercicios CASCADE;
DROP TABLE IF EXISTS alimentos CASCADE;
DROP TABLE IF EXISTS xp_transacoes CASCADE;
DROP TABLE IF EXISTS missoes_semanais CASCADE;
DROP TABLE IF EXISTS medalhas CASCADE;
DROP TABLE IF EXISTS xp_regras CASCADE;
DROP TABLE IF EXISTS niveis CASCADE;
DROP TABLE IF EXISTS usuarios CASCADE;
