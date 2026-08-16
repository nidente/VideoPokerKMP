export const CardColor = {
  TREFLE: "TREFLE",
  CARREAU: "CARREAU",
  COEUR: "COEUR",
  PIQUE: "PIQUE",
} as const;

export type CardColor = (typeof CardColor)[keyof typeof CardColor];

export const CARD_COLOR_EMOJI: Record<CardColor, string> = {
  [CardColor.TREFLE]: "♣️",
  [CardColor.CARREAU]: "♦️",
  [CardColor.COEUR]: "♥️",
  [CardColor.PIQUE]: "♠️",
};

export class Card {
  readonly value: number;
  readonly color: CardColor;

  constructor(value: number, color: CardColor) {
    this.value = value;
    this.color = color;
  }

  get rank(): number {
    return this.value === 1 ? 14 : this.value;
  }

  toString(): string {
    const v =
      this.value === 1
        ? "A"
        : this.value === 10
          ? "T"
          : this.value === 11
            ? "J"
            : this.value === 12
              ? "Q"
              : this.value === 13
                ? "K"
                : String(this.value);
    return v + CARD_COLOR_EMOJI[this.color];
  }

  /** Display rank matching original app's card sprite sheet convention. */
  get displayValue(): string {
    switch (this.value) {
      case 1:
        return "A";
      case 11:
        return "J";
      case 12:
        return "Q";
      case 13:
        return "K";
      default:
        return String(this.value);
    }
  }
}
