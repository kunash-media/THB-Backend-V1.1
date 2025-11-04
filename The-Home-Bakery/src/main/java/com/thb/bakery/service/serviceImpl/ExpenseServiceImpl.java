package com.thb.bakery.service.serviceImpl;


import com.thb.bakery.dto.request.ExpenseRequestDto;
import com.thb.bakery.dto.response.ExpenseResponseDto;
import com.thb.bakery.entity.ExpenseEntity;
import com.thb.bakery.repository.ExpenseRepository;
import com.thb.bakery.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public ExpenseResponseDto createExpense(ExpenseRequestDto requestDto) {
        ExpenseEntity entity = new ExpenseEntity();
        entity.setStaffName(requestDto.getStaffName());
        entity.setAmount(requestDto.getAmount());
        entity.setNote(requestDto.getNote());
        entity.setDate(requestDto.getDate());
        entity.setStatus("ACTIVE");
        entity.setCategory(requestDto.getCategory());

        ExpenseEntity savedEntity = expenseRepository.save(entity);

        ExpenseResponseDto response = new ExpenseResponseDto();
        response.setSuccess(true);
        response.setMessage("Expense created successfully");
        response.setAction("CREATE_EXPENSE");
        response.setExpenseData(convertToDto(savedEntity));
        response.setMetadata(createMetadata("WEB_ADMIN"));

        return response;
    }

    @Override
    public ExpenseResponseDto updateExpense(ExpenseRequestDto requestDto) {
        Optional<ExpenseEntity> optionalEntity = expenseRepository.findById(requestDto.getExpenseId());

        if (!optionalEntity.isPresent()) {
            ExpenseResponseDto response = new ExpenseResponseDto();
            response.setSuccess(false);
            response.setMessage("Expense not found with ID: " + requestDto.getExpenseId());
            return response;
        }

        ExpenseEntity entity = optionalEntity.get();
        entity.setStaffName(requestDto.getStaffName());
        entity.setAmount(requestDto.getAmount());
        entity.setNote(requestDto.getNote());
        entity.setDate(requestDto.getDate());
        entity.setStatus("UPDATED");
        entity.setCategory(requestDto.getCategory());

        ExpenseEntity updatedEntity = expenseRepository.save(entity);

        ExpenseResponseDto response = new ExpenseResponseDto();
        response.setSuccess(true);
        response.setMessage("Expense updated successfully");
        response.setAction("UPDATE_EXPENSE");
        response.setExpenseData(convertToDto(updatedEntity));
        response.setMetadata(createMetadata("WEB_ADMIN"));

        return response;
    }

    @Override
    public ExpenseResponseDto deleteExpense(Long expenseId) {
        Optional<ExpenseEntity> optionalEntity = expenseRepository.findById(expenseId);

        if (!optionalEntity.isPresent()) {
            ExpenseResponseDto response = new ExpenseResponseDto();
            response.setSuccess(false);
            response.setMessage("Expense not found with ID: " + expenseId);
            return response;
        }

        ExpenseEntity entity = optionalEntity.get();
        entity.setStatus("DELETED");
        expenseRepository.save(entity);

        ExpenseResponseDto response = new ExpenseResponseDto();
        response.setSuccess(true);
        response.setMessage("Expense deleted successfully");
        response.setAction("DELETE_EXPENSE");

        ExpenseResponseDto.ExpenseDataDto dataDto = new ExpenseResponseDto.ExpenseDataDto();
        dataDto.setExpenseId(expenseId);
        dataDto.setStatus("DELETED");
        response.setExpenseData(dataDto);
        response.setMetadata(createMetadata("WEB_ADMIN"));

        return response;
    }

    @Override
    public ExpenseResponseDto getExpenseById(Long expenseId) {
        Optional<ExpenseEntity> optionalEntity = expenseRepository.findById(expenseId);

        if (!optionalEntity.isPresent()) {
            ExpenseResponseDto response = new ExpenseResponseDto();
            response.setSuccess(false);
            response.setMessage("Expense not found with ID: " + expenseId);
            return response;
        }

        ExpenseResponseDto response = new ExpenseResponseDto();
        response.setSuccess(true);
        response.setMessage("Expense retrieved successfully");
        response.setExpenseData(convertToDto(optionalEntity.get()));
        response.setMetadata(createMetadata("WEB_ADMIN"));

        return response;
    }

    @Override
    public ExpenseResponseDto getAllExpenses() {
        List<ExpenseEntity> entities = expenseRepository.findByStatusNot("DELETED");

        ExpenseResponseDto response = new ExpenseResponseDto();
        response.setSuccess(true);
        response.setMessage("All expenses retrieved successfully");
        response.setExpenseList(entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));

        ExpenseResponseDto.MetadataDto metadata = createMetadata("WEB_ADMIN");
        metadata.setTotalRecords(entities.size());
        response.setMetadata(metadata);

        return response;
    }

    @Override
    public ExpenseResponseDto filterExpenses(ExpenseRequestDto requestDto) {
        ExpenseRequestDto.FiltersDto filters = requestDto.getFilters();
        ExpenseRequestDto.PaginationDto pagination = requestDto.getPagination();

        if (filters == null) {
            filters = new ExpenseRequestDto.FiltersDto();
        }

        if (pagination == null) {
            pagination = new ExpenseRequestDto.PaginationDto();
            pagination.setPage(0);
            pagination.setSize(10);
            pagination.setSortBy("date");
            pagination.setSortDirection("DESC");
        }

        Sort sort = pagination.getSortDirection().equalsIgnoreCase("ASC")
                ? Sort.by(pagination.getSortBy()).ascending()
                : Sort.by(pagination.getSortBy()).descending();

        Pageable pageable = PageRequest.of(pagination.getPage(), pagination.getSize(), sort);

        Page<ExpenseEntity> page = expenseRepository.searchExpenses(
                filters.getSearchTerm(),
                filters.getCategory(),
                filters.getStaffName(),
                filters.getDateFilter(),
                pageable
        );

        ExpenseResponseDto response = new ExpenseResponseDto();
        response.setSuccess(true);
        response.setMessage("Expenses filtered successfully");
        response.setAction("FILTER_EXPENSES");
        response.setExpenseList(page.getContent().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));

        ExpenseResponseDto.PaginationInfoDto paginationInfo = new ExpenseResponseDto.PaginationInfoDto();
        paginationInfo.setCurrentPage(page.getNumber());
        paginationInfo.setPageSize(page.getSize());
        paginationInfo.setTotalElements(page.getTotalElements());
        paginationInfo.setTotalPages(page.getTotalPages());
        response.setPaginationInfo(paginationInfo);

        response.setMetadata(createMetadata("WEB_ADMIN"));

        return response;
    }

    @Override
    public ExpenseResponseDto getBulkExpenses(Integer month, Integer year) {
        List<ExpenseEntity> entities = expenseRepository.findByMonthAndYear(month, year);

        Double totalAmount = entities.stream()
                .mapToDouble(ExpenseEntity::getAmount)
                .sum();

        ExpenseResponseDto response = new ExpenseResponseDto();
        response.setSuccess(true);
        response.setMessage("Bulk expenses retrieved successfully");
        response.setAction("BULK_EXPORT");
        response.setExpenseList(entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));

        ExpenseResponseDto.MetadataDto metadata = createMetadata("EXCEL_EXPORT");
        metadata.setMonth(getMonthName(month));
        metadata.setYear(year);
        metadata.setTotalAmount(totalAmount);
        metadata.setTotalRecords(entities.size());
        response.setMetadata(metadata);

        return response;
    }

    @Override
    public ExpenseResponseDto getExpensesByCategory(String category) {
        List<ExpenseEntity> entities = expenseRepository.findByCategory(category);

        ExpenseResponseDto response = new ExpenseResponseDto();
        response.setSuccess(true);
        response.setMessage("Expenses retrieved by category");
        response.setExpenseList(entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));

        ExpenseResponseDto.MetadataDto metadata = createMetadata("WEB_ADMIN");
        metadata.setTotalRecords(entities.size());
        response.setMetadata(metadata);

        return response;
    }

    @Override
    public ExpenseResponseDto getExpensesByStaffName(String staffName) {
        List<ExpenseEntity> entities = expenseRepository.findByStaffNameContainingIgnoreCase(staffName);

        ExpenseResponseDto response = new ExpenseResponseDto();
        response.setSuccess(true);
        response.setMessage("Expenses retrieved by staff name");
        response.setExpenseList(entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));

        ExpenseResponseDto.MetadataDto metadata = createMetadata("WEB_ADMIN");
        metadata.setTotalRecords(entities.size());
        response.setMetadata(metadata);

        return response;
    }

    // Helper method to convert Entity to DTO
    private ExpenseResponseDto.ExpenseDataDto convertToDto(ExpenseEntity entity) {
        ExpenseResponseDto.ExpenseDataDto dto = new ExpenseResponseDto.ExpenseDataDto();
        dto.setExpenseId(entity.getExpenseId());
        dto.setStaffName(entity.getStaffName());
        dto.setAmount(entity.getAmount());
        dto.setNote(entity.getNote());
        dto.setDate(entity.getDate());
        dto.setStatus(entity.getStatus());
        dto.setCategory(entity.getCategory());
        return dto;
    }

    // Helper method to create metadata
    private ExpenseResponseDto.MetadataDto createMetadata(String source) {
        ExpenseResponseDto.MetadataDto metadata = new ExpenseResponseDto.MetadataDto();
        metadata.setTimestamp(LocalDateTime.now());
        metadata.setVersion("1.0");
        metadata.setSource(source);
        return metadata;
    }

    // Helper method to get month name
    private String getMonthName(int month) {
        return Month.of(month).name();
    }
}