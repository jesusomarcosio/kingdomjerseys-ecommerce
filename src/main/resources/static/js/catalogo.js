// Búsqueda y filtros del catálogo (se usa en /novedades).
// Lee la dirección de la página, por ejemplo:
//   /novedades?categoria=clubes&buscar=real
// y muestra solo las camisetas que coinciden. El resto se oculta.

var CATEGORIAS = ["todas", "clubes", "selecciones", "retro", "tribuna"];

var tarjetas = document.querySelectorAll("#productos .card");
var pestanas = document.querySelectorAll("#pestanas a");
var grilla = document.getElementById("productos");
var sinResultados = document.getElementById("sin-resultados");
var contador = document.getElementById("contador");
var etiqueta = document.getElementById("etiqueta");
var titulo = document.getElementById("titulo");
var migaja = document.getElementById("migaja");
var campoBusqueda = document.querySelector(".nav-search input");


// "México" -> "mexico": todo en minúsculas y sin acentos,
// para que buscar "mexico" también encuentre "México".
function normalizar(texto) {
  return texto
    .toLowerCase()
    .normalize("NFD")
    .replace(/[\u0300-\u036f]/g, "")
    .trim();
}


// Lee ?categoria= y ?buscar= de la dirección.
function leerFiltros() {
  var params = new URLSearchParams(window.location.search);

  var categoria = params.get("categoria") || "todas";
  if (CATEGORIAS.indexOf(categoria) === -1) {
    categoria = "todas"; // una categoría inventada se trata como "todas"
  }

  var buscar = (params.get("buscar") || "").trim();
  return { categoria: categoria, buscar: buscar };
}


// Muestra u oculta cada tarjeta según los filtros y actualiza los textos.
function aplicarFiltros() {
  var filtros = leerFiltros();
  var textoBuscado = normalizar(filtros.buscar);
  var visibles = 0;

  tarjetas.forEach(function (tarjeta) {
    var nombre = normalizar(tarjeta.querySelector("h3").textContent);

    var coincideCategoria =
      filtros.categoria === "todas" || tarjeta.dataset.categoria === filtros.categoria;
    var coincideTexto = textoBuscado === "" || nombre.indexOf(textoBuscado) !== -1;

    var mostrar = coincideCategoria && coincideTexto;
    tarjeta.hidden = !mostrar;
    if (mostrar) {
      visibles++;
    }
  });

  // Contador y mensaje cuando no hay resultados
  contador.textContent = visibles + (visibles === 1 ? " producto" : " productos");
  grilla.hidden = visibles === 0;
  sinResultados.hidden = visibles !== 0;

  // Pestaña activa
  pestanas.forEach(function (pestana) {
    pestana.classList.toggle("active", pestana.dataset.categoria === filtros.categoria);
  });

  // Encabezado: cambia si hay una búsqueda
  if (filtros.buscar !== "") {
    etiqueta.textContent = "BÚSQUEDA";
    titulo.textContent = "Resultados para “" + filtros.buscar + "”.";
    migaja.textContent = "Resultados";
    document.title = "KingdomJerseys — Resultados";
  } else {
    etiqueta.textContent = "RECIÉN LLEGADAS";
    titulo.textContent = "Novedades.";
    migaja.textContent = "Novedades";
    document.title = "KingdomJerseys — Novedades";
  }

  // Deja escrito en el buscador del nav lo que se buscó
  campoBusqueda.value = filtros.buscar;
}


// Al hacer clic en una pestaña: cambia el filtro sin recargar la página
// y conserva la búsqueda que hubiera.
pestanas.forEach(function (pestana) {
  pestana.addEventListener("click", function (evento) {
    // Ctrl/Cmd/Shift + clic: deja que el navegador abra el enlace normal
    if (evento.ctrlKey || evento.metaKey || evento.shiftKey) {
      return;
    }
    evento.preventDefault();

    var params = new URLSearchParams(window.location.search);
    var categoria = pestana.dataset.categoria;
    if (categoria === "todas") {
      params.delete("categoria");
    } else {
      params.set("categoria", categoria);
    }

    var query = params.toString();
    history.pushState(null, "", window.location.pathname + (query ? "?" + query : ""));
    aplicarFiltros();
  });
});

// Botones "atrás" y "adelante" del navegador
window.addEventListener("popstate", aplicarFiltros);

aplicarFiltros();
