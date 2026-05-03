# Planning Workflow

## Workflow ID: `/speckit.plan`

## Purpose
This workflow guides the agent through technical planning, ensuring architectural compliance and implementation feasibility.

## Trigger Conditions
- Automatically triggered after specification approval
- Manual trigger for technical planning reviews
- Architecture change requests

## Prerequisites
- Approved specification document exists
- Constitutional constraints are loaded
- Architecture guidelines are understood

## Workflow Steps

### Step 1: Specification Analysis
**Agent Action**: Analyze approved specification for technical requirements

**Analysis Points**:
- Functional requirements extraction
- Non-functional requirements identification
- Data model requirements
- API endpoint specifications
- Integration requirements
- Performance requirements

### Step 2: Architecture Design
**Agent Action**: Design technical architecture

**Design Elements**:
1. **System Architecture**: High-level component design
2. **Data Architecture**: Database schema and relationships
3. **API Architecture**: Endpoint design and contracts
4. **Security Architecture**: Authentication and authorization
5. **Integration Architecture**: External system interfaces

**Constitutional Validation**:
- Check against Clean Architecture principles
- Validate dependency injection usage
- Ensure proper layering
- Verify security requirements
- Confirm testing strategy

### Step 3: Technology Selection
**Agent Action**: Select appropriate technologies and frameworks

**Selection Criteria**:
- Project requirements alignment
- Team expertise
- Performance requirements
- Security considerations
- Maintenance overhead
- Community support

**Required Technologies** (Spring Boot Project):
- Spring Boot (Web, Data JPA, Security)
- Database (H2 for dev, PostgreSQL for prod)
- Testing (JUnit, Mockito, TestContainers)
- Build tool (Gradle)
- Documentation (OpenAPI/Swagger)

### Step 4: Implementation Planning
**Agent Action**: Break down implementation into phases and tasks

**Planning Elements**:
1. **Phase Breakdown**: Logical implementation phases
2. **Task Identification**: Specific development tasks
3. **Dependency Mapping**: Task dependencies and order
4. **Effort Estimation**: Time estimates for each task
5. **Risk Assessment**: Technical and schedule risks

### Step 5: Constitutional Compliance Check
**Agent Action**: Validate plan against constitutional requirements

**Compliance Checklist**:
- [ ] Clean Architecture compliance
- [ ] Security requirements met
- [ ] Testing strategy adequate
- [ ] Performance requirements addressed
- [ ] Code quality standards defined
- [ ] Monitoring and observability included

**Automatic Rejection Triggers**:
- Direct database access without repositories
- Missing input validation strategy
- Hardcoded configuration values
- Missing error handling approach
- Inadequate testing coverage

### Step 6: Plan Documentation
**Agent Action**: Create comprehensive technical plan document

**Document Structure**:
1. Use `plan.template.md` as base
2. Fill all required sections
3. Include detailed architecture diagrams (ASCII)
4. Specify exact implementation approach
5. Define clear task breakdown
6. Include risk mitigation strategies

**Output**: `sdd/plans/P-[spec-id].md`

### Step 7: Plan Review and Approval
**Agent Action**: Present plan for stakeholder review

**Review Process**:
1. Display completed technical plan
2. Highlight architectural decisions
3. Explain technology choices
4. Present risk assessment
5. Request approval or changes
6. Update plan based on feedback

### Step 8: Task Generation Trigger
**Agent Action**: Trigger task breakdown workflow

**Transition**:
- Once plan is approved, trigger `/speckit.implement`
- Pass plan reference to implementation workflow
- Ensure task traceability to plan and spec

## Quality Gates

### Architectural Quality
- [ ] Follows Clean Architecture principles
- [ ] Proper separation of concerns
- [ ] Appropriate abstraction levels
- [ ] Clear dependency management
- [ ] Scalable design patterns

### Technical Quality
- [ ] Technology choices are justified
- [ ] Performance requirements addressed
- [ ] Security considerations included
- [ ] Testing strategy comprehensive
- [ ] Monitoring approach defined

### Documentation Quality
- [ ] All plan sections completed
- [ ] Architecture diagrams clear
- [ ] Implementation approach detailed
- [ ] Risks identified and mitigated
- [ ] Task breakdown comprehensive

## Constitutional Validation Rules

### Mandatory Checks
1. **Repository Pattern**: Must use Spring Data repositories
2. **Dependency Injection**: All dependencies via Spring IoC
3. **Input Validation**: Controller-level validation required
4. **Error Handling**: Standardized error response format
5. **Testing**: Unit and integration test strategy
6. **Security**: Authentication and authorization approach
7. **Configuration**: Externalized configuration
8. **Logging**: Structured logging strategy

### Warning Triggers
- Complex methods (>15 lines)
- Large classes (>150 lines)
- Missing documentation
- Synchronous external calls
- Missing performance considerations

## Error Handling

### Constitutional Violations
**Condition**: Plan violates architectural principles
**Action**: Reject plan, highlight violations, request revision

### Incomplete Planning
**Condition**: Missing critical planning elements
**Action**: Request additional information, don't proceed

### Technology Conflicts
**Condition**: Selected technologies conflict with requirements
**Action**: Highlight conflicts, suggest alternatives

## Example Workflow Execution

### Input
```
Approved Specification: US1 - User Management API
```

### Agent Analysis
```
Analyzing specification US1 for technical planning...

Key Requirements Identified:
- RESTful API for user CRUD operations
- Input validation and error handling
- Data persistence with JPA
- Authentication and authorization
- Comprehensive testing

Creating technical plan P-US1...
```

### Constitutional Check
```
Validating plan against constitutional requirements...

✅ Repository pattern for data access
✅ Spring dependency injection
✅ Controller-level validation
✅ Standardized error handling
✅ Comprehensive testing strategy
✅ Security implementation
✅ Externalized configuration
✅ Structured logging

Plan passes all constitutional checks.
```

### Plan Presentation
```
Technical Plan P-US1 completed.

Key Architectural Decisions:
- Spring Boot with Web, Data JPA, Security starters
- H2 database for development, PostgreSQL for production
- Repository pattern with custom validation
- JWT-based authentication
- OpenAPI documentation

Implementation broken into 8 tasks with 2-week timeline.

Please review the complete plan. Approve to proceed with task breakdown?
```

## Success Criteria

- Technical plan is architecturally sound
- All constitutional requirements met
- Implementation approach is clear
- Risks are identified and mitigated
- Task breakdown is comprehensive
- Stakeholder approval obtained

## Workflow Outputs

1. **Technical Plan**: `sdd/plans/P-[spec-id].md`
2. **Architecture Documentation**: Detailed system design
3. **Task Breakdown**: Implementation task list
4. **Risk Assessment**: Technical and schedule risks
5. **Implementation Trigger**: Automatic task generation initiation

---

**Workflow Version**: 1.0
**Last Updated**: [Date]
**Next Review**: [Date]