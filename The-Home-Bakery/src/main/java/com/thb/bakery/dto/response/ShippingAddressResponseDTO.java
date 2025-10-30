package com.thb.bakery.dto.response;

public class ShippingAddressResponseDTO {
    private Long shippingId;
    private String customerPhone;
    private String customerEmail;
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingPincode;
    private String shippingCountry;

    private String addressType;
    private String houseNo;
    private String streetArea;
    private String landmark;

    // Default constructor
    public ShippingAddressResponseDTO() {}

    // Parameterized constructor
    public ShippingAddressResponseDTO(Long shippingId, String customerPhone, String customerEmail,
                                      String shippingAddress, String shippingCity, String shippingState,
                                      String shippingPincode, String shippingCountry, String addressType,
                                      String houseNo, String streetArea, String landmark) {
        this.shippingId = shippingId;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.shippingAddress = shippingAddress;
        this.shippingCity = shippingCity;
        this.shippingState = shippingState;
        this.shippingPincode = shippingPincode;
        this.shippingCountry = shippingCountry;
        this.addressType = addressType;
        this.houseNo = houseNo;
        this.streetArea = streetArea;
        this.landmark = landmark;
    }

    // Getters and Setters
    public Long getShippingId() { return shippingId; }
    public void setShippingId(Long shippingId) { this.shippingId = shippingId; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getShippingCity() { return shippingCity; }
    public void setShippingCity(String shippingCity) { this.shippingCity = shippingCity; }

    public String getShippingState() { return shippingState; }
    public void setShippingState(String shippingState) { this.shippingState = shippingState; }

    public String getShippingPincode() { return shippingPincode; }
    public void setShippingPincode(String shippingPincode) { this.shippingPincode = shippingPincode; }

    public String getShippingCountry() { return shippingCountry; }
    public void setShippingCountry(String shippingCountry) { this.shippingCountry = shippingCountry; }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getStreetArea() {
        return streetArea;
    }

    public void setStreetArea(String streetArea) {
        this.streetArea = streetArea;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}