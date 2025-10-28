package com.thb.bakery.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductDTO {

    @JsonProperty("productId")
    private Long productId;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("productCategory")
    private String productCategory;

    @JsonProperty("productSubCategory")
    private String productSubCategory;

    @JsonProperty("productFoodType")
    private String productFoodType;

    @JsonProperty("skuNumber")
    private String skuNumber;

    @JsonProperty("nameOnCake")
    private String nameOnCake;

    @JsonProperty("orderCount")
    private Integer orderCount;

    @JsonProperty("description")
    private String description;

    @JsonProperty("productIngredients")
    private String productIngredients;

    @JsonProperty("allergenInfo")
    private String allergenInfo;

    @JsonProperty("careInstructions")
    private String careInstructions;

    @JsonProperty("storageInstructions")
    private String storageInstructions;

    @JsonProperty("shelfLife")
    private String shelfLife;

    @JsonProperty("bestServed")
    private String bestServed;

    @JsonProperty("preparationTime")
    private String preparationTime;

    @JsonProperty("flavor")
    private String flavor;

    @JsonProperty("shape")
    private String shape;

    @JsonProperty("defaultWeight")
    private String defaultWeight;

    @JsonProperty("layers")
    private String layers;

    @JsonProperty("serves")
    private String serves;

    @JsonProperty("note")
    private String note;

    @JsonProperty("productOldPrice")
    private BigDecimal productOldPrice;

    @JsonProperty("productNewPrice")
    private BigDecimal productNewPrice;

    @JsonProperty("weights")
    private List<String> weights = new ArrayList<>();

    @JsonProperty("weightPrices")
    private List<BigDecimal> weightPrices = new ArrayList<>();

    @JsonProperty("features")
    private List<String> features = new ArrayList<>();

    @JsonProperty("ratings")
    private Double ratings;

    @JsonProperty("reviews")
    private Integer reviews;

    @JsonProperty("productDiscount")
    private String productDiscount;

    @JsonProperty("deliveryTime")
    private String deliveryTime;

    @JsonProperty("freeDeliveryThreshold")
    private BigDecimal freeDeliveryThreshold;

    @JsonProperty("productImageUrl")
    private String productImageUrl;

    @JsonProperty("productSubImageUrls")
    private List<String> productSubImageUrls = new ArrayList<>();

    @JsonProperty("deleted")
    private boolean deleted;

    // Default constructor
    public ProductDTO() {}

    // Full constructor

    public ProductDTO(Long productId, String productName, String productCategory,
                      String productSubCategory, String productFoodType, String skuNumber,
                      String nameOnCake, Integer orderCount, String description,
                      String productIngredients, String allergenInfo, String careInstructions,
                      String storageInstructions, String shelfLife, String bestServed, String preparationTime, String flavor, String shape, String defaultWeight, String layers, String serves, String note, BigDecimal productOldPrice, BigDecimal productNewPrice, List<String> weights, List<BigDecimal> weightPrices, List<String> features, Double ratings, Integer reviews, String productDiscount, String deliveryTime, BigDecimal freeDeliveryThreshold, String productImageUrl, List<String> productSubImageUrls, boolean deleted) {
        this.productId = productId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productSubCategory = productSubCategory;
        this.productFoodType = productFoodType;
        this.skuNumber = skuNumber;
        this.nameOnCake = nameOnCake;
        this.orderCount = orderCount;
        this.description = description;
        this.productIngredients = productIngredients;
        this.allergenInfo = allergenInfo;
        this.careInstructions = careInstructions;
        this.storageInstructions = storageInstructions;
        this.shelfLife = shelfLife;
        this.bestServed = bestServed;
        this.preparationTime = preparationTime;
        this.flavor = flavor;
        this.shape = shape;
        this.defaultWeight = defaultWeight;
        this.layers = layers;
        this.serves = serves;
        this.note = note;
        this.productOldPrice = productOldPrice;
        this.productNewPrice = productNewPrice;
        this.weights = weights;
        this.weightPrices = weightPrices;
        this.features = features;
        this.ratings = ratings;
        this.reviews = reviews;
        this.productDiscount = productDiscount;
        this.deliveryTime = deliveryTime;
        this.freeDeliveryThreshold = freeDeliveryThreshold;
        this.productImageUrl = productImageUrl;
        this.productSubImageUrls = productSubImageUrls;
        this.deleted = deleted;
    }

    // Getters and Setters


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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

    public String getProductSubCategory() {
        return productSubCategory;
    }

    public void setProductSubCategory(String productSubCategory) {
        this.productSubCategory = productSubCategory;
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

    public String getStorageInstructions() {
        return storageInstructions;
    }

    public void setStorageInstructions(String storageInstructions) {
        this.storageInstructions = storageInstructions;
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

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public List<String> getProductSubImageUrls() {
        return productSubImageUrls;
    }

    public void setProductSubImageUrls(List<String> productSubImageUrls) {
        this.productSubImageUrls = productSubImageUrls;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productCategory='" + productCategory + '\'' +
                ", skuNumber='" + skuNumber + '\'' +
                ", productNewPrice=" + productNewPrice +
                ", ratings=" + ratings +
                ", deleted=" + deleted +
                '}';
    }
}
