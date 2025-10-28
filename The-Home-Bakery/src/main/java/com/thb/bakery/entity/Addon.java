package com.thb.bakery.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "party_addon_entity")
public class Addon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemKey;
    private String name;
    private double price;
    private String imageUrl;


    // --- Many-to-Many with CartItemEntity (optional, for completeness) ---
    @ManyToMany(mappedBy = "addons")
    private Set<CartItemEntity> cartItems = new HashSet<>();

    public Addon(){}

    public Addon(Long id, String itemKey, String name, double price, String imageUrl) {
        this.id = id;
        this.itemKey = itemKey;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Set<CartItemEntity> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItemEntity> cartItems) {
        this.cartItems = cartItems;
    }
}