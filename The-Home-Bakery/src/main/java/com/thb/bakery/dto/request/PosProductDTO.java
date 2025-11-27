package com.thb.bakery.dto.request;

import java.math.BigDecimal;

public class PosProductDTO {
    private Long productId;
    private String productImage;
    private String productName;
    private String productCategory;
    private String productSubCategory;
    private String skuNumber;
    private BigDecimal productOldPrice;
    private BigDecimal productNewPrice;
    private Integer productQuantity; // Stock quantity

    // Constructors
    public PosProductDTO() {}

    public PosProductDTO(Long productId, String productImage, String productName,
                         String productCategory, String productSubCategory,
                         String skuNumber, BigDecimal productOldPrice,
                         BigDecimal productNewPrice, Integer productQuantity) {
        this.productId = productId;
        this.productImage = productImage;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productSubCategory = productSubCategory;
        this.skuNumber = skuNumber;
        this.productOldPrice = productOldPrice;
        this.productNewPrice = productNewPrice;
        this.productQuantity = productQuantity;
    }

    // Getters and Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductImage() { return productImage; }
    public void setProductImage(String productImage) { this.productImage = productImage; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductCategory() { return productCategory; }
    public void setProductCategory(String productCategory) { this.productCategory = productCategory; }

    public String getProductSubCategory() { return productSubCategory; }
    public void setProductSubCategory(String productSubCategory) { this.productSubCategory = productSubCategory; }

    public String getSkuNumber() { return skuNumber; }
    public void setSkuNumber(String skuNumber) { this.skuNumber = skuNumber; }

    public BigDecimal getProductOldPrice() { return productOldPrice; }
    public void setProductOldPrice(BigDecimal productOldPrice) { this.productOldPrice = productOldPrice; }

    public BigDecimal getProductNewPrice() { return productNewPrice; }
    public void setProductNewPrice(BigDecimal productNewPrice) { this.productNewPrice = productNewPrice; }

    public Integer getProductQuantity() { return productQuantity; }
    public void setProductQuantity(Integer productQuantity) { this.productQuantity = productQuantity; }
}