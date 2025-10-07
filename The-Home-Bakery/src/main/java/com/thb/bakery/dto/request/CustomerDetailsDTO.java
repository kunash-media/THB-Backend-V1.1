package com.thb.bakery.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

class CustomerDetailsDTO {

    @NotBlank(message = "Customer name is required")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    @JsonProperty("phone")
    private String phone;

    @Pattern(regexp = "^[0-9]{10}$", message = "Alternate phone number must be 10 digits")
    @JsonProperty("alternate_phone")
    private String alternatePhone;

    // Constructors
    public CustomerDetailsDTO() {}

    public CustomerDetailsDTO(String name, String email, String phone, String alternatePhone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.alternatePhone = alternatePhone;
    }

    // Getters and Setters
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

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public void setAlternatePhone(String alternatePhone) {
        this.alternatePhone = alternatePhone;
    }
}