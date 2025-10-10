package com.thb.bakery.service;

import com.thb.bakery.dto.request.AdvanceRequest;

public interface AdvanceService {

    void addAdvance(Long staffId, AdvanceRequest request);
}
