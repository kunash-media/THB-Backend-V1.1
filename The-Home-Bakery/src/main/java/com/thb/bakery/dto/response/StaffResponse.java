package com.thb.bakery.dto.response;

import java.time.LocalDate;
import java.util.List;

public class StaffResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String status;
    private Double salary;

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    private List<AdvanceDto> advances; // => References to separate AdvacneDto class;
    private List<BonusDto> bonuses; // => References to separate BonusDto class;

    private LocalDate lastActive;


    //NO ARGS Constructor..
    public StaffResponse(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AdvanceDto> getAdvances() {
        return advances;
    }

    public void setAdvances(List<AdvanceDto> advances) {
        this.advances = advances;
    }

    public List<BonusDto> getBonuses() {
        return bonuses;
    }

    public void setBonuses(List<BonusDto> bonuses) {
        this.bonuses = bonuses;
    }

    public LocalDate getLastActive() {
        return lastActive;
    }

    public void setLastActive(LocalDate lastActive) {
        this.lastActive = lastActive;
    }
}