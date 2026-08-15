# 🚀 START HERE

**Welcome to VideoPoker Multiplatform!** This is your action plan for the next 2 weeks.

---

## ⚡ TL;DR (Read This First)

- **Goal:** Ship Android + Web Video Poker game in 2 weeks (iOS bonus)
- **Tech:** Kotlin Multiplatform (shared logic), React (web), Jetpack Compose (Android)
- **Work:** ~25-28 hours total, ~2-3 hours/day for 10 days
- **Free:** All tools and hosting are free (GitHub Pages)

---

## 📚 Documentation Order

**Read these in order (takes 1 hour):**

1. **This file** (you're reading it) - Action plan overview
2. **docs/02-ARCHITECTURE.md** - Understand system design before coding
3. **docs/03-SETUP_GUIDE.md** - Verify your environment is ready
4. **docs/04-WEEK1_TASKS.md** - Detailed 5-day Week 1 sprint
5. **docs/05-WEEK2_TASKS.md** - Detailed 5-day Week 2 sprint

**Reference as needed:**
- **docs/06-DEPLOYMENT.md** - How to deploy when done
- **ISSUES_TEMPLATE.md** - Create GitHub issues for tracking

---

## ✅ Pre-Work Checklist (Do This Today)

```bash
# 1. Verify environment
java -version           # Should be 11+
git --version          # Should be 2.x+
node --version         # Should be 16+
npm --version          # Should be 8+

# 2. Clone this repo
cd ~/projects
git clone https://github.com/nidente/VideoPokerKMP.git
cd VideoPokerKMP

# 3. Read the docs (1 hour)
# Start with 02-ARCHITECTURE.md

# 4. Create GitHub issues (optional but recommended)
# Use ISSUES_TEMPLATE.md to create sprint tasks on GitHub
```

---

## 🎯 Week 1: Foundation (5 Days, 12-14 hours)

**What:** Extract game logic to Kotlin Multiplatform

**Daily Schedule:**

| Day | Task | Hours | Deliverable |
|-----|------|-------|-------------|
| **Mon** | 1.1-1.6: KMP Setup | 2-3h | Gradle files, folder structure |
| **Tue** | 1.6-2.1: First commit | 1h | Code in GitHub |
| **Wed** | 2.1-2.5: Game Logic | 3-4h | Card, Deck, Hand, GameEngine |
| **Thu** | 2.5-3.1: Tests | 2-3h | Unit tests passing |
| **Fri** | 3.2-3.3: Docs & Push | 1h | Final commit & GitHub ready |

**Success Criteria:**
- ✅ Android app still compiles
- ✅ Game logic in `shared/src/commonMain`
- ✅ Unit tests pass
- ✅ Code pushed to GitHub

**Get Started:**
1. Read **docs/04-WEEK1_TASKS.md** (careful, it's detailed!)
2. Follow Tasks 1.1 → 1.2 → 1.3 ... → 3.3 in order
3. Each task has step-by-step bash commands and code templates
4. Commit after each major task
5. Push when done with full day

---

## 🎯 Week 2: MVP Release (5 Days, 12-14 hours)

**What:** Build React web app, deploy to GitHub Pages

**Daily Schedule:**

| Day | Task | Hours | Deliverable |
|-----|------|-------|-------------|
| **Mon** | 4.1-4.4: React Setup | 2-3h | React dev server running |
| **Tue** | 4.5-4.8: Components | 3-4h | All 5 game screens built |
| **Wed** | 4.9-4.10: Styling | 2-3h | Responsive CSS done |
| **Thu** | 4.11-4.12: Deploy | 2-3h | Live on GitHub Pages |
| **Fri** | 4.13: Docs & Closeout | 1h | Documentation complete |

**Success Criteria:**
- ✅ Web app runs locally
- ✅ All 4 game screens working
- ✅ Mobile-responsive design
- ✅ Live at `https://nidente.github.io/VideoPokerKMP/`

**Get Started:**
1. Read **docs/05-WEEK2_TASKS.md**
2. Follow Tasks 4.1 → 4.2 → 4.3 ... → 4.13 in order
3. Copy-paste code templates from the guide
4. Commit daily
5. Deploy end of Friday

---

## 💡 Tips for Success

### Time Management
- ⏰ Work in 1-2 hour focused blocks
- ⏰ Take 10-15 min breaks between tasks
- ⏰ Commit at end of each task (backup progress)
- ⏰ Sunday-Friday work schedule = 2 weeks exactly

### Debugging
- 🐛 Read error messages carefully (they're usually helpful)
- 🐛 Check Gradle/npm version if build fails
- 🐛 See **docs/03-SETUP_GUIDE.md** for common issues
- 🐛 Don't skip steps in the task guides

### Learning While Building
- 📖 You'll learn KMP concepts as you build
- 📖 Pause and read Kotlin docs if confused
- 📖 Type code out, don't just copy-paste (better learning)
- 📖 Ask AI/ChatGPT if Kotlin syntax is unclear

### Code Quality
- ✅ Don't worry about perfection, ship working code
- ✅ Tests are your safety net (run them!)
- ✅ Comments only for tricky logic
- ✅ Follow folder structure exactly (prevents errors)

---

## 🤖 What We Built (Summary)

### Shared KMP Logic (Both Android + Web)
```
shared/src/commonMain/
├── models/
│   ├── GameState.kt (CONFIG, MISE, CHOIX, GAIN)
│   ├── GameModels.kt (data classes)
│   └── PokerHand.kt (enums)
├── engine/
│   ├── GameEngine.kt (main orchestrator)
│   ├── Card.kt, Deck.kt, Hand.kt (game pieces)
│   └── Constants.kt (payouts)
└── util/
    └── Helpers.kt (shuffling, random)
```

### Android (Existing Compose UI)
```
android/
├── src/main/java/com/example/tp4/
│   ├── MainActivity.kt (existing, just imports GameEngine)
│   ├── UI screens (keep as-is)
│   └── build.gradle.kts (imports :shared)
```

### Web React App (NEW)
```
web/
├── src/
│   ├── components/ (ConfigScreen, MiseScreen, etc.)
│   ├── hooks/ (useGameEngine)
│   ├── engine/ (GameEngine TypeScript wrapper)
│   ├── styles/ (CSS)
│   └── App.tsx
├── package.json (React + Vite + gh-pages)
└── vite.config.ts (GitHub Pages config)
```

---

## 🚀 Phase 2 (After Week 2 - BONUS)

Once Android + Web MVP is shipped:

- **Option 1:** iOS app with SwiftUI (8-10 hours)
- **Option 2:** Features (animations, sounds, statistics)
- **Option 3:** Backend (score database, multiplayer)

We'll decide after Week 2!

---

## 📞 Troubleshooting Quick Links

| Issue | Solution |
|-------|----------|
| Java not found | **docs/03-SETUP_GUIDE.md** → "Java not found" |
| Gradle build fails | See **docs/03-SETUP_GUIDE.md** → "gradlew permission" |
| npm install fails | Clear cache: `npm cache clean --force` |
| Can't find code | Check folder path matches guide exactly |
| Tests fail | Run: `./gradlew clean build` then `./gradlew test` |

---

## 🎯 Success Metrics

**Week 1 ✅**
- [ ] Gradle project builds without errors
- [ ] GameEngine.kt compiles and unit tests pass
- [ ] Android app still runs (adapts shared logic)
- [ ] All code committed to GitHub

**Week 2 ✅**
- [ ] React dev server runs on localhost:5173
- [ ] All 4 game screens appear
- [ ] Styling complete and mobile-responsive
- [ ] Web app deployed and live on GitHub Pages
- [ ] Android + Web both playable

**Phase 2 ✅** (Bonus)
- [ ] iOS app in TestFlight (or working on simulator)

---

## 📅 Timeline at a Glance

```
Week 1 (Aug 18-22)
├─ Mon-Tue: KMP setup (Days 1-2)
├─ Wed-Thu: Extract logic + tests (Days 3-5)
└─ Fri: Final commit + docs

Week 2 (Aug 25-29)
├─ Mon-Tue: React components (Days 1-2)
├─ Wed: Styling (Day 3)
├─ Thu: Deploy (Day 4)
└─ Fri: Docs + closeout

Phase 2 (Sept 1-7) - Optional
└─ iOS or features
```

---

## 🎓 What You'll Learn

**Kotlin & KMP:**
- Multiplatform project structure
- commonMain shared code
- Platform-specific modules (androidMain, jsMain)
- Gradle multiplatform configuration

**React & TypeScript:**
- Functional components and hooks
- State management (useState)
- Component composition
- CSS styling & responsive design

**Deployment:**
- GitHub Pages hosting
- GitHub Actions CI/CD (optional)
- App Store (Phase 2)

**Bonus:**
- How to architect games (state machines)
- Code reuse across platforms
- Testing game logic

---

## 🎉 Let's Ship This!

You have everything you need:
- ✅ Complete documentation
- ✅ Step-by-step tasks
- ✅ Code templates (copy-paste ready)
- ✅ Git commit messages
- ✅ Deployment guides
- ✅ Troubleshooting help

**Next Step:** Open **docs/02-ARCHITECTURE.md** and read for 15 minutes.

**Then:** Read **docs/03-SETUP_GUIDE.md** and verify your environment.

**Then:** Dive into **docs/04-WEEK1_TASKS.md** and start building!

---

**You've got this! 🚀**

*Questions? Re-read the relevant doc section. Stuck? Check ISSUES_TEMPLATE.md for common patterns.*

**Last updated:** Aug 15, 2026
