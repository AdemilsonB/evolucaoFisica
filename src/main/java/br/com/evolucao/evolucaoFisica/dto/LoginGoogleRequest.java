package br.com.evolucao.evolucaoFisica.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginGoogleRequest(
        @NotBlank(message = "O id token do Google e obrigatorio.")
        String idToken,
        String username
) {
}
