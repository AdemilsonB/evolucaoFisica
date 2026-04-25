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
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "usuarios_missoes_semanais",
        uniqueConstraints = @UniqueConstraint(name = "uk_usuario_missao_semana", columnNames = {"usuario_id", "missao_id", "semana_referencia"})
)
public class UsuarioMissaoSemanal extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "missao_id", nullable = false)
    private MissaoSemanal missao;

    @Column(name = "semana_referencia", nullable = false)
    private LocalDate semanaReferencia;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal progresso = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean concluida = Boolean.FALSE;

    @Column(name = "total_conclusoes", nullable = false)
    private Integer totalConclusoes = 0;

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

    public MissaoSemanal getMissao() {
        return missao;
    }

    public void setMissao(MissaoSemanal missao) {
        this.missao = missao;
    }

    public LocalDate getSemanaReferencia() {
        return semanaReferencia;
    }

    public void setSemanaReferencia(LocalDate semanaReferencia) {
        this.semanaReferencia = semanaReferencia;
    }

    public BigDecimal getProgresso() {
        return progresso;
    }

    public void setProgresso(BigDecimal progresso) {
        this.progresso = progresso;
    }

    public Boolean getConcluida() {
        return concluida;
    }

    public void setConcluida(Boolean concluida) {
        this.concluida = concluida;
    }

    public Integer getTotalConclusoes() {
        return totalConclusoes;
    }

    public void setTotalConclusoes(Integer totalConclusoes) {
        this.totalConclusoes = totalConclusoes;
    }
}
