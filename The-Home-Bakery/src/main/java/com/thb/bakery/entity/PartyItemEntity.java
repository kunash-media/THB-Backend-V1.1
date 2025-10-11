package com.thb.bakery.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "party_items")
public class PartyItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_item_id")
    private Long partyItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItemEntity orderItem;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "party_item_name")
    private String partyItemName;

    @Column(name = "party_item_quantity", nullable = false)
    private Integer partyItemQuantity;

    @Column(name = "party_item_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal partyItemPrice;

    @Column(name = "party_item_subtotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal partyItemSubtotal;

    // Constructors
    public PartyItemEntity() {}

    // Getters and Setters
    public Long getPartyItemId() {
        return partyItemId;
    }

    public void setPartyItemId(Long partyItemId) {
        this.partyItemId = partyItemId;
    }

    public OrderItemEntity getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItemEntity orderItem) {
        this.orderItem = orderItem;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getPartyItemName() {
        return partyItemName;
    }

    public void setPartyItemName(String partyItemName) {
        this.partyItemName = partyItemName;
    }

    public Integer getPartyItemQuantity() {
        return partyItemQuantity;
    }

    public void setPartyItemQuantity(Integer partyItemQuantity) {
        this.partyItemQuantity = partyItemQuantity;
    }

    public BigDecimal getPartyItemPrice() {
        return partyItemPrice;
    }

    public void setPartyItemPrice(BigDecimal partyItemPrice) {
        this.partyItemPrice = partyItemPrice;
    }

    public BigDecimal getPartyItemSubtotal() {
        return partyItemSubtotal;
    }

    public void setPartyItemSubtotal(BigDecimal partyItemSubtotal) {
        this.partyItemSubtotal = partyItemSubtotal;
    }
}
