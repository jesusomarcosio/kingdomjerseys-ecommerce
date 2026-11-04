package com.kingdomjerseys.ecommerce.service;

import com.kingdomjerseys.ecommerce.dto.AdministradorForm;
import com.kingdomjerseys.ecommerce.model.Administrador;
import com.kingdomjerseys.ecommerce.repository.AdministradorRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Service
@Transactional(readOnly = true)
public class AdministradorService {

    /** BCrypt solo usa los primeros 72 bytes de la contraseña. */
    private static final int MAX_BYTES_CONTRASENIA = 72;

    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    public AdministradorService(AdministradorRepository administradorRepository, PasswordEncoder passwordEncoder) {
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<AdministradorResumen> listar() {
        return administradorRepository.findAll(Sort.by("nombre").ascending()).stream()
                .map(a -> new AdministradorResumen(a.getId(), a.getNombre(), a.getCorreo(), a.isActivo()))
                .toList();
    }

    /** Lo usa el filtro de sesión: ¿esta cuenta sigue existiendo y activa? */
    public boolean estaActivo(String correo) {
        return administradorRepository.findByCorreoIgnoreCase(correo)
                .map(Administrador::isActivo)
                .orElse(false);
    }

    @Transactional
    public Administrador crear(AdministradorForm form) {
        if (!form.getContrasenia().equals(form.getConfirmacion())) {
            throw new ReglaNegocioException("confirmacion", "Las contraseñas no coinciden");
        }
        if (form.getContrasenia().getBytes(StandardCharsets.UTF_8).length > MAX_BYTES_CONTRASENIA) {
            throw new ReglaNegocioException("contrasenia", "La contraseña es demasiado larga; usa menos caracteres especiales");
        }
        String correo = form.getCorreo().trim().toLowerCase(Locale.ROOT);
        if (administradorRepository.existsByCorreoIgnoreCase(correo)) {
            throw new ReglaNegocioException("correo", "Ya existe un administrador con ese correo");
        }
        Administrador admin = new Administrador(form.getNombre().trim(), correo,
                passwordEncoder.encode(form.getContrasenia()));
        admin.setActivo(true);
        try {
            return administradorRepository.saveAndFlush(admin);
        } catch (DataIntegrityViolationException e) {   // dos altas simultáneas con el mismo correo
            throw new ReglaNegocioException("correo", "Ya existe un administrador con ese correo");
        }
    }

    /**
     * Activa o desactiva una cuenta.
     * @param correoActual correo de quien hace la acción (la sesión actual)
     */
    @Transactional
    public Administrador alternarActivo(Long id, String correoActual) {
        Administrador admin = administradorRepository.findById(id)
                .orElseThrow(() -> new ReglaNegocioException("El administrador no existe"));
        if (admin.isActivo()) {
            if (admin.getCorreo().equalsIgnoreCase(correoActual)) {
                throw new ReglaNegocioException("No puedes desactivar tu propia cuenta.");
            }
            if (administradorRepository.countByActivoTrue() <= 1) {
                throw new ReglaNegocioException("Debe quedar al menos un administrador activo.");
            }
        }
        admin.setActivo(!admin.isActivo());
        return admin;
    }

    public record AdministradorResumen(Long id, String nombre, String correo, boolean activo) {}
}