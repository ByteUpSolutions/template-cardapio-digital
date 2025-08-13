package com.cardapio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 20, message = "Número da mesa deve ter no máximo 20 caracteres")
    @Column(length = 20)
    private String mesa; // Pode ser null para pedidos de adega

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PedidoItem> itens = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PedidoStatus status = PedidoStatus.RECEBIDO;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    // Construtores
    public Pedido() {
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Pedido(String mesa, String observacoes) {
        this();
        this.mesa = mesa;
        this.observacoes = observacoes;
    }

    // Métodos auxiliares
    public BigDecimal getValorTotal() {
        if (itens == null) {
            return BigDecimal.ZERO;
        }
        return itens.stream()
                // 1. Mapeia cada PedidoItem para o seu subtotal (que é BigDecimal)
                .map(PedidoItem::getSubtotal)
                // 2. Soma todos os BigDecimals do stream
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void adicionarItem(PedidoItem item) {
        item.setPedido(this);
        this.itens.add(item);
    }

    @PreUpdate
    public void preUpdate() {
        this.dataAtualizacao = LocalDateTime.now();
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

    public List<PedidoItem> getItens() {
        return itens;
    }

    public void setItens(List<PedidoItem> itens) {
        this.itens = itens;
        // Garantir que a referência bidirecional esteja correta
        if (itens != null) {
            itens.forEach(item -> item.setPedido(this));
        }
    }

    public PedidoStatus getStatus() {
        return status;
    }

    public void setStatus(PedidoStatus status) {
        this.status = status;
        this.dataAtualizacao = LocalDateTime.now();
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
}
