package com.cardapio.mapper;

import com.cardapio.dto.ItemCardapioDTO;
import com.cardapio.model.ItemCardapio;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-01T23:49:52-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Ubuntu)"
)
@Component
public class ItemCardapioMapperImpl implements ItemCardapioMapper {

    @Override
    public ItemCardapioDTO toDTO(ItemCardapio itemCardapio) {
        if ( itemCardapio == null ) {
            return null;
        }

        ItemCardapioDTO itemCardapioDTO = new ItemCardapioDTO();

        itemCardapioDTO.setId( itemCardapio.getId() );
        itemCardapioDTO.setNome( itemCardapio.getNome() );
        itemCardapioDTO.setDescricao( itemCardapio.getDescricao() );
        itemCardapioDTO.setPreco( itemCardapio.getPreco() );
        itemCardapioDTO.setImagemUrl( itemCardapio.getImagemUrl() );
        itemCardapioDTO.setAtivo( itemCardapio.getAtivo() );

        return itemCardapioDTO;
    }

    @Override
    public ItemCardapio toEntity(ItemCardapioDTO itemCardapioDTO) {
        if ( itemCardapioDTO == null ) {
            return null;
        }

        ItemCardapio itemCardapio = new ItemCardapio();

        itemCardapio.setNome( itemCardapioDTO.getNome() );
        itemCardapio.setDescricao( itemCardapioDTO.getDescricao() );
        itemCardapio.setPreco( itemCardapioDTO.getPreco() );
        itemCardapio.setImagemUrl( itemCardapioDTO.getImagemUrl() );
        itemCardapio.setAtivo( itemCardapioDTO.getAtivo() );

        return itemCardapio;
    }

    @Override
    public void updateEntityFromDTO(ItemCardapioDTO itemCardapioDTO, ItemCardapio itemCardapio) {
        if ( itemCardapioDTO == null ) {
            return;
        }

        itemCardapio.setNome( itemCardapioDTO.getNome() );
        itemCardapio.setDescricao( itemCardapioDTO.getDescricao() );
        itemCardapio.setPreco( itemCardapioDTO.getPreco() );
        itemCardapio.setImagemUrl( itemCardapioDTO.getImagemUrl() );
        itemCardapio.setAtivo( itemCardapioDTO.getAtivo() );
    }
}
