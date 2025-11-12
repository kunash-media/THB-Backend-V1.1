package com.thb.bakery.dto.response;

import java.math.BigDecimal;
import java.util.List;

public class OrderItemResponse {

    private Long productId;
    private Long snackId;
    private String productName;
    private String productCategory;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
    private String selectedWeight;
    private String cakeMessage;
    private String specialInstructions;
    private String productImage;
    private List<PartyItem> partyItems;

    // Constructors
    public OrderItemResponse() {}

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getSnackId() {
        return snackId;
    }

    public void setSnackId(Long snackId) {
        this.snackId = snackId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getSelectedWeight() {
        return selectedWeight;
    }

    public void setSelectedWeight(String selectedWeight) {
        this.selectedWeight = selectedWeight;
    }

    public String getCakeMessage() {
        return cakeMessage;
    }

    public void setCakeMessage(String cakeMessage) {
        this.cakeMessage = cakeMessage;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public List<PartyItem> getPartyItems() {
        return partyItems;
    }

    public void setPartyItems(List<PartyItem> partyItems) {
        this.partyItems = partyItems;
    }

    public static class PartyItem {

        private Long partyItemId;
        private String partyItemName;
        private Integer partyItemQuantity;
        private BigDecimal partyItemPrice;
        private BigDecimal partyItemSubtotal;

        // Getters and Setters


        public Long getPartyItemId() {
            return partyItemId;
        }

        public void setPartyItemId(Long partyItemId) {
            this.partyItemId = partyItemId;
        }

        public String getPartyItemName() {
            return partyItemName;
        }

        public void setPartyItemName(String partyItemName) {
            this.partyItemName = partyItemName;
        }

        public Integer getPartyItemQuantity() {
            return partyItemQuantity;
        }

        public void setPartyItemQuantity(Integer partyItemQuantity) {
            this.partyItemQuantity = partyItemQuantity;
        }

        public BigDecimal getPartyItemPrice() {
            return partyItemPrice;
        }

        public void setPartyItemPrice(BigDecimal partyItemPrice) {
            this.partyItemPrice = partyItemPrice;
        }

        public BigDecimal getPartyItemSubtotal() {
            return partyItemSubtotal;
        }

        public void setPartyItemSubtotal(BigDecimal partyItemSubtotal) {
            this.partyItemSubtotal = partyItemSubtotal;
        }
    }
}