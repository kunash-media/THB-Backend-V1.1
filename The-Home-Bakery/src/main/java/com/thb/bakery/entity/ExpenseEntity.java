package com.thb.bakery.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses_table")
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expenseId;

    @Column(nullable = false)
    private String staffName;

    @Column(nullable = false)
    private Double amount;

    @Column(length = 500)
    private String note;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public ExpenseEntity() {
    }

    public ExpenseEntity(String staffName, Double amount, String note, LocalDateTime date, String status, String category) {
        this.staffName = staffName;
        this.amount = amount;
        this.note = note;
        this.date = date;
        this.status = status;
        this.category = category;
    }

    // Getters and Setters
    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
