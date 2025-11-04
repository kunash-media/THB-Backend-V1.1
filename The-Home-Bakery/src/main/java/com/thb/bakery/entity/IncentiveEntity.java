package com.thb.bakery.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "staff_incentives")
public class IncentiveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "incentive_id")
    private Long incentiveId;

    @Column(name = "month", nullable = false)
    private String month;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "incentive_date", nullable = false)
    private LocalDate incentiveDate;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private StaffEntity staff;

    // Constructors
    public IncentiveEntity() {}

    public IncentiveEntity(Long incentiveId, String month, Double amount, LocalDate incentiveDate, String notes, StaffEntity staff) {
        this.incentiveId = incentiveId;
        this.month = month;
        this.amount = amount;
        this.incentiveDate = incentiveDate;
        this.notes = notes;
        this.staff = staff;
    }

    // Getters and Setters
    public Long getIncentiveId() { return incentiveId; }
    public void setIncentiveId(Long incentiveId) { this.incentiveId = incentiveId; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public LocalDate getIncentiveDate() { return incentiveDate; }
    public void setIncentiveDate(LocalDate incentiveDate) { this.incentiveDate = incentiveDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public StaffEntity getStaff() { return staff; }
    public void setStaff(StaffEntity staff) { this.staff = staff; }
}