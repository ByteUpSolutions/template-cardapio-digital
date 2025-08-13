package com.cardapio.mapper;

import com.cardapio.dto.ItemCardapioDTO;
import com.cardapio.model.ItemCardapio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ItemCardapioMapper {
    
    ItemCardapioMapper INSTANCE = Mappers.getMapper(ItemCardapioMapper.class);
    
    ItemCardapioDTO toDTO(ItemCardapio itemCardapio);
    
    @Mapping(target = "id", ignore = true) // ID será gerado automaticamente
    ItemCardapio toEntity(ItemCardapioDTO itemCardapioDTO);
    
    @Mapping(target = "id", ignore = true) // Não atualizar o ID
    void updateEntityFromDTO(ItemCardapioDTO itemCardapioDTO, @MappingTarget ItemCardapio itemCardapio);
}

