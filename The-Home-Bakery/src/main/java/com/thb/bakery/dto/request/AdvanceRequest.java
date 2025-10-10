package com.thb.bakery.dto.request;

import java.time.LocalDate;

public class AdvanceRequest {

    private String month;
    private Double amount;
    private Double salary;
    private LocalDate advanceDate;

    public LocalDate getAdvanceDate() {
        return advanceDate;
    }

    public void setAdvanceDate(LocalDate advanceDate) {
        this.advanceDate = advanceDate;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    //NO ARGS Constructor..
    public AdvanceRequest(){

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