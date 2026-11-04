package com.kingdomjerseys.ecommerce.controller.admin;

import com.kingdomjerseys.ecommerce.model.Pedido;
import com.kingdomjerseys.ecommerce.model.enums.EstadoPedido;
import com.kingdomjerseys.ecommerce.service.PedidoService;
import com.kingdomjerseys.ecommerce.service.PedidoService.PedidoDetalle;
import com.kingdomjerseys.ecommerce.service.ReglaNegocioException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/pedidos")
public class AdminPedidoController {

    private final PedidoService pedidoService;

    public AdminPedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String lista(@RequestParam(defaultValue = "") String estado,
                        @RequestParam(defaultValue = "0") int pagina,
                        Model model) {
        model.addAttribute("resultado", pedidoService.listar(estado, pagina));
        model.addAttribute("estado", estado);
        model.addAttribute("estados", EstadoPedido.values());
        return "panel-admin/pedidos/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model, RedirectAttributes ra) {
        Optional<PedidoDetalle> pedido = pedidoService.detalle(id);
        if (pedido.isEmpty()) {
            ra.addFlashAttribute("error", "El pedido no existe.");
            return "redirect:/admin/pedidos";
        }
        model.addAttribute("pedido", pedido.get());
        return "panel-admin/pedidos/detalle";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id, @RequestParam EstadoPedido estado, RedirectAttributes ra) {
        try {
            Pedido pedido = pedidoService.cambiarEstado(id, estado);
            String mensaje = "Pedido #" + pedido.getId() + " ahora está «" + pedido.getEstado().getEtiqueta() + "».";
            if (pedido.getEstado() == EstadoPedido.CANCELADO) mensaje += " Se repuso el stock.";
            ra.addFlashAttribute("mensaje", mensaje);
        } catch (ReglaNegocioException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/pedidos/" + id;
    }
}