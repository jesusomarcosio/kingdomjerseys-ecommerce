package com.kingdomjerseys.ecommerce.controller;

import com.kingdomjerseys.ecommerce.model.ItemCarrito;
import com.kingdomjerseys.ecommerce.model.Usuario;
import com.kingdomjerseys.ecommerce.service.CarritoService;
import com.kingdomjerseys.ecommerce.service.ProductoService;
import com.kingdomjerseys.ecommerce.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class MainController {

    private static final List<String> CATEGORIAS = List.of("todas", "clubes", "selecciones", "retro");

    private final UsuarioService usuarioService;
    private final ProductoService productoService;
    private final CarritoService carritoService;

    public MainController(UsuarioService usuarioService,
                          ProductoService productoService,
                          CarritoService carritoService) {
        this.usuarioService = usuarioService;
        this.productoService = productoService;
        this.carritoService = carritoService;
    }

    @GetMapping({"/", "/index", "/index.html"})
    public String index() {
        return "index";
    }

    // /novedades?categoria=clubes&buscar=real  (los dos parámetros son opcionales)
    @GetMapping({"/novedades", "/novedades.html"})
    public String novedades(@RequestParam(name = "categoria", defaultValue = "todas") String categoria,
                            @RequestParam(name = "buscar", defaultValue = "") String buscar,
                            Model model) {
        if (!CATEGORIAS.contains(categoria)) {
            categoria = "todas"; // una categoría inventada se trata como "todas"
        }
        buscar = buscar.trim();

        model.addAttribute("productos", productoService.filtrar(categoria, buscar));
        model.addAttribute("categoria", categoria);
        model.addAttribute("buscar", buscar.isEmpty() ? null : buscar);
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

    // Cada camiseta tiene su página: /producto/mexico-seleccion, /producto/argentina...
    @GetMapping("/producto/{id}")
    public String producto(@PathVariable String id,
                           @RequestParam(name = "cantidad", defaultValue = "1") int cantidad,
                           Model model) {
        model.addAttribute("producto", productoService.buscar(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        // La cantidad elegida viaja en la dirección: /producto/argentina?cantidad=3
        model.addAttribute("cantidad", Math.clamp(cantidad, 1, CarritoService.CANTIDAD_MAXIMA));
        return "producto";
    }

    @GetMapping("/producto")
    public String productoSinId() {
        return "redirect:/novedades";
    }

    @GetMapping("/carrito")
    public String carrito(Model model) {
        resumen(model, carritoService.getItems(), carritoService.getSubtotal());
        return "carrito";
    }

    @PostMapping("/carrito/agregar")
    public String agregarAlCarrito(@RequestParam("id") String id,
                                   @RequestParam(name = "cantidad", defaultValue = "1") int cantidad) {
        carritoService.agregar(id, cantidad);
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/cantidad")
    public String cambiarCantidad(@RequestParam("id") String id,
                                  @RequestParam("delta") int delta) {
        carritoService.cambiarCantidad(id, delta);
        return "redirect:/carrito";
    }

    @PostMapping("/carrito/remover")
    public String removerDelCarrito(@RequestParam("id") String id) {
        carritoService.remover(id);
        return "redirect:/carrito";
    }

    // Los pasos de la compra necesitan al menos una camiseta en el carrito
    @GetMapping("/datos")
    public String datos(Model model) {
        return pasoDeCompra("datos", model);
    }

    @GetMapping("/envio")
    public String envio(Model model) {
        return pasoDeCompra("envio", model);
    }

    @GetMapping("/pago")
    public String pago(Model model) {
        return pasoDeCompra("pago", model);
    }

    @PostMapping("/pago")
    public String pagar() {
        if (carritoService.estaVacio()) {
            return "redirect:/carrito";
        }
        carritoService.confirmarCompra();
        return "redirect:/pago-confirmado";
    }

    @GetMapping("/pago-confirmado")
    public String pagoConfirmado(Model model) {
        if (carritoService.getUltimaOrden().isEmpty()) {
            return "redirect:/carrito";
        }
        resumen(model, carritoService.getUltimaOrden(), carritoService.getSubtotalUltimaOrden());
        return "pago-confirmado";
    }

    private String pasoDeCompra(String vista, Model model) {
        if (carritoService.estaVacio()) {
            return "redirect:/carrito";
        }
        resumen(model, carritoService.getItems(), carritoService.getSubtotal());
        return vista;
    }

    private void resumen(Model model, List<ItemCarrito> items, int subtotal) {
        model.addAttribute("items", items);
        model.addAttribute("subtotal", subtotal);
    }

}
