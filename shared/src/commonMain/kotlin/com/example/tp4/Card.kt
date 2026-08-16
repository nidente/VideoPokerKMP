package com.example.tp4

class Card(val value: Int, val color: Color) : Comparable<Card> {

    enum class Color(val emoji: String) {
        TREFLE("♣️"),
        CARREAU("♦️"),
        COEUR("♥️"),
        PIQUE("♠️")
    }

    val rank: Int
        get() {
            return if (value == 1) {
                14
            } else {
                value
            }
        }

    override fun compareTo(other: Card): Int {
        return if (this.rank < other.rank) {
            -1
        } else if (this.rank > other.rank) {
            1
        } else {
            0
        }
    }

    override fun toString(): String {
        val v = when (value) {
            1 -> "A"
            10 -> "T"
            11 -> "J"
            12 -> "Q"
            13 -> "K"
            else -> value.toString()
        }
        return v + color.emoji
    }
}
