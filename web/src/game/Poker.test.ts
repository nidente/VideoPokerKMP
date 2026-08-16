import { describe, expect, it } from "vitest";
import { Card, CardColor } from "./Card";
import { Hand } from "./Hand";
import { evaluateHand, POKER_HAND_MULTIPLIER, PokerHandValue } from "./Poker";

const handOf = (...cards: Card[]) => new Hand(cards);

describe("evaluateHand", () => {
  it("royal flush", () => {
    const hand = handOf(
      new Card(1, CardColor.PIQUE),
      new Card(13, CardColor.PIQUE),
      new Card(12, CardColor.PIQUE),
      new Card(11, CardColor.PIQUE),
      new Card(10, CardColor.PIQUE),
    );
    expect(evaluateHand(hand)).toBe(PokerHandValue.ROYAL_FLUSH);
  });

  it("straight flush", () => {
    const hand = handOf(
      new Card(2, CardColor.COEUR),
      new Card(3, CardColor.COEUR),
      new Card(4, CardColor.COEUR),
      new Card(5, CardColor.COEUR),
      new Card(6, CardColor.COEUR),
    );
    expect(evaluateHand(hand)).toBe(PokerHandValue.STRAIGHT_FLUSH);
  });

  it("four of a kind", () => {
    const hand = handOf(
      new Card(7, CardColor.COEUR),
      new Card(7, CardColor.CARREAU),
      new Card(7, CardColor.PIQUE),
      new Card(7, CardColor.TREFLE),
      new Card(2, CardColor.COEUR),
    );
    expect(evaluateHand(hand)).toBe(PokerHandValue.FOUR_OF_A_KIND);
  });

  it("full house", () => {
    const hand = handOf(
      new Card(9, CardColor.COEUR),
      new Card(9, CardColor.CARREAU),
      new Card(9, CardColor.PIQUE),
      new Card(4, CardColor.TREFLE),
      new Card(4, CardColor.COEUR),
    );
    expect(evaluateHand(hand)).toBe(PokerHandValue.FULL_HOUSE);
  });

  it("ace-low straight", () => {
    const hand = handOf(
      new Card(1, CardColor.COEUR),
      new Card(2, CardColor.CARREAU),
      new Card(3, CardColor.PIQUE),
      new Card(4, CardColor.TREFLE),
      new Card(5, CardColor.COEUR),
    );
    expect(evaluateHand(hand)).toBe(PokerHandValue.STRAIGHT);
  });

  it("rich pair (jacks or better)", () => {
    const hand = handOf(
      new Card(11, CardColor.COEUR),
      new Card(11, CardColor.CARREAU),
      new Card(2, CardColor.PIQUE),
      new Card(4, CardColor.TREFLE),
      new Card(6, CardColor.COEUR),
    );
    expect(evaluateHand(hand)).toBe(PokerHandValue.RICH_PAIR);
  });

  it("low pair is nothing", () => {
    const hand = handOf(
      new Card(9, CardColor.COEUR),
      new Card(9, CardColor.CARREAU),
      new Card(2, CardColor.PIQUE),
      new Card(4, CardColor.TREFLE),
      new Card(6, CardColor.COEUR),
    );
    expect(evaluateHand(hand)).toBe(PokerHandValue.NOTHING);
  });

  it("payout multiplier applies to bet", () => {
    const hand = handOf(
      new Card(7, CardColor.COEUR),
      new Card(7, CardColor.CARREAU),
      new Card(7, CardColor.PIQUE),
      new Card(7, CardColor.TREFLE),
      new Card(2, CardColor.COEUR),
    );
    const result = evaluateHand(hand);
    expect(POKER_HAND_MULTIPLIER[result]).toBe(20);
    expect(5 * POKER_HAND_MULTIPLIER[result]).toBe(100);
  });
});
