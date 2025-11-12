package com.thb.bakery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderItemEntity – a line-item of an order.
 *
 * ORIGINAL (already in production):
 *   - @ManyToOne → ProductEntity   (FK column: product_id  → products.product_id)
 *
 * NEW (added for snacks):
 *   - @ManyToOne → SnacksEntity    (FK column: snack_id    → snacks_table.id)
 *
 * Both columns can be NULL – the application decides which one to fill.
 */
@Entity
@Table(name = "order_items")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private OrderEntity order;

    /* ---------- EXISTING PRODUCT (unchanged) ---------- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = true)
    private ProductEntity product;

    /* ---------- NEW SNACK REFERENCE (added) ---------- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snack_id", referencedColumnName = "snackId", nullable = true)
    @JsonIgnore
    private SnacksEntity snack;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "selected_weight")
    private String selectedWeight;

    @Column(name = "cake_message")
    private String cakeMessage;

    @Column(name = "special_instructions")
    private String specialInstructions;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<PartyItemEntity> partyItems = new ArrayList<>();

    /* ----------------- Constructors ----------------- */
    public OrderItemEntity() {}

    /* ----------------- Getters / Setters ----------------- */
    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    /* ----- Product ----- */
    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    /* ----- Snack ----- */
    public SnacksEntity getSnack() {
        return snack;
    }

    public void setSnack(SnacksEntity snack) {
        this.snack = snack;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getSelectedWeight() {
        return selectedWeight;
    }

    public void setSelectedWeight(String selectedWeight) {
        this.selectedWeight = selectedWeight;
    }

    public String getCakeMessage() {
        return cakeMessage;
    }

    public void setCakeMessage(String cakeMessage) {
        this.cakeMessage = cakeMessage;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public List<PartyItemEntity> getPartyItems() {
        return partyItems;
    }

    public void setPartyItems(List<PartyItemEntity> partyItems) {
        this.partyItems = partyItems;
    }
}