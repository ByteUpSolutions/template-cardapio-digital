package com.cardapio.service;

import com.cardapio.dto.PedidoDTO;
import com.cardapio.dto.PedidoItemDTO;
import com.cardapio.dto.StatusUpdateRequest;
import com.cardapio.exception.RecursoNaoEncontradoException;
import com.cardapio.mapper.PedidoMapper;
import com.cardapio.model.*;
import com.cardapio.repository.ItemCardapioRepository;
import com.cardapio.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal; // IMPORTANTE: Importar BigDecimal
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections; // Usar para lista vazia
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private PedidoMapper pedidoMapper;

    @InjectMocks
    private PedidoService pedidoService;

    private Pedido pedido;
    private PedidoDTO pedidoDTO;
    private ItemCardapio itemCardapio;
    private PedidoItemDTO pedidoItemDTO;

    @BeforeEach
    void setUp() {
        // CORREÇÃO: Use new BigDecimal("valor") para os preços
        itemCardapio = new ItemCardapio("Hambúrguer", "Delicioso hambúrguer", new BigDecimal("25.90"), "url");
        itemCardapio.setId(1L);
        itemCardapio.setAtivo(true);

        pedidoItemDTO = new PedidoItemDTO(1L, 2, new BigDecimal("25.90"));
        pedidoItemDTO.setNomeItem("Hambúrguer");

        pedidoDTO = new PedidoDTO("Mesa 5", Arrays.asList(pedidoItemDTO), "Sem cebola");

        pedido = new Pedido("Mesa 5", "Sem cebola");
        pedido.setId(1L);
        pedido.setStatus(PedidoStatus.RECEBIDO);
        pedido.setDataCriacao(LocalDateTime.now());
    }

    @Test
    void deveCriarPedido() {
        // Given
        when(itemCardapioRepository.findById(1L)).thenReturn(Optional.of(itemCardapio));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(pedidoMapper.toDTO(any(Pedido.class))).thenReturn(pedidoDTO);

        // When
        PedidoDTO resultado = pedidoService.criarPedido(pedidoDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("Mesa 5", resultado.getMesa());
        assertEquals("Sem cebola", resultado.getObservacoes());

        verify(itemCardapioRepository).findById(1L);
        verify(pedidoRepository).save(any(Pedido.class));
        verify(pedidoMapper).toDTO(any(Pedido.class));
    }

    @Test
    void deveLancarExcecaoQuandoItemNaoEncontrado() {
        // Given
        when(itemCardapioRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            pedidoService.criarPedido(pedidoDTO);
        });

        verify(itemCardapioRepository).findById(1L);
        verifyNoInteractions(pedidoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoItemInativo() {
        // Given
        itemCardapio.setAtivo(false);
        when(itemCardapioRepository.findById(1L)).thenReturn(Optional.of(itemCardapio));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.criarPedido(pedidoDTO);
        });

        verify(itemCardapioRepository).findById(1L);
        verifyNoInteractions(pedidoRepository);
    }

    @Test
    void deveBuscarPedidoPorId() {
        // Given
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoMapper.toDTO(pedido)).thenReturn(pedidoDTO);

        // When
        PedidoDTO resultado = pedidoService.buscarPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals("Mesa 5", resultado.getMesa());

        verify(pedidoRepository).findById(1L);
        verify(pedidoMapper).toDTO(pedido);
    }

    @Test
    void deveLancarExcecaoQuandoPedidoNaoEncontrado() {
        // Given
        when(pedidoRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            pedidoService.buscarPorId(999L);
        });

        verify(pedidoRepository).findById(999L);
        verifyNoInteractions(pedidoMapper);
    }

    @Test
    void deveAtualizarStatusPedido() {
        // Given
        StatusUpdateRequest statusRequest = new StatusUpdateRequest(PedidoStatus.EM_PREPARO);
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(pedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);
        when(pedidoMapper.toDTO(any(Pedido.class))).thenReturn(pedidoDTO);

        // When
        PedidoDTO resultado = pedidoService.atualizarStatusPedido(1L, statusRequest);

        // Then
        assertNotNull(resultado);
        assertEquals(PedidoStatus.EM_PREPARO, pedido.getStatus());

        verify(pedidoRepository).findById(1L);
        verify(pedidoRepository).save(pedido);
        verify(pedidoMapper).toDTO(pedido);
    }

    @Test
    void deveListarPedidosPorStatus() {
        // Given
        List<Pedido> pedidos = Arrays.asList(pedido);
        when(pedidoRepository.findByStatusOrderByDataCriacaoAsc(PedidoStatus.RECEBIDO)).thenReturn(pedidos);
        when(pedidoMapper.toDTO(any(Pedido.class))).thenReturn(pedidoDTO);

        // When
        List<PedidoDTO> resultado = pedidoService.listarPedidosPorStatus(PedidoStatus.RECEBIDO);

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Mesa 5", resultado.get(0).getMesa());

        verify(pedidoRepository).findByStatusOrderByDataCriacaoAsc(PedidoStatus.RECEBIDO);
        verify(pedidoMapper).toDTO(pedido);
    }

    @Test
    void deveLancarExcecaoQuandoPedidoSemItens() {
        // Given
        // CORREÇÃO: Use Collections.emptyList() para criar uma lista vazia
        PedidoDTO pedidoSemItens = new PedidoDTO("Mesa 1", Collections.emptyList(), "Observação");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.criarPedido(pedidoSemItens);
        });

        verifyNoInteractions(itemCardapioRepository);
        verifyNoInteractions(pedidoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoQuantidadeInvalida() {
        // Given
        // CORREÇÃO: Use new BigDecimal para o preço
        PedidoItemDTO itemInvalido = new PedidoItemDTO(1L, 0, new BigDecimal("25.90"));
        PedidoDTO pedidoInvalido = new PedidoDTO("Mesa 1", Arrays.asList(itemInvalido), "Observação");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            pedidoService.criarPedido(pedidoInvalido);
        });

        verifyNoInteractions(itemCardapioRepository);
        verifyNoInteractions(pedidoRepository);
    }
}