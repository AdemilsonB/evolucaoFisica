package br.com.evolucao.evolucaoFisica.entity;

import br.com.evolucao.evolucaoFisica.enumeration.ProvedorAutenticacao;
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

import java.time.LocalDateTime;

@Entity
@Table(
        name = "usuarios_identidades_externas",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_provedor_identificador_externo",
                columnNames = {"provedor", "identificador_externo"}
        )
)
public class UsuarioIdentidadeExterna extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProvedorAutenticacao provedor;

    @Column(name = "identificador_externo", nullable = false, length = 150)
    private String identificadorExterno;

    @Column(name = "email_externo", length = 150)
    private String emailExterno;

    @Column(name = "nome_exibicao", length = 120)
    private String nomeExibicao;

    @Column(name = "foto_perfil_url", length = 500)
    private String fotoPerfilUrl;

    @Column(name = "ultimo_login_em")
    private LocalDateTime ultimoLoginEm;

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

    public ProvedorAutenticacao getProvedor() {
        return provedor;
    }

    public void setProvedor(ProvedorAutenticacao provedor) {
        this.provedor = provedor;
    }

    public String getIdentificadorExterno() {
        return identificadorExterno;
    }

    public void setIdentificadorExterno(String identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public String getEmailExterno() {
        return emailExterno;
    }

    public void setEmailExterno(String emailExterno) {
        this.emailExterno = emailExterno;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }

    public void setNomeExibicao(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    public String getFotoPerfilUrl() {
        return fotoPerfilUrl;
    }

    public void setFotoPerfilUrl(String fotoPerfilUrl) {
        this.fotoPerfilUrl = fotoPerfilUrl;
    }

    public LocalDateTime getUltimoLoginEm() {
        return ultimoLoginEm;
    }

    public void setUltimoLoginEm(LocalDateTime ultimoLoginEm) {
        this.ultimoLoginEm = ultimoLoginEm;
    }
}
