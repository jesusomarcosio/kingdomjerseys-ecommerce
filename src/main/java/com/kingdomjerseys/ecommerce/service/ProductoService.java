package com.kingdomjerseys.ecommerce.service;

import com.kingdomjerseys.ecommerce.model.Producto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    // Catálogo en memoria; el id es el nombre de la foto y sale en la dirección: /producto/{id}
    private final List<Producto> productos = List.of(
            new Producto("madrid-local", "Madrid · Camiseta local", "Clubes",
                    "Madrid · Camiseta local. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "madrid-local.jpg", "madrid-local.jpg", "100% Poliéster", "Ceñido", "Blanco"),
            new Producto("madrid-visitante", "Real Madrid · Visitante", "Clubes",
                    "Real Madrid · Visitante. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "madrid-visitante.jpg", "madrid-visitante.jpg", "100% Poliéster", "Ceñido", "Verde oscuro"),
            new Producto("barcelona-local", "Barcelona · Camiseta local", "Clubes",
                    "Barcelona · Camiseta local. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "barcelona-local.jpg", "barcelona-local.jpg", "100% Poliéster", "Ceñido", "Azul y grana"),
            new Producto("boca-local", "Boca Juniors · Local", "Clubes",
                    "Boca Juniors · Local. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "boca-local.jpg", "boca-local.jpg", "100% Poliéster", "Ceñido", "Azul y amarillo"),
            new Producto("liverpool-local", "Liverpool · Local", "Clubes",
                    "Liverpool · Local. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "liverpool-local.jpg", "liverpool-local.jpg", "100% Poliéster", "Ceñido", "Rojo"),
            new Producto("juventus-local", "Juventus · Local", "Clubes",
                    "Juventus · Local. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "juventus-local.jpg", "juventus-local.jpg", "100% Poliéster", "Ceñido", "Blanco y negro"),
            new Producto("river-local", "River Plate · Local", "Clubes",
                    "River Plate · Local. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "river-local.jpg", "river-local.jpg", "100% Poliéster", "Ceñido", "Blanco con franja roja"),
            new Producto("psg-local", "PSG · Local", "Clubes",
                    "PSG · Local. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "psg-local.jpg", "psg-local.jpg", "100% Poliéster", "Ceñido", "Azul y rojo"),
            new Producto("alemania-seleccion", "Alemania · Selección", "Selecciones",
                    "Alemania · Selección. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "alemania-seleccion.jpg", "alemania-seleccion.jpg", "100% Poliéster", "Ceñido", "Blanco"),
            new Producto("argentina", "Argentina", "Selecciones",
                    "Argentina. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "argentina.jpg", "argentina.jpg", "100% Poliéster", "Ceñido", "Celeste y blanco"),
            new Producto("brasil", "Brasil", "Selecciones",
                    "Brasil. Temporada 25/26, corte ceñido y tela ligera que respira durante todo el partido.",
                    600, "brasil.jpg", "brasil.jpg", "100% Poliéster", "Ceñido", "Amarillo"),
            new Producto("mexico-seleccion", "México · Selección", "Selecciones",
                    "Diseñado para los aficionados apasionados que llevan su orgullo a flor de piel, el Jersey Local Selección Nacional de México 26 es algo más que un uniforme; es un símbolo de unidad y celebración.",
                    600, "mexico-seleccion.jpg", "mexico-local.jpg", "100% Poliéster", "Ceñido", "Verde intenso"),
            new Producto("cancha-verde", "Cancha · Verde tribuna", "Kingdom Studio",
                    "Diseño propio de Kingdom Studio inspirado en la cultura de la tribuna. Corte relajado para usarse todos los días.",
                    500, "cancha-verde.jpg", "cancha-verde.jpg", "100% Poliéster", "Relajado", "Verde"),
            new Producto("cancha-hueso", "Cancha · Hueso y verde", "Kingdom Studio",
                    "Diseño propio de Kingdom Studio inspirado en la cultura de la tribuna. Corte relajado para usarse todos los días.",
                    500, "cancha-hueso.jpg", "cancha-hueso.jpg", "100% Poliéster", "Relajado", "Hueso y verde")
    );

    public List<Producto> listar() {
        return productos;
    }

    public Optional<Producto> buscar(String id) {
        return productos.stream()
                .filter(producto -> producto.getId().equals(id))
                .findFirst();
    }
}
