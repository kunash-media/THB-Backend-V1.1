package com.thb.bakery.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "staff_bonuses")
public class BonusEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bonus_id") // Explicit column name
    private Long bonusId; // Changed to lowercase

    @Column(name = "month", nullable = false)
    private String month;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "bonus_date", nullable = false)
    private LocalDate bonusDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private StaffEntity staff;

    // Constructors
    public BonusEntity() {}

    // Corrected Getters and Setters


    public BonusEntity(Long bonusId, String month, Double amount, LocalDate bonusDate, StaffEntity staff) {
        this.bonusId = bonusId;
        this.month = month;
        this.amount = amount;
        this.bonusDate = bonusDate;
        this.staff = staff;
    }

    public Long getBonusId() {
        return bonusId;
    }

    public void setBonusId(Long bonusId) {
        this.bonusId = bonusId;
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

    public LocalDate getBonusDate() {
        return bonusDate;
    }

    public void setBonusDate(LocalDate bonusDate) {
        this.bonusDate = bonusDate;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }
}