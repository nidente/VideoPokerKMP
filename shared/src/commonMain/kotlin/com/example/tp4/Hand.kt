package com.example.tp4

class Hand(val cards: MutableList<Card>) {

    override fun toString(): String {
        return cards.joinToString("") { it.toString() }
    }

    operator fun get(index: Int): Card {
        return cards[index]
    }

    operator fun set(index: Int, card: Card) {
        cards[index] = card
    }
}
