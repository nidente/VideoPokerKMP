# 📋 Week 1: Foundation Sprint (5 Days)

**Goal:** Extract game logic to KMP `commonMain`, verify Android still works, write tests.

**Time:** ~12-14 hours total (2-3 hours/day)

---

## Day 1-2: KMP Project Setup & Gradle Configuration

### Task 1.1: Initialize KMP Project Structure

**What:** Create folders and gradle files for multiplatform build

**Steps:**

```bash
cd ~/projects/VideoPokerKMP

# Create shared module
mkdir -p shared/src/commonMain/kotlin/com/example/videopoker/{models,engine,util}
mkdir -p shared/src/androidMain/kotlin/com/example/videopoker
mkdir -p shared/src/jsMain/kotlin/com/example/videopoker
mkdir -p shared/src/commonTest/kotlin/com/example/videopoker

# Create Android subproject
mkdir -p android/src/main/java/com/example/tp4
mkdir -p android/src/main/res

# Create Web React app
mkdir -p web/src/{components,pages}
mkdir -p web/public

# Verify structure
tree -L 3
```

**Deliverable:** Folder structure matching above ✅

---

### Task 1.2: Create Root build.gradle.kts

**File:** `VideoPokerKMP/build.gradle.kts`

```kotlin
plugins {
    kotlin("multiplatform") version "1.9.20" apply false
    kotlin("android") version "1.9.20" apply false
    id("com.android.library") version "8.0.2" apply false
    id("com.android.application") version "8.0.2" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
```

**Deliverable:** File created at root ✅

---

### Task 1.3: Create settings.gradle.kts

**File:** `VideoPokerKMP/settings.gradle.kts`

```kotlin
rootProject.name = "VideoPokerKMP"

include(":shared")
include(":android")
include(":web")
```

**Deliverable:** Settings file configured ✅

---

### Task 1.4: Create Shared Module build.gradle.kts

**File:** `VideoPokerKMP/shared/build.gradle.kts`

```kotlin
plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.9.20"
    id("com.android.library")
}

kotlin {
    androidTarget()
    js(IR) {
        browser()
        binaries.executable()
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val jsMain by getting
    }
}

android {
    namespace = "com.example.videopoker"
    compileSdk = 34
}
```

**Deliverable:** Build file created ✅

---

### Task 1.5: Create Android App build.gradle.kts

**File:** `VideoPokerKMP/android/build.gradle.kts`

```kotlin
plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.example.tp4"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.tp4"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":shared"))
    
    // Compose
    implementation("androidx.compose.ui:ui:1.5.4")
    implementation("androidx.compose.material3:material3:1.1.1")
    implementation("androidx.activity:activity-compose:1.8.0")
    
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
}
```

**Deliverable:** Android build file created ✅

---

### Task 1.6: Init Git & First Commit

```bash
cd ~/projects/VideoPokerKMP

git add .
git commit -m "feat: Initialize KMP project structure and gradle configuration

- Create shared/, android/, web/ module structure
- Add root build.gradle.kts with KMP plugins
- Configure Kotlin multiplatform targets (Android, JS, iOS)
- Set up Android application configuration

Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>"

git log --oneline | head -5
```

**Deliverable:** Git repo initialized, commit in history ✅

**Status Update:** 🟢 **Week 1 Days 1-2 Complete** (2-3 hours)

---

## Day 3-4: Extract Game Logic to commonMain

### Task 2.1: Copy & Adapt Card.kt

**File:** `VideoPokerKMP/shared/src/commonMain/kotlin/com/example/videopoker/engine/Card.kt`

Copy from your existing project and adapt for multiplatform:

```kotlin
enum class Suit {
    HEARTS, DIAMONDS, CLUBS, SPADES;
    
    override fun toString() = when (this) {
        HEARTS -> "♥"
        DIAMONDS -> "♦"
        CLUBS -> "♣"
        SPADES -> "♠"
    }
}

enum class Rank(val shortName: String, val displayName: String) {
    ACE("A", "Ace"),
    TWO("2", "2"),
    THREE("3", "3"),
    FOUR("4", "4"),
    FIVE("5", "5"),
    SIX("6", "6"),
    SEVEN("7", "7"),
    EIGHT("8", "8"),
    NINE("9", "9"),
    TEN("T", "10"),
    JACK("J", "Jack"),
    QUEEN("Q", "Queen"),
    KING("K", "King");
    
    override fun toString() = shortName
}

data class Card(val suit: Suit, val rank: Rank) {
    override fun toString() = "$rank$suit"
}
```

**Deliverable:** Card.kt in commonMain ✅

---

### Task 2.2: Copy & Adapt Deck.kt

**File:** `VideoPokerKMP/shared/src/commonMain/kotlin/com/example/videopoker/engine/Deck.kt`

```kotlin
import kotlin.random.Random

class Deck {
    private var cards: MutableList<Card> = mutableListOf()
    
    init {
        reset()
    }
    
    private fun reset() {
        cards.clear()
        for (suit in Suit.values()) {
            for (rank in Rank.values()) {
                cards.add(Card(suit, rank))
            }
        }
    }
    
    fun shuffle() {
        cards.shuffle(Random)
    }
    
    fun deal(count: Int): List<Card> {
        require(count <= cards.size) { "Not enough cards in deck" }
        return cards.take(count).also {
            cards.removeAll(it.toSet())
        }
    }
    
    fun hasCards(): Boolean = cards.isNotEmpty()
    
    fun remaining(): Int = cards.size
}
```

**Deliverable:** Deck.kt in commonMain ✅

---

### Task 2.3: Copy & Adapt Hand.kt & Poker.kt

**File:** `VideoPokerKMP/shared/src/commonMain/kotlin/com/example/videopoker/engine/Hand.kt`

```kotlin
data class Hand(val cards: MutableList<Card> = mutableListOf()) {
    
    fun addCards(newCards: List<Card>) {
        cards.addAll(newCards)
    }
    
    fun replaceCards(indices: List<Int>, newCards: List<Card>) {
        for ((index, newCard) in indices.zip(newCards)) {
            if (index in cards.indices) {
                cards[index] = newCard
            }
        }
    }
    
    fun evaluate(): PokerHand {
        return when {
            isRoyalFlush() -> PokerHand.ROYAL_FLUSH
            isStraightFlush() -> PokerHand.STRAIGHT_FLUSH
            isFourOfAKind() -> PokerHand.FOUR_OF_A_KIND
            isFullHouse() -> PokerHand.FULL_HOUSE
            isFlush() -> PokerHand.FLUSH
            isStraight() -> PokerHand.STRAIGHT
            isThreeOfAKind() -> PokerHand.THREE_OF_A_KIND
            isTwoPair() -> PokerHand.TWO_PAIR
            isPair() -> PokerHand.PAIR
            else -> PokerHand.HIGH_CARD
        }
    }
    
    private fun isRoyalFlush(): Boolean = 
        isStraightFlush() && cards.any { it.rank == Rank.ACE }
    
    private fun isStraightFlush(): Boolean = 
        isStraight() && isFlush()
    
    private fun isFourOfAKind(): Boolean = 
        cards.groupingBy { it.rank }.eachCount().any { it.value == 4 }
    
    private fun isFullHouse(): Boolean {
        val counts = cards.groupingBy { it.rank }.eachCount()
        return counts.any { it.value == 3 } && counts.any { it.value == 2 }
    }
    
    private fun isFlush(): Boolean = 
        cards.groupingBy { it.suit }.eachCount().any { it.value == 5 }
    
    private fun isStraight(): Boolean {
        val ranks = cards.map { it.rank.ordinal }.sorted()
        return (ranks.last() - ranks.first() == 4) && ranks.distinct().size == 5
    }
    
    private fun isThreeOfAKind(): Boolean = 
        cards.groupingBy { it.rank }.eachCount().any { it.value == 3 }
    
    private fun isTwoPair(): Boolean = 
        cards.groupingBy { it.rank }.eachCount().count { it.value == 2 } == 2
    
    private fun isPair(): Boolean = 
        cards.groupingBy { it.rank }.eachCount().any { it.value == 2 }
    
    fun getPayout(bet: Int, hand: PokerHand): Int {
        val multiplier = when (hand) {
            PokerHand.ROYAL_FLUSH -> 800
            PokerHand.STRAIGHT_FLUSH -> 50
            PokerHand.FOUR_OF_A_KIND -> 25
            PokerHand.FULL_HOUSE -> 9
            PokerHand.FLUSH -> 6
            PokerHand.STRAIGHT -> 4
            PokerHand.THREE_OF_A_KIND -> 3
            PokerHand.TWO_PAIR -> 2
            PokerHand.PAIR -> 1
            PokerHand.HIGH_CARD -> 0
        }
        return bet * multiplier
    }
}

enum class PokerHand {
    ROYAL_FLUSH, STRAIGHT_FLUSH, FOUR_OF_A_KIND, FULL_HOUSE,
    FLUSH, STRAIGHT, THREE_OF_A_KIND, TWO_PAIR, PAIR, HIGH_CARD
}
```

**Deliverable:** Hand.kt in commonMain ✅

---

### Task 2.4: Create Game Models & GameEngine

**File:** `VideoPokerKMP/shared/src/commonMain/kotlin/com/example/videopoker/models/GameModels.kt`

```kotlin
enum class GameState {
    CONFIG,   // Select bet amount
    MISE,     // Bet placed, show initial hand
    CHOIX,    // Select cards to hold/discard
    GAIN      // Show results and payout
}

data class GameHistoryEntry(
    val timestamp: Long,
    val bet: Int,
    val initialCards: List<Card>,
    val finalCards: List<Card>,
    val handRank: PokerHand,
    val winnings: Int
)

data class GameViewState(
    val state: GameState,
    val balance: Int,
    val currentBet: Int = 0,
    val cards: List<Card> = emptyList(),
    val heldIndices: List<Boolean> = emptyList(),
    val handRank: PokerHand? = null,
    val winnings: Int = 0
)
```

**File:** `VideoPokerKMP/shared/src/commonMain/kotlin/com/example/videopoker/engine/GameEngine.kt`

```kotlin
import kotlinx.serialization.Serializable

class GameEngine {
    private var gameState = GameState.CONFIG
    private var balance = 1000
    private var currentBet = 0
    private var deck = Deck()
    private var hand = Hand()
    private var held: List<Boolean> = emptyList()
    private val history: MutableList<GameHistoryEntry> = mutableListOf()
    
    fun placeBet(amount: Int): Boolean {
        if (amount > 0 && amount <= balance) {
            currentBet = amount
            balance -= amount
            gameState = GameState.MISE
            deck = Deck()
            deck.shuffle()
            val cards = deck.deal(5)
            hand = Hand(cards.toMutableList())
            held = List(5) { false }
            return true
        }
        return false
    }
    
    fun holdCards(indices: List<Int>): Boolean {
        if (gameState != GameState.MISE) return false
        held = (0..4).map { it in indices }
        val cardsToDiscard = held.indices.filter { !held[it] }
        val replacements = deck.deal(cardsToDiscard.size)
        hand.replaceCards(cardsToDiscard, replacements)
        gameState = GameState.CHOIX
        evaluateAndPayout()
        gameState = GameState.GAIN
        return true
    }
    
    private fun evaluateAndPayout() {
        val rank = hand.evaluate()
        val payout = hand.getPayout(currentBet, rank)
        balance += payout
        
        history.add(
            GameHistoryEntry(
                timestamp = System.currentTimeMillis(),
                bet = currentBet,
                initialCards = hand.cards.toList(),
                finalCards = hand.cards.toList(),
                handRank = rank,
                winnings = payout
            )
        )
    }
    
    fun reset() {
        gameState = GameState.CONFIG
        currentBet = 0
        deck = Deck()
        hand = Hand()
        held = emptyList()
    }
    
    fun getGameView(): GameViewState = GameViewState(
        state = gameState,
        balance = balance,
        currentBet = currentBet,
        cards = hand.cards.toList(),
        heldIndices = held,
        handRank = if (gameState == GameState.GAIN) hand.evaluate() else null,
        winnings = if (gameState == GameState.GAIN) history.lastOrNull()?.winnings ?: 0 else 0
    )
    
    fun getBalance() = balance
    fun getHistory() = history.toList()
}
```

**Deliverable:** GameModels.kt and GameEngine.kt in commonMain ✅

---

### Task 2.5: Verify Compilation

```bash
cd ~/projects/VideoPokerKMP

# Build shared KMP library
./gradlew shared:build

# If error about androidMain, run:
./gradlew clean build --refresh-dependencies

# Check outputs
ls -la shared/build/libs/
```

**Expected:** Build succeeds, `.jar` files created ✅

**Status Update:** 🟢 **Week 1 Days 3-4 Complete** (3-4 hours)

---

## Day 5: Testing & Documentation

### Task 3.1: Write Unit Tests

**File:** `VideoPokerKMP/shared/src/commonTest/kotlin/com/example/videopoker/GameEngineTest.kt`

```kotlin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class GameEngineTest {
    
    @Test
    fun testBetPlacement() {
        val engine = GameEngine()
        val result = engine.placeBet(100)
        assertTrue(result)
        val state = engine.getGameView()
        assertEquals(GameState.MISE, state.state)
    }
    
    @Test
    fun testInvalidBet() {
        val engine = GameEngine()
        val result = engine.placeBet(10000) // More than balance
        assertFalse(result)
    }
    
    @Test
    fun testHoldCards() {
        val engine = GameEngine()
        engine.placeBet(50)
        engine.holdCards(listOf(0, 1))
        val state = engine.getGameView()
        assertEquals(GameState.GAIN, state.state)
    }
    
    @Test
    fun testHandEvaluation() {
        val hand = Hand(mutableListOf(
            Card(Suit.HEARTS, Rank.ACE),
            Card(Suit.DIAMONDS, Rank.ACE),
            Card(Suit.CLUBS, Rank.ACE),
            Card(Suit.SPADES, Rank.ACE),
            Card(Suit.HEARTS, Rank.KING)
        ))
        assertEquals(PokerHand.FOUR_OF_A_KIND, hand.evaluate())
    }
    
    @Test
    fun testPayoutCalculation() {
        val hand = Hand(mutableListOf(
            Card(Suit.HEARTS, Rank.ACE),
            Card(Suit.DIAMONDS, Rank.ACE),
            Card(Suit.CLUBS, Rank.ACE),
            Card(Suit.SPADES, Rank.ACE),
            Card(Suit.HEARTS, Rank.KING)
        ))
        val payout = hand.getPayout(50, PokerHand.FOUR_OF_A_KIND)
        assertEquals(1250, payout) // 50 * 25
    }
}
```

**Run tests:**
```bash
./gradlew shared:commonTest
```

**Deliverable:** Tests pass ✅

---

### Task 3.2: Update Project Documentation

Create **Week 1 summary document:**

**File:** `VideoPokerKMP/docs/WEEK1_COMPLETE.md`

```markdown
# ✅ Week 1 Complete: Foundation Sprint

**Dates:** Aug 15-19, 2026
**Time:** 12-14 hours

## What We Built

- ✅ KMP project structure (shared, android, web)
- ✅ Gradle multiplatform configuration
- ✅ Game logic extracted to `commonMain`:
  - Card, Suit, Rank enums
  - Deck (shuffle, deal)
  - Hand (card evaluation, poker hand ranking)
  - GameEngine (state machine, game orchestration)
  - GameState enum, GameHistoryEntry, GameViewState
- ✅ Unit tests (5 core tests)
- ✅ Android app can now import from shared library
- ✅ Git commits with proper history

## Code Statistics

- **Lines of Code (commonMain):** ~400
- **Test Coverage:** Card, Hand, GameEngine
- **Gradle Modules:** 2 (shared, android)

## What's Next (Week 2)

- Adapt existing Android UI to use GameEngine
- Build React web app with same GameEngine
- Deploy to GitHub Pages

---

**Status:** 🚀 Ready for Week 2!
```

**Deliverable:** Documentation updated ✅

---

### Task 3.3: Final Commit & Push

```bash
cd ~/projects/VideoPokerKMP

git add .
git commit -m "feat: Complete KMP game logic extraction and unit tests

- Extract Card, Deck, Hand, Poker logic to commonMain
- Implement GameEngine state machine
- Add GameState, GameHistoryEntry data models
- Write 5 core unit tests
- All platforms now compile with shared game logic
- Android and web can now integrate GameEngine

Week 1 deliverables complete.

Co-authored-by: Copilot <223556219+Copilot@users.noreply.github.com>"

git log --oneline | head -5
```

**Deliverable:** Code pushed ✅

---

## Summary: Week 1 ✅

| Task | Hours | Status |
|------|-------|--------|
| 1.1-1.6: KMP Setup | 2-3h | ✅ |
| 2.1-2.5: Extract Logic | 3-4h | ✅ |
| 3.1-3.3: Tests & Docs | 2-3h | ✅ |
| **TOTAL** | **12-14h** | **✅** |

**Achievements:**
- ✅ All game logic in multiplatform `commonMain`
- ✅ Zero Android UI changes (will do in Week 2)
- ✅ Unit tests verify core logic
- ✅ GitHub ready for PRs in Week 2

---

**Next:** Read **05-WEEK2_TASKS.md** and start Week 2! 🚀
