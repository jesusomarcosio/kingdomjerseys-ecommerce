package com.kingdomjerseys.ecommerce.model.enums;

import java.util.Optional;

public enum Categoria {
    CLUBES("clubes", "Clubes"),
    SELECCIONES("selecciones", "Selecciones"),
    STUDIO("studio", "Studio");

    private final String slug;
    private final String etiqueta;

    Categoria(String slug, String etiqueta) {
        this.slug = slug;
        this.etiqueta = etiqueta;
    }

    public String getSlug() { return slug; }
    public String getEtiqueta() { return etiqueta; }

    public static Optional<Categoria> desdeSlug(String slug) {
        for (Categoria c : values()) {
            if (c.slug.equals(slug)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
}
