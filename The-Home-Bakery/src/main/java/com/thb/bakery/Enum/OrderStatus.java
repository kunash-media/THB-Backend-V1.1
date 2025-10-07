package com.thb.bakery.Enum;


public enum OrderStatus {
    PENDING,        // Order received
    CONFIRMED,      // Payment verified/order confirmed
    PREPARING,      // Kitchen started preparation
    READY,          // Order ready for pickup
    OUT_FOR_DELIVERY, // On the way to customer
    DELIVERED,      // Successfully delivered
    CANCELLED,      // Order cancelled
    FAILED          // Delivery failed
}

