package com.thb.bakery.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class InventoryResponseDto {

    private Long id;
    private String name;
    private String category;
    private Integer currentStock;
    private Integer minThreshold;
    private BigDecimal unitPrice;
    private LocalDateTime lastUpdated;
    private List<HistoryDto> history;

    // Dashboard Stats
    private DashboardStats dashboardStats;
    private List<AlertDto> alerts;
    private ChartDataDto chartData;
    private SystemConfigDto systemConfig;

    // Constructors
    public InventoryResponseDto() {}

    // Overloaded for single item
    public InventoryResponseDto(Long id, String name, String category, Integer currentStock, Integer minThreshold,
                                BigDecimal unitPrice, LocalDateTime lastUpdated, List<HistoryDto> history) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.currentStock = currentStock;
        this.minThreshold = minThreshold;
        this.unitPrice = unitPrice;
        this.lastUpdated = lastUpdated;
        this.history = history;
    }

    // Overloaded for full dashboard
    public InventoryResponseDto(List<InventoryResponseDto> items, DashboardStats dashboardStats, List<AlertDto> alerts,
                                ChartDataDto chartData, SystemConfigDto systemConfig) {
        // Simplified; in service, populate items list
        this.dashboardStats = dashboardStats;
        this.alerts = alerts;
        this.chartData = chartData;
        this.systemConfig = systemConfig;
    }

    // Getters and Setters (for item fields)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    // Dashboard Getters/Setters
    public DashboardStats getDashboardStats() { return dashboardStats; }
    public void setDashboardStats(DashboardStats dashboardStats) { this.dashboardStats = dashboardStats; }

    public List<AlertDto> getAlerts() { return alerts; }
    public void setAlerts(List<AlertDto> alerts) { this.alerts = alerts; }

    public ChartDataDto getChartData() { return chartData; }
    public void setChartData(ChartDataDto chartData) { this.chartData = chartData; }

    public SystemConfigDto getSystemConfig() { return systemConfig; }
    public void setSystemConfig(SystemConfigDto systemConfig) { this.systemConfig = systemConfig; }


    // Nested DTOs (matching payload)
    public static class HistoryDto {
        private LocalDateTime date;
        private String action;
        private Integer quantity;
        private String reason;

        // Constructors, Getters, Setters (same as in RequestDto)
        public HistoryDto() {}
        public HistoryDto(LocalDateTime date, String action, Integer quantity, String reason) {
            this.date = date; this.action = action; this.quantity = quantity; this.reason = reason;
        }
        public LocalDateTime getDate() { return date; } public void setDate(LocalDateTime date) { this.date = date; }
        public String getAction() { return action; } public void setAction(String action) { this.action = action; }
        public Integer getQuantity() { return quantity; } public void setQuantity(Integer quantity) { this.quantity = quantity; }
        public String getReason() { return reason; } public void setReason(String reason) { this.reason = reason; }
    }

    public static class DashboardStats {
        private Integer totalProducts;
        private Integer lowStockItems;
        private BigDecimal totalValue;
        private LocalDateTime lastUpdated;
        private List<String> categories;
        private Map<String, Integer> stockLevels;

        // Constructors, Getters, Setters
        public DashboardStats() {}
        public DashboardStats(Integer totalProducts, Integer lowStockItems, BigDecimal totalValue, LocalDateTime lastUpdated,
                              List<String> categories, Map<String, Integer> stockLevels) {
            this.totalProducts = totalProducts; this.lowStockItems = lowStockItems; this.totalValue = totalValue;
            this.lastUpdated = lastUpdated; this.categories = categories; this.stockLevels = stockLevels;
        }
        public Integer getTotalProducts() { return totalProducts; } public void setTotalProducts(Integer totalProducts) { this.totalProducts = totalProducts; }
        public Integer getLowStockItems() { return lowStockItems; } public void setLowStockItems(Integer lowStockItems) { this.lowStockItems = lowStockItems; }
        public BigDecimal getTotalValue() { return totalValue; } public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }
        public LocalDateTime getLastUpdated() { return lastUpdated; } public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
        public List<String> getCategories() { return categories; } public void setCategories(List<String> categories) { this.categories = categories; }
        public Map<String, Integer> getStockLevels() { return stockLevels; } public void setStockLevels(Map<String, Integer> stockLevels) { this.stockLevels = stockLevels; }
    }

    public static class AlertDto {
        private Long id;
        private Long productId;
        private String productName;
        private String type;
        private String message;
        private Integer currentStock;
        private Integer minThreshold;
        private String priority;
        private LocalDateTime timestamp;

        // Constructors, Getters, Setters
        public AlertDto() {}
        public AlertDto(Long id, Long productId, String productName, String type, String message, Integer currentStock,
                        Integer minThreshold, String priority, LocalDateTime timestamp) {
            this.id = id; this.productId = productId; this.productName = productName; this.type = type;
            this.message = message; this.currentStock = currentStock; this.minThreshold = minThreshold;
            this.priority = priority; this.timestamp = timestamp;
        }
        public Long getId() { return id; } public void setId(Long id) { this.id = id; }
        public Long getProductId() { return productId; } public void setProductId(Long productId) { this.productId = productId; }
        public String getProductName() { return productName; } public void setProductName(String productName) { this.productName = productName; }
        public String getType() { return type; } public void setType(String type) { this.type = type; }
        public String getMessage() { return message; } public void setMessage(String message) { this.message = message; }
        public Integer getCurrentStock() { return currentStock; } public void setCurrentStock(Integer currentStock) { this.currentStock = currentStock; }
        public Integer getMinThreshold() { return minThreshold; } public void setMinThreshold(Integer minThreshold) { this.minThreshold = minThreshold; }
        public String getPriority() { return priority; } public void setPriority(String priority) { this.priority = priority; }
        public LocalDateTime getTimestamp() { return timestamp; } public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    }

    public static class ChartDataDto {
        private String period;
        private List<String> labels;
        private List<Integer> stockQuantities;
        private List<BigDecimal> inventoryValues;

        // Constructors, Getters, Setters
        public ChartDataDto() {}
        public ChartDataDto(String period, List<String> labels, List<Integer> stockQuantities, List<BigDecimal> inventoryValues) {
            this.period = period; this.labels = labels; this.stockQuantities = stockQuantities; this.inventoryValues = inventoryValues;
        }
        public String getPeriod() { return period; } public void setPeriod(String period) { this.period = period; }
        public List<String> getLabels() { return labels; } public void setLabels(List<String> labels) { this.labels = labels; }
        public List<Integer> getStockQuantities() { return stockQuantities; } public void setStockQuantities(List<Integer> stockQuantities) { this.stockQuantities = stockQuantities; }
        public List<BigDecimal> getInventoryValues() { return inventoryValues; } public void setInventoryValues(List<BigDecimal> inventoryValues) { this.inventoryValues = inventoryValues; }
    }

    public static class SystemConfigDto {
        private String currency;
        private Double lowStockThreshold;
        private Double mediumStockThreshold;
        private Integer autoRefreshInterval;
        private List<String> exportFormats;
        private String dateFormat;

        // Constructors, Getters, Setters
        public SystemConfigDto() {}
        public SystemConfigDto(String currency, Double lowStockThreshold, Double mediumStockThreshold, Integer autoRefreshInterval,
                               List<String> exportFormats, String dateFormat) {
            this.currency = currency; this.lowStockThreshold = lowStockThreshold; this.mediumStockThreshold = mediumStockThreshold;
            this.autoRefreshInterval = autoRefreshInterval; this.exportFormats = exportFormats; this.dateFormat = dateFormat;
        }
        public String getCurrency() { return currency; } public void setCurrency(String currency) { this.currency = currency; }
        public Double getLowStockThreshold() { return lowStockThreshold; } public void setLowStockThreshold(Double lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }
        public Double getMediumStockThreshold() { return mediumStockThreshold; } public void setMediumStockThreshold(Double mediumStockThreshold) { this.mediumStockThreshold = mediumStockThreshold; }
        public Integer getAutoRefreshInterval() { return autoRefreshInterval; } public void setAutoRefreshInterval(Integer autoRefreshInterval) { this.autoRefreshInterval = autoRefreshInterval; }
        public List<String> getExportFormats() { return exportFormats; } public void setExportFormats(List<String> exportFormats) { this.exportFormats = exportFormats; }
        public String getDateFormat() { return dateFormat; } public void setDateFormat(String dateFormat) { this.dateFormat = dateFormat; }
    }
}