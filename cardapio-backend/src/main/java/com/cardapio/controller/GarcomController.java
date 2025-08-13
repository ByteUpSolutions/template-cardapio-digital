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
@RequestMapping("/garcom")
@PreAuthorize("hasRole('GARCOM')")
@CrossOrigin(origins = "*")
public class GarcomController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @GetMapping("/pedidos/stream")
    public SseEmitter receberNotificacoesStream() {
        return pedidoService.criarConexaoGarcom();
    }
    
    @GetMapping("/pedidos/prontos")
    public ResponseEntity<List<PedidoDTO>> listarPedidosProntos() {
        List<PedidoDTO> pedidos = pedidoService.listarPedidosPorStatus(PedidoStatus.PRONTO);
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/pedidos/{id}")
    public ResponseEntity<PedidoDTO> buscarPedidoPorId(@PathVariable Long id) {
        PedidoDTO pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }
    
    @PatchMapping("/pedidos/{id}/entregar")
    public ResponseEntity<PedidoDTO> marcarComoEntregue(@PathVariable Long id) {
        StatusUpdateRequest statusRequest = new StatusUpdateRequest(PedidoStatus.ENTREGUE);
        PedidoDTO pedidoAtualizado = pedidoService.atualizarStatusPedido(id, statusRequest);
        return ResponseEntity.ok(pedidoAtualizado);
    }
    
    @GetMapping("/pedidos/mesa/{mesa}")
    public ResponseEntity<List<PedidoDTO>> listarPedidosPorMesa(@PathVariable String mesa) {
        List<PedidoDTO> pedidos = pedidoService.listarPedidosPorMesa(mesa);
        return ResponseEntity.ok(pedidos);
    }
}

