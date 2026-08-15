# 📋 Sprint Board Template

Use this to create issues on GitHub. Copy-paste into GitHub Issues.

---

## Week 1: Foundation Sprint

### Week 1 - Task 1.1: KMP Project Setup
```
Title: [Week1-Day1] Initialize KMP project structure

Labels: week-1, setup, kmp

Description:
Create folder structure and gradle files for multiplatform Kotlin project.

Tasks:
- [ ] Create shared/, android/, web/ module directories
- [ ] Create common/androidMain/jsMain source folders
- [ ] Create root build.gradle.kts
- [ ] Create settings.gradle.kts
- [ ] Create shared/build.gradle.kts with multiplatform config
- [ ] Create android/build.gradle.kts

Deliverable: Folder structure + gradle files
Time: 1-2 hours
```

### Week 1 - Task 2.1: Extract Game Logic
```
Title: [Week1-Day3] Extract game logic to commonMain

Labels: week-1, kmp, core-logic

Description:
Copy and adapt Card, Deck, Hand classes from existing Android project to KMP commonMain.

Files:
- Card.kt (Suit, Rank enums + data class)
- Deck.kt (shuffle, deal methods)
- Hand.kt (card evaluation, poker ranking)

Deliverable: Game logic in shared/src/commonMain/
Time: 1.5-2 hours
```

### Week 1 - Task 3.1: GameEngine Implementation
```
Title: [Week1-Day4] Implement GameEngine orchestrator

Labels: week-1, kmp, core-logic

Description:
Create main GameEngine class that orchestrates game state and logic.

Files:
- GameModels.kt (GameState enum, GameHistoryEntry, GameViewState)
- GameEngine.kt (state machine, game flow)

Deliverable: GameEngine compiles, unit tests pass
Time: 2-2.5 hours
```

### Week 1 - Task 4.1: Unit Tests
```
Title: [Week1-Day5] Write core unit tests

Labels: week-1, testing

Description:
Write unit tests for GameEngine, Hand evaluation, payout calculation.

Files:
- GameEngineTest.kt
- HandEvaluationTest.kt
- PayoutCalculationTest.kt

Deliverable: 5+ tests passing
Time: 1.5-2 hours
```

---

## Week 2: MVP Release Sprint

### Week 2 - Task 4.2: React Web Setup
```
Title: [Week2-Day1] Initialize React + TypeScript web app

Labels: week-2, react, web

Description:
Create React project with TypeScript, install dependencies.

Steps:
- [ ] Create web folder with React + Vite
- [ ] Install dependencies
- [ ] Verify npm dev server runs
- [ ] Create GameEngine TypeScript wrapper

Deliverable: React dev server running on localhost:5173
Time: 1-1.5 hours
```

### Week 2 - Task 4.3: React Components
```
Title: [Week2-Day2] Build React game screen components

Labels: week-2, react, ui

Description:
Create ConfigScreen, MiseScreen, ChoixScreen, GainScreen components.

Files:
- ConfigScreen.tsx
- MiseScreen.tsx
- ChoixScreen.tsx
- GainScreen.tsx
- CardDisplay.tsx

Deliverable: All screens render, no logic errors
Time: 2-2.5 hours
```

### Week 2 - Task 4.4: Styling & Polish
```
Title: [Week2-Day3] Add CSS styling and responsive design

Labels: week-2, react, styling

Description:
Implement CSS for all game screens, ensure mobile-responsive.

Files:
- Game.module.css (main styling)
- App.css (global styles)

Deliverable: All screens styled, mobile-responsive
Time: 1.5-2 hours
```

### Week 2 - Task 4.5: GitHub Pages Deployment
```
Title: [Week2-Day5] Deploy React web app to GitHub Pages

Labels: week-2, deployment, web

Description:
Configure GitHub Pages hosting and deploy web app.

Steps:
- [ ] Install gh-pages package
- [ ] Configure vite.config.ts with base URL
- [ ] Run npm run deploy
- [ ] Verify live at GitHub Pages

Deliverable: Web app live at https://nidente.github.io/VideoPokerKMP/
Time: 1-1.5 hours
```

---

## Phase 2: iOS (Bonus)

### Phase 2 - Task: iOS App Development
```
Title: [Phase2] iOS app with SwiftUI + shared KMP

Labels: phase-2, ios, swiftui

Description:
Create iOS app using SwiftUI, import shared KMP game logic.

Requirements:
- SwiftUI UI (similar to Web/Android)
- Use shared GameEngine
- Support iPhone + iPad
- Deploy to App Store

Deliverable: iOS app in TestFlight
Time: 8-10 hours
```

---

## How to Use This Template

1. Copy each issue text
2. Go to GitHub repo → Issues → New Issue
3. Paste title and description
4. Add labels
5. Assign to yourself
6. Create issue

---

**Total Work:** ~26-28 hours across 2 weeks
**Status:** Ready to start Week 1! 🚀
