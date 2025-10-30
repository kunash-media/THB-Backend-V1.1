package com.thb.bakery.dto.request;

import jakarta.validation.constraints.*;
import java.util.List;

public class OrderRequestDTO {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String customerPhone;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;

    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @NotBlank(message = "Shipping city is required")
    private String shippingCity;

    @NotBlank(message = "Shipping state is required")
    private String shippingState;

    @NotBlank(message = "Shipping pincode is required")
    private String shippingPincode;

    private String shippingCountry = "India";

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    // Optional billing fields (if different from shipping)
    private String billingAddress;
    private String billingCity;
    private String billingState;
    private String billingPincode;
    private String billingCountry = "India";
    private Boolean shippingIsBilling = true;

    // Optional fields
    private String couponCode;
    private String specialInstructions;
    private String deliveryType = "HOME_DELIVERY";

    private String recipientName;
    private String recipientMobile;
    private String giftMessage;

    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemRequest> items;

    // Constructors
    public OrderRequestDTO() {}

    public OrderRequestDTO(Long userId, String customerName, String customerPhone, String customerEmail,
                           String shippingAddress, String shippingCity, String shippingState,
                           String shippingPincode, String shippingCountry, String paymentMethod,
                           String billingAddress, String billingCity, String billingState,
                           String billingPincode, String billingCountry, Boolean shippingIsBilling,
                           String couponCode, String specialInstructions, String deliveryType,
                           String recipientName, String recipientMobile, String giftMessage,
                           List<OrderItemRequest> items) {
        this.userId = userId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.shippingAddress = shippingAddress;
        this.shippingCity = shippingCity;
        this.shippingState = shippingState;
        this.shippingPincode = shippingPincode;
        this.shippingCountry = shippingCountry;
        this.paymentMethod = paymentMethod;
        this.billingAddress = billingAddress;
        this.billingCity = billingCity;
        this.billingState = billingState;
        this.billingPincode = billingPincode;
        this.billingCountry = billingCountry;
        this.shippingIsBilling = shippingIsBilling;
        this.couponCode = couponCode;
        this.specialInstructions = specialInstructions;
        this.deliveryType = deliveryType;
        this.recipientName = recipientName;
        this.recipientMobile = recipientMobile;
        this.giftMessage = giftMessage;
        this.items = items;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getShippingCity() { return shippingCity; }
    public void setShippingCity(String shippingCity) { this.shippingCity = shippingCity; }

    public String getShippingState() { return shippingState; }
    public void setShippingState(String shippingState) { this.shippingState = shippingState; }

    public String getShippingPincode() { return shippingPincode; }
    public void setShippingPincode(String shippingPincode) { this.shippingPincode = shippingPincode; }

    public String getShippingCountry() { return shippingCountry; }
    public void setShippingCountry(String shippingCountry) { this.shippingCountry = shippingCountry; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }

    public String getBillingCity() { return billingCity; }
    public void setBillingCity(String billingCity) { this.billingCity = billingCity; }

    public String getBillingState() { return billingState; }
    public void setBillingState(String billingState) { this.billingState = billingState; }

    public String getBillingPincode() { return billingPincode; }
    public void setBillingPincode(String billingPincode) { this.billingPincode = billingPincode; }

    public String getBillingCountry() { return billingCountry; }
    public void setBillingCountry(String billingCountry) { this.billingCountry = billingCountry; }

    public Boolean getShippingIsBilling() { return shippingIsBilling; }
    public void setShippingIsBilling(Boolean shippingIsBilling) { this.shippingIsBilling = shippingIsBilling; }

    public String getCouponCode() { return couponCode; }
    public void setCouponCode(String couponCode) { this.couponCode = couponCode; }

    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }

    public String getDeliveryType() { return deliveryType; }
    public void setDeliveryType(String deliveryType) { this.deliveryType = deliveryType; }

    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientMobile() {
        return recipientMobile;
    }

    public void setRecipientMobile(String recipientMobile) {
        this.recipientMobile = recipientMobile;
    }

    public String getGiftMessage() {
        return giftMessage;
    }

    public void setGiftMessage(String giftMessage) {
        this.giftMessage = giftMessage;
    }
}