package br.com.evolucao.evolucaoFisica.dto;

import java.time.LocalDateTime;

public record AuthResponse(
        String accessToken,
        String tokenType,
        LocalDateTime expiraEm,
        boolean onboardingPendente,
        UsuarioResponse usuario
) {
}
