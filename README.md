# EPS Manger (Abdo Rh)

A minimal Android WebView wrapper for the live EPS Manger website. The app opens:

`https://rhpeman-z8yc.whacka.app/#/calendar?tab=schedule`

Because the pages are loaded from the live site, website changes appear when the app next loads or refreshes. The Android package itself does not need rebuilding for ordinary website content updates. The website and internet connection must remain available.

## Build an APK for free with GitHub Actions

1. Create a free GitHub account if needed, then create a **public** repository named `eps-manger-apk` (a private repository is also fine within GitHub's free allowances).
2. Upload the contents of this project folder to the repository root. Keep the `.github/workflows/android.yml` file and its folders; GitHub may hide dot-folders in some upload dialogs.
3. Push/commit the files to the `main` branch. GitHub Actions will start the build. You can also open **Actions → Build EPS Manger APK → Run workflow**.
4. Open the completed workflow run and download the `eps-manger-debug-apk` artifact. Unzip it to get `app-debug.apk`.
5. Transfer the APK to the Android phone and open it. Android may ask you to allow installation from that source.

The workflow builds a **debug APK**. It is installable for personal use and is signed automatically with Android's shared debug key. No signing secret or paid build service is needed. Do not distribute a debug build as a production release.

## Optional release signing

For a distributable release, create and securely back up your own Android keystore. Do not commit it to GitHub. Add these repository **Actions secrets**: `ANDROID_KEYSTORE_BASE64` (the keystore file encoded as Base64), `ANDROID_KEYSTORE_PASSWORD`, `ANDROID_KEY_ALIAS`, and `ANDROID_KEY_PASSWORD`; then configure a release signing build to decode/use them. Keep the keystore and passwords private and backed up: future updates to an installed release must be signed with the same key. This starter workflow intentionally produces only a debug build and requires no secrets.

## Project notes

- Display name: **EPS Manger**; developer name: **Abdo Rh**.
- Uses HTTPS only and permits in-app navigation only to the website's host. Other hosts/schemes are blocked.
- JavaScript and DOM storage are enabled because modern website applications need them. Local file/content access, mixed HTTP content, pop-up windows, and third-party cookies are disabled.
- Android Back returns through WebView history. The app has a simple Arabic offline/retry screen.
- Full screen hides browser chrome and lays content under the Android status bar. Android system navigation remains available.
- Minimum Android version: Android 6.0 (API 23).

## Build locally

Install JDK 17 and Android SDK, then run `gradle assembleDebug` from this directory. The APK is created at `app/build/outputs/apk/debug/app-debug.apk`.
