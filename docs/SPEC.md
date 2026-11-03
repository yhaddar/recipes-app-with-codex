# SPEC.md

# Recipe Manager — Technical and Functional Specification

## 1. Project Overview

Recipe Manager is a multi-user SaaS web application that allows users to discover, create, manage, share, and organize recipes.

The application consists of three main areas:

- **Frontoffice:** Public website for exploring recipes, reading blogs, discovering creators, and accessing platform information.
- **User Backoffice:** Authenticated workspace for managing recipes, personal tasks, blogs, community interactions, meal planning, and settings.
- **Admin Backoffice:** Administrative workspace for managing users, content, subscriptions, and platform activity.

The application is created from scratch.

This document defines the approved technology stack, architecture, functional requirements, technical standards, testing strategy, and implementation backlog.

The agent must follow this specification and must not silently introduce features, technologies, or architectural changes.

## 1.1 Two-Version Delivery Strategy

The project has exactly two versions. The former MVP/V1/Future classification is removed.

### Version 1 — UI Prototype

Version 1 is an HTML and CSS UI prototype focused on layout, navigation, responsive design, and user experience. It is not the first functional application release. It follows the task-based Git workflow and requires applicable tests, visual regression testing, an independent read-only Code Review Subagent, remediation of valid findings, and final verification. The user must explicitly approve Version 1 before Version 2 begins.

After that approval, generate `docs/DESIGN.md` with the final approved UI design, components, layouts, navigation, interactions, and responsive behavior.

### Version 2 — Final Application

After Version 1 approval, analyze `docs/DESIGN.md`, `docs/SPEC.md`, `docs/LOCK.md`, and `docs/TEST.md`, then generate `docs/IMPLEMENTATION.md`. That plan must include architecture, implementation phases, tasks, dependencies, testing requirements, and acceptance criteria. Wait for explicit user approval of the plan before starting Version 2.

Version 2 uses Spring Boot, Java 25, Angular, PostgreSQL, JPA/Hibernate, Redis, SMTP, Swagger/OpenAPI, Jenkins, and local filesystem image storage.

---

# 2. Technology Stack

## 2.1 Approved Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot |
| Backend language | Java 25 |
| Frontend | Angular |
| Frontend language | TypeScript |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Cache | Redis |
| API Documentation | Swagger / OpenAPI |
| Email | SMTP |
| Image storage | Local filesystem |
| CI | Jenkins |
| Coding Agent | Codex CLI |

## 2.2 Version Rules

- Java: 25.
- Spring Boot: latest compatible stable version selected and approved before setup.
- Angular: latest stable version selected and approved before setup.
- PostgreSQL: compatible stable version selected and approved before setup.
- Redis: compatible stable version selected and approved before setup.
- TypeScript: version compatible with the selected Angular version.

The agent must verify compatibility before proposing versions.

The agent must not silently change approved versions.

Any dependency installation, update, removal, or package modification requires explicit user approval.

## 2.3 Architecture Separation

- Spring Boot handles business logic, authentication, authorization, validation, and database operations.
- Angular handles presentation, user interaction, frontend routing, and communication with backend APIs.
- PostgreSQL is the persistent relational database.
- Redis is used for caching.
- The backend is the source of truth for business rules and authorization.

Business logic must not be duplicated between Angular and Spring Boot.

Frontend validation does not replace backend validation.

---

# 3. Backend Architecture — Spring Boot

## 3.1 General Architecture

The backend must follow a layered MVC architecture.

Main responsibilities:

- Controller: HTTP requests and responses.
- Service: business logic and application operations.
- Repository: database access.
- Entity: Java objects mapped to database tables.
- DTO: API request and response contracts.
- Mapper: conversion between entities and DTOs.

Controllers must not contain business logic.

Repositories must not contain business workflows.

Services must coordinate business operations and transactions.

## 3.2 Proposed Backend Structure

```text
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── .../
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       ├── entity/
│   │   │       ├── dto/
│   │   │       │   ├── request/
│   │   │       │   └── response/
│   │   │       ├── mapper/
│   │   │       ├── helper/
│   │   │       ├── annotation/
│   │   │       ├── validation/
│   │   │       ├── exception/
│   │   │       ├── security/
│   │   │       ├── storage/
│   │   │       ├── email/
│   │   │       ├── cache/
│   │   │       └── ...
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── db/
│   │       │   ├── migration/
│   │       │   └── seed/
│   │       └── ...
│   └── test/
│       └── java/
└── logs/
```

This is the proposed initial structure. The Main Agent may refine package organization when necessary, but must preserve the approved architecture.

## 3.3 Controller Layer

Controllers must:

- Define REST API endpoints.
- Receive and validate request DTOs.
- Call the appropriate service.
- Return response DTOs.
- Use appropriate HTTP status codes.
- Avoid direct database operations.
- Avoid business logic.

## 3.4 Service Layer

Services must:

- Contain business logic.
- Coordinate repositories.
- Enforce business rules.
- Handle application workflows.
- Manage transactional operations.
- Avoid returning persistence entities directly through public APIs.

## 3.5 Repository Layer

Repositories must:

- Use Spring Data JPA.
- Handle database access.
- Define custom queries only when necessary.
- Support pagination and filtering where required.
- Avoid unnecessary duplicate queries.

## 3.6 Entity and ORM Rules

Use Java JPA entities mapped to PostgreSQL tables.

Entity requirements:

- Define appropriate primary keys.
- Define relationships.
- Define required and optional attributes.
- Apply suitable validation constraints.
- Apply unique and database constraints where necessary.
- Avoid exposing entities directly through API responses.
- Avoid uncontrolled eager loading.
- Prevent circular serialization problems.

Relationships must be explicitly designed and documented.

Use `@OneToOne`, `@OneToMany`, `@ManyToOne`, and `@ManyToMany` only when appropriate.

## 3.7 DTO Architecture

DTOs are mandatory for API communication.

Organize DTOs into:

```text
dto/
├── request/
└── response/
```

Request DTOs:

- Used for POST requests.
- Used for PUT/PATCH requests.
- Must include appropriate validation.
- Must not accept fields that the client is not authorized to modify.

Response DTOs:

- Used for GET responses.
- Used for other API responses when appropriate.
- Must expose only the information needed by the frontend.
- Must not expose sensitive internal fields.

Use dedicated DTOs for different operations when their fields or validation rules differ.

## 3.8 Validation

Use Jakarta Bean Validation.

Examples:

- `@NotNull`
- `@NotBlank`
- `@Email`
- `@Size`
- `@Min`
- `@Max`
- `@Positive`
- `@Pattern`

Use:

- Entity attribute validation.
- Request DTO validation.
- Custom validation annotations when standard validation is insufficient.
- Business validation inside services.

Validation errors must return a consistent API error structure.

## 3.9 Helper Folder

The `helper/` package must contain reusable methods shared by multiple parts of the application.

Rules:

- Do not put business logic in generic helpers.
- Do not create helpers for one-line operations without a good reason.
- Prefer clear, focused classes.
- Avoid large utility classes containing unrelated methods.

## 3.10 Custom Annotations

The `annotation/` package is reserved for custom annotations.

Examples may include:

- Custom validation annotations.
- Reusable security-related annotations.
- Other justified cross-cutting annotations.

Do not create custom annotations when a standard Spring or Jakarta annotation already solves the problem.

## 3.11 Transaction Management

Use Spring's `@Transactional` where transaction boundaries are required.

Rules:

- Apply transactions primarily at the Service layer.
- Use `@Transactional` for operations that must succeed or fail atomically.
- Use `@Transactional(readOnly = true)` for appropriate read operations.
- Ensure related database changes remain consistent.
- Avoid unnecessary transactions.
- Do not apply `@Transactional` blindly to every function.
- Pay attention to transaction propagation and rollback behavior where necessary.

Examples include:

- Creating a recipe and its related ingredients.
- Creating an order and updating related stock, if such a workflow is introduced and approved.
- Updating multiple related records.
- Deleting records with dependent operations.

Every important transactional workflow must have appropriate tests.

## 3.12 Exception Handling

Implement centralized exception handling.

Requirements:

- Consistent error response structure.
- Correct HTTP status codes.
- Clear validation error messages.
- No sensitive information in error responses.
- No exposed stack traces in production responses.
- Proper logging without secrets.

## 3.13 Seed Data

Create a seed-data mechanism for generating fake development and test data.

Requirements:

- Seed users with appropriate roles.
- Seed representative recipes.
- Seed ingredients and related records.
- Seed blog posts.
- Seed community interactions when needed.
- Seed subscription plans.
- Maintain valid relationships.
- Make seeds repeatable and predictable.
- Avoid inserting duplicate data on repeated execution.
- Never use production credentials or real private user data.

Test fixtures must be isolated from production data.

---

# 4. Frontend Architecture — Angular

## 4.1 General Architecture

The frontend must use Angular and TypeScript.

Use Angular's component-based architecture.

The frontend must separate:

- Components.
- Views and pages.
- Services.
- Models and interfaces.
- Guards.
- Interceptors.
- Validators.
- Shared UI components.
- Utilities.

The frontend communicates with Spring Boot through REST APIs.

## 4.2 Proposed Frontend Structure

```text
frontend/
├── src/
│   ├── app/
│   │   ├── core/
│   │   │   ├── services/
│   │   │   ├── guards/
│   │   │   ├── interceptors/
│   │   │   └── ...
│   │   ├── shared/
│   │   │   ├── components/
│   │   │   ├── models/
│   │   │   ├── pipes/
│   │   │   ├── directives/
│   │   │   └── ...
│   │   ├── features/
│   │   │   ├── auth/
│   │   │   ├── recipes/
│   │   │   ├── dashboard/
│   │   │   ├── tasks/
│   │   │   ├── blogs/
│   │   │   ├── community/
│   │   │   ├── meal-planner/
│   │   │   ├── profile/
│   │   │   ├── subscriptions/
│   │   │   └── admin/
│   │   ├── layouts/
│   │   │   ├── public/
│   │   │   ├── dashboard/
│   │   │   └── admin/
│   │   ├── app.routes.ts
│   │   └── ...
│   ├── assets/
│   ├── environments/
│   └── styles/
└── ...
```

The final structure must follow the selected Angular version's recommended conventions.

Feature modules or standalone components may be used according to the chosen Angular architecture.

## 4.3 Components

Components must:

- Handle presentation and user interaction.
- Use reusable UI components where appropriate.
- Keep business logic out of presentation components.
- Handle loading, error, empty, and success states.
- Be accessible and responsive.

## 4.4 Services

Angular services must:

- Communicate with backend APIs.
- Encapsulate reusable frontend logic.
- Handle API requests.
- Avoid duplicating backend business rules.
- Use appropriate dependency injection.

## 4.5 Models and Interfaces

Define TypeScript interfaces and types for API contracts.

Requirements:

- Separate request and response models where necessary.
- Use explicit types.
- Avoid unnecessary `any`.
- Keep API models consistent with backend DTOs.
- Avoid duplicating unrelated models.

## 4.6 Guards and Interceptors

Use route guards for frontend navigation control.

Use HTTP interceptors for shared HTTP behavior where appropriate.

Important:

- Angular guards are not security boundaries.
- Backend authorization remains mandatory.
- Sensitive permissions must always be verified by Spring Boot.

## 4.7 Frontend Validation

Use Angular form validation.

Requirements:

- Validate required fields.
- Validate input formats.
- Display clear error messages.
- Support custom validators when required.
- Handle backend validation errors.
- Keep validation consistent with backend rules.

---

# 5. Database Architecture — PostgreSQL

## 5.1 General Rules

PostgreSQL is the main relational database.

The database must use:

- Primary keys.
- Foreign keys.
- Relationships.
- Unique constraints.
- Check constraints.
- Appropriate indexes.
- Timestamps.
- Referential integrity.

## 5.2 Database Relationships

Define relationships according to the domain.

Examples:

- User to recipes.
- User to tasks.
- User to blog posts.
- Recipe to ingredients.
- Recipe to instructions.
- User to comments.
- User to favorites.
- User to subscriptions.
- Blog posts to categories and tags.

Do not create relationships without a clear business reason.

## 5.3 Database Migrations

Every schema change must be version-controlled through migrations.

Requirements:

- Keep migration files ordered.
- Avoid silently modifying previously applied migrations.
- Preserve existing data.
- Test migrations.
- Document destructive or complex changes.
- Obtain approval before database migrations.

## 5.4 Database Functions, Procedures and Triggers

PostgreSQL functions, stored procedures, and triggers may be used when justified.

Rules:

- Every database function must have a defined purpose.
- Every procedure must have a defined use case.
- Every trigger must have documented behavior.
- Avoid duplicating business logic unnecessarily between Java and PostgreSQL.
- Test all custom database logic.
- Document transaction and error behavior.
- Obtain approval before creating or changing database objects.

Do not create triggers or procedures simply to increase the number of database features.

## 5.5 Database Test Data

Every new table or relationship that requires test data must have suitable fixtures or seed data.

Tests must verify:

- Foreign-key relationships.
- Required fields.
- Unique constraints.
- Check constraints.
- Cascading behavior.
- Transaction rollback.
- Trigger behavior.
- Database function/procedure behavior.

---

# 6. API Documentation — Swagger / OpenAPI

Use Swagger / OpenAPI to document the Spring Boot REST API.

Requirements:

- Document all public API endpoints.
- Document request DTOs and their validation constraints.
- Document response DTOs.
- Document HTTP status codes.
- Document authentication requirements.
- Document authorization requirements.
- Document pagination, filtering, and sorting parameters.
- Document validation and error response formats.
- Group endpoints by functional domain.
- Keep API documentation synchronized with implementation.

The Swagger UI must be available in development.

Production exposure of API documentation must be restricted according to the approved security configuration.

Tests or verification must confirm that the OpenAPI specification can be generated and that important endpoints are documented.

---

# 7. Logging and Monitoring

## 7.1 Logging Architecture

The application must maintain log files inside a `logs/` directory.

The logging system must create separate daily log files.

Example:

```text
logs/
├── application-2026-11-03.log
├── application-2026-11-04.log
└── ...
```

The exact filename pattern may be refined during implementation.

## 7.2 Log Levels

Use appropriate log levels:

- ERROR
- WARN
- INFO
- DEBUG

Development may use DEBUG when necessary.

Production must use an appropriate production logging level, normally INFO, with WARN and ERROR for important problems.

## 7.3 Production User Context

Production logs may contain safe user context to help the owner perform maintenance and investigate problems.

Examples:

- Internal user ID.
- Action performed.
- Timestamp.
- Request correlation ID.
- Relevant resource ID.
- Operation result.
- Error category.

Do not log:

- Passwords.
- Authentication tokens.
- Session identifiers.
- API keys.
- SMTP credentials.
- Database credentials.
- Secrets.
- Sensitive personal information.
- Full request bodies containing private information.

User context must be minimized to what is necessary for maintenance.

## 7.4 Log Management

Requirements:

- Daily log file rotation.
- Retain daily log files for 7 days, then automatically delete files older than 7 days.
- Avoid unlimited log growth.
- Protect log files from unauthorized access.
- Handle logging failures safely.
- Avoid exposing log files through public HTTP routes.
- Use structured logging where appropriate.
- Include enough context to investigate errors without exposing secrets.

## 7.5 Logging Tests

Test:

- Correct log level behavior.
- Daily file configuration.
- Log rotation configuration.
- Secret redaction.
- Sensitive data exclusion.
- Correct user context handling.

---

# 8. Image Storage — Local Filesystem

## 8.1 Storage Decision

Uploaded images must be stored locally on the application's server filesystem.

Do not introduce cloud object storage without explicit approval.

The database must store image metadata and the storage reference/path, not the binary file itself by default.

## 8.2 Storage Requirements

- Use a dedicated storage directory.
- Keep uploaded files outside executable application directories.
- Generate safe unique filenames.
- Do not trust user-provided filenames.
- Validate MIME type and file extension.
- Validate maximum file size.
- Prevent path traversal.
- Restrict permitted image formats.
- Prevent unsafe file execution.
- Protect private files.
- Serve public images through controlled application routes or an approved static-resource configuration.

## 8.3 Image Operations

Support the required image operations:

- Upload.
- Retrieve/display.
- Replace.
- Delete.

Image metadata should include relevant information such as:

- Stored filename.
- Storage path or reference.
- MIME type.
- File size.
- Upload timestamp.
- Owner or related resource.

## 8.4 Storage Error Handling

The application must handle:

- Invalid file formats.
- Files exceeding size limits.
- Missing files.
- Failed writes.
- Failed deletions.
- Unauthorized access.

## 8.5 Image Storage Tests

Test:

- Valid uploads.
- Invalid formats.
- File-size restrictions.
- Safe filenames.
- Path traversal prevention.
- Ownership restrictions.
- File replacement.
- File deletion.
- Missing file behavior.

---

# 9. Email Service — SMTP

## 9.1 SMTP Decision

SMTP is the default email transport.

The application must support configurable SMTP settings.

SMTP credentials and configuration must be provided through environment variables or approved secret configuration.

Never commit credentials to source code.

## 9.2 Email Use Cases

Email may be used for:

- Account verification.
- Password reset.
- Account-related notifications.
- Other explicitly approved application notifications.

## 9.3 Email Templates

Use reusable email templates.

Requirements:

- Clear subject.
- English content.
- Consistent application branding.
- Appropriate links.
- Safe handling of user-provided data.
- No sensitive credentials in email content.

## 9.4 Email Failure Handling

The application must handle:

- SMTP connection failures.
- Authentication failures.
- Delivery errors.
- Invalid recipient addresses.
- Timeouts.

Email failures must not expose SMTP credentials or sensitive information in logs or API responses.

## 9.5 Email Tests

Use a test SMTP server or mock mail sender during automated tests.

Test:

- Verification email generation.
- Password reset email generation.
- Correct recipient.
- Correct template data.
- SMTP failure behavior.
- No real email delivery during ordinary automated tests.

---

# 10. Rate Limiting

The application must implement rate limiting to protect API endpoints from excessive requests and abuse.

## 10.1 Approved Limit

The approved rate limit is **5 requests per IP address within 60 seconds**.

Requirements:

- Track requests using the client's IP address.
- Allow a maximum of 5 requests within a rolling 60-second window.
- Reject additional requests with HTTP 429 Too Many Requests.
- Return a clear error response.
- Use Redis to maintain shared rate-limit state across application instances.
- Expire rate-limit records according to the configured 60-second window.
- Do not trust arbitrary client-provided IP headers.
- Configure trusted proxies explicitly when the application operates behind a reverse proxy.
- Ensure IP extraction cannot be spoofed through untrusted headers.
- Do not expose internal rate-limiting implementation details.

## 10.2 Rate-Limiting Tests

Test:

- Requests below the limit.
- Exactly 5 requests within 60 seconds.
- The 6th request within the same window.
- Requests after the 60-second window expires.
- Different IP addresses having independent limits.
- Shared state across application instances.
- HTTP 429 response behavior.
- IP extraction behind trusted proxies.
- Protection against spoofed forwarding headers.
- Authenticated and unauthenticated requests.
- Reset behavior after the window expires.

---

# 11. Caching — Redis

## 11.1 Redis Decision

Use Redis for application caching.

Redis must not replace PostgreSQL as the persistent source of truth.

## 11.2 Cache Requirements

Use caching only where it provides a clear benefit.

Potential candidates:

- Public recipe listings.
- Public recipe details.
- Public blog content.
- Categories and tags.
- Other frequently accessed public data.

Do not cache sensitive user data across users.

## 11.3 Cache Management

Define:

- Cache keys.
- Time-to-live (TTL).
- Invalidation rules.
- Cache refresh behavior.
- Error behavior when Redis is unavailable.

Cache invalidation must be considered when related database records change.

Examples:

- Updating a recipe must invalidate or refresh the corresponding cached recipe.
- Publishing a blog must update the relevant public blog cache.
- Deleting a category must not leave invalid cached category data.

## 11.4 Redis Failure Handling

The application must handle Redis unavailability safely.

Caching failures must not cause incorrect persistent data or expose private information.

Where appropriate, the application may fall back to PostgreSQL for data retrieval.

## 11.5 Redis Tests

Test:

- Cache hit.
- Cache miss.
- TTL expiration.
- Cache invalidation.
- Cache refresh.
- User data isolation.
- Redis unavailable behavior.
- Correct behavior after database updates.

---

# 12. Internationalization and Language

The application's only approved language is **English**.

Requirements:

- Frontoffice content in English.
- Backoffice content in English.
- Admin interface in English.
- Validation messages in English.
- API-facing error messages in English.
- Email templates in English.
- UI labels and navigation in English.

Do not introduce a translation or multilingual system in the initial scope.

The application should still use a consistent approach for managing text and labels.

---

# 13. CI — Jenkins

## 13.1 CI Decision

Use Jenkins for Continuous Integration.

Deployment is not part of the current scope.

## 13.2 Pipeline Requirements

The Jenkins pipeline should verify:

### Backend

- Compile/build.
- Unit tests.
- Integration tests where configured.
- Relevant quality checks.

### Frontend

- TypeScript compilation.
- Unit/component tests.
- Linting.
- Production build verification.

### General

- Verify the repository structure.
- Produce test reports.
- Report failed stages.
- Preserve useful build logs.
- Never expose secrets in pipeline output.

## 13.3 Jenkins Pipeline Rules

- Pipeline configuration must be version-controlled.
- Pipeline changes must be reviewed.
- Credentials must use Jenkins credential management.
- Secrets must never be written directly into Jenkinsfiles.
- Do not run deployment stages in the current project scope.
- Do not automatically push or merge code.

## 13.4 CI Tests

Verify:

- Successful backend pipeline.
- Successful frontend pipeline.
- Failed test detection.
- Failed build detection.
- Correct report generation.
- Secret masking.
- Pipeline behavior when required environment configuration is missing.

---

# 14. Backup, Recovery and Deployment Scope

## 14.1 Backup and Recovery

Backup and recovery functionality is not required in the current project scope.

Do not implement a dedicated backup/recovery system unless the user approves it later.

## 14.2 Deployment

Deployment is deferred.

The current specification focuses on application development, local development, testing, and Jenkins CI.

Do not implement production deployment infrastructure or deployment automation without a separate approved requirement.

---

# 15. Functional Requirements and Application Features

## 15.1 User Roles

The application supports:

- Guest.
- User.
- Admin.

Role permissions must be enforced by the backend.

## 15.2 Frontoffice

Public features:

- Landing page.
- Navbar and footer.
- Platform introduction.
- Features and pricing.
- Free trial information.
- Recipe exploration.
- Search.
- Filtering.
- Sorting.
- Recipe details.
- Public blogs.
- Creator profiles.
- Legal and informational pages.
- Authentication access.

## 15.3 Authentication

Features:

1. User registration.
2. Email verification.
3. Login.
4. Logout.
5. Password hashing.
6. Forgot password.
7. Password reset.
8. Authentication state.
9. Session/token management.
10. Role-based authorization.
11. Protected resources.
12. Account status management.

The exact authentication mechanism must be selected and approved during technical planning.

Social login must support approved external identity providers, including Google and Facebook. OAuth flows must securely verify identity, support account linking, handle sessions safely, and never store passwords from external providers.

## 15.4 Recipe Management

A recipe may contain:

- Title.
- Main image.
- Additional images.
- Description.
- Preparation time.
- Cooking time.
- Total time.
- Servings.
- Difficulty.
- Ingredients.
- Ingredient quantities.
- Ordered instructions.
- Optional nutritional information.
- Author.
- Ratings.
- Comments.
- Publication status.

Operations:

1. Create recipe.
2. Read recipe.
3. Update recipe.
4. Delete recipe.
5. Upload images.
6. Manage ingredients.
7. Manage instructions.
8. Save draft.
9. Publish recipe.
10. Unpublish recipe.
11. Explore public recipes.
12. Search recipes.
13. Filter recipes.
14. Sort recipes.

## 15.5 User Dashboard

Features:

- Personal dashboard.
- Recipe statistics.
- Content statistics.
- Activity summaries.
- Quick actions.

## 15.6 Private Tasks

Features:

1. Create task.
2. Read tasks.
3. Update task.
4. Delete task.
5. Complete task.
6. Set priority.
7. Set due date.
8. Associate a task with an owned recipe when applicable.
9. Filter tasks.
10. View task statistics.

Tasks are private to their owner.

Admins must not automatically access private task details.

## 15.7 Blog Management

Features:

1. Create blog post.
2. Read blog posts.
3. Update blog post.
4. Delete blog post.
5. Upload cover image.
6. Manage rich-text content.
7. Manage categories.
8. Manage tags.
9. Save drafts.
10. Publish posts.
11. Associate posts with recipes when applicable.
12. Support comments and likes.

## 15.8 Community

Features:

1. Browse public recipes.
2. Like recipes.
3. Unlike recipes.
4. Comment on recipes.
5. Edit own comments.
6. Delete own comments.
7. Favorite recipes.
8. Remove favorites.
9. Discover creators.
10. View creator profiles.
11. Like blog posts.
12. Comment on blog posts.

Following creators is a potential extension and must be confirmed before implementation.

## 15.9 Meal Planner

Proposed features:

1. Weekly meal planning.
2. Add recipe to a planned meal.
3. Update planned meal.
4. Remove planned meal.
5. Calendar interface.
6. Shopping list generation.
7. Shopping list management.

## 15.10 Profile and Settings

Features:

1. View profile.
2. Update personal information.
3. Change password.
4. Notification preferences.
5. Favorite recipes.
6. Subscription information.

## 15.11 Subscription Management

Proposed plans:

- Free trial.
- Free.
- Pro.
- Premium.

Features:

1. Define plans.
2. Start free trial.
3. View current plan.
4. Upgrade.
5. Downgrade.
6. Cancel subscription.
7. View subscription status.
8. Apply plan-based feature restrictions.

The initial implementation may simulate subscriptions.

Real payment integration is optional and requires separate approval.

## 15.12 Admin Backoffice

Features:

1. Admin dashboard.
2. Platform statistics.
3. User listing.
4. User details.
5. Activate/deactivate accounts.
6. Recipe moderation.
7. Blog moderation.
8. Category management.
9. Tag management.
10. Content reports.
11. Subscription plan management.
12. Platform activity monitoring.

Admin access must be restricted to authorized administrative operations.

---

# 16. Implementation Backlog — Task by Task

Every task must have:

- Task ID.
- Objective.
- Scope.
- Dependencies.
- Backend changes.
- Frontend changes.
- Database changes.
- Expected files/modules.
- Required tests.
- Acceptance criteria.
- Risks and approval requirements.

Every task must be implemented and verified independently.

The Main Agent must finish the current task before moving to the next dependent task.

## Phase 1 — Project Initialization

| ID | Task | Required verification |
|---|---|---|
| SETUP-01 | Create project repository structure | Verify expected folders and files |
| SETUP-02 | Initialize Spring Boot project | Backend build |
| SETUP-03 | Configure Java 25 | Java version and build verification |
| SETUP-04 | Initialize Angular project | Angular build |
| SETUP-05 | Configure TypeScript | Type checking |
| SETUP-06 | Configure PostgreSQL connection | Database connectivity test |
| SETUP-07 | Configure Redis connection | Redis connectivity test |
| SETUP-08 | Configure environment profiles | Configuration tests |
| SETUP-09 | Configure backend testing | Initial backend test suite |
| SETUP-10 | Configure frontend testing | Initial frontend test suite |
| SETUP-11 | Configure linting and formatting | Lint and format verification |
| SETUP-12 | Configure API error response structure | Error response tests |
| SETUP-13 | Configure logs directory and daily logging | Logging configuration tests |
| SETUP-14 | Configure local image storage foundation | Storage configuration tests |
| SETUP-15 | Configure SMTP integration foundation | Mail configuration tests |
| SETUP-16 | Configure Swagger/OpenAPI | OpenAPI generation verification |
| SETUP-17 | Verify initial application builds | Backend and frontend builds |

## Phase 2 — Database Foundation

| ID | Task | Required verification |
|---|---|---|
| DB-01 | Define database naming conventions | Schema review |
| DB-02 | Configure migration mechanism | Migration execution test |
| DB-03 | Create user table | Repository and migration tests |
| DB-04 | Create role table | Relationship and constraint tests |
| DB-05 | Create user-role relationships | Relationship tests |
| DB-06 | Create recipe table | Entity and repository tests |
| DB-07 | Create ingredient table | Entity and relationship tests |
| DB-08 | Create recipe ingredient relationships | Quantity and relationship tests |
| DB-09 | Create recipe instruction table | Ordering and relationship tests |
| DB-10 | Create recipe image metadata table | Image relationship tests |
| DB-11 | Create blog tables | Entity and relationship tests |
| DB-12 | Create task table | Entity and ownership tests |
| DB-13 | Create community interaction tables | Relationship and uniqueness tests |
| DB-14 | Create subscription tables | Entity and constraint tests |
| DB-15 | Add indexes and constraints | Database integrity tests |
| DB-16 | Create development seed data | Seed repeatability tests |
| DB-17 | Verify database migrations | Migration tests |
| DB-18 | Verify database relationships | Integration tests |

## Phase 3 — Backend Core

| ID | Task | Required verification |
|---|---|---|
| BE-01 | Configure global exception handling | Exception handler tests |
| BE-02 | Create common API response structures | Serialization tests |
| BE-03 | Configure DTO mapping conventions | Mapper tests |
| BE-04 | Configure pagination conventions | Pagination tests |
| BE-05 | Configure validation conventions | Validation tests |
| BE-06 | Implement reusable helpers when justified | Helper unit tests |
| BE-07 | Implement custom validators when needed | Validator tests |
| BE-08 | Configure safe logging | Logging and redaction tests |
| BE-09 | Configure Swagger/OpenAPI documentation | OpenAPI verification |
| BE-10 | Configure rate-limiting mechanism | Rate-limit tests |
| BE-11 | Configure Redis caching foundation | Cache integration tests |
| BE-12 | Configure local image storage service | Storage tests |
| BE-13 | Configure SMTP email service | Mail service tests |

## Phase 4 — Authentication and Security

| ID | Task | Required verification |
|---|---|---|
| AUTH-01 | Implement user registration | Registration service and API tests |
| AUTH-02 | Implement password hashing | Password hashing tests |
| AUTH-03 | Implement email verification | Verification workflow tests |
| AUTH-04 | Implement login | Login unit and API tests |
| AUTH-05 | Implement logout | Session/token invalidation tests |
| AUTH-06 | Implement authentication mechanism | Authentication tests |
| AUTH-07 | Implement forgot password | Password recovery tests |
| AUTH-08 | Implement reset password | Reset token and validation tests |
| AUTH-09 | Implement role management | Role repository and service tests |
| AUTH-10 | Implement backend authorization | Access-control tests |
| AUTH-11 | Implement resource ownership checks | User isolation tests |
| AUTH-12 | Implement account activation/deactivation | Account status tests |
| AUTH-13 | Test security-sensitive endpoints | Security integration tests |
| AUTH-14 | Implement approved OAuth/social login providers, including Google and Facebook | OAuth flow, identity verification, account-linking, session, and secret-protection tests |

## Phase 5 — Frontend Foundation

| ID | Task | Required verification |
|---|---|---|
| FE-01 | Configure Angular application structure | Build and structure verification |
| FE-02 | Configure application routing | Route tests |
| FE-03 | Create public layout | Component tests |
| FE-04 | Create authenticated dashboard layout | Component tests |
| FE-05 | Create admin layout | Component tests |
| FE-06 | Create reusable UI components | Component tests |
| FE-07 | Configure HTTP communication | HTTP service tests |
| FE-08 | Configure authentication interceptor | Interceptor tests |
| FE-09 | Configure route guards | Guard tests |
| FE-10 | Configure frontend models | Type checking |
| FE-11 | Configure shared form validation | Validator tests |
| FE-12 | Configure frontend error handling | Error state tests |
| FE-13 | Configure English-only UI text conventions | UI verification |

## Phase 6 — Authentication Frontend

| ID | Task | Required verification |
|---|---|---|
| FE-AUTH-01 | Registration page | Component and form tests |
| FE-AUTH-02 | Login page | Component and form tests |
| FE-AUTH-03 | Logout behavior | Interaction tests |
| FE-AUTH-04 | Email verification page | Component tests |
| FE-AUTH-05 | Forgot password page | Component and form tests |
| FE-AUTH-06 | Reset password page | Component and form tests |
| FE-AUTH-07 | Protected route behavior | Routing and guard tests |
| FE-AUTH-08 | Authentication error handling | Integration tests |

## Phase 7 — Public Frontoffice

| ID | Task | Required verification |
|---|---|---|
| PUB-01 | Create landing page | Component tests |
| PUB-02 | Create navbar | Component and interaction tests |
| PUB-03 | Create footer | Component tests |
| PUB-04 | Create features section | Component tests |
| PUB-05 | Create pricing page | Component tests |
| PUB-06 | Create public recipe listing API | Controller and service tests |
| PUB-07 | Create public recipe listing UI | Component and integration tests |
| PUB-08 | Implement recipe search | Search API and UI tests |
| PUB-09 | Implement recipe filters | Filtering API and UI tests |
| PUB-10 | Implement recipe sorting | Sorting API and UI tests |
| PUB-11 | Implement recipe detail API | Controller and service tests |
| PUB-12 | Create recipe detail page | Component and integration tests |
| PUB-13 | Create public blog listing | API and component tests |
| PUB-14 | Create blog detail page | API and component tests |
| PUB-15 | Create creator public profile | API and component tests |
| PUB-16 | Create legal and informational pages | Component tests |

## Phase 8 — Recipe Management

| ID | Task | Required verification |
|---|---|---|
| RECIPE-01 | Implement recipe entity and mapping | Entity and mapper tests |
| RECIPE-02 | Implement create recipe service | Service unit tests |
| RECIPE-03 | Implement create recipe API | Controller integration tests |
| RECIPE-04 | Implement get recipe service | Service and repository tests |
| RECIPE-05 | Implement get recipe API | Controller tests |
| RECIPE-06 | Implement update recipe service | Service unit tests |
| RECIPE-07 | Implement update recipe API | Controller tests |
| RECIPE-08 | Implement delete recipe service | Service and repository tests |
| RECIPE-09 | Implement delete recipe API | Controller tests |
| RECIPE-10 | Implement ingredient management | Relationship and service tests |
| RECIPE-11 | Implement instruction management | Ordering and service tests |
| RECIPE-12 | Implement recipe image upload | Upload validation and API tests |
| RECIPE-13 | Implement draft status | Status transition tests |
| RECIPE-14 | Implement publish/unpublish | Authorization and status tests |
| RECIPE-15 | Implement recipe ownership checks | Security tests |
| RECIPE-16 | Create recipe listing UI | Component tests |
| RECIPE-17 | Create recipe form | Form and validation tests |
| RECIPE-18 | Create recipe editing UI | Component and integration tests |
| RECIPE-19 | Create recipe management details | Component tests |

## Phase 9 — User Dashboard

| ID | Task | Required verification |
|---|---|---|
| DASH-01 | Implement dashboard summary API | Service and controller tests |
| DASH-02 | Implement recipe statistics | Aggregation tests |
| DASH-03 | Implement activity summaries | Service tests |
| DASH-04 | Create dashboard overview UI | Component tests |
| DASH-05 | Create statistics components | Component tests |
| DASH-06 | Integrate dashboard APIs | Integration tests |
| DASH-07 | Test loading, empty, and error states | Component tests |

## Phase 10 — Private Tasks

| ID | Task | Required verification |
|---|---|---|
| TASK-01 | Create task entity | Entity tests |
| TASK-02 | Implement create task service | Unit tests |
| TASK-03 | Implement create task API | Controller tests |
| TASK-04 | Implement list tasks | Repository and service tests |
| TASK-05 | Implement update task | Service and controller tests |
| TASK-06 | Implement delete task | Service and controller tests |
| TASK-07 | Implement complete/uncomplete task | State transition tests |
| TASK-08 | Implement priority | Validation and service tests |
| TASK-09 | Implement due dates | Date validation tests |
| TASK-10 | Implement optional recipe association | Relationship tests |
| TASK-11 | Implement filtering | Repository and API tests |
| TASK-12 | Implement task statistics | Aggregation tests |
| TASK-13 | Create task management UI | Component tests |
| TASK-14 | Create task form | Form tests |
| TASK-15 | Verify task privacy | Security integration tests |

## Phase 11 — Blog Management

| ID | Task | Required verification |
|---|---|---|
| BLOG-01 | Create blog entities | Entity tests |
| BLOG-02 | Implement create blog service | Unit tests |
| BLOG-03 | Implement create blog API | Controller tests |
| BLOG-04 | Implement read blog APIs | Repository and controller tests |
| BLOG-05 | Implement update blog service | Unit tests |
| BLOG-06 | Implement update blog API | Controller tests |
| BLOG-07 | Implement delete blog service | Unit tests |
| BLOG-08 | Implement delete blog API | Controller tests |
| BLOG-09 | Implement cover image upload | Upload tests |
| BLOG-10 | Implement rich-text content handling | Validation and security tests |
| BLOG-11 | Implement categories | Repository and service tests |
| BLOG-12 | Implement tags | Repository and service tests |
| BLOG-13 | Implement draft/publish status | State transition tests |
| BLOG-14 | Implement recipe association | Relationship tests |
| BLOG-15 | Create blog management UI | Component tests |
| BLOG-16 | Create blog editor | Form and editor tests |

## Phase 12 — Community

| ID | Task | Required verification |
|---|---|---|
| COMMUNITY-01 | Implement recipe like | Service and API tests |
| COMMUNITY-02 | Implement recipe unlike | Service and API tests |
| COMMUNITY-03 | Implement recipe comments | CRUD and validation tests |
| COMMUNITY-04 | Implement comment ownership | Authorization tests |
| COMMUNITY-05 | Implement recipe favorites | Service and repository tests |
| COMMUNITY-06 | Implement favorite removal | Service and API tests |
| COMMUNITY-07 | Implement blog likes | Service and API tests |
| COMMUNITY-08 | Implement blog comments | CRUD and validation tests |
| COMMUNITY-09 | Implement public community feed | API and repository tests |
| COMMUNITY-10 | Implement community UI | Component and integration tests |

## Phase 13 — Meal Planner

| ID | Task | Required verification |
|---|---|---|
| MEAL-01 | Design meal planner entities | Entity and relationship tests |
| MEAL-02 | Implement weekly planning | Service tests |
| MEAL-03 | Implement add planned meal | API and service tests |
| MEAL-04 | Implement update planned meal | API and service tests |
| MEAL-05 | Implement remove planned meal | API and service tests |
| MEAL-06 | Implement calendar view | Component tests |
| MEAL-07 | Implement shopping list generation | Algorithm and service tests |
| MEAL-08 | Implement shopping list management | CRUD tests |
| MEAL-09 | Integrate meal planner frontend | Integration tests |

## Phase 14 — Profile and Settings

| ID | Task | Required verification |
|---|---|---|
| PROFILE-01 | Implement get profile | Service and API tests |
| PROFILE-02 | Implement update profile | Validation and API tests |
| PROFILE-03 | Implement change password | Security tests |
| PROFILE-04 | Implement notification preferences | Service and repository tests |
| PROFILE-05 | Implement favorites view | API and component tests |
| PROFILE-06 | Implement subscription information view | API and component tests |
| PROFILE-07 | Create profile UI | Component tests |
| PROFILE-08 | Create settings UI | Component and form tests |

## Phase 15 — Subscription Management

| ID | Task | Required verification |
|---|---|---|
| SUB-01 | Define subscription plans | Entity and repository tests |
| SUB-02 | Implement free trial | Date and business-rule tests |
| SUB-03 | Implement free plan | Service tests |
| SUB-04 | Implement Pro plan | Service tests |
| SUB-05 | Implement Premium plan | Service tests |
| SUB-06 | Implement upgrade | Transition and authorization tests |
| SUB-07 | Implement downgrade | Transition tests |
| SUB-08 | Implement cancellation | Cancellation tests |
| SUB-09 | Implement subscription status | Service and API tests |
| SUB-10 | Implement plan restrictions | Authorization tests |
| SUB-11 | Create subscription management UI | Component tests |
| SUB-12 | Verify subscription workflows | Integration tests |

## Phase 16 — Admin Backoffice

| ID | Task | Required verification |
|---|---|---|
| ADMIN-01 | Implement admin dashboard API | Service and controller tests |
| ADMIN-02 | Implement platform statistics | Aggregation tests |
| ADMIN-03 | Implement user listing | Repository and API tests |
| ADMIN-04 | Implement user details | Authorization tests |
| ADMIN-05 | Implement activate/deactivate account | State and security tests |
| ADMIN-06 | Implement recipe moderation | Service and API tests |
| ADMIN-07 | Implement blog moderation | Service and API tests |
| ADMIN-08 | Implement category management | CRUD tests |
| ADMIN-09 | Implement tag management | CRUD tests |
| ADMIN-10 | Implement content reports | Service and API tests |
| ADMIN-11 | Implement subscription plan management | Service and API tests |
| ADMIN-12 | Implement platform activity | Audit tests |
| ADMIN-13 | Create admin dashboard UI | Component tests |
| ADMIN-14 | Create admin user management UI | Component and integration tests |
| ADMIN-15 | Create moderation UI | Component and integration tests |
| ADMIN-16 | Verify admin authorization | Security integration tests |
| ADMIN-17 | Verify private task restrictions | Security integration tests |

## Phase 17 — Quality Assurance

| ID | Task | Required verification |
|---|---|---|
| QA-01 | Run backend full test suite | Backend test report |
| QA-02 | Run frontend full test suite | Frontend test report |
| QA-03 | Run integration tests | Integration report |
| QA-04 | Run end-to-end tests | E2E report |
| QA-05 | Perform security review | Security report |
| QA-06 | Perform database integrity review | Database review report |
| QA-07 | Review Swagger documentation | OpenAPI verification |
| QA-08 | Review logging and secret redaction | Logging audit |
| QA-09 | Review local image storage security | Storage security report |
| QA-10 | Review SMTP configuration and email behavior | Email tests |
| QA-11 | Review rate limiting | Rate-limit tests |
| QA-12 | Review Redis caching and invalidation | Cache tests |
| QA-13 | Review Jenkins CI pipeline | CI report |
| QA-14 | Review responsive behavior | Responsive UI verification |
| QA-15 | Review accessibility | Accessibility report |
| QA-16 | Review performance | Performance report |
| QA-17 | Review configuration and secrets | Configuration audit |
| QA-18 | Launch complete Code Review Subagent analysis | Full source code review report |
| QA-19 | Address approved review findings | Regression tests |
| QA-20 | Perform final verification | Complete test report |
| QA-21 | Prepare final completion report | Final report |
| QA-22 | Perform Version 1 and Version 2 visual regression testing | Visual regression evidence and report |

---

# 17. Task Execution Requirements

## 17.1 Task Definition

Before implementation, the Main Agent must define:

- Task ID.
- Task objective.
- Scope.
- Dependencies.
- Backend changes.
- Frontend changes.
- Database changes.
- Expected files/modules.
- Required tests.
- Acceptance criteria.
- Risks and approval requirements.

## 17.2 Task Isolation

- Each task must be developed in its own Git branch.
- The Main Agent must not work directly on `main`.
- Related tasks must not be mixed into unrelated commits.
- A task may contain multiple focused commits.
- Every task must be independently reviewable.
- No remote push is permitted.
- Branch merging requires explicit user approval.

## 17.3 Task Completion

Before moving to the next task:

1. Implementation must be complete.
2. Required tests must pass.
3. Relevant verification must pass.
4. Task commits must be created.
5. Code Review Subagent must review the changes.
6. Valid review findings must be addressed.
7. Required regression tests must pass.
8. Main Agent must deliver a completion report.

---

# 18. Code Review Requirements

The Main Agent must use a dedicated Code Review Subagent.

The Code Review Subagent must conduct an independent review.

## 18.1 Review Scope

Review the complete relevant source code, including:

- Backend.
- Frontend.
- Database migrations.
- Database functions and triggers.
- Tests.
- Security implementation.
- API contracts.
- Architecture.
- Error handling.
- Documentation.
- Configuration files.
- Logging.
- Storage.
- Email integration.
- Redis caching.
- CI configuration.

## 18.2 Review Criteria

Check for:

- Functional bugs.
- Incorrect business logic.
- Security vulnerabilities.
- Authorization failures.
- Data leakage.
- Broken database relationships.
- Transaction problems.
- Missing validation.
- Missing tests.
- Weak test quality.
- Unnecessary complexity.
- Duplicate logic.
- Performance problems.
- Inconsistent API contracts.
- Violations of this specification.
- Violations of `AGENTS.md`.

## 18.3 Review Restrictions

The Code Review Subagent must be read-only.

It must not modify files, fix code, create commits, push, or merge.

## 18.4 Review Report

The final review report must include:

- Review scope.
- Files and modules reviewed.
- Findings.
- Severity.
- File and line references.
- Explanation of each finding.
- Potential impact.
- Recommended fix.
- Tests or verification still required.
- Overall review conclusion.

If no findings are detected, the report must state that explicitly.

The Main Agent is responsible for handling review findings and reporting the final status.

---

# 19. Two-Version Delivery Scope

The project has exactly two versions. The former MVP/V1/Future classification is not used.

## 19.1 Version 1 — UI Prototype

Version 1 is an HTML and CSS prototype for the approved user experience. It covers layout, navigation, responsive behavior, and interactions without being the first functional application release. It requires task isolation, applicable tests, visual regression testing, independent read-only code review, remediation of valid findings, and final verification. Explicit user approval is required before Version 2.

After approval, generate `docs/DESIGN.md` documenting the final approved UI design, components, layouts, navigation, interactions, and responsive behavior.

## 19.2 Version 2 — Final Application

Version 2 is the final application built from the approved functional scope in this specification and the approved Version 1 design. After Version 1 approval, generate `docs/IMPLEMENTATION.md` from `docs/DESIGN.md`, `docs/SPEC.md`, `docs/LOCK.md`, and `docs/TEST.md`. The implementation plan requires explicit user approval before Version 2 implementation begins.

Version 2 uses Spring Boot, Java 25, Angular, PostgreSQL, JPA/Hibernate, Redis, SMTP, Swagger/OpenAPI, Jenkins, and local filesystem image storage.

---

# 20. Acceptance Criteria and Definition of Done

Every task must have explicit acceptance criteria before implementation.

A task is complete only when:

- It satisfies its requirements.
- It respects the approved architecture.
- It does not introduce unapproved dependencies or features.
- Required tests exist.
- Required tests pass.
- Relevant builds and verification commands pass.
- Security requirements are respected.
- Code Review has been completed.
- Valid review findings have been addressed or explicitly reported.
- Task changes are committed in their dedicated branch.
- No unauthorized push or merge has occurred.
- The Main Agent has provided a complete report.

---

# 21. Final Rules

1. Never implement an unclear requirement by guessing.
2. Never skip required tests.
3. Never modify tests just to make them pass.
4. Never move to the next function before verifying the current one.
5. Never move to the next task before completing the current task lifecycle.
6. Never introduce unapproved dependencies.
7. Never change the approved stack silently.
8. Never expose secrets.
9. Never bypass security or authorization.
10. Never report unverified success.
11. Never push or merge without the required authorization.
12. Always keep implementation, tests, and documentation consistent.

**Correctness, security, testability, maintainability, and respect for approved requirements take priority over implementation speed.**
