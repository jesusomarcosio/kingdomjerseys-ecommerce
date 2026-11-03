package com.kingdomjerseys.ecommerce.controller.admin;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminAuthController {

    @GetMapping("/admin/login")
    public String login(Authentication auth) {
        boolean yaAutenticado = auth != null
                && auth.isAuthenticated()
                && !(auth instanceof AnonymousAuthenticationToken);
        return yaAutenticado ? "redirect:/admin/productos" : "login";
    }

    @GetMapping("/admin")
    public String raiz() {
        return "redirect:/admin/productos";
    }
}