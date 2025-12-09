package com.thb.bakery.controller;

import com.thb.bakery.entity.*;
import com.thb.bakery.repository.CustomizeCakeRepository;
import com.thb.bakery.repository.ProductRepository;
import com.thb.bakery.repository.SnacksRepository;
import com.thb.bakery.repository.WishlistItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional; // Add this import

@RestController
@RequestMapping("/api/wishlist")
@Slf4j
public class WishlistController {

    private static final Logger logger = LoggerFactory.getLogger(WishlistController.class);

    private final WishlistItemRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final SnacksRepository snacksRepository;
    private final CustomizeCakeRepository customizeCakeRepository;


    public WishlistController(WishlistItemRepository wishlistRepository, ProductRepository productRepository, SnacksRepository snacksRepository, CustomizeCakeRepository customizeCakeRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.snacksRepository = snacksRepository;
        this.customizeCakeRepository = customizeCakeRepository;
    }

    /**
     * Add item to wishlist - supports PRODUCT, SNACK, CUSTOMIZE_CAKE
     */
    @PostMapping("/add-to-wishlist")
    @Transactional
    public ResponseEntity<?> addToWishlist(@RequestBody Map<String, Object> payload) {
        logger.info("Add to wishlist request: {}", payload);

        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            String itemTypeStr = (String) payload.get("itemType");

            if (userId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "userId is required"));
            }
            if (itemTypeStr == null || itemTypeStr.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "itemType is required"));
            }

            WishlistItemEntity.ItemType itemType;
            try {
                itemType = WishlistItemEntity.ItemType.valueOf(itemTypeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid itemType: " + itemTypeStr));
            }

            // Check if already exists
            Optional<WishlistItemEntity> existing = switch (itemType) {
                case PRODUCT -> {
                    Long productId = Long.valueOf(payload.get("productId").toString());
                    yield wishlistRepository.findByUserIdAndProductProductId(userId, productId);
                }
                case SNACK -> {
                    Long snackId = Long.valueOf(payload.get("snackId").toString());
                    yield wishlistRepository.findByUserIdAndSnackSnackId(userId, snackId);
                }
                case CUSTOMIZE_CAKE -> {
                    Long cakeId = Long.valueOf(payload.get("customizeCakeId").toString());
                    yield wishlistRepository.findByUserIdAndCustomizeCakeId(userId, cakeId);
                }
            };

            if (existing.isPresent()) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Already in wishlist",
                        "wishlistItemId", existing.get().getId()
                ));
            }

            // Create new item
            WishlistItemEntity item = new WishlistItemEntity();
            item.setUserId(userId);
            item.setItemType(itemType);

            // Set the actual item based on type
            switch (itemType) {
                case PRODUCT -> {
                    Long productId = Long.valueOf(payload.get("productId").toString());
                    if (productId == null) {
                        return ResponseEntity.badRequest().body(Map.of("error", "productId required for PRODUCT"));
                    }
                    ProductEntity product = productRepository.findById(productId)
                            .orElse(null);
                    if (product == null) {
                        return ResponseEntity.badRequest().body(Map.of("error", "Product not found"));
                    }
                    item.setProduct(product);
                }
                case SNACK -> {
                    Long snackId = Long.valueOf(payload.get("snackId").toString());
                    if (snackId == null) {
                        return ResponseEntity.badRequest().body(Map.of("error", "snackId required for SNACK"));
                    }
                    SnacksEntity snack = snacksRepository.findById(snackId)
                            .orElse(null);
                    if (snack == null) {
                        return ResponseEntity.badRequest().body(Map.of("error", "Snack not found"));
                    }
                    item.setSnack(snack);
                }
                case CUSTOMIZE_CAKE -> {
                    Long cakeId = Long.valueOf(payload.get("customizeCakeId").toString());
                    if (cakeId == null) {
                        return ResponseEntity.badRequest().body(Map.of("error", "customizeCakeId required for CUSTOMIZE_CAKE"));
                    }
                    CustomizeCakeEntity cake = customizeCakeRepository.findById(cakeId)
                            .orElse(null);
                    if (cake == null) {
                        return ResponseEntity.badRequest().body(Map.of("error", "Custom cake not found"));
                    }
                    item.setCustomizeCake(cake);
                }
            }

            WishlistItemEntity saved = wishlistRepository.save(item);
            logger.info("Added to wishlist for user {}: itemId={}", userId, saved.getId());

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Added to wishlist successfully",
                    "wishlistItemId", saved.getId()
            ));

        } catch (NumberFormatException e) {
            logger.error("Invalid number format in payload", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid number format"));
        } catch (Exception e) {
            logger.error("Error adding to wishlist", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error"));
        }
    }

    /**
     * Get all wishlist items for a user
     */
    @GetMapping("/get-wishlisted-items")
    public ResponseEntity<?> getWishlist(@RequestParam Long userId) {
        logger.debug("Get wishlist request for userId: {}", userId);

        if (userId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "userId is required"));
        }

        try {
            List<WishlistItemEntity> items = wishlistRepository.findByUserIdWithItems(userId);
            logger.debug("Found {} wishlist items for user {}", items.size(), userId);

            List<Map<String, Object>> response = items.stream().map(this::buildWishlistItemResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "totalItems", response.size(),
                    "items", response
            ));

        } catch (Exception e) {
            logger.error("Error getting wishlist for user {}", userId, e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error"));
        }
    }

    /**
     * Remove specific item from wishlist
     */
    @PostMapping("/remove-wishlist-item")
    @Transactional
    public ResponseEntity<?> removeFromWishlist(@RequestBody Map<String, Object> payload) {
        logger.debug("Remove from wishlist request: {}", payload);

        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            Long wishlistItemId = Long.valueOf(payload.get("wishlistItemId").toString());

            if (userId == null || wishlistItemId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "userId and wishlistItemId are required"));
            }

            Optional<WishlistItemEntity> existing = wishlistRepository.findById(wishlistItemId);
            if (existing.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Item not found"));
            }

            WishlistItemEntity item = existing.get();
            if (!item.getUserId().equals(userId)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Unauthorized"));
            }

            wishlistRepository.delete(item);
            logger.info("Removed wishlist item {} for user {}", wishlistItemId, userId);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Removed from wishlist successfully"
            ));

        } catch (NumberFormatException e) {
            logger.error("Invalid number format in payload", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid number format"));
        } catch (Exception e) {
            logger.error("Error removing from wishlist", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error"));
        }
    }

    /**
     * Clear entire wishlist
     */
    @PostMapping("/clear-wishlist")
    @Transactional
    public ResponseEntity<?> clearWishlist(@RequestBody Map<String, Object> payload) {
        logger.debug("Clear wishlist request: {}", payload);

        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            if (userId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "userId is required"));
            }

            long count = wishlistRepository.countByUserId(userId);
            wishlistRepository.deleteByUserId(userId);
            logger.info("Cleared {} wishlist items for user {}", count, userId);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Wishlist cleared successfully",
                    "itemsRemoved", count
            ));

        } catch (NumberFormatException e) {
            logger.error("Invalid number format in payload", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid number format"));
        } catch (Exception e) {
            logger.error("Error clearing wishlist", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error"));
        }
    }

    /**
     * Merge localStorage wishlist with server wishlist
     */
    @PostMapping("/merge-wishlist")
    @Transactional
    public ResponseEntity<?> mergeLocalWishlist(@RequestBody Map<String, Object> payload) {
        logger.debug("Merge wishlist request: {}", payload);

        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> localItems = (List<Map<String, Object>>) payload.get("items");

            if (userId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "userId is required"));
            }
            if (localItems == null || localItems.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "No items to merge"
                ));
            }

            int mergedCount = 0;
            for (Map<String, Object> localItem : localItems) {
                String productType = (String) localItem.get("productType");
                Long itemId = Long.valueOf(localItem.get("id").toString());

                if (productType == null || itemId == null) {
                    logger.warn("Skipping invalid local item: {}", localItem);
                    continue;
                }

                // Check if already exists
                boolean alreadyExists = switch (productType.toUpperCase()) {
                    case "PRODUCT" -> wishlistRepository.findByUserIdAndProductProductId(userId, itemId).isPresent();
                    case "SNACK" -> wishlistRepository.findByUserIdAndSnackSnackId(userId, itemId).isPresent();
                    case "CUSTOMIZE_CAKE" -> wishlistRepository.findByUserIdAndCustomizeCakeId(userId, itemId).isPresent();
                    default -> {
                        logger.warn("Unknown productType: {}", productType);
                        yield true;
                    }
                };

                if (!alreadyExists) {
                    // Create new item
                    WishlistItemEntity item = new WishlistItemEntity();
                    item.setUserId(userId);
                    item.setItemType(WishlistItemEntity.ItemType.valueOf(productType.toUpperCase()));

                    // Set the actual item
                    switch (productType.toUpperCase()) {
                        case "PRODUCT" -> productRepository.findById(itemId).ifPresent(item::setProduct);
                        case "SNACK" -> snacksRepository.findById(itemId).ifPresent(item::setSnack);
                        case "CUSTOMIZE_CAKE" -> customizeCakeRepository.findById(itemId).ifPresent(item::setCustomizeCake);
                    }

                    if (item.getProduct() != null || item.getSnack() != null || item.getCustomizeCake() != null) {
                        wishlistRepository.save(item);
                        mergedCount++;
                    }
                }
            }

            logger.info("Merged {} items to wishlist for user {}", mergedCount, userId);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Wishlist merged successfully",
                    "itemsMerged", mergedCount,
                    "totalLocalItems", localItems.size()
            ));

        } catch (NumberFormatException e) {
            logger.error("Invalid number format in payload", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid number format"));
        } catch (Exception e) {
            logger.error("Error merging wishlist", e);
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal server error"));
        }
    }

    // Helper method to build response
    private Map<String, Object> buildWishlistItemResponse(WishlistItemEntity item) {
        Map<String, Object> map = new HashMap<>();
        map.put("wishlistItemId", item.getId());
        map.put("addedDate", item.getAddedDate());
        map.put("itemType", item.getItemType().name());
        map.put("productType", item.getItemType().name());

        switch (item.getItemType()) {
            case PRODUCT -> {
                ProductEntity p = item.getProduct();
                if (p != null) {
                    map.put("id", p.getProductId());
                    map.put("productId", p.getProductId());
                    map.put("name", p.getProductName());
                    map.put("price", p.getProductNewPrice());
                    map.put("image", "/api/v1/products/" + p.getProductId() + "/image");
                }
            }
            case SNACK -> {
                SnacksEntity s = item.getSnack();
                if (s != null) {
                    map.put("id", s.getSnackId());
                    map.put("snackId", s.getSnackId());
                    map.put("name", s.getProductName());
                    map.put("price", s.getProductNewPrice());
                    map.put("image", "/api/v1/snacks/" + s.getSnackId() + "/image");
                }
            }
            case CUSTOMIZE_CAKE -> {
                CustomizeCakeEntity c = item.getCustomizeCake();
                if (c != null) {
                    map.put("id", c.getId());
                    map.put("customizeCakeId", c.getId());
                    map.put("name", c.getTitle());
                    map.put("price", c.getNewPrices() != null && !c.getNewPrices().isEmpty()
                            ? c.getNewPrices().get(0) : 0);
                    map.put("image", "/api/customize-cakes/" + c.getId() + "/image");
                }
            }
        }
        return map;
    }
}