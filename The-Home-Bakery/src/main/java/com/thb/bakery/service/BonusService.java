package com.thb.bakery.service;

import com.thb.bakery.dto.request.BonusRequest;

public interface BonusService {
    void addBonus(Long staffId, BonusRequest request);
}
