package br.com.evolucao.evolucaoFisica.service;

import br.com.evolucao.evolucaoFisica.dto.DashboardGamificacaoResponse;
import br.com.evolucao.evolucaoFisica.dto.MedalhaRequest;
import br.com.evolucao.evolucaoFisica.dto.MedalhaResponse;
import br.com.evolucao.evolucaoFisica.dto.MissaoSemanalRequest;
import br.com.evolucao.evolucaoFisica.dto.MissaoSemanalResponse;
import br.com.evolucao.evolucaoFisica.dto.PerfilGamificacaoResponse;
import br.com.evolucao.evolucaoFisica.dto.RankingItemResponse;
import br.com.evolucao.evolucaoFisica.dto.RegistroDiarioRequest;
import br.com.evolucao.evolucaoFisica.dto.RegistroDiarioResponse;
import br.com.evolucao.evolucaoFisica.dto.UsuarioMedalhaResponse;
import br.com.evolucao.evolucaoFisica.dto.UsuarioMissaoSemanalResponse;
import br.com.evolucao.evolucaoFisica.dto.XpRegraRequest;
import br.com.evolucao.evolucaoFisica.dto.XpRegraResponse;
import br.com.evolucao.evolucaoFisica.dto.XpTransacaoResponse;
import br.com.evolucao.evolucaoFisica.entity.GrupoMembro;
import br.com.evolucao.evolucaoFisica.entity.Medalha;
import br.com.evolucao.evolucaoFisica.entity.MissaoSemanal;
import br.com.evolucao.evolucaoFisica.entity.Nivel;
import br.com.evolucao.evolucaoFisica.entity.PerfilGamificacaoUsuario;
import br.com.evolucao.evolucaoFisica.entity.RegistroDiario;
import br.com.evolucao.evolucaoFisica.entity.RegistroTreino;
import br.com.evolucao.evolucaoFisica.entity.Usuario;
import br.com.evolucao.evolucaoFisica.entity.UsuarioMedalha;
import br.com.evolucao.evolucaoFisica.entity.UsuarioMissaoSemanal;
import br.com.evolucao.evolucaoFisica.entity.XpRegra;
import br.com.evolucao.evolucaoFisica.entity.XpTransacao;
import br.com.evolucao.evolucaoFisica.enumeration.MotivacaoRegistro;
import br.com.evolucao.evolucaoFisica.enumeration.TierRanking;
import br.com.evolucao.evolucaoFisica.enumeration.TipoMedalha;
import br.com.evolucao.evolucaoFisica.enumeration.TipoRegraGamificacao;
import br.com.evolucao.evolucaoFisica.exception.BusinessException;
import br.com.evolucao.evolucaoFisica.exception.ResourceNotFoundException;
import br.com.evolucao.evolucaoFisica.repository.GrupoMembroRepository;
import br.com.evolucao.evolucaoFisica.repository.MedalhaRepository;
import br.com.evolucao.evolucaoFisica.repository.MissaoSemanalRepository;
import br.com.evolucao.evolucaoFisica.repository.NivelRepository;
import br.com.evolucao.evolucaoFisica.repository.PerfilGamificacaoUsuarioRepository;
import br.com.evolucao.evolucaoFisica.repository.RegistroDiarioRepository;
import br.com.evolucao.evolucaoFisica.repository.RegistroExercicioRepository;
import br.com.evolucao.evolucaoFisica.repository.RegistroTreinoRepository;
import br.com.evolucao.evolucaoFisica.repository.SeguidorRepository;
import br.com.evolucao.evolucaoFisica.repository.UsuarioMedalhaRepository;
import br.com.evolucao.evolucaoFisica.repository.UsuarioMissaoSemanalRepository;
import br.com.evolucao.evolucaoFisica.repository.XpRegraRepository;
import br.com.evolucao.evolucaoFisica.repository.XpTransacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class GamificacaoService {

    private static final Logger log = LoggerFactory.getLogger(GamificacaoService.class);
    private static final BigDecimal CEM = new BigDecimal("100");

    private final UsuarioService usuarioService;
    private final PerfilGamificacaoUsuarioRepository perfilRepository;
    private final RegistroDiarioRepository registroDiarioRepository;
    private final RegistroTreinoRepository registroTreinoRepository;
    private final XpRegraRepository xpRegraRepository;
    private final XpTransacaoRepository xpTransacaoRepository;
    private final NivelRepository nivelRepository;
    private final MedalhaRepository medalhaRepository;
    private final UsuarioMedalhaRepository usuarioMedalhaRepository;
    private final MissaoSemanalRepository missaoSemanalRepository;
    private final UsuarioMissaoSemanalRepository usuarioMissaoSemanalRepository;
    private final RegistroExercicioRepository registroExercicioRepository;
    private final SeguidorRepository seguidorRepository;
    private final GrupoMembroRepository grupoMembroRepository;

    public GamificacaoService(
            UsuarioService usuarioService,
            PerfilGamificacaoUsuarioRepository perfilRepository,
            RegistroDiarioRepository registroDiarioRepository,
            RegistroTreinoRepository registroTreinoRepository,
            XpRegraRepository xpRegraRepository,
            XpTransacaoRepository xpTransacaoRepository,
            NivelRepository nivelRepository,
            MedalhaRepository medalhaRepository,
            UsuarioMedalhaRepository usuarioMedalhaRepository,
            MissaoSemanalRepository missaoSemanalRepository,
            UsuarioMissaoSemanalRepository usuarioMissaoSemanalRepository,
            RegistroExercicioRepository registroExercicioRepository,
            SeguidorRepository seguidorRepository,
            GrupoMembroRepository grupoMembroRepository
    ) {
        this.usuarioService = usuarioService;
        this.perfilRepository = perfilRepository;
        this.registroDiarioRepository = registroDiarioRepository;
        this.registroTreinoRepository = registroTreinoRepository;
        this.xpRegraRepository = xpRegraRepository;
        this.xpTransacaoRepository = xpTransacaoRepository;
        this.nivelRepository = nivelRepository;
        this.medalhaRepository = medalhaRepository;
        this.usuarioMedalhaRepository = usuarioMedalhaRepository;
        this.missaoSemanalRepository = missaoSemanalRepository;
        this.usuarioMissaoSemanalRepository = usuarioMissaoSemanalRepository;
        this.registroExercicioRepository = registroExercicioRepository;
        this.seguidorRepository = seguidorRepository;
        this.grupoMembroRepository = grupoMembroRepository;
    }

    @Transactional
    public RegistroDiarioResponse registrarDia(RegistroDiarioRequest request) {
        validarRegistroDiario(request);
        Usuario usuario = usuarioService.buscarEntidade(request.usuarioId());
        RegistroDiario registro = registroDiarioRepository
                .findByUsuarioIdAndDataReferencia(request.usuarioId(), request.dataReferencia())
                .orElseGet(RegistroDiario::new);

        registro.setUsuario(usuario);
        registro.setDataReferencia(request.dataReferencia());
        registro.setRealizouTreino(Boolean.TRUE.equals(request.realizouTreino()));
        registro.setTipoTreino(request.tipoTreino());
        registro.setPeso(request.peso());
        registro.setProteinaConsumida(request.proteinaConsumida());
        registro.setBateuProteina(Boolean.TRUE.equals(request.bateuProteina()));
        registro.setAlimentacaoAlinhada(Boolean.TRUE.equals(request.alimentacaoAlinhada()));
        registro.setHorasSono(request.horasSono());
        registro.setHouveProgressao(Boolean.TRUE.equals(request.houveProgressao()));
        registro.setDescricaoProgressao(request.descricaoProgressao());
        registro.setMotivacao(request.motivacao() == null ? MotivacaoRegistro.MEDIA : request.motivacao());
        registro.setObservacao(request.observacao());

        RegistroDiario salvo = registroDiarioRepository.save(registro);
        processarGamificacaoDiaria(salvo);
        log.info("Registro diario processado para usuarioId={} data={}", request.usuarioId(), request.dataReferencia());
        return toRegistroResponse(salvo);
    }

    @Transactional
    public void processarTreinoConcluido(RegistroTreino registroTreino) {
        if (registroTreino == null || !registroTreino.isConcluido()) {
            throw new BusinessException("Somente treinos concluidos podem gerar recompensas.");
        }

        PerfilGamificacaoUsuario perfil = obterOuCriarPerfil(registroTreino.getUsuario());
        boolean retornoAposPausa = atualizarPerfilBasicoPorTreino(perfil, registroTreino);
        processarXpTreino(perfil, registroTreino, retornoAposPausa);
        processarNivelETier(perfil);
        processarMedalhas(perfil);
        processarMissoesSemanais(perfil, dataReferenciaTreino(registroTreino), null, registroTreino);
        perfilRepository.save(perfil);
        log.info("Gamificacao processada a partir de treino concluido registroTreinoId={} usuarioId={}",
                registroTreino.getId(),
                registroTreino.getUsuario().getId());
    }

    public List<RegistroDiarioResponse> listarRegistrosDiarios(Long usuarioId) {
        return registroDiarioRepository.findAllByUsuarioIdOrderByDataReferenciaDesc(usuarioId).stream().map(this::toRegistroResponse).toList();
    }

    public DashboardGamificacaoResponse montarDashboard(Long usuarioId) {
        PerfilGamificacaoUsuario perfil = obterPerfil(usuarioId);
        LocalDate inicioSemana = inicioSemana(LocalDate.now());

        List<UsuarioMedalhaResponse> medalhas = usuarioMedalhaRepository.findAllByUsuarioIdOrderByMedalhaNomeAsc(usuarioId)
                .stream()
                .map(this::toUsuarioMedalhaResponse)
                .toList();

        List<UsuarioMedalhaResponse> unicas = medalhas.stream().filter(m -> m.tipo() == TipoMedalha.UNICA).toList();
        List<UsuarioMedalhaResponse> repetiveis = medalhas.stream().filter(m -> m.tipo() == TipoMedalha.REPETIVEL).toList();

        List<UsuarioMissaoSemanalResponse> missoes = usuarioMissaoSemanalRepository
                .findAllByUsuarioIdAndSemanaReferenciaOrderByMissaoNomeAsc(usuarioId, inicioSemana)
                .stream()
                .map(this::toUsuarioMissaoResponse)
                .toList();

        List<XpRegraResponse> regras = xpRegraRepository.findAllByAtivoTrue().stream().map(this::toXpRegraResponse).toList();
        List<XpTransacaoResponse> historico = xpTransacaoRepository.findAllByUsuarioIdOrderByCriadoEmDesc(usuarioId).stream()
                .limit(20)
                .map(this::toXpTransacaoResponse)
                .toList();

        return new DashboardGamificacaoResponse(
                toPerfilResponse(perfil),
                unicas,
                repetiveis,
                missoes,
                regras,
                historico
        );
    }

    public List<RankingItemResponse> rankingGeral() {
        return toRanking(perfilRepository.findTop20ByOrderByXpTotalDescNivelAtualDesc());
    }

    public List<RankingItemResponse> rankingSeguindo(Long usuarioId) {
        Set<Long> ids = new HashSet<>();
        ids.add(usuarioId);
        seguidorRepository.findAllBySeguidorIdAndAtivoTrue(usuarioId).forEach(s -> ids.add(s.getSeguido().getId()));
        return toRanking(perfilRepository.findAllByUsuarioIdInOrderByXpTotalDescNivelAtualDesc(new ArrayList<>(ids)));
    }

    public List<RankingItemResponse> rankingGrupo(Long grupoId) {
        List<Long> ids = grupoMembroRepository.findAllByGrupoIdAndAtivoTrue(grupoId)
                .stream()
                .map(GrupoMembro::getUsuario)
                .map(Usuario::getId)
                .toList();
        return toRanking(perfilRepository.findAllByUsuarioIdInOrderByXpTotalDescNivelAtualDesc(ids));
    }

    @Transactional
    public XpRegraResponse salvarXpRegra(Long id, XpRegraRequest request) {
        validarConfiguracaoXp(request);
        XpRegra regra = id == null ? new XpRegra() : xpRegraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Regra de XP nao encontrada."));
        regra.setNome(request.nome());
        regra.setTipoRegra(request.tipoRegra());
        regra.setXpConcedido(request.tipoRegra() == TipoRegraGamificacao.TREINO_SEM_VONTADE ? 0 : request.xpConcedido());
        regra.setPercentualBonus(request.tipoRegra() == TipoRegraGamificacao.TREINO_SEM_VONTADE ? request.percentualBonus() : null);
        regra.setDescricao(request.descricao());
        regra.setAtivo(request.ativo() == null ? Boolean.TRUE : request.ativo());
        return toXpRegraResponse(xpRegraRepository.save(regra));
    }

    public List<XpRegraResponse> listarXpRegras() {
        return xpRegraRepository.findAll().stream().map(this::toXpRegraResponse).toList();
    }

    @Transactional
    public MedalhaResponse salvarMedalha(Long id, MedalhaRequest request) {
        Medalha medalha = id == null ? new Medalha() : medalhaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medalha nao encontrada."));
        medalha.setNome(request.nome());
        medalha.setDescricao(request.descricao());
        medalha.setTipo(request.tipo());
        medalha.setTipoRegra(request.tipoRegra());
        medalha.setValorMeta(request.valorMeta());
        medalha.setValorReferencia(request.valorReferencia());
        medalha.setAtivo(request.ativo() == null ? Boolean.TRUE : request.ativo());
        return toMedalhaResponse(medalhaRepository.save(medalha));
    }

    public List<MedalhaResponse> listarMedalhasConfiguradas() {
        return medalhaRepository.findAll().stream().map(this::toMedalhaResponse).toList();
    }

    @Transactional
    public MissaoSemanalResponse salvarMissao(Long id, MissaoSemanalRequest request) {
        MissaoSemanal missao = id == null ? new MissaoSemanal() : missaoSemanalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Missao semanal nao encontrada."));
        missao.setNome(request.nome());
        missao.setDescricao(request.descricao());
        missao.setTipoRegra(request.tipoRegra());
        missao.setMetaValor(request.metaValor());
        missao.setXpRecompensa(request.xpRecompensa());
        missao.setAtivo(request.ativo() == null ? Boolean.TRUE : request.ativo());
        return toMissaoResponse(missaoSemanalRepository.save(missao));
    }

    public List<MissaoSemanalResponse> listarMissoesConfiguradas() {
        return missaoSemanalRepository.findAll().stream().map(this::toMissaoResponse).toList();
    }

    private void validarRegistroDiario(RegistroDiarioRequest request) {
        if (request == null) {
            throw new BusinessException("O registro diario precisa ser informado.");
        }
    }

    private void validarConfiguracaoXp(XpRegraRequest request) {
        if (request == null) {
            throw new BusinessException("A configuracao de XP precisa ser informada.");
        }
        if (request.tipoRegra() == TipoRegraGamificacao.TREINO_SEM_VONTADE) {
            if (request.percentualBonus() == null || request.percentualBonus().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException("O percentual bonus do treino sem vontade deve ser maior que zero.");
            }
            return;
        }
        if (request.xpConcedido() == null || request.xpConcedido() <= 0) {
            throw new BusinessException("O XP concedido deve ser maior que zero.");
        }
    }

    private void processarGamificacaoDiaria(RegistroDiario registro) {
        PerfilGamificacaoUsuario perfil = obterOuCriarPerfil(registro.getUsuario());
        atualizarPerfilBasico(perfil, registro);
        processarXpDiario(registro, perfil);
        processarNivelETier(perfil);
        processarMedalhas(perfil);
        processarMissoesSemanais(perfil, registro.getDataReferencia(), registro, null);
        perfilRepository.save(perfil);
    }

    private void atualizarPerfilBasico(PerfilGamificacaoUsuario perfil, RegistroDiario registro) {
        perfil.setUltimaDataRegistro(registro.getDataReferencia());
        if (registro.getPeso() != null) {
            perfil.setPesoAtual(registro.getPeso());
        }
        if (Boolean.TRUE.equals(registro.getRealizouTreino())) {
            perfil.setTreinosRealizados(perfil.getTreinosRealizados() + 1);
            perfil.setDiasTreinadosTotal((int) registroDiarioRepository.countByUsuarioIdAndRealizouTreinoTrue(perfil.getUsuario().getId()));
            if (registro.getMotivacao() == MotivacaoRegistro.BAIXA) {
                perfil.setTreinosSemVontadeTotal((int) registroDiarioRepository.countByUsuarioIdAndRealizouTreinoTrueAndMotivacao(perfil.getUsuario().getId(), MotivacaoRegistro.BAIXA));
            }
            if (registro.getHouveProgressao()) {
                perfil.setProgressoesTotal((int) registroDiarioRepository.countByUsuarioIdAndHouveProgressaoTrue(perfil.getUsuario().getId()));
            }
        }
        if (Boolean.TRUE.equals(registro.getAlimentacaoAlinhada())) {
            perfil.setDiasAlimentacaoAlinhada((int) registroDiarioRepository.countByUsuarioIdAndAlimentacaoAlinhadaTrue(perfil.getUsuario().getId()));
        }

        recalcularSequencias(perfil);
    }

    private boolean atualizarPerfilBasicoPorTreino(PerfilGamificacaoUsuario perfil, RegistroTreino registroTreino) {
        LocalDate dataTreino = dataReferenciaTreino(registroTreino);
        boolean retornoAposPausa = perfil.getUltimaDataTreino() != null
                && perfil.getUltimaDataTreino().plusDays(7).isBefore(dataTreino);

        if (retornoAposPausa) {
            perfil.setRetornosAposPausaTotal(perfil.getRetornosAposPausaTotal() + 1);
        }

        perfil.setUltimaDataTreino(dataTreino);
        perfil.setTreinosRealizados(Math.max(perfil.getTreinosRealizados(), totalTreinosConcluidos(perfil.getUsuario().getId())));
        perfil.setDiasTreinadosTotal(Math.max(perfil.getDiasTreinadosTotal(), totalDiasTreinadosReais(perfil.getUsuario().getId())));
        perfil.setTreinosSemVontadeTotal(Math.max(perfil.getTreinosSemVontadeTotal(), totalTreinosSemVontade(perfil.getUsuario().getId())));
        return retornoAposPausa;
    }

    private void processarXpDiario(RegistroDiario registro, PerfilGamificacaoUsuario perfil) {
        Long usuarioId = perfil.getUsuario().getId();
        concederXpPorRegra(registro.getUsuario(), registro, null, TipoRegraGamificacao.REGISTRO_DIARIO, "Registro diario", "daily:" + registro.getDataReferencia());

        if (Boolean.TRUE.equals(registro.getHouveProgressao())) {
            concederXpPorRegra(registro.getUsuario(), registro, null, TipoRegraGamificacao.PROGRESSAO_REAL, "Progressao real", "progress:" + registro.getDataReferencia());
            perfil.setProgressoesTotal((int) registroDiarioRepository.countByUsuarioIdAndHouveProgressaoTrue(usuarioId));
        }
        if (Boolean.TRUE.equals(registro.getBateuProteina())) {
            concederXpPorRegra(registro.getUsuario(), registro, null, TipoRegraGamificacao.PROTEINA_DIARIA, "Proteina diaria batida", "protein:" + registro.getDataReferencia());
        }
        if (sonoAdequado(registro.getHorasSono())) {
            concederXpPorRegra(registro.getUsuario(), registro, null, TipoRegraGamificacao.SONO_ADEQUADO, "Sono adequado", "sleep:" + registro.getDataReferencia());
        }

        LocalDate inicioSemana = inicioSemana(registro.getDataReferencia());
        if (diasSonoConsistenteSemana(usuarioId, inicioSemana) >= 5) {
            concederXpPorRegra(registro.getUsuario(), registro, null, TipoRegraGamificacao.SONO_CONSISTENTE_SEMANA, "5 dias de sono consistente", "week-sleep:" + inicioSemana);
        }
        if (semanaCompletaRegistrada(usuarioId, inicioSemana)) {
            concederXpPorRegra(registro.getUsuario(), registro, null, TipoRegraGamificacao.SEMANA_COMPLETA_REGISTRADA, "Semana completa registrada", "week-complete:" + inicioSemana);
        }
    }

    private void processarXpTreino(PerfilGamificacaoUsuario perfil, RegistroTreino registroTreino, boolean retornoAposPausa) {
        Usuario usuario = perfil.getUsuario();
        LocalDate dataTreino = dataReferenciaTreino(registroTreino);
        int xpBaseTreino = concederXpPorRegra(
                usuario,
                null,
                registroTreino,
                TipoRegraGamificacao.TREINO_COMPLETO,
                "Treino completo",
                "workout:" + registroTreino.getId()
        );

        if (registroTreino.getMotivacao() == MotivacaoRegistro.BAIXA) {
            concederBonusTreinoSemVontade(usuario, registroTreino, xpBaseTreino);
        }
        if (retornoAposPausa) {
            concederXpPorRegra(
                    usuario,
                    null,
                    registroTreino,
                    TipoRegraGamificacao.RETORNO_APOS_PAUSA,
                    "Retorno apos pausa",
                    "return:" + registroTreino.getId()
            );
        }
        if (treinosSemana(usuario.getId(), inicioSemana(dataTreino)) >= 5) {
            concederXpPorRegra(
                    usuario,
                    null,
                    registroTreino,
                    TipoRegraGamificacao.TREINOS_SEMANA,
                    "5 treinos na semana",
                    "week-trainings:" + inicioSemana(dataTreino)
            );
        }
    }

    private void concederBonusTreinoSemVontade(Usuario usuario, RegistroTreino registroTreino, int xpBaseTreino) {
        XpRegra regraBonus = xpRegraRepository.findByTipoRegra(TipoRegraGamificacao.TREINO_SEM_VONTADE)
                .filter(XpRegra::getAtivo)
                .orElse(null);
        if (regraBonus == null || regraBonus.getPercentualBonus() == null) {
            log.warn("Regra percentual de treino sem vontade nao configurada para usuarioId={}", usuario.getId());
            return;
        }

        int baseConfigurada = xpBaseTreino > 0 ? xpBaseTreino : obterXpBaseTreinoCompleto();
        if (baseConfigurada <= 0) {
            return;
        }

        int valorBonus = BigDecimal.valueOf(baseConfigurada)
                .multiply(regraBonus.getPercentualBonus())
                .divide(CEM, 0, RoundingMode.HALF_UP)
                .intValue();

        concederXpLivre(
                usuario,
                null,
                registroTreino,
                "Bonus treino sem vontade",
                "workout:no-motivation:" + registroTreino.getId(),
                valorBonus,
                regraBonus
        );
    }

    private int obterXpBaseTreinoCompleto() {
        return xpRegraRepository.findByTipoRegra(TipoRegraGamificacao.TREINO_COMPLETO)
                .filter(XpRegra::getAtivo)
                .map(XpRegra::getXpConcedido)
                .orElse(0);
    }

    private void processarNivelETier(PerfilGamificacaoUsuario perfil) {
        int xpAtual = perfil.getXpTotal();
        while (xpAtual >= calcularXpAcumuladoParaNivel(perfil.getNivelAtual() + 1)) {
            perfil.setNivelAtual(perfil.getNivelAtual() + 1);
            perfil.setNiveisSubidosTotal(perfil.getNiveisSubidosTotal() + 1);
            log.info("UsuarioId={} subiu para nivel={}", perfil.getUsuario().getId(), perfil.getNivelAtual());
        }
        perfil.setTierAtual(calcularTier(perfil.getNivelAtual()));
    }

    private void processarMedalhas(PerfilGamificacaoUsuario perfil) {
        for (Medalha medalha : medalhaRepository.findAllByAtivoTrueOrderByNomeAsc()) {
            int quantidadeEsperada = calcularQuantidadeMedalha(perfil, medalha);
            UsuarioMedalha usuarioMedalha = usuarioMedalhaRepository
                    .findByUsuarioIdAndMedalhaId(perfil.getUsuario().getId(), medalha.getId())
                    .orElseGet(() -> {
                        UsuarioMedalha novo = new UsuarioMedalha();
                        novo.setUsuario(perfil.getUsuario());
                        novo.setMedalha(medalha);
                        return novo;
                    });

            if (quantidadeEsperada > usuarioMedalha.getQuantidade()) {
                usuarioMedalha.setQuantidade(quantidadeEsperada);
                usuarioMedalha.setUltimaConquistaEm(java.time.LocalDateTime.now());
                usuarioMedalhaRepository.save(usuarioMedalha);
            }
        }
    }

    private void processarMissoesSemanais(
            PerfilGamificacaoUsuario perfil,
            LocalDate dataReferencia,
            RegistroDiario registroDiario,
            RegistroTreino registroTreino
    ) {
        LocalDate inicioSemana = inicioSemana(dataReferencia);
        for (MissaoSemanal missao : missaoSemanalRepository.findAllByAtivoTrueOrderByNomeAsc()) {
            UsuarioMissaoSemanal usuarioMissao = usuarioMissaoSemanalRepository
                    .findByUsuarioIdAndMissaoIdAndSemanaReferencia(perfil.getUsuario().getId(), missao.getId(), inicioSemana)
                    .orElseGet(() -> {
                        UsuarioMissaoSemanal novo = new UsuarioMissaoSemanal();
                        novo.setUsuario(perfil.getUsuario());
                        novo.setMissao(missao);
                        novo.setSemanaReferencia(inicioSemana);
                        return novo;
                    });

            BigDecimal progresso = calcularMetrica(perfil, missao.getTipoRegra(), null, inicioSemana);
            usuarioMissao.setProgresso(progresso);

            if (!Boolean.TRUE.equals(usuarioMissao.getConcluida()) && progresso.compareTo(missao.getMetaValor()) >= 0) {
                usuarioMissao.setConcluida(Boolean.TRUE);
                usuarioMissao.setTotalConclusoes((int) usuarioMissaoSemanalRepository
                        .countByUsuarioIdAndMissaoIdAndConcluidaTrue(perfil.getUsuario().getId(), missao.getId()) + 1);
                concederXpLivre(
                        perfil.getUsuario(),
                        registroDiario,
                        registroTreino,
                        "Missao semanal concluida: " + missao.getNome(),
                        "mission:" + inicioSemana + ":" + missao.getId(),
                        missao.getXpRecompensa()
                );
            }
            usuarioMissaoSemanalRepository.save(usuarioMissao);
        }
    }

    private int concederXpPorRegra(
            Usuario usuario,
            RegistroDiario registroDiario,
            RegistroTreino registroTreino,
            TipoRegraGamificacao tipo,
            String descricao,
            String referencia
    ) {
        XpRegra regra = xpRegraRepository.findByTipoRegra(tipo).filter(XpRegra::getAtivo).orElse(null);
        if (regra == null) {
            return 0;
        }
        return concederXpLivre(usuario, registroDiario, registroTreino, descricao, referencia, regra.getXpConcedido(), regra);
    }

    private int concederXpLivre(
            Usuario usuario,
            RegistroDiario registroDiario,
            RegistroTreino registroTreino,
            String descricao,
            String referencia,
            Integer valor
    ) {
        return concederXpLivre(usuario, registroDiario, registroTreino, descricao, referencia, valor, null);
    }

    private int concederXpLivre(
            Usuario usuario,
            RegistroDiario registroDiario,
            RegistroTreino registroTreino,
            String descricao,
            String referencia,
            Integer valor,
            XpRegra regra
    ) {
        if (valor == null || valor <= 0) {
            return 0;
        }
        if (xpTransacaoRepository.existsByUsuarioIdAndReferenciaUnica(usuario.getId(), referencia)) {
            return 0;
        }
        XpTransacao transacao = new XpTransacao();
        transacao.setUsuario(usuario);
        transacao.setRegistroDiario(registroDiario);
        transacao.setRegistroTreino(registroTreino);
        transacao.setRegra(regra);
        transacao.setDescricao(descricao);
        transacao.setReferenciaUnica(referencia);
        transacao.setValor(valor);
        xpTransacaoRepository.save(transacao);

        PerfilGamificacaoUsuario perfil = obterPerfil(usuario.getId());
        perfil.setXpTotal(perfil.getXpTotal() + valor);
        perfilRepository.save(perfil);
        return valor;
    }

    private PerfilGamificacaoUsuario obterPerfil(Long usuarioId) {
        return perfilRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil de gamificacao nao encontrado."));
    }

    private PerfilGamificacaoUsuario obterOuCriarPerfil(Usuario usuario) {
        return perfilRepository.findByUsuarioId(usuario.getId()).orElseGet(() -> {
            PerfilGamificacaoUsuario perfil = new PerfilGamificacaoUsuario();
            perfil.setUsuario(usuario);
            perfil.setDataInicio(LocalDate.now());
            perfil.setPesoInicial(usuario.getPesoAtual());
            perfil.setPesoAtual(usuario.getPesoAtual());
            return perfilRepository.save(perfil);
        });
    }

    private Nivel obterOuCriarNivel(Integer numero) {
        return nivelRepository.findByNumero(numero).orElseGet(() -> {
            Nivel nivel = new Nivel();
            nivel.setNumero(numero);
            nivel.setXpNecessario(calcularXpAcumuladoParaNivel(numero));
            return nivelRepository.save(nivel);
        });
    }

    private int calcularXpNecessarioProximoNivel(int nivelAtual) {
        return 500 + (nivelAtual * 250);
    }

    private int calcularXpAcumuladoParaNivel(int nivel) {
        int xpTotalNecessario = 0;
        for (int nivelAtual = 1; nivelAtual < nivel; nivelAtual++) {
            xpTotalNecessario += calcularXpNecessarioProximoNivel(nivelAtual);
        }
        return xpTotalNecessario;
    }

    private TierRanking calcularTier(int nivelAtual) {
        if (nivelAtual <= 5) {
            return TierRanking.BRONZE;
        }
        if (nivelAtual <= 10) {
            return TierRanking.PRATA;
        }
        if (nivelAtual <= 20) {
            return TierRanking.OURO;
        }
        return TierRanking.LENDA;
    }

    private boolean sonoAdequado(BigDecimal horasSono) {
        if (horasSono == null) {
            return false;
        }
        return horasSono.compareTo(new BigDecimal("7.00")) >= 0 && horasSono.compareTo(new BigDecimal("8.00")) <= 0;
    }

    private int treinosSemana(Long usuarioId, LocalDate inicioSemana) {
        return (int) registroTreinoRepository
                .findAllByUsuarioIdAndConcluidoTrueOrderByDataRegistroAsc(usuarioId)
                .stream()
                .map(this::dataReferenciaTreino)
                .filter(data -> !data.isBefore(inicioSemana) && !data.isAfter(inicioSemana.plusDays(6)))
                .distinct()
                .count();
    }

    private int diasSonoConsistenteSemana(Long usuarioId, LocalDate inicioSemana) {
        return (int) registroDiarioRepository
                .findAllByUsuarioIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(usuarioId, inicioSemana, inicioSemana.plusDays(6))
                .stream()
                .filter(r -> sonoAdequado(r.getHorasSono()))
                .count();
    }

    private boolean semanaCompletaRegistrada(Long usuarioId, LocalDate inicioSemana) {
        List<RegistroDiario> registros = registroDiarioRepository
                .findAllByUsuarioIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(usuarioId, inicioSemana, inicioSemana.plusDays(6));
        Set<DayOfWeek> dias = registros.stream().map(RegistroDiario::getDataReferencia).map(LocalDate::getDayOfWeek).collect(java.util.stream.Collectors.toSet());
        return dias.containsAll(Set.of(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        ));
    }

    private void recalcularSequencias(PerfilGamificacaoUsuario perfil) {
        List<LocalDate> datas = registroDiarioRepository.findAllByUsuarioIdOrderByDataReferenciaDesc(perfil.getUsuario().getId())
                .stream()
                .map(RegistroDiario::getDataReferencia)
                .distinct()
                .sorted()
                .toList();

        int melhor = 0;
        int atual = 0;
        LocalDate anterior = null;
        for (LocalDate data : datas) {
            if (anterior == null) {
                atual = 1;
            } else {
                LocalDate proximoDiaUtil = proximoDiaUtil(anterior);
                if (data.equals(proximoDiaUtil) || isFimDeSemana(data)) {
                    atual++;
                } else {
                    atual = 1;
                }
            }
            melhor = Math.max(melhor, atual);
            anterior = data;
        }
        perfil.setSequenciaAtual(atual);
        perfil.setMelhorSequencia(melhor);
    }

    private LocalDate proximoDiaUtil(LocalDate data) {
        LocalDate proximo = data.plusDays(1);
        while (isFimDeSemana(proximo)) {
            proximo = proximo.plusDays(1);
        }
        return proximo;
    }

    private boolean isFimDeSemana(LocalDate data) {
        return data.getDayOfWeek() == DayOfWeek.SATURDAY || data.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private LocalDate inicioSemana(LocalDate data) {
        return data.minusDays(data.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue());
    }

    private LocalDate dataReferenciaTreino(RegistroTreino registroTreino) {
        return registroTreino.getFinalizadoEm() != null
                ? registroTreino.getFinalizadoEm().toLocalDate()
                : registroTreino.getDataRegistro().toLocalDate();
    }

    private int totalTreinosConcluidos(Long usuarioId) {
        return registroTreinoRepository.findAllByUsuarioIdAndConcluidoTrueOrderByDataRegistroAsc(usuarioId).size();
    }

    private int totalDiasTreinadosReais(Long usuarioId) {
        return (int) registroTreinoRepository.findAllByUsuarioIdAndConcluidoTrueOrderByDataRegistroAsc(usuarioId)
                .stream()
                .map(this::dataReferenciaTreino)
                .distinct()
                .count();
    }

    private int totalTreinosSemVontade(Long usuarioId) {
        return (int) registroTreinoRepository.findAllByUsuarioIdAndConcluidoTrueOrderByDataRegistroAsc(usuarioId)
                .stream()
                .filter(registroTreino -> registroTreino.getMotivacao() == MotivacaoRegistro.BAIXA)
                .count();
    }

    private int calcularQuantidadeMedalha(PerfilGamificacaoUsuario perfil, Medalha medalha) {
        BigDecimal metrica = calcularMetrica(perfil, medalha.getTipoRegra(), medalha.getValorReferencia(), inicioSemana(LocalDate.now()));
        if (medalha.getTipo() == TipoMedalha.UNICA) {
            return metrica.compareTo(medalha.getValorMeta()) >= 0 ? 1 : 0;
        }
        if (medalha.getValorMeta().compareTo(BigDecimal.ZERO) <= 0) {
            return 0;
        }
        return metrica.divide(medalha.getValorMeta(), 0, RoundingMode.DOWN).intValue();
    }

    private BigDecimal calcularMetrica(PerfilGamificacaoUsuario perfil, TipoRegraGamificacao tipo, String referencia, LocalDate inicioSemana) {
        Long usuarioId = perfil.getUsuario().getId();
        return switch (tipo) {
            case TREINO_COMPLETO, DIAS_TREINADOS_TOTAL -> BigDecimal.valueOf(totalDiasTreinadosReais(usuarioId));
            case TREINO_SEM_VONTADE, TREINOS_SEM_VONTADE_TOTAL -> BigDecimal.valueOf(totalTreinosSemVontade(usuarioId));
            case PROGRESSAO_REAL, PROGRESSOES_TOTAL -> BigDecimal.valueOf(registroDiarioRepository.countByUsuarioIdAndHouveProgressaoTrue(usuarioId));
            case PROTEINA_DIARIA -> BigDecimal.valueOf(registroDiarioRepository
                    .findAllByUsuarioIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(usuarioId, inicioSemana, inicioSemana.plusDays(6))
                    .stream()
                    .filter(RegistroDiario::getBateuProteina)
                    .count());
            case TREINOS_SEMANA -> BigDecimal.valueOf(treinosSemana(usuarioId, inicioSemana));
            case SONO_ADEQUADO -> BigDecimal.valueOf(diasSonoConsistenteSemana(usuarioId, inicioSemana));
            case SONO_CONSISTENTE_SEMANA -> BigDecimal.valueOf(diasSonoConsistenteSemana(usuarioId, inicioSemana));
            case RETORNO_APOS_PAUSA, RETORNOS_APOS_PAUSA_TOTAL -> BigDecimal.valueOf(perfil.getRetornosAposPausaTotal());
            case REGISTRO_DIARIO, REGISTROS_SEMANA -> BigDecimal.valueOf(
                    registroDiarioRepository.findAllByUsuarioIdAndDataReferenciaBetweenOrderByDataReferenciaAsc(usuarioId, inicioSemana, inicioSemana.plusDays(6)).size()
            );
            case SEMANA_COMPLETA_REGISTRADA -> semanaCompletaRegistrada(usuarioId, inicioSemana) ? BigDecimal.ONE : BigDecimal.ZERO;
            case PESO_ATINGIDO -> perfil.getPesoAtual() == null ? BigDecimal.ZERO : perfil.getPesoAtual();
            case CARGA_EXERCICIO_ATINGIDA -> {
                BigDecimal max = referencia == null ? null : registroExercicioRepository.findMaxCargaByUsuarioAndNomeExercicio(usuarioId, referencia);
                yield max == null ? BigDecimal.ZERO : max;
            }
            case SEQUENCIA_ATUAL -> BigDecimal.valueOf(perfil.getSequenciaAtual());
            case DIAS_ALIMENTACAO_ALINHADA, ALIMENTACAO_ALINHADA -> BigDecimal.valueOf(
                    registroDiarioRepository.countByUsuarioIdAndAlimentacaoAlinhadaTrue(usuarioId)
            );
            case NIVEIS_SUBIDOS_TOTAL -> BigDecimal.valueOf(perfil.getNiveisSubidosTotal());
            case NIVEL_ATUAL -> BigDecimal.valueOf(perfil.getNivelAtual());
            case XP_TOTAL -> BigDecimal.valueOf(perfil.getXpTotal());
        };
    }

    private RegistroDiarioResponse toRegistroResponse(RegistroDiario registro) {
        return new RegistroDiarioResponse(
                registro.getId(),
                registro.getUsuario().getId(),
                registro.getDataReferencia(),
                registro.getRealizouTreino(),
                registro.getTipoTreino(),
                registro.getPeso(),
                registro.getProteinaConsumida(),
                registro.getBateuProteina(),
                registro.getAlimentacaoAlinhada(),
                registro.getHorasSono(),
                registro.getHouveProgressao(),
                registro.getDescricaoProgressao(),
                registro.getMotivacao(),
                registro.getObservacao()
        );
    }

    private PerfilGamificacaoResponse toPerfilResponse(PerfilGamificacaoUsuario perfil) {
        int xpBaseNivelAtual = calcularXpAcumuladoParaNivel(perfil.getNivelAtual());
        int xpNecessarioFaixa = calcularXpNecessarioProximoNivel(perfil.getNivelAtual());
        int xpAtualFaixa = Math.max(0, perfil.getXpTotal() - xpBaseNivelAtual);
        int percentual = xpNecessarioFaixa == 0 ? 100 : (xpAtualFaixa * 100) / xpNecessarioFaixa;

        return new PerfilGamificacaoResponse(
                perfil.getUsuario().getId(),
                perfil.getDataInicio(),
                perfil.getPesoInicial(),
                perfil.getPesoAtual(),
                perfil.getTreinosRealizados(),
                perfil.getSequenciaAtual(),
                perfil.getMelhorSequencia(),
                perfil.getXpTotal(),
                perfil.getNivelAtual(),
                perfil.getTierAtual(),
                xpAtualFaixa,
                xpNecessarioFaixa,
                Math.min(percentual, 100)
        );
    }

    private XpRegraResponse toXpRegraResponse(XpRegra regra) {
        return new XpRegraResponse(
                regra.getId(),
                regra.getNome(),
                regra.getTipoRegra(),
                regra.getXpConcedido(),
                regra.getPercentualBonus(),
                regra.getDescricao(),
                regra.getAtivo()
        );
    }

    private XpTransacaoResponse toXpTransacaoResponse(XpTransacao transacao) {
        return new XpTransacaoResponse(
                transacao.getId(),
                transacao.getValor(),
                transacao.getDescricao(),
                transacao.getReferenciaUnica(),
                transacao.getCriadoEm()
        );
    }

    private MedalhaResponse toMedalhaResponse(Medalha medalha) {
        return new MedalhaResponse(
                medalha.getId(),
                medalha.getNome(),
                medalha.getDescricao(),
                medalha.getTipo(),
                medalha.getTipoRegra(),
                medalha.getValorMeta(),
                medalha.getValorReferencia(),
                medalha.getAtivo()
        );
    }

    private UsuarioMedalhaResponse toUsuarioMedalhaResponse(UsuarioMedalha usuarioMedalha) {
        return new UsuarioMedalhaResponse(
                usuarioMedalha.getMedalha().getId(),
                usuarioMedalha.getMedalha().getNome(),
                usuarioMedalha.getMedalha().getDescricao(),
                usuarioMedalha.getMedalha().getTipo(),
                usuarioMedalha.getQuantidade(),
                usuarioMedalha.getMedalha().getValorMeta(),
                usuarioMedalha.getUltimaConquistaEm()
        );
    }

    private MissaoSemanalResponse toMissaoResponse(MissaoSemanal missao) {
        return new MissaoSemanalResponse(
                missao.getId(),
                missao.getNome(),
                missao.getDescricao(),
                missao.getTipoRegra(),
                missao.getMetaValor(),
                missao.getXpRecompensa(),
                missao.getAtivo()
        );
    }

    private UsuarioMissaoSemanalResponse toUsuarioMissaoResponse(UsuarioMissaoSemanal usuarioMissao) {
        return new UsuarioMissaoSemanalResponse(
                usuarioMissao.getMissao().getId(),
                usuarioMissao.getMissao().getNome(),
                usuarioMissao.getMissao().getDescricao(),
                usuarioMissao.getProgresso(),
                usuarioMissao.getMissao().getMetaValor(),
                usuarioMissao.getConcluida(),
                usuarioMissao.getTotalConclusoes(),
                usuarioMissao.getMissao().getXpRecompensa()
        );
    }

    private List<RankingItemResponse> toRanking(List<PerfilGamificacaoUsuario> perfis) {
        List<PerfilGamificacaoUsuario> ordenados = perfis.stream()
                .sorted(Comparator.comparing(PerfilGamificacaoUsuario::getXpTotal).reversed()
                        .thenComparing(PerfilGamificacaoUsuario::getNivelAtual).reversed())
                .toList();
        List<RankingItemResponse> ranking = new ArrayList<>();
        int posicao = 1;
        for (PerfilGamificacaoUsuario perfil : ordenados) {
            ranking.add(new RankingItemResponse(
                    posicao++,
                    perfil.getUsuario().getId(),
                    perfil.getUsuario().getUsername(),
                    perfil.getXpTotal(),
                    perfil.getNivelAtual(),
                    perfil.getTierAtual()
            ));
        }
        return ranking;
    }
}
