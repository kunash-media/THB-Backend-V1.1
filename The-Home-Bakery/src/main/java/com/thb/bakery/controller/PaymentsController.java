package com.thb.bakery.controller;

import com.thb.bakery.dto.response.StaffPaymentResponse;
import com.thb.bakery.service.PaymentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentsController.class);

    @Autowired
    private PaymentsService paymentsService;

    //Get payments for all or specific staff
    @GetMapping("/get-payments")
    public ResponseEntity<List<StaffPaymentResponse>> getPayments(@RequestParam(required = false) Long staffId){
        logger.info("Received request to get payments. StaffId: {}", staffId);

        try {
            List<StaffPaymentResponse> response = paymentsService.getPayments(staffId);
            logger.info("Successfully retrieved {} payment record(s) for staffId: {}", response.size(), staffId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error occurred while retrieving payments for staffId: {}", staffId, e);
            throw e;
        }
    }
}
