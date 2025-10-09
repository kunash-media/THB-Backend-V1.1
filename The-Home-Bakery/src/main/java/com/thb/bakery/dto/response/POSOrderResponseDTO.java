package com.thb.bakery.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class POSOrderResponseDTO {
    private Long id;
    private String orderId;
    private String customerName;
    private String customerMobile;
    private List<OrderItem> orderItems;
    private Integer totalQuantity;
    private String totalDiscount;
    private String paymentMode;
    private String paymentStatus;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private String receiptId;
    private String tableNumber;
    private Long productId;


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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public static class OrderItem {
        private Long itemId;
        private String itemName;
        private Integer itemQuantity;
        private Double itemPrice;
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