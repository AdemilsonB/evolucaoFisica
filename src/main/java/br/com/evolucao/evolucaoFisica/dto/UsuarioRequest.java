package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.Objetivo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UsuarioRequest(
        @NotBlank(message = "Nome e obrigatorio.")
        String nome,
        @Email(message = "Email invalido.")
        @NotBlank(message = "Email e obrigatorio.")
        String email,
        @NotBlank(message = "Senha e obrigatoria.")
        String senha,
        BigDecimal pesoAtual,
        BigDecimal altura,
        @NotNull(message = "Objetivo e obrigatorio.")
        Objetivo objetivo,
        Boolean ativo
) {
}
