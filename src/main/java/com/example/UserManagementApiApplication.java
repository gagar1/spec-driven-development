package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the User Management API.
 * 
 * @spec: US1-T001 - Spring Boot application entry point
 * 
 * This application demonstrates a complete Spec-Driven Development (SDD) framework
 * implementation with a RESTful API for user management operations.
 * 
 * Key Features:
 * - CRUD operations for user management
 * - Input validation and error handling
 * - Security with authentication
 * - Comprehensive testing
 * - API documentation with OpenAPI/Swagger
 * - Full traceability from specifications to code
 * 
 * @author SDD Framework
 * @version 1.0
 * @since 2024-01-01
 */
@SpringBootApplication
public class UserManagementApiApplication {

	/**
	 * Main method to start the Spring Boot application.
	 * 
	 * @spec: US1-T001 - Application startup
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		// @spec: US1-T001 - Start Spring Boot application
		SpringApplication.run(UserManagementApiApplication.class, args);
	}

}