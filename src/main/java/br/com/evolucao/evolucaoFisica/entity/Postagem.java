package br.com.evolucao.evolucaoFisica.entity;

import br.com.evolucao.evolucaoFisica.enumeration.TipoPostagem;
import br.com.evolucao.evolucaoFisica.enumeration.VisibilidadeConteudo;
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

@Entity
@Table(name = "postagens")
public class Postagem extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoPostagem tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VisibilidadeConteudo visibilidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registro_treino_id")
    private RegistroTreino registroTreino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evolucao_fisica_id")
    private EvolucaoFisica evolucaoFisica;

    @Column(length = 2000)
    private String conteudo;

    @Column(name = "midia_url", length = 500)
    private String midiaUrl;

    @Column(nullable = false)
    private Boolean ativo = Boolean.TRUE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public TipoPostagem getTipo() {
        return tipo;
    }

    public void setTipo(TipoPostagem tipo) {
        this.tipo = tipo;
    }

    public VisibilidadeConteudo getVisibilidade() {
        return visibilidade;
    }

    public void setVisibilidade(VisibilidadeConteudo visibilidade) {
        this.visibilidade = visibilidade;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public RegistroTreino getRegistroTreino() {
        return registroTreino;
    }

    public void setRegistroTreino(RegistroTreino registroTreino) {
        this.registroTreino = registroTreino;
    }

    public EvolucaoFisica getEvolucaoFisica() {
        return evolucaoFisica;
    }

    public void setEvolucaoFisica(EvolucaoFisica evolucaoFisica) {
        this.evolucaoFisica = evolucaoFisica;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getMidiaUrl() {
        return midiaUrl;
    }

    public void setMidiaUrl(String midiaUrl) {
        this.midiaUrl = midiaUrl;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
