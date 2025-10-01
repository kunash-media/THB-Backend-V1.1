package com.bakery.The.Home.Bakery.controller;

import com.bakery.The.Home.Bakery.entity.ProductEntity;
import com.bakery.The.Home.Bakery.entity.WishlistItemEntity;
import com.bakery.The.Home.Bakery.repository.ProductRepository;
import com.bakery.The.Home.Bakery.repository.WishlistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import org.springframework.transaction.annotation.Transactional; // Add this import

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired private WishlistItemRepository wishlistItemRepository;
    @Autowired private ProductRepository productRepository;

    @PostMapping("/add-wishlist-items")
    public ResponseEntity<String> addToWishlist(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        if (userId == null) return ResponseEntity.badRequest().body("User ID required");

        Long productId = Long.parseLong(request.get("productId").toString());
        Optional<WishlistItemEntity> existing = wishlistItemRepository.findByUserIdAndProductId(userId, productId);
        if (existing.isPresent()) {
            return ResponseEntity.ok("Already in wishlist");
        }

        WishlistItemEntity item = new WishlistItemEntity();
        item.setUserId(userId);
        item.setProductId(productId);
        wishlistItemRepository.save(item);
        return ResponseEntity.ok("Added to wishlist");
    }

    @GetMapping("/get-wishlist-items")
    public ResponseEntity<List<Map<String, Object>>> getWishlist(@RequestParam Long userId) {
        if (userId == null) return ResponseEntity.badRequest().build();

        List<WishlistItemEntity> items = wishlistItemRepository.findByUserId(userId);
        List<Map<String, Object>> response = new ArrayList<>();
        for (WishlistItemEntity item : items) {
            Optional<ProductEntity> product = productRepository.findById(item.getProductId());
            if (product.isPresent()) {
                Map<String, Object> map = new HashMap<>();
                map.put("productId", product.get().getProductId());
                map.put("title", product.get().getProductName());
                map.put("price", product.get().getProductNewPrice());
                map.put("imageUrl", "/api/products/" + product.get().getProductId() + "/image");
                map.put("subImageUrls", getSubImageUrls(product.get().getProductId(), product.get().getProductSubImages().size()));
                response.add(map);
            }
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/remove-wishlist-items")
    public ResponseEntity<String> removeFromWishlist(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        if (userId == null) return ResponseEntity.badRequest().body("User ID required");

        Long productId = Long.parseLong(request.get("productId").toString());
        Optional<WishlistItemEntity> existing = wishlistItemRepository.findByUserIdAndProductId(userId, productId);
        if (existing.isPresent()) {
            wishlistItemRepository.delete(existing.get());
            return ResponseEntity.ok("Removed from wishlist");
        }
        return ResponseEntity.badRequest().body("Item not found in wishlist");
    }

    @PostMapping("/merge-wishlist-items")
    public ResponseEntity<String> mergeWishlist(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        if (userId == null) return ResponseEntity.badRequest().body("User ID required");

        List<Map<String, Object>> localWishlist = (List<Map<String, Object>>) request.get("items");
        if (localWishlist == null || localWishlist.isEmpty()) return ResponseEntity.ok("No items to merge");

        for (Map<String, Object> localItem : localWishlist) {
            Long productId = Long.parseLong(localItem.get("id").toString());
            Optional<WishlistItemEntity> existing = wishlistItemRepository.findByUserIdAndProductId(userId, productId);
            if (!existing.isPresent()) {
                WishlistItemEntity item = new WishlistItemEntity();
                item.setUserId(userId);
                item.setProductId(productId);
                wishlistItemRepository.save(item);
            }
        }
        return ResponseEntity.ok("Wishlist merged successfully");
    }

    @PostMapping("/clear-wishlist")
    @Transactional // Ensure transaction context for delete operation
    public ResponseEntity<String> clearWishlist(@RequestBody Map<String, Object> request) {
        Long userId = Long.parseLong(request.get("userId").toString());
        if (userId == null) return ResponseEntity.badRequest().body("User ID required");

        wishlistItemRepository.deleteByUserId(userId);
        return ResponseEntity.ok("Wishlist cleared successfully");
    }

    private List<String> getSubImageUrls(Long productId, int size) {
        List<String> urls = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            urls.add("/api/products/" + productId + "/subimage/" + i);
        }
        return urls;
    }
}