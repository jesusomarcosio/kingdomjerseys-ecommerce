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
    private boolean novedad;
    private String etiqueta;

    public Producto(String id, String nombre, String categoria, String descripcion, int precio,
                    String imagen, String imagenDetalle, String material, String ajuste, String color,
                    boolean novedad, String etiqueta) {
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
        this.novedad = novedad;
        this.etiqueta = etiqueta;
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

    // Foto de las tarjetas
    public String getImagen() {
        return imagen;
    }

    // Foto de la página del producto
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

    // Sale en las tarjetas del catálogo de Novedades
    public boolean isNovedad() {
        return novedad;
    }

    // Texto del distintivo de la tarjeta (NUEVA, MÁS VENDIDA...); null si no lleva
    public String getEtiqueta() {
        return etiqueta;
    }

    // Línea que va debajo del nombre en las tarjetas
    public String getResumen() {
        return switch (categoria) {
            case "clubes" -> "Temporada 25/26 · Adulto · S–XXL";
            case "selecciones" -> "Selecciones · Adulto · S–XXL";
            default -> "Kingdom Studio · Corte relajado · S–XXL";
        };
    }
}
