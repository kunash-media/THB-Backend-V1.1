
package com.thb.bakery.service;


import com.thb.bakery.dto.request.IncentiveRequest;
import org.springframework.stereotype.Service;

@Service
public interface IncentiveService {
    void addIncentive(Long staffId, IncentiveRequest request);
}