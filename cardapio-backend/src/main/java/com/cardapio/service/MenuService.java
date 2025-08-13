package com.cardapio.service;

import com.cardapio.dto.ItemCardapioDTO;
import com.cardapio.exception.RecursoNaoEncontradoException;
import com.cardapio.mapper.ItemCardapioMapper;
import com.cardapio.model.ItemCardapio;
import com.cardapio.repository.ItemCardapioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {

    @Autowired
    private ItemCardapioRepository itemCardapioRepository;

    @Autowired
    private ItemCardapioMapper itemCardapioMapper;

    @Transactional(readOnly = true)
    public List<ItemCardapioDTO> listarCardapioAtivo() {
        List<ItemCardapio> itens = itemCardapioRepository.findAllAtivosOrdenados();
        return itens.stream()
                .map(itemCardapioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ItemCardapioDTO> listarTodosItens() {
        List<ItemCardapio> itens = itemCardapioRepository.findAll();
        return itens.stream()
                .map(itemCardapioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemCardapioDTO buscarPorId(Long id) {
        ItemCardapio item = itemCardapioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item não encontrado com ID: " + id));
        return itemCardapioMapper.toDTO(item);
    }

    @Transactional(readOnly = true)
    public List<ItemCardapioDTO> buscarPorNome(String nome) {
        List<ItemCardapio> itens = itemCardapioRepository.findByNomeContainingAndAtivoTrue(nome);
        return itens.stream()
                .map(itemCardapioMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ItemCardapioDTO adicionarItem(ItemCardapioDTO itemDTO) {
        validarItem(itemDTO);

        ItemCardapio item = itemCardapioMapper.toEntity(itemDTO);
        ItemCardapio itemSalvo = itemCardapioRepository.save(item);

        return itemCardapioMapper.toDTO(itemSalvo);
    }

    public ItemCardapioDTO atualizarItem(Long id, ItemCardapioDTO itemDTO) {
        ItemCardapio itemExistente = itemCardapioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item não encontrado com ID: " + id));

        validarItem(itemDTO);

        itemCardapioMapper.updateEntityFromDTO(itemDTO, itemExistente);
        ItemCardapio itemAtualizado = itemCardapioRepository.save(itemExistente);

        return itemCardapioMapper.toDTO(itemAtualizado);
    }

    public void removerItem(Long id) {
        ItemCardapio item = itemCardapioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item não encontrado com ID: " + id));

        // Soft delete - apenas marcar como inativo
        item.setAtivo(false);
        itemCardapioRepository.save(item);
    }

    public void ativarItem(Long id) {
        ItemCardapio item = itemCardapioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item não encontrado com ID: " + id));

        item.setAtivo(true);
        itemCardapioRepository.save(item);
    }

    public void deletarItemPermanentemente(Long id) {
        if (!itemCardapioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Item não encontrado com ID: " + id);
        }
        itemCardapioRepository.deleteById(id);
    }

    private void validarItem(ItemCardapioDTO itemDTO) {
        if (itemDTO.getNome() == null || itemDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do item é obrigatório");
        }

        if (itemDTO.getPreco() == null || itemDTO.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }

        if (itemDTO.getNome().length() > 100) {
            throw new IllegalArgumentException("Nome do item deve ter no máximo 100 caracteres");
        }

        if (itemDTO.getDescricao() != null && itemDTO.getDescricao().length() > 500) {
            throw new IllegalArgumentException("Descrição deve ter no máximo 500 caracteres");
        }
    }
}
