package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.RoleGrupo;

public record GrupoMembroResponse(
        Long id,
        Long usuarioId,
        String username,
        RoleGrupo role,
        Boolean ativo
) {
}
