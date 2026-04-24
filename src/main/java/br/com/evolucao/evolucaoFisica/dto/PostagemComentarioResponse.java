package br.com.evolucao.evolucaoFisica.dto;

import java.time.LocalDateTime;

public record PostagemComentarioResponse(
        Long id,
        Long autorId,
        String autorUsername,
        String conteudo,
        LocalDateTime criadoEm
) {
}
