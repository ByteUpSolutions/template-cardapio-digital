package com.cardapio.service;

import com.cardapio.dto.ItemCardapioDTO;
import com.cardapio.exception.RecursoNaoEncontradoException;
import com.cardapio.mapper.ItemCardapioMapper;
import com.cardapio.model.ItemCardapio;
import com.cardapio.repository.ItemCardapioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal; // IMPORTANTE: Importar BigDecimal
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private ItemCardapioMapper itemCardapioMapper;

    @InjectMocks
    private MenuService menuService;

    private ItemCardapio itemCardapio;
    private ItemCardapioDTO itemCardapioDTO;

    @BeforeEach
    void setUp() {
        // CORREÇÃO: Use new BigDecimal("valor") para criar os preços
        itemCardapio = new ItemCardapio("Hambúrguer", "Delicioso hambúrguer", new BigDecimal("25.90"), "url-imagem");
        itemCardapio.setId(1L);

        itemCardapioDTO = new ItemCardapioDTO(1L, "Hambúrguer", "Delicioso hambúrguer", new BigDecimal("25.90"),
                "url-imagem", true);
    }

    @Test
    void deveListarCardapioAtivo() {
        // Given
        List<ItemCardapio> itensAtivos = Arrays.asList(itemCardapio);
        when(itemCardapioRepository.findAllAtivosOrdenados()).thenReturn(itensAtivos);
        when(itemCardapioMapper.toDTO(any(ItemCardapio.class))).thenReturn(itemCardapioDTO);

        // When
        List<ItemCardapioDTO> resultado = menuService.listarCardapioAtivo();

        // Then
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Hambúrguer", resultado.get(0).getNome());

        verify(itemCardapioRepository).findAllAtivosOrdenados();
        verify(itemCardapioMapper).toDTO(itemCardapio);
    }

    @Test
    void deveBuscarItemPorId() {
        // Given
        when(itemCardapioRepository.findById(1L)).thenReturn(Optional.of(itemCardapio));
        when(itemCardapioMapper.toDTO(itemCardapio)).thenReturn(itemCardapioDTO);

        // When
        ItemCardapioDTO resultado = menuService.buscarPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals("Hambúrguer", resultado.getNome());
        // CORREÇÃO: Use compareTo para verificar a igualdade de BigDecimals
        assertEquals(0, new BigDecimal("25.90").compareTo(resultado.getPreco()));

        verify(itemCardapioRepository).findById(1L);
        verify(itemCardapioMapper).toDTO(itemCardapio);
    }

    @Test
    void deveLancarExcecaoQuandoItemNaoEncontrado() {
        // Given
        when(itemCardapioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecursoNaoEncontradoException.class, () -> {
            menuService.buscarPorId(999L);
        });

        verify(itemCardapioRepository).findById(999L);
        verifyNoInteractions(itemCardapioMapper);
    }

    @Test
    void deveAdicionarItem() {
        // Given
        // CORREÇÃO: Use new BigDecimal para os preços
        ItemCardapioDTO novoItemDTO = new ItemCardapioDTO(null, "Pizza", "Pizza deliciosa", new BigDecimal("30.00"),
                "url", true);
        ItemCardapio novoItem = new ItemCardapio("Pizza", "Pizza deliciosa", new BigDecimal("30.00"), "url");
        ItemCardapio itemSalvo = new ItemCardapio("Pizza", "Pizza deliciosa", new BigDecimal("30.00"), "url");
        itemSalvo.setId(2L);
        ItemCardapioDTO itemSalvoDTO = new ItemCardapioDTO(2L, "Pizza", "Pizza deliciosa", new BigDecimal("30.00"),
                "url", true);

        when(itemCardapioMapper.toEntity(novoItemDTO)).thenReturn(novoItem);
        when(itemCardapioRepository.save(novoItem)).thenReturn(itemSalvo);
        when(itemCardapioMapper.toDTO(itemSalvo)).thenReturn(itemSalvoDTO);

        // When
        ItemCardapioDTO resultado = menuService.adicionarItem(novoItemDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(2L, resultado.getId());
        assertEquals("Pizza", resultado.getNome());

        verify(itemCardapioMapper).toEntity(novoItemDTO);
        verify(itemCardapioRepository).save(novoItem);
        verify(itemCardapioMapper).toDTO(itemSalvo);
    }

    @Test
    void deveLancarExcecaoQuandoNomeVazio() {
        // Given
        // CORREÇÃO: Use new BigDecimal
        ItemCardapioDTO itemInvalido = new ItemCardapioDTO(null, "", "Descrição", new BigDecimal("25.90"), "url", true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            menuService.adicionarItem(itemInvalido);
        });

        verifyNoInteractions(itemCardapioRepository);
        verifyNoInteractions(itemCardapioMapper);
    }

    @Test
    void deveLancarExcecaoQuandoPrecoInvalido() {
        // Given
        // CORREÇÃO: Use BigDecimal.ZERO
        ItemCardapioDTO itemInvalido = new ItemCardapioDTO(null, "Item", "Descrição", BigDecimal.ZERO, "url", true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            menuService.adicionarItem(itemInvalido);
        });

        verifyNoInteractions(itemCardapioRepository);
        verifyNoInteractions(itemCardapioMapper);
    }

    @Test
    void deveRemoverItem() {
        // Given
        when(itemCardapioRepository.findById(1L)).thenReturn(Optional.of(itemCardapio));
        when(itemCardapioRepository.save(any(ItemCardapio.class))).thenReturn(itemCardapio);

        // When
        menuService.removerItem(1L);

        // Then
        verify(itemCardapioRepository).findById(1L);
        verify(itemCardapioRepository).save(itemCardapio);
        assertFalse(itemCardapio.getAtivo());
    }
}