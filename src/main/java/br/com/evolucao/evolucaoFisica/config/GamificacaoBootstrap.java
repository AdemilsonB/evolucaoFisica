package br.com.evolucao.evolucaoFisica.config;

import br.com.evolucao.evolucaoFisica.entity.Medalha;
import br.com.evolucao.evolucaoFisica.entity.MissaoSemanal;
import br.com.evolucao.evolucaoFisica.entity.Nivel;
import br.com.evolucao.evolucaoFisica.entity.XpRegra;
import br.com.evolucao.evolucaoFisica.enumeration.TipoMedalha;
import br.com.evolucao.evolucaoFisica.enumeration.TipoRegraGamificacao;
import br.com.evolucao.evolucaoFisica.repository.MedalhaRepository;
import br.com.evolucao.evolucaoFisica.repository.MissaoSemanalRepository;
import br.com.evolucao.evolucaoFisica.repository.NivelRepository;
import br.com.evolucao.evolucaoFisica.repository.XpRegraRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class GamificacaoBootstrap {

    @Bean
    CommandLineRunner initGamificacao(
            XpRegraRepository xpRegraRepository,
            MedalhaRepository medalhaRepository,
            MissaoSemanalRepository missaoSemanalRepository,
            NivelRepository nivelRepository
    ) {
        return args -> {
            if (xpRegraRepository.count() == 0) {
                xpRegraRepository.saveAll(List.of(
                        xpRegra("Treino completo", TipoRegraGamificacao.TREINO_COMPLETO, 50),
                        xpRegraBonusPendente("Treino sem vontade", TipoRegraGamificacao.TREINO_SEM_VONTADE),
                        xpRegra("Progressao real", TipoRegraGamificacao.PROGRESSAO_REAL, 100),
                        xpRegra("Proteina diaria", TipoRegraGamificacao.PROTEINA_DIARIA, 40),
                        xpRegra("Cinco treinos na semana", TipoRegraGamificacao.TREINOS_SEMANA, 250),
                        xpRegra("Sono adequado", TipoRegraGamificacao.SONO_ADEQUADO, 30),
                        xpRegra("Sono consistente semanal", TipoRegraGamificacao.SONO_CONSISTENTE_SEMANA, 80),
                        xpRegra("Retorno apos pausa", TipoRegraGamificacao.RETORNO_APOS_PAUSA, 250),
                        xpRegra("Registro diario", TipoRegraGamificacao.REGISTRO_DIARIO, 10),
                        xpRegra("Semana completa registrada", TipoRegraGamificacao.SEMANA_COMPLETA_REGISTRADA, 50)
                ));
            }

            if (nivelRepository.count() == 0) {
                int xpAcumulado = 0;
                for (int i = 1; i <= 30; i++) {
                    Nivel nivel = new Nivel();
                    nivel.setNumero(i);
                    nivel.setXpNecessario(xpAcumulado);
                    nivelRepository.save(nivel);
                    xpAcumulado += 500 + (i * 250);
                }
            }

            if (medalhaRepository.count() == 0) {
                medalhaRepository.saveAll(List.of(
                        medalha("3 meses", TipoMedalha.UNICA, TipoRegraGamificacao.DIAS_TREINADOS_TOTAL, "90"),
                        medalha("6 meses", TipoMedalha.UNICA, TipoRegraGamificacao.DIAS_TREINADOS_TOTAL, "180"),
                        medalha("1 ano", TipoMedalha.UNICA, TipoRegraGamificacao.DIAS_TREINADOS_TOTAL, "365"),
                        medalha("77kg atingido", TipoMedalha.UNICA, TipoRegraGamificacao.PESO_ATINGIDO, "77"),
                        medalha("80kg atingido", TipoMedalha.UNICA, TipoRegraGamificacao.PESO_ATINGIDO, "80"),
                        medalhaComReferencia("Supino 30kg", TipoMedalha.UNICA, TipoRegraGamificacao.CARGA_EXERCICIO_ATINGIDA, "30", "Supino"),
                        medalhaComReferencia("Supino 40kg", TipoMedalha.UNICA, TipoRegraGamificacao.CARGA_EXERCICIO_ATINGIDA, "40", "Supino"),
                        medalhaComReferencia("Supino 50kg", TipoMedalha.UNICA, TipoRegraGamificacao.CARGA_EXERCICIO_ATINGIDA, "50", "Supino"),
                        medalha("Consistencia Brutal", TipoMedalha.REPETIVEL, TipoRegraGamificacao.SEQUENCIA_ATUAL, "15"),
                        medalha("Treinei sem vontade", TipoMedalha.REPETIVEL, TipoRegraGamificacao.TREINOS_SEM_VONTADE_TOTAL, "1"),
                        medalha("Progressao Real", TipoMedalha.REPETIVEL, TipoRegraGamificacao.PROGRESSOES_TOTAL, "1"),
                        medalha("Dieta Alinhada", TipoMedalha.REPETIVEL, TipoRegraGamificacao.DIAS_ALIMENTACAO_ALINHADA, "1"),
                        medalha("Voltei Mais Forte", TipoMedalha.REPETIVEL, TipoRegraGamificacao.RETORNOS_APOS_PAUSA_TOTAL, "1"),
                        medalha("Subi de nivel", TipoMedalha.REPETIVEL, TipoRegraGamificacao.NIVEIS_SUBIDOS_TOTAL, "1")
                ));
            }

            if (missaoSemanalRepository.count() == 0) {
                missaoSemanalRepository.saveAll(List.of(
                        missao("Treinar 5 dias", TipoRegraGamificacao.TREINOS_SEMANA, "5", 250),
                        missao("Bater proteina diaria", TipoRegraGamificacao.PROTEINA_DIARIA, "5", 100),
                        missao("Progredir carga ou reps", TipoRegraGamificacao.PROGRESSAO_REAL, "1", 100),
                        missao("Dormir 7-8 horas", TipoRegraGamificacao.SONO_CONSISTENTE_SEMANA, "5", 80),
                        missao("Registrar o dia", TipoRegraGamificacao.REGISTROS_SEMANA, "5", 50)
                ));
            }
        };
    }

    private XpRegra xpRegra(String nome, TipoRegraGamificacao tipo, int xp) {
        XpRegra regra = new XpRegra();
        regra.setNome(nome);
        regra.setTipoRegra(tipo);
        regra.setXpConcedido(xp);
        regra.setPercentualBonus(null);
        regra.setDescricao(nome);
        regra.setAtivo(Boolean.TRUE);
        return regra;
    }

    private XpRegra xpRegraBonusPendente(String nome, TipoRegraGamificacao tipo) {
        XpRegra regra = new XpRegra();
        regra.setNome(nome);
        regra.setTipoRegra(tipo);
        regra.setXpConcedido(0);
        regra.setPercentualBonus(null);
        regra.setDescricao("Configurar percentual bonus sobre o XP-base do treino completo.");
        regra.setAtivo(Boolean.FALSE);
        return regra;
    }

    private Medalha medalha(String nome, TipoMedalha tipo, TipoRegraGamificacao regra, String valor) {
        return medalhaComReferencia(nome, tipo, regra, valor, null);
    }

    private Medalha medalhaComReferencia(String nome, TipoMedalha tipo, TipoRegraGamificacao regra, String valor, String referencia) {
        Medalha medalha = new Medalha();
        medalha.setNome(nome);
        medalha.setDescricao(nome);
        medalha.setTipo(tipo);
        medalha.setTipoRegra(regra);
        medalha.setValorMeta(new BigDecimal(valor));
        medalha.setValorReferencia(referencia);
        medalha.setAtivo(Boolean.TRUE);
        return medalha;
    }

    private MissaoSemanal missao(String nome, TipoRegraGamificacao regra, String meta, int xp) {
        MissaoSemanal missao = new MissaoSemanal();
        missao.setNome(nome);
        missao.setDescricao(nome);
        missao.setTipoRegra(regra);
        missao.setMetaValor(new BigDecimal(meta));
        missao.setXpRecompensa(xp);
        missao.setAtivo(Boolean.TRUE);
        return missao;
    }
}
