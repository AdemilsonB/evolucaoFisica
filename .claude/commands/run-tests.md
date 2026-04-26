# Rodar Testes — EvolucaoFisica

Executa a suíte de testes e analisa os resultados.

## Uso

```
/project:run-tests [modulo]
```

Exemplos:
- `/project:run-tests` — roda todos os testes
- `/project:run-tests treino` — roda apenas testes do módulo treino

## O que este comando faz

### 1. Executar os testes

Se nenhum módulo informado:
```bash
./mvnw test -q
```

Se módulo informado (ex: treino):
```bash
./mvnw test -q -Dtest="*Treino*,*Registro*,*Exercicio*"
```

### 2. Verificar cobertura (se jacoco configurado)

```bash
./mvnw verify -q
```

### 3. Analisar falhas

Para cada teste falhando:
- Mostrar nome do teste e mensagem de erro
- Identificar a causa raiz (mock incorreto, lógica errada, dado de teste inválido)
- Propor correção

### 4. Gerar relatório

```
## Resultado dos Testes — <data>

### Resumo
- Total: X testes
- Passou: X ✅
- Falhou: X ❌
- Ignorado: X ⏭️

### Falhas
<Para cada falha:>
**<NomeDoTeste>**
Motivo: <mensagem de erro simplificada>
Causa provável: <análise>
Sugestão: <correção>

### Cenários obrigatórios ausentes
<lista de cenários do rules/testing-rules.md que ainda não existem>
```

### 5. Sugerir testes faltantes

Verificar `.claude/rules/testing-rules.md` e listar cenários obrigatórios que ainda não foram implementados para o módulo analisado.
