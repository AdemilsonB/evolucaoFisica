package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.Objetivo;
import br.com.evolucao.evolucaoFisica.enumeration.RoleSistema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String username,
        String telefone,
        String bio,
        String fotoPerfilUrl,
        BigDecimal pesoAtual,
        BigDecimal altura,
        Objetivo objetivo,
        LocalDate dataNascimento,
        String cidade,
        String estado,
        Boolean perfilPrivado,
        RoleSistema roleSistema,
        Boolean ativo,
        LocalDateTime dataCriacao
) {
}
