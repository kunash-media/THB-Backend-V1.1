package com.thb.bakery.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "wishlist_items")
public class WishlistItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snack_id")
    private SnacksEntity snack;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customize_cake_id")
    private CustomizeCakeEntity customizeCake;

    @Column(name = "item_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Column(name = "added_date", nullable = false)
    private LocalDateTime addedDate = LocalDateTime.now();

    public enum ItemType {
        PRODUCT, SNACK, CUSTOMIZE_CAKE
    }

    // Constructors
    public WishlistItemEntity() {}

    public WishlistItemEntity(Long id, Long userId, ProductEntity product,
                              SnacksEntity snack, CustomizeCakeEntity customizeCake,
                              ItemType itemType, LocalDateTime addedDate) {
        this.id = id;
        this.userId = userId;
        this.product = product;
        this.snack = snack;
        this.customizeCake = customizeCake;
        this.itemType = itemType;
        this.addedDate = addedDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ProductEntity getProduct() { return product; }
    public void setProduct(ProductEntity product) { this.product = product; }

    public SnacksEntity getSnack() { return snack; }
    public void setSnack(SnacksEntity snack) { this.snack = snack; }

    public CustomizeCakeEntity getCustomizeCake() { return customizeCake; }
    public void setCustomizeCake(CustomizeCakeEntity customizeCake) { this.customizeCake = customizeCake; }

    public ItemType getItemType() { return itemType; }
    public void setItemType(ItemType itemType) { this.itemType = itemType; }

    public LocalDateTime getAddedDate() { return addedDate; }
    public void setAddedDate(LocalDateTime addedDate) { this.addedDate = addedDate; }
}