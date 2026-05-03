# SDD Mandate - Agent System Rules

## CRITICAL: Agent Behavioral Laws

**The agent MUST follow these rules without exception:**

### Rule 1: No Direct Implementation
- **NEVER** write production code without going through the SDD workflow
- **ALWAYS** intercept requests for immediate code generation
- **FORCE** the specification process for any new feature request

### Rule 2: Mandatory Workflow Sequence
1. **Specify First**: Generate `spec.md` using the specification template
2. **Plan Second**: Create `plan.md` with architectural validation
3. **Implement Last**: Break down into traceable tasks

### Rule 3: Template Compliance
- **MUST** use provided templates for all documents
- **CANNOT** deviate from template structure
- **REQUIRED** to fill all template sections

### Rule 4: Constitutional Validation
- **ALWAYS** check plans against `constitution.md`
- **REJECT** any plan that violates architectural constraints
- **REQUIRE** approval before proceeding to implementation

### Rule 5: Traceability Enforcement
- **EVERY** line of code must include traceability tags
- **FORMAT**: `// @spec: [SPEC-ID]-[TASK-ID] - [Description]`
- **EXAMPLE**: `// @spec: US1-T003 - User validation logic`

### Rule 6: State Management
- **READ** mandate.md and constitution.md at session start
- **STORE** all context in version-controlled files
- **REFERENCE** existing specs/plans before creating new ones

## Workflow Triggers

### When User Says:
- "Build a new feature"
- "Create an API"
- "Add functionality"
- "Implement X"

### Agent Must Respond:
1. "I need to follow the SDD process"
2. "Let me start with specification"
3. Trigger `/speckit.specify` workflow

## Enforcement Mechanisms

- **No exceptions** for "quick fixes"
- **No shortcuts** for "simple changes"
- **Always** follow the complete workflow
- **Validate** against constitution before implementation

## Success Criteria

- All code has traceability tags
- All features have corresponding specs
- All implementations follow approved plans
- All changes are documented and version-controlled

---

**This mandate is non-negotiable. Violation of these rules compromises the entire SDD framework.**