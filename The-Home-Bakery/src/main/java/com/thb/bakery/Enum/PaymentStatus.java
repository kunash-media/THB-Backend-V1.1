package com.thb.bakery.Enum;


public enum PaymentStatus {
    PENDING,        // Payment not yet made
    PAID,           // Payment successful
    FAILED,         // Payment failed
    REFUNDED,       // Amount refunded
    CANCELLED       // Payment cancelled
}
