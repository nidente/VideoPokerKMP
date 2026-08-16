import { Card } from "./Card";

export class Hand {
  cards: Card[];

  constructor(cards: Card[]) {
    this.cards = cards;
  }

  toString(): string {
    return this.cards.map((c) => c.toString()).join("");
  }

  get(index: number): Card {
    return this.cards[index];
  }

  set(index: number, card: Card): void {
    this.cards[index] = card;
  }
}
