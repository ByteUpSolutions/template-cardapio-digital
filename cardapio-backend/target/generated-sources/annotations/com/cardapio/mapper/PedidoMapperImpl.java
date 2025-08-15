package com.cardapio.mapper;

import com.cardapio.dto.PedidoDTO;
import com.cardapio.dto.PedidoItemDTO;
import com.cardapio.model.ItemCardapio;
import com.cardapio.model.Pedido;
import com.cardapio.model.PedidoItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-13T21:53:02-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Ubuntu)"
)
@Component
public class PedidoMapperImpl implements PedidoMapper {

    @Override
    public PedidoDTO toDTO(Pedido pedido) {
        if ( pedido == null ) {
            return null;
        }

        PedidoDTO pedidoDTO = new PedidoDTO();

        pedidoDTO.setId( pedido.getId() );
        pedidoDTO.setMesa( pedido.getMesa() );
        pedidoDTO.setItens( pedidoItemListToPedidoItemDTOList( pedido.getItens() ) );
        pedidoDTO.setStatus( pedido.getStatus() );
        pedidoDTO.setDataCriacao( pedido.getDataCriacao() );
        pedidoDTO.setDataAtualizacao( pedido.getDataAtualizacao() );
        pedidoDTO.setObservacoes( pedido.getObservacoes() );

        pedidoDTO.setValorTotal( pedido.getValorTotal() );

        return pedidoDTO;
    }

    @Override
    public Pedido toEntity(PedidoDTO pedidoDTO) {
        if ( pedidoDTO == null ) {
            return null;
        }

        Pedido pedido = new Pedido();

        pedido.setMesa( pedidoDTO.getMesa() );
        pedido.setItens( pedidoItemDTOListToPedidoItemList( pedidoDTO.getItens() ) );
        pedido.setStatus( pedidoDTO.getStatus() );
        pedido.setObservacoes( pedidoDTO.getObservacoes() );

        return pedido;
    }

    @Override
    public PedidoItemDTO toItemDTO(PedidoItem pedidoItem) {
        if ( pedidoItem == null ) {
            return null;
        }

        PedidoItemDTO pedidoItemDTO = new PedidoItemDTO();

        pedidoItemDTO.setItemId( pedidoItemItemId( pedidoItem ) );
        pedidoItemDTO.setNomeItem( pedidoItemItemNome( pedidoItem ) );
        pedidoItemDTO.setId( pedidoItem.getId() );
        pedidoItemDTO.setQuantidade( pedidoItem.getQuantidade() );
        pedidoItemDTO.setPrecoUnitario( pedidoItem.getPrecoUnitario() );
        pedidoItemDTO.setObservacoesItem( pedidoItem.getObservacoesItem() );

        pedidoItemDTO.setSubtotal( pedidoItem.getSubtotal() );

        return pedidoItemDTO;
    }

    @Override
    public PedidoItem toItemEntity(PedidoItemDTO pedidoItemDTO) {
        if ( pedidoItemDTO == null ) {
            return null;
        }

        PedidoItem pedidoItem = new PedidoItem();

        pedidoItem.setQuantidade( pedidoItemDTO.getQuantidade() );
        pedidoItem.setPrecoUnitario( pedidoItemDTO.getPrecoUnitario() );
        pedidoItem.setObservacoesItem( pedidoItemDTO.getObservacoesItem() );

        return pedidoItem;
    }

    protected List<PedidoItemDTO> pedidoItemListToPedidoItemDTOList(List<PedidoItem> list) {
        if ( list == null ) {
            return null;
        }

        List<PedidoItemDTO> list1 = new ArrayList<PedidoItemDTO>( list.size() );
        for ( PedidoItem pedidoItem : list ) {
            list1.add( toItemDTO( pedidoItem ) );
        }

        return list1;
    }

    protected List<PedidoItem> pedidoItemDTOListToPedidoItemList(List<PedidoItemDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<PedidoItem> list1 = new ArrayList<PedidoItem>( list.size() );
        for ( PedidoItemDTO pedidoItemDTO : list ) {
            list1.add( toItemEntity( pedidoItemDTO ) );
        }

        return list1;
    }

    private Long pedidoItemItemId(PedidoItem pedidoItem) {
        if ( pedidoItem == null ) {
            return null;
        }
        ItemCardapio item = pedidoItem.getItem();
        if ( item == null ) {
            return null;
        }
        Long id = item.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String pedidoItemItemNome(PedidoItem pedidoItem) {
        if ( pedidoItem == null ) {
            return null;
        }
        ItemCardapio item = pedidoItem.getItem();
        if ( item == null ) {
            return null;
        }
        String nome = item.getNome();
        if ( nome == null ) {
            return null;
        }
        return nome;
    }
}
