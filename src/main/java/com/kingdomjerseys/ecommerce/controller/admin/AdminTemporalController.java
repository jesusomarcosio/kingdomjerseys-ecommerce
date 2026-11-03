package com.kingdomjerseys.ecommerce.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminTemporalController {

    @GetMapping("/admin/productos")   // TEMPORAL: se elimina en la Fase 4
    public String inicio() {
        return "panel-admin/admin-inicio";
    }
}