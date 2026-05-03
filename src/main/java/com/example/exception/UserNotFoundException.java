package com.example.exception;

/**
 * Exception thrown when a requested user is not found.
 * 
 * @spec: US1-T005 - User not found exception
 * 
 * This exception is thrown when attempting to access a user that
 * does not exist or has been soft deleted (inactive).
 * 
 * @author SDD Framework
 * @version 1.0
 * @since 2024-01-01
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Constructs a new UserNotFoundException with the specified detail message.
     * 
     * @spec: US1-T005 - Exception with message
     * 
     * @param message the detail message
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new UserNotFoundException with the specified detail message and cause.
     * 
     * @spec: US1-T005 - Exception with message and cause
     * 
     * @param message the detail message
     * @param cause   the cause
     */
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}