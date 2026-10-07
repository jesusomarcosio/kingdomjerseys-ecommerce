package com.kingdomjerseys.ecommerce.service;

import com.kingdomjerseys.ecommerce.model.Producto;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    // Catálogo en memoria; el id es el nombre de la foto y sale en la dirección: /producto/{id}
    private final List<Producto> productos = List.of(
            new Producto("madrid-visitante", "Real Madrid · Visitante", "clubes",
                    "Real Madrid · Visitante. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "madrid-visitante.jpg", "madrid-visitante.jpg", "100% Poliéster", "Ceñido", "Verde oscuro",
                    true, "NUEVA"),
            new Producto("boca-local", "Boca Juniors · Local", "clubes",
                    "Boca Juniors · Local. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "boca-local.jpg", "boca-local.jpg", "100% Poliéster", "Ceñido", "Azul y amarillo",
                    true, null),
            new Producto("mexico-seleccion", "México · Selección", "selecciones",
                    "Diseñado para los aficionados apasionados que llevan su orgullo a flor de piel, el Jersey Local Selección Nacional de México 26 es algo más que un uniforme; es un símbolo de unidad y celebración.",
                    600, "mexico-seleccion.jpg", "mexico-local.jpg", "100% Poliéster", "Ceñido", "Verde intenso",
                    true, "MÁS VENDIDA"),
            new Producto("liverpool-local", "Liverpool · Local", "clubes",
                    "Liverpool · Local. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "liverpool-local.jpg", "liverpool-local.jpg", "100% Poliéster", "Ceñido", "Rojo",
                    true, null),
            new Producto("juventus-local", "Juventus · Local", "clubes",
                    "Juventus · Local. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "juventus-local.jpg", "juventus-local.jpg", "100% Poliéster", "Ceñido", "Blanco y negro",
                    true, null),
            new Producto("river-local", "River Plate · Local", "clubes",
                    "River Plate · Local. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "river-local.jpg", "river-local.jpg", "100% Poliéster", "Ceñido", "Blanco con franja roja",
                    true, "NUEVA"),
            new Producto("alemania-seleccion", "Alemania · Selección", "selecciones",
                    "Alemania · Selección. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "alemania-seleccion.jpg", "alemania-seleccion.jpg", "100% Poliéster", "Ceñido", "Blanco",
                    true, null),
            new Producto("psg-local", "PSG · Local", "clubes",
                    "PSG · Local. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "psg-local.jpg", "psg-local.jpg", "100% Poliéster", "Ceñido", "Azul y rojo",
                    true, null),
            new Producto("madrid-local", "Madrid · Camiseta local", "clubes",
                    "Madrid · Camiseta local. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "madrid-local.jpg", "madrid-local.jpg", "100% Poliéster", "Ceñido", "Blanco",
                    false, null),
            new Producto("barcelona-local", "Barcelona · Camiseta local", "clubes",
                    "Barcelona · Camiseta local. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "barcelona-local.jpg", "barcelona-local.jpg", "100% Poliéster", "Ceñido", "Azul y grana",
                    false, "FAVORITA"),
            new Producto("argentina", "Argentina", "selecciones",
                    "Argentina. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "argentina.jpg", "argentina.jpg", "100% Poliéster", "Ceñido", "Celeste y blanco",
                    false, "MÁS VENDIDA"),
            new Producto("brasil", "Brasil", "selecciones",
                    "Brasil. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "brasil.jpg", "brasil.jpg", "100% Poliéster", "Ceñido", "Amarillo",
                    false, null),
            new Producto("cancha-verde", "Cancha · Verde tribuna", "studio",
                    "Diseño propio de Kingdom Studio. Corte relajado para usarse todos los días.",
                    500, "cancha-verde.jpg", "cancha-verde.jpg", "100% Poliéster", "Relajado", "Verde",
                    false, null),
            new Producto("cancha-hueso", "Cancha · Hueso y verde", "studio",
                    "Diseño propio de Kingdom Studio. Corte relajado para usarse todos los días.",
                    500, "cancha-hueso.jpg", "cancha-hueso.jpg", "100% Poliéster", "Relajado", "Hueso y verde",
                    false, null)
    );

    public Optional<Producto> buscar(String id) {
        return productos.stream()
                .filter(producto -> producto.getId().equals(id))
                .findFirst();
    }

    // Sin filtros se muestran las novedades; con categoría o busquedatodo el catalogo.
    public List<Producto> filtrar(String categoria, String texto) {
        boolean sinFiltros = categoria.equals("todas") && texto.isEmpty();
        String buscado = normalizar(texto);

        return productos.stream()
                .filter(producto -> !sinFiltros || producto.isNovedad())
                .filter(producto -> categoria.equals("todas") || producto.getCategoria().equals(categoria))
                .filter(producto -> normalizar(producto.getNombre()).contains(buscado))
                .toList();
    }

    // "México" = "mexico" en minúsculas y sin acentos, para que buscar "mexico" también encuentre "México"
    private String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim();
    }
}
