package com.kingdomjerseys.ecommerce.dto;

import com.kingdomjerseys.ecommerce.model.enums.Categoria;
import jakarta.validation.constraints.*;

public class ProductoForm {

    public static final int STOCK_MAXIMO = 100_000;

    @Size(max = 100, message = "El slug no puede pasar de 100 caracteres")
    @Pattern(regexp = "^$|^[a-z0-9]+(-[a-z0-9]+)*$",
            message = "Usa solo minúsculas, números y guiones (ej. brasil-visitante)")
    private String slug;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede pasar de 150 caracteres")
    private String nombre;

    @NotNull(message = "Elige una categoría")
    private Categoria categoria;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 1000, message = "La descripción no puede pasar de 1000 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 1, message = "El precio debe ser mayor a 0")
    @Max(value = 999_999, message = "El precio es demasiado alto")
    private Integer precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Max(value = STOCK_MAXIMO, message = "El stock es demasiado alto")
    private Integer stock;

    @NotBlank(message = "La imagen es obligatoria")
    @Size(max = 255, message = "Máximo 255 caracteres")
    private String imagen;

    @Size(max = 255, message = "Máximo 255 caracteres")
    private String imagenDetalle;

    @NotBlank(message = "El material es obligatorio")
    @Size(max = 255, message = "Máximo 255 caracteres")
    private String material;

    @NotBlank(message = "El ajuste es obligatorio")
    @Size(max = 255, message = "Máximo 255 caracteres")
    private String ajuste;

    @NotBlank(message = "El color es obligatorio")
    @Size(max = 255, message = "Máximo 255 caracteres")
    private String color;

    @Size(max = 50, message = "La etiqueta no puede pasar de 50 caracteres")
    private String etiqueta;

    private boolean novedad;
    private boolean activo = true;

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getPrecio() { return precio; }
    public void setPrecio(Integer precio) { this.precio = precio; }
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