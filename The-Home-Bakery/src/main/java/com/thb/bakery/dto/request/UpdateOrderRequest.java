package com.thb.bakery.dto.request;

import java.time.LocalDate;

public class UpdateOrderRequest {
    private String orderStatus;
    private LocalDate deliveryDate;
    private String deliveryTime;
    private String specialInstructions;

    // Constructors
    public UpdateOrderRequest() {}

    // Getters and Setters
    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public LocalDate getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDate deliveryDate) { this.deliveryDate = deliveryDate; }

    public String getDeliveryTime() { return deliveryTime; }
    public void setDeliveryTime(String deliveryTime) { this.deliveryTime = deliveryTime; }

    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
}