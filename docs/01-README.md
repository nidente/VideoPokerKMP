# 🎰 VideoPoker Multiplatform

Kotlin Multiplatform project: **Android + Web (React) + iOS** in 2 weeks.

## 📊 Project Status

**Phase 1: MVP (2 weeks)**
- ✅ Week 1: KMP Setup + Shared Game Logic
- ✅ Week 2: Android Adapter + React Web App
- 🔄 Phase 2: iOS (Bonus, after MVP)

## 🚀 Quick Start

```bash
# Clone repo
git clone https://github.com/nidente/VideoPokerKMP.git
cd VideoPokerKMP

# Setup
./gradlew build

# Android
cd android && ./gradlew installDebug

# Web
cd web && npm install && npm run dev

# Read docs
open docs/01-README.md
```

## 📁 Project Structure

```
VideoPokerKMP/
├── docs/                      # 📖 All documentation
│   ├── 01-README.md          # This file
│   ├── 02-ARCHITECTURE.md    # System design & flow
│   ├── 03-SETUP_GUIDE.md     # Environment setup
│   ├── 04-WEEK1_TASKS.md     # Week 1 sprint tasks
│   ├── 05-WEEK2_TASKS.md     # Week 2 sprint tasks
│   └── 06-DEPLOYMENT.md      # Deploy to GitHub Pages
│
├── shared/                    # Kotlin Multiplatform (commonMain)
│   └── src/commonMain/kotlin/
│       └── com/example/videopoker/
│           ├── models/       # GameState, GameHistoryEntry
│           ├── engine/       # GameEngine (core logic)
│           └── util/         # Helpers
│
├── android/                   # Android (Compose UI)
│   └── src/main/java/com/example/tp4/
│       ├── MainActivity.kt    # Existing Compose UI
│       └── ...                # Keep as-is, use shared logic
│
├── web/                       # React + TypeScript
│   ├── src/
│   │   ├── components/        # React components
│   │   ├── pages/             # Page layouts
│   │   └── App.tsx            # Main app
│   └── package.json
│
└── ios/                       # iOS (SwiftUI) - Phase 2
```

## 🎯 Interactive Sprint (2 Weeks)

### Week 1: Foundations
**Goal:** Shared game logic in KMP, Android working

| Day | Task | Status |
|-----|------|--------|
| 1-2 | KMP setup + gradle | 🔄 |
| 3-4 | Extract game logic (Card, Deck, Hand, Poker, GameEngine) | ⏳ |
| 5 | Unit tests + docs | ⏳ |

### Week 2: Web MVP
**Goal:** React web app deployed, full MVP ready

| Day | Task | Status |
|-----|------|--------|
| 1-2 | React setup + game component | ⏳ |
| 3-4 | Connect to shared logic, styling | ⏳ |
| 5 | Deploy to GitHub Pages + docs | ⏳ |

## 📚 Documentation Index

| File | Purpose | Read When |
|------|---------|-----------|
| **01-README.md** | Overview (you are here) | Start here |
| **02-ARCHITECTURE.md** | System design, data flow, state machine | Before coding |
| **03-SETUP_GUIDE.md** | Install tools, verify environment | First time setup |
| **04-WEEK1_TASKS.md** | Day-by-day Week 1 sprint with code | Coding Week 1 |
| **05-WEEK2_TASKS.md** | Day-by-day Week 2 sprint with code | Coding Week 2 |
| **06-DEPLOYMENT.md** | Deploy React to GitHub Pages | End of Week 2 |

## 🔧 Tech Stack

| Layer | Tech | Why |
|-------|------|-----|
| **Shared Logic** | Kotlin Multiplatform | Code reuse, type-safe |
| **Android** | Jetpack Compose + Kotlin | Existing, proven |
| **Web** | React 18 + TypeScript | Fast, GitHub Pages hosting |
| **iOS** | SwiftUI + shared KMP | Phase 2 (optional) |
| **Build** | Gradle (KMP) + npm | Standard tooling |
| **Hosting** | GitHub Pages (free) | No cost, integrated |

## 📋 Sprint Board

**Track progress here:**
- **GitHub Issues:** Sprint tasks
- **Pull Requests:** Code reviews (interactive)
- **Discussions:** Design decisions

👉 See `/issues` and `/pulls` to follow the sprint.

## 🎓 Learning Path

**Week 1:**
- Kotlin Multiplatform basics
- commonMain/androidMain structure
- Game logic extraction

**Week 2:**
- React hooks, state management
- TypeScript integration
- Web deployment

**Phase 2:**
- SwiftUI + shared KMP logic
- iOS deployment

## 🚨 Important Notes

1. **Android:** Keep existing UI, just import shared logic
2. **Web:** GitHub Pages = static hosting (no backend needed)
3. **iOS:** Post-MVP, uses same KMP logic
4. **Interactive:** I'll create issues → PRs → you review → merge

## 📞 Need Help?

Check docs first:
1. Read **02-ARCHITECTURE.md** to understand the design
2. Read **03-SETUP_GUIDE.md** if setup fails
3. Read **04-WEEK1_TASKS.md** for step-by-step instructions
4. Check GitHub Issues for sprint context

## 🎯 Success Criteria

- ✅ Week 1: Android app compiles, game logic in commonMain, unit tests pass
- ✅ Week 2: React web app runs locally, deploys to GitHub Pages, game works
- ✅ Phase 2: iOS app using shared logic (bonus)

---

**Let's ship this in 2 weeks!** 🚀

*Last updated: Aug 15, 2026*
