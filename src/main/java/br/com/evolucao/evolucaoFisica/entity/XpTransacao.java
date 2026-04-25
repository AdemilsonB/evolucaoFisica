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

@Entity
@Table(
        name = "xp_transacoes",
        uniqueConstraints = @UniqueConstraint(name = "uk_xp_usuario_referencia", columnNames = {"usuario_id", "referencia_unica"})
)
public class XpTransacao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regra_id")
    private XpRegra regra;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registro_diario_id")
    private RegistroDiario registroDiario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registro_treino_id")
    private RegistroTreino registroTreino;

    @Column(nullable = false)
    private Integer valor;

    @Column(nullable = false, length = 150)
    private String descricao;

    @Column(name = "referencia_unica", nullable = false, length = 150)
    private String referenciaUnica;

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

    public XpRegra getRegra() {
        return regra;
    }

    public void setRegra(XpRegra regra) {
        this.regra = regra;
    }

    public RegistroDiario getRegistroDiario() {
        return registroDiario;
    }

    public void setRegistroDiario(RegistroDiario registroDiario) {
        this.registroDiario = registroDiario;
    }

    public RegistroTreino getRegistroTreino() {
        return registroTreino;
    }

    public void setRegistroTreino(RegistroTreino registroTreino) {
        this.registroTreino = registroTreino;
    }

    public Integer getValor() {
        return valor;
    }

    public void setValor(Integer valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getReferenciaUnica() {
        return referenciaUnica;
    }

    public void setReferenciaUnica(String referenciaUnica) {
        this.referenciaUnica = referenciaUnica;
    }
}
