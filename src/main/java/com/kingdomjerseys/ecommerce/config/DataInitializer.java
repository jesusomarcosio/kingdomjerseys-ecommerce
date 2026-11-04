package com.kingdomjerseys.ecommerce.config;

import com.kingdomjerseys.ecommerce.model.Administrador;
import com.kingdomjerseys.ecommerce.model.DetallePedido;
import com.kingdomjerseys.ecommerce.model.Pedido;
import com.kingdomjerseys.ecommerce.model.Producto;
import com.kingdomjerseys.ecommerce.model.enums.Categoria;
import com.kingdomjerseys.ecommerce.model.enums.EstadoPedido;
import com.kingdomjerseys.ecommerce.repository.AdministradorRepository;
import com.kingdomjerseys.ecommerce.repository.PedidoRepository;
import com.kingdomjerseys.ecommerce.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final int STOCK_INICIAL = 20;
    private static final String DESC_GENERICA =
            ". Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.";
    private static final String DESC_STUDIO =
            "Diseño propio de Kingdom Studio. Corte relajado para usarse todos los días.";

    private final ProductoRepository productoRepository;
    private final AdministradorRepository administradorRepository;
    private final PedidoRepository pedidoRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ProductoRepository productoRepository,
                           AdministradorRepository administradorRepository,
                           PedidoRepository pedidoRepository,
                           PasswordEncoder passwordEncoder) {
        this.productoRepository = productoRepository;
        this.administradorRepository = administradorRepository;
        this.pedidoRepository = pedidoRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (administradorRepository.count() == 0) {
            // Solo desarrollo
            administradorRepository.save(new Administrador(
                    "Administrador", "admin@kingdomjerseys.com", passwordEncoder.encode("Admin123!")));
        }
        if (productoRepository.count() == 0) cargarProductos();
        if (pedidoRepository.count() == 0) cargarPedidos();
    }

    private void cargarProductos() {
        producto("madrid-visitante", "Real Madrid · Visitante", Categoria.CLUBES, null, 600,
                "madrid-visitante.jpg", "madrid-visitante.jpg", "Ceñido", "Verde oscuro", true, "NUEVA");
        producto("boca-local", "Boca Juniors · Local", Categoria.CLUBES, null, 600,
                "boca-local.jpg", "boca-local.jpg", "Ceñido", "Azul y amarillo", true, null);
        producto("mexico-seleccion", "México · Selección", Categoria.SELECCIONES,
                "Diseñado para los aficionados apasionados que llevan su orgullo a flor de piel, el Jersey Local "
                        + "Selección Nacional de México 26 es algo más que un uniforme; es un símbolo de unidad y celebración.",
                600, "mexico-seleccion.jpg", "mexico-local.jpg", "Ceñido", "Verde intenso", true, "MÁS VENDIDA");
        producto("liverpool-local", "Liverpool · Local", Categoria.CLUBES, null, 600,
                "liverpool-local.jpg", "liverpool-local.jpg", "Ceñido", "Rojo", true, null);
        producto("juventus-local", "Juventus · Local", Categoria.CLUBES, null, 600,
                "juventus-local.jpg", "juventus-local.jpg", "Ceñido", "Blanco y negro", true, null);
        producto("river-local", "River Plate · Local", Categoria.CLUBES, null, 600,
                "river-local.jpg", "river-local.jpg", "Ceñido", "Blanco con franja roja", true, "NUEVA");
        producto("alemania-seleccion", "Alemania · Selección", Categoria.SELECCIONES, null, 600,
                "alemania-seleccion.jpg", "alemania-seleccion.jpg", "Ceñido", "Blanco", true, null);
        producto("psg-local", "PSG · Local", Categoria.CLUBES, null, 600,
                "psg-local.jpg", "psg-local.jpg", "Ceñido", "Azul y rojo", true, null);
        producto("madrid-local", "Madrid · Camiseta local", Categoria.CLUBES, null, 600,
                "madrid-local.jpg", "madrid-local.jpg", "Ceñido", "Blanco", false, null);
        producto("barcelona-local", "Barcelona · Camiseta local", Categoria.CLUBES, null, 600,
                "barcelona-local.jpg", "barcelona-local.jpg", "Ceñido", "Azul y grana", false, "FAVORITA");
        producto("argentina", "Argentina", Categoria.SELECCIONES, null, 600,
                "argentina.jpg", "argentina.jpg", "Ceñido", "Celeste y blanco", false, "MÁS VENDIDA");
        producto("brasil", "Brasil", Categoria.SELECCIONES, null, 600,
                "brasil.jpg", "brasil.jpg", "Ceñido", "Amarillo", false, null);
        producto("cancha-verde", "Cancha · Verde tribuna", Categoria.STUDIO, DESC_STUDIO, 500,
                "cancha-verde.jpg", "cancha-verde.jpg", "Relajado", "Verde", false, null);
        producto("cancha-hueso", "Cancha · Hueso y verde", Categoria.STUDIO, DESC_STUDIO, 500,
                "cancha-hueso.jpg", "cancha-hueso.jpg", "Relajado", "Hueso y verde", false, null);
    }

    private void producto(String slug, String nombre, Categoria categoria, String descripcion, int precio,
                          String imagen, String imagenDetalle, String ajuste, String color,
                          boolean novedad, String etiqueta) {
        Producto p = new Producto();
        p.setSlug(slug);
        p.setNombre(nombre);
        p.setCategoria(categoria);
        p.setDescripcion(descripcion != null ? descripcion : nombre + DESC_GENERICA);
        p.setPrecio(BigDecimal.valueOf(precio));
        p.setStock(STOCK_INICIAL);
        p.setImagen(imagen);
        p.setImagenDetalle(imagenDetalle);
        p.setMaterial("100% Poliéster");
        p.setAjuste(ajuste);
        p.setColor(color);
        p.setNovedad(novedad);
        p.setEtiqueta(etiqueta);
        p.setActivo(true);
        productoRepository.save(p);
    }

    // ---- Pedidos de ejemplo (clientes ficticios) ----

    private record Linea(String slug, String talla, int cantidad) {}

    private void cargarPedidos() {
        pedido(0, EstadoPedido.PENDIENTE, "Luis Hernández", "luis.hernandez@example.com",
                "Calle Sonora 123, Col. Centro, Cd. Obregón, Sonora, 85000",
                new Linea("mexico-seleccion", "M", 1), new Linea("cancha-verde", "L", 1));
        pedido(1, EstadoPedido.PENDIENTE, "Mariana López", "mariana.lopez@example.com",
                "Av. Reforma 456, Col. Juárez, Ciudad de México, 06600",
                new Linea("psg-local", "S", 1));
        pedido(1, EstadoPedido.PENDIENTE, "Andrés Soto", "andres.soto@example.com",
                "Calle Morelos 88, Col. Centro, Mazatlán, Sinaloa, 82000",
                new Linea("alemania-seleccion", "XL", 1), new Linea("madrid-local", "M", 1));
        pedido(2, EstadoPedido.PAGADO, "Jorge Pérez", "jorge.perez@example.com",
                "Blvd. Kino 789, Col. Valle Verde, Hermosillo, Sonora, 83200",
                new Linea("madrid-visitante", "L", 2));
        pedido(3, EstadoPedido.PAGADO, "Ana Torres", "ana.torres@example.com",
                "Calle 5 de Febrero 321, Col. Centro, Guadalajara, Jalisco, 44100",
                new Linea("argentina", "M", 1), new Linea("brasil", "M", 1), new Linea("barcelona-local", "M", 1));
        pedido(4, EstadoPedido.PAGADO, "Paola Rivera", "paola.rivera@example.com",
                "Av. Obregón 210, Col. Modelo, Guaymas, Sonora, 85440",
                new Linea("cancha-hueso", "M", 1));
        pedido(5, EstadoPedido.ENVIADO, "Diego Morales", "diego.morales@example.com",
                "Av. Universidad 654, Col. Las Palmas, Monterrey, Nuevo León, 64000",
                new Linea("liverpool-local", "XL", 1));
        pedido(7, EstadoPedido.ENVIADO, "Sofía Vargas", "sofia.vargas@example.com",
                "Calle Hidalgo 987, Col. Centro, Navojoa, Sonora, 85800",
                new Linea("cancha-hueso", "S", 2));
        pedido(9, EstadoPedido.ENTREGADO, "Ricardo Núñez", "ricardo.nunez@example.com",
                "Av. Juárez 159, Col. Centro, Cd. Obregón, Sonora, 85000",
                new Linea("boca-local", "L", 1), new Linea("river-local", "L", 1));
        pedido(12, EstadoPedido.ENTREGADO, "Fernanda Ruiz", "fernanda.ruiz@example.com",
                "Calle Madero 45, Col. Centro, Tijuana, Baja California, 22000",
                new Linea("mexico-seleccion", "S", 2));
        pedido(15, EstadoPedido.ENTREGADO, "Héctor Medina", "hector.medina@example.com",
                "Blvd. Díaz Ordaz 902, Col. Chapultepec, Querétaro, Querétaro, 76000",
                new Linea("juventus-local", "M", 1), new Linea("brasil", "L", 1));
        pedido(11, EstadoPedido.CANCELADO, "Valeria Castro", "valeria.castro@example.com",
                "Calle Allende 753, Col. Centro, Culiacán, Sinaloa, 80000",
                new Linea("juventus-local", "M", 1));
    }

    private void pedido(int diasAtras, EstadoPedido estado, String nombre, String correo,
                        String direccion, Linea... lineas) {
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDateTime.now().minusDays(diasAtras).minusHours(diasAtras % 6));
        pedido.setEstado(estado);
        pedido.setNombreCliente(nombre);
        pedido.setCorreoCliente(correo);
        pedido.setDireccionEnvio(direccion);

        BigDecimal total = BigDecimal.ZERO;
        for (Linea linea : lineas) {
            Producto producto = productoRepository.findBySlugAndActivoTrue(linea.slug()).orElseThrow();
            DetallePedido detalle = new DetallePedido(producto, linea.talla(), linea.cantidad(), producto.getPrecio());
            pedido.agregarDetalle(detalle);
            total = total.add(detalle.getSubtotal());
            // Un pedido cancelado ya devolvió su stock; el resto lo tiene descontado
            if (estado != EstadoPedido.CANCELADO) {
                producto.setStock(producto.getStock() - linea.cantidad());
            }
        }
        pedido.setTotal(total);
        pedidoRepository.save(pedido);
    }
}