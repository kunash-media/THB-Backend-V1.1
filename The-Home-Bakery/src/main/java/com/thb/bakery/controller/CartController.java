package com.thb.bakery.controller;

import com.thb.bakery.entity.Addon;
import com.thb.bakery.entity.CartItemEntity;
import com.thb.bakery.entity.ProductEntity;
import com.thb.bakery.repository.AddonRepository;
import com.thb.bakery.repository.CartItemRepository;
import com.thb.bakery.repository.ProductRepository;
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

    @PostMapping("/add-cart-items")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody Map<String, Object> request) {
        logger.info("Add to cart request received: {}", request);
        try {
            Long userId = Long.parseLong(request.get("userId").toString());
            Long productId = Long.parseLong(request.get("productId").toString());
            Integer quantity = Integer.parseInt(request.getOrDefault("quantity", "1").toString());
            String size = request.getOrDefault("size", "free size").toString();
            List<Long> addonIds = request.get("addonIds") != null ?
                    ((List<?>) request.get("addonIds")).stream()
                            .map(id -> Long.parseLong(id.toString()))
                            .collect(Collectors.toList()) : new ArrayList<>();

            logger.debug("Parsed request - userId: {}, productId: {}, quantity: {}, size: {}, addonIds: {}",
                    userId, productId, quantity, size, addonIds);

            if (userId == null) {
                logger.warn("Add to cart failed: User ID is null");
                return ResponseEntity.badRequest().body(Map.of("error", "User ID required"));
            }
            if (quantity < 1) {
                logger.warn("Add to cart failed: Invalid quantity - {}", quantity);
                return ResponseEntity.badRequest().body(Map.of("error", "Quantity must be positive"));
            }

            // Find the product entity
            Optional<ProductEntity> productOpt = productRepository.findById(productId);
            if (productOpt.isEmpty()) {
                logger.warn("Add to cart failed: Product not found - productId: {}", productId);
                return ResponseEntity.badRequest().body(Map.of("error", "Product not found"));
            }

            ProductEntity product = productOpt.get();
            logger.debug("Product found: {}", product.getProductName());

            // Fetch add-ons
            Set<Addon> addons = new HashSet<>();
            if (!addonIds.isEmpty()) {
                addons = new HashSet<>(addonRepository.findAllById(addonIds));
                if (addons.size() != addonIds.size()) {
                    logger.warn("Some add-ons not found - requested: {}, found: {}", addonIds, addons.size());
                }
            }

            // Check if item already exists in cart
            Optional<CartItemEntity> existing = cartItemRepository.findByUserIdAndProductAndSize(userId, product, size);
            CartItemEntity item;

            if (existing.isPresent()) {
                item = existing.get();
                item.setQuantity(quantity);
                item.setAddons(addons);
                logger.info("Updating existing cart item - cartItemId: {}, new quantity: {}, addons: {}",
                        item.getId(), quantity, addons.size());
            } else {
                item = new CartItemEntity();
                item.setUserId(userId);
                item.setProduct(product);
                item.setQuantity(quantity);
                item.setSize(size);
                item.setAddons(addons);
                logger.info("Creating new cart item for userId: {}, productId: {}, addons: {}",
                        userId, productId, addons.size());
            }

            cartItemRepository.save(item);
            logger.info("Cart item saved successfully - cartItemId: {}", item.getId());

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Added to cart",
                    "productId", productId,
                    "quantity", quantity,
                    "cartItemId", item.getId(),
                    "addonIds", addonIds
            ));

        } catch (Exception e) {
            logger.error("Error adding to cart: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request data: " + e.getMessage()));
        }
    }

    @GetMapping("/get-cart-items")
    public ResponseEntity<List<Map<String, Object>>> getCart(@RequestParam Long userId) {
        logger.info("Get cart request received for userId: {}", userId);

        if (userId == null) {
            logger.warn("Get cart failed: User ID is null");
            return ResponseEntity.badRequest().build();
        }

        try {
            // Use the eager fetching method to avoid LazyInitializationException
            List<CartItemEntity> items = cartItemRepository.findByUserIdWithProductAndAddons(userId);
            logger.debug("Found {} cart items for userId: {}", items.size(), userId);

            List<Map<String, Object>> response = new ArrayList<>();

            for (CartItemEntity item : items) {
                ProductEntity product = item.getProduct();
                if (product != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("cartItemId", item.getId());
                    map.put("productId", product.getProductId());
                    map.put("title", product.getProductName());
                    map.put("price", product.getProductNewPrice());
                    map.put("imageUrl", "/api/v1/products/" + product.getProductId() + "/image");
                    map.put("subImageUrls", getSubImageUrls(product.getProductId(), product.getProductSubImages().size()));
                    map.put("quantity", item.getQuantity());
                    map.put("size", item.getSize());
                    map.put("addedDate", item.getAddedDate());
                    // Add add-ons to the response
                    List<Map<String, Object>> addonList = item.getAddons().stream().map(addon -> {
                        Map<String, Object> addonMap = new HashMap<>();
                        addonMap.put("id", addon.getId());
                        addonMap.put("itemKey", addon.getItemKey());
                        addonMap.put("name", addon.getName());
                        addonMap.put("price", addon.getPrice());
                        addonMap.put("imageUrl", addon.getImageUrl());
                        return addonMap;
                    }).collect(Collectors.toList());
                    map.put("addons", addonList);
                    response.add(map);
                    logger.debug("Added cart item to response - cartItemId: {}, productId: {}, addons: {}",
                            item.getId(), product.getProductId(), addonList.size());
                }
            }

            logger.info("Successfully retrieved {} cart items for userId: {}", response.size(), userId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error getting cart for userId {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/remove-cart-items")
    public ResponseEntity<String> removeFromCart(@RequestBody Map<String, Object> request) {
        logger.info("Remove from cart request received: {}", request);

        try {
            Long userId = Long.parseLong(request.get("userId").toString());
            Long productId = Long.parseLong(request.get("productId").toString());
            String size = request.getOrDefault("size", "free size").toString();

            logger.debug("Parsed request - userId: {}, productId: {}, size: {}", userId, productId, size);

            Optional<CartItemEntity> existing = cartItemRepository.findByUserIdAndProductIdAndSize(userId, productId, size);
            if (existing.isPresent()) {
                CartItemEntity item = existing.get();
                cartItemRepository.delete(item);
                logger.info("Successfully removed cart item - cartItemId: {}, userId: {}, productId: {}",
                        item.getId(), userId, productId);
                return ResponseEntity.ok("Removed from cart");
            }

            logger.warn("Remove from cart failed: Item not found - userId: {}, productId: {}, size: {}",
                    userId, productId, size);
            return ResponseEntity.badRequest().body("Item not found in cart");

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
            Long productId = Long.parseLong(request.get("productId").toString());
            Integer quantity = Integer.parseInt(request.get("quantity").toString());
            String size = request.getOrDefault("size", "free size").toString();
            List<Long> addonIds = request.get("addonIds") != null ?
                    ((List<?>) request.get("addonIds")).stream()
                            .map(id -> Long.parseLong(id.toString()))
                            .collect(Collectors.toList()) : new ArrayList<>();

            logger.debug("Parsed request - userId: {}, productId: {}, quantity: {}, size: {}, addonIds: {}",
                    userId, productId, quantity, size, addonIds);

            if (quantity <= 0) {
                logger.warn("Update cart failed: Invalid quantity - {}", quantity);
                return ResponseEntity.badRequest().body(Map.of("error", "Quantity must be positive"));
            }

            Optional<CartItemEntity> existing = cartItemRepository.findByUserIdAndProductIdAndSize(userId, productId, size);
            if (existing.isPresent()) {
                CartItemEntity item = existing.get();
                int oldQuantity = item.getQuantity();
                item.setQuantity(quantity);
                // Update add-ons
                Set<Addon> addons = new HashSet<>(addonRepository.findAllById(addonIds));
                item.setAddons(addons);
                cartItemRepository.save(item);

                logger.info("Successfully updated cart item - cartItemId: {}, userId: {}, productId: {}, oldQuantity: {}, newQuantity: {}, addons: {}",
                        item.getId(), userId, productId, oldQuantity, quantity, addons.size());

                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "Cart updated",
                        "productId", productId,
                        "quantity", quantity,
                        "cartItemId", item.getId(),
                        "addonIds", addonIds
                ));
            }

            logger.warn("Update cart failed: Item not found - userId: {}, productId: {}, size: {}",
                    userId, productId, size);
            return ResponseEntity.badRequest().body(Map.of("error", "Item not found in cart"));

        } catch (Exception e) {
            logger.error("Error updating cart item: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid request data"));
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