package com.kingdomjerseys.ecommerce.repository;

import com.kingdomjerseys.ecommerce.model.Pedido;
import com.kingdomjerseys.ecommerce.model.enums.EstadoPedido;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Page<Pedido> findByEstado(EstadoPedido estado, Pageable pageable);
    long countByEstado(EstadoPedido estado);

    /** Bloquea la fila hasta terminar la transacción: dos cambios de estado simultáneos no se pisan. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Pedido p where p.id = :id")
    Optional<Pedido> buscarParaActualizar(@Param("id") Long id);
}