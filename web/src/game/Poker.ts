import { Hand } from "./Hand";

export const PokerHandValue = {
  NOTHING: "NOTHING",
  RICH_PAIR: "RICH_PAIR",
  TWO_PAIR: "TWO_PAIR",
  THREE_OF_A_KIND: "THREE_OF_A_KIND",
  STRAIGHT: "STRAIGHT",
  FLUSH: "FLUSH",
  FULL_HOUSE: "FULL_HOUSE",
  FOUR_OF_A_KIND: "FOUR_OF_A_KIND",
  STRAIGHT_FLUSH: "STRAIGHT_FLUSH",
  ROYAL_FLUSH: "ROYAL_FLUSH",
} as const;

export type PokerHandValue = (typeof PokerHandValue)[keyof typeof PokerHandValue];

export const POKER_HAND_MULTIPLIER: Record<PokerHandValue, number> = {
  [PokerHandValue.NOTHING]: 0,
  [PokerHandValue.RICH_PAIR]: 1,
  [PokerHandValue.TWO_PAIR]: 2,
  [PokerHandValue.THREE_OF_A_KIND]: 3,
  [PokerHandValue.STRAIGHT]: 4,
  [PokerHandValue.FLUSH]: 6,
  [PokerHandValue.FULL_HOUSE]: 9,
  [PokerHandValue.FOUR_OF_A_KIND]: 20,
  [PokerHandValue.STRAIGHT_FLUSH]: 50,
  [PokerHandValue.ROYAL_FLUSH]: 250,
};

function arraysEqual(a: number[], b: number[]): boolean {
  return a.length === b.length && a.every((v, i) => v === b[i]);
}

export function evaluateHand(hand: Hand): PokerHandValue {
  const ranks = hand.cards.map((c) => c.rank).sort((a, b) => a - b);
  const values = hand.cards.map((c) => c.value).sort((a, b) => a - b);
  const colors = hand.cards.map((c) => c.color);

  const isFlush = new Set(colors).size === 1;

  const isStraight =
    arraysEqual(ranks, [10, 11, 12, 13, 14]) ||
    (new Set(ranks).size === 5 && ranks[ranks.length - 1] - ranks[0] === 4) ||
    arraysEqual(values, [1, 2, 3, 4, 5]);

  const rankCounts = new Map<number, number>();
  for (const r of ranks) {
    rankCounts.set(r, (rankCounts.get(r) ?? 0) + 1);
  }
  const counts = [...rankCounts.values()].sort((a, b) => b - a);

  if (isFlush && arraysEqual(ranks, [10, 11, 12, 13, 14])) {
    return PokerHandValue.ROYAL_FLUSH;
  }
  if (isFlush && isStraight) {
    return PokerHandValue.STRAIGHT_FLUSH;
  }
  if (arraysEqual(counts, [4, 1])) {
    return PokerHandValue.FOUR_OF_A_KIND;
  }
  if (arraysEqual(counts, [3, 2])) {
    return PokerHandValue.FULL_HOUSE;
  }
  if (isFlush) {
    return PokerHandValue.FLUSH;
  }
  if (isStraight) {
    return PokerHandValue.STRAIGHT;
  }
  if (arraysEqual(counts, [3, 1, 1])) {
    return PokerHandValue.THREE_OF_A_KIND;
  }
  if (arraysEqual(counts, [2, 2, 1])) {
    return PokerHandValue.TWO_PAIR;
  }
  if (
    arraysEqual(counts, [2, 1, 1, 1]) &&
    [...rankCounts.entries()].some(([rank, count]) => count === 2 && rank >= 11)
  ) {
    return PokerHandValue.RICH_PAIR;
  }
  return PokerHandValue.NOTHING;
}
