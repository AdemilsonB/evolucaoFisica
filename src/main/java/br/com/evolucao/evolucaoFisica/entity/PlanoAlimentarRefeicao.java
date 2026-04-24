package br.com.evolucao.evolucaoFisica.entity;

import br.com.evolucao.evolucaoFisica.enumeration.TipoRefeicao;
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

import java.time.LocalTime;

@Entity
@Table(name = "planos_alimentares_refeicoes")
public class PlanoAlimentarRefeicao extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plano_dia_id", nullable = false)
    private PlanoAlimentarDia planoAlimentarDia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoRefeicao tipoRefeicao;

    @Column(name = "horario_sugerido")
    private LocalTime horarioSugerido;

    @Column(length = 500)
    private String observacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlanoAlimentarDia getPlanoAlimentarDia() {
        return planoAlimentarDia;
    }

    public void setPlanoAlimentarDia(PlanoAlimentarDia planoAlimentarDia) {
        this.planoAlimentarDia = planoAlimentarDia;
    }

    public TipoRefeicao getTipoRefeicao() {
        return tipoRefeicao;
    }

    public void setTipoRefeicao(TipoRefeicao tipoRefeicao) {
        this.tipoRefeicao = tipoRefeicao;
    }

    public LocalTime getHorarioSugerido() {
        return horarioSugerido;
    }

    public void setHorarioSugerido(LocalTime horarioSugerido) {
        this.horarioSugerido = horarioSugerido;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
