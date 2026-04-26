# Regras de Testes — EvolucaoFisica

Leia este arquivo antes de criar ou revisar qualquer teste do projeto.

## Estratégia

| Tipo | Camada | Ferramentas |
|---|---|---|
| Unitário | Service | JUnit 5 + Mockito |
| Integração | Controller | `@SpringBootTest` ou `@WebMvcTest` |

## Estrutura padrão de teste unitário (Service)

```java
@ExtendWith(MockitoExtension.class)
class TreinoServiceTest {

    @Mock
    private TreinoRepository treinoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private TreinoService treinoService;

    @Test
    @DisplayName("Deve concluir treino e conceder XP corretamente")
    void deveConcluirTreinoEConcederXp() {
        // Arrange
        // Act
        // Assert
    }
}
```

## Cenários obrigatórios por módulo

### Gamificação (M3)
- [ ] Idempotência de XP: mesmo evento não concede XP duas vezes
- [ ] Subida de nível sequencial: nunca pular nível
- [ ] Reset semanal de missões: `semana_iso` calculado corretamente
- [ ] Sequência de treino: fim de semana não quebra, dia útil sem treino quebra
- [ ] Medalha única não concedida duas vezes
- [ ] Medalha repetível sempre incrementa, nunca bloqueia

### Treinos (M1)
- [ ] Treino planejado (`Treino`) nunca confundido com registro de execução (`RegistroTreino`)
- [ ] Idempotência: chave única `(usuario_id, treino_id, data_registro)` — não permitir duplicata no mesmo dia
- [ ] XP de treino só concedido em estado `CONCLUÍDO`, nunca em `ABORTADO`
- [ ] XP de progressão (+100) só se houve aumento real de carga ou reps vs última execução

### Alimentação (M2)
- [ ] Plano alimentar (`PlanoAlimentar`) nunca confundido com execução (`RegistroDiario`)
- [ ] Apenas um plano ativo por usuário: trocar desativa o anterior
- [ ] `BigDecimal` em todos os cálculos — nunca `double`
- [ ] "Proteína batida" calculada corretamente sobre `RegistroRefeicaoItem` reais

### Segurança / Ownership
- [ ] Usuário não consegue acessar recurso de outro usuário (lança `UnauthorizedAccessException`)
- [ ] Endpoint sensível sem autenticação retorna 401
- [ ] Endpoint com role errada retorna 403

## Boas práticas

- Nome do teste: `deve<Comportamento>Quando<Condicao>` ou `@DisplayName` descritivo em português.
- Sempre usar padrão **Arrange / Act / Assert** com comentários.
- Mockar apenas o necessário — se mockar demais, o teste não testa nada.
- Nunca usar `Thread.sleep()` em testes — usar `@Sql`, fixtures ou `Clock` injetável para datas.
- Datas e tempo: injetar `Clock` no Service para tornar testes determinísticos.
- Não usar banco real nos testes unitários — Mockito apenas.
- Para testes de integração: usar banco em memória (H2) ou Testcontainers com PostgreSQL.

## Cobertura mínima esperada

- Services críticos (gamificação, autenticação): 80%+
- Services de CRUD simples: 60%+
- Controllers: pelo menos o caminho feliz + erro 400/401/403/404
