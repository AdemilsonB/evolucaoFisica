# Backlog Priorizado - Evolucao Fisica

## Diretrizes

- Separar rigorosamente plano/modelo de execucao real.
- Nenhuma recompensa, progresso ou analytics pode depender de dado ficticio.
- Toda integracao de produto deve preservar auditabilidade, idempotencia e privacidade.
- Prioridade de entrega: fundacao do dominio -> captacao de eventos reais -> gamificacao -> social -> escala.

## Legenda de prioridade

- `P0`: fundacao obrigatoria para MVP confiavel
- `P1`: essencial para experiencia principal
- `P2`: melhora forte de produto e retencao
- `P3`: expansao recomendada

## Fase 1 - Fundacao do produto e identidade

### EPIC P0.1 - Identidade, onboarding e perfil

#### US P0.1.1 - Cadastro e autenticacao local
- Objetivo: permitir criacao de conta com email e senha.
- Valor: entrada segura no sistema.
- Entregas:
  - cadastro com validacao de email/username unicos
  - hash de senha com `PasswordEncoder`
  - recuperacao de senha
  - verificacao de email
  - gestao de sessao/token

#### US P0.1.2 - Login social Google
- Objetivo: permitir vinculacao e login via Google.
- Valor: reducao de friccao no onboarding.
- Entregas:
  - vinculo de identidade externa
  - prevencao de duplicidade por identidade externa
  - unificacao com conta local existente

#### US P0.1.3 - Onboarding do atleta
- Objetivo: coletar objetivo, peso, altura, metas e preferencias iniciais.
- Valor: personalizacao de treino, dieta e gamificacao.
- Entregas:
  - objetivo principal
  - metas de frequencia semanal
  - metas de proteina e calorias
  - configuracoes de privacidade

### EPIC P0.2 - Base tecnica de producao

#### US P0.2.1 - Migracoes de banco
- Objetivo: retirar dependencia de `ddl-auto=update`.
- Entregas:
  - Flyway/Liquibase
  - baseline de schema
  - seeds controlados para nivel, regras, medalhas e missoes

#### US P0.2.2 - Observabilidade e seguranca
- Objetivo: aumentar confiabilidade operacional.
- Entregas:
  - logs estruturados
  - tratamento global de excecao com rastreabilidade
  - auditoria de eventos criticos
  - rate limit e politicas basicas de seguranca

## Fase 2 - Treinos

### EPIC P1.1 - Biblioteca e montagem de treino

#### US P1.1.1 - Biblioteca de exercicios
- Valor: base para montagem e execucao.
- Entregas:
  - cadastro/admin de exercicios
  - grupo muscular
  - busca e filtros
  - descricao e midia de apoio

#### US P1.1.2 - Criacao de treino
- Entregas:
  - nome, descricao, observacoes
  - exercicios ordenados
  - series, reps, carga alvo, dificuldade
  - treino recorrente por dia da semana
  - treino publico/privado

### EPIC P1.2 - Execucao real do treino

#### US P1.2.1 - Iniciar e finalizar treino
- Entregas:
  - modo checklist
  - estados iniciado/concluido
  - inicio/fim e observacoes
  - motivacao do treino

#### US P1.2.2 - Registrar execucao por exercicio
- Entregas:
  - reps reais
  - carga real
  - conclusao por exercicio
  - historico detalhado

#### US P1.2.3 - Progressao real
- Entregas:
  - comparativo com ultima execucao
  - PR por exercicio
  - criterio claro para progressao

## Fase 3 - Alimentacao

### EPIC P1.3 - Base nutricional e planos

#### US P1.3.1 - Cadastro/base de alimentos
- Entregas:
  - calorias
  - proteina
  - carboidrato
  - gordura
  - acucares

#### US P1.3.2 - Plano alimentar semanal
- Entregas:
  - plano por usuario
  - dias da semana
  - refeicoes planejadas
  - composicao por alimento
  - publico/privado

### EPIC P1.4 - Execucao alimentar

#### US P1.4.1 - Registro de refeicoes reais
- Entregas:
  - refeicao por data/hora
  - alimentos e quantidade
  - totais nutricionais da refeicao

#### US P1.4.2 - Registro diario consolidado
- Entregas:
  - proteina batida
  - alimentacao alinhada
  - historico diario
  - futuros calculos por meta

## Fase 4 - Gamificacao

### EPIC P1.5 - Motor de recompensa auditavel

#### US P1.5.1 - XP transacional
- Entregas:
  - `XpTransacao` imutavel
  - idempotencia por referencia unica
  - XP por treino, alimentacao, sono, registro e progressao

#### US P1.5.2 - Nivel, tier e dashboard
- Entregas:
  - progressao acumulativa
  - tiers
  - progresso para proximo nivel
  - feed de conquistas

#### US P1.5.3 - Medalhas e missoes
- Entregas:
  - medalhas unicas e repetiveis
  - missoes semanais
  - historico vitalicio
  - reset semanal automatico

## Fase 5 - Social e comunidade

### EPIC P2.1 - Social basico

#### US P2.1.1 - Seguidores e privacidade
- Entregas:
  - seguir/desseguir
  - perfil privado
  - controle de visibilidade do conteudo

#### US P2.1.2 - Feed social
- Entregas:
  - postagens de treino/evolucao/texto/foto
  - curtidas
  - comentarios
  - feed por seguidores e grupos

#### US P2.1.3 - Grupos
- Entregas:
  - criacao de grupo
  - membros e papeis
  - conteudo restrito ao grupo

### EPIC P2.2 - Social + gamificacao

#### US P2.2.1 - Ranking social
- Entregas:
  - ranking por amigos
  - ranking por grupos
  - desempate consistente

#### US P2.2.2 - Compartilhamento de conquistas
- Entregas:
  - compartilhar medalhas
  - compartilhar marcos de treino/evolucao
  - opt-in para dados sensiveis

## Fase 6 - Engajamento e retencao

### EPIC P2.3 - Notificacoes e lembretes
- treino planejado
- refeicoes
- missoes semanais
- streak e conquistas

### EPIC P2.4 - Analytics pessoais
- consistencia semanal
- adesao alimentar
- progresso por exercicio
- comparativos por periodo

## Fase 7 - Expansao

### EPIC P3.1 - Integracoes externas
- Google Fit
- Apple Health
- wearables

### EPIC P3.2 - Experiencia avancada
- offline-first
- recomendacoes personalizadas
- desafios temporarios
- templates premium/admin

## Riscos prioritarios

1. Misturar plano com execucao real e contaminar historico.
2. Gamificacao sem reprocessamento controlado.
3. Social sem privacidade e visibilidade coerentes.
4. Falta de migracoes formais de banco para crescimento do produto.
5. Flutter iniciar sem contratos estaveis de agregacao e estados de execucao.

## Proxima entrega sugerida

1. Fechar identidade, onboarding e metas do atleta.
2. Consolidar treino real como principal fato gerador.
3. Consolidar alimentacao real com totais nutricionais.
4. Evoluir dashboard/gamificacao sobre os fatos persistidos.
5. Fechar social basico com visibilidade e ranking.
