package com.thb.bakery.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@SQLDelete(sql = "UPDATE products SET is_deleted = true WHERE product_id = ?")
@Where(clause = "is_deleted = false")
public class ProductEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "product_id")
   private Long productId;

   @Column(name = "product_name", nullable = false)
   private String productName;

   @Column(name = "product_category", nullable = false)
   private String productCategory;

   @Column(name = "product_sub_category", nullable = false)
   private String productSubCategory;

   @Column(name = "product_food_type", nullable = false)
   private String productFoodType;

   @Column(name = "sku_number", nullable = false, unique = true)
   private String skuNumber;

   @Column(name = "name_on_cake")
   private String nameOnCake;

   @Column(name = "order_count")
   private Integer orderCount;

   @Column(name = "description")
   private String description;

   @Column(name = "product_ingredients")
   private String productIngredients;

   @Column(name = "allergen_info")
   private String allergenInfo;

   @Column(name = "care_instructions")
   private String careInstructions;

   @Column(name = "storage_instructions")
   private String storageInstructions;

   @Column(name = "shelf_life")
   private String shelfLife;

   @Column(name = "best_served")
   private String bestServed;

   @Column(name = "preparation_time")
   private String preparationTime;

   @Column(name = "flavor")
   private String flavor;

   @Column(name = "shape")
   private String shape;

   @Column(name = "default_weight", nullable = false)
   private String defaultWeight;

   @Column(name = "layers")
   private String layers;

   @Column(name = "serves")
   private String serves;

   @Column(name = "note")
   private String note;

   @Column(name = "product_old_price")
   private BigDecimal productOldPrice;

   @Column(name = "product_new_price", nullable = false)
   private BigDecimal productNewPrice;

   @ElementCollection
   @CollectionTable(name = "product_weights", joinColumns = @JoinColumn(name = "product_id"))
   @Column(name = "weight")
   private List<String> weights = new ArrayList<>();

   @ElementCollection
   @CollectionTable(name = "product_weight_prices", joinColumns = @JoinColumn(name = "product_id"))
   @Column(name = "weight_price")
   private List<BigDecimal> weightPrices = new ArrayList<>();

   @ElementCollection
   @CollectionTable(name = "product_features", joinColumns = @JoinColumn(name = "product_id"))
   @Column(name = "feature")
   private List<String> features = new ArrayList<>();

   @Column(name = "ratings")
   private Double ratings;

   @Column(name = "reviews")
   private Integer reviews;

   @Column(name = "product_discount")
   private String productDiscount;

   @Column(name = "delivery_time")
   private String deliveryTime;

   @Column(name = "free_delivery_threshold")
   private BigDecimal freeDeliveryThreshold;

   @Lob
   @Column(name = "product_image", columnDefinition = "LONGBLOB")
   private byte[] productImage;

   @ElementCollection
   @CollectionTable(name = "product_sub_images", joinColumns = @JoinColumn(name = "product_id"))
   @Lob
   @Column(name = "sub_image", columnDefinition = "LONGBLOB")
   private List<byte[]> productSubImages = new ArrayList<>();

   @Column(name = "is_deleted", nullable = false)
   private boolean deleted = false; // Changed from 'isDeleted' to 'deleted'

   public ProductEntity() {}


   public ProductEntity(Long productId, String productName, String productCategory,
                        String productSubCategory, String productFoodType, String skuNumber, String nameOnCake, Integer orderCount, String description, String productIngredients, String allergenInfo, String careInstructions, String storageInstructions, String shelfLife, String bestServed, String preparationTime, String flavor, String shape, String defaultWeight, String layers, String serves, String note, BigDecimal productOldPrice, BigDecimal productNewPrice, List<String> weights, List<BigDecimal> weightPrices, List<String> features, Double ratings, Integer reviews, String productDiscount, String deliveryTime, BigDecimal freeDeliveryThreshold, byte[] productImage, List<byte[]> productSubImages, boolean deleted) {
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
      this.productImage = productImage;
      this.productSubImages = productSubImages;
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

   public byte[] getProductImage() {
      return productImage;
   }

   public void setProductImage(byte[] productImage) {
      this.productImage = productImage;
   }

   public List<byte[]> getProductSubImages() {
      return productSubImages;
   }

   public void setProductSubImages(List<byte[]> productSubImages) {
      this.productSubImages = productSubImages;
   }

   public boolean isDeleted() {
      return deleted;
   }

   public void setDeleted(boolean deleted) {
      this.deleted = deleted;
   }

   public String getProductSubCategory() {
      return productSubCategory;
   }

   public void setProductSubCategory(String productSubCategory) {
      this.productSubCategory = productSubCategory;
   }
}
