package com.thb.bakery.dto.request;

import java.time.LocalDate;

public class BonusRequest {

    private String month;
    private Double amount;
    private LocalDate bonusDate;

    public LocalDate getBonusDate() {
        return bonusDate;
    }

    public void setBonusDate(LocalDate bonusDate) {
        this.bonusDate = bonusDate;
    }

    //NO ARGS Constructor..
    public BonusRequest() {

    }

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
