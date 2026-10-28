package com.kingdomjerseys.admin.model;

public enum EstadoPedido {

    PENDIENTE("Pendiente"),
    PAGADO("Pagado"),
    ENVIADO("Enviado"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");

    private final String etiqueta;

    EstadoPedido(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public boolean esFinal() {
        return this == ENTREGADO || this == CANCELADO;
    }
}
