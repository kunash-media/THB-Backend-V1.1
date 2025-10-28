package com.thb.bakery.service;

import com.thb.bakery.dto.request.AddonDto;

import java.util.List;

public interface AddonService {

    List<AddonDto> getAllAddons();

    AddonDto createAddon(AddonDto addonDto);
}