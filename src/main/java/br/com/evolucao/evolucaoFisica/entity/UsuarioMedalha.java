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

import java.time.LocalDateTime;

@Entity
@Table(
        name = "usuarios_medalhas",
        uniqueConstraints = @UniqueConstraint(name = "uk_usuario_medalha", columnNames = {"usuario_id", "medalha_id"})
)
public class UsuarioMedalha extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medalha_id", nullable = false)
    private Medalha medalha;

    @Column(nullable = false)
    private Integer quantidade = 0;

    @Column(name = "ultima_conquista_em")
    private LocalDateTime ultimaConquistaEm;

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

    public Medalha getMedalha() {
        return medalha;
    }

    public void setMedalha(Medalha medalha) {
        this.medalha = medalha;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDateTime getUltimaConquistaEm() {
        return ultimaConquistaEm;
    }

    public void setUltimaConquistaEm(LocalDateTime ultimaConquistaEm) {
        this.ultimaConquistaEm = ultimaConquistaEm;
    }
}
