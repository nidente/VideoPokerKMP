package com.example.tp4

import kotlin.test.Test
import kotlin.test.assertEquals

class PokerTest {
    private val poker = Poker()

    private fun handOf(vararg cards: Card) = Hand(cards.toMutableList())

    @Test
    fun royalFlush() {
        val hand = handOf(
            Card(1, Card.Color.PIQUE),
            Card(13, Card.Color.PIQUE),
            Card(12, Card.Color.PIQUE),
            Card(11, Card.Color.PIQUE),
            Card(10, Card.Color.PIQUE)
        )
        assertEquals(Poker.PokerHandValue.ROYAL_FLUSH, poker.evaluateHand(hand))
    }

    @Test
    fun straightFlush() {
        val hand = handOf(
            Card(2, Card.Color.COEUR),
            Card(3, Card.Color.COEUR),
            Card(4, Card.Color.COEUR),
            Card(5, Card.Color.COEUR),
            Card(6, Card.Color.COEUR)
        )
        assertEquals(Poker.PokerHandValue.STRAIGHT_FLUSH, poker.evaluateHand(hand))
    }

    @Test
    fun fourOfAKind() {
        val hand = handOf(
            Card(7, Card.Color.COEUR),
            Card(7, Card.Color.CARREAU),
            Card(7, Card.Color.PIQUE),
            Card(7, Card.Color.TREFLE),
            Card(2, Card.Color.COEUR)
        )
        assertEquals(Poker.PokerHandValue.FOUR_OF_A_KIND, poker.evaluateHand(hand))
    }

    @Test
    fun fullHouse() {
        val hand = handOf(
            Card(9, Card.Color.COEUR),
            Card(9, Card.Color.CARREAU),
            Card(9, Card.Color.PIQUE),
            Card(4, Card.Color.TREFLE),
            Card(4, Card.Color.COEUR)
        )
        assertEquals(Poker.PokerHandValue.FULL_HOUSE, poker.evaluateHand(hand))
    }

    @Test
    fun aceLowStraight() {
        val hand = handOf(
            Card(1, Card.Color.COEUR),
            Card(2, Card.Color.CARREAU),
            Card(3, Card.Color.PIQUE),
            Card(4, Card.Color.TREFLE),
            Card(5, Card.Color.COEUR)
        )
        assertEquals(Poker.PokerHandValue.STRAIGHT, poker.evaluateHand(hand))
    }

    @Test
    fun richPairJacksOrBetter() {
        val hand = handOf(
            Card(11, Card.Color.COEUR),
            Card(11, Card.Color.CARREAU),
            Card(2, Card.Color.PIQUE),
            Card(4, Card.Color.TREFLE),
            Card(6, Card.Color.COEUR)
        )
        assertEquals(Poker.PokerHandValue.RICH_PAIR, poker.evaluateHand(hand))
    }

    @Test
    fun lowPairIsNothing() {
        val hand = handOf(
            Card(9, Card.Color.COEUR),
            Card(9, Card.Color.CARREAU),
            Card(2, Card.Color.PIQUE),
            Card(4, Card.Color.TREFLE),
            Card(6, Card.Color.COEUR)
        )
        assertEquals(Poker.PokerHandValue.NOTHING, poker.evaluateHand(hand))
    }

    @Test
    fun payoutMultiplierAppliesToBet() {
        val hand = handOf(
            Card(7, Card.Color.COEUR),
            Card(7, Card.Color.CARREAU),
            Card(7, Card.Color.PIQUE),
            Card(7, Card.Color.TREFLE),
            Card(2, Card.Color.COEUR)
        )
        val result = poker.evaluateHand(hand)
        assertEquals(20, result.multiplier)
        assertEquals(100, 5 * result.multiplier)
    }
}
