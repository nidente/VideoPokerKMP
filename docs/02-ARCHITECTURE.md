# 📐 Architecture

## System Overview

```
┌─────────────────────────────────────────────────────────┐
│                  User Interface Layer                   │
├─────────────────────────────────────────────────────────┤
│  Android (Compose)    │    Web (React)    │   iOS (Swift)
└───────────┬───────────┴───────┬───────────┴──────┬──────┘
            │                   │                  │
      ┌─────▼───────────────────▼──────────────────▼────┐
      │     KMP Platform Abstraction Layer              │
      │  (share game logic across all platforms)        │
      └─────┬───────────────────┬──────────────────┬────┘
            │                   │                  │
      ┌─────▼─────┐       ┌─────▼─────┐      ┌────▼────┐
      │ androidMain       │  jsMain   │      │ iosMain │
      │ (Kotlin)   │      │ (Kotlin)  │      │ (Kotlin)│
      └─────┬─────┘       └─────┬─────┘      └────┬────┘
            │                   │                  │
      ┌─────▼───────────────────▼──────────────────▼────┐
      │         commonMain (Shared Kotlin)              │
      │  ┌──────────────────────────────────────────┐  │
      │  │  Game Models: GameState, GameEngine      │  │
      │  │  Logic: Card, Deck, Hand, Poker          │  │
      │  │  Storage: GameHistoryEntry                │  │
      │  └──────────────────────────────────────────┘  │
      └─────────────────────────────────────────────────┘
```

## Data Flow: Game State Machine

```
CONFIG ──── user selects stakes ───→ MISE
  │                                    │
  │ (show config                       │ (show current bet,
  │  menu)                             │  enable card select)
  │                                    │
  └─ player quits                  CHOIX
                                        │
                                        │ user selects cards
                                        │ to hold/discard
                                        │
                                    GAIN
                                        │
                                        │ (show winnings,
                                        │  update balance)
                                        │
                                    ┌───┴──────┐
                                    │           │
                                play again    quit
                                    │
                                 CONFIG
```

## Game Logic Flow

```
GameEngine.playRound()
├─ 1. showCurrentBalance()
├─ 2. requestBet()
│   └─ player selects stake (CONFIG state)
├─ 3. dealInitialCards()
│   ├─ shuffle deck
│   ├─ deal 5 cards
│   └─ transition to MISE state
├─ 4. requestCardSelection()
│   └─ player selects cards to hold (CHOIX state)
├─ 5. replaceUnheldCards()
│   ├─ draw replacement cards
│   └─ evaluate hand
├─ 6. determineWinnings()
│   ├─ check hand rank (Pair, Three-of-a-Kind, etc.)
│   ├─ calculate payout = bet × multiplier
│   └─ update balance
├─ 7. recordHistory()
│   └─ save GameHistoryEntry
└─ 8. transitionToGain()
    └─ display result, wait for next action
```

## Code Organization

### commonMain (Kotlin - Shared Across All Platforms)

```
shared/src/commonMain/kotlin/com/example/videopoker/
├── models/
│   ├── GameState.kt           # enum: CONFIG, MISE, CHOIX, GAIN
│   ├── GameModels.kt          # data classes for state
│   └── PokerHand.kt           # enum for hand ranks
│
├── engine/
│   ├── GameEngine.kt          # Main logic orchestrator
│   ├── Deck.kt                # Shuffling, dealing
│   ├── Hand.kt                # Card evaluation
│   └── Card.kt                # Card definition
│
└── util/
    └── Constants.kt           # Payout tables, configs
```

### Platform-Specific

- **androidMain:** Minimal (only if needed)
- **jsMain:** Kotlin/JS runtime interop
- **iosMain:** Minimal (Phase 2)

## Single Source of Truth: GameEngine

```kotlin
class GameEngine {
    // Private state
    private var gameState: GameState = CONFIG
    private var balance: Int = 100
    private var currentBet: Int = 0
    private var deck: Deck = Deck()
    private var currentHand: Hand = Hand()
    private var history: MutableList<GameHistoryEntry> = mutableListOf()
    
    // Public API - UI calls these
    fun placeBet(amount: Int): Boolean { ... }
    fun dealInitialCards(): List<Card> { ... }
    fun holdCards(indices: List<Int>): List<Card> { ... }
    fun getGameView(): GameViewState { ... }
    fun getBalance(): Int { ... }
    fun getHistory(): List<GameHistoryEntry> { ... }
}
```

### UI Pattern (All Platforms)

```
UI calls:               GameEngine returns:
placeBet(50)     →      GameViewState (state=MISE, balance=50)
holdCards([1,2]) →      GameViewState (state=GAIN, winnings=100)
reset()          →      GameViewState (state=CONFIG, balance=150)
```

**Key:** UI is **read-only** to game state. No direct state mutations from UI.

## Testing Strategy

### Unit Tests (commonMain)

```kotlin
shared/src/commonTest/kotlin/com/example/videopoker/

- GameEngineTest.kt          # State transitions, bet validation
- HandEvaluationTest.kt      # Poker hand ranking
- DeckTest.kt                # Card shuffling, dealing
- PayoutCalculationTest.kt   # Payout logic
```

**All game logic tested in one place**, before platforms deploy it.

### UI Tests (Platform-Specific, Phase 2)

- Android: Compose UI tests
- Web: Vitest/RTL tests
- iOS: XCTest (Phase 2)

---

**Next:** Read **03-SETUP_GUIDE.md** to prepare your environment.
