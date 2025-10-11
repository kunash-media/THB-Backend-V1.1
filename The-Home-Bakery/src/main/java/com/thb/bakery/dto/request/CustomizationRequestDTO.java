package com.thb.bakery.dto.request;

import lombok.Data;

public class CustomizationRequestDTO {


    private String cakeFlavour;
    private String cakeSize;
    private String cakeAddOnes;
    private Long userId;
    private boolean isGuest;


    public String getCakeFlavour() {
        return cakeFlavour;
    }

    public void setCakeFlavour(String cakeFlavour) {
        this.cakeFlavour = cakeFlavour;
    }

    public String getCakeSize() {
        return cakeSize;
    }

    public void setCakeSize(String cakeSize) {
        this.cakeSize = cakeSize;
    }

    public String getCakeAddOnes() {
        return cakeAddOnes;
    }

    public void setCakeAddOnes(String cakeAddOnes) {
        this.cakeAddOnes = cakeAddOnes;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setGuest(boolean guest) {
        isGuest = guest;
    }
}