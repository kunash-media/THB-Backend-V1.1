package com.thb.bakery.dto.request;

import org.springframework.web.multipart.MultipartFile;
import java.math.BigDecimal;
import java.util.List;

public class CustomizeCakeRequestDTO {
    private MultipartFile productImage;
    private String title;
    private String category;
    private String subcategory;
    private BigDecimal discount;
    private List<String> weights;
    private List<BigDecimal> oldPrices;
    private List<BigDecimal> newPrices;
    private Boolean isActive;

    // Constructors
    public CustomizeCakeRequestDTO() {
    }

    // Getters and Setters
    public MultipartFile getProductImage() {
        return productImage;
    }

    public void setProductImage(MultipartFile productImage) {
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
}