package com.thb.bakery.controller;

import com.thb.bakery.entity.Addon;
import com.thb.bakery.entity.CartItemEntity;
import com.thb.bakery.entity.ProductEntity;
import com.thb.bakery.entity.SnacksEntity;
import com.thb.bakery.repository.AddonRepository;
import com.thb.bakery.repository.CartItemRepository;
import com.thb.bakery.repository.ProductRepository;
import com.thb.bakery.repository.SnacksRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private AddonRepository addonRepository;
    @Autowired private SnacksRepository snacksRepository;



    @PostMapping("/add-cart-items")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody Map<String, Object> request) {
        logger.info("Add to cart request received: {}", request);
        try {
            Long userId = Long.parseLong(request.get("userId").toString());
            Integer quantity = Integer.parseInt(request.getOrDefault("quantity", "1").toString());
            String size = request.getOrDefault("size", "free size").toString();
            List<Long> addonIds = request.get("addonIds") != null ?
                    ((List<?>) request.get("addonIds")).stream()
                            .map(id -> Long.parseLong(id.toString()))
                            .collect(Collectors.toList()) : new ArrayList<>();

            // READ itemType FROM REQUEST
            String itemTypeStr = (String) request.get("itemType");
            if (itemTypeStr == null || itemTypeStr.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "itemType is required"));
            }

            CartItemEntity.ItemType itemType;
            try {
                itemType = CartItemEntity.ItemType.valueOf(itemTypeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body(Map.of("error", "Invalid itemType: " + itemTypeStr));
            }

            if (userId == null) return ResponseEntity.badRequest().body(Map.of("error", "User ID required"));
            if (quantity < 1) return ResponseEntity.badRequest().body(Map.of("error", "Quantity must be positive"));

            Set<Addon> addons = new HashSet<>();
            if (!addonIds.isEmpty()) {
                addons = new HashSet<>(addonRepository.findAllById(addonIds));
            }

            CartItemEntity item;
            Optional<CartItemEntity> existing;

            if (itemType == CartItemEntity.ItemType.PRODUCT) {
                Long productId = request.containsKey("productId") ? Long.parseLong(request.get("productId").toString()) : null;
                if (productId == null || productId <= 0) {
                    return ResponseEntity.badRequest().body(Map.of("error", "productId required for PRODUCT"));
                }

                Optional<ProductEntity> productOpt = productRepository.findById(productId);
                if (productOpt.isEmpty()) return ResponseEntity.badRequest().body(Map.of("error", "Product not found"));

                ProductEntity product = productOpt.get();
                existing = cartItemRepository.findByUserIdAndProductAndSize(userId, product, size);

                if (existing.isPresent()) {
                    item = existing.get();
                    item.setQuantity(quantity);
                    item.setAddons(addons);
                } else {
                    item = new CartItemEntity();
                    item.setUserId(userId);
                    item.setProduct(product);
                    item.setItemType(CartItemEntity.ItemType.PRODUCT);
                    item.setQuantity(quantity);
                    item.setSize(size);
                    item.setAddons(addons);
                }

            } else if (itemType == CartItemEntity.ItemType.SNACK) {
                Long snackId = request.containsKey("snackId") ? Long.parseLong(request.get("snackId").toString()) : null;
                if (snackId == null || snackId <= 0) {
                    return ResponseEntity.badRequest().body(Map.of("error", "snackId required for SNACK"));
                }

                Optional<SnacksEntity> snackOpt = snacksRepository.findById(snackId);
                if (snackOpt.isEmpty()) return ResponseEntity.badRequest().body(Map.of("error", "Snack not found"));

                SnacksEntity snack = snackOpt.get();
                existing = cartItemRepository.findByUserIdAndSnackAndSize(userId, snack, size);

                if (existing.isPresent()) {
                    item = existing.get();
                    item.setQuantity(quantity);
                    item.setAddons(addons);
                } else {
                    item = new CartItemEntity();
                    item.setUserId(userId);
                    item.setSnack(snack);
                    item.setItemType(CartItemEntity.ItemType.SNACK);
                    item.setQuantity(quantity);
                    item.setSize(size);
                    item.setAddons(addons);
                }

            } else {
                return ResponseEntity.badRequest().body(Map.of("error", "Unsupported itemType"));
            }

            cartItemRepository.save(item);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Added to cart",
                    "cartItemId", item.getId(),
                    "quantity", quantity
            ));

        } catch (Exception e) {
            logger.error("Error adding to cart", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request: " + e.getMessage()));
        }
    }





    @GetMapping("/get-cart-items")
    public ResponseEntity<List<Map<String, Object>>> getCart(@RequestParam Long userId) {
        logger.info("Get cart request received for userId: {}", userId);

        if (userId == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            List<CartItemEntity> items = cartItemRepository.findByUserIdWithProductAndAddons(userId);
            logger.debug("Found {} cart items for userId: {}", items.size(), userId);

            List<Map<String, Object>> response = new ArrayList<>();

            for (CartItemEntity item : items) {
                Map<String, Object> map = new HashMap<>();
                map.put("cartItemId", item.getId());
                map.put("quantity", item.getQuantity());
                map.put("size", item.getSize());
                map.put("addedDate", item.getAddedDate());

                if (item.getItemType() == CartItemEntity.ItemType.PRODUCT && item.getProduct() != null) {
                    ProductEntity p = item.getProduct();
                    map.put("productId", p.getProductId());
                    map.put("title", p.getProductName());
                    map.put("price", p.getProductNewPrice());
                    map.put("imageUrl", "/api/v1/products/" + p.getProductId() + "/image");
                    map.put("subImageUrls", getSubImageUrls(p.getProductId(), p.getProductSubImages().size()));
                    map.put("itemType", "PRODUCT");
                }
                else if (item.getItemType() == CartItemEntity.ItemType.SNACK && item.getSnack() != null) {
                    SnacksEntity s = item.getSnack();
                    map.put("snackId", s.getSnackId());
                    map.put("title", s.getProductName());
                    map.put("price", s.getProductNewPrice());
                    map.put("imageUrl", "/api/v1/snacks/" + s.getSnackId() + "/image");
                    map.put("itemType", "SNACK");
                }

                List<Map<String, Object>> addonList = item.getAddons().stream().map(addon -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", addon.getId());
                    m.put("itemKey", addon.getItemKey());
                    m.put("name", addon.getName());
                    m.put("price", addon.getPrice());
                    m.put("imageUrl", addon.getImageUrl());
                    return m;
                }).collect(Collectors.toList());
                map.put("addons", addonList);

                response.add(map);
            }

            logger.info("Successfully retrieved {} cart items for userId: {}", response.size(), userId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting cart", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/remove-cart-items")
    public ResponseEntity<String> removeFromCart(@RequestBody Map<String, Object> request) {
        logger.info("Remove from cart request received: {}", request);

        try {
            Long userId = Long.parseLong(request.get("userId").toString());
            Long cartItemId = Long.parseLong(request.get("cartItemId").toString());

            Optional<CartItemEntity> existing = cartItemRepository.findById(cartItemId);
            if (existing.isEmpty()) {
                return ResponseEntity.badRequest().body("Item not found in cart");
            }

            CartItemEntity item = existing.get();
            if (!item.getUserId().equals(userId)) {
                return ResponseEntity.badRequest().body("Unauthorized");
            }

            cartItemRepository.delete(item);
            return ResponseEntity.ok("Removed from cart");

        } catch (Exception e) {
            logger.error("Error removing from cart: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Invalid request data");
        }
    }

    @PostMapping("/update-cart-items")
    public ResponseEntity<Map<String, Object>> updateCartItem(@RequestBody Map<String, Object> request) {
        logger.info("Update cart item request received: {}", request);

        try {
            Long userId = Long.parseLong(request.get("userId").toString());
            Long cartItemId = Long.parseLong(request.get("cartItemId").toString());
            Integer quantity = Integer.parseInt(request.get("quantity").toString());

            if (quantity <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Quantity must be positive"));
            }

            Optional<CartItemEntity> existing = cartItemRepository.findById(cartItemId);
            if (existing.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Item not found in cart"));
            }

            CartItemEntity item = existing.get();
            if (!item.getUserId().equals(userId)) {
                return ResponseEntity.badRequest().body(Map.of("error", "Unauthorized"));
            }

            item.setQuantity(quantity);
            cartItemRepository.save(item);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Cart updated",
                    "cartItemId", cartItemId,
                    "quantity", quantity
            ));

        } catch (Exception e) {
            logger.error("Error updating cart item: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request data: " + e.getMessage()));
        }
    }

    @PostMapping("/merge-cart-items")
    public ResponseEntity<String> mergeCart(@RequestBody Map<String, Object> request) {
        logger.info("Merge cart request received: {}", request);

        try {
            Long userId = Long.parseLong(request.get("userId").toString());
            if (userId == null) {
                logger.warn("Merge cart failed: User ID is null");
                return ResponseEntity.badRequest().body("User ID required");
            }

            List<Map<String, Object>> localCart = (List<Map<String, Object>>) request.get("items");
            if (localCart == null || localCart.isEmpty()) {
                logger.info("Merge cart: No items to merge for userId: {}", userId);
                return ResponseEntity.ok("No items to merge");
            }

            logger.debug("Merging {} items for userId: {}", localCart.size(), userId);
            int mergedCount = 0;
            int skippedCount = 0;

            for (Map<String, Object> localItem : localCart) {
                Long productId = Long.parseLong(localItem.get("id").toString());
                Integer quantity = Integer.parseInt(localItem.getOrDefault("quantity", "1").toString());
                String size = localItem.getOrDefault("size", "free size").toString();
                List<Long> addonIds = localItem.get("addonIds") != null ?
                        ((List<?>) localItem.get("addonIds")).stream()
                                .map(id -> Long.parseLong(id.toString()))
                                .collect(Collectors.toList()) : new ArrayList<>();

                Optional<ProductEntity> productOpt = productRepository.findById(productId);
                if (productOpt.isEmpty()) {
                    logger.warn("Merge cart: Product not found - productId: {}, skipping", productId);
                    skippedCount++;
                    continue;
                }

                ProductEntity product = productOpt.get();
                Set<Addon> addons = new HashSet<>(addonRepository.findAllById(addonIds));

                Optional<CartItemEntity> existing = cartItemRepository.findByUserIdAndProductIdAndSize(userId, productId, size);
                if (existing.isPresent()) {
                    CartItemEntity item = existing.get();
                    item.setQuantity(quantity);
                    item.setAddons(addons);
                    cartItemRepository.save(item);
                    logger.debug("Updated existing cart item during merge - cartItemId: {}, productId: {}, addons: {}",
                            item.getId(), productId, addons.size());
                } else {
                    CartItemEntity item = new CartItemEntity();
                    item.setUserId(userId);
                    item.setProduct(product);
                    item.setQuantity(quantity);
                    item.setSize(size);
                    item.setAddons(addons);
                    cartItemRepository.save(item);
                    logger.debug("Created new cart item during merge - cartItemId: {}, productId: {}, addons: {}",
                            item.getId(), productId, addons.size());
                }
                mergedCount++;
            }

            logger.info("Cart merge completed for userId: {} - merged: {}, skipped: {}", userId, mergedCount, skippedCount);
            return ResponseEntity.ok("Cart merged successfully");

        } catch (Exception e) {
            logger.error("Error merging cart: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Invalid request data: " + e.getMessage());
        }
    }

    @PostMapping("/clear-cart")
    @Transactional
    public ResponseEntity<String> clearCart(@RequestBody Map<String, Object> request) {
        logger.info("Clear cart request received: {}", request);

        try {
            Long userId = Long.parseLong(request.get("userId").toString());
            if (userId == null) {
                logger.warn("Clear cart failed: User ID is null");
                return ResponseEntity.badRequest().body("User ID required");
            }

            int itemCount = cartItemRepository.countByUserId(userId);
            cartItemRepository.deleteByUserId(userId);
            logger.info("Successfully cleared cart for userId: {} - {} items deleted", userId, itemCount);

            return ResponseEntity.ok("Cart cleared successfully");

        } catch (Exception e) {
            logger.error("Error clearing cart: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Invalid request data");
        }
    }

    private List<String> getSubImageUrls(Long productId, int size) {
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            urls.add("/api/products/" + productId + "/subimage/" + i);
        }
        return urls;
    }
}