package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoPostagem;
import br.com.evolucao.evolucaoFisica.enumeration.VisibilidadeConteudo;
import jakarta.validation.constraints.NotNull;

public record PostagemRequest(
        @NotNull(message = "Autor e obrigatorio.")
        Long autorId,
        @NotNull(message = "Tipo da postagem e obrigatorio.")
        TipoPostagem tipo,
        @NotNull(message = "Visibilidade e obrigatoria.")
        VisibilidadeConteudo visibilidade,
        Long grupoId,
        Long registroTreinoId,
        Long evolucaoFisicaId,
        String conteudo,
        String midiaUrl
) {
}
