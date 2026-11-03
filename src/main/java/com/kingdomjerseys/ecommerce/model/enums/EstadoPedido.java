package com.kingdomjerseys.ecommerce.model.enums;

import java.util.List;

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

    /** Estados a los que se puede pasar desde este. */
    public List<EstadoPedido> siguientes() {
        return switch (this) {
            case PENDIENTE -> List.of(PAGADO, CANCELADO);
            case PAGADO -> List.of(ENVIADO, CANCELADO);
            case ENVIADO -> List.of(ENTREGADO);
            case ENTREGADO, CANCELADO -> List.of();
        };
    }

    public boolean puedeCambiarA(EstadoPedido destino) {
        return siguientes().contains(destino);
    }
}
