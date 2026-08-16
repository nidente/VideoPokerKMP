package com.example.tp4

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GameEngineTest {

    @Test
    fun newGameStartsInMiseState() {
        val engine = GameEngine()
        engine.newGame(100)
        assertEquals(GameState.MISE, engine.state)
        assertEquals(100, engine.balance)
        assertEquals(1, engine.bet)
    }

    @Test
    fun dealMovesToChoixAndDeductsBet() {
        val engine = GameEngine()
        engine.newGame(100)
        engine.increaseBet()
        engine.increaseBet()
        engine.play()

        assertEquals(GameState.CHOIX, engine.state)
        assertEquals(97, engine.balance)
        assertEquals(5, engine.cards.size)
    }

    @Test
    fun resolveRoundReplacesOnlySelectedCards() {
        val engine = GameEngine()
        engine.newGame(100)
        engine.play() // deal
        val initialCards = engine.cards.toList()
        engine.toggleSelected(0)
        engine.toggleSelected(1)
        engine.play() // resolve

        assertEquals(GameState.GAIN, engine.state)
        assertEquals(initialCards[2], engine.cards[2])
        assertEquals(initialCards[3], engine.cards[3])
        assertEquals(initialCards[4], engine.cards[4])
    }

    @Test
    fun gainStateAdvancesToMiseWhenBalanceRemains() {
        val engine = GameEngine()
        engine.newGame(100)
        engine.play() // deal
        engine.play() // resolve
        engine.play() // advance

        assertTrue(engine.state == GameState.MISE || engine.state == GameState.CONFIG)
    }

    @Test
    fun betCannotExceedBalanceOrGoBelowOne() {
        val engine = GameEngine()
        engine.newGame(2)
        engine.increaseBet()
        engine.increaseBet()
        engine.increaseBet()
        assertEquals(2, engine.bet)

        engine.decreaseBet()
        engine.decreaseBet()
        engine.decreaseBet()
        assertEquals(1, engine.bet)
    }
}
