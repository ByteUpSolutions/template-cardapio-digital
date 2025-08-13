package com.cardapio.service;

import com.cardapio.dto.LoginRequest;
import com.cardapio.dto.LoginResponse;
import com.cardapio.dto.UsuarioDTO;
import com.cardapio.exception.RecursoNaoEncontradoException;
import com.cardapio.mapper.UsuarioMapper;
import com.cardapio.model.Usuario;
import com.cardapio.repository.UsuarioRepository;
import com.cardapio.security.CustomUserDetails;
import com.cardapio.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private UsuarioMapper usuarioMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public UsuarioDTO criarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }
        
        Usuario usuario = usuarioMapper.toEntityWithPassword(usuarioDTO);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(usuarioSalvo);
    }
    
    public LoginResponse autenticar(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    loginRequest.getSenha()
                )
            );
            
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);
            
            UsuarioDTO usuarioDTO = new UsuarioDTO(
                userDetails.getId(),
                userDetails.getNome(),
                userDetails.getUsername(),
                userDetails.getRole()
            );
            
            return new LoginResponse(token, usuarioDTO);
            
        } catch (Exception e) {
            throw new IllegalArgumentException("Credenciais inválidas");
        }
    }
    
    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id));
        return usuarioMapper.toDTO(usuario);
    }
    
    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com email: " + email));
        return usuarioMapper.toDTO(usuario);
    }
    
    public UsuarioDTO atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id));
        
        // Verificar se o email já está em uso por outro usuário
        if (!usuario.getEmail().equals(usuarioDTO.getEmail()) && 
            usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("Email já está em uso");
        }
        
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setRole(usuarioDTO.getRole());
        
        // Atualizar senha apenas se fornecida
        if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().trim().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        }
        
        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toDTO(usuarioAtualizado);
    }
    
    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}

