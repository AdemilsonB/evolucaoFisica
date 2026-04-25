package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.ProvedorAutenticacao;

import java.time.LocalDateTime;

public record UsuarioIdentidadeExternaResponse(
        Long id,
        Long usuarioId,
        ProvedorAutenticacao provedor,
        String identificadorExterno,
        String emailExterno,
        String nomeExibicao,
        String fotoPerfilUrl,
        LocalDateTime ultimoLoginEm,
        LocalDateTime vinculadoEm
) {
}
