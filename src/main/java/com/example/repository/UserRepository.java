package com.example.repository;

import com.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for User entity data access operations.
 * 
 * @spec: US1-T002 - User repository interface
 * 
 * This repository provides data access methods for User entities,
 * extending Spring Data JPA's JpaRepository for basic CRUD operations
 * and adding custom query methods for business-specific requirements.
 * 
 * Key Features:
 * - Basic CRUD operations via JpaRepository
 * - Custom queries for active users
 * - Email uniqueness validation
 * - Pagination support for user listing
 * - Soft delete awareness
 * 
 * @author SDD Framework
 * @version 1.0
 * @since 2024-01-01
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find an active user by email address.
     * 
     * @spec: US1-T002 - Find user by email (active only)
     * 
     * This method is used for user authentication and duplicate email validation.
     * Only returns users that are currently active (not soft-deleted).
     * 
     * @param email the email address to search for
     * @return Optional containing the user if found and active, empty otherwise
     */
    Optional<User> findByEmailAndActiveTrue(String email);

    /**
     * Find all active users with pagination support.
     * 
     * @spec: US1-T002 - Get all active users with pagination
     * 
     * This method supports the user listing API endpoint by returning
     * only active users with proper pagination to handle large datasets.
     * 
     * @param pageable pagination information (page, size, sort)
     * @return Page containing active users
     */
    Page<User> findByActiveTrue(Pageable pageable);

    /**
     * Check if an active user exists with the given email.
     * 
     * @spec: US1-T002 - Email uniqueness validation
     * 
     * This method is used to validate email uniqueness during user creation
     * and updates, considering only active users to allow email reuse after
     * soft deletion.
     * 
     * @param email the email address to check
     * @return true if an active user with this email exists, false otherwise
     */
    boolean existsByEmailAndActiveTrue(String email);

    /**
     * Check if an active user exists with the given email, excluding a specific user ID.
     * 
     * @spec: US1-T002 - Email uniqueness validation for updates
     * 
     * This method is used during user updates to ensure email uniqueness
     * while allowing the current user to keep their existing email.
     * 
     * @param email the email address to check
     * @param id    the user ID to exclude from the check
     * @return true if another active user with this email exists, false otherwise
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
           "FROM User u WHERE u.email = :email AND u.active = true AND u.id != :id")
    boolean existsByEmailAndActiveTrueAndIdNot(@Param("email") String email, @Param("id") Long id);

    /**
     * Find an active user by ID.
     * 
     * @spec: US1-T002 - Find active user by ID
     * 
     * This method ensures that only active users can be retrieved by ID,
     * effectively implementing soft delete at the repository level.
     * 
     * @param id the user ID to search for
     * @return Optional containing the user if found and active, empty otherwise
     */
    Optional<User> findByIdAndActiveTrue(Long id);

    /**
     * Count all active users in the system.
     * 
     * @spec: US1-T002 - Count active users
     * 
     * This method provides statistics about the number of active users
     * in the system for monitoring and reporting purposes.
     * 
     * @return the number of active users
     */
    long countByActiveTrue();

    /**
     * Find users by name pattern (case-insensitive) among active users.
     * 
     * @spec: US1-T002 - Search users by name pattern
     * 
     * This method supports user search functionality by allowing
     * partial name matching while respecting soft delete status.
     * 
     * @param namePattern the name pattern to search for (supports % wildcards)
     * @param pageable    pagination information
     * @return Page containing matching active users
     */
    @Query("SELECT u FROM User u WHERE u.active = true AND LOWER(u.name) LIKE LOWER(:namePattern)")
    Page<User> findByNameContainingIgnoreCaseAndActiveTrue(
            @Param("namePattern") String namePattern, 
            Pageable pageable
    );

    /**
     * Find the most recently created active users.
     * 
     * @spec: US1-T002 - Get recently created users
     * 
     * This method supports administrative functionality by providing
     * a list of recently registered users for monitoring purposes.
     * 
     * @param pageable pagination information (should include sort by createdAt DESC)
     * @return Page containing recently created active users
     */
    @Query("SELECT u FROM User u WHERE u.active = true ORDER BY u.createdAt DESC")
    Page<User> findRecentlyCreatedActiveUsers(Pageable pageable);
}