# VideoPokerKMP

A casino-style Video Poker game with two front ends — an Android app (Jetpack Compose) and a web app (React + TypeScript) — sharing the same game rules and session tracking.

## Project structure

```
VideoPokerKMP/
├── shared/     Kotlin game logic (Poker/Card/Deck/Hand/GameEngine), consumed by android/
├── android/    Jetpack Compose UI, depends on :shared
├── web/        Standalone React + TypeScript port of the same game rules
└── gradlew, settings.gradle.kts, build.gradle.kts   Gradle build for shared/ + android/
```

`shared/` is a Kotlin Multiplatform module targeting only Android — it holds the poker hand
evaluation, deck/dealing logic, bet/session state machine, and history tracking used by the
Android app. `web/` is not built from `shared/`; it's a hand-written TypeScript port of the
same rules under `web/src/game/`, kept in sync manually so both front ends behave identically.

## Game rules

Standard 5-card draw video poker: deal 5 cards, choose which to hold, draw replacements for
the rest, and get paid out on the resulting hand (pair through royal flush). Both apps track
balance, bet size, and a rolling session history (rounds played, wins/losses, net gain, best
round).

## Running the Android app

Requires an Android SDK. From the repo root:

```bash
./gradlew :android:installDebug        # build + install debug APK on a connected device/emulator
./gradlew :android:assembleRelease     # build a release APK (unsigned — add a signingConfig before publishing)
```

Tests:

```bash
./gradlew :shared:testDebugUnitTest    # game logic tests (GameEngineTest, PokerTest)
./gradlew :android:lintDebug           # Android lint
```

## Running the web app

```bash
cd web
npm install
npm run dev      # dev server at http://localhost:5173
npm test         # vitest
npm run build    # production build (tsc + vite) into web/dist
npm run lint      # oxlint
```

## Tech stack

- **shared/android**: Kotlin, Jetpack Compose, Gradle
- **web**: React 19, TypeScript, Vite, Vitest, oxlint
