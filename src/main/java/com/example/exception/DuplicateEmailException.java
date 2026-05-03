package com.example.exception;

/**
 * Exception thrown when attempting to create or update a user with an email that already exists.
 * 
 * @spec: US1-T005 - Duplicate email exception
 * 
 * This exception is thrown when email uniqueness validation fails
 * during user creation or update operations.
 * 
 * @author SDD Framework
 * @version 1.0
 * @since 2024-01-01
 */
public class DuplicateEmailException extends RuntimeException {

    /**
     * Constructs a new DuplicateEmailException with the specified detail message.
     * 
     * @spec: US1-T005 - Exception with message
     * 
     * @param message the detail message
     */
    public DuplicateEmailException(String message) {
        super(message);
    }

    /**
     * Constructs a new DuplicateEmailException with the specified detail message and cause.
     * 
     * @spec: US1-T005 - Exception with message and cause
     * 
     * @param message the detail message
     * @param cause   the cause
     */
    public DuplicateEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}