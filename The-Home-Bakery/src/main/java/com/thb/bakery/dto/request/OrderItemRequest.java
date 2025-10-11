package com.thb.bakery.dto.request;

import java.math.BigDecimal;
import java.util.List;

public class OrderItemRequest {
    private Long productId;
    private Integer quantity;
    private String selectedWeight;
    private String cakeMessage;
    private String specialInstructions;

    //party items
    private List<PartyItems> partyItems;

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


    public List<PartyItems> getPartyItems() {
        return partyItems;
    }

    public void setPartyItems(List<PartyItems> partyItems) {
        this.partyItems = partyItems;
    }

    public static class PartyItems {

        private Long itemId;
        private String partItemName;
        private Integer partyItemQuantity;
        private BigDecimal partyItemPrice;

        private BigDecimal partyItemSubTotal;


        public Long getItemId() {
            return itemId;
        }

        public void setItemId(Long itemId) {
            this.itemId = itemId;
        }

        public String getPartItemName() {
            return partItemName;
        }

        public void setPartItemName(String partItemName) {
            this.partItemName = partItemName;
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

        public BigDecimal getPartyItemSubTotal() {
            return partyItemSubTotal;
        }

        public void setPartyItemSubTotal(BigDecimal partyItemSubTotal) {
            this.partyItemSubTotal = partyItemSubTotal;
        }
    }
}