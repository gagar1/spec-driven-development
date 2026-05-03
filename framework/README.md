# SDD Framework Documentation

## Core Philosophy

The Spec-Driven Development (SDD) framework enforces a strict workflow to prevent common AI agent pitfalls:

### The Three Pillars

1. **Memory Externalization**: All context is stored in version-controlled markdown files
2. **Rigid Templates**: Non-negotiable structure prevents hallucination
3. **State Machine Workflow**: Forces proper design before implementation

## Framework Components

### Global State (Agent's Brain)
- `constitution.md` - Architectural laws and constraints
- `mandate.md` - System rules the agent must follow
- `workflows/` - Predefined process flows

### Templates (Framework Law)
- `spec.template.md` - Specification structure
- `plan.template.md` - Technical planning format
- `task.template.md` - Implementation task format

### Local State (Output)
- `specs/` - Generated specifications
- `plans/` - Technical implementation plans
- `tasks/` - Broken down implementation tasks

## The SDD State Machine

```
[Request] → [Specify] → [Plan] → [Implement]
    ↑          │         │         │
    └──────────┴─────────┴─────────┘
         Validation & Approval Required
```

## Usage

1. **Initialization**: Agent reads `mandate.md` and `constitution.md`
2. **Interception**: Agent refuses direct code requests
3. **Specification**: Generates `spec.md` using template
4. **Planning**: Creates `plan.md` with architecture validation
5. **Implementation**: Breaks down into tasks with traceability tags

## Traceability System

Every line of code includes tags linking back to specifications:

```java
// @spec: US1-T003 - User validation logic
public boolean validateUser(User user) {
    // Implementation with clear traceability
}
```

## Benefits

- **Consistency**: Same process every time
- **Traceability**: Every code line traces to requirements
- **Quality**: Forced design review before implementation
- **Memory**: Persistent context across sessions
- **Collaboration**: Clear documentation for team review