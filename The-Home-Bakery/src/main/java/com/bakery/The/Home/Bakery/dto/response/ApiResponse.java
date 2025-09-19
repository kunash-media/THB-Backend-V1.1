package com.bakery.The.Home.Bakery.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @JsonProperty("success")
    private boolean success;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private T data;

    @JsonProperty("error")
    private String error;

    @JsonProperty("errorCode")
    private String errorCode;

    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    @JsonProperty("path")
    private String path;

    @JsonProperty("method")
    private String method;

    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiResponse(boolean success, String message, T data, String error, String errorCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = error;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }

    // Static factory methods for success responses
    public static <T> ApiResponse<T> success(String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.message = message;
        response.data = data;
        return response;
    }

    public static <T> ApiResponse<T> success(T data) {
        return success("Operation completed successfully", data);
    }

    public static ApiResponse<Void> success(String message) {
        return success(message, null);
    }

    // Static factory methods for error responses
    public static <T> ApiResponse<T> error(String error) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.error = error;
        return response;
    }

    public static <T> ApiResponse<T> error(String error, String errorCode) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.error = error;
        response.errorCode = errorCode;
        return response;
    }

    public static <T> ApiResponse<T> validationError(String error) {
        return error(error, "VALIDATION_ERROR");
    }

    public static <T> ApiResponse<T> notFoundError(String error) {
        return error(error, "NOT_FOUND");
    }

    public static <T> ApiResponse<T> internalError(String error) {
        return error(error, "INTERNAL_SERVER_ERROR");
    }

    // Builder pattern for more complex responses
    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<>();
    }

    public static class ApiResponseBuilder<T> {
        private boolean success;
        private String message;
        private T data;
        private String error;
        private String errorCode;
        private String path;
        private String method;

        public ApiResponseBuilder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        public ApiResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        public ApiResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ApiResponseBuilder<T> error(String error) {
            this.error = error;
            return this;
        }

        public ApiResponseBuilder<T> errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public ApiResponseBuilder<T> path(String path) {
            this.path = path;
            return this;
        }

        public ApiResponseBuilder<T> method(String method) {
            this.method = method;
            return this;
        }

        public ApiResponse<T> build() {
            ApiResponse<T> response = new ApiResponse<>(success, message, data, error, errorCode);
            response.path = this.path;
            response.method = this.method;
            return response;
        }
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", error='" + error + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", timestamp=" + timestamp +
                ", path='" + path + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}