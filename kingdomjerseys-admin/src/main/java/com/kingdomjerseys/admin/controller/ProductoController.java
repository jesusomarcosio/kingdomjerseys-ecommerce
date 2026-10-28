package com.kingdomjerseys.admin.controller;

import com.kingdomjerseys.admin.model.Producto;
import com.kingdomjerseys.admin.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/productos")
public class ProductoController {

    private static final List<String> CATEGORIAS = List.of("Clubes", "Selecciones", "Retro", "Kingdom Studio");

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("productos", productoService.listar(q));
        model.addAttribute("q", q);
        return "productos/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", CATEGORIAS);
        return "productos/formulario";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("producto", productoService.buscarPorId(id));
            model.addAttribute("categorias", CATEGORIAS);
            return "productos/formulario";
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/productos";
        }
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("producto") Producto producto,
                          BindingResult resultado,
                          Model model,
                          RedirectAttributes redirect) {
        model.addAttribute("categorias", CATEGORIAS);

        if (resultado.hasErrors()) {
            return "productos/formulario";
        }

        boolean esNuevo = producto.getId() == null;
        productoService.guardar(producto);
        redirect.addFlashAttribute("mensaje", esNuevo ? "Producto creado" : "Producto actualizado");
        return "redirect:/admin/productos";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            productoService.eliminar(id);
            redirect.addFlashAttribute("mensaje", "Producto eliminado");
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/productos";
    }
}
