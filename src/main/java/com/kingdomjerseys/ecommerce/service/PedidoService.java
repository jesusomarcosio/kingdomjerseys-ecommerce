package com.kingdomjerseys.ecommerce.service;

import com.kingdomjerseys.ecommerce.model.DetallePedido;
import com.kingdomjerseys.ecommerce.model.Pedido;
import com.kingdomjerseys.ecommerce.model.enums.EstadoPedido;
import com.kingdomjerseys.ecommerce.repository.PedidoRepository;
import com.kingdomjerseys.ecommerce.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PedidoService {

    public static final int TAMANIO_PAGINA = 10;

    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    /** Lista paginada, más recientes primero. Estado vacío o desconocido = sin filtro. */
    public Page<PedidoResumen> listar(String estadoNombre, int pagina) {
        Pageable pageable = PageRequest.of(Math.max(pagina, 0), TAMANIO_PAGINA,
                Sort.by(Sort.Order.desc("fecha"), Sort.Order.desc("id")));
        Optional<EstadoPedido> estado = interpretarEstado(estadoNombre);
        Page<Pedido> resultado = estado.isPresent()
                ? pedidoRepository.findByEstado(estado.get(), pageable)
                : pedidoRepository.findAll(pageable);
        return resultado.map(this::resumen);
    }

    public Optional<PedidoDetalle> detalle(Long id) {
        return pedidoRepository.findById(id).map(this::detalleDe);
    }

    /**
     * Cambia el estado validando la transición. Al cancelar se repone el stock de cada producto.
     * PENDIENTE y PAGADO son los únicos estados desde los que se puede cancelar, y ambos tienen el stock descontado.
     */
    @Transactional
    public Pedido cambiarEstado(Long id, EstadoPedido destino) {
        if (destino == null) throw new ReglaNegocioException("Elige un estado");
        Pedido pedido = pedidoRepository.buscarParaActualizar(id)
                .orElseThrow(() -> new ReglaNegocioException("El pedido no existe"));

        EstadoPedido actual = pedido.getEstado();
        if (!actual.puedeCambiarA(destino)) {
            throw new ReglaNegocioException("El pedido #" + id + " está «" + actual.getEtiqueta()
                    + "» y no puede pasar a «" + destino.getEtiqueta() + "».");
        }
        if (destino == EstadoPedido.CANCELADO) {
            for (DetallePedido detalle : pedido.getDetalles()) {
                productoRepository.sumarStock(detalle.getProducto().getId(), detalle.getCantidad());
            }
        }
        pedido.setEstado(destino);
        return pedido;
    }

    // ---- internos ----

    private Optional<EstadoPedido> interpretarEstado(String nombre) {
        if (nombre == null || nombre.isBlank()) return Optional.empty();
        try {
            return Optional.of(EstadoPedido.valueOf(nombre.trim().toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private PedidoResumen resumen(Pedido p) {
        int articulos = p.getDetalles().stream().mapToInt(DetallePedido::getCantidad).sum();
        return new PedidoResumen(p.getId(), p.getFecha(), p.getNombreCliente(), p.getCorreoCliente(),
                p.getEstado(), p.getTotal(), articulos);
    }

    private PedidoDetalle detalleDe(Pedido p) {
        List<LineaPedido> lineas = p.getDetalles().stream()
                .map(d -> new LineaPedido(d.getProducto().getId(), d.getProducto().getNombre(),
                        d.getProducto().getImagen(), d.getTalla(), d.getCantidad(),
                        d.getPrecioUnitario(), d.getSubtotal()))
                .toList();
        return new PedidoDetalle(p.getId(), p.getFecha(), p.getEstado(), p.getNombreCliente(),
                p.getCorreoCliente(), p.getDireccionEnvio(), p.getTotal(), lineas, p.getEstado().siguientes());
    }

    // ---- datos para las vistas ----

    public record PedidoResumen(Long id, LocalDateTime fecha, String nombreCliente, String correoCliente,
                                EstadoPedido estado, BigDecimal total, int articulos) {}

    public record LineaPedido(Long productoId, String productoNombre, String imagen, String talla,
                              int cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {}

    public record PedidoDetalle(Long id, LocalDateTime fecha, EstadoPedido estado, String nombreCliente,
                                String correoCliente, String direccionEnvio, BigDecimal total,
                                List<LineaPedido> lineas, List<EstadoPedido> siguientes) {}
}