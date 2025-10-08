package com.thb.bakery.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class OrderResponse {

    private boolean success;
    private String status;
    private String message;
    private Long orderId;
    private BigDecimal totalAmount;
    private BigDecimal tax;
    private BigDecimal couponApplied;
    private BigDecimal convenienceFee;
    private BigDecimal discountPercent;
    private BigDecimal discountAmount;
    private String orderStatus;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private String deliveryTime;
    private String paymentMethod;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String shippingAddress;
    private String shippingAddress2;
    private String shippingCity;
    private String shippingState;
    private String shippingPincode;
    private String shippingCountry;
    private String shippingCustomerName;
    private String shippingEmail;
    private String shippingPhone;
    private String specialInstructions;
    private String cakeMessage;
    private List<OrderItemResponse> items;

    // Constructors
    public OrderResponse() {}

    public OrderResponse(boolean success, Long orderId, BigDecimal totalAmount, String orderStatus, LocalDate orderDate, String message) {
        this.success = success;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.message = message;
        this.status = success ? "success" : "error";
    }

    public OrderResponse(boolean success, String status, String message, Long orderId) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.orderId = orderId;
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getCouponApplied() {
        return couponApplied;
    }

    public void setCouponApplied(BigDecimal couponApplied) {
        this.couponApplied = couponApplied;
    }

    public BigDecimal getConvenienceFee() {
        return convenienceFee;
    }

    public void setConvenienceFee(BigDecimal convenienceFee) {
        this.convenienceFee = convenienceFee;
    }

    public BigDecimal getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(BigDecimal discountPercent) {
        this.discountPercent = discountPercent;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getShippingAddress2() {
        return shippingAddress2;
    }

    public void setShippingAddress2(String shippingAddress2) {
        this.shippingAddress2 = shippingAddress2;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingState() {
        return shippingState;
    }

    public void setShippingState(String shippingState) {
        this.shippingState = shippingState;
    }

    public String getShippingPincode() {
        return shippingPincode;
    }

    public void setShippingPincode(String shippingPincode) {
        this.shippingPincode = shippingPincode;
    }

    public String getShippingCountry() {
        return shippingCountry;
    }

    public void setShippingCountry(String shippingCountry) {
        this.shippingCountry = shippingCountry;
    }

    public String getShippingCustomerName() {
        return shippingCustomerName;
    }

    public void setShippingCustomerName(String shippingCustomerName) {
        this.shippingCustomerName = shippingCustomerName;
    }

    public String getShippingEmail() {
        return shippingEmail;
    }

    public void setShippingEmail(String shippingEmail) {
        this.shippingEmail = shippingEmail;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public String getCakeMessage() {
        return cakeMessage;
    }

    public void setCakeMessage(String cakeMessage) {
        this.cakeMessage = cakeMessage;
    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }
}