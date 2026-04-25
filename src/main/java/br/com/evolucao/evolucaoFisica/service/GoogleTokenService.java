package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.config.GoogleOAuthProperties;
import br.com.evolucao.evolucaoFisica.exception.BusinessException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoogleTokenService {

    private final GoogleOAuthProperties googleOAuthProperties;
    private volatile JwtDecoder googleJwtDecoder;

    public GoogleTokenService(GoogleOAuthProperties googleOAuthProperties) {
        this.googleOAuthProperties = googleOAuthProperties;
    }

    public Jwt validarIdToken(String idToken) {
        if (googleOAuthProperties.clientId() == null || googleOAuthProperties.clientId().isBlank()) {
            throw new BusinessException("O login Google nao esta configurado no ambiente.");
        }

        Jwt jwt = getGoogleJwtDecoder().decode(idToken);
        String issuer = jwt.getIssuer() != null ? jwt.getIssuer().toString() : null;
        if (!googleOAuthProperties.issuer().equals(issuer)) {
            throw new BusinessException("Issuer invalido para o token Google.");
        }

        List<String> audience = jwt.getAudience();
        if (audience == null || !audience.contains(googleOAuthProperties.clientId())) {
            throw new BusinessException("Audience invalida para o token Google.");
        }

        return jwt;
    }

    private JwtDecoder getGoogleJwtDecoder() {
        if (googleJwtDecoder == null) {
            synchronized (this) {
                if (googleJwtDecoder == null) {
                    googleJwtDecoder = NimbusJwtDecoder.withJwkSetUri(googleOAuthProperties.jwkSetUri()).build();
                }
            }
        }
        return googleJwtDecoder;
    }
}
