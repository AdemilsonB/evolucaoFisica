package br.com.evolucao.evolucaoFisica.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "alimentos")
public class Alimento extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal calorias;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal proteina;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal carboidrato;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal gordura;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal acucares;

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

    public BigDecimal getCalorias() {
        return calorias;
    }

    public void setCalorias(BigDecimal calorias) {
        this.calorias = calorias;
    }

    public BigDecimal getProteina() {
        return proteina;
    }

    public void setProteina(BigDecimal proteina) {
        this.proteina = proteina;
    }

    public BigDecimal getCarboidrato() {
        return carboidrato;
    }

    public void setCarboidrato(BigDecimal carboidrato) {
        this.carboidrato = carboidrato;
    }

    public BigDecimal getGordura() {
        return gordura;
    }

    public void setGordura(BigDecimal gordura) {
        this.gordura = gordura;
    }

    public BigDecimal getAcucares() {
        return acucares;
    }

    public void setAcucares(BigDecimal acucares) {
        this.acucares = acucares;
    }
}
