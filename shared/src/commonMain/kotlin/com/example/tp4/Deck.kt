package com.example.tp4

class Deck {

    private val cards: MutableList<Card>

    init {
        cards = mutableListOf()

        for (color in Card.Color.entries) {
            for (value in 1..13) {
                cards.add(Card(value, color))
            }
        }
    }

    fun Shuffle() {
        cards.shuffle()
    }

    fun size(): Int = cards.size

    fun drawCard(): Card? {
        if (size() == 0) return null
        return cards.removeAt(0)
    }

    fun drawCards(num: Int): Hand? {
        if (cards.size < num) {
            return null
        }

        val drawnCards = mutableListOf<Card>()
        repeat(num) {
            drawnCards.add(cards.removeAt(0))
        }

        return Hand(drawnCards)
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (i in cards.indices) {
            sb.append(cards[i]).append(" ")

            if ((i + 1) % 13 == 0) {
                sb.append("\n")
            }
        }
        return sb.toString()
    }
}
