package com.kingdomjerseys.ecommerce.repository;

import com.kingdomjerseys.ecommerce.model.Producto;
import com.kingdomjerseys.ecommerce.model.enums.Categoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /** Suma stock en la base de datos (atómico): no pisa un cambio de stock hecho al mismo tiempo desde el panel. */
    @Modifying(flushAutomatically = true)
    @Query("update Producto p set p.stock = p.stock + :cantidad where p.id = :id")
    int sumarStock(@Param("id") Long id, @Param("cantidad") int cantidad);
}