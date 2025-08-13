package com.cardapio.service;

import com.cardapio.dto.PedidoDTO;
import com.cardapio.dto.PedidoItemDTO;
import com.cardapio.dto.StatusUpdateRequest;
import com.cardapio.exception.RecursoNaoEncontradoException;
import com.cardapio.mapper.PedidoMapper;
import com.cardapio.model.*;
import com.cardapio.repository.ItemCardapioRepository;
import com.cardapio.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@Transactional
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ItemCardapioRepository itemCardapioRepository;
    
    @Autowired
    private PedidoMapper pedidoMapper;
    
    // Lista thread-safe para armazenar conexões SSE
    private final List<SseEmitter> cozinhaEmitters = new CopyOnWriteArrayList<>();
    private final List<SseEmitter> garcomEmitters = new CopyOnWriteArrayList<>();
    
    public PedidoDTO criarPedido(PedidoDTO pedidoDTO) {
        validarPedido(pedidoDTO);
        
        Pedido pedido = new Pedido(pedidoDTO.getMesa(), pedidoDTO.getObservacoes());
        
        // Processar itens do pedido
        for (PedidoItemDTO itemDTO : pedidoDTO.getItens()) {
            ItemCardapio itemCardapio = itemCardapioRepository.findById(itemDTO.getItemId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Item não encontrado com ID: " + itemDTO.getItemId()));
            
            if (!itemCardapio.getAtivo()) {
                throw new IllegalArgumentException("Item não está disponível: " + itemCardapio.getNome());
            }
            
            PedidoItem pedidoItem = new PedidoItem(
                itemCardapio, 
                itemDTO.getQuantidade(), 
                itemCardapio.getPreco(), // Usar preço atual do cardápio
                itemDTO.getObservacoesItem()
            );
            
            pedido.adicionarItem(pedidoItem);
        }
        
        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        PedidoDTO resultado = pedidoMapper.toDTO(pedidoSalvo);
        
        // Notificar cozinha sobre novo pedido
        notificarCozinha(resultado);
        
        return resultado;
    }
    
    @Transactional(readOnly = true)
    public PedidoDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado com ID: " + id));
        return pedidoMapper.toDTO(pedido);
    }
    
    @Transactional(readOnly = true)
    public List<PedidoDTO> listarPedidosPorStatus(PedidoStatus status) {
        List<Pedido> pedidos = pedidoRepository.findByStatusOrderByDataCriacaoAsc(status);
        return pedidos.stream()
                .map(pedidoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<PedidoDTO> listarPedidosPaginados(Pageable pageable) {
        Page<Pedido> pedidos = pedidoRepository.findAll(pageable);
        return pedidos.map(pedidoMapper::toDTO);
    }
    
    @Transactional(readOnly = true)
    public Page<PedidoDTO> listarPedidosPorPeriodo(LocalDateTime inicio, LocalDateTime fim, Pageable pageable) {
        Page<Pedido> pedidos = pedidoRepository.findByDataCriacaoBetween(inicio, fim, pageable);
        return pedidos.map(pedidoMapper::toDTO);
    }
    
    @Transactional(readOnly = true)
    public List<PedidoDTO> listarPedidosPorMesa(String mesa) {
        List<Pedido> pedidos = pedidoRepository.findByMesaOrderByDataCriacaoDesc(mesa);
        return pedidos.stream()
                .map(pedidoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public PedidoDTO atualizarStatusPedido(Long id, StatusUpdateRequest statusRequest) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado com ID: " + id));
        
        PedidoStatus statusAnterior = pedido.getStatus();
        pedido.setStatus(statusRequest.getStatus());
        
        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        PedidoDTO resultado = pedidoMapper.toDTO(pedidoAtualizado);
        
        // Notificar garçom se pedido ficou pronto
        if (statusRequest.getStatus() == PedidoStatus.PRONTO && statusAnterior != PedidoStatus.PRONTO) {
            notificarGarcom(resultado);
        }
        
        return resultado;
    }
    
    // Métodos para SSE (Server-Sent Events)
    public SseEmitter criarConexaoCozinha() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        cozinhaEmitters.add(emitter);
        
        emitter.onCompletion(() -> cozinhaEmitters.remove(emitter));
        emitter.onTimeout(() -> cozinhaEmitters.remove(emitter));
        emitter.onError((ex) -> cozinhaEmitters.remove(emitter));
        
        return emitter;
    }
    
    public SseEmitter criarConexaoGarcom() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        garcomEmitters.add(emitter);
        
        emitter.onCompletion(() -> garcomEmitters.remove(emitter));
        emitter.onTimeout(() -> garcomEmitters.remove(emitter));
        emitter.onError((ex) -> garcomEmitters.remove(emitter));
        
        return emitter;
    }
    
    private void notificarCozinha(PedidoDTO pedido) {
        List<SseEmitter> emittersParaRemover = new CopyOnWriteArrayList<>();
        
        for (SseEmitter emitter : cozinhaEmitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("novo-pedido")
                        .data(pedido));
            } catch (IOException e) {
                emittersParaRemover.add(emitter);
            }
        }
        
        cozinhaEmitters.removeAll(emittersParaRemover);
    }
    
    private void notificarGarcom(PedidoDTO pedido) {
        List<SseEmitter> emittersParaRemover = new CopyOnWriteArrayList<>();
        
        for (SseEmitter emitter : garcomEmitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("pedido-pronto")
                        .data(pedido));
            } catch (IOException e) {
                emittersParaRemover.add(emitter);
            }
        }
        
        garcomEmitters.removeAll(emittersParaRemover);
    }
    
    // Métodos para estatísticas
    @Transactional(readOnly = true)
    public Long contarPedidosHoje() {
        LocalDateTime inicioHoje = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        return pedidoRepository.countPedidosHoje(inicioHoje);
    }
    
    @Transactional(readOnly = true)
    public Double calcularVendasHoje() {
        LocalDateTime inicioHoje = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        Double vendas = pedidoRepository.somaVendasHoje(inicioHoje);
        return vendas != null ? vendas : 0.0;
    }
    
    private void validarPedido(PedidoDTO pedidoDTO) {
        if (pedidoDTO.getItens() == null || pedidoDTO.getItens().isEmpty()) {
            throw new IllegalArgumentException("Pedido deve ter pelo menos um item");
        }
        
        for (PedidoItemDTO item : pedidoDTO.getItens()) {
            if (item.getItemId() == null) {
                throw new IllegalArgumentException("ID do item é obrigatório");
            }
            
            if (item.getQuantidade() == null || item.getQuantidade() <= 0) {
                throw new IllegalArgumentException("Quantidade deve ser maior que zero");
            }
        }
        
        if (pedidoDTO.getMesa() != null && pedidoDTO.getMesa().length() > 20) {
            throw new IllegalArgumentException("Número da mesa deve ter no máximo 20 caracteres");
        }
        
        if (pedidoDTO.getObservacoes() != null && pedidoDTO.getObservacoes().length() > 500) {
            throw new IllegalArgumentException("Observações devem ter no máximo 500 caracteres");
        }
    }
}

