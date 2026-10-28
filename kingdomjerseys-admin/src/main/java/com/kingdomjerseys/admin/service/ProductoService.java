package com.kingdomjerseys.admin.service;

import com.kingdomjerseys.admin.model.Producto;
import com.kingdomjerseys.admin.repository.PedidoRepository;
import com.kingdomjerseys.admin.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;

    public ProductoService(ProductoRepository productoRepository, PedidoRepository pedidoRepository) {
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public List<Producto> listar(String texto) {
        if (texto == null || texto.isBlank()) {
            return productoRepository.findAllByOrderByNombreAsc();
        }
        return productoRepository.findByNombreContainingIgnoreCaseOrderByNombreAsc(texto.trim());
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("El producto no existe"));
    }

    @Transactional
    public void guardar(Producto datos) {
        datos.setNombre(datos.getNombre().trim());

        if (datos.getId() == null) {
            productoRepository.save(datos);
            return;
        }

        Producto existente = buscarPorId(datos.getId());
        existente.setNombre(datos.getNombre());
        existente.setCategoria(datos.getCategoria());
        existente.setDescripcion(datos.getDescripcion());
        existente.setPrecio(datos.getPrecio());
        existente.setStock(datos.getStock());
        existente.setImagen(datos.getImagen());
        existente.setEtiqueta(datos.getEtiqueta());
        existente.setNovedad(datos.isNovedad());
        existente.setActivo(datos.isActivo());
        productoRepository.save(existente);
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = buscarPorId(id);

        if (pedidoRepository.existsByProductoId(id)) {
            throw new IllegalStateException("\"" + producto.getNombre()
                    + "\" ya aparece en pedidos. Desactívalo en lugar de eliminarlo");
        }
        productoRepository.delete(producto);
    }
}
