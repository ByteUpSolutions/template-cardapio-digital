package com.cardapio.dto;

import com.cardapio.model.PedidoStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoDTO {

    private Long id;

    @Size(max = 20, message = "Número da mesa deve ter no máximo 20 caracteres")
    private String mesa;

    @NotEmpty(message = "Pedido deve ter pelo menos um item")
    @Valid
    private List<PedidoItemDTO> itens;

    private PedidoStatus status;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String observacoes;

    private BigDecimal valorTotal;

    // Construtores
    public PedidoDTO() {
    }

    public PedidoDTO(String mesa, List<PedidoItemDTO> itens, String observacoes) {
        this.mesa = mesa;
        this.itens = itens;
        this.observacoes = observacoes;
        this.status = PedidoStatus.RECEBIDO;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
        this.calcularValorTotal();
    }

    public PedidoDTO(Long id, String mesa, List<PedidoItemDTO> itens, PedidoStatus status,
            LocalDateTime dataCriacao, LocalDateTime dataAtualizacao, String observacoes) {
        this.id = id;
        this.mesa = mesa;
        this.itens = itens;
        this.status = status;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
        this.observacoes = observacoes;
        this.calcularValorTotal();
    }

    // Métodos auxiliares
    public void calcularValorTotal() {
        if (itens != null && !itens.isEmpty()) {
            this.valorTotal = itens.stream()
                    // 1. Mapeia cada item para seu subtotal (que é um BigDecimal)
                    .map(item -> item.getSubtotal()) // Supondo que PedidoItemDTO agora tem um getSubtotal() que retorna
                                                     // BigDecimal
                    // 2. Soma todos os BigDecimals usando reduce
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            // Use BigDecimal.ZERO para representar zero
            this.valorTotal = BigDecimal.ZERO;
        }
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public List<PedidoItemDTO> getItens() {
        return itens;
    }

    public void setItens(List<PedidoItemDTO> itens) {
        this.itens = itens;
        this.calcularValorTotal();
    }

    public PedidoStatus getStatus() {
        return status;
    }

    public void setStatus(PedidoStatus status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }
}
