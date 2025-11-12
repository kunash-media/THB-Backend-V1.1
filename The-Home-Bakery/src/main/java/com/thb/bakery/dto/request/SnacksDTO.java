package com.thb.bakery.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SnacksDTO {

   // ← changed from productId
    private String productName;
    private String productCategory;
    private String productSubcategory;
    private String skuNumber;
    private Double productOldPrice;
    private Double productNewPrice;
    private Double ratings;
    private String productDiscount;
    private String productImageUrl;

    // Constructors
    public SnacksDTO() {}

    public SnacksDTO(String productName, String productCategory, String productSubcategory,
                     String skuNumber, Double productOldPrice, Double productNewPrice,
                     Double ratings, String productDiscount, String productImageUrl) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.productSubcategory = productSubcategory;
        this.skuNumber = skuNumber;
        this.productOldPrice = productOldPrice;
        this.productNewPrice = productNewPrice;
        this.ratings = ratings;
        this.productDiscount = productDiscount;
        this.productImageUrl = productImageUrl;
    }

    // Getters and Setters


    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductCategory() { return productCategory; }
    public void setProductCategory(String productCategory) { this.productCategory = productCategory; }

    public String getProductSubcategory() { return productSubcategory; }
    public void setProductSubcategory(String productSubcategory) { this.productSubcategory = productSubcategory; }

    public String getSkuNumber() { return skuNumber; }
    public void setSkuNumber(String skuNumber) { this.skuNumber = skuNumber; }

    public Double getProductOldPrice() { return productOldPrice; }
    public void setProductOldPrice(Double productOldPrice) { this.productOldPrice = productOldPrice; }

    public Double getProductNewPrice() { return productNewPrice; }
    public void setProductNewPrice(Double productNewPrice) { this.productNewPrice = productNewPrice; }

    public Double getRatings() { return ratings; }
    public void setRatings(Double ratings) { this.ratings = ratings; }

    public String getProductDiscount() { return productDiscount; }
    public void setProductDiscount(String productDiscount) { this.productDiscount = productDiscount; }

    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }
}