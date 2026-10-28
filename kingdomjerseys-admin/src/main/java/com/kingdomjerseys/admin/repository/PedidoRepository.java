package com.kingdomjerseys.admin.repository;

import com.kingdomjerseys.admin.model.EstadoPedido;
import com.kingdomjerseys.admin.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findAllByOrderByFechaDesc();

    List<Pedido> findByEstadoOrderByFechaDesc(EstadoPedido estado);

    boolean existsByProductoId(Long productoId);
}
