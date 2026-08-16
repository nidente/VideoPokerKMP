package com.example.tp4

class Poker {
    enum class PokerHandValue(val multiplier: Int) {
        NOTHING(0),
        RICH_PAIR(1),
        TWO_PAIR(2),
        THREE_OF_A_KIND(3),
        STRAIGHT(4),
        FLUSH(6),
        FULL_HOUSE(9),
        FOUR_OF_A_KIND(20),
        STRAIGHT_FLUSH(50),
        ROYAL_FLUSH(250)
    }

    fun evaluateHand(hand: Hand): PokerHandValue {

        val ranks = hand.cards.map { it.rank }.sorted()
        val values = hand.cards.map { it.value }.sorted()
        val colors = hand.cards.map { it.color }

        val isFlush = colors.distinct().size == 1

        val isStraight =
            ranks == listOf(10, 11, 12, 13, 14) ||
                    (ranks.distinct().size == 5 && ranks.last() - ranks.first() == 4) ||
                    values == listOf(1, 2, 3, 4, 5)

        val counts = ranks
            .groupBy { it }
            .map { it.value.size }
            .sortedDescending()

        return when {
            isFlush && ranks == listOf(10, 11, 12, 13, 14) ->
                PokerHandValue.ROYAL_FLUSH

            isFlush && isStraight ->
                PokerHandValue.STRAIGHT_FLUSH

            counts == listOf(4, 1) ->
                PokerHandValue.FOUR_OF_A_KIND

            counts == listOf(3, 2) ->
                PokerHandValue.FULL_HOUSE

            isFlush ->
                PokerHandValue.FLUSH

            isStraight ->
                PokerHandValue.STRAIGHT

            counts == listOf(3, 1, 1) ->
                PokerHandValue.THREE_OF_A_KIND

            counts == listOf(2, 2, 1) ->
                PokerHandValue.TWO_PAIR

            counts == listOf(2, 1, 1, 1) &&
                    ranks.groupingBy { it }.eachCount()
                        .any { (rank, count) -> count == 2 && rank >= 11 } ->
                PokerHandValue.RICH_PAIR

            else ->
                PokerHandValue.NOTHING
        }
    }
}
