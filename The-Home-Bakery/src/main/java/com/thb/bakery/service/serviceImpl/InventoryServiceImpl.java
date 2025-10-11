package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.InventoryRequestDto;
import com.thb.bakery.dto.response.InventoryResponseDto;
import com.thb.bakery.entity.InventoryEntity;
import com.thb.bakery.entity.InventoryHistoryEntity;
import com.thb.bakery.repository.InventoryRepository;
import com.thb.bakery.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Autowired
    private InventoryRepository repository;

    // In-memory caches for fast access (refreshed on writes)
    private volatile InventoryResponseDto.DashboardStats cachedStats;
    private volatile List<InventoryResponseDto.AlertDto> cachedAlerts;

    @Override
    public InventoryResponseDto createItem(InventoryRequestDto requestDto) {
        logger.info("Creating new inventory item: {}", requestDto.getName());
        InventoryEntity entity = mapToEntity(requestDto);
        entity.setLastUpdated(LocalDateTime.now());
        entity = repository.save(entity);
        saveHistory(entity, requestDto.getHistory());
        refreshCaches();
        logger.info("Created inventory item with ID: {}", entity.getId());
        return mapToResponse(entity);
    }

    @Override
    public InventoryResponseDto getItemById(Long id) {
        logger.debug("Fetching item by ID: {}", id);
        return repository.findById(id)
                .map(this::mapToResponse)
                .orElse(null);
    }

    @Override
    public List<InventoryResponseDto> getAllItems() {
        logger.debug("Fetching all items");
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InventoryResponseDto updateItem(Long id, InventoryRequestDto requestDto) {

        logger.info("Updating item ID: {}", id);
        InventoryEntity entity = repository.findById(id).orElseThrow();

        entity.setName(requestDto.getName());
        entity.setCategory(requestDto.getCategory());
        entity.setCurrentStock(requestDto.getCurrentStock());
        entity.setMinThreshold(requestDto.getMinThreshold());
        entity.setUnitPrice(requestDto.getUnitPrice());
        entity.setLastUpdated(LocalDateTime.now());
        entity = repository.save(entity);

        // Clear old history and save new
        entity.getHistory().clear();
        saveHistory(entity, requestDto.getHistory());
        refreshCaches();
        logger.info("Updated item ID: {}", id);
        return mapToResponse(entity);
    }

    @Override
    public void deleteItem(Long id) {
        logger.info("Deleting item ID: {}", id);
        repository.deleteById(id);
        refreshCaches();
    }

    @Override
    public List<InventoryResponseDto> bulkUpload(List<InventoryRequestDto> requestDtos) {

        logger.info("Bulk uploading {} items", requestDtos.size());

        List<InventoryEntity> entities = requestDtos.stream()
                .map(this::mapToEntity)
                .peek(e -> e.setLastUpdated(LocalDateTime.now()))
                .collect(Collectors.toList());
        List<InventoryEntity> saved = repository.saveAll(entities);
        // Save histories
        for (int i = 0; i < saved.size(); i++) {
            saveHistory(saved.get(i), requestDtos.get(i).getHistory());
        }
        refreshCaches();
        return saved.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public InventoryResponseDto getDashboard() {
        logger.debug("Generating dashboard data");
        List<InventoryResponseDto> items = getAllItems();
        InventoryResponseDto.DashboardStats stats = getCachedStats();
        List<InventoryResponseDto.AlertDto> alerts = getCachedAlerts();
        InventoryResponseDto.ChartDataDto chartData = generateChartData();
        InventoryResponseDto.SystemConfigDto config = new InventoryResponseDto.SystemConfigDto(
                "INR", 1.0, 2.0, 30000, Arrays.asList("csv", "pdf"), "en-IN"
        );
        InventoryResponseDto response = new InventoryResponseDto();
        response.setDashboardStats(stats);
        response.setAlerts(alerts);
        response.setChartData(chartData);
        response.setSystemConfig(config);
        // Add items to response if needed; here it's separate for optimization
        return response;
    }

    private void saveHistory(InventoryEntity entity, List<InventoryRequestDto.HistoryDto> historyDtos) {
        if (historyDtos != null) {
            List<InventoryHistoryEntity> histories = historyDtos.stream()
                    .map(h -> new InventoryHistoryEntity(entity, h.getDate(), h.getAction(), h.getQuantity(), h.getReason()))
                    .collect(Collectors.toList());
            entity.setHistory(histories);
        }
    }

    private InventoryEntity mapToEntity(InventoryRequestDto dto) {
        return new InventoryEntity(dto.getName(), dto.getCategory(), dto.getCurrentStock(), dto.getMinThreshold(),
                dto.getUnitPrice(), dto.getLastUpdated());
    }

    private InventoryResponseDto mapToResponse(InventoryEntity entity) {

        List<InventoryResponseDto.HistoryDto> historyDtos = entity.getHistory().stream()
                .map(h -> new InventoryResponseDto.HistoryDto(h.getDate(), h.getAction(), h.getQuantity(), h.getReason()))
                .collect(Collectors.toList());
        return new InventoryResponseDto(entity.getId(), entity.getName(), entity.getCategory(), entity.getCurrentStock(),
                entity.getMinThreshold(), entity.getUnitPrice(), entity.getLastUpdated(), historyDtos);
    }

    private void refreshCaches() {
        logger.debug("Refreshing caches");
        cachedStats = new InventoryResponseDto.DashboardStats(
                Math.toIntExact(repository.count()),
                repository.countLowStockItems(),
                BigDecimal.valueOf(repository.getTotalValue()),
                LocalDateTime.now(),
                repository.getAllCategories(),
                Map.of(
                        "low", repository.countByStockLevelLow(5), // Assuming thresholds
                        "medium", repository.countByStockLevelMedium(5, 20),
                        "high", repository.countByStockLevelHigh(20)
                )
        );
        cachedAlerts = generateAlerts();
    }

    private List<InventoryResponseDto.AlertDto> getCachedAlerts() {
        return cachedAlerts != null ? cachedAlerts : generateAlerts();
    }

    private InventoryResponseDto.DashboardStats getCachedStats() {
        return cachedStats != null ? cachedStats : computeStats();
    }

    private InventoryResponseDto.DashboardStats computeStats() {
        // Fallback computation if cache miss
        return new InventoryResponseDto.DashboardStats(
                Math.toIntExact(repository.count()),
                repository.countLowStockItems(),
                BigDecimal.valueOf(repository.getTotalValue()),
                LocalDateTime.now(),
                repository.getAllCategories(),
                Map.of("low", 3, "medium", 2, "high", 2) // Simplified
        );
    }

    private List<InventoryResponseDto.AlertDto> generateAlerts() {
        // Generate based on low stock (simplified)
        return repository.findAll().stream()
                .filter(e -> e.getCurrentStock() < e.getMinThreshold())
                .map(e -> new InventoryResponseDto.AlertDto(
                        System.currentTimeMillis(), e.getId(), e.getName(), "low_stock",
                        "Stock level critical (" + e.getCurrentStock() + " units remaining)",
                        e.getCurrentStock(), e.getMinThreshold(), "high", LocalDateTime.now()
                ))
                .collect(Collectors.toList());
    }

    private InventoryResponseDto.ChartDataDto generateChartData() {
        // Simplified trend data (in prod, query history aggregates)
        return new InventoryResponseDto.ChartDataDto(
                "7",
                Arrays.asList("Jul 11", "Jul 12", "Jul 13", "Jul 14", "Jul 15", "Jul 16", "Jul 17"),
                Arrays.asList(180, 195, 210, 190, 175, 165, 155),
                Arrays.asList(new BigDecimal("15600"), new BigDecimal("16800"), new BigDecimal("17500"),
                        new BigDecimal("18200"), new BigDecimal("19200"), new BigDecimal("18500"), new BigDecimal("18935"))
        );
    }
}