package com.thb.bakery.mapper;

import com.thb.bakery.dto.response.AdvanceDto;
import com.thb.bakery.dto.response.BonusDto;
import com.thb.bakery.dto.response.StaffResponse;
import com.thb.bakery.entity.AdvanceEntity;
import com.thb.bakery.entity.BonusEntity;
import com.thb.bakery.entity.StaffEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component  // Makes it a Spring bean for autowiring
public class StaffMapper {

    // Entity to Response (one-way mapping)
    public StaffResponse entityToResponse(StaffEntity staff) {
        if (staff == null) {
            return null;
        }

        StaffResponse response = new StaffResponse();
        response.setId(staff.getStaffid());
        response.setName(staff.getName());
        response.setEmail(staff.getEmail());
        response.setPhone(staff.getPhone());
        response.setRole(staff.getRole());
        response.setStatus(staff.getStatus());
        response.setSalary(staff.getSalary());
        response.setLastActive(staff.getLastActive());

        // Map advances list
        List<AdvanceDto> advanceDtos = new ArrayList<>();
        for (AdvanceEntity advance : staff.getAdvances()) {
            AdvanceDto dto = new AdvanceDto();
            dto.setMonth(advance.getMonth());
            dto.setAmount(advance.getAmount());
            advanceDtos.add(dto);
        }
        response.setAdvances(advanceDtos);

        // Map bonuses list
        List<BonusDto> bonusDtos = new ArrayList<>();
        for (BonusEntity bonus : staff.getBonuses()) {
            BonusDto dto = new BonusDto();
            dto.setMonth(bonus.getMonth());
            dto.setAmount(bonus.getAmount());
            bonusDtos.add(dto);
        }
        response.setBonuses(bonusDtos);

        return response;
    }

    // For lists: Map a list of entities to a list of responses
    public List<StaffResponse> entitiesToResponses(List<StaffEntity> staffList) {
        if (staffList == null) {
            return new ArrayList<>();
        }
        return staffList.stream()
                .map(this::entityToResponse)
                .collect(java.util.stream.Collectors.toList());
    }
}