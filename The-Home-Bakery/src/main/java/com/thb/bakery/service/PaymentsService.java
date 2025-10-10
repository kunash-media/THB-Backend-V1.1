package com.thb.bakery.service;

import com.thb.bakery.dto.response.StaffPaymentResponse;

import java.util.List;

public interface PaymentsService {
    List<StaffPaymentResponse> getPayments(Long staffId);
}
