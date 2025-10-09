package com.thb.bakery.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public class POSOrderRequestDTO {

    private String orderId;

    private String customerName;

    private String customerMobile;

    @NotNull(message = "orderItems cannot be null")
    private List<OrderItem> orderItems;

    @NotNull(message = "totalQuantity cannot be null")
    private Integer totalQuantity;

    private String totalDiscount;

    private String paymentMode;

    private String paymentStatus;

    @NotNull(message = "totalAmount cannot be null")
    private Double totalAmount;

    private String receiptId;

    private String tableNumber;

    @NotNull(message = "productId cannot be null")
    private Long productId;


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
        @NotNull(message = "itemId cannot be null")
        private Long itemId;

        @NotBlank(message = "itemName cannot be blank")
        private String itemName;

        @NotNull(message = "itemQuantity cannot be null")
        private Integer itemQuantity;

        @NotNull(message = "itemPrice cannot be null")
        private Double itemPrice;

        @NotNull(message = "subTotal cannot be null")
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