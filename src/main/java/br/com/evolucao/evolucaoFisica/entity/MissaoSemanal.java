package br.com.evolucao.evolucaoFisica.entity;

import br.com.evolucao.evolucaoFisica.enumeration.TipoRegraGamificacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "missoes_semanais")
public class MissaoSemanal extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_regra", nullable = false, length = 50)
    private TipoRegraGamificacao tipoRegra;

    @Column(name = "meta_valor", nullable = false, precision = 12, scale = 2)
    private BigDecimal metaValor;

    @Column(name = "xp_recompensa", nullable = false)
    private Integer xpRecompensa;

    @Column(nullable = false)
    private Boolean ativo = Boolean.TRUE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoRegraGamificacao getTipoRegra() {
        return tipoRegra;
    }

    public void setTipoRegra(TipoRegraGamificacao tipoRegra) {
        this.tipoRegra = tipoRegra;
    }

    public BigDecimal getMetaValor() {
        return metaValor;
    }

    public void setMetaValor(BigDecimal metaValor) {
        this.metaValor = metaValor;
    }

    public Integer getXpRecompensa() {
        return xpRecompensa;
    }

    public void setXpRecompensa(Integer xpRecompensa) {
        this.xpRecompensa = xpRecompensa;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
