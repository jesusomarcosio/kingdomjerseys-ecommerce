package com.kingdomjerseys.admin.config;

import com.kingdomjerseys.admin.model.Administrador;
import com.kingdomjerseys.admin.model.EstadoPedido;
import com.kingdomjerseys.admin.model.Pedido;
import com.kingdomjerseys.admin.model.Producto;
import com.kingdomjerseys.admin.repository.AdministradorRepository;
import com.kingdomjerseys.admin.repository.PedidoRepository;
import com.kingdomjerseys.admin.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DatosIniciales implements CommandLineRunner {

    private static final String CORREO_ADMIN = "admin@kingdomjerseys.com";
    private static final String CONTRASENIA_ADMIN = "Admin123!";

    private final AdministradorRepository administradorRepository;
    private final ProductoRepository productoRepository;
    private final PedidoRepository pedidoRepository;
    private final PasswordEncoder passwordEncoder;

    public DatosIniciales(AdministradorRepository administradorRepository,
                          ProductoRepository productoRepository,
                          PedidoRepository pedidoRepository,
                          PasswordEncoder passwordEncoder) {
        this.administradorRepository = administradorRepository;
        this.productoRepository = productoRepository;
        this.pedidoRepository = pedidoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (administradorRepository.findByCorreo(CORREO_ADMIN).isEmpty()) {
            administradorRepository.save(new Administrador(
                    "Administrador KingdomJerseys",
                    CORREO_ADMIN,
                    passwordEncoder.encode(CONTRASENIA_ADMIN)));
        }

        if (productoRepository.count() == 0) {
            List<Producto> productos = cargarProductos();
            cargarPedidos(productos);
        }
    }

    private List<Producto> cargarProductos() {
        return productoRepository.saveAll(List.of(
                producto("Madrid · Camiseta local", "Clubes", 600, 18, "madrid-local", null, false),
                producto("Barcelona · Camiseta local", "Clubes", 600, 12, "barcelona-local", "FAVORITA", false),
                producto("Real Madrid · Visitante", "Clubes", 600, 3, "madrid-visitante", null, false),
                producto("Boca Juniors · Local", "Clubes", 600, 9, "boca-local", "NUEVA", true),
                producto("Juventus · Local", "Clubes", 600, 4, "juventus-local", null, false),
                producto("PSG · Local", "Clubes", 600, 15, "psg-local", null, false),
                producto("River Plate · Local", "Clubes", 600, 10, "river-local", null, false),
                producto("Argentina", "Selecciones", 600, 20, "argentina", "MÁS VENDIDA", false),
                producto("Brasil", "Selecciones", 600, 14, "brasil", null, false),
                producto("México · Selección", "Selecciones", 600, 2, "mexico-seleccion", null, false),
                producto("Alemania · Selección", "Selecciones", 600, 11, "alemania-seleccion", "NUEVA", true),
                producto("Cancha · Verde tribuna", "Kingdom Studio", 500, 8, "cancha-verde", "NUEVA", true),
                producto("Cancha · Hueso y verde", "Kingdom Studio", 500, 6, "cancha-hueso", "NUEVA", true)));
    }

    private Producto producto(String nombre, String categoria, int precio, int stock,
                              String imagen, String etiqueta, boolean novedad) {
        Producto producto = new Producto(
                nombre,
                categoria,
                "Tejido ligero transpirable, escudo bordado y corte entallado.",
                BigDecimal.valueOf(precio),
                stock);
        producto.setImagen("/imgs/" + imagen + ".jpg");
        producto.setEtiqueta(etiqueta);
        producto.setNovedad(novedad);
        return producto;
    }

    private void cargarPedidos(List<Producto> productos) {
        pedido(productos.get(0), 1, "M", "Carlos Mendoza", "carlos.mendoza@correo.com",
                "Blvd. Kino 120, Hermosillo, Son.", EstadoPedido.ENTREGADO, LocalDateTime.now().minusDays(20));
        pedido(productos.get(1), 2, "S", "Daniela Ortiz", "daniela.ortiz@correo.com",
                "Calle Sonora 45, Cd. Obregón, Son.", EstadoPedido.ENVIADO, LocalDateTime.now().minusDays(4));
        pedido(productos.get(9), 1, "XL", "Luis Fernández", "luis.fernandez@correo.com",
                "Av. Universidad 310, Cd. Obregón, Son.", EstadoPedido.PAGADO, LocalDateTime.now().minusDays(2));
        pedido(productos.get(3), 1, "L", "Carlos Mendoza", "carlos.mendoza@correo.com",
                "Blvd. Kino 120, Hermosillo, Son.", EstadoPedido.PENDIENTE, LocalDateTime.now().minusHours(3));
    }

    private void pedido(Producto producto, int cantidad, String talla, String cliente, String correo,
                        String direccion, EstadoPedido estado, LocalDateTime fecha) {
        Pedido pedido = new Pedido(producto, cantidad, talla, cliente, correo, direccion, fecha);
        pedido.setEstado(estado);
        pedidoRepository.save(pedido);
    }
}
