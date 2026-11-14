# Chinese Chat Tutor — Phase 1

An offline native Android prototype for Android's **Improve Chinese** selection action. Kotlin, Jetpack Compose and Material 3; Android 8+ (API 26), targeting API 35. No AI, account, API key, network permission, accessibility service or message sending. All feedback is the fixed sample greeting, regardless of input. This is deliberately not yet a tutor for arbitrary sentences.

## Open and build (beginner guide)

1. Install a current stable **Android Studio** from https://developer.android.com/studio . Complete its setup wizard, including the Android SDK.
2. Extract this ZIP to a normal folder. In Android Studio choose **Open** and select `ChineseChatTutor`, the folder containing `settings.gradle.kts`. Do not create a new project or open only `app`.
3. Allow Gradle sync and dependency downloads. In Settings > Build, Execution, Deployment > Build Tools > Gradle, select the bundled JDK (17 or newer; 21 is supported by this wrapper). Under Tools > SDK Manager install **Android SDK Platform 35** and **Android SDK Build-Tools 35.0.0** if requested. First build needs internet; the installed app does not.
4. Select the `app` configuration. To create an APK, use **Build > Build Bundle(s) / APK(s) > Build APK(s)** (some Studio versions call this **Generate APKs**).
5. Find the debug APK at `app/build/outputs/apk/debug/app-debug.apk`. It is automatically debug-signed; no signing account is needed. Do not distribute this as a production release.

Command-line alternative, from this project folder:

```sh
# macOS / Linux
chmod +x gradlew
./gradlew assembleDebug testDebugUnitTest lintDebug
# With an authorized phone connected:
./gradlew installDebug
```

Windows PowerShell:

```powershell
.\gradlew.bat assembleDebug testDebugUnitTest lintDebug
.\gradlew.bat installDebug
```

Android Studio writes `local.properties` with your SDK location. For command-line-only setup, set `ANDROID_HOME` to the SDK directory or create `local.properties` containing `sdk.dir=/absolute/path/to/your/Android/Sdk` (use forward slashes on Windows). Never copy someone else's SDK path.

## Connect your Galaxy S25 Ultra

1. Phone: Settings > About phone > Software information. Tap **Build number** seven times; enter your screen-lock PIN if asked.
2. Return to Settings > **Developer options**, enable **USB debugging**.
3. Connect with a data-capable USB cable. Unlock the phone and accept the **Allow USB debugging?** RSA prompt for your trusted computer.
4. Select the phone in Android Studio's device list, then press **Run ▶**. The app installs and the test screen opens.
5. If absent: try a different data cable/USB port, select USB File transfer, install Samsung's official Windows USB driver if needed, and run `adb devices` from SDK `platform-tools`. `unauthorized` means accept the phone prompt. Organizational policy or Samsung security settings can restrict USB installations; follow your phone's displayed guidance and restore any temporarily changed setting afterward.
6. Turn USB debugging off when finished if you do not need it.

Alternative: transfer `app-debug.apk` to the phone, open it in My Files and grant that source **Install unknown apps** only when prompted. Install, then revoke that source permission. USB Run is generally easier while developing. An existing app signed with a different key may need uninstalling first.

## Test the actual Android selection action

Start with the built-in screen. Its editable field is a native `EditText`, embedded in Compose to exercise Android's real selection menu. There is no simulated replacement button bypassing the platform.

| Check | Steps | Expected result |
|---|---|---|
| Discovery | Long-press 哈喽，美女, select all, open ⋮ / More in the contextual menu | Improve Chinese appears; menu order varies |
| Actual selection | Select only 美女 and invoke the action | Original Chinese shows only 美女 |
| Replace | Load first sample; select the whole phrase; choose Use Suggestion | Field becomes 嗨，你好呀！ |
| Partial replacement | Type 今天：哈喽，美女, select only 哈喽，美女, accept | 今天：嗨，你好呀！ |
| Cancel | Load a sample, invoke, press Cancel | Original remains unchanged |
| System dismissal | Repeat, press Back or tap outside | Original remains unchanged |
| Clipboard | Invoke, tap Copy, then Cancel; paste in editable field | Clipboard contains 嗨，你好呀！; Copy alone does not replace |
| Read-only | Select the greeting under Read-only test | Original displays correctly; Use Suggestion disabled; Copy enabled |
| Other input | Select another sample | Actual original is displayed; same clearly labeled demonstration feedback |
| Lifecycle / layout | Rotate popup, open keyboard, use large font size | Content remains scrollable; buttons accessible; no automatic replacement |
| Privacy | Launch and use in airplane mode | Works with no API key or subscription |

Copy deliberately keeps the popup open so you can still inspect or accept feedback. On Android 13+ the system may also display a clipboard preview. Clipboard content may be visible to the current keyboard, system clipboard UI or explicitly pasted destinations; do not copy sensitive text unnecessarily.

### External applications and compatibility

**No physical Galaxy or WeChat test has been performed by the project generator.** Do not interpret successful compilation as device compatibility. Record real results in `docs/TESTING.md`.

After the built-in tests pass, type a harmless draft in WeChat and try selecting it. Check menu discovery, exact received text, replacement, Copy and Cancel separately. Never press Send just to test this tool. Repeat for another messaging app if desired. Test received/read-only text separately.

Apps can use custom editors, hide PROCESS_TEXT actions, omit them for some selections, or ignore returned replacement text. Samsung One UI, app versions, keyboards and S Pen selection tools may affect discovery. S Pen Smart Select / screenshot OCR is not the same as an editor's text-selection menu. An app may report editability incorrectly; this tool cannot detect whether the host actually applied a returned result. Use Copy and paste where available. If the action never appears, this app cannot force it into that app; use the built-in screen to confirm installation, then manually copy/paste a draft through that screen. No accessibility or clipboard monitoring workaround is included.

## Main files

- `app/src/main/AndroidManifest.xml`: launcher and exported PROCESS_TEXT activity, `DEFAULT`, `text/plain`, selection-action label; no requested platform permissions, backups disabled.
- `MainActivity.kt`: test bench, framework editable/read-only fields and sample phrases. Draft state survives UI recreation but is not saved as a conversation history.
- `ProcessTextActivity.kt`: validates the incoming intent, reads actual selected text and read-only flag (defaults to read-only), shows compact scrollable feedback, returns RESULT_OK only after acceptance. Other exits keep RESULT_CANCELED. Does not log or persist selections.
- `TutorFeedback.kt`: data structure and deterministic sample provider, isolated from Android integration for future replacement.
- `TutorTheme.kt`, `res/values/styles.xml`: Material 3 content and native dialog window.
- Gradle files and wrapper: pinned build toolchain; `SampleTutorTest.kt`: small JVM check of sample content, not proof of selection integration.

Android's dialog Activity remains subject to the system's multi-window, font-size and accessibility layout behavior. It does not use an overlay permission.

## Phase 2 recommendation — plan only

Proceed only once Phase 1 passes the physical-device checklist. Keep the intent/result handling and swap the sample provider for an asynchronous feedback repository. Call an HTTPS backend with short-lived user authentication; keep provider keys exclusively on the server. Do not add a key entry field or ship a shared server secret in the APK.

Use a structured response with `improvedChinese`, `pinyin`, `english`, `explanation`, optional `toneGuidance`, and an indication of grammar correction vs stylistic alternative vs already natural. Preserve meaning, tone and everyday simplified Chinese; explain in accessible English for HSK 3–4. Do not invent relationship context or rewrite already-natural text. Treat submitted text as untrusted data, not instructions to the tutor.

Require explicit notice/consent before selected text first leaves the device. Send only the selection, with no surrounding chat or clipboard scraping. Avoid conversation/request logging by default on both client and server, define retention and provider handling clearly, and apply input limits, authentication, quotas and rate limits. Show loading/error/retry states, allow cancellation, validate responses, and never enable acceptance of a stale response for a different selection. Keep Copy and Cancel available. Fail without modifying the host field; require review before returning text. No automatic sending. None of these network features are implemented here.

The merged APK also contains AndroidX’s generated signature-only `DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION` for internal receiver protection. It grants no network, accessibility, clipboard monitoring or dangerous runtime permission.
