package br.com.evolucao.evolucaoFisica.config;

import br.com.evolucao.evolucaoFisica.enumeration.RoleSistema;

public record UsuarioAutenticado(
        Long usuarioId,
        String email,
        String username,
        RoleSistema roleSistema
) {
}
