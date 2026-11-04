package com.kingdomjerseys.ecommerce.repository;

import com.kingdomjerseys.ecommerce.model.Producto;
import com.kingdomjerseys.ecommerce.model.enums.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findBySlugAndActivoTrue(String slug);
    List<Producto> findByActivoTrue();
    List<Producto> findByActivoTrueAndCategoria(Categoria categoria);
    boolean existsBySlug(String slug);

    // Dashboard
    long countByActivoTrue();
    List<Producto> findByActivoTrueAndStockLessThanEqualOrderByStockAsc(Integer stock);

    // Panel de administración (incluye inactivos)
    Page<Producto> findByNombreContainingIgnoreCase(String texto, Pageable pageable);
    Page<Producto> findByCategoriaAndNombreContainingIgnoreCase(Categoria categoria, String texto, Pageable pageable);
}