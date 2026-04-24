package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.Objetivo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UsuarioRequest(
        @NotBlank(message = "Nome e obrigatorio.")
        String nome,
        @Email(message = "Email invalido.")
        @NotBlank(message = "Email e obrigatorio.")
        String email,
        @NotBlank(message = "Username e obrigatorio.")
        String username,
        @NotBlank(message = "Senha e obrigatoria.")
        String senha,
        String telefone,
        String bio,
        String fotoPerfilUrl,
        BigDecimal pesoAtual,
        BigDecimal altura,
        @NotNull(message = "Objetivo e obrigatorio.")
        Objetivo objetivo,
        LocalDate dataNascimento,
        String cidade,
        String estado,
        Boolean perfilPrivado,
        Boolean ativo
) {
}
