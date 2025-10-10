package com.thb.bakery.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "staff_advances")
public class AdvanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advance_id") // Explicit column name
    private Long advanceId; // Changed to lowercase

    @Column(name = "month", nullable = false)
    private String month;


    @Column(name = "salary", nullable = false)
    private Double salary;

    @Column(name = "advance_date", nullable = false)
    private LocalDate advanceDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false) // Changed to staff_id for consistency
    private StaffEntity staff;

    @Column(name = "amount") // Added @Column annotation
    private Double amount;

    // Constructors
    public AdvanceEntity() {}

    public AdvanceEntity(Long advanceId, String month, Double salary,
                         LocalDate advanceDate, StaffEntity staff, Double amount) {
        this.advanceId = advanceId;
        this.month = month;
        this.salary = salary;
        this.advanceDate = advanceDate;
        this.staff = staff;
        this.amount = amount;
    }

    // Corrected Getters and Setters

    public Long getAdvanceId() {
        return advanceId;
    }

    public void setAdvanceId(Long advanceId) {
        this.advanceId = advanceId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public LocalDate getAdvanceDate() {
        return advanceDate;
    }

    public void setAdvanceDate(LocalDate advanceDate) {
        this.advanceDate = advanceDate;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}