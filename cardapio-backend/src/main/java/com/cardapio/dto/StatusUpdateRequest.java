package com.cardapio.dto;

import com.cardapio.model.PedidoStatus;
import jakarta.validation.constraints.NotNull;

public class StatusUpdateRequest {
    
    @NotNull(message = "Status é obrigatório")
    private PedidoStatus status;
    
    // Construtores
    public StatusUpdateRequest() {}
    
    public StatusUpdateRequest(PedidoStatus status) {
        this.status = status;
    }
    
    // Getters e Setters
    public PedidoStatus getStatus() {
        return status;
    }
    
    public void setStatus(PedidoStatus status) {
        this.status = status;
    }
}

