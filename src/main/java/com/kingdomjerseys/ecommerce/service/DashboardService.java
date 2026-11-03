package com.kingdomjerseys.ecommerce.service;

import com.kingdomjerseys.ecommerce.model.Producto;
import com.kingdomjerseys.ecommerce.model.enums.EstadoPedido;
import com.kingdomjerseys.ecommerce.repository.PedidoRepository;
import com.kingdomjerseys.ecommerce.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    /** Un producto activo con este stock o menos se considera "stock bajo". */
    private static final int UMBRAL_STOCK_BAJO = 5;

    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;

    public DashboardService(ProductoRepository productoRepository, PedidoRepository pedidoRepository) {
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public ResumenDashboard obtenerResumen() {
        List<ConteoEstado> porEstado = new ArrayList<>();
        for (EstadoPedido estado : EstadoPedido.values()) {
            porEstado.add(new ConteoEstado(estado, pedidoRepository.countByEstado(estado)));
        }
        return new ResumenDashboard(
                productoRepository.count(),
                productoRepository.countByActivoTrue(),
                productoRepository.findByActivoTrueAndStockLessThanEqualOrderByStockAsc(UMBRAL_STOCK_BAJO),
                UMBRAL_STOCK_BAJO,
                pedidoRepository.count(),
                porEstado);
    }

    public record ConteoEstado(EstadoPedido estado, long cantidad) {}

    public record ResumenDashboard(long totalProductos, long productosActivos,
                                   List<Producto> stockBajo, int umbralStockBajo,
                                   long totalPedidos, List<ConteoEstado> pedidosPorEstado) {}
}