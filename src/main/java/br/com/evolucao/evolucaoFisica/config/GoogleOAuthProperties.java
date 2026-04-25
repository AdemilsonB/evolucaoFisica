package br.com.evolucao.evolucaoFisica.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.security.google")
public record GoogleOAuthProperties(
        String clientId,
        String jwkSetUri,
        String issuer
) {
}
