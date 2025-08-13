package com.cardapio.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pedido_itens")
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_cardapio_id", nullable = false)
    private ItemCardapio item;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull(message = "Preço unitário é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço unitário deve ser maior que zero")
    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(name = "observacoes_item", length = 200)
    private String observacoesItem;

    // Construtores
    public PedidoItem() {
    }

    public PedidoItem(ItemCardapio item, Integer quantidade, BigDecimal precoUnitario) {
        this.item = item;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public PedidoItem(ItemCardapio item, Integer quantidade, BigDecimal precoUnitario, String observacoesItem) {
        this(item, quantidade, precoUnitario);
        this.observacoesItem = observacoesItem;
    }

    // Métodos auxiliares
    public BigDecimal getSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public ItemCardapio getItem() {
        return item;
    }

    public void setItem(ItemCardapio item) {
        this.item = item;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public String getObservacoesItem() {
        return observacoesItem;
    }

    public void setObservacoesItem(String observacoesItem) {
        this.observacoesItem = observacoesItem;
    }
}
