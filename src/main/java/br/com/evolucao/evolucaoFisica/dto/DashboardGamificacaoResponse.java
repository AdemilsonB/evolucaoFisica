package br.com.evolucao.evolucaoFisica.dto;

import java.util.List;

public record DashboardGamificacaoResponse(
        PerfilGamificacaoResponse perfil,
        List<UsuarioMedalhaResponse> medalhasUnicas,
        List<UsuarioMedalhaResponse> medalhasRepetiveis,
        List<UsuarioMissaoSemanalResponse> missoesSemanais,
        List<XpRegraResponse> regrasXp,
        List<XpTransacaoResponse> historicoXp
) {
}
