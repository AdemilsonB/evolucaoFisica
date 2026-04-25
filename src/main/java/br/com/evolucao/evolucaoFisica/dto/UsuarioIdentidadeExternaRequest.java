package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.ProvedorAutenticacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioIdentidadeExternaRequest(
        @NotNull(message = "Usuario e obrigatorio.")
        Long usuarioId,
        @NotNull(message = "Provedor de autenticacao e obrigatorio.")
        ProvedorAutenticacao provedor,
        @NotBlank(message = "Identificador externo e obrigatorio.")
        String identificadorExterno,
        String emailExterno,
        String nomeExibicao,
        String fotoPerfilUrl
) {
}
