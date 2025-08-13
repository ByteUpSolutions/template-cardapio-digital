package com.cardapio.repository;

import com.cardapio.model.ItemCardapio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItemCardapioRepository extends JpaRepository<ItemCardapio, Long> {
    
    List<ItemCardapio> findByAtivoTrue();
    
    @Query("SELECT i FROM ItemCardapio i WHERE i.ativo = true ORDER BY i.nome ASC")
    List<ItemCardapio> findAllAtivosOrdenados();
    
    @Query("SELECT i FROM ItemCardapio i WHERE i.nome LIKE %:nome% AND i.ativo = true")
    List<ItemCardapio> findByNomeContainingAndAtivoTrue(String nome);
}

