# Android project instructions

- Open this directory in Android Studio if desired, but keep `C:\src\Maintena3` as the Git root.
- Use `maintena-android\.\gradlew.bat` for builds and tests.
- The Android Java source and target compatibility is 11. The Gradle daemon JDK/toolchain is configured separately; do not infer the app bytecode target from the IDE runtime.
- Do not commit `local.properties`, `.idea`, `.gradle`, `build`, `.kotlin`, signing files, or generated reports.
- For Kotlin/Compose or resource changes, run `.\gradlew.bat testDebugUnitTest` and, when practical, `.\gradlew.bat assembleDebug`.
- Keep Android and server API models/endpoints synchronized when changing the network contract.
