package com.thb.bakery.service;

import com.thb.bakery.dto.request.ShippingAddressDTO;

import java.util.List;

public interface ShippingAddressService {

    ShippingAddressDTO createAddress(Long userId, ShippingAddressDTO addressDTO);

    List<ShippingAddressDTO> getAddressesByUserId(Long userId);

    List<ShippingAddressDTO> getAllAddresses();

    ShippingAddressDTO updateAddress(Long userId, Long shippingId, ShippingAddressDTO addressDTO);

    void deleteAddress(Long userId, Long shippingId);
}