package br.com.evolucao.evolucaoFisica.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "registro_exercicios")
public class RegistroExercicio extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "registro_treino_id", nullable = false)
    private RegistroTreino registroTreino;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercicio_id", nullable = false)
    private Exercicio exercicio;

    @Column(name = "carga_real", precision = 10, scale = 2)
    private BigDecimal cargaReal;

    @Column(name = "repeticoes_real", nullable = false)
    private Integer repeticoesReal;

    @Column(nullable = false)
    private boolean concluido;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegistroTreino getRegistroTreino() {
        return registroTreino;
    }

    public void setRegistroTreino(RegistroTreino registroTreino) {
        this.registroTreino = registroTreino;
    }

    public Exercicio getExercicio() {
        return exercicio;
    }

    public void setExercicio(Exercicio exercicio) {
        this.exercicio = exercicio;
    }

    public BigDecimal getCargaReal() {
        return cargaReal;
    }

    public void setCargaReal(BigDecimal cargaReal) {
        this.cargaReal = cargaReal;
    }

    public Integer getRepeticoesReal() {
        return repeticoesReal;
    }

    public void setRepeticoesReal(Integer repeticoesReal) {
        this.repeticoesReal = repeticoesReal;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }
}
