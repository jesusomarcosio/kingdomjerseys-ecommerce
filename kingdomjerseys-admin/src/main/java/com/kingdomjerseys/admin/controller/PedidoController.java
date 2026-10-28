package com.kingdomjerseys.admin.controller;

import com.kingdomjerseys.admin.model.EstadoPedido;
import com.kingdomjerseys.admin.service.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) EstadoPedido estado, Model model) {
        model.addAttribute("pedidos", pedidoService.listar(estado));
        model.addAttribute("estados", EstadoPedido.values());
        model.addAttribute("estadoFiltro", estado);
        return "pedidos/lista";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model, RedirectAttributes redirect) {
        try {
            model.addAttribute("pedido", pedidoService.buscarPorId(id));
            model.addAttribute("estados", EstadoPedido.values());
            return "pedidos/detalle";
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/pedidos";
        }
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Long id,
                                @RequestParam EstadoPedido estado,
                                RedirectAttributes redirect) {
        try {
            pedidoService.cambiarEstado(id, estado);
            redirect.addFlashAttribute("mensaje", "Pedido #" + id + " marcado como " + estado.getEtiqueta().toLowerCase());
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/pedidos/" + id;
    }
}
