package br.com.evolucao.evolucaoFisica.entity;

import br.com.evolucao.evolucaoFisica.enumeration.TierRanking;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "perfis_gamificacao_usuarios")
public class PerfilGamificacaoUsuario extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "peso_inicial", precision = 10, scale = 2)
    private BigDecimal pesoInicial;

    @Column(name = "peso_atual", precision = 10, scale = 2)
    private BigDecimal pesoAtual;

    @Column(name = "treinos_realizados", nullable = false)
    private Integer treinosRealizados = 0;

    @Column(name = "sequencia_atual", nullable = false)
    private Integer sequenciaAtual = 0;

    @Column(name = "melhor_sequencia", nullable = false)
    private Integer melhorSequencia = 0;

    @Column(name = "dias_treinados_total", nullable = false)
    private Integer diasTreinadosTotal = 0;

    @Column(name = "treinos_sem_vontade_total", nullable = false)
    private Integer treinosSemVontadeTotal = 0;

    @Column(name = "progressoes_total", nullable = false)
    private Integer progressoesTotal = 0;

    @Column(name = "dias_alimentacao_alinhada", nullable = false)
    private Integer diasAlimentacaoAlinhada = 0;

    @Column(name = "retornos_apos_pausa_total", nullable = false)
    private Integer retornosAposPausaTotal = 0;

    @Column(name = "niveis_subidos_total", nullable = false)
    private Integer niveisSubidosTotal = 0;

    @Column(name = "xp_total", nullable = false)
    private Integer xpTotal = 0;

    @Column(name = "nivel_atual", nullable = false)
    private Integer nivelAtual = 1;

    @Enumerated(EnumType.STRING)
    @Column(name = "tier_atual", nullable = false, length = 20)
    private TierRanking tierAtual = TierRanking.BRONZE;

    @Column(name = "ultima_data_treino")
    private LocalDate ultimaDataTreino;

    @Column(name = "ultima_data_registro")
    private LocalDate ultimaDataRegistro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public BigDecimal getPesoInicial() {
        return pesoInicial;
    }

    public void setPesoInicial(BigDecimal pesoInicial) {
        this.pesoInicial = pesoInicial;
    }

    public BigDecimal getPesoAtual() {
        return pesoAtual;
    }

    public void setPesoAtual(BigDecimal pesoAtual) {
        this.pesoAtual = pesoAtual;
    }

    public Integer getTreinosRealizados() {
        return treinosRealizados;
    }

    public void setTreinosRealizados(Integer treinosRealizados) {
        this.treinosRealizados = treinosRealizados;
    }

    public Integer getSequenciaAtual() {
        return sequenciaAtual;
    }

    public void setSequenciaAtual(Integer sequenciaAtual) {
        this.sequenciaAtual = sequenciaAtual;
    }

    public Integer getMelhorSequencia() {
        return melhorSequencia;
    }

    public void setMelhorSequencia(Integer melhorSequencia) {
        this.melhorSequencia = melhorSequencia;
    }

    public Integer getDiasTreinadosTotal() {
        return diasTreinadosTotal;
    }

    public void setDiasTreinadosTotal(Integer diasTreinadosTotal) {
        this.diasTreinadosTotal = diasTreinadosTotal;
    }

    public Integer getTreinosSemVontadeTotal() {
        return treinosSemVontadeTotal;
    }

    public void setTreinosSemVontadeTotal(Integer treinosSemVontadeTotal) {
        this.treinosSemVontadeTotal = treinosSemVontadeTotal;
    }

    public Integer getProgressoesTotal() {
        return progressoesTotal;
    }

    public void setProgressoesTotal(Integer progressoesTotal) {
        this.progressoesTotal = progressoesTotal;
    }

    public Integer getDiasAlimentacaoAlinhada() {
        return diasAlimentacaoAlinhada;
    }

    public void setDiasAlimentacaoAlinhada(Integer diasAlimentacaoAlinhada) {
        this.diasAlimentacaoAlinhada = diasAlimentacaoAlinhada;
    }

    public Integer getRetornosAposPausaTotal() {
        return retornosAposPausaTotal;
    }

    public void setRetornosAposPausaTotal(Integer retornosAposPausaTotal) {
        this.retornosAposPausaTotal = retornosAposPausaTotal;
    }

    public Integer getNiveisSubidosTotal() {
        return niveisSubidosTotal;
    }

    public void setNiveisSubidosTotal(Integer niveisSubidosTotal) {
        this.niveisSubidosTotal = niveisSubidosTotal;
    }

    public Integer getXpTotal() {
        return xpTotal;
    }

    public void setXpTotal(Integer xpTotal) {
        this.xpTotal = xpTotal;
    }

    public Integer getNivelAtual() {
        return nivelAtual;
    }

    public void setNivelAtual(Integer nivelAtual) {
        this.nivelAtual = nivelAtual;
    }

    public TierRanking getTierAtual() {
        return tierAtual;
    }

    public void setTierAtual(TierRanking tierAtual) {
        this.tierAtual = tierAtual;
    }

    public LocalDate getUltimaDataTreino() {
        return ultimaDataTreino;
    }

    public void setUltimaDataTreino(LocalDate ultimaDataTreino) {
        this.ultimaDataTreino = ultimaDataTreino;
    }

    public LocalDate getUltimaDataRegistro() {
        return ultimaDataRegistro;
    }

    public void setUltimaDataRegistro(LocalDate ultimaDataRegistro) {
        this.ultimaDataRegistro = ultimaDataRegistro;
    }
}
