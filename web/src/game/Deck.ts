import { Card, CardColor } from "./Card";
import { Hand } from "./Hand";

export class Deck {
  private cards: Card[] = [];

  constructor() {
    for (const color of Object.values(CardColor)) {
      for (let value = 1; value <= 13; value++) {
        this.cards.push(new Card(value, color));
      }
    }
  }

  shuffle(): void {
    for (let i = this.cards.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [this.cards[i], this.cards[j]] = [this.cards[j], this.cards[i]];
    }
  }

  size(): number {
    return this.cards.length;
  }

  drawCard(): Card | null {
    if (this.size() === 0) return null;
    return this.cards.shift() ?? null;
  }

  drawCards(num: number): Hand | null {
    if (this.cards.length < num) return null;
    const drawn: Card[] = [];
    for (let i = 0; i < num; i++) {
      drawn.push(this.cards.shift()!);
    }
    return new Hand(drawn);
  }
}
