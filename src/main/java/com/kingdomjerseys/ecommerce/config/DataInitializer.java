package com.kingdomjerseys.ecommerce.config;

import com.kingdomjerseys.ecommerce.model.Administrador;
import com.kingdomjerseys.ecommerce.model.enums.Categoria;
import com.kingdomjerseys.ecommerce.model.Producto;
import com.kingdomjerseys.ecommerce.repository.AdministradorRepository;
import com.kingdomjerseys.ecommerce.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/** Datos mock para desarrollo. Solo inserta si las tablas están vacías. */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final int STOCK_INICIAL = 20;
    private static final String DESC_GENERICA =
            ". Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.";
    private static final String DESC_STUDIO =
            "Diseño propio de Kingdom Studio. Corte relajado para usarse todos los días.";

    private final ProductoRepository productoRepository;
    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ProductoRepository productoRepository,
                           AdministradorRepository administradorRepository,
                           PasswordEncoder passwordEncoder) {
        this.productoRepository = productoRepository;
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (administradorRepository.count() == 0) {
            // Solo desarrollo: cambiar esta contraseña fuera de este entorno
            administradorRepository.save(new Administrador(
                    "Administrador", "admin@kingdomjerseys.com", passwordEncoder.encode("Admin123!")));
        }
        if (productoRepository.count() == 0) {
            cargarProductos();
        }
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
}