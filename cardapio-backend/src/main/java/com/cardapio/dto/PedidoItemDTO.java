package com.cardapio.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PedidoItemDTO {

    private Long id;

    @NotNull(message = "ID do item é obrigatório")
    private Long itemId;

    private String nomeItem;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
    private Integer quantidade;

    @NotNull(message = "Preço unitário é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço unitário deve ser maior que zero")
    private BigDecimal precoUnitario;

    private String observacoesItem;

    private BigDecimal subtotal;

    // Construtores
    public PedidoItemDTO() {
    }

    public PedidoItemDTO(Long itemId, Integer quantidade, BigDecimal precoUnitario) {
        this.itemId = itemId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = calcularSubtotal();
    }

    public PedidoItemDTO(Long id, Long itemId, String nomeItem, Integer quantidade, BigDecimal precoUnitario,
            String observacoesItem) {
        this.id = id;
        this.itemId = itemId;
        this.nomeItem = nomeItem;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.observacoesItem = observacoesItem;
        this.subtotal = calcularSubtotal();
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getNomeItem() {
        return nomeItem;
    }

    public void setNomeItem(String nomeItem) {
        this.nomeItem = nomeItem;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
        if (this.precoUnitario != null) {
            this.subtotal = calcularSubtotal();
        }
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
        if (this.quantidade != null) {
            this.subtotal = calcularSubtotal();
        }
    }

    public String getObservacoesItem() {
        return observacoesItem;
    }

    public void setObservacoesItem(String observacoesItem) {
        this.observacoesItem = observacoesItem;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    private BigDecimal calcularSubtotal() {
        if (precoUnitario == null || quantidade == null) {
            return BigDecimal.ZERO;
        }
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}
