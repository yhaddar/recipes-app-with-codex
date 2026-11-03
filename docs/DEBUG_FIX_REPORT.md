# Backend Debug and Fix Report

Date: 2026-11-03
Branch: `fix/auth-reactivation-and-interceptor`

## Initial errors

The initial offline Maven test run compiled successfully but failed during Spring test context creation: 73 tests ran with 28 errors and no assertion failures. The first actionable context failure was the missing `SmtpEmailService` bean. A separate profile-dependent failure reported that `AppUserRepository` was unavailable when the local profile excluded JPA/DataSource auto-configuration. Jackson 2 `ObjectMapper` references were also incompatible with the project’s Jackson 3 setup.

## Confirmed root causes

1. `SmtpEmailService` used `@ConditionalOnBean(JavaMailSender.class)`. The condition was evaluated while component configuration was being processed, before the mail sender was available, so the service was omitted and dependent contexts failed.
2. `application-local.yml` excluded the persistence stack even though security and application services require JPA repositories. This made the missing `AppUserRepository` error a profile configuration problem, not a repository scan problem.
3. The application uses Jackson 3, whose mapper type is `tools.jackson.databind.json.JsonMapper`; remaining Jackson 2 types and Jackson 2 module-registration APIs were incompatible.
4. Redis-backed authentication and rate limiting could throw infrastructure exceptions through servlet filters without a controlled API response.
5. The Redis test configuration was coupled to the application’s JPA configuration, and its nested boot configuration could interfere with profile context selection.
6. The Maven POM contained duplicate web starters.

## Fixes implemented

- Made `SmtpEmailService` a normal service with required `JavaMailSender` injection.
- Moved PostgreSQL/JPA/Flyway configuration into `application-postgres.yml`; imported it from `local` and kept Redis isolated.
- Removed the duplicate `spring-boot-starter-webmvc` dependency.
- Completed Jackson 3 `JsonMapper` wiring in security and tests.
- Added controlled 503 JSON responses for Redis failures in session authentication and rate limiting, with regression tests.
- Isolated the Redis connection test from JDBC/JPA/Flyway and removed its global nested boot-configuration side effect.
- Added Spring Security integration coverage for unauthenticated and non-admin admin mutations.
- Made those authorization tests use distinct client IPs so the global rate limiter cannot mask authorization behavior.

## Files modified

Application/configuration:

- `backend/pom.xml`
- `backend/src/main/java/com/recipemanager/auth/SessionAuthenticationFilter.java`
- `backend/src/main/java/com/recipemanager/config/SecurityConfig.java`
- `backend/src/main/java/com/recipemanager/email/SmtpEmailService.java`
- `backend/src/main/resources/application-local.yml`
- `backend/src/main/resources/application-postgres.yml`
- `backend/src/main/resources/application-redis.yml`

Tests:

- `backend/src/test/java/com/recipemanager/auth/SessionAuthenticationFilterTest.java`
- `backend/src/test/java/com/recipemanager/config/OpenApiConfigTest.java`
- `backend/src/test/java/com/recipemanager/integration/RedisConnectionTest.java`
- `backend/src/test/java/com/recipemanager/ratelimit/RateLimitFilterTest.java`
- `backend/src/test/java/com/recipemanager/security/SecurityAuthorizationIntegrationTest.java`

Existing staged changes in other files were preserved and were not reset or overwritten.

## Verification

Executed from `backend/`:

- `mvn -o clean compile` — passed; Java 25 compilation completed.
- `mvn -o -Dtest=SessionAuthenticationFilterTest,OpenApiConfigTest test` — passed; 7 tests, 0 failures/errors/skips.
- `mvn -o -Dtest=SecurityAuthorizationIntegrationTest,RedisConnectionTest,RateLimitFilterTest test` — passed; 5 tests, 0 failures/errors/skips.
- `mvn -o -Dtest=PostgresConnectionTest,SecurityAuthorizationIntegrationTest test` — passed; 3 tests, 0 failures/errors/skips.
- `mvn -o test` — passed; 78 tests, 0 failures, 0 errors, 0 skipped.
- `mvn -o spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.arguments=--server.port=18080` — application started successfully with 12 JPA repositories, including `AppUserRepository`.
- `curl http://127.0.0.1:18080/api/health` — HTTP 200, `{"status":"UP","service":"recipe-manager-api"}`.
- `git diff --check` — passed.

The attempted offline dependency-tree command could not run because the Maven dependency plugin was not cached locally. No network access or dependency installation was performed.

## Code review

An independent read-only Code Review subagent reviewed the complete diff after implementation and verification. No files were modified by the reviewer.

Resolved findings:

- Duplicate web starter — removed.
- Profile coupling between Redis and JPA — fixed with explicit profile imports and isolated Redis test configuration.
- Jackson tests constructing a separate mapper — production `JsonMapper` is now autowired in the serialization test.
- Unhandled Redis failures in session authentication and rate limiting — controlled 503 responses and regression tests added.
- Missing basic authorization integration coverage — two passing Spring Security integration tests added.

Remaining review findings:

- The new authorization integration test is currently untracked because no commit or staging operation was authorized for this task. It was compiled and executed in the verified working tree; it must be explicitly tracked before a future commit.
- User-data isolation has not been proven by an end-to-end test. The current recipe controller derives ownership from the authenticated principal, but the existing task scope does not provide a complete private-resource integration scenario. This remains a coverage gap, not a reported test failure.

## Remaining blockers and risks

- The local SMTP server at `127.0.0.1:1025` was unavailable. The application still started and `/api/health` returned 200, but `/actuator/health` returned HTTP 503 because the mail health indicator could not connect. SMTP delivery and a fully healthy actuator readiness result remain unverified.
- Startup emitted existing warnings about the missing macOS Netty DNS native provider and Mockito’s future Java-agent requirement; these did not fail compilation or tests.

## Git state

- Branch: `fix/auth-reactivation-and-interceptor`
- No commit, push, merge, deployment, dependency installation, or database migration was performed.
- No commit hash is available because committing was not authorized and existing staged work was preserved.
