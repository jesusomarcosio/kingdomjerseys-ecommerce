package com.kingdomjerseys.ecommerce.service;

import com.kingdomjerseys.ecommerce.model.ItemCarrito;
import com.kingdomjerseys.ecommerce.model.Producto;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Un carrito por sesión del navegador
@Service
@SessionScope
public class CarritoService {

    private static final int CANTIDAD_MAXIMA = 10;

    private final ProductoService productoService;
    private final Map<String, ItemCarrito> items = new LinkedHashMap<>();
    private List<ItemCarrito> ultimaOrden = new ArrayList<>();

    public CarritoService(ProductoService productoService) {
        this.productoService = productoService;
    }

    public void agregar(String id, int cantidad) {
        Producto producto = productoService.buscar(id).orElse(null);
        if (producto == null) {
            return;
        }
        ItemCarrito item = items.get(id);
        if (item == null) {
            items.put(id, new ItemCarrito(producto, limitar(cantidad)));
        } else {
            item.setCantidad(limitar(item.getCantidad() + cantidad));
        }
    }

    public void cambiarCantidad(String id, int cambio) {
        ItemCarrito item = items.get(id);
        if (item != null) {
            item.setCantidad(limitar(item.getCantidad() + cambio));
        }
    }

    public void remover(String id) {
        items.remove(id);
    }

    public List<ItemCarrito> getItems() {
        return new ArrayList<>(items.values());
    }

    public int getSubtotal() {
        return subtotalDe(items.values());
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    // Al pagar, el carrito se guarda como "última orden" para mostrarla en la confirmación
    public void confirmarCompra() {
        ultimaOrden = new ArrayList<>(items.values());
        items.clear();
    }

    public List<ItemCarrito> getUltimaOrden() {
        return ultimaOrden;
    }

    public int getSubtotalUltimaOrden() {
        return subtotalDe(ultimaOrden);
    }

    private int subtotalDe(Iterable<ItemCarrito> lista) {
        int suma = 0;
        for (ItemCarrito item : lista) {
            suma += item.getTotal();
        }
        return suma;
    }

    private int limitar(int cantidad) {
        return Math.max(1, Math.min(cantidad, CANTIDAD_MAXIMA));
    }
}
