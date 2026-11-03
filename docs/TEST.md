# TEST.md

# Recipe Manager — Testing Strategy and Requirements

## 1. Purpose

This document defines the mandatory testing strategy, testing requirements, execution rules, and acceptance criteria for the Recipe Manager project.

The AI Agent must read and follow this document throughout the entire development lifecycle.

The objective is to ensure that:

- Every implemented feature is properly tested.
- Every important business rule is verified.
- Backend and frontend behavior are tested independently and together.
- Database integrity is verified.
- Security and authorization are tested.
- Regressions are detected.
- No task is considered complete without the required tests passing.

This document must be used together with:

- `AGENTS.md`
- `docs/SPEC.md`

If a testing requirement conflicts with an approved functional or technical requirement, stop and report the conflict instead of silently changing either requirement.

---

# 2. General Testing Principles

## 2.1 Test-Driven Development (TDD)

Follow the Red-Green-Refactor cycle whenever implementing testable functionality.

1. **Red:** Write a test that describes the expected behavior and verify that it fails for the expected reason.
2. **Green:** Implement the minimum required code to make the test pass.
3. **Refactor:** Improve the implementation without changing the expected behavior.
4. Run the relevant tests again.
5. Continue to the next function or behavior.

Do not write an entire feature and postpone all its tests until the end.

## 2.2 Test Every Meaningful Function

Every meaningful function or method must have appropriate test coverage.

This includes:

- Business logic.
- Service methods.
- Validation logic.
- Custom validators.
- Utility functions.
- Helper methods containing meaningful logic.
- Data transformations.
- API behavior.
- Frontend services.
- Frontend component behavior.
- Database functions and procedures.
- Other custom logic introduced by the project.

Testing rules:

- Test public behavior rather than implementation details.
- Private methods should normally be tested through the public behavior that uses them.
- Do not create meaningless tests for getters, setters, or trivial configuration.
- Static configuration must be verified through appropriate configuration or integration tests.
- Do not create artificial tests just to increase test counts.

## 2.3 Test File Organization

Tests must be organized by the feature or class they verify.

Examples:

```text
backend/src/test/java/.../
├── controller/
│   ├── AuthControllerTest.java
│   └── RecipeControllerTest.java
├── service/
│   ├── AuthServiceTest.java
│   └── RecipeServiceTest.java
├── repository/
│   ├── UserRepositoryTest.java
│   └── RecipeRepositoryTest.java
├── security/
├── validation/
├── integration/
└── ...
```

```text
frontend/src/
├── app/
│   ├── features/
│   │   ├── auth/
│   │   │   ├── ...
│   │   │   └── ...
│   │   └── recipes/
│   │       ├── ...
│   │       └── ...
└── ...
```

Frontend test files must follow the conventions of the selected Angular version and testing framework.

Rules:

- Keep tests close to their related feature when appropriate.
- Use descriptive test names.
- Avoid putting all tests in one large test file.
- Separate unit, integration, and end-to-end tests.
- Reuse test utilities and fixtures when justified.
- Do not duplicate test logic unnecessarily.

---

# 3. Testing Levels

The project must use the following testing levels.

## 3.1 Unit Tests

Test a single function, method, class, or isolated unit.

Backend examples:

- Service business rules.
- Helper methods.
- Custom validators.
- DTO mapping.
- Utility functions.
- Data transformation.
- Error handling.

Frontend examples:

- Angular services.
- Validators.
- Pipes.
- Guards.
- Interceptors.
- Utility functions.
- Component behavior.

Unit tests must be fast and independent.

External dependencies should be mocked when appropriate.

## 3.2 Repository and Database Tests

Test database access and persistence behavior.

Verify:

- Queries.
- Filtering.
- Sorting.
- Pagination.
- Relationships.
- Constraints.
- Unique fields.
- Foreign keys.
- Transactions.
- Rollbacks.
- Cascading behavior.
- Database functions.
- Stored procedures.
- Triggers.

Use a test database environment isolated from development and production data.

Do not run destructive tests against a real production database.

## 3.3 Integration Tests

Test interactions between multiple application layers.

Examples:

- Controller + Service + Repository.
- Authentication + Authorization.
- Recipe creation + Ingredients + Instructions.
- Image upload + Metadata persistence.
- SMTP integration.
- Redis integration.
- API validation + Error handling.
- Database transaction workflows.

Verify actual integration behavior rather than relying only on mocks.

## 3.4 API Tests

Every API endpoint must have appropriate tests.

Verify:

- HTTP method.
- URL.
- Request body.
- Request validation.
- Authentication.
- Authorization.
- Response status.
- Response structure.
- Returned data.
- Error responses.
- Ownership restrictions.
- Pagination and filtering where applicable.

Test both successful and unsuccessful requests.

## 3.5 Frontend Component Tests

Test Angular components according to their responsibilities.

Verify:

- Correct rendering.
- User interactions.
- Form validation.
- Loading state.
- Success state.
- Empty state.
- Error state.
- API response handling.
- Navigation behavior.
- Conditional rendering.
- Permission-dependent UI behavior.

Do not test only whether a component exists.

Test the actual behavior expected from the user interface.

## 3.6 End-to-End Tests

E2E tests must verify complete user journeys through the application.

Examples:

- Registration and email verification.
- Login and logout.
- Creating and publishing a recipe.
- Editing and deleting a recipe.
- Creating and completing a private task.
- Creating and publishing a blog.
- Liking and commenting on a recipe.
- Managing profile information.
- Subscription workflows.
- Admin moderation.

E2E tests must use controlled test accounts and isolated test data.

## 3.7 Visual Regression Testing

Visual regression testing is mandatory for both versions:

- Version 1: verify the approved HTML/CSS prototype across the required responsive layouts, navigation states, and key interactions.
- Version 2: verify the Angular implementation against the approved `docs/DESIGN.md` and detect regressions across the supported responsive layouts.

Visual results must include the executed scenarios, viewport or device profiles, comparison evidence, and disposition of any differences.

---

# 4. Backend Testing Requirements

## 4.1 Controller Tests

Every meaningful controller endpoint must be tested.

Verify:

- Correct HTTP method and status code.
- Valid request handling.
- Invalid request handling.
- Request DTO validation.
- Correct service interaction.
- Correct response DTO.
- Unauthorized requests.
- Forbidden requests.
- Not-found behavior.
- Unexpected error handling.

Controllers must not contain business logic that cannot be tested independently.

## 4.2 Service Tests

Every meaningful service method must have appropriate tests.

Verify:

- Expected business behavior.
- Valid input.
- Invalid input.
- Boundary conditions.
- Missing records.
- Duplicate records.
- Authorization and ownership rules.
- Correct repository interactions.
- Correct exception behavior.
- Transaction-related behavior where applicable.

For every business rule, include tests for both valid and invalid scenarios.

## 4.3 Repository Tests

Test custom queries and important persistence operations.

Verify:

- Correct query results.
- Filtering.
- Sorting.
- Pagination.
- Relationships.
- Unique constraints.
- Foreign-key behavior.
- Empty results.
- Invalid references.
- Database constraints.

Do not write redundant tests for standard framework behavior unless the project introduces custom behavior that requires verification.

## 4.4 DTO and Validation Tests

Test request DTO validation.

Examples:

- Required fields.
- Empty strings.
- Invalid email addresses.
- Invalid formats.
- Minimum and maximum lengths.
- Numeric boundaries.
- Invalid values.
- Custom validation rules.

Verify that invalid requests are rejected before business operations are executed.

Test response DTO mapping to ensure that sensitive entity attributes are not exposed.

## 4.5 Transaction Tests

Test workflows that modify multiple related records.

Verify:

- All expected changes are committed together.
- Failure causes the expected rollback.
- No partial or inconsistent data remains.
- Relationships remain valid.
- Transaction boundaries behave as intended.

## 4.6 Exception Handling Tests

Verify:

- Validation errors.
- Not-found errors.
- Conflict errors.
- Unauthorized errors.
- Forbidden errors.
- Internal errors.

Ensure responses do not expose:

- Stack traces.
- Passwords.
- Tokens.
- Credentials.
- Internal secrets.

---

# 5. Frontend Testing Requirements

## 5.1 Angular Services

Test:

- Correct API endpoint.
- HTTP method.
- Request payload.
- Response handling.
- Error handling.
- Loading behavior where applicable.
- Authentication headers where applicable.

Use Angular's appropriate HTTP testing utilities.

## 5.2 Components

Test:

- Initial rendering.
- Input handling.
- Button interactions.
- Form submission.
- Validation messages.
- Conditional content.
- Loading indicators.
- Empty states.
- Error states.
- Success feedback.
- Navigation.
- API integration behavior.

## 5.3 Forms and Validators

Test:

- Required fields.
- Invalid formats.
- Minimum and maximum values.
- Custom validation.
- Form submission with invalid data.
- Form submission with valid data.
- Backend validation error display.

## 5.4 Guards and Interceptors

Test:

- Guest access.
- Authenticated access.
- Admin-only access.
- Invalid authentication state.
- Expired or invalid credentials.
- HTTP error handling.
- Correct request behavior.

Remember that frontend guards are not a replacement for backend security.

## 5.5 Frontend Models and Utilities

Test meaningful:

- Data transformations.
- Formatting functions.
- Filtering utilities.
- Sorting utilities.
- Date calculations.
- Validation functions.
- Reusable business-independent utilities.

Use TypeScript type checking in addition to runtime tests.

---

# 6. Database Testing Requirements

Every custom database object must be tested.

This includes:

- Tables and constraints.
- Relationships.
- Custom indexes where behavior or performance needs verification.
- Database functions.
- Stored procedures.
- Triggers.
- Migration scripts.

Verify:

1. Valid data can be inserted.
2. Invalid data is rejected.
3. Unique constraints work.
4. Foreign keys work.
5. Referential integrity is preserved.
6. Expected cascading behavior occurs.
7. Transaction rollback works.
8. Functions return expected results.
9. Procedures perform expected operations.
10. Triggers execute only under the intended conditions.
11. Migrations can be applied in the expected order.

Database tests must run against an isolated test database.

---

# 7. Security Testing Requirements

Security tests are mandatory.

## 7.1 Authentication

Test:

- Registration.
- Login.
- Logout.
- Password hashing.
- Email verification.
- Password recovery.
- Invalid credentials.
- Expired or invalid tokens.
- Disabled accounts.
- Unauthenticated access.

For OAuth/social login, test approved providers including Google and Facebook, secure callback and state handling, identity verification, account linking, session handling, failure cases, and confirmation that external-provider passwords are never stored.

## 7.2 Authorization

Test:

- Guest restrictions.
- User permissions.
- Admin permissions.
- Role-based restrictions.
- Ownership restrictions.
- Access to another user's resources.
- Unauthorized modifications.
- Unauthorized deletions.

## 7.3 Private Data Isolation

Test that:

- Users cannot access another user's private tasks.
- Users cannot modify another user's recipes.
- Users cannot modify another user's blogs.
- Users cannot modify another user's comments.
- Private information is not returned through public APIs.
- Admins cannot automatically access private task details.

## 7.4 Input and File Security

Test:

- Invalid input.
- Malformed requests.
- Unexpected values.
- Path traversal attempts.
- Invalid image formats.
- Oversized files.
- Unauthorized file access.
- Unsafe rich-text content.
- Injection-related risks where relevant.

## 7.5 Secret Protection

Verify that secrets are not exposed in:

- API responses.
- Logs.
- Error messages.
- Test reports.
- Source code.
- CI output.

Never use real production credentials in automated tests.

---

# 8. Infrastructure and Integration Testing

## 8.1 Redis

Test:

- Cache hit.
- Cache miss.
- TTL expiration.
- Cache invalidation.
- Cache refresh.
- Cache isolation between users.
- Redis connection failure.
- Application fallback behavior.
- Consistency after database updates.

## 8.2 Rate Limiting

Approved requirement:

**Maximum 5 requests per IP address within 60 seconds.**

Test:

1. First request is accepted.
2. Second request is accepted.
3. Third request is accepted.
4. Fourth request is accepted.
5. Fifth request is accepted.
6. Sixth request within the same window is rejected.
7. Rejected request returns HTTP 429.
8. Requests from another IP have an independent limit.
9. The limit resets after the configured window expires.
10. Redis shared state behaves correctly across application instances.
11. IP extraction works behind explicitly trusted proxies.
12. Untrusted forwarding headers cannot spoof the client's IP.

## 8.3 Local Image Storage

Test:

- Valid upload.
- Invalid extension.
- Invalid MIME type.
- Oversized file.
- Unique filename generation.
- Path traversal prevention.
- Correct metadata persistence.
- Image retrieval.
- Image replacement.
- Image deletion.
- Missing file handling.
- Unauthorized access.
- Storage failure handling.

## 8.4 SMTP

Test:

- Verification email generation.
- Password reset email generation.
- Correct recipient.
- Correct template data.
- SMTP authentication failure.
- SMTP connection failure.
- Timeout handling.
- No credentials in logs.
- No real email delivery during ordinary automated tests.

## 8.5 Logging

Test:

- Correct log levels.
- Daily file configuration.
- Rotation configuration.
- Retention configuration.
- Safe user context.
- Request correlation ID.
- Secret redaction.
- Sensitive data exclusion.
- Production error handling.

## 8.6 Swagger / OpenAPI

Verify:

- OpenAPI specification generation.
- Endpoint documentation.
- Request schemas.
- Response schemas.
- HTTP status documentation.
- Authentication documentation.
- Error response documentation.
- API documentation access restrictions according to configuration.

---

# 9. Functional Test Matrix

Every implemented feature must have tests corresponding to its expected behavior.

| Feature | Required test coverage |
|---|---|
| Registration | Valid registration, invalid input, duplicate email, password rules |
| Login | Valid credentials, invalid credentials, disabled account |
| Logout | Session/token invalidation |
| Email verification | Valid token, invalid token, expired token |
| Password reset | Valid reset, invalid token, expired token |
| Recipe CRUD | Create, read, update, delete, validation |
| Recipe ingredients | Create, update, delete, quantity validation |
| Recipe instructions | Create, update, delete, ordering |
| Recipe images | Upload, replace, delete, invalid files |
| Recipe publishing | Draft, publish, unpublish, permissions |
| Recipe search | Search terms, empty results, invalid filters |
| Recipe sorting | Correct sorting and pagination |
| Dashboard | Statistics, empty state, user isolation |
| Private tasks | CRUD, completion, priority, due dates |
| Task privacy | Access denied to another user's tasks |
| Blogs | CRUD, drafts, publication, categories, tags |
| Blog content | Validation and unsafe content handling |
| Community likes | Like, unlike, duplicate prevention |
| Comments | Create, update, delete, ownership |
| Favorites | Add, remove, duplicate prevention |
| Profile | Read, update, validation |
| Password change | Current password, new password, invalid input |
| Subscriptions | Trial, plan selection, upgrade, downgrade, cancel |
| Admin users | Listing, details, activation, deactivation |
| Admin moderation | Recipe and blog moderation permissions |
| Categories | CRUD and validation |
| Tags | CRUD and validation |
| Meal planner | Planning, update, removal, calendar data |
| Shopping list | Generation and management |
| Redis | Cache hit, miss, expiration, invalidation |
| Rate limiting | 5 requests per IP / 60 seconds |
| SMTP | Email generation and failure handling |
| Logging | Rotation, retention, secret redaction |
| Swagger | OpenAPI generation and documentation |
| Jenkins | Build, tests, reports, failure detection |

A feature must not be marked complete merely because one test in its row passes.

The test cases must cover the meaningful success, failure, boundary, and authorization scenarios for the implemented behavior.

---

# 10. End-to-End Test Scenarios

Implement E2E scenarios for the most important complete user journeys.

## E2E-01 — Authentication Journey

1. Register a new user.
2. Verify the email using the test email mechanism.
3. Log in.
4. Access the protected dashboard.
5. Log out.
6. Verify that protected resources are no longer accessible.

## E2E-02 — Recipe Management Journey

1. Log in as a user.
2. Create a recipe.
3. Add ingredients.
4. Add instructions.
5. Upload an image.
6. Save the recipe as a draft.
7. Edit the recipe.
8. Publish the recipe.
9. Verify it appears in public exploration.
10. Delete the recipe.
11. Verify it is no longer available.

## E2E-03 — Private Tasks Journey

1. Log in as User A.
2. Create a task.
3. Set priority and due date.
4. Complete the task.
5. Verify the task statistics.
6. Log in as User B.
7. Attempt to access User A's task.
8. Verify access is denied.

## E2E-04 — Blog Journey

1. Log in as a user.
2. Create a blog post.
3. Add cover image and content.
4. Save as draft.
5. Publish the post.
6. Verify public visibility.
7. Update the post.
8. Verify updated content.

## E2E-05 — Community Journey

1. Log in as User A.
2. Explore a public recipe.
3. Like the recipe.
4. Add a comment.
5. Add the recipe to favorites.
6. Verify the interactions.
7. Remove the favorite.
8. Unlike the recipe.

## E2E-06 — Admin Journey

1. Log in as Admin.
2. Open the admin dashboard.
3. View users.
4. Deactivate a test account.
5. Moderate a test recipe or blog post.
6. Verify that non-admin users cannot access admin operations.

## E2E-07 — Subscription Journey

1. Register a test user.
2. Verify the initial subscription state.
3. Select an available plan.
4. Upgrade or downgrade according to supported business rules.
5. Cancel the subscription.
6. Verify the resulting status and restrictions.

## E2E-08 — Meal Planner Journey

Implement when the meal planner enters the approved delivery scope.

1. Log in as a user.
2. Add recipes to a weekly plan.
3. Update a planned meal.
4. Remove a planned meal.
5. Generate a shopping list.
6. Verify the shopping list contents.

---

# 11. Test Data and Environment Isolation

All automated tests must use controlled test data.

Requirements:

- Do not use production data.
- Do not depend on manually created user accounts.
- Create predictable test fixtures.
- Isolate tests from development data.
- Clean up test data where appropriate.
- Ensure tests can be repeated.
- Avoid test execution order dependencies.
- Avoid tests depending on external services being available unless specifically testing those integrations.

Use mocks, fakes, or test containers when appropriate.

Do not introduce new dependencies or services without the approval required by `AGENTS.md`.

If an external service is unavailable, report the blocker instead of silently skipping its required tests.

---

# 12. Test Execution Workflow

For every task:

## Step 1 — Understand

Before writing tests, read:

- `AGENTS.md`
- `docs/SPEC.md`
- This `docs/TEST.md`
- The task requirements and acceptance criteria.

Identify the required test scenarios.

## Step 2 — Plan

Create a test checklist containing:

- Unit tests.
- Repository tests.
- Integration tests.
- API tests.
- Frontend component tests.
- Security tests.
- E2E tests where applicable.

Only include test categories relevant to the task, but do not omit any mandatory coverage.

## Step 3 — Write Tests

Write tests before implementation whenever following TDD.

Ensure the tests describe expected behavior, including invalid and boundary cases.

## Step 4 — Verify Red

Run the new tests.

Confirm that they fail for the expected reason before implementing the behavior.

A test that fails because of an unrelated configuration error does not demonstrate the expected Red phase.

## Step 5 — Implement

Implement the required functionality.

Do not weaken or remove test expectations to make the implementation pass.

## Step 6 — Verify Green

Run the relevant tests.

All required tests must pass.

## Step 7 — Refactor

Refactor when necessary.

Rerun affected tests after refactoring.

## Step 8 — Regression Testing

Run the existing tests that may be affected by the change.

Do not assume that a new feature cannot break existing functionality.

## Step 9 — Code Review

The Code Review Subagent must independently review the implementation and tests.

The reviewer must check:

- Missing test cases.
- Weak assertions.
- False-positive tests.
- Incorrect mocks.
- Missing boundary tests.
- Missing authorization tests.
- Missing regression tests.
- Tests that do not verify actual requirements.

The Code Review Subagent is read-only.

## Step 10 — Final Verification

The Main Agent must:

1. Address valid review findings.
2. Add missing tests.
3. Rerun relevant tests.
4. Rerun regression tests.
5. Verify the final build.
6. Produce a test report.

Only then may the task be marked complete.

---

# 13. Test Execution Commands


Do not assume that a particular test runner or package manager is installed.

Use the commands defined by the project's actual build configuration.

Typical commands may include:

### Backend

```bash
# Run all backend tests
./mvnw test

# Or, if Gradle is approved and configured
./gradlew test

# Build and verify
./mvnw verify
```

Use only the build tool actually selected for the project.

### Frontend

Use the scripts and test runner configured in `package.json` and the Angular project configuration.

Typical commands may include:

```bash
npm test
npm run build
npm run lint
```

These commands are examples, not a requirement to install or configure a particular tool without approval.

### Database

Run database tests through the project's configured test environment and migration tooling.

### End-to-End

Run E2E tests through the approved E2E framework and configured test environment.

### Jenkins

Verify that the Jenkins pipeline executes the same required build and test stages used by the project.

Do not report a test as passed unless the command was actually executed and its result verified.

---

# 14. Test Failure Rules

When a test fails:

1. Read the complete error output.
2. Identify the root cause.
3. Determine whether the issue is in implementation, test setup, test data, environment, or configuration.
4. Fix the root cause.
5. Rerun the failing test.
6. Rerun related regression tests.
7. Report unresolved failures.

Never:

- Delete a failing test to obtain a green result.
- Disable a test without approval.
- Add arbitrary skips.
- Weaken assertions without justification.
- Change expected behavior to match incorrect implementation.
- Mock the functionality that the test is supposed to verify.
- Ignore flaky tests.
- Claim success based only on compilation.

If a requirement is ambiguous or a test reveals a conflict in `docs/SPEC.md`, stop and report the conflict.

A specification change requires explicit user approval.

---

# 15. Coverage Requirements

Use test coverage reports to identify untested code.

Coverage is a diagnostic tool, not proof of correctness.

Requirements:

- Review coverage for important business logic.
- Identify untested branches.
- Test meaningful error paths.
- Test boundary conditions.
- Test authorization decisions.
- Test important transaction workflows.

Do not chase 100% line coverage by writing meaningless tests.

Do not claim that all requirements are tested solely because a coverage percentage is high.

No arbitrary total number of unit tests is required. Report actual executed test-case counts and meaningful behavioral coverage.

The target is complete meaningful behavioral coverage of approved requirements.

---

# 16. Test Reporting

At the end of every task, provide a test report.

The report must include:

## Task Information

- Task ID.
- Task name.
- Branch name.
- Commit hash or hashes.

## Test Execution

| Test category | Executed | Passed | Failed | Skipped |
|---|---|---|---|---|
| Unit | | | | |
| Repository/Database | | | | |
| Integration | | | | |
| API | | | | |
| Frontend Components | | | | |
| Security | | | | |
| E2E | | | | |
| Visual Regression | | | | |
| Build | | | | |

Only report categories that apply, but explain any required category that was not executed.

## Evidence

For each executed test suite, provide:

- Exact command.
- Actual execution result.
- Number of passed tests.
- Number of failed tests.
- Number of skipped tests.
- Relevant output or report location.

## Failures and Blockers

For each failure or blocker, provide:

- Description.
- Root cause if known.
- Affected feature.
- Required action.
- Current status.

## Final Status

Use one of:

- **PASS:** All applicable required tests passed.
- **FAIL:** One or more required tests failed.
- **BLOCKED:** Testing could not be completed because of an unresolved blocker.
- **NOT RUN:** Required tests were not executed.

Never use PASS when required tests are skipped, blocked, or unverified.

---

# 17. Definition of Test Completion

A task is test-complete only when:

- All applicable required test scenarios have been identified.
- All required tests have been implemented.
- TDD has been followed where applicable.
- Unit tests pass.
- Relevant database and repository tests pass.
- Relevant integration and API tests pass.
- Relevant frontend tests pass.
- Relevant security tests pass.
- Relevant regression tests pass.
- Required E2E scenarios pass when applicable.
- The build succeeds.
- The Code Review Subagent has reviewed the tests.
- Valid review findings have been addressed.
- Evidence is included in the task report.

A task must not be marked complete while required tests remain unexecuted or failing.

---

# 18. Final Instructions to the AI Agent

1. Read `AGENTS.md`, `docs/SPEC.md`, `docs/TEST.md`, and `docs/LOCK.md` before starting any task.
2. Derive the required tests from the approved specification and task acceptance criteria.
3. Do not invent requirements or silently change expected behavior.
4. Use TDD for meaningful functionality.
5. Test every meaningful function and business rule.
6. Keep tests organized by feature and responsibility.
7. Test success, failure, boundary, and authorization scenarios.
8. Verify database integrity and transaction behavior.
9. Test backend and frontend independently and together.
10. Never skip or weaken tests to make the project appear complete.
11. Never claim a test passed without executing it.
12. Run regression tests after fixing defects.
13. Ensure the Code Review Subagent reviews test quality.
14. Report exact commands and actual results.
15. Do not move to the next task before the current task is fully verified.
16. Respect all approval and permission restrictions in `AGENTS.md`.
17. If testing requires a new dependency, installation, network access, database creation, migration, or destructive operation, request the required approval first.
18. If a required test cannot run, report the blocker clearly.
19. Do not declare the project complete while mandatory tests remain failing, skipped, blocked, or unverified.

# 19. Final Project Testing Report

At the end of the entire project, the AI Agent must generate a complete testing report named:

`TEST_REPORT.md`

The report must include:

## 1. Project Summary
- Project name.
- Testing date.
- Branch and final commit hash.
- Overall testing status.

## 2. Test Statistics

| Test Category | Total | Passed | Failed | Skipped |
|---|---:|---:|---:|---:|
| Backend Unit Tests | | | | |
| Frontend Unit Tests | | | | |
| Repository Tests | | | | |
| Integration Tests | | | | |
| API Tests | | | | |
| Component Tests | | | | |
| Security Tests | | | | |
| E2E Tests | | | | |
| Visual Regression Tests | | | | |
| **Total** | | | | |

Avoid double-counting tests that belong to multiple categories.

## 3. Unit Test Breakdown

Report separately:
- Total backend unit test cases.
- Total frontend unit test cases.
- Total unit test cases.
- Passed, failed, and skipped unit test cases.
- Coverage report and important uncovered areas.

Count actual executed test cases, not test files or test functions.

## 4. Feature-by-Feature Results

For every implemented feature, report:
- Feature name and Task ID.
- Tests executed.
- Passed / failed / skipped.
- Important scenarios verified.
- Remaining issues.

## 5. Security Testing Results

Report authentication, authorization, ownership, private data isolation, file security, rate limiting, and secret protection results.

## 6. Code Quality and Coverage

Include:
- Backend coverage.
- Frontend coverage.
- Important untested branches.
- Build and lint results.
- Relevant quality issues.

## 7. Failed Tests and Blockers

For every failure:
- Test name.
- Error or failure reason.
- Root cause.
- Fix applied or remaining action.
- Retest result.

## 8. Execution Evidence

Include:
- Exact commands executed.
- Actual test outputs.
- Test report locations.
- Coverage report locations.

## 9. Final Verdict

Use one of:

- PASS: All mandatory tests passed.
- FAIL: One or more mandatory tests failed.
- BLOCKED: Required tests could not be completed.
- NOT COMPLETE: Required testing remains unverified.

Never declare PASS if mandatory tests are skipped, blocked, or unverified.

The report must reflect actual execution results. Never invent test counts, coverage percentages, or successful results.

**The AI Agent is responsible for proving that the implementation satisfies the approved requirements through real, repeatable, and meaningful tests.**
