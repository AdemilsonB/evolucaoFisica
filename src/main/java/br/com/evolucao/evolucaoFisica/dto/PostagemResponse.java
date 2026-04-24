package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoPostagem;
import br.com.evolucao.evolucaoFisica.enumeration.VisibilidadeConteudo;

import java.time.LocalDateTime;
import java.util.List;

public record PostagemResponse(
        Long id,
        Long autorId,
        String autorUsername,
        TipoPostagem tipo,
        VisibilidadeConteudo visibilidade,
        Long grupoId,
        Long registroTreinoId,
        Long evolucaoFisicaId,
        String conteudo,
        String midiaUrl,
        Integer totalCurtidas,
        List<PostagemComentarioResponse> comentarios,
        LocalDateTime criadoEm
) {
}
