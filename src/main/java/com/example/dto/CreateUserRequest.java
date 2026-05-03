package com.example.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user creation requests.
 * 
 * @spec: US1-T004 - Create user request DTO
 * 
 * This DTO represents the data required to create a new user account.
 * It includes validation annotations to ensure data integrity and
 * provides clear error messages for validation failures.
 * 
 * Key Features:
 * - Input validation with custom messages
 * - Password strength requirements
 * - Email format validation
 * - Name length and character constraints
 * 
 * @author SDD Framework
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    /**
     * User's full name.
     * 
     * @spec: US1-T004 - Name validation for user creation
     */
    @NotBlank(message = "Name is required and cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(
        regexp = "^[a-zA-Z\\s\\-']+$", 
        message = "Name can only contain letters, spaces, hyphens, and apostrophes"
    )
    private String name;

    /**
     * User's email address.
     * 
     * @spec: US1-T004 - Email validation for user creation
     */
    @NotBlank(message = "Email is required and cannot be blank")
    @Email(message = "Email must be a valid email address")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    /**
     * User's password.
     * 
     * @spec: US1-T004 - Password validation for user creation
     */
    @NotBlank(message = "Password is required and cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
        message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit"
    )
    private String password;

    /**
     * Normalize email to lowercase for consistency.
     * 
     * @spec: US1-T004 - Email normalization
     * 
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email != null ? email.trim().toLowerCase() : null;
    }

    /**
     * Trim whitespace from name.
     * 
     * @spec: US1-T004 - Name normalization
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    /**
     * Custom toString method that excludes sensitive information.
     * 
     * @spec: US1-T004 - Safe string representation
     * 
     * @return string representation without password
     */
    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}