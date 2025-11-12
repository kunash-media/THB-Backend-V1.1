package com.thb.bakery.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SnacksEntity – a snack product.
 *
 * PRODUCTION SCHEMA (unchanged):
 *   PK  : id
 *   FK  : none (order_items holds snack_id → snacks_table.id)
 *
 * NEW RELATIONSHIP (fixed):
 *   @OneToMany → OrderItemEntity (mapped by "snack")
 */
@Entity
@Table(name = "snacks_table")
public class SnacksEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "snack_id")
    private Long snackId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_category", nullable = false)
    private String productCategory;

    @Column(name = "product_subcategory")
    private String productSubcategory;

    @Column(name = "sku_number", unique = true, nullable = false)
    private String skuNumber;

    @Column(name = "product_old_price")
    private double productOldPrice;

    @Column(name = "product_new_price")
    private double productNewPrice;

    @Column(name = "ratings")
    private double ratings;

    @Column(name = "product_discount")
    private String productDiscount;

    @Lob
    @Column(name = "product_main_img", columnDefinition = "LONGBLOB")
    private byte[] productMainImage;

    /* --------------------------------------------------------------
       FIXED ONE-TO-MANY – a snack can be used in many order-items
       (the FK column in order_items is now "snack_id")
       -------------------------------------------------------------- */
    @OneToMany(mappedBy = "snack", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnore
    private List<OrderItemEntity> orderItems = new ArrayList<>();

    /* ----------------------------------------------------------------
       Helper methods – keep bidirectional sync (optional but safe)
       ---------------------------------------------------------------- */
    public void addOrderItem(OrderItemEntity orderItem) {
        orderItems.add(orderItem);
        orderItem.setSnack(this);
    }

    public void removeOrderItem(OrderItemEntity orderItem) {
        orderItems.remove(orderItem);
        orderItem.setSnack(null);
    }

    /* --------------------- Constructors --------------------- */
    public SnacksEntity() {}

    public SnacksEntity(Long snackId, String productName, String productCategory,
                        String productSubcategory, String skuNumber,
                        double productOldPrice, double productNewPrice,
                        double ratings, String productDiscount,
                        byte[] productMainImage) {
        this.snackId = snackId;
        this.productName = productName;
        this.productCategory = productCategory;
        this.productSubcategory = productSubcategory;
        this.skuNumber = skuNumber;
        this.productOldPrice = productOldPrice;
        this.productNewPrice = productNewPrice;
        this.ratings = ratings;
        this.productDiscount = productDiscount;
        this.productMainImage = productMainImage;
    }

    /* --------------------- Getters / Setters --------------------- */

    public Long getSnackId() {
        return snackId;
    }

    public void setSnackId(Long snackId) {
        this.snackId = snackId;
    }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getProductCategory() { return productCategory; }
    public void setProductCategory(String productCategory) { this.productCategory = productCategory; }

    public String getProductSubcategory() { return productSubcategory; }
    public void setProductSubcategory(String productSubcategory) { this.productSubcategory = productSubcategory; }

    public String getSkuNumber() { return skuNumber; }
    public void setSkuNumber(String skuNumber) { this.skuNumber = skuNumber; }

    public double getProductOldPrice() { return productOldPrice; }
    public void setProductOldPrice(double productOldPrice) { this.productOldPrice = productOldPrice; }

    public double getProductNewPrice() { return productNewPrice; }
    public void setProductNewPrice(double productNewPrice) { this.productNewPrice = productNewPrice; }

    public double getRatings() { return ratings; }
    public void setRatings(double ratings) { this.ratings = ratings; }

    public String getProductDiscount() { return productDiscount; }
    public void setProductDiscount(String productDiscount) { this.productDiscount = productDiscount; }

    public byte[] getProductMainImage() { return productMainImage; }
    public void setProductMainImage(byte[] productMainImage) { this.productMainImage = productMainImage; }

    public List<OrderItemEntity> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemEntity> orderItems) { this.orderItems = orderItems; }
}