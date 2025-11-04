package com.thb.bakery.controller;

import com.thb.bakery.dto.response.SalarySlipResponseDTO;
import com.thb.bakery.service.SalarySlipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/salary-slip")
public class SalarySlipController {
    @Autowired
    private SalarySlipService salarySlipService;

    @GetMapping("/{staffId}")
    public ResponseEntity<SalarySlipResponseDTO> generate(@PathVariable Long staffId, @RequestParam String month) {
        SalarySlipResponseDTO dto = salarySlipService.generateSalarySlip(staffId, month);
        return dto != null ? ResponseEntity.ok(dto) : ResponseEntity.notFound().build();
    }
}