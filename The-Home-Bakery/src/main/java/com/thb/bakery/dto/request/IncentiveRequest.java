package com.thb.bakery.dto.request;
import java.time.LocalDate;

public class IncentiveRequest {
    private String month;
    private Double amount;
    private LocalDate incentiveDate;
    private String notes;

    // Constructors
    public IncentiveRequest() {}

    // Getters and Setters
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDate getIncentiveDate() { return incentiveDate; }
    public void setIncentiveDate(LocalDate incentiveDate) { this.incentiveDate = incentiveDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}