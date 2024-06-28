package com.m2g2.mobiauto_backend_interview.model;

import com.m2g2.mobiauto_backend_interview.enums.StatusOportunidade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class Oportunidade  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private StatusOportunidade statusOportunidade;

    @ManyToOne
    @JoinColumn(name = "revenda_id")
    private Revenda revenda;

    @NotNull(message = "Campo 'veículo' precisa ser informado.")
    @OneToOne(cascade = CascadeType.PERSIST)
    private Veiculo veiculo;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    private LocalDate dataInicio;

    private LocalDate dataFim;

    private LocalDate dataUltimaTransferencia;

    private String motivoConclusao;

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

    public StatusOportunidade getStatusOportunidade() {
        return statusOportunidade;
    }

    public void setStatusOportunidade(StatusOportunidade statusOportunidade) {
        this.statusOportunidade = statusOportunidade;
    }

    public Revenda getRevenda() {
        return revenda;
    }

    public void setRevenda(Revenda revenda) {
        this.revenda = revenda;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public LocalDate getDataUltimaTransferencia() {
        return dataUltimaTransferencia;
    }

    public void setDataUltimaTransferencia(LocalDate dataUltimaTransferencia) {
        this.dataUltimaTransferencia = dataUltimaTransferencia;
    }

    public String getMotivoConclusao() {
        return motivoConclusao;
    }

    public void setMotivoConclusao(String motivoConclusao) {
        this.motivoConclusao = motivoConclusao;
    }

    @Override
    public String toString() {
        return "Oportunidade{" +
                "id=" + id +
                ", usuario=" + usuario +
                ", statusOportunidade=" + statusOportunidade +
                ", revenda=" + revenda +
                ", veiculo=" + veiculo +
                ", cliente=" + cliente +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", dataUltimaTransferencia=" + dataUltimaTransferencia +
                ", motivoConclusao='" + motivoConclusao + '\'' +
                '}';
    }
}
