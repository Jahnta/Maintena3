# Maintena3 repository instructions

## Scope and layout

This directory is the Git root of the Maintena3 monorepo:

- `maintena-android/` — Android client.
- `maintena-server/` — Kotlin/Ktor server.

Keep one Git history at the repository root. Do not create nested `.git` directories.

## Working rules

- Use the Gradle wrapper from the project being changed (`.\gradlew.bat` on Windows).
- Treat Gradle build files and version catalogs as the source of truth; do not edit generated `.idea`, `.gradle`, `build`, or `.kotlin` state.
- Keep `local.properties`, credentials, signing keys, service-account files, and other machine-specific data out of Git.
- When an API contract changes, check both the server and Android client and update tests or documentation as appropriate.
- Preserve the existing Java/Kotlin toolchain choices unless a change is intentional and verified.
- Keep text files normalized to LF according to `.gitattributes`; do not disable the repository’s line-ending policy to hide a diff.

## Common commands

Run commands from the relevant subproject:

```powershell
Set-Location maintena-android
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebug

Set-Location ..\maintena-server
.\gradlew.bat test
.\gradlew.bat build
```

`maintena-server` can be started with `.\gradlew.bat run`; it is a long-running process and should not be used as the default verification command.

## Verification and completion

- Run the narrowest relevant tests first, then the affected project build when practical.
- For cross-project changes, verify both projects.
- Inspect `git diff --check` and `git status` before committing.
- Do not commit generated build output or IDE metadata.
- Report commands that were not run and any environment limitation that affected verification.
