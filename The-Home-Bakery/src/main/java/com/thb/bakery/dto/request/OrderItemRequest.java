package com.thb.bakery.dto.request;

public class OrderItemRequest {
    private Long productId;
    private Integer quantity;
    private String selectedWeight;
    private String cakeMessage;
    private String specialInstructions;

    // Constructors
    public OrderItemRequest() {}

    // Getters and Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getSelectedWeight() { return selectedWeight; }
    public void setSelectedWeight(String selectedWeight) { this.selectedWeight = selectedWeight; }

    public String getCakeMessage() { return cakeMessage; }
    public void setCakeMessage(String cakeMessage) { this.cakeMessage = cakeMessage; }

    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }
}