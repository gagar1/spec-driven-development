package com.example.service;

import com.example.dto.CreateUserRequest;
import com.example.dto.UpdateUserRequest;
import com.example.entity.User;
import com.example.exception.DuplicateEmailException;
import com.example.exception.UserNotFoundException;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for user management business logic.
 * 
 * @spec: US1-T003 - User service implementation
 * 
 * This service implements all business logic for user management operations
 * including validation, security, and data persistence. It serves as the
 * intermediary between the controller layer and the repository layer.
 * 
 * Key Features:
 * - User CRUD operations with business validation
 * - Password encryption and security
 * - Email uniqueness validation
 * - Soft delete implementation
 * - Transaction management
 * - Comprehensive logging
 * 
 * @author SDD Framework
 * @version 1.0
 * @since 2024-01-01
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Create a new user account.
     * 
     * @spec: US1-T003 - User creation business logic
     * 
     * This method handles user creation with proper validation,
     * password encryption, and duplicate email checking.
     * 
     * @param request the user creation request
     * @return the created user
     * @throws DuplicateEmailException if email already exists
     */
    @Transactional
    public User createUser(CreateUserRequest request) {
        log.info("Creating new user with email: {}", request.getEmail());
        
        // @spec: US1-T003 - Email uniqueness validation
        if (userRepository.existsByEmailAndActiveTrue(request.getEmail())) {
            log.warn("Attempt to create user with duplicate email: {}", request.getEmail());
            throw new DuplicateEmailException("User with email " + request.getEmail() + " already exists");
        }
        
        // @spec: US1-T003 - Password encryption
        String encryptedPassword = passwordEncoder.encode(request.getPassword());
        log.debug("Password encrypted for user: {}", request.getEmail());
        
        // @spec: US1-T003 - User entity creation
        User user = new User(
            request.getName(),
            request.getEmail(),
            encryptedPassword
        );
        
        // @spec: US1-T003 - User persistence
        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {} and email: {}", savedUser.getId(), savedUser.getEmail());
        
        return savedUser;
    }

    /**
     * Retrieve a user by their ID.
     * 
     * @spec: US1-T003 - User retrieval by ID
     * 
     * @param id the user ID
     * @return the user if found and active
     * @throws UserNotFoundException if user not found or inactive
     */
    public User getUserById(Long id) {
        log.debug("Retrieving user by ID: {}", id);
        
        // @spec: US1-T003 - Active user lookup
        return userRepository.findByIdAndActiveTrue(id)
            .orElseThrow(() -> {
                log.warn("User not found or inactive with ID: {}", id);
                return new UserNotFoundException("User not found with ID: " + id);
            });
    }

    /**
     * Retrieve all active users with pagination.
     * 
     * @spec: US1-T003 - User listing with pagination
     * 
     * @param pageable pagination information
     * @return page of active users
     */
    public Page<User> getAllUsers(Pageable pageable) {
        log.debug("Retrieving all active users with pagination: page={}, size={}", 
                 pageable.getPageNumber(), pageable.getPageSize());
        
        // @spec: US1-T003 - Paginated active user retrieval
        Page<User> users = userRepository.findByActiveTrue(pageable);
        log.info("Retrieved {} users out of {} total active users", 
                users.getNumberOfElements(), users.getTotalElements());
        
        return users;
    }

    /**
     * Update an existing user's information.
     * 
     * @spec: US1-T003 - User update business logic
     * 
     * @param id      the user ID to update
     * @param request the update request with new data
     * @return the updated user
     * @throws UserNotFoundException   if user not found
     * @throws DuplicateEmailException if new email already exists
     */
    @Transactional
    public User updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", id);
        
        // @spec: US1-T003 - Validate update request has content
        if (!request.hasUpdates()) {
            log.warn("Update request for user {} has no valid updates", id);
            throw new IllegalArgumentException("At least one field must be provided for update");
        }
        
        // @spec: US1-T003 - Retrieve existing user
        User existingUser = getUserById(id);
        
        // @spec: US1-T003 - Email uniqueness validation for updates
        if (request.hasEmailUpdate()) {
            String newEmail = request.getNormalizedEmail();
            if (userRepository.existsByEmailAndActiveTrueAndIdNot(newEmail, id)) {
                log.warn("Attempt to update user {} with duplicate email: {}", id, newEmail);
                throw new DuplicateEmailException("User with email " + newEmail + " already exists");
            }
        }
        
        // @spec: US1-T003 - Apply updates to user
        if (request.hasNameUpdate()) {
            existingUser.setName(request.getNormalizedName());
            log.debug("Updated name for user {}: {}", id, request.getNormalizedName());
        }
        
        if (request.hasEmailUpdate()) {
            existingUser.setEmail(request.getNormalizedEmail());
            log.debug("Updated email for user {}: {}", id, request.getNormalizedEmail());
        }
        
        // @spec: US1-T003 - Save updated user
        User updatedUser = userRepository.save(existingUser);
        log.info("User updated successfully with ID: {}", updatedUser.getId());
        
        return updatedUser;
    }

    /**
     * Soft delete a user account.
     * 
     * @spec: US1-T003 - User soft deletion
     * 
     * @param id the user ID to delete
     * @throws UserNotFoundException if user not found
     */
    @Transactional
    public void deleteUser(Long id) {
        log.info("Soft deleting user with ID: {}", id);
        
        // @spec: US1-T003 - Retrieve user for deletion
        User user = getUserById(id);
        
        // @spec: US1-T003 - Perform soft delete
        user.deactivate();
        userRepository.save(user);
        
        log.info("User soft deleted successfully with ID: {}", id);
    }

    /**
     * Check if a user exists by email.
     * 
     * @spec: US1-T003 - Email existence check
     * 
     * @param email the email to check
     * @return true if an active user with this email exists
     */
    public boolean existsByEmail(String email) {
        log.debug("Checking if user exists with email: {}", email);
        
        // @spec: US1-T003 - Active user email check
        boolean exists = userRepository.existsByEmailAndActiveTrue(email);
        log.debug("User exists with email {}: {}", email, exists);
        
        return exists;
    }

    /**
     * Find a user by email address.
     * 
     * @spec: US1-T003 - User lookup by email
     * 
     * @param email the email address
     * @return the user if found and active
     * @throws UserNotFoundException if user not found or inactive
     */
    public User findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        
        // @spec: US1-T003 - Active user email lookup
        return userRepository.findByEmailAndActiveTrue(email)
            .orElseThrow(() -> {
                log.warn("User not found or inactive with email: {}", email);
                return new UserNotFoundException("User not found with email: " + email);
            });
    }

    /**
     * Get the total count of active users.
     * 
     * @spec: US1-T003 - Active user count
     * 
     * @return the number of active users
     */
    public long getActiveUserCount() {
        log.debug("Getting active user count");
        
        // @spec: US1-T003 - Count active users
        long count = userRepository.countByActiveTrue();
        log.debug("Active user count: {}", count);
        
        return count;
    }

    /**
     * Search users by name pattern.
     * 
     * @spec: US1-T003 - User search functionality
     * 
     * @param namePattern the name pattern to search for
     * @param pageable    pagination information
     * @return page of matching users
     */
    public Page<User> searchUsersByName(String namePattern, Pageable pageable) {
        log.debug("Searching users by name pattern: {}", namePattern);
        
        // @spec: US1-T003 - Name-based user search
        String searchPattern = "%" + namePattern.toLowerCase() + "%";
        Page<User> users = userRepository.findByNameContainingIgnoreCaseAndActiveTrue(searchPattern, pageable);
        
        log.info("Found {} users matching pattern '{}'", users.getTotalElements(), namePattern);
        return users;
    }

    /**
     * Get recently created users.
     * 
     * @spec: US1-T003 - Recent user retrieval
     * 
     * @param pageable pagination information (should include sort by createdAt DESC)
     * @return page of recently created users
     */
    public Page<User> getRecentlyCreatedUsers(Pageable pageable) {
        log.debug("Retrieving recently created users");
        
        // @spec: US1-T003 - Recent user lookup
        Page<User> users = userRepository.findRecentlyCreatedActiveUsers(pageable);
        log.info("Retrieved {} recently created users", users.getNumberOfElements());
        
        return users;
    }
}