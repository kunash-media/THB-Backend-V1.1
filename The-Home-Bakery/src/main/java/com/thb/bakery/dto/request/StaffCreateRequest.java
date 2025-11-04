package com.thb.bakery.dto.request;

import java.time.LocalDate;

public class StaffCreateRequest {
    private String name;
    private String email;
    private String phone;
    private String accountNo; // NEW FIELD
    private LocalDate dateOfJoining; // NEW FIELD
    private String role;
    private String status;
    private Double salary;

    // Constructors
    public StaffCreateRequest() {}

    // Getters and Setters
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
}
