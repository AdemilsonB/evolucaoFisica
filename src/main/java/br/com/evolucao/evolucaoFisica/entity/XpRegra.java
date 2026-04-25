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
@Table(name = "xp_regras")
public class XpRegra extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_regra", nullable = false, unique = true, length = 50)
    private TipoRegraGamificacao tipoRegra;

    @Column(name = "xp_concedido", nullable = false)
    private Integer xpConcedido;

    @Column(name = "percentual_bonus", precision = 8, scale = 2)
    private BigDecimal percentualBonus;

    @Column(nullable = false)
    private Boolean ativo = Boolean.TRUE;

    @Column(length = 300)
    private String descricao;

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

    public TipoRegraGamificacao getTipoRegra() {
        return tipoRegra;
    }

    public void setTipoRegra(TipoRegraGamificacao tipoRegra) {
        this.tipoRegra = tipoRegra;
    }

    public Integer getXpConcedido() {
        return xpConcedido;
    }

    public void setXpConcedido(Integer xpConcedido) {
        this.xpConcedido = xpConcedido;
    }

    public BigDecimal getPercentualBonus() {
        return percentualBonus;
    }

    public void setPercentualBonus(BigDecimal percentualBonus) {
        this.percentualBonus = percentualBonus;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
