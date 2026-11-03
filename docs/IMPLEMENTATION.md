# Generate Version 2 Implementation Plan — Recipe Manager

Act as a Senior Software Architect and Technical Project Manager.

The project owner has reviewed the Version 1 prototype design and approved moving to the Version 2 planning phase.

Your current mission is to analyze the complete project documentation and the approved prototype, then create a detailed and actionable:

`docs/implementation.md`

This document will serve as the technical blueprint for implementing the final Recipe Manager application (Version 2).

**Important: Your mission is to prepare the implementation plan only. Do not start Version 2 development.**

## 1. Read and Analyze All Project Sources

Before writing anything, inspect the repository and read the following files:

- `AGENTS.md`
- `docs/SPEC.md`
- `docs/LOCK.md`
- `docs/TEST.md`
- `docs/design.md`
- All relevant files inside `prototype/`

Also inspect any other existing project documentation, architecture documents, README files, configuration files, and relevant assets.

Do not assume that the documentation is complete or consistent.

Treat the documents according to their authority:

- `AGENTS.md`: agent behavior, permissions, and development workflow.
- `docs/SPEC.md`: approved product requirements and functional scope.
- `docs/LOCK.md`: authoritative locked decisions.
- `docs/TEST.md`: testing requirements and verification strategy.
- `docs/design.md`: approved Version 1 visual and UX design.
- `prototype/`: actual implemented visual reference.

If two authoritative sources conflict, identify the conflict explicitly. Do not silently choose one or invent a decision.

## 2. Understand the Approved Prototype

Inspect the actual Version 1 prototype, not just its documentation.

Analyze:

- Every implemented page and screen.
- Public Frontoffice.
- User Backoffice.
- Admin Backoffice.
- Navigation and information architecture.
- Layouts and reusable components.
- Forms and interactions.
- Authentication UI.
- Responsive behavior.
- Visual identity, colors, typography, spacing, and design system.
- Animations and visual effects, if present.
- Images, icons, and other assets.

The final Version 2 frontend must reproduce and implement the approved prototype design as closely as practical.

Do not redesign the application or introduce a different visual identity.

Create a mapping between the prototype pages/components and their intended Angular implementation.

If a required feature from `docs/SPEC.md` is not represented in the prototype, identify it and explain how it should be implemented without changing the approved product requirements.

## 3. Version 2 Technology Stack

Use the following intended technology stack:

### Backend

- Java 25
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- REST APIs
- Swagger / OpenAPI

### Frontend

- Angular
- TypeScript
- Tailwind CSS
- PrimeNG
- GSAP and/or Motion for animations

### Infrastructure and Integrations

- Redis for caching and shared rate-limit state.
- SMTP for email.
- Jenkins for CI.
- Local filesystem for image storage, with image metadata and paths stored in PostgreSQL.
- Daily log rotation with 7-day retention.

Respect the approved decisions in `docs/LOCK.md`.

If any technology above is not yet formally represented in `LOCK.md`, identify the required documentation update instead of silently modifying locked decisions.

Do not replace the intended stack with alternatives without explicit approval.

Do not install dependencies, modify package files, access the network, or perform environment setup during this planning task.

## 4. Architecture and Technical Design

Create a detailed technical architecture covering:

### 4.1 Overall Architecture

Explain:

- Angular frontend architecture.
- Spring Boot backend architecture.
- PostgreSQL database architecture.
- Frontend/backend communication.
- Authentication and authorization flow.
- External integrations.
- File and image storage.
- Redis usage.
- Logging and monitoring approach.

Include architecture diagrams using Mermaid where useful.

### 4.2 Backend Structure

Define the proposed package and folder organization, including:

- config
- controller
- service
- repository
- entity
- dto/request
- dto/response
- mapper
- helper
- annotation
- validation
- exception
- security

Explain the responsibilities and boundaries of each layer.

Use:

- Controllers for HTTP requests and responses.
- Services for business logic.
- Repositories for persistence.
- DTOs for API contracts.
- Mappers for entity/DTO conversion.
- Appropriate transactional boundaries.

Avoid putting business logic inside controllers, entities, or generic helper classes.

### 4.3 Frontend Structure

Define a maintainable Angular structure covering:

- Core services.
- Authentication.
- Guards.
- HTTP interceptors.
- Shared components.
- Models and types.
- Pipes and directives.
- Feature modules or feature-oriented standalone components.
- Layouts.
- Assets.
- Styles.
- Environment configuration.

Explain how Tailwind CSS and PrimeNG will coexist.

Define reusable components and how the frontend will reproduce the approved prototype design.

Explain how GSAP and/or Motion will be used without duplicating animation responsibilities.

### 4.4 Database Design

Define the proposed database entities, relationships, and constraints based on `docs/SPEC.md`.

Include:

- Entity responsibilities.
- Primary and foreign keys.
- Relationships and cardinalities.
- Unique constraints.
- Required fields.
- Indexes.
- Enums and status fields.
- Audit/history requirements where specified.
- Migration strategy.

Provide an ER diagram using Mermaid.

Do not invent business rules that are not approved.

### 4.5 API Design

Define the API structure by feature.

For each endpoint, specify where possible:

- HTTP method.
- URL.
- Purpose.
- Authentication requirement.
- Required role or permission.
- Request DTO.
- Response DTO.
- Validation.
- Expected error cases.

Cover all approved product features.

Avoid creating endpoints for features not included in the specification.

## 5. Functional Implementation Mapping

Translate the complete `docs/SPEC.md` into implementation modules and tasks.

Include all approved functionality, including:

- Public Frontoffice.
- Authentication and account management.
- Recipe management.
- User dashboard.
- Private tasks.
- Blog management.
- Community interactions.
- Meal planner.
- Shopping list.
- User profile and settings.
- Subscription management.
- Admin dashboard.
- User management.
- Content moderation.
- Categories and tags.
- Reports.
- Platform activity and statistics.

For every module, describe:

1. Functional objective.
2. Frontend pages and components.
3. Backend controllers and services.
4. Database entities involved.
5. API endpoints.
6. Authorization requirements.
7. Validation and business rules.
8. Required tests.
9. Dependencies on other modules.

Do not omit features simply because they were not fully represented in the prototype.

## 6. Security Architecture

Define the implementation approach for:

- Guest, User, and Admin roles.
- Backend-enforced authorization.
- User data isolation.
- Secure authentication and sessions.
- Password hashing.
- Social Login / OAuth2 / OIDC.
- Secure account linking.
- CSRF and CORS where applicable.
- Input validation and output handling.
- Secure image uploads.
- Rate limiting: 5 requests per 60 seconds per IP.
- Trusted proxy configuration.
- Redis-backed shared rate-limit state.
- Secret and credential management.
- Safe logging.

Respect the rule that Admin must not automatically gain access to private user task details.

For Social Login, preserve the approved authentication UI and identify any unresolved provider decisions. Do not invent provider integrations or credentials.

## 7. Infrastructure and Integrations

Document the implementation approach for:

- PostgreSQL.
- Redis.
- SMTP.
- Local image storage.
- Swagger / OpenAPI.
- Jenkins CI.
- Daily logs with 7-day retention.

Explain configuration requirements, failure handling, testing, and security considerations.

Do not include deployment automation or backup/recovery implementation if these are outside the approved scope.

## 8. Testing Strategy

Use `docs/TEST.md` as the authoritative testing strategy.

Map testing requirements to implementation modules.

Include:

- Unit tests.
- Repository/database tests.
- Integration tests.
- API tests.
- Angular component tests.
- Frontend service tests.
- Authentication and authorization tests.
- Security tests.
- End-to-end tests.
- Visual regression tests.

Explain how test data and test environments will be isolated.

Respect the TDD workflow:

1. Write the test.
2. Verify that it fails for the expected reason.
3. Implement the minimum required behavior.
4. Verify that it passes.
5. Refactor.
6. Run regression tests.

Do not weaken or remove tests to make the project pass.

## 9. Implementation Roadmap

Create a detailed implementation roadmap divided into logical phases.

For each phase, include:

- Phase objective.
- Tasks and unique task IDs.
- Dependencies.
- Backend work.
- Frontend work.
- Database work.
- Testing requirements.
- Acceptance criteria.
- Risks and approval gates.

Respect the task-by-task workflow defined in `AGENTS.md`.

Each task must be independently understandable and verifiable.

Do not begin implementation during this task.

## 10. Code Quality and Formatting

Define mandatory code quality rules:

- Never put entire components, functions, or code blocks on one line.
- Use proper indentation and line breaks.
- Keep files readable and consistently formatted.
- Use clear naming conventions.
- Keep components focused and reusable.
- Avoid duplicated business logic.
- Avoid unnecessary abstractions.
- Keep frontend and backend responsibilities separated.
- Follow the conventions of Java, Spring Boot, Angular, TypeScript, HTML, and CSS.

## 11. Dependencies and Environment Setup

List the required tools, frameworks, and dependencies for Version 2.

For each dependency, explain:

- Its purpose.
- Which part of the application uses it.
- Whether it is mandatory or optional.
- Any compatibility considerations.

Do not invent exact version numbers.

Exact dependency versions must be verified for compatibility before setup, and any required network access or installation must follow the explicit approval rules in `AGENTS.md`.

Do not install anything now.

## 12. Unresolved Decisions and Risks

Create a dedicated section listing:

- Conflicts between project documents.
- Missing product decisions.
- Missing technical decisions.
- Unclear API contracts.
- Security concerns.
- Infrastructure blockers.
- Dependencies requiring approval.
- Any prototype behavior that cannot be translated directly into the final application.

For every unresolved item, include:

- Description.
- Why it matters.
- Affected modules.
- Required decision or approval.

Do not silently resolve an unresolved product decision.

## 13. Final Document Requirements

Create:

`docs/implementation.md`

The document must be:

- Detailed and technically actionable.
- Consistent with the approved prototype.
- Traceable to `docs/SPEC.md`.
- Consistent with `docs/LOCK.md`.
- Aligned with `docs/TEST.md`.
- Compatible with `AGENTS.md`.
- Clear enough for the development agent or subagents to implement Version 2 task by task.

Include Mermaid diagrams, tables, folder structures, API mappings, database design, and implementation checklists where useful.

## 14. Strict Scope and Approval Gate

This task authorizes you to:

- Inspect project files.
- Analyze the prototype.
- Analyze project documentation.
- Create `docs/implementation.md`.

This task does NOT authorize you to:

- Implement Version 2.
- Create backend or frontend application code.
- Install dependencies.
- Modify package configurations.
- Run database creation or migrations.
- Perform network operations.
- Modify `docs/SPEC.md` or `docs/LOCK.md` to silently approve new decisions.
- Change the approved prototype design.

After creating `docs/implementation.md`:

1. Review it for completeness and consistency.
2. Verify that every approved feature is covered.
3. Verify that the prototype design is mapped to the final frontend.
4. Verify that no locked decision has been silently changed.
5. Report unresolved decisions and blockers.
6. Provide the exact file path and a summary of its contents.

## Mandatory Task Execution and Git Workflow

The implementation roadmap must strictly follow `AGENTS.md`.

For every implementation task:

1. Read the relevant requirements in `SPEC.md`, `LOCK.md`, `TEST.md`, and `implementation.md`.
2. Create a dedicated branch for the task. Never work directly on `main`.
3. Implement only the scope of that task.
4. Follow TDD:
   - Write tests before implementation.
   - Verify the tests fail for the expected reason.
   - Implement the required behavior.
   - Verify tests pass.
   - Refactor and run regression tests.
5. Test every meaningful function and business behavior, including validation, edge cases, and error handling. Do not create meaningless tests for simple getters, setters, or private implementation details.
6. Run all relevant tests for the task.
7. Request an independent read-only Code Review Subagent.
8. Address review findings and rerun affected tests.
9. Verify the final task state and record actual command outputs and test evidence.
10. Commit the completed task with a clear, task-specific commit message.
11. Report the branch name, commit hash, tests executed, results, review findings, and remaining blockers.

Do not start the next task until the current task is completed and verified.

Never push or merge without explicit user approval.

The implementation roadmap must make these steps mandatory for every task, not merely describe them as general recommendations.

**STOP after generating and verifying `docs/implementation.md`.**

Wait for my explicit approval of this document before starting any Version 2 implementation.