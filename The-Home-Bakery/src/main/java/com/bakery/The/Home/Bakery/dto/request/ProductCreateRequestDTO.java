package com.bakery.The.Home.Bakery.dto.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductCreateRequestDTO {
    private String productName;
    private String productCategory;
    private String productFoodType;
    private String skuNumber;
    private String nameOnCake;
    private Integer orderCount;
    private String description;
    private String productIngredients;
    private String allergenInfo;
    private String careInstructions;
    private String storageInstructions;
    private String shelfLife;
    private String bestServed;
    private String preparationTime;
    private String flavor;
    private String shape;
    private String defaultWeight;
    private String layers;
    private String serves;
    private String note;
    private BigDecimal productOldPrice;
    private BigDecimal productNewPrice;
    private List<String> weights = new ArrayList<>();
    private List<BigDecimal> weightPrices = new ArrayList<>();
    private List<String> features = new ArrayList<>();
    private Double ratings;
    private Integer reviews;
    private String productDiscount;
    private String deliveryTime;
    private BigDecimal freeDeliveryThreshold;
    private byte[] productImage;
    private boolean productImagePresent;
    private List<byte[]> productSubImages = new ArrayList<>();
    private boolean productSubImagesPresent;

    public String getStorageInstructions() {
        return storageInstructions;
    }

    public void setStorageInstructions(String storageInstructions) {
        this.storageInstructions = storageInstructions;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductFoodType() {
        return productFoodType;
    }

    public void setProductFoodType(String productFoodType) {
        this.productFoodType = productFoodType;
    }

    public String getSkuNumber() {
        return skuNumber;
    }

    public void setSkuNumber(String skuNumber) {
        this.skuNumber = skuNumber;
    }

    public String getNameOnCake() {
        return nameOnCake;
    }

    public void setNameOnCake(String nameOnCake) {
        this.nameOnCake = nameOnCake;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductIngredients() {
        return productIngredients;
    }

    public void setProductIngredients(String productIngredients) {
        this.productIngredients = productIngredients;
    }

    public String getAllergenInfo() {
        return allergenInfo;
    }

    public void setAllergenInfo(String allergenInfo) {
        this.allergenInfo = allergenInfo;
    }

    public String getCareInstructions() {
        return careInstructions;
    }

    public void setCareInstructions(String careInstructions) {
        this.careInstructions = careInstructions;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getBestServed() {
        return bestServed;
    }

    public void setBestServed(String bestServed) {
        this.bestServed = bestServed;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getDefaultWeight() {
        return defaultWeight;
    }

    public void setDefaultWeight(String defaultWeight) {
        this.defaultWeight = defaultWeight;
    }

    public String getLayers() {
        return layers;
    }

    public void setLayers(String layers) {
        this.layers = layers;
    }

    public String getServes() {
        return serves;
    }

    public void setServes(String serves) {
        this.serves = serves;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getProductOldPrice() {
        return productOldPrice;
    }

    public void setProductOldPrice(BigDecimal productOldPrice) {
        this.productOldPrice = productOldPrice;
    }

    public BigDecimal getProductNewPrice() {
        return productNewPrice;
    }

    public void setProductNewPrice(BigDecimal productNewPrice) {
        this.productNewPrice = productNewPrice;
    }

    public List<String> getWeights() {
        return weights;
    }

    public void setWeights(List<String> weights) {
        this.weights = weights;
    }

    public List<BigDecimal> getWeightPrices() {
        return weightPrices;
    }

    public void setWeightPrices(List<BigDecimal> weightPrices) {
        this.weightPrices = weightPrices;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public Double getRatings() {
        return ratings;
    }

    public void setRatings(Double ratings) {
        this.ratings = ratings;
    }

    public Integer getReviews() {
        return reviews;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public String getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public BigDecimal getFreeDeliveryThreshold() {
        return freeDeliveryThreshold;
    }

    public void setFreeDeliveryThreshold(BigDecimal freeDeliveryThreshold) {
        this.freeDeliveryThreshold = freeDeliveryThreshold;
    }

    public byte[] getProductImage() {
        return productImage;
    }

    public void setProductImage(byte[] productImage) {
        this.productImage = productImage;
    }

    public boolean isProductImagePresent() {
        return productImagePresent;
    }

    public void setProductImagePresent(boolean productImagePresent) {
        this.productImagePresent = productImagePresent;
    }

    public List<byte[]> getProductSubImages() {
        return productSubImages;
    }

    public void setProductSubImages(List<byte[]> productSubImages) {
        this.productSubImages = productSubImages;
    }

    public boolean isProductSubImagesPresent() {
        return productSubImagesPresent;
    }

    public void setProductSubImagesPresent(boolean productSubImagesPresent) {
        this.productSubImagesPresent = productSubImagesPresent;
    }
}