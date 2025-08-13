package com.cardapio.controller;

import com.cardapio.dto.ItemCardapioDTO;
import com.cardapio.dto.PedidoDTO;
import com.cardapio.service.MenuService;
import com.cardapio.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping
@CrossOrigin(origins = "*")
public class ClienteController {
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private PedidoService pedidoService;
    
    @GetMapping("/cardapio")
    public ResponseEntity<List<ItemCardapioDTO>> listarCardapio() {
        List<ItemCardapioDTO> cardapio = menuService.listarCardapioAtivo();
        return ResponseEntity.ok(cardapio);
    }
    
    @GetMapping("/cardapio/buscar")
    public ResponseEntity<List<ItemCardapioDTO>> buscarItens(@RequestParam String nome) {
        List<ItemCardapioDTO> itens = menuService.buscarPorNome(nome);
        return ResponseEntity.ok(itens);
    }
    
    @PostMapping("/pedidos")
    public ResponseEntity<PedidoDTO> criarPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
        PedidoDTO pedidoCriado = pedidoService.criarPedido(pedidoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoCriado);
    }
    
    @GetMapping("/pedidos/{id}")
    public ResponseEntity<PedidoDTO> consultarStatusPedido(@PathVariable Long id) {
        PedidoDTO pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }
    
    @GetMapping("/pedidos/mesa/{mesa}")
    public ResponseEntity<List<PedidoDTO>> listarPedidosPorMesa(@PathVariable String mesa) {
        List<PedidoDTO> pedidos = pedidoService.listarPedidosPorMesa(mesa);
        return ResponseEntity.ok(pedidos);
    }
}

