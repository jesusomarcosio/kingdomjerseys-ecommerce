package com.kingdomjerseys.ecommerce.service;

import com.kingdomjerseys.ecommerce.model.Administrador;
import com.kingdomjerseys.ecommerce.repository.AdministradorRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminUserDetailsService implements UserDetailsService {
    private final AdministradorRepository administradorRepository;

    public AdminUserDetailsService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    /** "username" = correo del administrador (sin importar mayúsculas). */
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Administrador admin = administradorRepository.findByCorreoIgnoreCase(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Administrador no encontrado"));
        return User.withUsername(admin.getCorreo())      // el nombre de la sesión es el correo guardado
                .password(admin.getContrasenia())        // hash BCrypt
                .roles("ADMIN")
                .disabled(!admin.isActivo())
                .build();
    }
}