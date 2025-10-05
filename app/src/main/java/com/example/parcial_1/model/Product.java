package com.example.parcial_1.model;

/** Producto del catálogo . */
public class Product {
    public final String name;      // ej: "Impresión B/N"
    public final String desc;      // ej: "Cara simple · A4"
    public final int price;        // precio unitario en ARS (entero para simplificar)
    public final Category category;
    public final int imageRes;     // drawable de la miniatura
    public final boolean copyBased; // true = item de "copias" (puede detectar páginas de PDF)

    public Product(String name, String desc, int price, Category category, int imageRes, boolean copyBased) {
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.category = category;
        this.imageRes = imageRes;
        this.copyBased = copyBased;
    }
}
