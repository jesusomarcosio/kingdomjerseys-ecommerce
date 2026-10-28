package com.kingdomjerseys.admin.service;

import com.kingdomjerseys.admin.model.Administrador;
import com.kingdomjerseys.admin.repository.AdministradorRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdministradorDetailsService implements UserDetailsService {

    private final AdministradorRepository administradorRepository;

    public AdministradorDetailsService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Administrador administrador = administradorRepository.findByCorreo(correo.trim().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("No existe un administrador con ese correo"));

        return User.withUsername(administrador.getCorreo())
                .password(administrador.getContrasenia())
                .roles("ADMIN")
                .disabled(!administrador.isActivo())
                .build();
    }
}
