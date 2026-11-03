package com.kingdomjerseys.ecommerce.model;

import com.kingdomjerseys.ecommerce.model.enums.Categoria;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identificador legible para las URLs de la tienda: /producto/{slug} */
    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Categoria categoria;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock = 0;

    private String imagen;
    private String imagenDetalle;
    private String material;
    private String ajuste;
    private String color;
    private String etiqueta;

    @Column(nullable = false)
    private boolean novedad;

    @Column(nullable = false)
    private boolean activo = true;

    public Producto() {
    }

    // Línea que va debajo del nombre en las tarjetas
    public String getResumen() {
        return switch (categoria) {
            case CLUBES -> "Temporada 25/26 · Adulto · S–XXL";
            case SELECCIONES -> "Selecciones · Adulto · S–XXL";
            case STUDIO -> "Kingdom Studio · Corte relajado · S–XXL";
        };
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
    public String getImagenDetalle() { return imagenDetalle; }
    public void setImagenDetalle(String imagenDetalle) { this.imagenDetalle = imagenDetalle; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public String getAjuste() { return ajuste; }
    public void setAjuste(String ajuste) { this.ajuste = ajuste; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }
    public boolean isNovedad() { return novedad; }
    public void setNovedad(boolean novedad) { this.novedad = novedad; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}