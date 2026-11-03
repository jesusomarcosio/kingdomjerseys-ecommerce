package com.kingdomjerseys.ecommerce.model;

import java.math.BigDecimal;

public class ItemCarrito {

    private final Producto producto;
    private int cantidad;

    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getTotal() {
        return producto.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }
}
