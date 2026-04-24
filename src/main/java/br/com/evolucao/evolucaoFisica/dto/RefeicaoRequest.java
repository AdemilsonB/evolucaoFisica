package br.com.evolucao.evolucaoFisica.dto;

import br.com.evolucao.evolucaoFisica.enumeration.TipoRefeicao;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RefeicaoRequest(
        @NotNull(message = "Usuario e obrigatorio.")
        Long usuarioId,
        @NotNull(message = "Data da refeicao e obrigatoria.")
        LocalDateTime data,
        @NotNull(message = "Tipo da refeicao e obrigatorio.")
        TipoRefeicao tipo
) {
}
