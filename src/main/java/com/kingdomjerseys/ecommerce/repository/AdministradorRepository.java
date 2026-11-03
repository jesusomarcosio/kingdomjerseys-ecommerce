package com.kingdomjerseys.ecommerce.repository;

import com.kingdomjerseys.ecommerce.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {

    Optional<Administrador> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
}