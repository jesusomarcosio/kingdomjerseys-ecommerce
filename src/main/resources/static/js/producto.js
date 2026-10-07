// Selector de cantidad de la página de producto 10 maximo.
// Los botones + y - cambian el número que se ve y el valor que se manda al carrito.

var CANTIDAD_MINIMA = 1;
var CANTIDAD_MAXIMA = 10;

var campoCantidad = document.getElementById("cantidad");
var textoCantidad = document.getElementById("cantidad-texto");
var botones = document.querySelectorAll(".qty button[data-cambio]");

botones.forEach(function (boton) {
    boton.addEventListener("click", function () {
        var cambio = Number(boton.dataset.cambio);
        var nueva = Number(campoCantidad.value) + cambio;
        nueva = Math.max(CANTIDAD_MINIMA, Math.min(nueva, CANTIDAD_MAXIMA));

        campoCantidad.value = nueva;
        textoCantidad.textContent = nueva;
    });
});
