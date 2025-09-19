package com.bakery.The.Home.Bakery.dto.request;

public class AdminUpdateDTO {
    private String name;
    private String mobile;
    private String email;
    private String password;
    private String role;

    // Constructors
    public AdminUpdateDTO() {}

    public AdminUpdateDTO(String name, String mobile,String email, String password, String role) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters and Setters
    public String getName() { return name; }

    public String getMobileNumber() {
        return mobile;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobile = mobile;
    }

    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
