package com.kingdomjerseys.ecommerce.service;

import com.kingdomjerseys.ecommerce.dto.ProductoForm;
import com.kingdomjerseys.ecommerce.model.Producto;
import com.kingdomjerseys.ecommerce.model.enums.Categoria;
import com.kingdomjerseys.ecommerce.repository.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.Locale;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AdminProductoService {

    public static final int TAMANIO_PAGINA = 10;

    private final ProductoRepository productoRepository;

    public AdminProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /** Lista paginada (incluye inactivos). Categoría desconocida o "todas" = sin filtro. */
    public Page<Producto> listar(String categoriaSlug, String texto, int pagina) {
        Pageable pageable = PageRequest.of(Math.max(pagina, 0), TAMANIO_PAGINA, Sort.by("nombre").ascending());
        String buscado = texto == null ? "" : texto.trim();
        Optional<Categoria> categoria = Categoria.desdeSlug(categoriaSlug);
        return categoria.isPresent()
                ? productoRepository.findByCategoriaAndNombreContainingIgnoreCase(categoria.get(), buscado, pageable)
                : productoRepository.findByNombreContainingIgnoreCase(buscado, pageable);
    }

    public ProductoForm formularioNuevo() {
        ProductoForm form = new ProductoForm();
        form.setStock(0);
        form.setMaterial("100% Poliéster");
        form.setAjuste("Ceñido");
        form.setActivo(true);
        return form;
    }

    public Optional<ProductoForm> formularioDe(Long id) {
        return productoRepository.findById(id).map(p -> {
            ProductoForm form = new ProductoForm();
            form.setSlug(p.getSlug());
            form.setNombre(p.getNombre());
            form.setCategoria(p.getCategoria());
            form.setDescripcion(p.getDescripcion());
            form.setPrecio(p.getPrecio().intValue());   // la tienda solo maneja pesos enteros
            form.setStock(p.getStock());
            form.setImagen(p.getImagen());
            form.setImagenDetalle(p.getImagenDetalle());
            form.setMaterial(p.getMaterial());
            form.setAjuste(p.getAjuste());
            form.setColor(p.getColor());
            form.setEtiqueta(p.getEtiqueta());
            form.setNovedad(p.isNovedad());
            form.setActivo(p.isActivo());
            return form;
        });
    }

    @Transactional
    public Producto crear(ProductoForm form) {
        String slug = texto(form.getSlug());
        if (slug == null) slug = generarSlug(form.getNombre());
        if (slug.isEmpty()) {
            throw new ReglaNegocioException("slug", "No se pudo generar el slug desde el nombre; escríbelo manualmente");
        }
        if (productoRepository.existsBySlug(slug)) {
            throw new ReglaNegocioException("slug", "Ya existe un producto con el slug «" + slug + "»");
        }
        Producto producto = new Producto();
        producto.setSlug(slug);
        copiar(form, producto);
        return productoRepository.save(producto);
    }

    /** El slug no se modifica al editar. */
    @Transactional
    public Producto actualizar(Long id, ProductoForm form) {
        Producto producto = obtenerOError(id);
        copiar(form, producto);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto alternarActivo(Long id) {
        Producto producto = obtenerOError(id);
        producto.setActivo(!producto.isActivo());
        return producto;
    }

    @Transactional
    public Producto actualizarStock(Long id, Integer stock) {
        if (stock == null || stock < 0 || stock > ProductoForm.STOCK_MAXIMO) {
            throw new ReglaNegocioException("El stock debe ser un entero entre 0 y " + ProductoForm.STOCK_MAXIMO);
        }
        Producto producto = obtenerOError(id);
        producto.setStock(stock);
        return producto;
    }

    // ---- internos ----

    private Producto obtenerOError(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ReglaNegocioException("El producto no existe"));
    }

    private void copiar(ProductoForm form, Producto producto) {
        String imagen = texto(form.getImagen());
        String detalle = texto(form.getImagenDetalle());
        producto.setNombre(texto(form.getNombre()));
        producto.setCategoria(form.getCategoria());
        producto.setDescripcion(texto(form.getDescripcion()));
        producto.setPrecio(BigDecimal.valueOf(form.getPrecio()));
        producto.setStock(form.getStock());
        producto.setImagen(imagen);
        producto.setImagenDetalle(detalle != null ? detalle : imagen);
        producto.setMaterial(texto(form.getMaterial()));
        producto.setAjuste(texto(form.getAjuste()));
        producto.setColor(texto(form.getColor()));
        producto.setEtiqueta(texto(form.getEtiqueta()));
        producto.setNovedad(form.isNovedad());
        producto.setActivo(form.isActivo());
    }

    /** Texto recortado, o null si está vacío. */
    private String texto(String valor) {
        if (valor == null) return null;
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    /** "Brasil · Visitante" -> "brasil-visitante" */
    private String generarSlug(String nombre) {
        String base = Normalizer.normalize(nombre, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");
        if (base.length() > 100) base = base.substring(0, 100).replaceAll("-+$", "");
        return base;
    }
}