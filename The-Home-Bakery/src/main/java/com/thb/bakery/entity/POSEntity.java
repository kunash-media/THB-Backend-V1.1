package com.thb.bakery.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "POS_Table")
public class POSEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", unique = true)
    private String orderId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_mobile")
    private String customerMobile;

    @ElementCollection
    @CollectionTable(name = "pos_order_items", joinColumns = @JoinColumn(name = "pos_id"))
    private List<OrderItem> orderItems;

    @Column(name = "total_quantity")
    private Integer totalQuantity;

    @Column(name = "total_discount")
    private String totalDiscount;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "total_amount")
    private Double totalAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "receipt_id")
    private String receiptId;

    @Column(name = "table_number")
    private String tableNumber;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    @Embeddable
    public static class OrderItem {

        @Column(name = "item_id")
        private Long itemId;

        @Column(name = "item_name")
        private String itemName;

        @Column(name = "item_quantity")
        private Integer itemQuantity;

        @Column(name = "item_price")
        private Double itemPrice;

        @Column(name = "sub_total")
        private Double subTotal;


        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public Integer getItemQuantity() {
            return itemQuantity;
        }

        public void setItemQuantity(Integer itemQuantity) {
            this.itemQuantity = itemQuantity;
        }

        public Double getItemPrice() {
            return itemPrice;
        }

        public void setItemPrice(Double itemPrice) {
            this.itemPrice = itemPrice;
        }

        public Double getSubTotal() {
            return subTotal;
        }

        public void setSubTotal(Double subTotal) {
            this.subTotal = subTotal;
        }
    }
}