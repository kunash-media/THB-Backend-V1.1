package com.thb.bakery.service;


import com.thb.bakery.dto.request.ExpenseRequestDto;
import com.thb.bakery.dto.response.ExpenseResponseDto;

public interface ExpenseService {

    // Create a new expense
    ExpenseResponseDto createExpense(ExpenseRequestDto requestDto);

    // Update an existing expense
    ExpenseResponseDto updateExpense(ExpenseRequestDto requestDto);

    // Delete an expense (soft delete)
    ExpenseResponseDto deleteExpense(Long expenseId);

    // Get expense by ID
    ExpenseResponseDto getExpenseById(Long expenseId);

    // Get all expenses
    ExpenseResponseDto getAllExpenses();

    // Filter and search expenses with pagination
    ExpenseResponseDto filterExpenses(ExpenseRequestDto requestDto);

    // Get expenses for bulk export (Excel download)
    ExpenseResponseDto getBulkExpenses(Integer month, Integer year);

    // Get expenses by category
    ExpenseResponseDto getExpensesByCategory(String category);

    // Get expenses by staff name
    ExpenseResponseDto getExpensesByStaffName(String staffName);
}
