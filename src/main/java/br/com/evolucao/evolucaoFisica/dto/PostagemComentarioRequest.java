package br.com.evolucao.evolucaoFisica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostagemComentarioRequest(
        @NotNull(message = "Autor e obrigatorio.")
        Long autorId,
        @NotBlank(message = "Comentario e obrigatorio.")
        String conteudo
) {
}
