package com.thb.bakery.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "staff_table")
public class StaffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staffid")
    private Long staffid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String phone;

    @Column(name = "account_no")
    private String accountNo; // NEW FIELD

    @Column(name = "date_of_joining")
    private LocalDate dateOfJoining; // NEW FIELD

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Double salary;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdvanceEntity> advances = new ArrayList<>();

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BonusEntity> bonuses = new ArrayList<>();

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IncentiveEntity> incentives = new ArrayList<>(); // NEW FIELD

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AttendanceEntity> attendanceRecords = new ArrayList<>();

    @Column(name = "last_active")
    private LocalDate lastActive;

    // Constructors
    public StaffEntity() {}

    // Getters and Setters
    public Long getStaffid() { return staffid; }
    public void setStaffid(Long staffid) { this.staffid = staffid; }

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

    public List<AdvanceEntity> getAdvances() { return advances; }
    public void setAdvances(List<AdvanceEntity> advances) { this.advances = advances; }

    public List<BonusEntity> getBonuses() { return bonuses; }
    public void setBonuses(List<BonusEntity> bonuses) { this.bonuses = bonuses; }

    public List<IncentiveEntity> getIncentives() { return incentives; } // NEW
    public void setIncentives(List<IncentiveEntity> incentives) { this.incentives = incentives; } // NEW

    public List<AttendanceEntity> getAttendanceRecords() { return attendanceRecords; }
    public void setAttendanceRecords(List<AttendanceEntity> attendanceRecords) { this.attendanceRecords = attendanceRecords; }

    public LocalDate getLastActive() { return lastActive; }
    public void setLastActive(LocalDate lastActive) { this.lastActive = lastActive; }
}