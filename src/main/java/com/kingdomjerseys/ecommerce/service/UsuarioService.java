package com.kingdomjerseys.ecommerce.service;

import com.kingdomjerseys.ecommerce.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UsuarioService {


    private final Map<String, Usuario> usuarios = new ConcurrentHashMap<>();

    public UsuarioService() {
    }

    /**
     * Registra un nuevo usuario en la lista en memoria.
     * @param usuario Datos del usuario provenientes del formulario.
     */
    public void registrar(Usuario usuario) {

        usuarios.put(usuario.getCorreo(), usuario);
    }
    public boolean existeCorreo(String correo) {
        return usuarios.containsKey(correo);
    }


    public boolean autenticar(String correo, String contrasenia) {
        Usuario usuario = usuarios.get(correo);
        return usuario != null && usuario.getContrasenia().equals(contrasenia);
    }

}
