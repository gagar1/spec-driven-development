package com.example.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for user update requests.
 * 
 * @spec: US1-T004 - Update user request DTO
 * 
 * This DTO represents the data that can be updated for an existing user.
 * All fields are optional to support partial updates, but when provided,
 * they must meet the same validation criteria as user creation.
 * 
 * Key Features:
 * - Optional fields for partial updates
 * - Same validation rules as creation
 * - Null-safe field handling
 * - Data normalization
 * 
 * @author SDD Framework
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    /**
     * User's updated full name (optional).
     * 
     * @spec: US1-T004 - Name validation for user updates
     */
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(
        regexp = "^[a-zA-Z\\s\\-']+$", 
        message = "Name can only contain letters, spaces, hyphens, and apostrophes"
    )
    private String name;

    /**
     * User's updated email address (optional).
     * 
     * @spec: US1-T004 - Email validation for user updates
     */
    @Email(message = "Email must be a valid email address")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    /**
     * Check if the request has any non-null fields.
     * 
     * @spec: US1-T004 - Validate update request has content
     * 
     * @return true if at least one field is provided for update
     */
    public boolean hasUpdates() {
        return (name != null && !name.trim().isEmpty()) || 
               (email != null && !email.trim().isEmpty());
    }

    /**
     * Check if name update is requested.
     * 
     * @spec: US1-T004 - Check name update availability
     * 
     * @return true if name is provided and not empty
     */
    public boolean hasNameUpdate() {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Check if email update is requested.
     * 
     * @spec: US1-T004 - Check email update availability
     * 
     * @return true if email is provided and not empty
     */
    public boolean hasEmailUpdate() {
        return email != null && !email.trim().isEmpty();
    }

    /**
     * Normalize email to lowercase for consistency.
     * 
     * @spec: US1-T004 - Email normalization for updates
     * 
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email != null ? email.trim().toLowerCase() : null;
    }

    /**
     * Trim whitespace from name.
     * 
     * @spec: US1-T004 - Name normalization for updates
     * 
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    /**
     * Get normalized name value.
     * 
     * @spec: US1-T004 - Get normalized name
     * 
     * @return trimmed name or null
     */
    public String getNormalizedName() {
        return name != null ? name.trim() : null;
    }

    /**
     * Get normalized email value.
     * 
     * @spec: US1-T004 - Get normalized email
     * 
     * @return trimmed and lowercase email or null
     */
    public String getNormalizedEmail() {
        return email != null ? email.trim().toLowerCase() : null;
    }

    /**
     * String representation of the update request.
     * 
     * @spec: US1-T004 - Safe string representation for updates
     * 
     * @return string representation of the update request
     */
    @Override
    public String toString() {
        return "UpdateUserRequest{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}