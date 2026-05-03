# SDD Framework Demo Guide

## 🚀 Quick Start

This project demonstrates a complete **Spec-Driven Development (SDD)** framework implementation with a Spring Boot REST API for user management.

### Prerequisites
- Java 17+
- Gradle 8.x (or use included wrapper)
- IDE with Spring Boot support (IntelliJ IDEA recommended)

### Run the Application

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun
```

The application will start on http://localhost:8080

## 📚 SDD Framework Overview

### What is Spec-Driven Development?

SDD solves three fundamental AI agent limitations:

1. **Context Limit (Amnesia)**: Externalizes memory into permanent, version-controlled files
2. **Hallucination Limit (Unpredictability)**: Provides rigid templates and architectural guardrails
3. **Execution Bias (Action over Design)**: Enforces strict workflow: Specify → Plan → Implement

### The SDD Workflow

```
[User Request] → [Specify] → [Plan] → [Implement]
      ↑             │         │         │
      └─────────────┴─────────┴─────────┘
           Validation & Approval Required
```

## 🏗️ Framework Architecture

### Directory Structure

```
sdd/
├── framework/           # SDD Framework Core
│   ├── mandate.md      # Agent behavioral rules
│   ├── constitution.md # Architectural laws
│   ├── templates/      # Document templates
│   └── workflows/      # Process definitions
├── specs/              # Generated specifications
├── plans/              # Technical implementation plans
├── tasks/              # Detailed implementation tasks
└── src/                # Source code with traceability
```

### Key Components

1. **Global State (Agent's Brain)**
   - `mandate.md` - Non-negotiable agent rules
   - `constitution.md` - Architectural constraints
   - `workflows/` - Process definitions

2. **Templates (Framework Law)**
   - `spec.template.md` - Specification structure
   - `plan.template.md` - Technical planning format
   - `task.template.md` - Implementation task format

3. **Local State (Output)**
   - `specs/` - Requirements and acceptance criteria
   - `plans/` - Technical architecture and design
   - `tasks/` - Implementation breakdown

## 🔍 Traceability System

### Every Line of Code is Traceable

The SDD framework ensures complete traceability from requirements to implementation:

```java
// @spec: US1-T003 - User validation logic
public boolean validateUser(User user) {
    // @spec: US1-T003 - Email format validation
    return isValidEmail(user.getEmail());
}
```

### Traceability Format
- `US1` - Specification ID (User Story 1)
- `T003` - Task ID (Task 003)
- Description - Brief explanation of the code's purpose

## 🌟 Demo Features

### 1. Complete User Management API

**Endpoints:**
- `POST /api/users` - Create user
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users` - List all users (paginated)
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Soft delete user
- `GET /api/users/search?namePattern=john` - Search users
- `GET /api/users/stats` - User statistics

### 2. Interactive API Documentation

Visit: http://localhost:8080/swagger-ui.html

### 3. Database Console (Development)

Visit: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

### 4. Health Monitoring

Visit: http://localhost:8080/actuator/health

## 🧪 Testing the API

### Create a User

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "SecurePass123"
  }'
```

### Get All Users

```bash
curl http://localhost:8080/api/users
```

### Update a User

```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe",
    "email": "jane.doe@example.com"
  }'
```

### Search Users

```bash
curl "http://localhost:8080/api/users/search?namePattern=john"
```

## 📖 SDD Documentation Deep Dive

### 1. Specification Example
See: `sdd/specs/US1-user-management-api.md`
- Complete requirements definition
- Acceptance criteria
- API specifications
- Data models
- Non-functional requirements

### 2. Technical Plan Example
See: `sdd/plans/P-US1-user-management-plan.md`
- Architecture design
- Technology selection
- Implementation approach
- Risk assessment
- Task breakdown

### 3. Task Example
See: `sdd/tasks/US1-T001-project-setup.md`
- Detailed implementation steps
- Success criteria
- Testing requirements
- Progress tracking

## 🔧 SDD Framework Usage

### For AI Agents

1. **Read the Mandate**: Always start by reading `sdd/framework/mandate.md`
2. **Follow the Workflow**: Never skip the Specify → Plan → Implement sequence
3. **Use Templates**: All documents must follow the provided templates
4. **Validate Against Constitution**: Check all plans against `constitution.md`
5. **Add Traceability**: Every line of code must include traceability tags

### For Developers

1. **Review Specifications**: Understand requirements before coding
2. **Follow the Plan**: Implement according to the technical plan
3. **Maintain Traceability**: Keep traceability tags updated
4. **Update Documentation**: Keep specs and plans current

## 🎯 Key Benefits Demonstrated

### 1. Complete Traceability
- Every requirement traces to code
- Every code line traces back to requirements
- Clear audit trail for changes

### 2. Consistent Quality
- Architectural constraints enforced
- Code standards maintained
- Testing requirements defined

### 3. Comprehensive Documentation
- Self-documenting codebase
- Living documentation that stays current
- Clear specifications for team collaboration

### 4. Predictable Development
- Structured workflow prevents scope creep
- Clear success criteria for each phase
- Risk identification and mitigation

## 🔄 Extending the Demo

### Adding New Features

1. **Create Specification**: Use `sdd/framework/templates/spec.template.md`
2. **Generate Plan**: Use `sdd/framework/templates/plan.template.md`
3. **Break Down Tasks**: Use `sdd/framework/templates/task.template.md`
4. **Implement with Traceability**: Add traceability tags to all code

### Example: Adding User Roles

1. Create `sdd/specs/US2-user-roles.md`
2. Create `sdd/plans/P-US2-user-roles-plan.md`
3. Create task files in `sdd/tasks/`
4. Implement with proper traceability tags

## 🚨 Common Pitfalls to Avoid

1. **Skipping Specification**: Never start coding without a spec
2. **Missing Traceability**: Every code line needs traceability tags
3. **Ignoring Constitution**: All plans must comply with architectural rules
4. **Incomplete Documentation**: Keep all documents updated
5. **Breaking the Workflow**: Always follow Specify → Plan → Implement

## 📞 Support and Resources

- **Framework Documentation**: `sdd/framework/README.md`
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Database Console**: http://localhost:8080/h2-console
- **Health Checks**: http://localhost:8080/actuator/health

## 🎉 Conclusion

This demo showcases how the SDD framework transforms chaotic development into a structured, traceable, and predictable process. The framework ensures:

- **Quality**: Architectural constraints prevent poor decisions
- **Traceability**: Complete audit trail from requirements to code
- **Consistency**: Standardized templates and processes
- **Collaboration**: Clear documentation for team coordination
- **Maintainability**: Living documentation that evolves with code

The SDD framework is particularly powerful for AI-assisted development, providing the guardrails and structure needed to harness AI capabilities while maintaining quality and control.

---

**Happy Coding with SDD! 🚀**