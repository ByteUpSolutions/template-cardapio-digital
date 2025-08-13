package com.cardapio.repository;

import com.cardapio.model.Pedido;
import com.cardapio.model.PedidoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    
    List<Pedido> findByStatus(PedidoStatus status);
    
    @Query("SELECT p FROM Pedido p WHERE p.status = :status ORDER BY p.dataCriacao ASC")
    List<Pedido> findByStatusOrderByDataCriacaoAsc(@Param("status") PedidoStatus status);
    
    @Query("SELECT p FROM Pedido p WHERE p.dataCriacao BETWEEN :inicio AND :fim ORDER BY p.dataCriacao DESC")
    Page<Pedido> findByDataCriacaoBetween(@Param("inicio") LocalDateTime inicio, 
                                          @Param("fim") LocalDateTime fim, 
                                          Pageable pageable);
    
    @Query("SELECT p FROM Pedido p WHERE p.mesa = :mesa ORDER BY p.dataCriacao DESC")
    List<Pedido> findByMesaOrderByDataCriacaoDesc(@Param("mesa") String mesa);
    
    @Query("SELECT p FROM Pedido p WHERE p.status IN :statuses ORDER BY p.dataCriacao ASC")
    List<Pedido> findByStatusInOrderByDataCriacaoAsc(@Param("statuses") List<PedidoStatus> statuses);
    
    // Para relatórios e estatísticas
    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.dataCriacao >= :inicio")
    Long countPedidosHoje(@Param("inicio") LocalDateTime inicio);
    
    @Query("SELECT SUM(pi.precoUnitario * pi.quantidade) FROM Pedido p JOIN p.itens pi WHERE p.dataCriacao >= :inicio AND p.status = 'ENTREGUE'")
    Double somaVendasHoje(@Param("inicio") LocalDateTime inicio);
}

