package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoTreino;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TreinoRequest(
        @NotBlank(message = "Nome do treino e obrigatorio.")
        String nome,
        String descricao,
        @NotNull(message = "Tipo do treino e obrigatorio.")
        TipoTreino tipoTreino,
        @NotNull(message = "Usuario e obrigatorio.")
        Long usuarioId,
        @NotNull(message = "Data do treino e obrigatoria.")
        LocalDateTime dataTreino
) {
}
