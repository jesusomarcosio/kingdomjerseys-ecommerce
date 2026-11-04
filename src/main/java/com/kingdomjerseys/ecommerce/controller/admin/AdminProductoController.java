package com.kingdomjerseys.ecommerce.controller.admin;

import com.kingdomjerseys.ecommerce.dto.ProductoForm;
import com.kingdomjerseys.ecommerce.model.Producto;
import com.kingdomjerseys.ecommerce.model.enums.Categoria;
import com.kingdomjerseys.ecommerce.service.AdminProductoService;
import com.kingdomjerseys.ecommerce.service.DashboardService;
import com.kingdomjerseys.ecommerce.service.ReglaNegocioException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/productos")
public class AdminProductoController {

    private static final String VISTA_LISTA = "panel-admin/productos/lista";
    private static final String VISTA_FORMULARIO = "panel-admin/productos/formulario";

    private final AdminProductoService productoService;

    public AdminProductoController(AdminProductoService productoService) {
        this.productoService = productoService;
    }

    @ModelAttribute("categorias")
    public Categoria[] categorias() { return Categoria.values(); }

    @GetMapping
    public String lista(@RequestParam(defaultValue = "todas") String categoria,
                        @RequestParam(defaultValue = "") String buscar,
                        @RequestParam(defaultValue = "0") int pagina,
                        Model model) {
        model.addAttribute("resultado", productoService.listar(categoria, buscar, pagina));
        model.addAttribute("categoria", categoria);
        model.addAttribute("buscar", buscar);
        model.addAttribute("umbralStockBajo", DashboardService.UMBRAL_STOCK_BAJO);
        return VISTA_LISTA;
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("form", productoService.formularioNuevo());
        model.addAttribute("modoEdicion", false);
        return VISTA_FORMULARIO;
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("form") ProductoForm form, BindingResult errores,
                        Model model, RedirectAttributes ra) {
        if (!errores.hasErrors()) {
            try {
                Producto creado = productoService.crear(form);
                ra.addFlashAttribute("mensaje", "Producto «" + creado.getNombre() + "» creado.");
                return "redirect:/admin/productos";
            } catch (ReglaNegocioException e) {
                registrarError(errores, e);
            }
        }
        model.addAttribute("modoEdicion", false);
        return VISTA_FORMULARIO;
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<ProductoForm> form = productoService.formularioDe(id);
        if (form.isEmpty()) {
            ra.addFlashAttribute("error", "El producto no existe.");
            return "redirect:/admin/productos";
        }
        model.addAttribute("form", form.get());
        model.addAttribute("modoEdicion", true);
        model.addAttribute("productoId", id);
        return VISTA_FORMULARIO;
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("form") ProductoForm form, BindingResult errores,
                             Model model, RedirectAttributes ra) {
        if (!errores.hasErrors()) {
            try {
                Producto actualizado = productoService.actualizar(id, form);
                ra.addFlashAttribute("mensaje", "Producto «" + actualizado.getNombre() + "» actualizado.");
                return "redirect:/admin/productos";
            } catch (ReglaNegocioException e) {
                registrarError(errores, e);
            }
        }
        model.addAttribute("modoEdicion", true);
        model.addAttribute("productoId", id);
        return VISTA_FORMULARIO;
    }

    @PostMapping("/{id}/activo")
    public String alternarActivo(@PathVariable Long id,
                                 @RequestParam(defaultValue = "todas") String categoria,
                                 @RequestParam(defaultValue = "") String buscar,
                                 @RequestParam(defaultValue = "0") int pagina,
                                 RedirectAttributes ra) {
        try {
            Producto producto = productoService.alternarActivo(id);
            ra.addFlashAttribute("mensaje", "«" + producto.getNombre() + "» ahora está "
                    + (producto.isActivo() ? "activo" : "inactivo") + ".");
        } catch (ReglaNegocioException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return volverALista(ra, categoria, buscar, pagina);
    }

    @PostMapping("/{id}/stock")
    public String actualizarStock(@PathVariable Long id,
                                  @RequestParam(required = false) Integer stock,
                                  @RequestParam(defaultValue = "todas") String categoria,
                                  @RequestParam(defaultValue = "") String buscar,
                                  @RequestParam(defaultValue = "0") int pagina,
                                  RedirectAttributes ra) {
        try {
            Producto producto = productoService.actualizarStock(id, stock);
            ra.addFlashAttribute("mensaje", "Stock de «" + producto.getNombre() + "»: " + producto.getStock() + " unidades.");
        } catch (ReglaNegocioException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return volverALista(ra, categoria, buscar, pagina);
    }

    // Conserva filtros y página como parámetros de la URL (no se redirige a una URL enviada por el cliente)
    private String volverALista(RedirectAttributes ra, String categoria, String buscar, int pagina) {
        ra.addAttribute("categoria", categoria);
        ra.addAttribute("buscar", buscar);
        ra.addAttribute("pagina", pagina);
        return "redirect:/admin/productos";
    }

    private void registrarError(BindingResult errores, ReglaNegocioException e) {
        if (e.getCampo() != null) {
            errores.rejectValue(e.getCampo(), "regla", e.getMessage());
        } else {
            errores.reject("regla", e.getMessage());
        }
    }
}