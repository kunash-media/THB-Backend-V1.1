package com.thb.bakery.dto.response;

import java.time.LocalDate;

public class BonusDto {

    private String month;
    private Double amount;
    private LocalDate bonusDate;

    // No-arg constructor
    public BonusDto() {}

    public LocalDate getBonusDate() {
        return bonusDate;
    }

    public void setBonusDate(LocalDate bonusDate) {
        this.bonusDate = bonusDate;
    }

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