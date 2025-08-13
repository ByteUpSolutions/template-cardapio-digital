package com.cardapio.mapper;

import com.cardapio.dto.PedidoDTO;
import com.cardapio.dto.PedidoItemDTO;
import com.cardapio.model.Pedido;
import com.cardapio.model.PedidoItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PedidoMapper {
    
    PedidoMapper INSTANCE = Mappers.getMapper(PedidoMapper.class);
    
    @Mapping(target = "valorTotal", expression = "java(pedido.getValorTotal())")
    PedidoDTO toDTO(Pedido pedido);
    
    @Mapping(target = "id", ignore = true) // ID será gerado automaticamente
    @Mapping(target = "dataCriacao", ignore = true) // Será definido no construtor
    @Mapping(target = "dataAtualizacao", ignore = true) // Será definido no construtor
    Pedido toEntity(PedidoDTO pedidoDTO);
    
    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "nomeItem", source = "item.nome")
    @Mapping(target = "subtotal", expression = "java(pedidoItem.getSubtotal())")
    PedidoItemDTO toItemDTO(PedidoItem pedidoItem);
    
    @Mapping(target = "id", ignore = true) // ID será gerado automaticamente
    @Mapping(target = "pedido", ignore = true) // Será definido no service
    @Mapping(target = "item", ignore = true) // Será buscado pelo itemId no service
    PedidoItem toItemEntity(PedidoItemDTO pedidoItemDTO);
}

