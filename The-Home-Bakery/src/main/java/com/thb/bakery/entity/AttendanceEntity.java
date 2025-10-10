package com.thb.bakery.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "attendance")
public class AttendanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private StaffEntity staff;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "check_in_time")
    private LocalTime checkInTime;

    @Column(name = "check_out_time")
    private LocalTime checkOutTime;

    @Column(name = "total_hours")
    private Double totalHours;

    @Column(name = "status", nullable = false)
    private String status; // PRESENT, ABSENT, HALF_DAY, LEAVE

    @Column(name = "notes")
    private String notes;

    public Long getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Long attendanceId) {
        this.attendanceId = attendanceId;
    }

    public StaffEntity getStaff() {
        return staff;
    }

    public void setStaff(StaffEntity staff) {
        this.staff = staff;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Double getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(Double totalHours) {
        this.totalHours = totalHours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public AttendanceEntity() {

    }


    // Helper method to calculate hours
    private void calculateTotalHours() {
        if (this.checkInTime != null && this.checkOutTime != null) {
            int checkInMinutes = this.checkInTime.getHour() * 60 + this.checkInTime.getMinute();
            int checkOutMinutes = this.checkOutTime.getHour() * 60 + this.checkOutTime.getMinute();

            double totalMinutes = checkOutMinutes - checkInMinutes;
            this.totalHours = totalMinutes / 60.0;

            // Auto-update status based on hours
            if (this.totalHours >= 6) {
                this.status = "PRESENT";
            } else if (this.totalHours >= 3) {
                this.status = "HALF_DAY";
            }
        }
    }
}