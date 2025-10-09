package com.thb.bakery.service.serviceImpl;

import com.thb.bakery.dto.request.POSOrderRequestDTO;
import com.thb.bakery.dto.response.POSOrderResponseDTO;
import com.thb.bakery.entity.POSEntity;
import com.thb.bakery.entity.ProductEntity;
import com.thb.bakery.repository.POSRepository;

import com.thb.bakery.repository.ProductRepository;
import com.thb.bakery.service.POSService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class POSServiceImpl implements POSService {
    private static final Logger logger = LoggerFactory.getLogger(POSServiceImpl.class);

    @Autowired
    private POSRepository posRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public POSOrderResponseDTO createOrder(POSOrderRequestDTO requestDTO) {
        logger.info("Creating new POS order for customer: {}", requestDTO.getCustomerName());

        if (requestDTO.getOrderId() == null || requestDTO.getOrderId().isBlank()) {
            logger.error("orderId cannot be null or blank");
            throw new IllegalArgumentException("orderId cannot be null or blank");
        }

        POSEntity entity = new POSEntity();
        mapRequestToEntity(requestDTO, entity);

        ProductEntity product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> {
                    logger.error("Product not found with ID: {}", requestDTO.getProductId());
                    return new RuntimeException("Product not found");
                });
        entity.setProduct(product);

        POSEntity savedEntity = posRepository.save(entity);
        logger.info("Successfully created order with ID: {}", savedEntity.getOrderId());

        return mapEntityToResponse(savedEntity);
    }

    @Override
    public List<POSOrderResponseDTO> getAllOrders(Pageable pageable) {
        logger.info("Fetching POS orders with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());
        List<POSEntity> entities = posRepository.findAll(pageable).getContent();
        logger.info("Retrieved {} orders", entities.size());

        return entities.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public POSOrderResponseDTO getOrderById(String orderId) {
        logger.info("Fetching POS order with orderId: {}", orderId);
        POSEntity entity = posRepository.findByOrderId(orderId)
                .orElseThrow(() -> {
                    logger.error("Order not found with orderId: {}", orderId);
                    return new RuntimeException("Order not found");
                });
        logger.info("Successfully retrieved order with orderId: {}", orderId);

        return mapEntityToResponse(entity);
    }

    @Override
    public POSOrderResponseDTO updateOrder(Long orderId, POSOrderRequestDTO requestDTO) {
        logger.info("Updating POS order with ID: {}", orderId);
        if (requestDTO.getOrderId() == null || requestDTO.getOrderId().isBlank()) {
            logger.error("orderId cannot be null or blank");
            throw new IllegalArgumentException("orderId cannot be null or blank");
        }

        POSEntity entity = posRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Order not found with ID: {}", orderId);
                    return new RuntimeException("Order not found");
                });

        mapRequestToEntity(requestDTO, entity);

        ProductEntity product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> {
                    logger.error("Product not found with ID: {}", requestDTO.getProductId());
                    return new RuntimeException("Product not found");
                });
        entity.setProduct(product);

        POSEntity updatedEntity = posRepository.save(entity);
        logger.info("Successfully updated order with ID: {}", orderId);

        return mapEntityToResponse(updatedEntity);
    }

    @Override
    public POSOrderResponseDTO patchOrder(Long orderId, Map<String, Object> updates) {
        logger.info("Patching POS order with ID: {} with fields: {}", orderId, updates.keySet());
        POSEntity entity = posRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Order not found with ID: {}", orderId);
                    return new RuntimeException("Order not found");
                });

        updates.forEach((key, value) -> {
            switch (key) {
                case "orderId":
                    if (value == null || ((String) value).isBlank()) {
                        logger.error("orderId cannot be null or blank in patch update");
                        throw new IllegalArgumentException("orderId cannot be null or blank");
                    }
                    entity.setOrderId((String) value);
                    break;
                case "customerName":
                    entity.setCustomerName((String) value);
                    break;
                case "customerMobile":
                    entity.setCustomerMobile((String) value);
                    break;
                case "orderItems":
                    List<Map<String, Object>> items = (List<Map<String, Object>>) value;
                    List<POSEntity.OrderItem> orderItems = items.stream().map(item -> {
                        POSEntity.OrderItem orderItem = new POSEntity.OrderItem();
                        orderItem.setItemId(((Number) item.get("itemId")).longValue());
                        orderItem.setItemName((String) item.get("itemName"));
                        orderItem.setItemQuantity(((Number) item.get("itemQuantity")).intValue());
                        orderItem.setItemPrice(((Number) item.get("itemPrice")).doubleValue());
                        orderItem.setSubTotal(((Number) item.get("subTotal")).doubleValue());
                        return orderItem;
                    }).collect(Collectors.toList());
                    entity.setOrderItems(orderItems);
                    break;
                case "totalQuantity":
                    entity.setTotalQuantity(((Number) value).intValue());
                    break;
                case "totalDiscount":
                    entity.setTotalDiscount((String) value);
                    break;
                case "paymentMode":
                    entity.setPaymentMode((String) value);
                    break;
                case "paymentStatus":
                    entity.setPaymentStatus((String) value);
                    break;
                case "totalAmount":
                    entity.setTotalAmount(((Number) value).doubleValue());
                    break;
                case "receiptId":
                    entity.setReceiptId((String) value);
                    break;
                case "tableNumber":
                    entity.setTableNumber((String) value);
                    break;
                case "productId":
                    ProductEntity product = productRepository.findById(((Number) value).longValue())
                            .orElseThrow(() -> {
                                logger.error("Product not found with ID: {}", value);
                                return new RuntimeException("Product not found");
                            });
                    entity.setProduct(product);
                    break;
            }
        });

        POSEntity updatedEntity = posRepository.save(entity);
        logger.info("Successfully patched order with ID: {}", orderId);

        return mapEntityToResponse(updatedEntity);
    }

    @Override
    public void deleteOrder(Long orderId) {
        logger.info("Deleting POS order with ID: {}", orderId);
        if (!posRepository.existsById(orderId)) {
            logger.error("Order not found with ID: {}", orderId);
            throw new RuntimeException("Order not found");
        }
        posRepository.deleteById(orderId);
        logger.info("Successfully deleted order with ID: {}", orderId);
    }

    @Override
    public List<POSOrderResponseDTO> getOrdersByCustomerName(String customerName) {
        logger.info("Fetching POS orders for customer: {}", customerName);
        List<POSEntity> entities = posRepository.findByCustomerName(customerName);
        logger.info("Retrieved {} orders for customer: {}", entities.size(), customerName);

        return entities.stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    private void mapRequestToEntity(POSOrderRequestDTO requestDTO, POSEntity entity) {
        entity.setOrderId(requestDTO.getOrderId());
        entity.setCustomerName(requestDTO.getCustomerName());
        entity.setCustomerMobile(requestDTO.getCustomerMobile());
        entity.setOrderItems(requestDTO.getOrderItems().stream().map(dtoItem -> {
            POSEntity.OrderItem entityItem = new POSEntity.OrderItem();
            entityItem.setItemId(dtoItem.getItemId());
            entityItem.setItemName(dtoItem.getItemName());
            entityItem.setItemQuantity(dtoItem.getItemQuantity());
            entityItem.setItemPrice(dtoItem.getItemPrice());
            entityItem.setSubTotal(dtoItem.getSubTotal());
            return entityItem;
        }).collect(Collectors.toList()));
        entity.setTotalQuantity(requestDTO.getTotalQuantity());
        entity.setTotalDiscount(requestDTO.getTotalDiscount());
        entity.setPaymentMode(requestDTO.getPaymentMode());
        entity.setPaymentStatus(requestDTO.getPaymentStatus());
        entity.setTotalAmount(requestDTO.getTotalAmount());
        entity.setReceiptId(requestDTO.getReceiptId());
        entity.setTableNumber(requestDTO.getTableNumber());
    }

    private POSOrderResponseDTO mapEntityToResponse(POSEntity entity) {
        POSOrderResponseDTO response = new POSOrderResponseDTO();
        response.setId(entity.getId());
        response.setOrderId(entity.getOrderId());
        response.setCustomerName(entity.getCustomerName());
        response.setCustomerMobile(entity.getCustomerMobile());
        response.setOrderItems(entity.getOrderItems().stream().map(entityItem -> {
            POSOrderResponseDTO.OrderItem dtoItem = new POSOrderResponseDTO.OrderItem();
            dtoItem.setItemId(entityItem.getItemId());
            dtoItem.setItemName(entityItem.getItemName());
            dtoItem.setItemQuantity(entityItem.getItemQuantity());
            dtoItem.setItemPrice(entityItem.getItemPrice());
            dtoItem.setSubTotal(entityItem.getSubTotal());
            return dtoItem;
        }).collect(Collectors.toList()));
        response.setTotalQuantity(entity.getTotalQuantity());
        response.setTotalDiscount(entity.getTotalDiscount());
        response.setPaymentMode(entity.getPaymentMode());
        response.setPaymentStatus(entity.getPaymentStatus());
        response.setTotalAmount(entity.getTotalAmount());
        response.setCreatedAt(entity.getCreatedAt());
        response.setReceiptId(entity.getReceiptId());
        response.setTableNumber(entity.getTableNumber());
        response.setProductId(entity.getProduct().getProductId());
        return response;
    }
}