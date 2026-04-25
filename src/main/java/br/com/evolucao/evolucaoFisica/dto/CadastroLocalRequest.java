package br.com.evolucao.evolucaoFisica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CadastroLocalRequest(
        @NotBlank(message = "Nome e obrigatorio.")
        String nome,
        @Email(message = "Email invalido.")
        @NotBlank(message = "Email e obrigatorio.")
        String email,
        @NotBlank(message = "Username e obrigatorio.")
        String username,
        @NotBlank(message = "Senha e obrigatoria.")
        @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres.")
        String senha
) {
}
