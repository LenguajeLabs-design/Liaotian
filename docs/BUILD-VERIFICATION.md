# Build verification

Verified in the cloud workspace on 14 November 2026:

- Gradle 8.11.1 distribution checksum verified against the official SHA-256; wrapper includes the pinned checksum.
- Android Gradle Plugin 8.9.2, Kotlin 2.1.20, local Temurin JDK 17, Android Platform 35 and Build Tools 35.0.0.
- `assembleDebug testDebugUnitTest lintDebug`: **BUILD SUCCESSFUL**.
- JVM test: 1 test, 0 failures, 0 errors. Covers sample feedback only.
- Android lint: 0 errors; 3 warnings about newer library versions. Versions are pinned for this prototype.
- Debug APK signature verified using Android SDK apksigner.
- APK metadata: com.example.chinesechattutor, version 0.1.0, min SDK 26, target SDK 35.
- Source manifest contract and XML checked. APK has no INTERNET or dangerous runtime permission; AndroidX adds an internal signature-only receiver permission.

The build used environment-local SDK/JDK/cache/proxy paths, none of which are included in the project. An Android analytics cache warning in this restricted workspace did not prevent the build. Use Android Studio's bundled JDK and your own SDK path.

Not verified: installation, rendering, selection menu discovery, Activity results, clipboard, read-only behavior, S Pen, rotation and third-party compatibility on any physical phone or emulator. Run README's checklist on the Galaxy S25 Ultra. WeChat is explicitly untested.

The separately supplied APK matches this source build. Debug signing is for personal testing, not production distribution.
