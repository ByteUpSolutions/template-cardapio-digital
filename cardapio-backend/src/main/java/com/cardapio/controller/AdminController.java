package com.cardapio.controller;

import com.cardapio.dto.ItemCardapioDTO;
import com.cardapio.dto.PedidoDTO;
import com.cardapio.service.MenuService;
import com.cardapio.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "*")
public class AdminController {
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private PedidoService pedidoService;
    
    // Gerenciamento do cardápio
    @GetMapping("/cardapio")
    public ResponseEntity<List<ItemCardapioDTO>> listarTodosItens() {
        List<ItemCardapioDTO> itens = menuService.listarTodosItens();
        return ResponseEntity.ok(itens);
    }
    
    @GetMapping("/cardapio/{id}")
    public ResponseEntity<ItemCardapioDTO> buscarItemPorId(@PathVariable Long id) {
        ItemCardapioDTO item = menuService.buscarPorId(id);
        return ResponseEntity.ok(item);
    }
    
    @PostMapping("/cardapio")
    public ResponseEntity<ItemCardapioDTO> adicionarItem(@Valid @RequestBody ItemCardapioDTO itemDTO) {
        ItemCardapioDTO itemCriado = menuService.adicionarItem(itemDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemCriado);
    }
    
    @PutMapping("/cardapio/{id}")
    public ResponseEntity<ItemCardapioDTO> atualizarItem(@PathVariable Long id, 
                                                         @Valid @RequestBody ItemCardapioDTO itemDTO) {
        ItemCardapioDTO itemAtualizado = menuService.atualizarItem(id, itemDTO);
        return ResponseEntity.ok(itemAtualizado);
    }
    
    @DeleteMapping("/cardapio/{id}")
    public ResponseEntity<Void> removerItem(@PathVariable Long id) {
        menuService.removerItem(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/cardapio/{id}/ativar")
    public ResponseEntity<Void> ativarItem(@PathVariable Long id) {
        menuService.ativarItem(id);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/cardapio/{id}/permanente")
    public ResponseEntity<Void> deletarItemPermanentemente(@PathVariable Long id) {
        menuService.deletarItemPermanentemente(id);
        return ResponseEntity.noContent().build();
    }
    
    // Gerenciamento de pedidos
    @GetMapping("/pedidos")
    public ResponseEntity<Page<PedidoDTO>> listarPedidos(
            @PageableDefault(size = 20) Pageable pageable,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        
        Page<PedidoDTO> pedidos;
        
        if (inicio != null && fim != null) {
            pedidos = pedidoService.listarPedidosPorPeriodo(inicio, fim, pageable);
        } else {
            pedidos = pedidoService.listarPedidosPaginados(pageable);
        }
        
        return ResponseEntity.ok(pedidos);
    }
    
    @GetMapping("/pedidos/{id}")
    public ResponseEntity<PedidoDTO> buscarPedidoPorId(@PathVariable Long id) {
        PedidoDTO pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }
    
    // Relatórios e estatísticas
    @GetMapping("/relatorios/hoje")
    public ResponseEntity<Map<String, Object>> relatorioHoje() {
        Map<String, Object> relatorio = new HashMap<>();
        relatorio.put("totalPedidos", pedidoService.contarPedidosHoje());
        relatorio.put("totalVendas", pedidoService.calcularVendasHoje());
        relatorio.put("data", LocalDateTime.now());
        
        return ResponseEntity.ok(relatorio);
    }
    
    @GetMapping("/relatorios/periodo")
    public ResponseEntity<Page<PedidoDTO>> relatorioPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
            @PageableDefault(size = 50) Pageable pageable) {
        
        Page<PedidoDTO> pedidos = pedidoService.listarPedidosPorPeriodo(inicio, fim, pageable);
        return ResponseEntity.ok(pedidos);
    }
}

