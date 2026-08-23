# Server project instructions

- Open this directory in IntelliJ IDEA if desired, but keep `C:\src\Maintena3` as the Git root.
- Use `maintena-server\.\gradlew.bat` for builds and tests.
- The server Kotlin/JVM target and toolchain are Java 21. Keep the Gradle JVM, Kotlin compiler target, and IDE settings aligned with that source of truth.
- Do not commit `.idea`, `.gradle`, `build`, local environment files, credentials, or generated reports.
- For Kotlin/Ktor changes, run `.\gradlew.bat test`; run `.\gradlew.bat build` when packaging or dependency configuration is affected.
- Keep the HTTP contract, configuration, and tests documented and synchronized with the Android client.
