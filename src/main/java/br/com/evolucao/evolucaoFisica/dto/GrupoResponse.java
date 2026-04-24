package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.VisibilidadeConteudo;

import java.util.List;

public record GrupoResponse(
        Long id,
        String nome,
        String descricao,
        Long criadorId,
        String criadorUsername,
        VisibilidadeConteudo visibilidade,
        Boolean ativo,
        List<GrupoMembroResponse> membros
) {
}
