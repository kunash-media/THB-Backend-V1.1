package com.thb.bakery.service;

import com.thb.bakery.dto.request.SalarySlipRequest;
import com.thb.bakery.dto.response.SalarySlipResponse;
import com.thb.bakery.dto.response.SalarySlipResponseDTO;

import java.time.LocalDate;

public interface SalarySlipService {

    SalarySlipResponse generateSalarySlip(Long staffId, LocalDate month);

    SalarySlipResponse generateSlip(Long staffId, SalarySlipRequest request);

    SalarySlipResponseDTO generateSalarySlip(Long staffId, String month);

}
