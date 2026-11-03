# LOCK.md
# Recipe Manager — Locked Decisions

**Document Type:** Project Decision Lock  
**Status:** Active  
**Purpose:** Protect approved project decisions from unauthorized changes.

---

## 1. Purpose and Authority

This document contains the functional, technical, architectural, security, and workflow decisions approved for the Recipe Manager project.

All AI agents, Subagents, and contributors MUST respect these decisions.

### 1.1 Mandatory Rules

- Read `AGENTS.md`, `docs/SPEC.md`, `docs/TEST.md`, and `docs/LOCK.md` before starting any task.
- Treat the decisions in this document as locked.
- Do not silently modify, remove, replace, or reinterpret an approved decision.
- Do not introduce a new technology or architectural approach that conflicts with this document.
- If a requirement appears contradictory, stop and report the conflict.
- If a locked decision prevents implementation, explain the issue and request approval.
- Never consider an implementation convenience sufficient justification to change a locked decision.

### 1.2 Document Responsibilities

| Document | Responsibility |
|---|---|
| `AGENTS.md` | Agent behavior, permissions, and workflow |
| `docs/SPEC.md` | Functional and technical requirements |
| `docs/TEST.md` | Testing strategy and requirements |
| `docs/LOCK.md` | Approved decisions that must not change without authorization |

---

## 2. Project Identity and Technology Stack

### 2.1 Project

- Project name: Recipe Manager.
- Application type: Multi-user SaaS.
- Architecture: Frontoffice + User Backoffice + Admin Backoffice.
- Primary language: English only.

### 2.2 Locked Technology Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot |
| Backend language | Java 25 |
| Frontend | Angular |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Cache and shared rate-limit state | Redis |
| API documentation | Swagger / OpenAPI |
| CI/CD | Jenkins |
| Email | SMTP |
| Image storage | Local filesystem |

The application language is English only. Deployment automation, backup, and recovery are outside the current scope.

### 2.3 Version Policy

- Use stable versions compatible with the approved stack.
- Verify compatibility before selecting versions.
- Do not replace an approved technology with another framework or platform.
- Dependency installation and package modifications require explicit user approval.
- Do not assume a version is approved simply because it is the latest available.

### 2.4 Two-Version Gate

- The project has exactly two versions; the former MVP/V1/Future classification is removed.
- Version 1 is an HTML/CSS UI prototype focused on layout, navigation, responsive design, and user experience. It is not the first functional application release.
- Version 1 requires task-based Git workflow, applicable tests, visual regression testing, independent read-only Code Review, remediation of valid findings, and final verification.
- Explicit user approval of Version 1 is required before Version 2 begins.
- After Version 1 approval, generate `docs/DESIGN.md` with the final approved UI design.
- Generate `docs/IMPLEMENTATION.md` only after Version 1 approval; it must cover Version 2 architecture, phases, tasks, dependencies, testing requirements, and acceptance criteria.
- Explicit user approval of `docs/IMPLEMENTATION.md` is required before Version 2 implementation begins.

---

## 3. Backend Architecture

### 3.1 Mandatory Architecture

The backend MUST follow:

- MVC architecture.
- Controller layer.
- Service layer.
- Repository layer.
- Entity layer.
- DTO layer.
- Mapper layer where appropriate.

Business logic MUST remain in the Service layer, not in controllers or repositories.

### 3.2 Required Organization

The backend must provide dedicated organization for:

- `config/`
- `controller/`
- `service/`
- `repository/`
- `entity/`
- `dto/request/`
- `dto/response/`
- `mapper/`
- `helper/`
- `annotation/`
- `validation/`
- `exception/`
- `security/`

Additional packages are allowed when justified and consistent with the architecture.

### 3.3 DTO Rules

- Use Request DTOs for POST, PUT, and PATCH operations.
- Use Response DTOs for API responses.
- Do not expose JPA entities directly through API responses.
- Apply validation to incoming Request DTOs.
- Keep API contracts separate from persistence models.

### 3.4 Transactions

- Use `@Transactional` for service operations that require atomic transactions.
- Use `@Transactional(readOnly = true)` for appropriate read operations.
- Do not annotate every method blindly.
- Ensure multi-step business operations cannot leave inconsistent data.

### 3.5 Validation

- Validate entity attributes where appropriate.
- Use standard validation annotations whenever possible.
- Use custom annotations and validators only when justified.
- Keep validation responsibilities clear and avoid duplicated business rules.

### 3.6 Helpers

- Keep reusable helper methods in `helper/`.
- Avoid placing business logic in generic helper classes.
- Do not create unnecessary utility classes.

---

## 4. Frontend Architecture

### 4.1 Locked Framework

Angular is the approved frontend framework.

Do not replace Angular with React, Next.js, Vue, or another framework without explicit approval.

### 4.2 Required Organization

The frontend architecture must support:

- `core/services/`
- `core/guards/`
- `core/interceptors/`
- `shared/components/`
- `shared/models/`
- `shared/pipes/`
- `shared/directives/`
- `features/`
- `layouts/`
- `assets/`
- `environments/`
- `styles/`

Feature-specific components, services, and models should remain organized inside their relevant feature modules or folders.

### 4.3 Frontend Rules

- Keep API communication inside services.
- Use guards for route protection.
- Use interceptors for cross-cutting HTTP concerns.
- Use reusable shared components where appropriate.
- Keep models and interfaces organized.
- Validate forms on the frontend and backend.
- Never rely on frontend authorization as the only security mechanism.

---

## 5. Database Decisions

### 5.1 Database and ORM

- PostgreSQL is the only approved relational database.
- Use JPA/Hibernate ORM.
- Represent database records using Java entity classes.
- Use relational constraints and foreign keys.
- Use indexes when justified by query patterns.
- Keep database relationships consistent with the approved data model.

### 5.2 Database Objects

Database functions, procedures, and triggers are allowed when justified.

However:

- Do not create unnecessary database objects.
- Avoid duplicating business logic between Java and PostgreSQL.
- Every custom database function, procedure, and trigger must have appropriate tests and documentation.
- Database changes must be version-controlled through migrations.

### 5.3 Database Operations

Database creation, migrations, destructive operations, and other sensitive database changes require explicit user approval.

### 5.4 Seed Data

- Keep fake and development seed data in the dedicated seed organization.
- Never use real production user data as test data.
- Seed data must not contain real credentials or secrets.

---

## 6. Authentication, Roles, and Authorization

### 6.1 Approved Roles

The application has three roles:

- `GUEST`
- `USER`
- `ADMIN`

### 6.2 Authorization Rules

- Guests can access only public resources and permitted public actions.
- Users can access their own private resources.
- Admins can perform authorized platform-management operations.
- Backend authorization is mandatory.
- Users must never access another user's private tasks or private data.
- Admin access to private task details is not automatically granted.

### 6.3 Security Rules

- Never expose passwords, tokens, API keys, or credentials.
- Never commit secrets to Git.
- Store sensitive configuration in environment variables or an approved secret-management mechanism.
- Validate ownership before accessing or modifying user-owned resources.
- Apply appropriate authentication, authorization, and input validation to protected endpoints.
- Support OAuth/social login through approved external identity providers, including Google and Facebook.
- Use secure OAuth flows, identity verification, account linking, and session handling.
- Never store passwords belonging to external identity providers.

---

## 7. Git and Branching Workflow

### 7.1 Branch Rules

- Never implement directly on `main`.
- Every task must have its own isolated Git branch.
- Branches must be dedicated to their respective tasks.
- Commits must remain specific to the task being implemented.

### 7.2 Git Permissions

The Main Agent may:

- Create branches.
- Switch branches.
- Create commits.
- Make changes inside its authorized task branch.

The Main Agent MUST NOT:

- Push to a remote repository.
- Merge branches.
- Modify protected main-branch history.

`git push` is reserved for the user.

Merging requires explicit user approval.

### 7.3 Task Isolation

- Complete and verify the current task before starting the next task.
- Do not mix unrelated task implementations in one branch.
- Do not include unrelated modifications in a task commit.
- Report unexpected existing changes before modifying them.

---

## 8. Agent and Subagent Rules

### 8.1 Main Agent

The Main Agent is responsible for:

- Understanding the requirements.
- Planning the task.
- Coordinating Subagents.
- Reviewing implementation results.
- Ensuring tests pass.
- Addressing review findings.
- Producing the final task report.

Delegation does not transfer final responsibility.

### 8.2 Code Review Subagent

The Code Review Subagent MUST be independent and read-only.

It may:

- Inspect source code.
- Inspect tests.
- Analyze architecture and security.
- Identify bugs and regressions.
- Produce a review report.

It MUST NOT:

- Edit files.
- Create commits.
- Push branches.
- Merge branches.
- Modify tests or implementation.

### 8.3 Code Review Report

Every review must report:

- Finding description.
- Severity.
- File and line reference.
- Potential impact.
- Recommended correction.

If no findings exist, explicitly report that no findings were identified.

### 8.4 Other Subagents

Specialist Subagents may be used for:

- Backend development.
- Backend testing.
- Frontend development.
- Frontend testing.
- Database work.
- Security analysis.
- Debugging.
- Code review.

All Subagents must follow the same locked decisions and permissions.

---

## 9. Testing and TDD

### 9.1 Testing Approach

TDD is the approved development approach:

1. Write the test.
2. Run the test and confirm the expected failure.
3. Implement the minimum required behavior.
4. Run the test and confirm success.
5. Refactor when appropriate.
6. Run regression tests.
7. Continue to the next meaningful function or behavior.

### 9.2 Test Organization

- Keep tests organized by feature and class.
- Do not put all tests into one giant test file.
- Test meaningful business behavior.
- Test public behavior when private methods are involved.
- Do not create meaningless tests for simple getters, setters, or static configuration.

### 9.3 Test Integrity

Agents MUST NOT:

- Delete tests to make a build pass.
- Skip failing tests without authorization.
- Disable tests to hide failures.
- Weaken assertions without justification and approval.
- Change expected behavior merely to obtain a passing result.
- Claim tests passed if they were not executed.

### 9.4 Completion

A task cannot be declared complete until all required tests have been executed and their results reported.

See `docs/TEST.md` for the complete testing strategy.

---

## 10. API Documentation

- Swagger / OpenAPI is the approved API documentation approach.
- Document relevant endpoints, request parameters, responses, and error cases.
- Keep API documentation consistent with actual API behavior.
- Do not replace Swagger with another documentation solution without approval.

---

## 11. Logging

### 11.1 Logging Requirements

- Maintain a dedicated `logs/` directory.
- Generate daily log files.
- Include sufficient operational context to support production maintenance and debugging.
- Where appropriate, log internal user ID, role, action, timestamp, and request/correlation ID.

### 11.2 Security

Never log:

- Passwords.
- Authentication tokens.
- API keys.
- Credentials.
- Sensitive request payloads.
- Unnecessary personal information.

### 11.3 Log Management

- Logs must have a retention and rotation policy.
- Avoid unlimited log growth.
- Do not silently remove logs needed for an active investigation.

- Retain log files for 7 days.
- Automatically delete log files older than 7 days.
- Apply daily log rotation.
---

## 12. Image Storage

- Store uploaded images on the local filesystem.
- Do not introduce cloud/object storage without approval.
- Store image metadata and storage paths in PostgreSQL.
- Validate file type and size.
- Generate safe storage filenames.
- Prevent path traversal and unauthorized file access.
- Do not trust the uploaded file extension alone.
- Protect stored files from unauthorized access.

---

## 13. Email Service

- SMTP is the approved email delivery mechanism.
- Keep SMTP credentials outside source code.
- Use environment variables or an approved secret-management mechanism.
- Never log SMTP passwords or credentials.
- Do not replace SMTP with another email provider without approval.

---

## 14. Rate Limiting

### 14.1 Locked Configuration

- Maximum: **5 requests**.
- Time window: **60 seconds**.
- Identification: **IP address**.
- Scope: Apply the configured limit per IP address.

### 14.2 Expected Behavior

- Track requests by client IP.
- Enforce the configured limit within the 60-second window.
- Return HTTP `429 Too Many Requests` when the limit is exceeded.
- Use Redis for shared rate-limit state.

### 14.3 Proxy and IP Security

- Configure trusted proxies explicitly.
- Do not blindly trust arbitrary forwarding headers.
- Prevent clients from bypassing limits by supplying forged IP headers.

Do not change the approved limit or identification strategy without explicit approval.

---

## 15. Redis and Caching

- Redis is the approved caching technology.
- Redis is also used for shared rate-limit state.
- PostgreSQL remains the source of truth for persistent application data.
- Define appropriate cache expiration.
- Define invalidation behavior when underlying data changes.
- Never allow private data belonging to one user to leak through shared cache entries.
- Do not replace Redis with another caching system without approval.

---

## 16. CI and Deployment

### 16.1 Continuous Integration

- Jenkins is the approved CI solution.
- CI must execute the relevant build and automated test checks.
- CI must report failures clearly.
- Do not replace Jenkins with another CI system without approval.

### 16.2 Deployment

Production deployment automation is outside the current approved scope.

Do not introduce deployment automation or deploy the application without explicit approval.

---

## 17. Internationalization

- English is the only approved application language.
- Do not introduce Arabic, French, or other application translations in the current scope.
- Do not build an unnecessary translation system.
- Changing the language scope requires explicit approval.

---

## 18. Backup and Recovery

Backup and recovery implementation is not part of the current approved project scope.

Do not introduce backup/recovery features as an assumed requirement.

Any future inclusion must be explicitly approved and documented.

---

## 19. Product and Subscription Decisions

### 19.1 Subscription Model

The approved subscription model includes:

- Free Trial.
- Free.
- Pro.
- Premium.

The proposed free-trial duration is 7 days.

### 19.2 Subscription Operations

The application scope includes:

- Upgrade.
- Downgrade.
- Cancellation.
- Subscription status management.

Real payment processing is optional and requires separate explicit approval. It is not part of the currently approved application scope by default.

---

## 20. Scope Protection

### 20.1 Approved Scope

The functional scope is defined in `docs/SPEC.md` and is delivered through the two approved versions: Version 1 UI Prototype and Version 2 Final Application.

### 20.2 Scope Changes

Any of the following requires explicit user approval:

- Adding a new feature.
- Removing an approved feature.
- Changing an approved business rule.
- Changing the technology stack.
- Changing architecture.
- Changing security requirements.
- Changing the database design in a way that affects approved requirements.
- Changing the approved testing strategy.
- Introducing a new external service or dependency.

### 20.3 Implementation Freedom

Agents may choose reversible implementation details inside the approved scope, provided they do not violate any locked decision.

---

## 21. Approval and Change Management

A locked decision can only be changed after explicit user approval.

When a change is requested:

1. Identify the existing locked decision.
2. Explain the requested change.
3. Explain the reason and technical impact.
4. Identify affected documents, code, tests, and architecture.
5. Wait for explicit approval.
6. Update `docs/LOCK.md` and other affected documents.
7. Record the change in the decision history.

Never interpret silence as approval.

### Decision History

| Version | Status | Description |
|---|---|---|
| 1.0 | Active | Initial approved project decisions |

---

## 22. Final Instructions

Every agent working on Recipe Manager MUST:

- Respect this document.
- Follow `AGENTS.md`.
- Implement only requirements defined in `docs/SPEC.md`.
- Follow the testing strategy in `docs/TEST.md`.
- Ask for approval before changing locked decisions.
- Never push or merge without authorization.
- Never expose or commit secrets.
- Report blockers instead of silently bypassing requirements.
- Provide evidence for completed work.

**A task is not permission to change the project rules.**

**A passing test is not permission to weaken the specification.**

**No locked decision may be changed without explicit user approval.**
