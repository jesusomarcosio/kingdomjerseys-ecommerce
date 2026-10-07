package com.kingdomjerseys.ecommerce.model;

public class Producto {

    private String id;
    private String nombre;
    private String categoria;
    private String descripcion;
    private int precio;
    private String imagen;
    private String imagenDetalle;
    private String material;
    private String ajuste;
    private String color;

    public Producto(String id, String nombre, String categoria, String descripcion, int precio,
                    String imagen, String imagenDetalle, String material, String ajuste, String color) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
        this.imagenDetalle = imagenDetalle;
        this.material = material;
        this.ajuste = ajuste;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPrecio() {
        return precio;
    }

    // Foto de las tarjetas del catálogo
    public String getImagen() {
        return imagen;
    }

    // Foto de la página del producto, del carrito y del resumen de compra
    public String getImagenDetalle() {
        return imagenDetalle;
    }

    public String getMaterial() {
        return material;
    }

    public String getAjuste() {
        return ajuste;
    }

    public String getColor() {
        return color;
    }
}
