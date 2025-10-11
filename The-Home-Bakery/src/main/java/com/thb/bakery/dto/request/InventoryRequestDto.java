package com.thb.bakery.dto.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class InventoryRequestDto {

    private String name;
    private String category;
    private Integer currentStock;
    private Integer minThreshold;
    private BigDecimal unitPrice;
    private LocalDateTime lastUpdated;
    private List<HistoryDto> history;

    // Constructors
    public InventoryRequestDto() {}

    public InventoryRequestDto(String name, String category, Integer currentStock, Integer minThreshold,
                               BigDecimal unitPrice, LocalDateTime lastUpdated, List<HistoryDto> history) {
        this.name = name;
        this.category = category;
        this.currentStock = currentStock;
        this.minThreshold = minThreshold;
        this.unitPrice = unitPrice;
        this.lastUpdated = lastUpdated;
        this.history = history;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getCurrentStock() { return currentStock; }
    public void setCurrentStock(Integer currentStock) { this.currentStock = currentStock; }

    public Integer getMinThreshold() { return minThreshold; }
    public void setMinThreshold(Integer minThreshold) { this.minThreshold = minThreshold; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public List<HistoryDto> getHistory() { return history; }
    public void setHistory(List<HistoryDto> history) { this.history = history; }

    // Nested HistoryDto
    public static class HistoryDto {
        private LocalDateTime date;
        private String action;
        private Integer quantity;
        private String reason;

        public HistoryDto() {}

        public HistoryDto(LocalDateTime date, String action, Integer quantity, String reason) {
            this.date = date;
            this.action = action;
            this.quantity = quantity;
            this.reason = reason;
        }

        // Getters and Setters
        public LocalDateTime getDate() { return date; }
        public void setDate(LocalDateTime date) { this.date = date; }

        public String getAction() { return action; }
        public void setAction(String action) { this.action = action; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }

        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
}