package com.thb.bakery.service;

import com.thb.bakery.dto.request.SalarySlipRequest;
import com.thb.bakery.dto.response.SalarySlipResponse;

public interface SalarySlipService {

    SalarySlipResponse generateSlip(Long staffId, SalarySlipRequest request);

}
