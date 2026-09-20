# Android

- Gradle configuration is authoritative for Android/Java versions; the Gradle daemon JDK is separate from app bytecode compatibility.
- Keep the app source and target compatibility on Java 17 for the current Compose and Hilt toolchain.
- Keep settings state in the `DataStore repository -> Hilt ViewModel -> lifecycle-aware Compose state` flow; composables stay stateless and do not access persistence directly.
- Navigation3 uses one back stack per top-level destination; add typed routes to the existing stacks instead of creating parallel navigation state.
- Reuse semantic `MaterialTheme` roles, `ui/theme` tokens, and shared UI components; do not duplicate settings rows or hardcode colors and layout values in screens.
- Target a relevant class with `.\gradlew.bat :app:testDebugUnitTest --tests "fully.qualified.TestClass"`; widen to `:app:testDebugUnitTest` for broader behavior changes. Add `:app:assembleDebug` for resources, manifests, dependencies, packaging, or build logic, and `:app:lintDebug` for Compose UI or accessibility changes. Pure documentation/config edits need no Gradle run.
- Let Gradle update dependency lockfiles and verification metadata; do not edit them by hand or run broad dependency reports unless the dependency graph changed.

## Architecture and ownership

- Source lives in `app/src/main/java/com/db/maintena`: `MainActivity.kt` owns navigation, `ui/settings` owns settings screens/ViewModel, `data/settings` owns the repository and DataStore, and `di/SettingsModule.kt` binds it with Hilt. Tests live under `app/src/test` and `app/src/androidTest`.
- Retrofit, Room, a domain/use-case layer, and remote synchronization are not implemented yet. Introduce only what an actual feature requires; preserve the existing simple settings flow.
- For features needing those layers, use Compose -> ViewModel -> domain/use cases (where justified) -> repository -> local/remote data sources -> Room/Retrofit. UI must not access Room, Retrofit, or DataStore directly. Keep DTOs, database entities, and domain models separate when those boundaries exist.
- For synchronized offline-first data, the local database is the UI source of truth. Remote synchronization updates local state; network errors must not erase local data or pending writes. Define retry, conflict, cancellation, and migration behavior and test changed behavior, including offline/error cases.
