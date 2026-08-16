package com.example.tp4

enum class GameState {
    CONFIG, MISE, CHOIX, GAIN
}

data class GameHistoryEntry(
    val roundNumber: Int,
    val mise: Int,
    val combinaison: String,
    val gainBrut: Int,
    val gainNet: Int,
    val soldeApres: Int
) {
    val isWin: Boolean
        get() = gainBrut > 0
}

fun handLabel(result: Poker.PokerHandValue): String {
    return when (result) {
        Poker.PokerHandValue.NOTHING -> "Perdu"
        Poker.PokerHandValue.RICH_PAIR -> "Paire"
        Poker.PokerHandValue.TWO_PAIR -> "Double paire"
        Poker.PokerHandValue.THREE_OF_A_KIND -> "Brelan"
        Poker.PokerHandValue.STRAIGHT -> "Suite"
        Poker.PokerHandValue.FLUSH -> "Couleur"
        Poker.PokerHandValue.FULL_HOUSE -> "Full"
        Poker.PokerHandValue.FOUR_OF_A_KIND -> "Carré"
        Poker.PokerHandValue.STRAIGHT_FLUSH -> "Quinte flush"
        Poker.PokerHandValue.ROYAL_FLUSH -> "Quinte flush royale"
    }
}

/**
 * Faithful extraction of the round state machine that used to live inline in
 * MainActivity's Compose lambdas, so both the Android UI and the web UI can
 * drive the same rules. Indices in [selectedIndices] are cards the player
 * wants to *change* (matches the original "Choisissez les cartes à changer" flow).
 */
class GameEngine {
    private val poker = Poker()

    var state: GameState = GameState.CONFIG
        private set
    var balance: Int = 0
        private set
    var bet: Int = 1
        private set
    var cards: List<Card> = emptyList()
        private set
    var selectedIndices: Set<Int> = emptySet()
        private set
    var message: String = "Choisissez un solde de départ"
        private set
    var lastResultLabel: String = ""
        private set
    var isJackpot: Boolean = false
        private set

    private var deck: Deck? = null
    private var roundCounter = 0
    private val _history = mutableListOf<GameHistoryEntry>()
    val history: List<GameHistoryEntry> get() = _history

    fun newGame(startingBalance: Int) {
        balance = startingBalance
        bet = 1
        cards = emptyList()
        selectedIndices = emptySet()
        deck = null
        message = "Appuie sur Jouer pour commencer"
        _history.clear()
        roundCounter = 0
        isJackpot = false
        lastResultLabel = ""
        state = GameState.MISE
    }

    fun increaseBet() {
        if (state == GameState.MISE && bet < balance) bet++
    }

    fun decreaseBet() {
        if (state == GameState.MISE && bet > 1) bet--
    }

    fun toggleSelected(index: Int) {
        if (state != GameState.CHOIX) return
        selectedIndices = if (index in selectedIndices) selectedIndices - index else selectedIndices + index
    }

    /** Advances the round: deals on MISE, resolves on CHOIX, resets on GAIN. */
    fun play() {
        when (state) {
            GameState.MISE -> deal()
            GameState.CHOIX -> resolveRound()
            GameState.GAIN -> advanceAfterRound()
            GameState.CONFIG -> Unit
        }
    }

    private fun deal() {
        isJackpot = false
        lastResultLabel = ""

        if (balance <= 0) {
            state = GameState.CONFIG
            message = "Choisissez un nouveau solde"
            return
        }
        if (bet > balance) bet = balance

        val newDeck = Deck()
        newDeck.Shuffle()
        deck = newDeck

        val hand = newDeck.drawCards(5) ?: return
        balance -= bet
        cards = hand.cards.toList()
        selectedIndices = emptySet()
        message = "Choisissez les cartes à changer"
        state = GameState.CHOIX
    }

    private fun resolveRound() {
        val currentDeck = deck
        val newCards = cards.toMutableList()

        if (currentDeck != null) {
            for (i in selectedIndices) {
                val newCard = currentDeck.drawCard()
                if (newCard != null) newCards[i] = newCard
            }
        }
        cards = newCards.toList()

        val finalHand = Hand(cards.toMutableList())
        val result = poker.evaluateHand(finalHand)
        val gainBrut = bet * result.multiplier
        val gainNet = gainBrut - bet

        balance += gainBrut
        roundCounter++

        val combinaison = handLabel(result)
        lastResultLabel = combinaison

        _history.add(
            GameHistoryEntry(
                roundNumber = roundCounter,
                mise = bet,
                combinaison = combinaison,
                gainBrut = gainBrut,
                gainNet = gainNet,
                soldeApres = balance
            )
        )

        isJackpot = result == Poker.PokerHandValue.ROYAL_FLUSH

        message = when {
            result == Poker.PokerHandValue.ROYAL_FLUSH -> "Jackpot absolu ! +${gainNet}€ net"
            gainNet > 0 -> "Tu as gagné ${gainNet}€ net avec $combinaison"
            gainNet == 0 && gainBrut > 0 -> "Tu récupères ta mise avec $combinaison"
            else -> "Tu as perdu cette manche"
        }

        state = GameState.GAIN
    }

    private fun advanceAfterRound() {
        selectedIndices = emptySet()
        cards = emptyList()
        deck = null

        if (balance <= 0) {
            bet = 1
            message = "Tu n'as plus d'argent. Choisis un nouveau solde."
            state = GameState.CONFIG
        } else {
            if (bet > balance) bet = balance
            message = "Appuie sur Jouer pour recommencer"
            state = GameState.MISE
        }
    }
}
