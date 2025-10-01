package com.bakery.The.Home.Bakery.dto.request;


public class CartItemDTO {
    private Long productId;
    private Integer quantity;
    private String selectedWeight;
    private String customMessage;

    // Getters and Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getSelectedWeight() { return selectedWeight; }
    public void setSelectedWeight(String selectedWeight) { this.selectedWeight = selectedWeight; }

    public String getCustomMessage() { return customMessage; }
    public void setCustomMessage(String customMessage) { this.customMessage = customMessage; }
}
