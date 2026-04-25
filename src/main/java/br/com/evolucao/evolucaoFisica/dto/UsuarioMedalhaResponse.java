package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoMedalha;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UsuarioMedalhaResponse(
        Long medalhaId,
        String nome,
        String descricao,
        TipoMedalha tipo,
        Integer quantidade,
        BigDecimal valorMeta,
        LocalDateTime ultimaConquistaEm
) {
}
