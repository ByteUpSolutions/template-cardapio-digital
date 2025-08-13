package com.cardapio.controller;

import com.cardapio.dto.PedidoDTO;
import com.cardapio.dto.StatusUpdateRequest;
import com.cardapio.model.PedidoStatus;
import com.cardapio.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;

@RestController
@RequestMapping("/cozinha")
@PreAuthorize("hasRole('COZINHA')")
@CrossOrigin(origins = "*")
public class CozinhaController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @GetMapping("/pedidos/stream")
    public SseEmitter receberPedidosStream() {
        return pedidoService.criarConexaoCozinha();
    }
    
    @GetMapping("/pedidos/novos")
    public ResponseEntity<List<PedidoDTO>> listarPedidosNovos() {
        List<PedidoDTO> pedidos = pedidoService.listarPedidosPorStatus(PedidoStatus.RECEBIDO);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/pedidos/em-preparo")
    public ResponseEntity<List<PedidoDTO>> listarPedidosEmPreparo() {
        List<PedidoDTO> pedidos = pedidoService.listarPedidosPorStatus(PedidoStatus.EM_PREPARO);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/pedidos/{id}")
    public ResponseEntity<PedidoDTO> buscarPedidoPorId(@PathVariable Long id) {
        PedidoDTO pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }
    
    @PatchMapping("/pedidos/{id}/status")
    public ResponseEntity<PedidoDTO> atualizarStatusPedido(@PathVariable Long id, 
                                                           @Valid @RequestBody StatusUpdateRequest statusRequest) {
        PedidoDTO pedidoAtualizado = pedidoService.atualizarStatusPedido(id, statusRequest);
        return ResponseEntity.ok(pedidoAtualizado);
    }
    
    @PatchMapping("/pedidos/{id}/iniciar-preparo")
    public ResponseEntity<PedidoDTO> iniciarPreparo(@PathVariable Long id) {
        StatusUpdateRequest statusRequest = new StatusUpdateRequest(PedidoStatus.EM_PREPARO);
        PedidoDTO pedidoAtualizado = pedidoService.atualizarStatusPedido(id, statusRequest);
        return ResponseEntity.ok(pedidoAtualizado);
    }
    
    @PatchMapping("/pedidos/{id}/finalizar")
    public ResponseEntity<PedidoDTO> finalizarPedido(@PathVariable Long id) {
        StatusUpdateRequest statusRequest = new StatusUpdateRequest(PedidoStatus.PRONTO);
        PedidoDTO pedidoAtualizado = pedidoService.atualizarStatusPedido(id, statusRequest);
        return ResponseEntity.ok(pedidoAtualizado);
    }
}

