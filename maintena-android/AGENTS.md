# Android

- Gradle configuration is authoritative for Android/Java versions; the Gradle daemon JDK is separate from app bytecode compatibility.
- Run `.\gradlew.bat testDebugUnitTest`; add `.\gradlew.bat assembleDebug` for resources, manifests, dependencies, packaging, or build-logic changes.
