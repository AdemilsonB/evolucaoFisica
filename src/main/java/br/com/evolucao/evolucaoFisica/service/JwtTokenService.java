package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.config.JwtProperties;
import br.com.evolucao.evolucaoFisica.config.UsuarioAutenticado;
import br.com.evolucao.evolucaoFisica.entity.Usuario;
import br.com.evolucao.evolucaoFisica.enumeration.RoleSistema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtTokenService {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenService.class);

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final JwtProperties jwtProperties;

    public JwtTokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, JwtProperties jwtProperties) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.jwtProperties = jwtProperties;
    }

    public String gerarToken(Usuario usuario) {
        LocalDateTime agora = LocalDateTime.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.issuer())
                .issuedAt(agora.toInstant(ZoneOffset.UTC))
                .expiresAt(agora.plus(jwtProperties.accessTokenTtl()).toInstant(ZoneOffset.UTC))
                .subject(String.valueOf(usuario.getId()))
                .claim("email", usuario.getEmail())
                .claim("username", usuario.getUsername())
                .claim("role", usuario.getRoleSistema().name())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public boolean isTokenValido(String token) {
        try {
            jwtDecoder.decode(token);
            return true;
        } catch (JwtException ex) {
            log.warn("Token JWT invalido: {}", ex.getMessage());
            return false;
        }
    }

    public UsuarioAutenticado extrairUsuario(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        String subject = jwt.getSubject();
        if (subject == null || subject.isBlank()) {
            throw new BadJwtException("Token sem subject.");
        }
        return new UsuarioAutenticado(
                Long.valueOf(subject),
                jwt.getClaimAsString("email"),
                jwt.getClaimAsString("username"),
                RoleSistema.valueOf(jwt.getClaimAsString("role"))
        );
    }

    public LocalDateTime calcularExpiracao() {
        return LocalDateTime.now().plus(jwtProperties.accessTokenTtl());
    }
}
