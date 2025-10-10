package com.thb.bakery.dto.response;

import java.time.LocalDate;

public class AdvanceDto {
    private String month;
    private Double amount;
    private LocalDate advanceDate;

    public LocalDate getAdvanceDate() {
        return advanceDate;
    }

    public void setAdvanceDate(LocalDate advanceDate) {
        this.advanceDate = advanceDate;
    }

    // No-arg constructor
    public AdvanceDto() {}

    // Getters and Setters
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}