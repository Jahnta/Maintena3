# Server

- Gradle configuration is authoritative for Kotlin/JVM versions.
- Target a relevant class with `.\gradlew.bat test --tests "com.db.ServerTest"`; widen to `test` for broader behavior changes. Use `build` for dependency, packaging, or build-logic changes (it includes tests). Pure documentation/config edits need no Gradle run.
- Use `.\gradlew.bat run` only when runtime behavior requires it; the process is long-running.

## Architecture and ownership

- `src/main/kotlin/main.kt` starts Ktor EngineMain; `Routing.kt` implements `configureRouting` and the root GET route. `src/main/resources` holds runtime configuration; `src/test/kotlin/ServerTest.kt` uses Ktor `testApplication`.
- The current server has no application/domain/repository/DTO/database layers. Exposed, PostgreSQL/SQLite, Flyway, HikariCP, kotlinx.serialization JSON, and Koin are not configured dependencies yet. Do not assume they exist or add the whole stack for a small route change.
- Before an API change, trace routes, request/response types, application/domain logic, repositories, database mappings/migrations, serialization, and error handling wherever present. Add layers only when the feature needs them, following existing conventions.
- Add Ktor tests for changed success/error behavior against the agreed contract.
- When persistence is introduced or changed, coordinate schema/migration order, transactions, connection handling, and supported database dialects. Preserve stored data, avoid blocking database work on request execution paths, and test migrations against the affected supported databases.
- Let Gradle maintain dependency locks and verification metadata when dependencies change.
