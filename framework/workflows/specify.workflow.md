# Specification Workflow

## Workflow ID: `/speckit.specify`

## Purpose
This workflow guides the agent through the specification creation process, ensuring all requirements are captured systematically.

## Trigger Conditions
- User requests new feature development
- User asks to "build", "create", "implement", or "add" functionality
- Any request that would result in code changes

## Workflow Steps

### Step 1: Requirement Gathering
**Agent Action**: Ask clarifying questions to understand the requirement

**Required Information**:
- What problem are we solving?
- Who are the users?
- What are the key use cases?
- What are the success criteria?
- Are there any constraints or limitations?

**Template Questions**:
```
I need to follow the SDD process and create a proper specification first.

To create a comprehensive spec, I need to understand:

1. **Problem Statement**: What specific problem are we solving?
2. **Target Users**: Who will use this feature?
3. **Key Use Cases**: What are the main scenarios this should support?
4. **Success Metrics**: How will we measure success?
5. **Constraints**: Are there any technical, business, or time constraints?
6. **Dependencies**: Does this depend on any existing systems or features?

Please provide details for these areas so I can create a proper specification.
```

### Step 2: Specification Creation
**Agent Action**: Create specification document using template

**Process**:
1. Generate unique Spec ID (format: US[number] for user stories, F[number] for features)
2. Use `spec.template.md` as the base structure
3. Fill in all sections based on gathered requirements
4. Include detailed acceptance criteria
5. Define API specifications if applicable
6. Specify data models and validation rules

**Output**: `sdd/specs/[spec-id].md`

### Step 3: Specification Review
**Agent Action**: Present specification for review

**Review Process**:
1. Display completed specification
2. Highlight key requirements and assumptions
3. Ask for confirmation or changes
4. Update specification based on feedback
5. Mark status as "Approved" when confirmed

### Step 4: Planning Trigger
**Agent Action**: Automatically trigger planning workflow

**Transition**:
- Once specification is approved, trigger `/speckit.plan`
- Pass specification reference to planning workflow
- Ensure continuity between specification and plan

## Quality Gates

### Specification Completeness
- [ ] All template sections are filled
- [ ] Acceptance criteria are specific and testable
- [ ] Success metrics are defined
- [ ] Dependencies are identified
- [ ] Constraints are documented

### Stakeholder Approval
- [ ] Requirements are clearly understood
- [ ] Scope is well-defined
- [ ] Assumptions are validated
- [ ] Acceptance criteria are agreed upon

## Error Handling

### Incomplete Information
**Condition**: User provides insufficient details
**Action**: Ask specific follow-up questions, don't proceed until complete

### Scope Creep
**Condition**: Requirements keep expanding during specification
**Action**: Document additional requirements as separate specs

### Conflicting Requirements
**Condition**: Requirements contradict each other
**Action**: Highlight conflicts and request clarification

## Example Workflow Execution

### User Request
```
"I need to build a user management API"
```

### Agent Response
```
I need to follow the SDD process and create a proper specification first.

To create a comprehensive spec for the user management API, I need to understand:

1. **Problem Statement**: What specific user management challenges are we solving?
2. **Target Users**: Who will consume this API (frontend apps, other services)?
3. **Key Use Cases**: What operations should be supported (CRUD, authentication, etc.)?
4. **Success Metrics**: What defines a successful user management system?
5. **Constraints**: Any security, performance, or integration requirements?
6. **Dependencies**: Does this integrate with existing auth systems or databases?

Please provide details for these areas so I can create a proper specification.
```

### After Information Gathering
```
Based on your requirements, I'm creating specification US1 for the User Management API.

[Generated specification document]

Please review this specification. Does it accurately capture your requirements?
Any changes or additions needed before I proceed to technical planning?
```

## Success Criteria

- Specification document is complete and approved
- All stakeholders understand the requirements
- Scope is clearly defined and bounded
- Planning workflow is successfully triggered
- Traceability is established for future implementation

## Workflow Outputs

1. **Specification Document**: `sdd/specs/[spec-id].md`
2. **Requirement Traceability**: Clear mapping of requirements to acceptance criteria
3. **Planning Trigger**: Automatic initiation of technical planning phase
4. **Stakeholder Sign-off**: Documented approval of requirements

---

**Workflow Version**: 1.0
**Last Updated**: [Date]
**Next Review**: [Date]