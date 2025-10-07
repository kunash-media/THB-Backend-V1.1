package com.thb.bakery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "email")
    private String email;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "status")
    private String status; // active/inactive

    @Column(name = "user_password")
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ShippingAddressEntity> shippingAddresses = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PaymentOrder> paymentOrders = new ArrayList<>();

    // Constructors
    public UserEntity() {}

    public UserEntity(String customerName, String email, String mobile, String password) {
        this.customerName = customerName;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.status = "active";
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public List<ShippingAddressEntity> getShippingAddresses() { return shippingAddresses; }
    public void setShippingAddresses(List<ShippingAddressEntity> shippingAddresses) { this.shippingAddresses = shippingAddresses; }

    public List<PaymentOrder> getPaymentOrders() { return paymentOrders; }
    public void setPaymentOrders(List<PaymentOrder> paymentOrders) { this.paymentOrders = paymentOrders; }

    // Helper methods
    public void addShippingAddress(ShippingAddressEntity address) {
        shippingAddresses.add(address);
        address.setUser(this);
    }

    public void removeShippingAddress(ShippingAddressEntity address) {
        shippingAddresses.remove(address);
        address.setUser(null);
    }

    public void addPaymentOrder(PaymentOrder paymentOrder) {
        paymentOrders.add(paymentOrder);
        paymentOrder.setUser(this);
    }
}