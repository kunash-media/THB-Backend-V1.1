package com.thb.bakery.entity;

import jakarta.persistence.*;

import com.thb.bakery.entity.UserEntity;


@Entity
@Table(name = "custom_cake_orders")
public class CustomizationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customizationId;

    @Column(name = "cake_flavour", nullable = false)
    private String cakeFlavour;

    @Column(name = "cake_size", nullable = false)
    private String cakeSize;

    @Column(name = "cake_add_ones", columnDefinition = "LONGTEXT")
    private String cakeAddOnes;


    @Column(name = "is_guest", nullable = false)
    private boolean isGuest;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private UserEntity user ;

    public CustomizationEntity() {
    }

    public CustomizationEntity(Long customizationId, String cakeFlavour, String cakeSize, String cakeAddOnes, UserEntity user, boolean isGuest) {
        this.customizationId = customizationId;
        this.cakeFlavour = cakeFlavour;
        this.cakeSize = cakeSize;
        this.cakeAddOnes = cakeAddOnes;
        this.user = user;
        this.isGuest = isGuest;
    }


    public Long getCustomizationId() {
        return customizationId;
    }

    public void setCustomizationId(Long customizationId) {
        this.customizationId = customizationId;
    }

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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public boolean isGuest() {
        return isGuest;
    }

    public void setIsGuest(boolean guest) {
        isGuest = guest;
    }
}