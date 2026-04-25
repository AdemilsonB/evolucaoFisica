package br.com.evolucao.evolucaoFisica.entity;

import br.com.evolucao.evolucaoFisica.enumeration.MotivacaoRegistro;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "registros_diarios",
        uniqueConstraints = @UniqueConstraint(name = "uk_registro_diario_usuario_data", columnNames = {"usuario_id", "data_referencia"})
)
public class RegistroDiario extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_alimentar_id")
    private PlanoAlimentar planoAlimentar;

    @Column(name = "data_referencia", nullable = false)
    private LocalDate dataReferencia;

    @Column(name = "realizou_treino", nullable = false)
    private Boolean realizouTreino = Boolean.FALSE;

    @Column(name = "tipo_treino", length = 120)
    private String tipoTreino;

    @Column(precision = 10, scale = 2)
    private BigDecimal peso;

    @Column(name = "proteina_consumida", precision = 10, scale = 2)
    private BigDecimal proteinaConsumida;

    @Column(name = "bateu_proteina", nullable = false)
    private Boolean bateuProteina = Boolean.FALSE;

    @Column(name = "alimentacao_alinhada", nullable = false)
    private Boolean alimentacaoAlinhada = Boolean.FALSE;

    @Column(name = "horas_sono", precision = 4, scale = 2)
    private BigDecimal horasSono;

    @Column(name = "houve_progressao", nullable = false)
    private Boolean houveProgressao = Boolean.FALSE;

    @Column(name = "descricao_progressao", length = 500)
    private String descricaoProgressao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MotivacaoRegistro motivacao = MotivacaoRegistro.MEDIA;

    @Column(length = 1000)
    private String observacao;

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

    public LocalDate getDataReferencia() {
        return dataReferencia;
    }

    public void setDataReferencia(LocalDate dataReferencia) {
        this.dataReferencia = dataReferencia;
    }

    public PlanoAlimentar getPlanoAlimentar() {
        return planoAlimentar;
    }

    public void setPlanoAlimentar(PlanoAlimentar planoAlimentar) {
        this.planoAlimentar = planoAlimentar;
    }

    public Boolean getRealizouTreino() {
        return realizouTreino;
    }

    public void setRealizouTreino(Boolean realizouTreino) {
        this.realizouTreino = realizouTreino;
    }

    public String getTipoTreino() {
        return tipoTreino;
    }

    public void setTipoTreino(String tipoTreino) {
        this.tipoTreino = tipoTreino;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getProteinaConsumida() {
        return proteinaConsumida;
    }

    public void setProteinaConsumida(BigDecimal proteinaConsumida) {
        this.proteinaConsumida = proteinaConsumida;
    }

    public Boolean getBateuProteina() {
        return bateuProteina;
    }

    public void setBateuProteina(Boolean bateuProteina) {
        this.bateuProteina = bateuProteina;
    }

    public Boolean getAlimentacaoAlinhada() {
        return alimentacaoAlinhada;
    }

    public void setAlimentacaoAlinhada(Boolean alimentacaoAlinhada) {
        this.alimentacaoAlinhada = alimentacaoAlinhada;
    }

    public BigDecimal getHorasSono() {
        return horasSono;
    }

    public void setHorasSono(BigDecimal horasSono) {
        this.horasSono = horasSono;
    }

    public Boolean getHouveProgressao() {
        return houveProgressao;
    }

    public void setHouveProgressao(Boolean houveProgressao) {
        this.houveProgressao = houveProgressao;
    }

    public String getDescricaoProgressao() {
        return descricaoProgressao;
    }

    public void setDescricaoProgressao(String descricaoProgressao) {
        this.descricaoProgressao = descricaoProgressao;
    }

    public MotivacaoRegistro getMotivacao() {
        return motivacao;
    }

    public void setMotivacao(MotivacaoRegistro motivacao) {
        this.motivacao = motivacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
