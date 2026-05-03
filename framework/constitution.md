# SDD Constitution - Architectural Laws

## Fundamental Architectural Principles

### 1. Clean Architecture Compliance
- **Dependency Inversion**: High-level modules must not depend on low-level modules
- **Single Responsibility**: Each class/method has one reason to change
- **Open/Closed**: Open for extension, closed for modification
- **Interface Segregation**: Clients should not depend on unused interfaces
- **Dependency Injection**: Use Spring's IoC container for all dependencies

### 2. API Design Standards
- **RESTful Principles**: Proper HTTP methods, status codes, and resource naming
- **Consistent Naming**: Use camelCase for JSON, kebab-case for URLs
- **Error Handling**: Standardized error response format
- **Validation**: Input validation at controller layer
- **Documentation**: OpenAPI/Swagger documentation required

### 3. Data Layer Rules
- **Repository Pattern**: Use Spring Data JPA repositories
- **Entity Integrity**: Proper JPA annotations and relationships
- **Transaction Management**: Use `@Transactional` appropriately
- **Database Migrations**: Use Flyway for schema versioning
- **No Direct SQL**: Use JPA/JPQL unless performance critical

### 4. Security Requirements
- **Input Sanitization**: Validate and sanitize all inputs
- **Authentication**: JWT-based authentication for protected endpoints
- **Authorization**: Role-based access control where applicable
- **HTTPS Only**: No plain HTTP in production
- **Sensitive Data**: Never log passwords or tokens

### 5. Testing Standards
- **Unit Tests**: Minimum 80% code coverage
- **Integration Tests**: Test all API endpoints
- **Test Isolation**: Each test must be independent
- **Mock External Dependencies**: Use Mockito for unit tests
- **Test Data**: Use TestContainers for integration tests

### 6. Code Quality Gates
- **Naming Conventions**: Clear, descriptive names for all entities
- **Method Length**: Maximum 20 lines per method
- **Class Size**: Maximum 200 lines per class
- **Cyclomatic Complexity**: Maximum complexity of 10
- **Documentation**: Javadoc for all public methods

### 7. Performance Requirements
- **Response Time**: API responses under 200ms for simple operations
- **Pagination**: Implement pagination for list endpoints
- **Caching**: Use Spring Cache for frequently accessed data
- **Connection Pooling**: Configure HikariCP properly
- **Lazy Loading**: Use lazy loading for JPA relationships

### 8. Monitoring and Observability
- **Logging**: Use SLF4J with structured logging
- **Metrics**: Expose Actuator endpoints for monitoring
- **Health Checks**: Implement custom health indicators
- **Tracing**: Include correlation IDs in logs
- **Error Tracking**: Log all exceptions with context

## Violation Consequences

### Automatic Rejection Triggers
- Direct database access without repositories
- Missing input validation
- Hardcoded configuration values
- Missing error handling
- No unit tests for new code

### Warning Triggers
- Methods exceeding 15 lines
- Classes exceeding 150 lines
- Missing Javadoc on public methods
- Synchronous calls to external services
- Missing logging for important operations

## Approval Requirements

### Code Review Checklist
- [ ] Follows Clean Architecture principles
- [ ] Implements proper error handling
- [ ] Includes comprehensive tests
- [ ] Has traceability tags
- [ ] Follows naming conventions
- [ ] Includes appropriate logging
- [ ] Validates all inputs
- [ ] Uses dependency injection

### Architecture Review Required For
- New external dependencies
- Database schema changes
- New API endpoints
- Security-related changes
- Performance-critical code

---

**These architectural laws are immutable. Any plan violating these principles must be rejected and revised.**