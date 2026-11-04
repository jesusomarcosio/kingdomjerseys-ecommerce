package com.kingdomjerseys.ecommerce.service;

/** Error de regla de negocio. Si trae "campo", se muestra junto a ese campo del formulario. */
public class ReglaNegocioException extends RuntimeException {
    private final String campo;

    public ReglaNegocioException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }

    public ReglaNegocioException(String mensaje) { this(null, mensaje); }

    public String getCampo() { return campo; }
}