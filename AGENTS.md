# Maintena3

- This is the Git root for `maintena-android` and `maintena-server`; never create nested repositories.
- Work from the affected subproject and use its Gradle wrapper. Build files and version catalogs are authoritative; do not edit generated `.idea`, `.gradle`, `build`, or `.kotlin` state.
- Never commit machine-local files or secrets, including `local.properties`, credentials, signing keys, and service-account files.
- API-contract changes must update and verify both projects. Otherwise, run the narrowest relevant test, then the affected build when practical.
- Preserve configured Java/Kotlin toolchains unless changing them intentionally.
- Before finishing, run `git diff --check` and `git status`; report skipped checks or environment limits.
- When a change alters project layout, toolchains, canonical checks, or these invariants, update the nearest `AGENTS.md` in the same change. Keep guidance durable and do not repeat parent rules.
