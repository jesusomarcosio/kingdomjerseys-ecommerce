package com.kingdomjerseys.ecommerce.controller;

import com.kingdomjerseys.ecommerce.model.Usuario;
import com.kingdomjerseys.ecommerce.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    private final UsuarioService usuarioService;

    public MainController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping({"/", "/index", "/index.html"})
    public String index() {
        return "index";
    }

    @GetMapping({"/novedades", "/novedades.html"})
    public String novedades() {
        return "novedades";
    }

    @GetMapping({"/iniciar-sesion", "/iniciar-sesion.html"})
    public String iniciarSesion() {
        return "iniciar-sesion";
    }

    @GetMapping({"/registro", "/registro.html"})
    public String registro() {
        return "registro";
    }

    @GetMapping("/error.html")
    public String errorPage() {
        return "error";
    }

    @PostMapping("/crear-cuenta")
    public String crearCuenta(@ModelAttribute Usuario usuario) {
        usuarioService.registrar(usuario);
        return "redirect:/iniciar-sesion";
    }

    @PostMapping("/autenticacion")
    public String autenticacion(@RequestParam("correo") String correo,
                                @RequestParam("contrasenia") String contrasenia) {
        if (usuarioService.autenticar(correo, contrasenia)) {
            return "redirect:/index";
        }
        return "redirect:/error.html";
    }

    @GetMapping("/cerrar-sesion")
    public String cerrarSesion() {
        return "redirect:/iniciar-sesion?logout";
    }

    @GetMapping("/producto")
    public String producto() {
        return "producto";
    }

    @GetMapping("/carrito")
    public String carrito() {
        return "carrito";
    }

    @GetMapping("/datos")
    public String datos() {
        return "datos";
    }

    @GetMapping("/envio")
    public String envio() {
        return "envio";
    }

    @GetMapping("/pago")
    public String pago() {
        return "pago";
    }

    @GetMapping("/pago-confirmado")
    public String pagoConfirmado() {
        return "pago-confirmado";
    }

}
