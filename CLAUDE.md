# 📋 Contexto do Projeto — Sistema Fitness Gamificado

> Documento de referência completo. Use como contexto fixo em qualquer sessão de desenvolvimento.

---

## 🎯 Objetivo do Sistema

Plataforma fitness unificada que centraliza todas as frentes do mundo da musculação e atividades físicas. O usuário pode registrar treinos, controlar alimentação, acompanhar outras atividades (corrida, caminhada, natação), montar treinos personalizados e evoluir dentro de um sistema de gamificação completo com ranking, medalhas e missões semanais.

**Público-alvo:** atletas e praticantes de academia e atividades físicas em geral.

---

## 🛠️ Tecnologias Utilizadas

| Camada | Tecnologia |
|---|---|
| Backend | Java + Spring Boot |
| Banco de dados | PostgreSQL |
| ORM | JPA / Hibernate |
| Frontend | Flutter (não iniciado) |
| Migração de schema | Flyway |
| Autenticação | JWT + OAuth2 Google |
| Hash de senha | BCrypt |
| Logs | SLF4J |
| Testes | JUnit 5 + Mockito |

---

## 🏗️ Arquitetura do Sistema

### Padrão de camadas
```
Controller → Service → Repository
```

- **Controller:** recebe requisição, valida formato, delega ao Service.
- **Service:** aplica regras de negócio, lança exceções customizadas, gerencia transações.
- **Repository:** acesso ao banco via JPA/Hibernate.

### REST API
- Versionada: `/api/v1/`
- Paginação obrigatória em todas as listagens (`Pageable`)
- Response de erro padronizado:
```json
{
  "timestamp": "",
  "status": 400,
  "error": "BusinessRuleException",
  "message": "Descrição do erro",
  "path": "/api/v1/recurso"
}
```

### Banco de dados
- PostgreSQL com migrações controladas via **Flyway** (nunca `ddl-auto=update` em staging/produção)
- Toda alteração de schema via migration versionada e reversível

---

## 📦 Módulos do Sistema

### M1 — Treinos
### M2 — Alimentação
### M3 — Gamificação
### M4 — Rede Social
### M5 — Infra, Auth e Configurações

---

## 🏋️ M1 — Módulo de Treinos

### Funcionalidades
- Criar treinos personalizados com seleção de exercícios
- Salvar, editar, duplicar e versionar treinos
- Executar treino em modo checklist (série por série)
- Registrar treino realizado: data, exercícios, cargas e repetições
- Acompanhar histórico de treinos por período
- Comparativo entre última execução e execução atual
- PRs pessoais por exercício
- Histórico por exercício específico
- Métricas: volume total, frequência semanal, constância

### Entidades principais
| Entidade | Descrição |
|---|---|
| `Treino` | Plano criado pelo usuário |
| `TreinoExercicio` | Exercício dentro do plano (ordem, séries/reps/carga alvo) |
| `Exercicio` | Biblioteca de exercícios (global ou personalizado do usuário) |
| `RegistroTreino` | Execução real (data, início, fim, estado, observação) |
| `RegistroExercicio` | Execução real por exercício (séries, reps, carga realizados) |

### Biblioteca de Exercícios — Dinâmica

Admin gerencia a biblioteca global por tela. Usuário pode adicionar exercício personalizado quando não encontrar na lista. Exercício personalizado pode ser sugerido ao admin e promovido a global.

**Campos de `Exercicio`:**
```
nome, descricao, grupo_muscular, equipamento, dificuldade
tipo_origem     → GLOBAL | PERSONALIZADO
status          → ATIVO | INATIVO | PENDENTE_REVISAO  (soft delete)
usuario_id      → null se GLOBAL; preenchido se PERSONALIZADO
aprovado_por    → admin_id que aprovou (null enquanto não aprovado)
midia_url       → vídeo/gif de demonstração (opcional)
criado_em / atualizado_em
```

**Fluxo de exercício personalizado:**
```
1. Usuário busca e não encontra → cria exercício personalizado
2. Sistema pergunta: "Deseja sugerir para a biblioteca global?"
3. Se sim → status = PENDENTE_REVISAO → entra na fila do admin
4. Admin aprova (vira GLOBAL) ou rejeita (permanece PERSONALIZADO do usuário)
5. Se aprovado: disponível para todos, histórico do usuário preservado
```

**Regras:**
- Usuário vê na busca: exercícios GLOBAL ativos + seus próprios PERSONALIZADO ativos
- Exercício PERSONALIZADO não aparece para outros usuários
- Exercício personalizado conta normalmente para XP e progressão
- Inativar exercício GLOBAL pelo admin preserva todos os registros históricos
- Admin pode mesclar exercício sugerido com um global existente (evitar duplicatas)

**Índices:**
```sql
exercicio(tipo_origem, status, grupo_muscular)
exercicio(usuario_id, tipo_origem)
exercicio(status) WHERE status = 'PENDENTE_REVISAO'
```

### Estados do treino
```
PLANEJADO → INICIADO → CONCLUÍDO
                     → ABORTADO
```

### Regras de negócio
- Treino planejado e execução real são **entidades separadas — nunca misturar**
- Conclusão parcial é permitida e registrada como tal
- Progressão real = aumento de carga **ou** reps vs última execução do mesmo exercício (comparar `RegistroExercicio` anterior persistido)
- XP de progressão (+100) só concedido se critério acima for verdadeiro e persistido
- XP de treino só concedido ao finalizar (estado `CONCLUÍDO`) — nunca em `ABORTADO`
- Idempotência: chave única `(usuario_id, treino_id, data_registro)` — não permitir dois registros do mesmo treino no mesmo dia
- Salvar progresso incremental durante execução (série por série)

### Índices obrigatórios
```sql
registro_treino(usuario_id, data_registro)
registro_exercicio(registro_treino_id)
registro_exercicio(exercicio_id, usuario_id)
exercicio(grupo_muscular, dificuldade)
```

---

## 🥗 M2 — Módulo de Alimentação

### Funcionalidades
- Montar planos alimentares semanais por dia da semana
- Salvar, editar, duplicar e versionar planos
- Executar plano durante a semana (registrar refeições do dia)
- Registrar refeições fora do plano sem punição
- Calcular macros e calorias por refeição e consolidado diário
- Acompanhar histórico de adesão alimentar por período
- Metas nutricionais por usuário (proteína, calorias, carboidratos, gorduras)

### Entidades principais
| Entidade | Descrição |
|---|---|
| `Alimento` | Tabela nutricional global (calorias/100g, proteína, carbo, gordura) |
| `PlanoAlimentar` | Plano do usuário (nome, objetivo, ativo, público/privado) |
| `PlanoAlimentarDia` | Dia da semana do plano |
| `PlanoAlimentarRefeicao` | Refeição planejada no dia |
| `PlanoAlimentarItem` | Alimento com quantidade dentro da refeição planejada |
| `RegistroDiario` | Execução real do dia (proteína batida, alimentação ok, observação) |
| `RegistroRefeicao` | Refeição real registrada no dia |
| `RegistroRefeicaoItem` | Alimento consumido com quantidade real |
| `MetaAtleta` | Metas nutricionais do usuário (entidade própria) |

### Regras de negócio
- Plano alimentar e registro diário são **entidades separadas — nunca misturar**
- "Proteína batida" = meta de proteína atingida com base na soma real de `RegistroRefeicaoItem`
- Apenas **um plano pode estar ativo** por vez por usuário (troca desativa o anterior)
- Refeição fora do plano é permitida sem punição de consistência
- Edição retroativa do histórico deve ser auditada (`atualizado_em` obrigatório)
- Usar **BigDecimal** para todos os cálculos de macros/calorias (nunca `double`)
- ✅ Validação no Service implementada
- ✅ Totais nutricionais por refeição implementados

### Índices obrigatórios
```sql
registro_diario(usuario_id, data)
registro_refeicao(usuario_id, data)
plano_alimentar(usuario_id, ativo)
plano_alimentar_item(refeicao_id)
```

---

## 🎮 M3 — Módulo de Gamificação

### Princípio
Nenhuma atividade de XP, medalha ou critério é hardcoded. Tudo é criado, editado e desativado pelo admin por tela, em tempo de execução, sem necessidade de deploy.

### Funcionalidades
- Motor de XP dinâmico configurável pelo admin
- Medalhas criadas e gerenciadas pelo admin por tela
- Níveis progressivos com fórmula definida
- Tiers de ranking automáticos por nível
- Missões semanais com reset automático e histórico total
- Dashboard de progresso visual
- Atividades sazonais com vigência configurável
- Painel admin para gestão completa sem código

### Entidades principais

| Entidade | Descrição |
|---|---|
| `PerfilGamificacao` | XP total, nível, tier, sequências do usuário |
| `AtividadeXp` | Definição de atividade criada pelo admin |
| `AtividadeXpCriterio` | Critérios parametrizáveis para conclusão |
| `AtividadeXpRecompensa` | XP e recompensas vinculadas (com versionamento) |
| `EventoAtividade` | Registro imutável de cada evento real do usuário |
| `XpTransacao` | Log auditável de XP concedido (append-only) |
| `MedalhaDefinicao` | Catálogo de medalhas gerenciado pelo admin |
| `MedalhaUsuario` | Medalha concedida ao usuário (com contador para repetíveis) |
| `MissaoDefinicao` | Catálogo de missões semanais |
| `MissaoSemanalUsuario` | Progresso da missão na semana atual |
| `MissaoHistoricoTotal` | Total acumulado de missões concluídas por usuário |

---

### 🎯 Motor de XP Dinâmico

**Campos de `AtividadeXp`:**
```
codigo           → slug único gerado automaticamente
nome, descricao
categoria        → TREINO | ALIMENTACAO | SONO | REGISTRO | SOCIAL | CUSTOM
tipo_gatilho     → evento disparador (ex: FINALIZOU_TREINO, REGISTROU_DIA)
status           → DRAFT | ATIVO | INATIVO  (soft delete)
unico_por_dia    → boolean
unico_por_semana → boolean
recorrente       → boolean
data_inicio / data_fim → vigência opcional (atividades sazonais)
versao           → incrementa a cada edição de critério
criado_em / atualizado_em
```

**Campos de `AtividadeXpCriterio`:**
```
atividade_xp_id
campo_avaliado   → campo do evento (ex: "motivacao", "horas_sono", "progressao")
tipo_criterio    → BOOLEANO | NUMERICO_MIN | NUMERICO_RANGE | CONTAGEM | SEQUENCIA
operador         → IGUAL | MAIOR_QUE | MAIOR_IGUAL | MENOR_IGUAL | ENTRE
valor_esperado   → threshold (ex: "7", "baixa", "true")
valor_maximo     → usado quando operador = ENTRE
```

**Campos de `AtividadeXpRecompensa`:**
```
atividade_xp_id
versao           → vinculado à versão da atividade no momento
xp_base          → XP fixo concedido
xp_bonus         → XP adicional condicional
condicao_bonus   → regra para bônus (ex: sequencia_atual >= 7)
medalha_codigo   → medalha a conceder junto (opcional)
```

**Campos de `EventoAtividade` (append-only):**
```
usuario_id, tipo_gatilho
referencia_id / referencia_tipo → ID e tipo do objeto real
payload          → JSON com todos os campos avaliáveis do evento
criado_em
```

**Campos de `XpTransacao` (append-only, nunca editar):**
```
usuario_id, atividade_xp_id
atividade_versao → versão no momento da concessão
xp_concedido     → valor snapshot do momento (não referencia valor atual)
referencia_id / referencia_tipo
criado_em
UNIQUE (usuario_id, atividade_xp_id, referencia_id, referencia_tipo)
```

**Fluxo do Motor:**
```
1. Usuário realiza ação → EventoAtividade persistido com payload
2. Motor consulta AtividadeXp com status=ATIVO e tipo_gatilho correspondente
3. Filtra por data_inicio/data_fim se definidos
4. Para cada atividade: avalia AtividadeXpCriterio contra o payload
5. Se aprovado: verifica idempotência via UNIQUE constraint
6. Insere XpTransacao com xp_concedido snapshot do momento
7. Atualiza PerfilGamificacao (xp_total, nível, tier)
8. Avalia bônus e medalhas vinculadas
```

**Exemplos de atividades configuradas pelo admin (não hardcoded):**

| Atividade | Gatilho | Critério | XP base |
|---|---|---|---|
| Treino completo | FINALIZOU_TREINO | estado = CONCLUÍDO | 50 |
| Treino sem vontade | FINALIZOU_TREINO | estado = CONCLUÍDO + motivacao = baixa | 80 |
| Progressão real | FINALIZOU_TREINO | progressao = true | 100 |
| Proteína batida | REGISTROU_DIA | proteina_batida = true | 40 |
| Sono ok | REGISTROU_DIA | horas_sono entre 7 e 8 | 30 |
| Registro diário | REGISTROU_DIA | qualquer registro | 10 |
| 5 treinos na semana | FINALIZOU_TREINO | contagem_semanal >= 5 | 250 |
| Retorno após pausa | FINALIZOU_TREINO | dias_desde_ultimo_treino >= 7 | 250 |

**Regras do motor:**
- Admin cria, edita e desativa atividades sem deploy
- Edição de critérios cria nova versão — não reprocessa histórico
- Desativar atividade não apaga `XpTransacao` anteriores
- `XpTransacao` grava `xp_concedido` snapshot — não referencia valor atual
- Exercício personalizado do usuário conta normalmente para XP
- Atividades com status `DRAFT` nunca são avaliadas pelo motor
- Admin pode definir `data_inicio` / `data_fim` para atividades sazonais

---

### 🏅 Sistema de Medalhas — Dinâmico

**Campos de `MedalhaDefinicao`:**
```
codigo, nome, descricao, icone_url
tipo             → UNICA | REPETIVEL
categoria        → TEMPO | PESO | CARGA | CONSISTENCIA | SOCIAL | CUSTOM
status           → DRAFT | ATIVO | INATIVO  (soft delete)
criterio_tipo    → CONTAGEM | SEQUENCIA | META_VALOR | MANUAL
criterio_valor   → threshold numérico (ex: 90 para 90 dias)
criterio_campo   → campo avaliado (ex: "treinos_realizados", "sequencia_atual")
data_inicio / data_fim → vigência opcional (medalhas sazonais)
criado_em / atualizado_em
```

**Regras:**
- **UNICA:** verificar `MedalhaUsuario` antes de conceder — ignorar silenciosamente se já existe
- **REPETIVEL:** sempre incrementar contador, nunca bloquear
- Desativar `MedalhaDefinicao`: soft delete — `MedalhaUsuario` sempre preservado
- Editar critério: vale para avaliações futuras, não reprocessa passado
- Medalhas com status `DRAFT` não são avaliadas nem exibidas ao usuário
- `criterio_tipo=MANUAL`: admin concede diretamente a usuários específicos

---

### 📊 Sistema de Níveis
```
XP necessário para subir do nível N = 500 + (N × 250)
```
- XP é **acumulativo e nunca reseta**
- Subida de nível é **sequencial** — nunca pular nível em um único cálculo
- Ao subir de nível: conceder medalha "Subi de nível" automaticamente

### 🏆 Sistema de Tiers
| Tier | Faixa de nível |
|---|---|
| 🥉 Bronze | 1 – 5 |
| 🥈 Prata | 6 – 10 |
| 🥇 Ouro | 11 – 20 |
| 🏆 Lenda | 21+ |

- Tier atualiza **automaticamente** ao subir de nível

### 📅 Missões Semanais
- Janela oficial: **segunda 00:00 → domingo 23:59** (timezone do sistema)
- Reset: nova linha em `MissaoSemanalUsuario` com `semana_iso` novo
- Histórico total: incrementar `MissaoHistoricoTotal` ao concluir cada missão
- Não duplicar conclusão da mesma missão na mesma semana

### Regras de Sequência
- Dia útil sem treino **quebra** a sequência
- **Sábado e domingo não quebram** a sequência
- "Retorno após pausa" = treino realizado após **7+ dias** sem registro

### Índices obrigatórios
```sql
atividade_xp(status, tipo_gatilho, data_inicio, data_fim)
atividade_xp_criterio(atividade_xp_id)
evento_atividade(usuario_id, tipo_gatilho, criado_em)
xp_transacao(usuario_id, atividade_xp_id, referencia_id, referencia_tipo) UNIQUE
medalha_definicao(status, categoria)
medalha_usuario(usuario_id, medalha_id) UNIQUE
missao_semanal_usuario(usuario_id, missao_id, semana_iso) UNIQUE
perfil_gamificacao(usuario_id) UNIQUE
```

> ✅ Script SQL de `perfil_gamificacao_usuario` corrigido.

---

## 🌐 M4 — Módulo de Rede Social

### Funcionalidades
- Seguir/desseguir usuários
- Perfil público/privado configurável
- Feed de postagens (texto, treino concluído, conquista, foto)
- Curtidas e comentários
- Compartilhamento de medalhas e conquistas (opt-in)
- Grupos com papéis (Admin / Membro)
- Ranking entre amigos e por grupo
- Bloqueio e silenciamento de usuários
- Moderação básica com denúncia

### Entidades principais
| Entidade | Descrição |
|---|---|
| `PerfilPublico` | Bio, foto, visibilidade (PUBLICO / PRIVADO / AMIGOS) |
| `Seguidor` | Relação seguidor/seguido |
| `Postagem` | Conteúdo (tipo: TEXTO / TREINO / CONQUISTA / FOTO) |
| `Curtida` | Curtida em postagem |
| `Comentario` | Comentário em postagem |
| `Grupo` | Grupo de usuários |
| `GrupoMembro` | Membro com papel no grupo |
| `Denuncia` | Registro de denúncia de conteúdo |
| `Bloqueio` | Bloqueio entre usuários |

### Regras de negócio
- Peso, fotos de progresso e evolução física têm **visibilidade própria** (opt-in — nunca automático)
- Compartilhar treino/conquista é sempre **escolha do usuário** ao finalizar
- Perfil privado: só seguidores aprovados visualizam conteúdo
- Bloqueio impede visualização mútua em feed, perfil e comentários
- Ranking entre amigos: filtrar por lista de seguidos, usando `xp_total` de `PerfilGamificacao`
- Feed: paginação **cursor-based** obrigatória (nunca offset em escala)
- Postagens de usuários bloqueados filtradas antes de retornar
- Ação social **nunca** é fonte primária de XP
- ✅ Regras de visibilidade implementadas
- ✅ Validações de acesso implementadas
- ✅ Proteção de vínculos indevidos em postagem/comentário/curtida implementada

### Índices obrigatórios
```sql
postagem(autor_id, criado_em DESC)
postagem(grupo_id, criado_em DESC)
seguidor(seguidor_id, ativo)
seguidor(seguido_id, ativo)
bloqueio(bloqueador_id, bloqueado_id) UNIQUE
curtida(usuario_id, postagem_id) UNIQUE
```

---

## ⚙️ M5 — Infra, Auth e Configurações

### Autenticação
- JWT: access token (15min) + refresh token (7 dias)
- Login local: email + senha com BCrypt
- OAuth2 Google com vinculação por email verificado
- Vinculação de múltiplos provedores à mesma conta
- Revogação de refresh token no logout
- Rate limiting em todos os endpoints de autenticação

### Onboarding
- Coleta obrigatória: objetivo, peso inicial, metas nutricionais e nível de experiência
- Base para configuração inicial de `MetaAtleta` e `PerfilGamificacao`

### Notificações
- Eventos de domínio via `@ApplicationEventPublisher` do Spring
- Exemplos: `NivelSubidoEvent`, `MedalhaConquistadaEvent`, `MissaoConcluida`
- Tabela `notificacao_pendente` como fallback para push notifications

---

## 🔒 Segurança

- Nunca logar dados sensíveis: senha, token, peso, dados pessoais
- Validar **propriedade do recurso** no Service (usuário só acessa o próprio dado)
- `@PreAuthorize` ou verificação explícita de ownership em todo endpoint sensível
- CORS configurado para origens permitidas
- Conformidade com **LGPD** (mercado brasileiro)
- Criptografia de senha com BCrypt
- Proteção de tokens JWT (expiração curta + refresh)
- Rate limiting em endpoints críticos

---

## ✅ Boas Práticas de Desenvolvimento

### Código
- Padrão de nomenclatura: `camelCase` variáveis/métodos · `PascalCase` classes · `UPPER_SNAKE_CASE` constantes
- `@Transactional` explícito em toda operação com múltiplas escritas
- `Optional` → sempre `.orElseThrow(CustomException)`, nunca `.get()` sem verificação
- Evitar N+1: usar `JOIN FETCH` ou `@EntityGraph` em queries com relacionamentos
- `LAZY` loading por padrão em todos os relacionamentos JPA
- `BigDecimal` para todos os cálculos de macros/calorias/nutrição
- Paginação obrigatória em listagens (`Pageable`)
- Validação de entrada no **Service**, nunca depender só do Controller

### Logs
```java
log.info("...");   // fluxo normal
log.warn("...");   // regra de negócio desviada
log.error("...");  // exceção real
// NUNCA: System.out.println(...)
```

### Exceções customizadas
| Exceção | Uso |
|---|---|
| `ResourceNotFoundException` | Recurso não encontrado |
| `BusinessRuleException` | Violação de regra de negócio |
| `DuplicateEntryException` | Tentativa de duplicidade |
| `UnauthorizedAccessException` | Acesso indevido a recurso de outro usuário |

- ✅ `@ControllerAdvice` global implementado com logs
- Nunca expor stack trace ao cliente

### Banco de dados
- Flyway para todas as migrações (nunca `ddl-auto=update` em staging/produção)
- Migrations versionadas e reversíveis quando possível
- Índices criados junto com a entidade, não depois

### Performance
- Queries paginadas para listagens
- `JOIN FETCH` / `@EntityGraph` para evitar N+1
- Aggregations diárias/semanais sem queries pesadas (considerar views materializadas para analytics)
- `XpTransacao` como append-only: nunca deletar, nunca editar

---

## 🧪 Testes

### Estratégia
- **Unitários:** camada Service com JUnit 5 + Mockito
- **Integração:** camada Controller com `@SpringBootTest` ou `@WebMvcTest`

### Cenários obrigatórios
- Idempotência de XP (mesmo evento não concede XP duas vezes)
- Subida de nível sequencial (nunca pular nível)
- Reset semanal de missões (semana_iso correto)
- Sequência de treino (fim de semana não quebra, dia útil sem treino quebra)
- Medalha única não concedida duas vezes
- Treino planejado não confundido com registro de execução
- Plano alimentar não confundido com registro diário

---

## 📊 Status de Implementação

### ✅ Já implementado
- Módulo alimentação: validação no Service e totais nutricionais por refeição
- Módulo social: regras de visibilidade, validações de acesso e proteção de vínculos indevidos em postagem/comentário/curtida
- Tratamento global de exceções com logs (`@ControllerAdvice`)
- Correção SQL: `perfil_gamificacao_usuario`
- Backlog priorizado gerado: `.md`, `.csv`, `.docx`

### 🔴 P0 — Próximo lote (implementar primeiro)
- [ ] Autenticação completa: local (email + senha + JWT) + OAuth2 Google
- [ ] Onboarding do usuário
- [ ] Migrações formais com Flyway

### 🟡 P1 — Após P0 fechado
- [ ] Biblioteca de exercícios (CRUD, busca por grupo muscular/equipamento)
- [ ] Treino: plano vs execução real de ponta a ponta
- [ ] Plano alimentar semanal: planejado vs `RegistroDiario` executado

### 🟠 P2
- [ ] Motor de notificações (eventos de domínio + tabela pendente)
- [ ] Analytics pessoais: consistência, frequência, adesão por período

### ⚪ P3
- [ ] Google Fit / Apple Health (integrações externas)
- [ ] Modo offline no Flutter
- [ ] Expansões de backlog restantes

---

## ⚙️ Painel Admin — Gestão por Tela

O admin gerencia todo o ecossistema dinâmico por tela, sem deploy.

### Capacidades

**Atividades de XP:**
- Criar, editar e desativar atividades de XP
- Definir critérios parametrizáveis por atividade
- Configurar vigência para atividades sazonais
- Publicar atividade (DRAFT → ATIVO) após validar
- Ver quantos usuários foram recompensados por cada atividade
- Visualizar histórico de versões de cada atividade

**Medalhas:**
- Criar, editar e desativar medalhas com ícone configurável
- Conceder medalha manualmente a usuário específico (tipo MANUAL)
- Ver quantos usuários têm cada medalha
- Criar medalhas sazonais com vigência

**Exercícios:**
- Criar e editar exercícios globais com mídia e metadados
- Ativar / desativar exercício global (preserva histórico)
- Revisar fila de exercícios personalizados pendentes
- Aprovar sugestão → promove para global
- Rejeitar sugestão com motivo (usuário é notificado)
- Mesclar exercício sugerido com global existente (evitar duplicatas)

**Usuários:**
- Ver perfil de gamificação de qualquer usuário
- Ajuste manual de XP com justificativa obrigatória (auditado)
- Conceder ou revogar medalha com justificativa
- Ver histórico completo de `XpTransacao` de um usuário

### Segurança do painel admin
- Role separada: `ROLE_ADMIN` — nunca misturar com `ROLE_USER`
- Toda ação administrativa gera log de auditoria (`admin_id`, `acao`, `alvo`, `criado_em`)
- Admin não pode excluir fisicamente `XpTransacao` ou `MedalhaUsuario`
- Ajuste manual de XP requer campo de justificativa obrigatório

---

## 🔄 Regras do Ecossistema Dinâmico

### Imutabilidade do histórico
- `XpTransacao`: append-only — nunca editar, nunca excluir
- `MedalhaUsuario`: nunca remover sem justificativa auditada de admin
- `EventoAtividade`: append-only, imutável após criação

### Soft delete universal
- `AtividadeXp`, `MedalhaDefinicao` e `Exercicio`: nunca excluir fisicamente
- Usar `status` (`ATIVO | INATIVO | DRAFT | PENDENTE_REVISAO`)
- Registros históricos sempre referenciam o ID original preservado

### Versionamento
- Edição de critérios de `AtividadeXp` cria nova versão — não reprocessa histórico
- `XpTransacao` grava a versão e o valor no momento da concessão
- Edições valem apenas para eventos futuros

### Atividades sazonais
- Admin define `data_inicio` e `data_fim` opcionais
- Motor ignora atividades fora da janela de vigência automaticamente
- Histórico de XP concedido durante a vigência é preservado

---

## 📐 Regras Absolutas do Sistema

1. **Nunca inventar ou gerar dados fictícios**
2. **Atualizar estado apenas via input real do usuário**
3. **Treino planejado ≠ RegistroTreino** — entidades sempre separadas
4. **Plano alimentar ≠ RegistroDiario** — entidades sempre separadas
5. **XP só por evento real persistido e auditável**
6. **Idempotência obrigatória** em todo evento de gamificação
7. **Medalhas únicas:** verificar antes de conceder — nunca duplicar
8. **Medalhas repetíveis:** sempre incrementar, nunca bloquear
9. **Falha não zera progresso** — fim de semana não quebra sequência
10. **Dados sensíveis têm privacidade granular** — opt-in para compartilhar
11. **Nada é hardcoded na gamificação** — atividades, XP, medalhas e critérios são configurados pelo admin por tela
12. **Soft delete universal** — `AtividadeXp`, `MedalhaDefinicao` e `Exercicio` nunca são excluídos fisicamente
13. **XpTransacao é imutável** — grava snapshot do valor no momento; edições futuras não afetam o passado
14. **Exercício personalizado conta normalmente** para XP e progressão, independente da origem

---

*Atualizar a seção "Status de Implementação" a cada lote concluído. Mover itens concluídos para "✅ Já implementado" e marcar com ✅ nos módulos correspondentes.*
