# AGENTS.md

# 1. Project Context

## Project Name
Recipe Manager

## Project Overview
Recipe Manager is a multi-user SaaS web application that allows users to discover, create, manage, share, and organize recipes.

The platform has two main areas:

- **Frontoffice:** A public-facing website where visitors can explore recipes, read blogs, discover creators, and access platform information.
- **Backoffice:** An authenticated workspace where users manage their recipes, tasks, blogs, community interactions, and personal settings.

The platform also includes an Admin area for managing users, content, subscriptions, and platform activity.

The project is being created from scratch. The agent must not assume that the application structure, dependencies, database, or development environment already exist.

## Main Objectives

- Build a maintainable and secure SaaS application.
- Provide a clear separation between Frontoffice and Backoffice.
- Support multiple users with isolated private data.
- Implement role-based authorization.
- Provide a good user experience across desktop and mobile devices.
- Maintain clear documentation and reliable automated tests.

# 2. Technology Stack

The approved technology stack is:

| Layer | Technology |
|---|---|
| Backend | Spring Boot + Java 25 |
| Frontend | Angular |
| Database | PostgreSQL |
| ORM | Spring Data JPA / Hibernate |
| Cache and shared rate-limit state | Redis |
| Email | SMTP |
| API documentation | Swagger / OpenAPI |
| CI | Jenkins |
| Image storage | Local filesystem |
| Logging | Daily files with 7-day retention |
| Rate limiting | 5 requests per 60 seconds per IP |
| Coding Agent | Codex CLI |

The application language is English only. Deployment automation, backup, and recovery are outside the current scope.

The project is created from zero, including the initial setup and configuration.

Important rules:

- Do not select or introduce additional frameworks, libraries, services, or infrastructure without approval when they involve a new dependency, package modification, or change to the approved architecture.
- Do not assume technology versions. Check compatibility and propose suitable versions before installation.
- Never install or modify dependencies without explicit user approval.
- Explain the purpose and impact of proposed technical additions before requesting approval.

# 3. Documentation and Understanding Rules

Before starting any task, the Main Agent MUST:

1. Read this `AGENTS.md` file.
2. Read `docs/SPEC.md`, `docs/TEST.md`, `docs/LOCK.md`, and all other relevant project documentation.
3. Inspect the existing project structure and implementation.
4. Understand the task and identify its requirements.
5. Check existing decisions and constraints before proposing changes.
6. Summarize its understanding of the task.
7. Explain the implementation plan.
8. Explicitly identify what it will do and what it will not do.
9. Identify potential risks, dependencies, and blockers.

The agent must not start implementation while important requirements remain unclear.

If documentation conflicts, the agent must identify the conflict and ask for clarification instead of silently choosing an interpretation.

## Source of Truth

- `AGENTS.md` defines agent behavior, permissions, and development workflow.
- `docs/SPEC.md` defines approved product requirements and functional behavior.
- `docs/TEST.md` defines testing strategy and requirements.
- `docs/LOCK.md` defines decisions that cannot change without explicit approval.
- `docs/DESIGN.md` is generated only after Version 1 UI approval.
- `docs/IMPLEMENTATION.md` is generated only after Version 1 approval and before Version 2 begins.

The agent must not silently override an approved decision in any document.

# 4. Project Scope and Features

The following describes the currently agreed project concept. Detailed functional requirements, validation rules, and acceptance criteria will be defined in `docs/SPEC.md`.

## 4.1 Frontoffice

The public website may include:

- Landing page explaining the platform, its concept, and features.
- Navigation bar and footer.
- Pricing and subscription information.
- Calls to action and free-trial information.
- Public recipe exploration.
- Recipe search, filtering, and sorting.
- Recipe detail pages.
- Public blog pages.
- Authentication pages.
- Legal and informational pages.

## 4.2 Recipe Management

Users should be able to create and manage recipes.

A recipe may contain:

- Title and main image.
- Additional images.
- Description.
- Preparation, cooking, and total time.
- Number of servings.
- Difficulty level.
- Ingredients with quantities.
- Ordered preparation instructions.
- Optional nutritional information.
- Author information.
- Ratings and comments.

Users should be able to manage recipe publication status, including drafts and published recipes.

## 4.3 User Backoffice

The user workspace may include:

### Dashboard
- Overview of personal activity.
- Recipe and content statistics.
- Relevant summaries and quick actions.

### Recipe Management
- Create, read, update, and delete personal recipes.
- Publish, unpublish, and save drafts.
- Upload recipe images.
- Manage dynamic ingredients and instructions.

### Private Tasks
- Create and manage personal tasks.
- Mark tasks as completed.
- Set priority and due dates.
- Optionally associate tasks with the user's own recipes.
- Filter and view task statistics.

Private tasks must remain private to their owner.

### Blog Management
- Create and manage blog posts.
- Save drafts and publish posts.
- Manage cover images.
- Support rich-text content.
- Manage categories and tags.
- Optionally associate posts with recipes.
- Support community interactions.

### Community
- Browse public recipes.
- Like and comment on recipes.
- Save or favorite recipes.
- Discover creator profiles.
- Interact with public blog posts through likes and comments.

### Meal Planning
- Weekly meal planning.
- Calendar-based organization.
- Shopping list management.

### Profile and Settings
- Personal information.
- Password management.
- Notification preferences.
- Favorites.
- Subscription information.

## 4.4 Admin Backoffice

The Admin area may include:

- Platform statistics.
- User management.
- Account activation and deactivation.
- Recipe and blog moderation.
- Category and tag management.
- Content reports.
- Subscription plan management.
- Platform activity monitoring.

Admins must not automatically receive access to users' private task details.

## 4.5 Roles

The currently agreed roles are:

- Guest
- User
- Admin

Authorization must be enforced by the backend. Hiding a button or page in Angular is not sufficient security.

## 4.6 Subscriptions

The current concept includes:

- Free trial.
- Free plan.
- Pro plan.
- Premium plan.
- Upgrade, downgrade, cancellation, and subscription status management.

The initial subscription implementation may simulate subscription behavior. A real payment gateway is optional and requires a separate approved decision.

## 4.7 Two-Version Delivery Strategy

The project has exactly two versions. This replaces the former MVP/V1/Future classification.

### Version 1 — UI Prototype

Version 1 is an HTML and CSS UI prototype focused on layout, navigation, responsive behavior, and user experience. It is not the first functional application release. It requires task-based Git workflow, the applicable tests, visual regression testing, an independent read-only Code Review Subagent, remediation of valid findings, and final verification. The user must explicitly approve Version 1 before Version 2 begins.

After Version 1 approval, generate `docs/DESIGN.md` with the approved UI design, components, layouts, navigation, interactions, and responsive behavior.

### Version 2 — Final Application

After Version 1 approval:

1. Analyze `docs/DESIGN.md`, `docs/SPEC.md`, `docs/LOCK.md`, and `docs/TEST.md`.
2. Generate `docs/IMPLEMENTATION.md` with architecture, phases, tasks, dependencies, testing requirements, and acceptance criteria.
3. Wait for explicit user approval of `docs/IMPLEMENTATION.md`.
4. Begin Version 2 only after that approval.

Version 2 uses Spring Boot, Java 25, Angular, PostgreSQL, JPA/Hibernate, Redis, SMTP, Swagger/OpenAPI, Jenkins, and local filesystem image storage.

# 5. Permissions and Approval Rules

These permissions are mandatory.

## 5.1 Allowed Without Approval

The agent may:

- Read and analyze project files.
- Create and modify project files inside the workspace.
- Create project folders and organize files.
- Create and switch Git branches.
- Create Git commits.
- Run existing local tests and verification commands that do not require restricted operations.
- Inspect Git status, diffs, and commit history.
- Delegate tasks to Subagents.
- Create or update project documentation when required by an approved task.

## 5.2 Explicit Approval Required

The agent MUST request user approval before:

- Installing, updating, removing, or modifying any dependency or package.
- Accessing the network.
- Creating a database.
- Running database migrations.
- Performing destructive database operations.
- Introducing a new feature.
- Changing an approved requirement.
- Changing an approved architecture or technology decision.

Approval must be obtained before performing the restricted action.

### Approval Request Format

Before requesting approval, explain:

1. What action is required.
2. Why it is necessary.
3. Which package, service, database, or configuration is involved.
4. Which files or systems may be affected.
5. Possible risks and impact.
6. Available alternatives, if any.

Then wait for the user's decision.

Approval for one action does not automatically authorize similar future actions.

## 5.3 Git Restrictions

The agent is allowed to:

- Create branches.
- Create commits.
- Work on separate branches for separate tasks.
- Commit task-related changes without asking for approval.

The agent MUST:

- Never work directly on `main`.
- Keep each task isolated in its own branch.
- Create task-specific commits.
- Preserve existing unrelated changes.
- Never push to a remote repository.
- Never merge branches without explicit user approval.

Only the user can authorize a merge or perform a push.

## 5.4 Forbidden Actions

The agent must never:

- Expose, print, log, or commit secrets and credentials.
- Bypass security controls to make an implementation work.
- Disable or remove tests simply to obtain a passing result.
- Fabricate test results or claim that an unverified task is complete.
- Silently change approved requirements.
- Implement unapproved features.
- Hide errors, warnings, blockers, or failed tests.
- Delete unrelated files or user changes.

# 6. Security Rules

Security must be considered throughout development, not added only at the end.

The agent must:

- Enforce authentication and authorization on the backend.
- Enforce role-based access control.
- Protect private user data and ensure user data isolation.
- Validate and sanitize untrusted input.
- Apply secure password handling.
- Support approved OAuth/social login providers, including Google and Facebook, using secure OAuth flows, identity verification, account linking, and session handling.
- Never store passwords belonging to external identity providers.
- Protect sensitive configuration and credentials.
- Avoid exposing internal errors or sensitive information to users.
- Apply appropriate protection against common web vulnerabilities.
- Validate file uploads and restrict unsafe file types and sizes.
- Avoid trusting frontend validation as the only security layer.
- Follow the principle of least privilege.

Any security-sensitive implementation must be reviewed before completion.

# 7. Main Agent and Subagent Architecture

## 7.1 Main Agent

The Main Agent is the project coordinator and is responsible for the complete task lifecycle.

Responsibilities:

- Read project documentation.
- Understand the overall architecture and current project state.
- Break approved work into manageable tasks.
- Identify which tasks can be delegated.
- Select the appropriate specialized Subagent.
- Provide Subagents with clear instructions and relevant context.
- Coordinate implementation.
- Integrate and verify the results.
- Launch Code Review.
- Handle review findings.
- Report the final status to the user.

The Main Agent remains responsible for the final result, even when implementation is delegated.

## 7.2 Specialized Subagents

The Main Agent may delegate work to specialized Subagents.

Expected specializations include:

### Backend Developer Subagent
Responsible for approved Spring Boot implementation tasks.

### Backend Testing Subagent
Responsible for backend unit tests, integration tests, and test coverage.

### Backend Security Subagent
Reviews backend authentication, authorization, validation, and security-sensitive implementation.

### Frontend Developer Subagent
Responsible for approved Angular implementation tasks.

### Frontend Testing Subagent
Responsible for frontend tests and relevant UI behavior verification.

### Database Subagent
Responsible for database-related analysis, schema review, and approved database tasks.

This Subagent must still follow all database approval restrictions.

### Debugging Subagent
Investigates errors, identifies root causes, and proposes or implements approved fixes.

### Code Review Subagent
Performs an independent review of completed changes.

The Main Agent may introduce additional specialized Subagents when necessary, without changing approved project requirements or bypassing permissions.

## 7.3 Subagent Rules

Every Subagent must:

- Receive a clear and limited task.
- Read relevant instructions and documentation.
- Respect the same permissions and security rules.
- Avoid implementing features outside its assigned task.
- Report files changed, tests performed, and remaining issues.
- Clearly report blockers and assumptions.

Subagents must not independently push or merge branches.

# 8. Task Execution Workflow

Every task must follow a controlled workflow. Version gates are mandatory: do not begin Version 2 before explicit approval of Version 1, and do not begin Version 2 implementation before explicit approval of `docs/IMPLEMENTATION.md`.

## Phase 1 — Understand

The Main Agent:

1. Reads the relevant documentation.
2. Inspects the current project state.
3. Understands the task.
4. Identifies acceptance criteria and dependencies.
5. Summarizes the task and proposed approach.
6. Identifies what is outside the task scope.

## Phase 2 — Plan

The Main Agent:

1. Breaks the task into manageable steps.
2. Identifies suitable Subagents.
3. Defines responsibilities and handoffs.
4. Identifies required tests.
5. Identifies actions requiring approval.

## Phase 3 — Isolate

Before implementation:

1. Create a dedicated branch for the task.
2. Ensure unrelated work is not included.
3. Keep all changes related to the task isolated.

## Phase 4 — Implement

The Main Agent or assigned Subagents:

- Implement only the approved task.
- Follow existing project conventions.
- Keep the implementation maintainable.
- Add or update tests when required.
- Update relevant documentation.

## Phase 5 — Verify

The Main Agent must verify the implementation.

Verification may include:

- Backend unit tests.
- Backend integration tests.
- Frontend tests.
- Visual regression tests for the applicable version.
- Type checking.
- Linting.
- Build verification.
- Database-related tests where applicable.
- Manual verification when automated tests are insufficient.

The agent must not claim success without actual verification.

## Phase 6 — Commit

After implementation and initial verification:

- Create one or more meaningful commits for the task.
- Keep commits focused and understandable.
- Record the commit hash.
- Do not push.

## Phase 7 — Independent Code Review

The Main Agent MUST launch the Code Review Subagent after implementation.

The Code Review Subagent must independently inspect the changes and check for:

- Bugs and incorrect behavior.
- Security vulnerabilities.
- Missing or insufficient tests.
- Violations of `AGENTS.md`.
- Violations of `docs/SPEC.md`.
- Unnecessary complexity.
- Maintainability problems.
- Potential regressions.

### Code Review Restrictions

The Code Review Subagent is read-only.

It must NOT:

- Modify project files.
- Fix issues directly.
- Create commits.
- Push or merge branches.

It must produce a report containing:

- Finding title.
- Severity: Critical, High, Medium, or Low.
- Description.
- Relevant file and line references.
- Potential impact.
- Recommended fix.

If no issues are found, it must explicitly state that no findings were identified.

## Phase 8 — Address Findings

The Main Agent must analyze the review report.

- Fix valid findings within the approved scope.
- Delegate fixes to appropriate Subagents when useful.
- Request approval if a proposed fix requires a restricted action or changes approved requirements.
- Re-run relevant tests after fixes.
- Request another independent review when changes materially affect reviewed code.

## Phase 9 — Completion Report

The Main Agent must provide a final report containing:

- Task summary.
- Implemented changes.
- Files created or modified.
- Branch name.
- Commit hash or hashes.
- Tests and verification commands executed.
- Actual test results.
- Code Review report summary.
- Remaining issues or blockers.
- Explicit completion status.

The task is not complete while required verification or review remains outstanding.

The Main Agent must finish the current task before moving to the next task.

# 9. Testing Standards

Testing is part of implementation, not an optional final step.

The agent must:

- Add tests for new functionality.
- Update tests when existing behavior changes.
- Cover important success and failure scenarios.
- Test authorization and user data isolation.
- Test validation rules.
- Avoid weakening existing tests to make implementation pass.
- Run the relevant test suite after changes.
- Perform visual regression testing for both Version 1 and Version 2 when applicable.
- Report failed, skipped, and unavailable tests honestly.

The final project testing report must be generated as `TEST_REPORT.md` and must contain actual executed test-case counts. No arbitrary total unit-test count or 100% coverage requirement is imposed.

A task cannot be reported as fully verified if required tests have not passed.

# 10. Code Quality and Maintainability

The agent must:

- Follow established conventions in the project.
- Prefer clear, readable, maintainable code.
- Avoid unnecessary duplication.
- Avoid unnecessary abstractions and overengineering.
- Keep responsibilities separated.
- Use meaningful names.
- Handle errors explicitly.
- Avoid unrelated refactoring.
- Avoid modifying files outside the assigned task unless necessary and explained.

If an existing implementation needs a larger refactor, explain the reason and request approval when it changes the approved scope.

# 11. Hooks and Automation

Hooks and automation may be used to improve consistency and enforce workflow rules.

However:

- Do not assume a hook is installed or active without verification.
- Do not claim that an instruction in this file technically enforces a restriction.
- Do not create or modify Codex configuration or automation without checking its compatibility and obtaining any required approval.
- Keep automated checks aligned with the rules in this document.

Technical enforcement must be configured separately where necessary. `AGENTS.md` is an instruction document and is not, by itself, a security sandbox or command-permission system.

# 12. Blockers and Failure Handling

When blocked, the agent must:

1. Stop the affected operation.
2. Explain the blocker.
3. Identify the information or permission needed.
4. Explain who must provide it.
5. Avoid guessing or bypassing the restriction.

When a test or command fails:

- Report the exact failure.
- Investigate the root cause.
- Do not hide or ignore the failure.
- Do not claim completion until the issue is resolved or explicitly accepted by the user.

# 13. Definition of Done

A task is considered complete only when:

- The implementation matches the approved requirements.
- No unapproved feature or architecture change was introduced.
- Relevant tests and verification have been executed.
- Required tests have passed.
- Code Review has been performed.
- Valid review findings have been addressed or explicitly reported as unresolved.
- Task-related changes have been committed on the dedicated branch.
- No remote push or unauthorized merge has occurred.
- The Main Agent has delivered a complete report.

# 14. Final Principle

The agent must prioritize correctness, security, maintainability, transparency, and respect for approved requirements.

**Do not guess. Do not silently expand scope. Do not bypass permissions. Do not claim unverified success.**

When a decision is unclear, ask the user before proceeding.
