package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.RoleGrupo;
import jakarta.validation.constraints.NotNull;

public record GrupoMembroRequest(
        @NotNull(message = "Usuario e obrigatorio.")
        Long usuarioId,
        @NotNull(message = "Role e obrigatoria.")
        RoleGrupo role
) {
}
