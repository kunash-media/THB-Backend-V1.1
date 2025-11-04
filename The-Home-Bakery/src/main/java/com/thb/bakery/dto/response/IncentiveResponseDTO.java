package com.thb.bakery.dto.response;

import java.time.LocalDate;

public class IncentiveResponseDTO {
    private Long id;
    private Long staffId;
    private Double amount;
    private LocalDate incentiveDate;
    private String month;
    private String note;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getIncentiveDate() {
        return incentiveDate;
    }

    public void setIncentiveDate(LocalDate incentiveDate) {
        this.incentiveDate = incentiveDate;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}