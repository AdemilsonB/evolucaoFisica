package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.MotivacaoRegistro;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistroDiarioResponse(
        Long id,
        Long usuarioId,
        Long planoAlimentarId,
        LocalDate dataReferencia,
        Boolean realizouTreino,
        String tipoTreino,
        BigDecimal peso,
        BigDecimal proteinaConsumida,
        Boolean bateuProteina,
        Boolean alimentacaoAlinhada,
        BigDecimal horasSono,
        Boolean houveProgressao,
        String descricaoProgressao,
        MotivacaoRegistro motivacao,
        String observacao
) {
}
