package com.example.dto;

import com.example.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for user response data.
 * 
 * @spec: US1-T004 - User response DTO
 * 
 * This DTO represents user data returned by API endpoints.
 * It excludes sensitive information like passwords and provides
 * a clean interface for client applications.
 * 
 * Key Features:
 * - Excludes sensitive data (passwords)
 * - Includes all public user information
 * - Supports conversion from User entity
 * - Consistent timestamp formatting
 * 
 * @author SDD Framework
 * @version 1.0
 * @since 2024-01-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    /**
     * User's unique identifier.
     * 
     * @spec: US1-T004 - User ID in response
     */
    private Long id;

    /**
     * User's full name.
     * 
     * @spec: US1-T004 - User name in response
     */
    private String name;

    /**
     * User's email address.
     * 
     * @spec: US1-T004 - User email in response
     */
    private String email;

    /**
     * Timestamp when the user account was created.
     * 
     * @spec: US1-T004 - Creation timestamp in response
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the user account was last updated.
     * 
     * @spec: US1-T004 - Update timestamp in response
     */
    private LocalDateTime updatedAt;

    /**
     * Flag indicating if the user account is active.
     * 
     * @spec: US1-T004 - Active status in response
     */
    private Boolean active;

    /**
     * Create a UserResponse from a User entity.
     * 
     * @spec: US1-T004 - Entity to DTO conversion
     * 
     * @param user the User entity to convert
     * @return UserResponse containing public user data
     */
    public static UserResponse fromEntity(User user) {
        if (user == null) {
            return null;
        }
        
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getActive()
        );
    }

    /**
     * Constructor from User entity.
     * 
     * @spec: US1-T004 - Constructor from entity
     * 
     * @param user the User entity to convert
     */
    public UserResponse(User user) {
        if (user != null) {
            this.id = user.getId();
            this.name = user.getName();
            this.email = user.getEmail();
            this.createdAt = user.getCreatedAt();
            this.updatedAt = user.getUpdatedAt();
            this.active = user.getActive();
        }
    }

    /**
     * Check if this response represents an active user.
     * 
     * @spec: US1-T004 - Active status check
     * 
     * @return true if the user is active, false otherwise
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(this.active);
    }

    /**
     * Get a display name for the user.
     * 
     * @spec: US1-T004 - User display name
     * 
     * @return formatted display name
     */
    public String getDisplayName() {
        return name != null ? name : "User #" + id;
    }

    /**
     * String representation of the user response.
     * 
     * @spec: US1-T004 - String representation
     * 
     * @return string representation of user data
     */
    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", active=" + active +
                '}';
    }
}