package com.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * User entity representing user accounts in the system.
 * 
 * @spec: US1-T002 - User entity definition
 * 
 * This entity implements the user data model as specified in the requirements,
 * including all necessary JPA annotations for database persistence and
 * validation constraints for data integrity.
 * 
 * Key Features:
 * - Auto-generated primary key
 * - Unique email constraint
 * - Automatic timestamp management
 * - Soft delete capability with active flag
 * - Bean validation annotations
 * 
 * @author SDD Framework
 * @version 1.0
 * @since 2024-01-01
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_active", columnList = "active"),
    @Index(name = "idx_created_at", columnList = "createdAt")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Unique identifier for the user.
     * 
     * @spec: US1-T002 - Primary key definition
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * User's full name.
     * 
     * @spec: US1-T002 - User name field with validation
     */
    @Column(name = "name", nullable = false, length = 50)
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z\\s\\-']+$", message = "Name can only contain letters, spaces, hyphens, and apostrophes")
    private String name;

    /**
     * User's email address (unique).
     * 
     * @spec: US1-T002 - Email field with uniqueness constraint
     */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    /**
     * User's encrypted password.
     * 
     * @spec: US1-T002 - Password field for authentication
     */
    @Column(name = "password", nullable = false)
    @NotBlank(message = "Password is required")
    private String password;

    /**
     * Timestamp when the user account was created.
     * 
     * @spec: US1-T002 - Automatic creation timestamp
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the user account was last updated.
     * 
     * @spec: US1-T002 - Automatic update timestamp
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Flag indicating if the user account is active (soft delete).
     * 
     * @spec: US1-T002 - Soft delete implementation
     */
    @Column(name = "active", nullable = false)
    @NotNull(message = "Active flag is required")
    private Boolean active = true;

    /**
     * Constructor for creating a new user with required fields.
     * 
     * @spec: US1-T002 - User creation constructor
     * 
     * @param name     the user's full name
     * @param email    the user's email address
     * @param password the user's encrypted password
     */
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.active = true;
    }

    /**
     * Soft delete the user by setting active flag to false.
     * 
     * @spec: US1-T002 - Soft delete method
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Reactivate a soft-deleted user.
     * 
     * @spec: US1-T002 - User reactivation method
     */
    public void activate() {
        this.active = true;
    }

    /**
     * Check if the user account is active.
     * 
     * @spec: US1-T002 - Active status check
     * 
     * @return true if the user is active, false otherwise
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(this.active);
    }

    /**
     * Update user information.
     * 
     * @spec: US1-T002 - User update method
     * 
     * @param name  the new name (optional)
     * @param email the new email (optional)
     */
    public void updateInfo(String name, String email) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name.trim();
        }
        if (email != null && !email.trim().isEmpty()) {
            this.email = email.trim().toLowerCase();
        }
    }

    /**
     * Custom toString method that excludes sensitive information.
     * 
     * @spec: US1-T002 - Safe string representation
     * 
     * @return string representation without password
     */
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}