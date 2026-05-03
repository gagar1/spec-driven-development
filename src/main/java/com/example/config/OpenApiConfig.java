package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI (Swagger) configuration for API documentation.
 * 
 * @spec: US1-T007 - API documentation configuration
 * 
 * This configuration sets up comprehensive API documentation using OpenAPI 3.0
 * specification. It provides interactive documentation for all API endpoints.
 * 
 * Key Features:
 * - Interactive API documentation
 * - Comprehensive endpoint descriptions
 * - Request/response examples
 * - Error response documentation
 * 
 * @author SDD Framework
 * @version 1.0
 * @since 2024-01-01
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configure OpenAPI documentation.
     * 
     * @spec: US1-T007 - OpenAPI configuration
     * 
     * @return OpenAPI configuration
     */
    @Bean
    public OpenAPI userManagementOpenAPI() {
        // @spec: US1-T007 - API documentation setup
        return new OpenAPI()
            .info(new Info()
                .title("User Management API")
                .description("RESTful API for managing user accounts with comprehensive CRUD operations. " +
                           "This API demonstrates Spec-Driven Development (SDD) framework implementation " +
                           "with full traceability from specifications to code.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("SDD Framework")
                    .email("sdd@example.com")
                    .url("https://github.com/sdd-framework/user-management-api")
                )
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")
                )
            );
    }
}