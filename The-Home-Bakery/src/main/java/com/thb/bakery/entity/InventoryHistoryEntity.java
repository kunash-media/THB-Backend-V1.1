package com.thb.bakery.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_history")
public class InventoryHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id", nullable = false)
    private InventoryEntity inventory;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private Integer quantity;

    @Column
    private String reason;

    // Constructors
    public InventoryHistoryEntity() {}

    public InventoryHistoryEntity(InventoryEntity inventory, LocalDateTime date, String action, Integer quantity, String reason) {
        this.inventory = inventory;
        this.date = date;
        this.action = action;
        this.quantity = quantity;
        this.reason = reason;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public InventoryEntity getInventory() { return inventory; }
    public void setInventory(InventoryEntity inventory) { this.inventory = inventory; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}