# Implementation Workflow

## Workflow ID: `/speckit.implement`

## Purpose
This workflow guides the agent through implementation task breakdown and code generation with full traceability.

## Trigger Conditions
- Automatically triggered after technical plan approval
- Manual trigger for implementation reviews
- Task completion and progression

## Prerequisites
- Approved specification document exists
- Approved technical plan exists
- Constitutional constraints are loaded
- Development environment is ready

## Workflow Steps

### Step 1: Task Breakdown
**Agent Action**: Break approved plan into detailed implementation tasks

**Task Categories**:
1. **Setup Tasks**: Project structure, dependencies, configuration
2. **Core Implementation**: Business logic, data models, services
3. **API Implementation**: Controllers, request/response handling
4. **Testing Tasks**: Unit tests, integration tests
5. **Documentation Tasks**: API docs, code documentation

**Task Prioritization**:
- Dependencies first (setup, core models)
- Core business logic
- API layer implementation
- Testing and documentation
- Integration and deployment

### Step 2: Task Documentation
**Agent Action**: Create detailed task documents

**For Each Task**:
1. Generate unique Task ID (T001, T002, etc.)
2. Use `task.template.md` as base structure
3. Define clear success criteria
4. Specify implementation approach
5. Include traceability tags format
6. Define testing requirements

**Output**: `sdd/tasks/[spec-id]-[task-id].md`

### Step 3: Implementation Planning
**Agent Action**: Plan implementation sequence

**Sequencing Rules**:
1. Setup and configuration first
2. Data models before services
3. Services before controllers
4. Core functionality before advanced features
5. Implementation before testing
6. Unit tests before integration tests

### Step 4: Code Generation
**Agent Action**: Generate code with full traceability

**Implementation Standards**:
- Every class/method includes traceability tags
- Follow constitutional coding standards
- Include comprehensive error handling
- Add proper logging statements
- Implement input validation
- Follow naming conventions

**Traceability Tag Format**:
```java
// @spec: [SPEC-ID]-[TASK-ID] - [Description]
public class UserService {
    
    // @spec: US1-T003 - User validation logic
    public boolean validateUser(User user) {
        // Implementation
    }
}
```

### Step 5: Testing Implementation
**Agent Action**: Generate comprehensive tests

**Testing Requirements**:
1. **Unit Tests**: Test individual methods and classes
2. **Integration Tests**: Test API endpoints and data flow
3. **Edge Case Tests**: Test boundary conditions
4. **Error Scenario Tests**: Test exception handling
5. **Performance Tests**: Validate response times

**Test Traceability**:
```java
// @spec: US1-T003 - Test user validation logic
@Test
void testValidateUser_ValidInput_ReturnsTrue() {
    // Test implementation
}
```

### Step 6: Quality Validation
**Agent Action**: Validate implementation against standards

**Quality Checks**:
- [ ] All code has traceability tags
- [ ] Constitutional compliance verified
- [ ] Error handling implemented
- [ ] Input validation present
- [ ] Logging statements added
- [ ] Test coverage adequate
- [ ] Documentation complete

### Step 7: Integration Verification
**Agent Action**: Verify all components work together

**Integration Points**:
- Database connectivity
- API endpoint functionality
- Service layer integration
- Error handling flow
- Authentication/authorization
- Logging and monitoring

### Step 8: Documentation Generation
**Agent Action**: Generate final documentation

**Documentation Types**:
1. **API Documentation**: OpenAPI/Swagger specs
2. **Code Documentation**: Javadoc for public methods
3. **Setup Documentation**: Installation and configuration
4. **Usage Examples**: Sample API calls and responses

## Quality Gates

### Implementation Quality
- [ ] All tasks completed successfully
- [ ] Code follows constitutional standards
- [ ] Traceability tags present on all code
- [ ] Error handling comprehensive
- [ ] Input validation implemented
- [ ] Logging statements appropriate

### Testing Quality
- [ ] Unit test coverage ≥ 80%
- [ ] Integration tests cover all endpoints
- [ ] Edge cases tested
- [ ] Error scenarios tested
- [ ] Performance requirements met

### Documentation Quality
- [ ] API documentation complete
- [ ] Code documentation adequate
- [ ] Setup instructions clear
- [ ] Usage examples provided

## Task Implementation Order

### Phase 1: Foundation
1. **T001**: Project setup and configuration
2. **T002**: Database schema and entities
3. **T003**: Repository layer implementation

### Phase 2: Business Logic
4. **T004**: Service layer implementation
5. **T005**: Business validation logic
6. **T006**: Error handling framework

### Phase 3: API Layer
7. **T007**: Controller implementation
8. **T008**: Request/response DTOs
9. **T009**: API documentation

### Phase 4: Testing
10. **T010**: Unit test implementation
11. **T011**: Integration test implementation
12. **T012**: Performance testing

## Traceability Management

### Tag Format Standards
```java
// @spec: [SPEC-ID]-[TASK-ID] - [Brief description]
```

### Examples
```java
// @spec: US1-T003 - User entity definition
@Entity
public class User {
    // Implementation
}

// @spec: US1-T005 - User validation service
@Service
public class UserValidationService {
    
    // @spec: US1-T005 - Email format validation
    public boolean isValidEmail(String email) {
        // Implementation
    }
}

// @spec: US1-T007 - User management controller
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    // @spec: US1-T007 - Create user endpoint
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserRequest request) {
        // Implementation
    }
}
```

## Error Handling

### Implementation Failures
**Condition**: Code generation fails or doesn't meet standards
**Action**: Regenerate with corrections, don't proceed until compliant

### Testing Failures
**Condition**: Tests fail or coverage insufficient
**Action**: Fix implementation and/or add more tests

### Integration Issues
**Condition**: Components don't integrate properly
**Action**: Review interfaces and fix integration points

## Success Criteria

- All tasks completed and documented
- Code implementation meets constitutional standards
- Full traceability from spec to code
- Comprehensive testing with adequate coverage
- Integration verification successful
- Documentation complete and accurate

## Workflow Outputs

1. **Task Documents**: `sdd/tasks/[spec-id]-[task-id].md`
2. **Source Code**: Fully implemented with traceability tags
3. **Test Suite**: Comprehensive unit and integration tests
4. **Documentation**: API docs, setup guides, usage examples
5. **Traceability Matrix**: Clear mapping from requirements to code

## Example Implementation Flow

### Task Creation
```
Breaking down plan P-US1 into implementation tasks...

Generated Tasks:
- US1-T001: Project setup and dependencies
- US1-T002: User entity and repository
- US1-T003: User service implementation
- US1-T004: User controller and API endpoints
- US1-T005: Input validation and error handling
- US1-T006: Unit and integration tests
- US1-T007: API documentation
- US1-T008: Configuration and deployment

Starting implementation with US1-T001...
```

### Code Generation
```
Implementing US1-T002: User entity and repository...

Generated files:
- src/main/java/com/example/entity/User.java
- src/main/java/com/example/repository/UserRepository.java
- src/test/java/com/example/repository/UserRepositoryTest.java

All code includes traceability tags linking to US1-T002.
Moving to next task: US1-T003...
```

---

**Workflow Version**: 1.0
**Last Updated**: [Date]
**Next Review**: [Date]