# Server

- Gradle configuration is authoritative for Kotlin/JVM versions.
- Target a relevant class with `.\gradlew.bat test --tests "com.example.ServerTest"`; widen to `test` for broader behavior changes. Use `build` for dependency, packaging, or build-logic changes (it includes tests). Pure documentation/config edits need no Gradle run.
- Use `.\gradlew.bat run` only when runtime behavior requires it; the process is long-running.
- Local development uses the already-running PostgreSQL service directly. Runtime configuration is in `src/main/resources/application.conf`: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, and `DB_PASSWORD`; defaults are localhost, 5432, maintena, and maintena. Direct Gradle/CI integration tests require `TEST_JDBC_DATABASE_URL`, `TEST_JDBC_DATABASE_USERNAME`, and `TEST_JDBC_DATABASE_PASSWORD` for a dedicated PostgreSQL test database.

## Architecture and ownership

- PostgreSQL is required for runtime, local development and integration tests. Use a separate test database and isolated schemas; never clear application data to run tests. No H2/SQLite fallback or new domain entities for database setup. Store credentials only in ignored local files/environment variables.
- `src/main/kotlin/main.kt` starts Ktor EngineMain; `Application.kt` wires the data source, Flyway migration, Exposed, and routes in that order. `src/main/resources` holds runtime configuration and Flyway migrations; API tests use Ktor `testApplication`.
- HikariCP, Flyway, PostgreSQL, Exposed and kotlinx.serialization JSON form the database stack. Flyway is the sole schema owner; do not use Exposed `SchemaUtils` at runtime. Koin is not configured. Keep wiring explicit unless a feature justifies a dependency-injection layer.
- Before an API change, trace routes, request/response types, application/domain logic, repositories, database mappings/migrations, serialization, and error handling wherever present. Add layers only when the feature needs them, following existing conventions.
- Add Ktor tests for changed success/error behavior against the agreed contract.
- When persistence is introduced or changed, coordinate schema/migration order, transactions, connection handling, and supported database dialects. Preserve stored data, avoid blocking database work on request execution paths, and test migrations against the affected supported databases.
- Let Gradle maintain dependency locks and verification metadata when dependencies change.

## Backend design and review checkpoints

- architect traces HTTP/API -> Ktor routes -> application/domain -> repository -> JDBC (Exposed if introduced) -> PostgreSQL. Specify domain invariants, API/compatibility changes, transaction boundaries, schema/migration order, concurrency risks, tests and dependencies between implementation tasks; skip unaffected layers.
- reviewer checks the same request path and response construction for correctness, HTTP semantics, validation, authentication/authorization, transaction safety, data integrity, coroutine/JDBC blocking, serialization, error handling, migration safety, missing tests, security and regressions. Use contract_reviewer's findings for client compatibility instead of repeating its comparison.
- Database advice and migration checks use the real JDBC/Flyway mappings today; do not assume Exposed or Koin exists. Verify PostgreSQL-specific constraints, locking and SQL against PostgreSQL. Never apply migrations or run mutating probes on a live database as part of read-only analysis.
