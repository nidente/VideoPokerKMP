import { Card } from "./Card";
import { Deck } from "./Deck";
import { Hand } from "./Hand";
import { evaluateHand, POKER_HAND_MULTIPLIER, PokerHandValue } from "./Poker";

export const GameState = {
  CONFIG: "CONFIG",
  MISE: "MISE",
  CHOIX: "CHOIX",
  GAIN: "GAIN",
} as const;

export type GameState = (typeof GameState)[keyof typeof GameState];

export interface GameHistoryEntry {
  roundNumber: number;
  mise: number;
  combinaison: string;
  gainBrut: number;
  gainNet: number;
  soldeApres: number;
  isWin: boolean;
}

export function handLabel(result: PokerHandValue): string {
  switch (result) {
    case PokerHandValue.NOTHING:
      return "Perdu";
    case PokerHandValue.RICH_PAIR:
      return "Paire";
    case PokerHandValue.TWO_PAIR:
      return "Double paire";
    case PokerHandValue.THREE_OF_A_KIND:
      return "Brelan";
    case PokerHandValue.STRAIGHT:
      return "Suite";
    case PokerHandValue.FLUSH:
      return "Couleur";
    case PokerHandValue.FULL_HOUSE:
      return "Full";
    case PokerHandValue.FOUR_OF_A_KIND:
      return "Carré";
    case PokerHandValue.STRAIGHT_FLUSH:
      return "Quinte flush";
    case PokerHandValue.ROYAL_FLUSH:
      return "Quinte flush royale";
  }
}

export interface GameSnapshot {
  state: GameState;
  balance: number;
  bet: number;
  cards: Card[];
  selectedIndices: Set<number>;
  message: string;
  lastResultLabel: string;
  isJackpot: boolean;
  history: GameHistoryEntry[];
}

/** TypeScript mirror of shared/src/commonMain/.../GameEngine.kt — keep both in sync. */
export class GameEngine {
  private state: GameState = GameState.CONFIG;
  private balance = 0;
  private bet = 1;
  private cards: Card[] = [];
  private selectedIndices: Set<number> = new Set();
  private message = "Choisissez un solde de départ";
  private lastResultLabel = "";
  private isJackpot = false;
  private deck: Deck | null = null;
  private roundCounter = 0;
  private history: GameHistoryEntry[] = [];

  newGame(startingBalance: number): void {
    this.balance = startingBalance;
    this.bet = 1;
    this.cards = [];
    this.selectedIndices = new Set();
    this.deck = null;
    this.message = "Appuie sur Jouer pour commencer";
    this.history = [];
    this.roundCounter = 0;
    this.isJackpot = false;
    this.lastResultLabel = "";
    this.state = GameState.MISE;
  }

  increaseBet(): void {
    if (this.state === GameState.MISE && this.bet < this.balance) this.bet++;
  }

  decreaseBet(): void {
    if (this.state === GameState.MISE && this.bet > 1) this.bet--;
  }

  toggleSelected(index: number): void {
    if (this.state !== GameState.CHOIX) return;
    if (this.selectedIndices.has(index)) {
      this.selectedIndices.delete(index);
    } else {
      this.selectedIndices.add(index);
    }
  }

  play(): void {
    switch (this.state) {
      case GameState.MISE:
        this.deal();
        break;
      case GameState.CHOIX:
        this.resolveRound();
        break;
      case GameState.GAIN:
        this.advanceAfterRound();
        break;
      case GameState.CONFIG:
        break;
    }
  }

  private deal(): void {
    this.isJackpot = false;
    this.lastResultLabel = "";

    if (this.balance <= 0) {
      this.state = GameState.CONFIG;
      this.message = "Choisissez un nouveau solde";
      return;
    }
    if (this.bet > this.balance) this.bet = this.balance;

    const newDeck = new Deck();
    newDeck.shuffle();
    this.deck = newDeck;

    const hand = newDeck.drawCards(5);
    if (!hand) return;
    this.balance -= this.bet;
    this.cards = [...hand.cards];
    this.selectedIndices = new Set();
    this.message = "Choisissez les cartes à changer";
    this.state = GameState.CHOIX;
  }

  private resolveRound(): void {
    const currentDeck = this.deck;
    const newCards = [...this.cards];

    if (currentDeck) {
      for (const i of this.selectedIndices) {
        const newCard = currentDeck.drawCard();
        if (newCard) newCards[i] = newCard;
      }
    }
    this.cards = newCards;

    const finalHand = new Hand([...this.cards]);
    const result = evaluateHand(finalHand);
    const gainBrut = this.bet * POKER_HAND_MULTIPLIER[result];
    const gainNet = gainBrut - this.bet;

    this.balance += gainBrut;
    this.roundCounter++;

    const combinaison = handLabel(result);
    this.lastResultLabel = combinaison;

    this.history = [
      ...this.history,
      {
        roundNumber: this.roundCounter,
        mise: this.bet,
        combinaison,
        gainBrut,
        gainNet,
        soldeApres: this.balance,
        isWin: gainBrut > 0,
      },
    ];

    this.isJackpot = result === PokerHandValue.ROYAL_FLUSH;

    if (result === PokerHandValue.ROYAL_FLUSH) {
      this.message = `Jackpot absolu ! +${gainNet}€ net`;
    } else if (gainNet > 0) {
      this.message = `Tu as gagné ${gainNet}€ net avec ${combinaison}`;
    } else if (gainNet === 0 && gainBrut > 0) {
      this.message = `Tu récupères ta mise avec ${combinaison}`;
    } else {
      this.message = "Tu as perdu cette manche";
    }

    this.state = GameState.GAIN;
  }

  private advanceAfterRound(): void {
    this.selectedIndices = new Set();
    this.cards = [];
    this.deck = null;

    if (this.balance <= 0) {
      this.bet = 1;
      this.message = "Tu n'as plus d'argent. Choisis un nouveau solde.";
      this.state = GameState.CONFIG;
    } else {
      if (this.bet > this.balance) this.bet = this.balance;
      this.message = "Appuie sur Jouer pour recommencer";
      this.state = GameState.MISE;
    }
  }

  getSnapshot(): GameSnapshot {
    return {
      state: this.state,
      balance: this.balance,
      bet: this.bet,
      cards: this.cards,
      selectedIndices: new Set(this.selectedIndices),
      message: this.message,
      lastResultLabel: this.lastResultLabel,
      isJackpot: this.isJackpot,
      history: this.history,
    };
  }
}
