package com.example.parcial_1.model;

/** Ítem del carrito (producto + cantidad). */
public class CartItem {
    public final Product product;
    public int qty;

    public CartItem(Product product, int qty) {
        this.product = product;
        this.qty = qty;
    }
}
