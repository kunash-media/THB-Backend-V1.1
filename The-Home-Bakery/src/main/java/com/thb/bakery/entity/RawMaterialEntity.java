package com.thb.bakery.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "raw_inventory")
public class RawMaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String category;
    private String vendorName;
    private String vendorContact;
    private String deliverTo;
    private String assignedTo;

    @Column(nullable = false)
    private Double currentStock = 0.0;

    private String unit;
    private Double minLevel;
    private Double maxLevel;
    private Double unitPrice;
    private Double totalPrice;
    private Double totalAmount;
    private String supplier;


    private String notes;

    private LocalDate posCreatedDate;
    private LocalDate dateReceived;

    @Column(nullable = false)
    private String status = "pending";

    private LocalDate lastUpdated;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "material_payments", joinColumns = @JoinColumn(name = "material_id"))
    private List<Payment> payments = new ArrayList<>();

    @Embeddable
    public static class Payment {
        private LocalDate date;
        private Double amount;
        private String method;

        public Payment(LocalDate date, Double amount, String method) {
        }
        public Payment(){

        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorContact() {
        return vendorContact;
    }

    public void setVendorContact(String vendorContact) {
        this.vendorContact = vendorContact;
    }

    public String getDeliverTo() {
        return deliverTo;
    }

    public void setDeliverTo(String deliverTo) {
        this.deliverTo = deliverTo;
    }

    public Double getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Double currentStock) {
        this.currentStock = currentStock;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(Double minLevel) {
        this.minLevel = minLevel;
    }

    public Double getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(Double maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getPosCreatedDate() {
        return posCreatedDate;
    }

    public void setPosCreatedDate(LocalDate posCreatedDate) {
        this.posCreatedDate = posCreatedDate;
    }

    public LocalDate getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDate lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
