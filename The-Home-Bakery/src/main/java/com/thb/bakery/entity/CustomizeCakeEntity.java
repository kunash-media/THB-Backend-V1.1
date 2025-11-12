package com.thb.bakery.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customize_cakes")
public class CustomizeCakeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "product_image", columnDefinition = "LONGBLOB")
    //NOW
    private byte[] productImage;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "subcategory", length = 100)
    private String subcategory;

    @Column(name = "discount", precision = 5, scale = 2)
    private BigDecimal discount;

    @ElementCollection
    @CollectionTable(
            name = "customize_cake_weights",
            joinColumns = @JoinColumn(name = "customize_cake_id")
    )
    @Column(name = "weight")
    private List<String> weights = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "customize_cake_old_prices",
            joinColumns = @JoinColumn(name = "customize_cake_id")
    )
    @Column(name = "old_price")
    private List<BigDecimal> oldPrices = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "customize_cake_new_prices",
            joinColumns = @JoinColumn(name = "customize_cake_id")
    )
    @Column(name = "new_price")
    private List<BigDecimal> newPrices = new ArrayList<>();

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    // Constructors
    public CustomizeCakeEntity() {
    }

    public CustomizeCakeEntity(Long id, byte[] productImage, String title,
                               String category, String subcategory, BigDecimal discount,
                               List < String > weights, List < BigDecimal > oldPrices,
                               List < BigDecimal > newPrices, Boolean isActive,
                               LocalDateTime createdAt, LocalDateTime updatedAt,
                               String createdBy, String updatedBy){
        this.id = id;
        this.productImage = productImage;
        this.title = title;
        this.category = category;
        this.subcategory = subcategory;
        this.discount = discount;
        this.weights = weights;
        this.oldPrices = oldPrices;
        this.newPrices = newPrices;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getProductImage() {
        return productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public List<String> getWeights() {
        return weights;
    }

    public void setWeights(List<String> weights) {
        this.weights = weights;
    }

    public List<BigDecimal> getOldPrices() {
        return oldPrices;
    }

    public void setOldPrices(List<BigDecimal> oldPrices) {
        this.oldPrices = oldPrices;
    }

    public List<BigDecimal> getNewPrices() {
        return newPrices;
    }

    public void setNewPrices(List<BigDecimal> newPrices) {
        this.newPrices = newPrices;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}