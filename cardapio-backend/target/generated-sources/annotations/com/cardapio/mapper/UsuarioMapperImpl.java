package com.cardapio.mapper;

import com.cardapio.dto.UsuarioDTO;
import com.cardapio.model.Usuario;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-01T23:49:52-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Ubuntu)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Override
    public UsuarioDTO toDTO(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();

        usuarioDTO.setId( usuario.getId() );
        usuarioDTO.setNome( usuario.getNome() );
        usuarioDTO.setEmail( usuario.getEmail() );
        usuarioDTO.setRole( usuario.getRole() );

        return usuarioDTO;
    }

    @Override
    public Usuario toEntity(UsuarioDTO usuarioDTO) {
        if ( usuarioDTO == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setNome( usuarioDTO.getNome() );
        usuario.setEmail( usuarioDTO.getEmail() );
        usuario.setSenha( usuarioDTO.getSenha() );
        usuario.setRole( usuarioDTO.getRole() );

        return usuario;
    }

    @Override
    public Usuario toEntityWithPassword(UsuarioDTO usuarioDTO) {
        if ( usuarioDTO == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setId( usuarioDTO.getId() );
        usuario.setNome( usuarioDTO.getNome() );
        usuario.setEmail( usuarioDTO.getEmail() );
        usuario.setSenha( usuarioDTO.getSenha() );
        usuario.setRole( usuarioDTO.getRole() );

        return usuario;
    }
}
