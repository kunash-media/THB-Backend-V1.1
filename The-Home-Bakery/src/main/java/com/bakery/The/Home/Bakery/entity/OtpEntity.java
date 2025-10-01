package com.bakery.The.Home.Bakery.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Opt_Entity")
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AdminEntity admin;

    @Column(name = "otp_code", nullable = false)
    private String otpCode;

    @Column(name = "mobile_number")
    private String mobile;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "is_used", nullable = false)
    private boolean isUsed = false;

    @Column(name = "email")
    private String email;


    @Column(name = "purpose")
    private String purpose; // "PASSWORD_RESET", "VERIFICATION", etc.


    public OtpEntity(){}

    public OtpEntity(UserEntity user, AdminEntity admin, String otpCode, String mobile,
                     LocalDateTime expiresAt, String email, String purpose) {
        this.user = user;
        this.admin = admin;
        this.otpCode = otpCode;
        this.mobile = mobile;
        this.expiresAt = expiresAt;
        this.email = email;
        this.purpose = purpose;
        this.createdAt = LocalDateTime.now(); // Set createdAt to current time
        this.isUsed = false; // Default value
    }

    public OtpEntity(UserEntity user, AdminEntity admin, String hashedOtp, Object o, LocalDateTime localDateTime, String email, String passwordReset) {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public AdminEntity getAdmin() {
        return admin;
    }

    public void setAdmin(AdminEntity admin) {
        this.admin = admin;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getMobileNumber() {
        return mobile;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobile = mobileNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }


}
