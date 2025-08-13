package com.cardapio.mapper;

import com.cardapio.dto.UsuarioDTO;
import com.cardapio.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    
    UsuarioMapper INSTANCE = Mappers.getMapper(UsuarioMapper.class);
    
    @Mapping(target = "senha", ignore = true) // Não expor senha no DTO de resposta
    UsuarioDTO toDTO(Usuario usuario);
    
    @Mapping(target = "id", ignore = true) // ID será gerado automaticamente
    Usuario toEntity(UsuarioDTO usuarioDTO);
    
    // Método específico para incluir senha (usado na criação)
    Usuario toEntityWithPassword(UsuarioDTO usuarioDTO);
}

