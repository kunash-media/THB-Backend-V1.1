package com.thb.bakery.dto.response;

import java.time.LocalDate;

public class SalarySlipResponseDTO {
    private String staffName;
    private String employeeId;
    private String role;
    private String month;
    private Double baseSalary;
    private Double payableSalary;
    private Double advance;
    private Double bonus;
    private Double incentive;
    private Double netSalary;
    private int totalWorkingDays;
    private int daysWorked;
    private int paidLeave;
    private int unpaidLeave;
    private int payableDays;
    private String bankAccount;
    private LocalDate dateOfJoining;
    private String dateOfIssue;

    // Constructors
    public SalarySlipResponseDTO() {}

    // Getters and Setters
    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(Double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public Double getPayableSalary() {
        return payableSalary;
    }

    public void setPayableSalary(Double payableSalary) {
        this.payableSalary = payableSalary;
    }

    public Double getAdvance() {
        return advance;
    }

    public void setAdvance(Double advance) {
        this.advance = advance;
    }

    public Double getBonus() {
        return bonus;
    }

    public void setBonus(Double bonus) {
        this.bonus = bonus;
    }

    public Double getIncentive() {
        return incentive;
    }

    public void setIncentive(Double incentive) {
        this.incentive = incentive;
    }

    public Double getNetSalary() {
        return netSalary;
    }

    public void setNetSalary(Double netSalary) {
        this.netSalary = netSalary;
    }

    public int getTotalWorkingDays() {
        return totalWorkingDays;
    }

    public void setTotalWorkingDays(int totalWorkingDays) {
        this.totalWorkingDays = totalWorkingDays;
    }

    public int getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(int daysWorked) {
        this.daysWorked = daysWorked;
    }

    public int getPaidLeave() {
        return paidLeave;
    }

    public void setPaidLeave(int paidLeave) {
        this.paidLeave = paidLeave;
    }

    public int getUnpaidLeave() {
        return unpaidLeave;
    }

    public void setUnpaidLeave(int unpaidLeave) {
        this.unpaidLeave = unpaidLeave;
    }

    public int getPayableDays() {
        return payableDays;
    }

    public void setPayableDays(int payableDays) {
        this.payableDays = payableDays;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public LocalDate getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(LocalDate dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }
}
