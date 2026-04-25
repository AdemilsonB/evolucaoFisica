package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.MotivacaoRegistro;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegistroDiarioRequest(
        @NotNull(message = "Usuario e obrigatorio.")
        Long usuarioId,
        Long planoAlimentarId,
        @NotNull(message = "Data de referencia e obrigatoria.")
        LocalDate dataReferencia,
        @NotNull(message = "Indicador de treino e obrigatorio.")
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
