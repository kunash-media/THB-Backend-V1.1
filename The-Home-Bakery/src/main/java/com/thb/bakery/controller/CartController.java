package com.thb.bakery.controller;


import com.thb.bakery.entity.CartItemEntity;
import com.thb.bakery.entity.ProductEntity;
import com.thb.bakery.repository.CartItemRepository;
import com.thb.bakery.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;


@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ProductRepository productRepository;

    @PostMapping("/add-cart-items")
    public ResponseEntity<Map<String, Object>> addToCart(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        if (userId == null) return ResponseEntity.badRequest().body(Map.of("error", "User ID required"));

        Long productId = Long.parseLong(request.get("productId").toString());
        Integer quantity = Integer.parseInt(request.getOrDefault("quantity", 1).toString());
        if (quantity < 1) return ResponseEntity.badRequest().body(Map.of("error", "Quantity must be positive"));
        String size = request.getOrDefault("size", "free size").toString();

        Optional<CartItemEntity> existing = cartItemRepository.findByUserIdAndProductIdAndSize(userId, productId, size);
        CartItemEntity item;
        if (existing.isPresent()) {
            item = existing.get();
            item.setQuantity(quantity);
        } else {
            item = new CartItemEntity();
            item.setUserId(userId);
            item.setProductId(productId);
            item.setQuantity(quantity);
            item.setSize(size);
        }
        cartItemRepository.save(item);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Added to cart",
                "productId", productId,
                "quantity", quantity
        ));
    }

    @GetMapping("/get-cart-items")
    public ResponseEntity<List<Map<String, Object>>> getCart(@RequestParam Long userId) {
        if (userId == null) return ResponseEntity.badRequest().build();

        List<CartItemEntity> items = cartItemRepository.findByUserId(userId);
        List<Map<String, Object>> response = new ArrayList<>();
        for (CartItemEntity item : items) {
            Optional<ProductEntity> product = productRepository.findById(item.getProductId());
            if (product.isPresent()) {
                Map<String, Object> map = new HashMap<>();
                map.put("productId", product.get().getProductId());
                map.put("title", product.get().getProductName());
                map.put("price", product.get().getProductNewPrice());
                map.put("imageUrl", "/api/products/" + product.get().getProductId() + "/image");
                map.put("subImageUrls", getSubImageUrls(product.get().getProductId(), product.get().getProductSubImages().size()));
                map.put("quantity", item.getQuantity());
                map.put("size", item.getSize());
                response.add(map);
            }
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove-cart-items")
    public ResponseEntity<String> removeFromCart(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        if (userId == null) return ResponseEntity.badRequest().body("User ID required");

        Long productId = Long.parseLong(request.get("productId").toString());
        String size = request.getOrDefault("size", "free size").toString();

        Optional<CartItemEntity> existing = cartItemRepository.findByUserIdAndProductIdAndSize(userId, productId, size);
        if (existing.isPresent()) {
            cartItemRepository.delete(existing.get());
            return ResponseEntity.ok("Removed from cart");
        }
        return ResponseEntity.badRequest().body("Item not found in cart");
    }

    @PostMapping("/update-cart-items")
    public ResponseEntity<Map<String, Object>> updateCartItem(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        if (userId == null) return ResponseEntity.badRequest().body(Map.of("error", "User ID required"));

        Long productId = Long.parseLong(request.get("productId").toString());
        Integer quantity = Integer.parseInt(request.get("quantity").toString());
        String size = request.getOrDefault("size", "free size").toString();
        if (quantity <= 0) return ResponseEntity.badRequest().body(Map.of("error", "Quantity must be positive"));

        Optional<CartItemEntity> existing = cartItemRepository.findByUserIdAndProductIdAndSize(userId, productId, size);
        if (existing.isPresent()) {
            existing.get().setQuantity(quantity);
            cartItemRepository.save(existing.get());
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Cart updated",
                    "productId", productId,
                    "quantity", quantity
            ));
        }
        return ResponseEntity.badRequest().body(Map.of("error", "Item not found in cart"));
    }

    @PostMapping("/merge-cart-items")
    public ResponseEntity<String> mergeCart(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        if (userId == null) return ResponseEntity.badRequest().body("User ID required");

        List<Map<String, Object>> localCart = (List<Map<String, Object>>) request.get("items");
        if (localCart == null || localCart.isEmpty()) return ResponseEntity.ok("No items to merge");

        for (Map<String, Object> localItem : localCart) {
            Long productId = Long.parseLong(localItem.get("id").toString());
            Integer quantity = Integer.parseInt(localItem.getOrDefault("quantity", 1).toString());
            String size = localItem.getOrDefault("size", "free size").toString();

            Optional<CartItemEntity> existing = cartItemRepository.findByUserIdAndProductIdAndSize(userId, productId, size);
            if (existing.isPresent()) {
                existing.get().setQuantity(quantity);
                cartItemRepository.save(existing.get());
            } else {
                CartItemEntity item = new CartItemEntity();
                item.setUserId(userId);
                item.setProductId(productId);
                item.setQuantity(quantity);
                item.setSize(size);
                cartItemRepository.save(item);
            }
        }
        return ResponseEntity.ok("Cart merged successfully");
    }

    @PostMapping("/clear-cart")
    @Transactional // Add this annotation to ensure transaction context
    public ResponseEntity<String> clearCart(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        if (userId == null) return ResponseEntity.badRequest().body("User ID required");

        cartItemRepository.deleteByUserId(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }

    private List<String> getSubImageUrls(Long productId, int size) {
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            urls.add("/api/products/" + productId + "/subimage/" + i);
        }
        return urls;
    }
}