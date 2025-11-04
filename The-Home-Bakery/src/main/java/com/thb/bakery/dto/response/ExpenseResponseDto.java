package com.thb.bakery.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class ExpenseResponseDto {

    private ExpenseDataDto expenseData;
    private List<ExpenseDataDto> expenseList;
    private MetadataDto metadata;
    private String action;
    private String message;
    private Boolean success;
    private PaginationInfoDto paginationInfo;

    // Constructors
    public ExpenseResponseDto() {
    }

    public ExpenseResponseDto(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and Setters
    public ExpenseDataDto getExpenseData() {
        return expenseData;
    }

    public void setExpenseData(ExpenseDataDto expenseData) {
        this.expenseData = expenseData;
    }

    public List<ExpenseDataDto> getExpenseList() {
        return expenseList;
    }

    public void setExpenseList(List<ExpenseDataDto> expenseList) {
        this.expenseList = expenseList;
    }

    public MetadataDto getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataDto metadata) {
        this.metadata = metadata;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public PaginationInfoDto getPaginationInfo() {
        return paginationInfo;
    }

    public void setPaginationInfo(PaginationInfoDto paginationInfo) {
        this.paginationInfo = paginationInfo;
    }

    // Nested DTOs
    public static class ExpenseDataDto {
        private Long expenseId;
        private String staffName;
        private Double amount;
        private String note;
        private LocalDateTime date;
        private String status;
        private String category;

        public ExpenseDataDto() {
        }

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
    }

    public static class MetadataDto {
        private LocalDateTime timestamp;
        private String version;
        private String source;
        private String month;
        private Integer year;
        private Double totalAmount;
        private Integer totalRecords;

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

        public Integer getTotalRecords() {
            return totalRecords;
        }

        public void setTotalRecords(Integer totalRecords) {
            this.totalRecords = totalRecords;
        }
    }

    public static class PaginationInfoDto {
        private Integer currentPage;
        private Integer pageSize;
        private Long totalElements;
        private Integer totalPages;

        public PaginationInfoDto() {
        }

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Long getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(Long totalElements) {
            this.totalElements = totalElements;
        }

        public Integer getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(Integer totalPages) {
            this.totalPages = totalPages;
        }
    }
}