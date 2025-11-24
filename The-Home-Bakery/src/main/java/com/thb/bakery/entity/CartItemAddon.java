package com.thb.bakery.entity;

// File: CartItemAddon.java  (NOT @Entity — just a helper)
public class CartItemAddon {

    private Addon addon;
    private int quantity = 1;

    // constructors, getters, setters
    public CartItemAddon() {}

    public CartItemAddon(Addon addon, int quantity) {
        this.addon = addon;
        this.quantity = quantity;
    }

    // getters & setters
    public Addon getAddon() { return addon; }
    public void setAddon(Addon addon) { this.addon = addon; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = Math.max(1, quantity); }
}