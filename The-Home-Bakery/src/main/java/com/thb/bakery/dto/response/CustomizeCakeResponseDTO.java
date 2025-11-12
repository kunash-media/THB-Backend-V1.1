package com.thb.bakery.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class CustomizeCakeResponseDTO {
    private Long id;
    private byte[] productImage;
    private String title;
    private String category;
    private String subcategory;
    private BigDecimal discount;
    private List<String> weights;
    private List<BigDecimal> oldPrices;
    private List<BigDecimal> newPrices;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public CustomizeCakeResponseDTO() {
    }

    public CustomizeCakeResponseDTO(byte[] productImage) {
        this.productImage = productImage;
    }

    public CustomizeCakeResponseDTO(Long id, byte[] productImage, String title, String category,
                                    String subcategory, BigDecimal discount, List<String> weights,
                                    List<BigDecimal> oldPrices, List<BigDecimal> newPrices,
                                    Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        //this.imageUrl = imageUrl;
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
    }

    public byte[] getProductImage() {
        return productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }

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
}