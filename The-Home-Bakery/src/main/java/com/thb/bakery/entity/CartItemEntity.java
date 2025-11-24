package com.thb.bakery.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Entity
@Table(name = "cart_items")
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "snack_id")
    private SnacksEntity snack;

    @Column(name = "item_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Column(name = "quantity")
    private Integer quantity = 1;

    @Column(name = "size")
    private String size = "free size";

    @Column(name = "added_date")
    private LocalDateTime addedDate = LocalDateTime.now();

    // THIS IS THE ONLY CHANGE — REPLACES Set<Addon>
    @ElementCollection
    @CollectionTable(
            name = "cart_item_addons",
            joinColumns = @JoinColumn(name = "cart_item_id")
    )
    @MapKeyColumn(name = "addon_id")
    @Column(name = "quantity", nullable = false)
    private Map<Addon, Integer> addonQuantityMap = new HashMap<>();

    public enum ItemType {
        PRODUCT, SNACK
    }

    // Helper Methods
    public void addAddonWithQuantity(Addon addon, int qty) {
        this.addonQuantityMap.put(addon, Math.max(1, qty));
    }

    public void updateAddonQuantity(Addon addon, int qty) {
        if (qty <= 0) {
            this.addonQuantityMap.remove(addon);
        } else {
            this.addonQuantityMap.put(addon, qty);
        }
    }

    public void clearAddons() {
        this.addonQuantityMap.clear();
    }

    public CartItemEntity() {}

    public CartItemEntity(Long id, Long userId, ProductEntity product, SnacksEntity snack,
                          ItemType itemType, Integer quantity, String size, LocalDateTime addedDate,
                          Map<Addon, Integer> addonQuantityMap) {
        this.id = id;
        this.userId = userId;
        this.product = product;
        this.snack = snack;
        this.itemType = itemType;
        this.quantity = quantity;
        this.size = size;
        this.addedDate = addedDate;
        this.addonQuantityMap = addonQuantityMap;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public SnacksEntity getSnack() {
        return snack;
    }

    public void setSnack(SnacksEntity snack) {
        this.snack = snack;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public LocalDateTime getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDateTime addedDate) {
        this.addedDate = addedDate;
    }

    public Map<Addon, Integer> getAddonQuantityMap() {
        return addonQuantityMap;
    }

    public void setAddonQuantityMap(Map<Addon, Integer> addonQuantityMap) {
        this.addonQuantityMap = addonQuantityMap;
    }
}














//@Entity
//@Table(name = "cart_items")
//public class CartItemEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(name = "user_id")
//    private Long userId;
//
//    // --- FK to Product (JPA Relationship) ---
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id", nullable = false)
//    private ProductEntity product;
//
//
//    // --- SNACK (NEW) ---
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "snack_id", nullable = true)
//    private SnacksEntity snack;
//
//    // --- ITEM TYPE (for clarity & future use) ---
//    @Column(name = "item_type", nullable = false)
//    @Enumerated(EnumType.STRING)
//    private ItemType itemType;  // PRODUCT or SNACK
//
//    // --- Many-to-Many with Addon ---
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "cart_item_addons",
//            joinColumns = @JoinColumn(name = "cart_item_id"),
//            inverseJoinColumns = @JoinColumn(name = "addon_id")
//    )
//    private Set<Addon> addons = new HashSet<>();
//
//    @Column(name = "quantity")
//    private Integer quantity = 1;
//
//    @Column(name = "size")
//    private String size;
//
//    @Column(name = "added_date")
//    private LocalDateTime addedDate = LocalDateTime.now();
//
//    public CartItemEntity() {}
//
//    public enum ItemType {
//        PRODUCT, SNACK
//    }
//
//    public CartItemEntity(Long id, Long userId, ProductEntity product, Integer quantity, String size, LocalDateTime addedDate, Set<Addon> addons) {
//        this.id = id;
//        this.userId = userId;
//        this.product = product;
//        this.quantity = quantity;
//        this.size = size;
//        this.addedDate = addedDate;
//        this.addons = addons != null ? addons : new HashSet<>();
//    }
//
//
//
//    // Getters and Setters
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public Long getUserId() { return userId; }
//    public void setUserId(Long userId) { this.userId = userId; }
//
//    public ProductEntity getProduct() { return product; }
//    public void setProduct(ProductEntity product) { this.product = product; }
//
//    public Set<Addon> getAddons() { return addons; }
//    public void setAddons(Set<Addon> addons) { this.addons = addons; }
//
//    public Integer getQuantity() { return quantity; }
//    public void setQuantity(Integer quantity) { this.quantity = quantity; }
//
//    public String getSize() { return size; }
//    public void setSize(String size) { this.size = size; }
//
//    public LocalDateTime getAddedDate() { return addedDate; }
//    public void setAddedDate(LocalDateTime addedDate) { this.addedDate = addedDate; }
//
//
//    public SnacksEntity getSnack() {
//        return snack;
//    }
//
//    public void setSnack(SnacksEntity snack) {
//        this.snack = snack;
//    }
//
//    public ItemType getItemType() {
//        return itemType;
//    }
//
//    public void setItemType(ItemType itemType) {
//        this.itemType = itemType;
//    }
//}