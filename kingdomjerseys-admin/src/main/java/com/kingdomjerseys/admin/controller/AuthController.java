package com.kingdomjerseys.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class AuthController {

    @GetMapping({"/", "/admin"})
    public String inicio() {
        return "redirect:/admin/productos";
    }

    @GetMapping("/admin/login")
    public String login(Principal principal) {
        if (principal != null) {
            return "redirect:/admin/productos";
        }
        return "login";
    }
}
