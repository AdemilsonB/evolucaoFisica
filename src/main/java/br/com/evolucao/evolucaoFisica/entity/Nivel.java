package br.com.evolucao.evolucaoFisica.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "niveis", uniqueConstraints = @UniqueConstraint(name = "uk_nivel_numero", columnNames = "numero"))
public class Nivel extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer numero;

    @Column(name = "xp_necessario", nullable = false)
    private Integer xpNecessario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getXpNecessario() {
        return xpNecessario;
    }

    public void setXpNecessario(Integer xpNecessario) {
        this.xpNecessario = xpNecessario;
    }
}
