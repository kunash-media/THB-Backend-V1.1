package com.thb.bakery.dto.response;

import com.thb.bakery.dto.request.IncentiveDto;

import java.time.LocalDate;
import java.util.List;

public class StaffResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String accountNo; // NEW FIELD
    private LocalDate dateOfJoining; // NEW FIELD
    private String role;
    private String status;
    private Double salary;
    private List<AdvanceDto> advances;
    private List<BonusDto> bonuses;
    private List<IncentiveDto> incentives; // NEW FIELD
    private LocalDate lastActive;

    // Constructors
    public StaffResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAccountNo() { return accountNo; } // NEW
    public void setAccountNo(String accountNo) { this.accountNo = accountNo; } // NEW

    public LocalDate getDateOfJoining() { return dateOfJoining; } // NEW
    public void setDateOfJoining(LocalDate dateOfJoining) { this.dateOfJoining = dateOfJoining; } // NEW

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getSalary() { return salary; }
    public void setSalary(Double salary) { this.salary = salary; }

    public List<AdvanceDto> getAdvances() { return advances; }
    public void setAdvances(List<AdvanceDto> advances) { this.advances = advances; }

    public List<BonusDto> getBonuses() { return bonuses; }
    public void setBonuses(List<BonusDto> bonuses) { this.bonuses = bonuses; }

    public List<IncentiveDto> getIncentives() { return incentives; } // NEW
    public void setIncentives(List<IncentiveDto> incentives) { this.incentives = incentives; } // NEW

    public LocalDate getLastActive() { return lastActive; }
    public void setLastActive(LocalDate lastActive) { this.lastActive = lastActive; }
}