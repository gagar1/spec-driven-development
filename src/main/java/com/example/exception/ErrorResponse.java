package com.example.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard error response structure for API errors.
 * 
 * @spec: US1-T005 - Standardized error response format
 * 
 * This class provides a consistent structure for error responses
 * across all API endpoints, ensuring clients can handle errors uniformly.
 * 
 * @author SDD Framework
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * Timestamp when the error occurred.
     * 
     * @spec: US1-T005 - Error timestamp
     */
    private LocalDateTime timestamp;

    /**
     * HTTP status code.
     * 
     * @spec: US1-T005 - HTTP status in error response
     */
    private int status;

    /**
     * Error type or category.
     * 
     * @spec: US1-T005 - Error type classification
     */
    private String error;

    /**
     * Main error message.
     * 
     * @spec: US1-T005 - Primary error message
     */
    private String message;

    /**
     * Request path where the error occurred.
     * 
     * @spec: US1-T005 - Request path in error
     */
    private String path;

    /**
     * Detailed validation errors (for validation failures).
     * 
     * @spec: US1-T005 - Detailed validation errors
     */
    private List<String> details;

    /**
     * Create an error response for a general error.
     * 
     * @spec: US1-T005 - General error response factory
     * 
     * @param status  HTTP status code
     * @param error   error type
     * @param message error message
     * @param path    request path
     * @return ErrorResponse instance
     */
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(
            LocalDateTime.now(),
            status,
            error,
            message,
            path,
            null
        );
    }

    /**
     * Create an error response with validation details.
     * 
     * @spec: US1-T005 - Validation error response factory
     * 
     * @param status  HTTP status code
     * @param error   error type
     * @param message error message
     * @param path    request path
     * @param details validation error details
     * @return ErrorResponse instance
     */
    public static ErrorResponse of(int status, String error, String message, String path, List<String> details) {
        return new ErrorResponse(
            LocalDateTime.now(),
            status,
            error,
            message,
            path,
            details
        );
    }
}