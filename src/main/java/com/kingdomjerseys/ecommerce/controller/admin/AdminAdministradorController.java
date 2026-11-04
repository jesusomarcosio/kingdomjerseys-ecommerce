package com.kingdomjerseys.ecommerce.controller.admin;

import com.kingdomjerseys.ecommerce.dto.AdministradorForm;
import com.kingdomjerseys.ecommerce.model.Administrador;
import com.kingdomjerseys.ecommerce.service.AdministradorService;
import com.kingdomjerseys.ecommerce.service.ReglaNegocioException;
import jakarta.validation.Valid;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/administradores")
public class AdminAdministradorController {

    private static final String VISTA_LISTA = "panel-admin/administradores/lista";
    private static final String VISTA_FORMULARIO = "panel-admin/administradores/formulario";

    private final AdministradorService administradorService;

    public AdminAdministradorController(AdministradorService administradorService) {
        this.administradorService = administradorService;
    }

    @InitBinder
    public void recortarEspacios(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, "nombre", new StringTrimmerEditor(false));
        binder.registerCustomEditor(String.class, "correo", new StringTrimmerEditor(false));
    }

    @GetMapping
    public String lista(Authentication authentication, Model model) {
        model.addAttribute("administradores", administradorService.listar());
        model.addAttribute("correoActual", authentication.getName());
        return VISTA_LISTA;
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("form", new AdministradorForm());
        return VISTA_FORMULARIO;
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("form") AdministradorForm form, BindingResult errores,
                        RedirectAttributes ra) {
        if (!errores.hasErrors()) {
            try {
                Administrador creado = administradorService.crear(form);
                ra.addFlashAttribute("mensaje", "Administrador «" + creado.getNombre() + "» creado.");
                return "redirect:/admin/administradores";
            } catch (ReglaNegocioException e) {
                registrarError(errores, e);
            }
        }
        return VISTA_FORMULARIO;
    }

    @PostMapping("/{id}/activo")
    public String alternarActivo(@PathVariable Long id, Authentication authentication, RedirectAttributes ra) {
        try {
            Administrador admin = administradorService.alternarActivo(id, authentication.getName());
            ra.addFlashAttribute("mensaje", "«" + admin.getNombre() + "» ahora está "
                    + (admin.isActivo() ? "activo" : "inactivo") + ".");
        } catch (ReglaNegocioException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/administradores";
    }

    private void registrarError(BindingResult errores, ReglaNegocioException e) {
        if (e.getCampo() != null) {
            errores.rejectValue(e.getCampo(), "regla", e.getMessage());
        } else {
            errores.reject("regla", e.getMessage());
        }
    }
}