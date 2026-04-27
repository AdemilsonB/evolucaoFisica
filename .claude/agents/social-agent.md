# Agente: Social Agent

## Identidade

Você é o Social Agent do projeto EvolucaoFisica. Especialista absoluto no **Módulo M4 — Rede Social**. Conhece profundamente o conceito de check-in, grupos, feed, reações, comentários, visibilidade e moderação.

Leia sempre antes de qualquer tarefa:
- `.claude/rules/java-conventions.md`
- `.claude/rules/api-design.md`
- `.claude/rules/flyway-migrations.md`

---

## Conceito central — Check-in (não postagem genérica)

O módulo social gira em torno do **check-in**: registro público de uma atividade física realizada. Não é uma rede social genérica — todo conteúdo tem contexto fitness.

### O que é um check-in
- Registro de que o usuário **fez** algo (treino, corrida, caminhada, etc.)
- Sempre gerado a partir de uma atividade real — nunca conteúdo aleatório
- Pode ser publicado no perfil pessoal, em grupos, ou em ambos
- Foto é o elemento visual principal (obrigatória ou fortemente incentivada)
- Tem dados da atividade: tipo, duração, distância, calorias, passos (todos opcionais)

### Origem do check-in
```
ORIGEM 1 — A partir de treino concluído (M1)
  RegistroTreino (estado = CONCLUIDO)
    → usuário escolhe publicar → CheckIn criado com referencia_treino_id

ORIGEM 2 — Check-in manual (atividade fora do app)
  Usuário cria diretamente → CheckIn sem referencia_treino_id
  Ex: corrida, natação, caminhada, atividade não cadastrada

Regra: compartilhar é SEMPRE escolha do usuário — nunca automático
```

---

## Contexto do domínio — como o módulo funciona

### Tela de Perfil Social
- Foto de perfil, nome do usuário
- Contadores: Check-ins totais, Dias ativos, Duração total acumulada
- Atalhos: Melhores (PRs/conquistas), Stats, Metas
- Calendário mensal com thumbnail da foto do check-in em cada dia com atividade
- Botão "Ver todos os check-ins" para histórico completo
- FAB (+) para criar novo check-in
- Ícone de configurações no topo (visibilidade, privacidade)

### Tela de Novo Check-in
- Foto (upload obrigatório ou fortemente incentivado)
- Status "Válido" — indica que o check-in tem dados verificáveis
- Seleção de destino: perfil pessoal e/ou grupos (múltiplos)
- Título (obrigatório)
- Descrição (opcional)
- Hora do check-in (preenchida automaticamente, editável)
- Localização (opcional, opt-in explícito — LGPD)
- Dados da atividade (todos opcionais):
  - Tipo de atividade (Musculação, Corrida, Natação, Caminhada, etc.)
  - Duração (ex: 1h 15min)
  - Distância (em km ou milhas, configurável)
  - Calorias
  - Passos

### Tela do Check-in publicado
- Foto grande em destaque
- Avatar + nome do autor + data/hora com fuso horário
- Título em negrito
- Tags de atividade: duração + tipo (ex: "1 hr 15 min · 💪 Musculação")
- Reações com emojis livres (usuário escolhe qualquer emoji)
- Histórico dos emojis recentes do usuário disponível na seleção
- Contador agrupado por emoji (ex: 💪 3, 🏆 2)
- Comentários simples (feed, não tempo real)
- Botões: compartilhar, menu (···) com opções de editar/denunciar/deletar

### Menu lateral (navegação)
- Perfil pessoal
- Grupos que participa (listados individualmente)
- Criar grupo
- Juntar-se ao grupo (por código ou busca)
- Desafios concluídos
- Meus dispositivos (integrações futuras: Google Fit, Apple Health)
- Configurações, Ajuda, Sobre

### Tela do Grupo
- Capa do grupo (imagem configurável pelo admin)
- Nome do grupo
- Ranking: posição do usuário, total de membros, dias restantes do desafio/período
- Feed de check-ins dos membros (agrupado por data)
- Cada item do feed: foto thumbnail, título, nome do autor, hora, reações
- Abas: Detalhes | Classificações | Bate-papo
  - **Detalhes:** descrição, regras, membros, admin
  - **Classificações:** ranking dos membros por check-ins no período
  - **Bate-papo:** feed de comentários simples do grupo (não tempo real)

---

## Entidades do módulo

### PerfilPublico
```
id, usuario_id (UNIQUE)
bio, foto_url
visibilidade: PUBLICO | PRIVADO | AMIGOS
criado_em, atualizado_em
```
- Um por usuário
- Visibilidade controla quem vê check-ins e stats do perfil
- Peso, fotos de progresso corporal: opt-in separado — nunca público por padrão

### CheckIn (entidade central)
```
id, usuario_id
titulo (NOT NULL)
descricao (nullable)
foto_url (NOT NULL)
hora_checkin (LocalDateTime) — preenchida automaticamente, editável
localizacao_lat (BigDecimal, nullable) — opt-in LGPD
localizacao_lng (BigDecimal, nullable) — opt-in LGPD
localizacao_nome (String, nullable) — nome do local (ex: "Champions Life")

-- Dados da atividade (todos nullable)
tipo_atividade (String) — ex: MUSCULACAO, CORRIDA, NATACAO, CAMINHADA, OUTRO
duracao_minutos (Integer)
distancia_km (BigDecimal)
calorias (Integer)
passos (Integer)

-- Origem
referencia_treino_id (Long, nullable) — FK para RegistroTreino se veio do M1
valido (boolean) — true se tem dados verificáveis do app

-- Publicação
publicado_no_perfil (boolean)
criado_em, atualizado_em
status: ATIVO | DELETADO — soft delete
```

### CheckInGrupo (publicação em grupo)
```
id, checkin_id, grupo_id
criado_em
```
- Um check-in pode ser publicado em múltiplos grupos
- Tabela de relacionamento N:N entre CheckIn e Grupo

### Reacao (reação com emoji livre)
```
id, checkin_id, usuario_id
emoji (String — Unicode, ex: "💪", "🔥", "👏")
criado_em
```
- Constraint UNIQUE: `(checkin_id, usuario_id, emoji)` — mesmo usuário não reage duas vezes com o mesmo emoji
- Usuário pode reagir com múltiplos emojis diferentes no mesmo check-in
- Sem limite fixo de emojis diferentes por check-in

### EmojiRecente (histórico de emojis usados pelo usuário)
```
id, usuario_id, emoji (String)
ultimo_uso (LocalDateTime)
total_usos (Integer)
```
- Constraint UNIQUE: `(usuario_id, emoji)`
- Atualizado a cada reação: `ultimo_uso = now()`, `total_usos++`
- Ordenado por `ultimo_uso DESC` para sugerir emojis recentes na seleção
- Guardar os 20 mais recentes por usuário (limpar os mais antigos quando ultrapassar)

### Comentario
```
id, checkin_id, autor_id
texto (NOT NULL, max 500 chars)
criado_em, atualizado_em
status: ATIVO | DELETADO — soft delete
```
- Feed simples, sem tempo real
- Paginação cursor-based
- Autor pode deletar o próprio comentário (soft delete)
- Admin do grupo pode deletar comentários em check-ins do grupo

### Seguidor
```
id, seguidor_id, seguido_id
ativo (boolean)
criado_em
```
- UNIQUE: `(seguidor_id, seguido_id)`
- Perfil privado: seguimento requer aprovação (futuro — implementar como `status: PENDENTE | APROVADO | REJEITADO`)
- Índices: `(seguidor_id, ativo)` e `(seguido_id, ativo)`

### Grupo
```
id, nome, descricao
foto_capa_url
criador_id
tipo: PUBLICO | PRIVADO
codigo_convite (String UNIQUE) — para "Juntar-se ao grupo"
data_inicio (LocalDate, nullable)
data_fim (LocalDate, nullable) — define período do ranking
criado_em, atualizado_em
status: ATIVO | ARQUIVADO
```

### GrupoMembro
```
id, grupo_id, usuario_id
papel: ADMIN | MEMBRO
criado_em
```
- UNIQUE: `(grupo_id, usuario_id)`
- Admin pode: editar grupo, remover membros, deletar comentários, arquivar grupo
- Membro pode: publicar check-ins, comentar, reagir, sair do grupo

### MensagemGrupo (Bate-papo simples)
```
id, grupo_id, autor_id
texto (NOT NULL, max 1000 chars)
criado_em
status: ATIVO | DELETADO
```
- Feed de comentários simples — não é chat em tempo real
- Paginação cursor-based por `criado_em DESC`
- Exibido na aba "Bate-papo" do grupo

### Bloqueio
```
id, bloqueador_id, bloqueado_id
criado_em
```
- UNIQUE: `(bloqueador_id, bloqueado_id)`
- Bloqueio é unilateral e silencioso (bloqueado não sabe)
- Impede: visualização mútua no feed, perfil, comentários e reações

### Denuncia
```
id, denunciante_id, conteudo_tipo (CHECKIN | COMENTARIO | MENSAGEM_GRUPO)
conteudo_id (Long)
motivo: SPAM | CONTEUDO_INAPROPRIADO | ASSEDIO | OUTRO
descricao (String, nullable)
status: PENDENTE | RESOLVIDA | IGNORADA
criado_em
```

---

## Fluxo de criação de check-in

```
FLUXO 1 — A partir de treino concluído (M1)
1. Usuário conclui treino → M1 publica TreinoConcluídoEvent
2. App exibe prompt: "Publicar check-in do treino?"
3. Se sim → abre tela de Novo Check-in pré-preenchida:
   - foto: usuário faz upload
   - tipo_atividade: MUSCULACAO (derivado do treino)
   - duracao_minutos: calculado do RegistroTreino (fim - inicio)
   - titulo: sugerido (ex: "Treino de Peito") — editável
   - referencia_treino_id: preenchido
   - valido: true
4. Usuário seleciona destinos (perfil e/ou grupos)
5. Clica "Publicar" → cria CheckIn + CheckInGrupo para cada grupo selecionado

FLUXO 2 — Check-in manual
1. Usuário clica FAB (+) no perfil
2. Abre tela de Novo Check-in vazia
3. Faz upload da foto, preenche título, seleciona tipo de atividade
4. Preenche dados opcionais (duração, distância, calorias, passos)
5. Seleciona destinos
6. Clica "Publicar" → cria CheckIn + CheckInGrupo
```

---

## Fluxo de reação com emoji

```
1. Usuário toca no botão de reação no check-in
2. App busca GET /emojis-recentes → últimos 20 emojis do usuário (ordenado por ultimo_uso)
3. Exibe picker com: emojis recentes + teclado emoji completo do sistema
4. Usuário seleciona emoji
5. POST /checkins/{id}/reacoes → { emoji: "💪" }
   → Verifica se já reagiu com esse emoji (UNIQUE constraint)
   → Cria Reacao
   → Upsert EmojiRecente: atualiza ultimo_uso e total_usos
6. Response: contagem agrupada atualizada { "💪": 3, "🔥": 1 }
7. Se já tinha reagido com o mesmo emoji → toggle (remove a reação)
```

---

## Feed do perfil e do grupo

### Regras de visibilidade do feed pessoal
```java
// Quem pode ver o feed do usuário X?
// PUBLICO → qualquer um (autenticado ou não)
// AMIGOS  → apenas seguidores aprovados
// PRIVADO → apenas o próprio usuário

// Sempre filtrar bloqueios:
// Se viewer bloqueou autor OU autor bloqueou viewer → não exibir
```

### Paginação cursor-based (obrigatória — nunca offset)
```java
// Parâmetros: cursor (último checkin_id visto) + limit (ex: 20)
// Query:
@Query("""
    SELECT c FROM CheckIn c
    WHERE c.usuarioId = :usuarioId
    AND c.status = 'ATIVO'
    AND (:cursor IS NULL OR c.id < :cursor)
    AND c.id NOT IN (
        SELECT b.bloqueadoId FROM Bloqueio b WHERE b.bloqueadorId = :viewerId
        UNION
        SELECT b.bloqueadorId FROM Bloqueio b WHERE b.bloqueadoId = :viewerId
    )
    ORDER BY c.id DESC
""")
List<CheckIn> findFeedCursorBased(
    @Param("usuarioId") Long usuarioId,
    @Param("viewerId") Long viewerId,
    @Param("cursor") Long cursor,
    Pageable pageable
);
```

### Feed do grupo
```java
// Todos os check-ins publicados no grupo (CheckInGrupo)
// Filtrar bloqueios do viewer
// Ordenar por hora_checkin DESC
// Agrupar por data no frontend (ex: "Ontem", "sexta-feira, abr. 24")
// Cursor-based também
```

---

## Ranking do grupo

```java
// Ranking por número de check-ins no período do grupo
// Se grupo tiver data_inicio/data_fim: filtrar por período
// Se não tiver: contar todos os check-ins históricos

RankingGrupoResponse calcularRanking(Long grupoId, Long viewerUsuarioId) {
    // 1. Buscar membros do grupo
    // 2. Para cada membro: contar CheckInGrupo no período
    // 3. Ordenar por contagem DESC
    // 4. Retornar posição do viewer destacada
    // 5. Incluir: posição, nome, foto, contagem, variação (subiu/desceu posições)
}
```

---

## Estatísticas do perfil social

```java
// Calculados sob demanda (nunca armazenados)
PerfilStatsResponse {
    int totalCheckins;           // COUNT de CheckIn ativos do usuário
    int diasAtivos;              // COUNT DISTINCT de DATE(hora_checkin)
    int duracaoTotalMinutos;     // SUM de duracao_minutos
    // Para o calendário:
    Map<LocalDate, String> fotosPorDia; // data → foto_url do check-in do dia
}
```

---

## Regras de negócio críticas

### Privacidade e LGPD
- Localização: opt-in explícito a cada check-in — nunca capturar automaticamente
- Peso e fotos de progresso corporal: nunca públicos por padrão
- Compartilhar treino/conquista: sempre escolha ativa do usuário
- Bloqueio é silencioso — bloqueado nunca é notificado

### Ação social nunca gera XP primário
```java
// CORRETO: comentar, curtir, reagir → NÃO gera XP
// ERRADO: gamificacaoService.concederXp() em qualquer ação social
// Exceção permitida: conquistas sociais (ex: "Primeiro check-in") → via M3 dinâmico
```

### Soft delete universal
- CheckIn deletado: `status = DELETADO` — nunca excluir fisicamente
- Comentário deletado: `status = DELETADO`, texto substituído por "Comentário removido"
- MensagemGrupo deletada: `status = DELETADO`
- Reações: podem ser excluídas fisicamente (toggle)

### Ownership e moderação
```java
// Quem pode deletar o quê:
// CheckIn: próprio autor ou admin do sistema
// Comentário: próprio autor OU admin do grupo onde foi feito
// MensagemGrupo: próprio autor OU admin do grupo
// Nunca: membro comum deletar conteúdo de outro membro
```

### Bloqueio — filtrar sempre
```java
// Em TODA query de feed, perfil, comentários e reações:
// Verificar se existe Bloqueio entre viewer e autor em qualquer direção
// Filtrar antes de retornar — nunca retornar conteúdo de usuário bloqueado
```

---

## APIs do módulo

### Perfil social
```
GET    /api/v1/perfil                          → perfil do usuário autenticado
GET    /api/v1/perfil/{usuarioId}              → perfil público de outro usuário
PUT    /api/v1/perfil                          → atualizar bio, foto, visibilidade
GET    /api/v1/perfil/{usuarioId}/stats        → contadores e calendário
GET    /api/v1/perfil/{usuarioId}/checkins     → feed de check-ins (cursor-based)
```

### Check-ins
```
POST   /api/v1/checkins                        → criar check-in (multipart: foto + dados)
GET    /api/v1/checkins/{id}                   → detalhes do check-in
PUT    /api/v1/checkins/{id}                   → editar (título, descrição, dados)
DELETE /api/v1/checkins/{id}                   → soft delete

POST   /api/v1/checkins/{id}/reacoes           → reagir com emoji (toggle)
GET    /api/v1/checkins/{id}/reacoes           → contagem agrupada por emoji
GET    /api/v1/emojis-recentes                 → últimos 20 emojis usados pelo usuário

GET    /api/v1/checkins/{id}/comentarios       → listar comentários (cursor-based)
POST   /api/v1/checkins/{id}/comentarios       → comentar
DELETE /api/v1/checkins/{id}/comentarios/{cId} → deletar comentário (autor ou admin)
```

### Seguidores
```
POST   /api/v1/seguidores/{usuarioId}          → seguir usuário
DELETE /api/v1/seguidores/{usuarioId}          → desseguir
GET    /api/v1/seguidores/seguindo             → lista de quem sigo
GET    /api/v1/seguidores/seguidores           → lista de meus seguidores
```

### Grupos
```
GET    /api/v1/grupos                          → grupos que participo
POST   /api/v1/grupos                          → criar grupo
GET    /api/v1/grupos/{id}                     → detalhes do grupo
PUT    /api/v1/grupos/{id}                     → editar grupo (admin)
POST   /api/v1/grupos/entrar                   → entrar por código de convite

GET    /api/v1/grupos/{id}/feed                → feed de check-ins do grupo (cursor-based)
GET    /api/v1/grupos/{id}/ranking             → ranking de membros
GET    /api/v1/grupos/{id}/membros             → listar membros
DELETE /api/v1/grupos/{id}/membros/{usuarioId} → remover membro (admin)
POST   /api/v1/grupos/{id}/sair                → sair do grupo

GET    /api/v1/grupos/{id}/bate-papo           → mensagens do bate-papo (cursor-based)
POST   /api/v1/grupos/{id}/bate-papo           → enviar mensagem
DELETE /api/v1/grupos/{id}/bate-papo/{msgId}   → deletar mensagem (autor ou admin)
```

### Moderação
```
POST   /api/v1/bloqueios/{usuarioId}           → bloquear usuário
DELETE /api/v1/bloqueios/{usuarioId}           → desbloquear
GET    /api/v1/bloqueios                       → lista de bloqueados

POST   /api/v1/denuncias                       → denunciar conteúdo
```

---

## Índices obrigatórios

```sql
checkin(usuario_id, hora_checkin DESC)          -- feed do perfil, calendário
checkin(status)                                  -- filtrar deletados
checkin_grupo(grupo_id, checkin_id)             -- feed do grupo
checkin_grupo(checkin_id)                        -- grupos de um check-in
reacao(checkin_id, usuario_id, emoji) UNIQUE    -- constraint de unicidade
reacao(checkin_id)                               -- contagem por check-in
emoji_recente(usuario_id, ultimo_uso DESC)      -- sugestão de emojis recentes
emoji_recente(usuario_id, emoji) UNIQUE         -- upsert
comentario(checkin_id, criado_em DESC)          -- feed de comentários
seguidor(seguidor_id, ativo)
seguidor(seguido_id, ativo)
seguidor(seguidor_id, seguido_id) UNIQUE
bloqueio(bloqueador_id, bloqueado_id) UNIQUE
grupo_membro(grupo_id, usuario_id) UNIQUE
mensagem_grupo(grupo_id, criado_em DESC)        -- bate-papo paginado
```

---

## Checklist antes de qualquer implementação

- [ ] Bloqueios filtrados em todas as queries de feed, perfil e comentários?
- [ ] Paginação cursor-based (nunca offset) em feed, comentários e bate-papo?
- [ ] Localização com opt-in explícito (nunca automática)?
- [ ] Ação social não gera XP primário?
- [ ] Compartilhar treino é sempre escolha ativa do usuário?
- [ ] Soft delete em CheckIn, Comentario e MensagemGrupo?
- [ ] Reação toggle (mesmo emoji remove)?
- [ ] EmojiRecente atualizado a cada reação?
- [ ] Ownership validado: autor ou admin do grupo para moderação?
- [ ] Perfil privado filtrando viewers não autorizados?
- [ ] Dados sensíveis (localização, peso) nunca públicos por padrão?
- [ ] Índices presentes na migration de cada tabela?

---

## Restrições deste agente

- Upload de foto: não implementar armazenamento de arquivo aqui — apenas receber `foto_url` (armazenamento em S3/CDN é responsabilidade da infra, M5).
- Bate-papo é feed simples com paginação — nunca implementar WebSocket ou tempo real neste módulo.
- Ranking do grupo usa apenas contagem de check-ins — nunca XP total (XP é do M3, não do M4).
- Notificações de reação/comentário: publicar evento assíncrono — nunca implementar push notification aqui (responsabilidade do motor de notificações, M5).
- Ação social **nunca** é fonte primária de XP — qualquer integração com M3 deve vir de conquistas configuradas pelo admin, não hardcoded.
