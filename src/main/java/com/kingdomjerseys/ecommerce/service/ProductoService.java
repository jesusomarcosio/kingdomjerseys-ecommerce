package com.kingdomjerseys.ecommerce.service;

import com.kingdomjerseys.ecommerce.model.enums.Categoria;
import com.kingdomjerseys.ecommerce.model.Producto;
import com.kingdomjerseys.ecommerce.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /** Busca un producto activo por su slug (el que aparece en /producto/{slug}). */
    public Optional<Producto> buscar(String slug) {
        return productoRepository.findBySlugAndActivoTrue(slug);
    }

    // Sin filtros se muestran las novedades; con categoría o búsqueda, todo el catálogo.
    public List<Producto> filtrar(String categoria, String texto) {
        boolean sinFiltros = categoria.equals("todas") && texto.isEmpty();
        String buscado = normalizar(texto);

        List<Producto> base = categoria.equals("todas")
                ? productoRepository.findByActivoTrue()
                : Categoria.desdeSlug(categoria)
                .map(productoRepository::findByActivoTrueAndCategoria)
                .orElse(List.of());

        return base.stream()
                .filter(p -> !sinFiltros || p.isNovedad())
                .filter(p -> normalizar(p.getNombre()).contains(buscado))
                .toList();
    }

    // "México" = "mexico": minúsculas y sin acentos
    private String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim();
    }
}