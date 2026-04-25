package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoRefeicao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record RefeicaoResponse(
        Long id,
        Long usuarioId,
        LocalDateTime data,
        TipoRefeicao tipo,
        List<RefeicaoAlimentoResponse> alimentos,
        BigDecimal caloriasTotais,
        BigDecimal proteinaTotal,
        BigDecimal carboidratoTotal,
        BigDecimal gorduraTotal,
        BigDecimal acucaresTotais
) {
}
