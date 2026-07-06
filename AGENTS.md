# Vuelos EAAI — AGENTS.md

## Project

Single-module Android app that scrapes the EAAI (Managua airport) website for flight arrival/departure info. Not a REST client — uses **Jsoup HTML scraping** against `https://www.eaai.com.ni`.

## Stack

| Tool | Version |
|---|---|
| AGP | 9.2.1 |
| Kotlin | 2.3.20 |
| Gradle | 9.4.1 |
| compileSdk / targetSdk | 37 |
| minSdk | 24 |
| Java | 17 |

UI: **Jetpack Compose + Material 3** with bottom navigation (3 tabs).

## Build & Run

```bash
./gradlew.bat assembleDebug     # Windows
./gradlew.bat test               # unit tests only (no androidTest without device)
./gradlew.bat lint               # lint check
```

## Architecture

- `data/repo/FlightsRepo` — Jsoup scraper, parses HTML `<table>` / `<tr>` from EAAI websites
- `data/FlightsViewModel` — ViewModel with `StateFlow<UiState>`, calls repo on `Dispatchers.IO`
- `screens/` — `Internationals` (scraped or WebView based on Remote Config flag), `Nationals` (always WebView), `Information` + `Parking` (static info)
- `navigation/` — sealed class routes + `NavHost` with `composable()` for each screen

## Key Endpoints (hardcoded)

```
https://www.eaai.com.ni/fids/read_vuelos_dias_fids.php?option=A|D
https://www.eaai.com.ni/pvnac/vuelos_dias_pvnac.php?option=A|D
```

Remote Config can override URLs (`eaai_int_url`, `eaai_nac_url`) and toggle scraping vs WebView (`scrape_vuelos_int`).

## Firebase / Ads

- Crashlytics, Performance, Analytics, Remote Config, AdMob
- App Open ads (`AdsOpenManager` via `ProcessLifecycleOwner`), Banner ads (top), Interstitial ads (on `onPause`)
- In debug mode (`BuildConfig.DEBUG`), all ads use test AdUnit IDs from `strings.xml`
- AdMob app ID in `AndroidManifest.xml` via `@string/admob_app_id`

## Gotchas

- `FlightsRepo.getFlights()` has `@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)` but doesn't use any API from that level — it's stale and can be removed.
- `InternationalDetails` hardcodes a FlightAware URL (`AVA396`) — not dynamically linked to the selected flight.
- Filename typo: `FligthData.kt` (class inside is named `FlightData`).
- `android:usesCleartextTraffic="true"` in manifest (EAAI sites aren't HTTPS-only).
- `RemoteConfig` has a 60-second `minimumFetchIntervalInSeconds` — during dev you may want 0.
- No `google-services.json` is committed? (file exists in `app/` per directory listing but is gitignored or absent — verify before clean clone.)
- `veaai_certificate.pem` and `veaaiKey.jks` in `app/` — used for release signing.

## Tests

- `src/test/` — JUnit 4 (unit)
- `src/androidTest/` — Instrumented (Compose UI test + Espresso)
- Run `./gradlew.bat test` for unit tests; `./gradlew.bat connectedCheck` for instrumented (requires emulator/device)
