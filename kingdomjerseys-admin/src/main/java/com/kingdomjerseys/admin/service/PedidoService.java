package com.kingdomjerseys.admin.service;

import com.kingdomjerseys.admin.model.EstadoPedido;
import com.kingdomjerseys.admin.model.Pedido;
import com.kingdomjerseys.admin.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> listar(EstadoPedido estado) {
        if (estado == null) {
            return pedidoRepository.findAllByOrderByFechaDesc();
        }
        return pedidoRepository.findByEstadoOrderByFechaDesc(estado);
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("El pedido no existe"));
    }

    @Transactional
    public void cambiarEstado(Long id, EstadoPedido nuevoEstado) {
        Pedido pedido = buscarPorId(id);

        if (pedido.getEstado().esFinal()) {
            throw new IllegalStateException("El pedido #" + id + " ya está "
                    + pedido.getEstado().getEtiqueta().toLowerCase() + " y no se puede modificar");
        }
        pedido.setEstado(nuevoEstado);
        pedidoRepository.save(pedido);
    }
}
