package com.thb.bakery.controller;


import com.thb.bakery.dto.request.ExpenseRequestDto;
import com.thb.bakery.dto.response.ExpenseResponseDto;
import com.thb.bakery.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    // Create a new expense
    @PostMapping("/create-Expense")
    public ResponseEntity<ExpenseResponseDto> createExpense(@RequestBody ExpenseRequestDto requestDto) {
        ExpenseResponseDto response = expenseService.createExpense(requestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Update an existing expense
    @PutMapping("/update-Expense")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@RequestBody ExpenseRequestDto requestDto) {
        ExpenseResponseDto response = expenseService.updateExpense(requestDto);
        if (response.getSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // Delete an expense (soft delete)
    @DeleteMapping("/delete-Expense/{expenseId}")
    public ResponseEntity<ExpenseResponseDto> deleteExpense(@PathVariable Long expenseId) {
        ExpenseResponseDto response = expenseService.deleteExpense(expenseId);
        if (response.getSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // Get expense by ID
    @GetMapping("/get-Expense-By-Id/{expenseId}")
    public ResponseEntity<ExpenseResponseDto> getExpenseById(@PathVariable Long expenseId) {
        ExpenseResponseDto response = expenseService.getExpenseById(expenseId);
        if (response.getSuccess()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // Get all expenses
    @GetMapping("/get-All-Expenses/all")
    public ResponseEntity<ExpenseResponseDto> getAllExpenses() {
        ExpenseResponseDto response = expenseService.getAllExpenses();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Filter and search expenses with pagination
    @PostMapping("/filter-Expenses/filter")
    public ResponseEntity<ExpenseResponseDto> filterExpenses(@RequestBody ExpenseRequestDto requestDto) {
        ExpenseResponseDto response = expenseService.filterExpenses(requestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get bulk expenses for Excel export
    @GetMapping("/get-Bulk-Expenses/bulk/{month}/{year}")
    public ResponseEntity<ExpenseResponseDto> getBulkExpenses(
            @PathVariable Integer month,
            @PathVariable Integer year) {
        ExpenseResponseDto response = expenseService.getBulkExpenses(month, year);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get expenses by category
    @GetMapping("/get-Expenses-By-Category/category/{category}")
    public ResponseEntity<ExpenseResponseDto> getExpensesByCategory(@PathVariable String category) {
        ExpenseResponseDto response = expenseService.getExpensesByCategory(category);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Get expenses by staff name
    @GetMapping("/get-Expenses-By-Staff-Name/staff/{staffName}")
    public ResponseEntity<ExpenseResponseDto> getExpensesByStaffName(@PathVariable String staffName) {
        ExpenseResponseDto response = expenseService.getExpensesByStaffName(staffName);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
