package com.kingdomjerseys.admin.repository;

import com.kingdomjerseys.admin.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findAllByOrderByNombreAsc();

    List<Producto> findByNombreContainingIgnoreCaseOrderByNombreAsc(String texto);
}
