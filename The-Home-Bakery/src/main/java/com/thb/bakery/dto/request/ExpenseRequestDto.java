package com.thb.bakery.dto.request;

import java.time.LocalDateTime;

public class ExpenseRequestDto {

    private Long expenseId;
    private String staffName;
    private Double amount;
    private String note;
    private LocalDateTime date;
    private String status;
    private String category;
    private String action;
    private MetadataDto metadata;
    private FiltersDto filters;
    private PaginationDto pagination;

    // Constructors
    public ExpenseRequestDto() {
    }

    // Getters and Setters
    public Long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Long expenseId) {
        this.expenseId = expenseId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public MetadataDto getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataDto metadata) {
        this.metadata = metadata;
    }

    public FiltersDto getFilters() {
        return filters;
    }

    public void setFilters(FiltersDto filters) {
        this.filters = filters;
    }

    public PaginationDto getPagination() {
        return pagination;
    }

    public void setPagination(PaginationDto pagination) {
        this.pagination = pagination;
    }

    // Nested DTOs
    public static class MetadataDto {
        private LocalDateTime timestamp;
        private String version;
        private String source;
        private String month;
        private Integer year;
        private Double totalAmount;

        public MetadataDto() {
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public Double getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(Double totalAmount) {
            this.totalAmount = totalAmount;
        }
    }

    public static class FiltersDto {
        private String searchTerm;
        private String dateFilter;
        private String category;
        private String staffName;

        public FiltersDto() {
        }

        public String getSearchTerm() {
            return searchTerm;
        }

        public void setSearchTerm(String searchTerm) {
            this.searchTerm = searchTerm;
        }

        public String getDateFilter() {
            return dateFilter;
        }

        public void setDateFilter(String dateFilter) {
            this.dateFilter = dateFilter;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getStaffName() {
            return staffName;
        }

        public void setStaffName(String staffName) {
            this.staffName = staffName;
        }
    }

    public static class PaginationDto {
        private Integer page;
        private Integer size;
        private String sortBy;
        private String sortDirection;

        public PaginationDto() {
        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public String getSortBy() {
            return sortBy;
        }

        public void setSortBy(String sortBy) {
            this.sortBy = sortBy;
        }

        public String getSortDirection() {
            return sortDirection;
        }

        public void setSortDirection(String sortDirection) {
            this.sortDirection = sortDirection;
        }
    }
}
