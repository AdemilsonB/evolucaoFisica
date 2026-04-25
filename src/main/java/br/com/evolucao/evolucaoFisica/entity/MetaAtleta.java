package br.com.evolucao.evolucaoFisica.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "metas_atletas")
public class MetaAtleta extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "frequencia_semanal_meta")
    private Integer frequenciaSemanalMeta;

    @Column(name = "proteina_diaria_meta", precision = 10, scale = 2)
    private BigDecimal proteinaDiariaMeta;

    @Column(name = "caloria_diaria_meta", precision = 10, scale = 2)
    private BigDecimal caloriaDiariaMeta;

    @Column(length = 300)
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

    public Integer getFrequenciaSemanalMeta() {
        return frequenciaSemanalMeta;
    }

    public void setFrequenciaSemanalMeta(Integer frequenciaSemanalMeta) {
        this.frequenciaSemanalMeta = frequenciaSemanalMeta;
    }

    public BigDecimal getProteinaDiariaMeta() {
        return proteinaDiariaMeta;
    }

    public void setProteinaDiariaMeta(BigDecimal proteinaDiariaMeta) {
        this.proteinaDiariaMeta = proteinaDiariaMeta;
    }

    public BigDecimal getCaloriaDiariaMeta() {
        return caloriaDiariaMeta;
    }

    public void setCaloriaDiariaMeta(BigDecimal caloriaDiariaMeta) {
        this.caloriaDiariaMeta = caloriaDiariaMeta;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
