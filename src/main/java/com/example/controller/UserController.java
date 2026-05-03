package com.example.controller;

import com.example.dto.CreateUserRequest;
import com.example.dto.UpdateUserRequest;
import com.example.dto.UserResponse;
import com.example.entity.User;
import com.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for User Management API endpoints.
 * 
 * @spec: US1-T004 - User management REST controller
 * 
 * This controller provides RESTful endpoints for user management operations
 * including CRUD operations with proper HTTP status codes, validation,
 * and comprehensive API documentation.
 * 
 * Key Features:
 * - RESTful API design with proper HTTP methods
 * - Input validation with detailed error responses
 * - Pagination support for user listing
 * - Comprehensive OpenAPI documentation
 * - Structured logging for monitoring
 * - Consistent response formats
 * 
 * @author SDD Framework
 * @version 1.0
 * @since 2024-01-01
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "API endpoints for managing user accounts")
public class UserController {

    private final UserService userService;

    /**
     * Create a new user account.
     * 
     * @spec: US1-T004 - Create user endpoint
     * 
     * @param request the user creation request
     * @return ResponseEntity with created user data and 201 status
     */
    @PostMapping
    @Operation(
        summary = "Create a new user",
        description = "Creates a new user account with the provided information. Email must be unique."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", 
            description = "User created successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input data",
            content = @Content(schema = @Schema(implementation = com.example.exception.ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Email already exists",
            content = @Content(schema = @Schema(implementation = com.example.exception.ErrorResponse.class))
        )
    })
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        
        log.info("Creating user with email: {}", request.getEmail());
        
        // @spec: US1-T004 - User creation via service
        User createdUser = userService.createUser(request);
        
        // @spec: US1-T004 - Convert entity to response DTO
        UserResponse response = UserResponse.fromEntity(createdUser);
        
        log.info("User created successfully with ID: {}", createdUser.getId());
        
        // @spec: US1-T004 - Return 201 Created with user data
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Retrieve a user by their ID.
     * 
     * @spec: US1-T004 - Get user by ID endpoint
     * 
     * @param id the user ID
     * @return ResponseEntity with user data and 200 status
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Get user by ID",
        description = "Retrieves a user by their unique identifier. Only returns active users."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User found",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid user ID format",
            content = @Content(schema = @Schema(implementation = com.example.exception.ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(schema = @Schema(implementation = com.example.exception.ErrorResponse.class))
        )
    })
    public ResponseEntity<UserResponse> getUserById(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        
        log.debug("Retrieving user with ID: {}", id);
        
        // @spec: US1-T004 - User retrieval via service
        User user = userService.getUserById(id);
        
        // @spec: US1-T004 - Convert entity to response DTO
        UserResponse response = UserResponse.fromEntity(user);
        
        log.debug("User retrieved successfully with ID: {}", id);
        
        // @spec: US1-T004 - Return 200 OK with user data
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve all users with pagination.
     * 
     * @spec: US1-T004 - Get all users endpoint with pagination
     * 
     * @param pageable pagination parameters
     * @return ResponseEntity with paginated user data and 200 status
     */
    @GetMapping
    @Operation(
        summary = "Get all users",
        description = "Retrieves all active users with pagination support. Default page size is 20."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Users retrieved successfully",
            content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid pagination parameters",
            content = @Content(schema = @Schema(implementation = com.example.exception.ErrorResponse.class))
        )
    })
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @Parameter(description = "Pagination parameters (page, size, sort)")
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        
        log.debug("Retrieving all users with pagination: page={}, size={}", 
                 pageable.getPageNumber(), pageable.getPageSize());
        
        // @spec: US1-T004 - User listing via service
        Page<User> users = userService.getAllUsers(pageable);
        
        // @spec: US1-T004 - Convert entities to response DTOs
        Page<UserResponse> response = users.map(UserResponse::fromEntity);
        
        log.info("Retrieved {} users out of {} total", 
                response.getNumberOfElements(), response.getTotalElements());
        
        // @spec: US1-T004 - Return 200 OK with paginated user data
        return ResponseEntity.ok(response);
    }

    /**
     * Update an existing user's information.
     * 
     * @spec: US1-T004 - Update user endpoint
     * 
     * @param id      the user ID to update
     * @param request the update request with new data
     * @return ResponseEntity with updated user data and 200 status
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Update user",
        description = "Updates an existing user's information. Supports partial updates. Email must remain unique."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = UserResponse.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input data or user ID",
            content = @Content(schema = @Schema(implementation = com.example.exception.ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(schema = @Schema(implementation = com.example.exception.ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "Email already exists",
            content = @Content(schema = @Schema(implementation = com.example.exception.ErrorResponse.class))
        )
    })
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        
        log.info("Updating user with ID: {}", id);
        
        // @spec: US1-T004 - User update via service
        User updatedUser = userService.updateUser(id, request);
        
        // @spec: US1-T004 - Convert entity to response DTO
        UserResponse response = UserResponse.fromEntity(updatedUser);
        
        log.info("User updated successfully with ID: {}", id);
        
        // @spec: US1-T004 - Return 200 OK with updated user data
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a user account (soft delete).
     * 
     * @spec: US1-T004 - Delete user endpoint
     * 
     * @param id the user ID to delete
     * @return ResponseEntity with 204 status (no content)
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete user",
        description = "Soft deletes a user account by marking it as inactive. The user data is preserved but becomes inaccessible."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204", 
            description = "User deleted successfully"
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid user ID format",
            content = @Content(schema = @Schema(implementation = com.example.exception.ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(schema = @Schema(implementation = com.example.exception.ErrorResponse.class))
        )
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", required = true)
            @PathVariable Long id) {
        
        log.info("Deleting user with ID: {}", id);
        
        // @spec: US1-T004 - User deletion via service
        userService.deleteUser(id);
        
        log.info("User deleted successfully with ID: {}", id);
        
        // @spec: US1-T004 - Return 204 No Content
        return ResponseEntity.noContent().build();
    }

    /**
     * Search users by name pattern.
     * 
     * @spec: US1-T004 - Search users endpoint
     * 
     * @param namePattern the name pattern to search for
     * @param pageable    pagination parameters
     * @return ResponseEntity with matching users and 200 status
     */
    @GetMapping("/search")
    @Operation(
        summary = "Search users by name",
        description = "Searches for users by name pattern (case-insensitive partial matching). Only returns active users."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Search completed successfully",
            content = @Content(schema = @Schema(implementation = Page.class))
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid search parameters",
            content = @Content(schema = @Schema(implementation = com.example.exception.ErrorResponse.class))
        )
    })
    public ResponseEntity<Page<UserResponse>> searchUsers(
            @Parameter(description = "Name pattern to search for", required = true)
            @RequestParam String namePattern,
            @Parameter(description = "Pagination parameters")
            @PageableDefault(size = 20, sort = "name") Pageable pageable) {
        
        log.debug("Searching users with name pattern: {}", namePattern);
        
        // @spec: US1-T004 - User search via service
        Page<User> users = userService.searchUsersByName(namePattern, pageable);
        
        // @spec: US1-T004 - Convert entities to response DTOs
        Page<UserResponse> response = users.map(UserResponse::fromEntity);
        
        log.info("Found {} users matching pattern '{}'", response.getTotalElements(), namePattern);
        
        // @spec: US1-T004 - Return 200 OK with search results
        return ResponseEntity.ok(response);
    }

    /**
     * Get user statistics.
     * 
     * @spec: US1-T004 - User statistics endpoint
     * 
     * @return ResponseEntity with user count statistics
     */
    @GetMapping("/stats")
    @Operation(
        summary = "Get user statistics",
        description = "Retrieves basic statistics about users in the system."
    )
    @ApiResponse(
        responseCode = "200", 
        description = "Statistics retrieved successfully"
    )
    public ResponseEntity<UserStatsResponse> getUserStats() {
        log.debug("Retrieving user statistics");
        
        // @spec: US1-T004 - User statistics via service
        long activeUserCount = userService.getActiveUserCount();
        
        UserStatsResponse stats = new UserStatsResponse(activeUserCount);
        
        log.debug("User statistics retrieved: {} active users", activeUserCount);
        
        // @spec: US1-T004 - Return 200 OK with statistics
        return ResponseEntity.ok(stats);
    }

    /**
     * Simple statistics response for user count.
     * 
     * @spec: US1-T004 - User statistics response DTO
     */
    public record UserStatsResponse(long activeUserCount) {}
}