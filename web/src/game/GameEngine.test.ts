import { describe, expect, it } from "vitest";
import { GameEngine, GameState } from "./GameEngine";

describe("GameEngine", () => {
  it("new game starts in MISE state", () => {
    const engine = new GameEngine();
    engine.newGame(100);
    const snap = engine.getSnapshot();
    expect(snap.state).toBe(GameState.MISE);
    expect(snap.balance).toBe(100);
    expect(snap.bet).toBe(1);
  });

  it("deal moves to CHOIX and deducts bet", () => {
    const engine = new GameEngine();
    engine.newGame(100);
    engine.increaseBet();
    engine.increaseBet();
    engine.play();

    const snap = engine.getSnapshot();
    expect(snap.state).toBe(GameState.CHOIX);
    expect(snap.balance).toBe(97);
    expect(snap.cards.length).toBe(5);
  });

  it("resolve round replaces only selected cards", () => {
    const engine = new GameEngine();
    engine.newGame(100);
    engine.play(); // deal
    const initialCards = [...engine.getSnapshot().cards];
    engine.toggleSelected(0);
    engine.toggleSelected(1);
    engine.play(); // resolve

    const snap = engine.getSnapshot();
    expect(snap.state).toBe(GameState.GAIN);
    expect(snap.cards[2]).toBe(initialCards[2]);
    expect(snap.cards[3]).toBe(initialCards[3]);
    expect(snap.cards[4]).toBe(initialCards[4]);
  });

  it("GAIN state advances to MISE when balance remains", () => {
    const engine = new GameEngine();
    engine.newGame(100);
    engine.play(); // deal
    engine.play(); // resolve
    engine.play(); // advance

    const state = engine.getSnapshot().state;
    expect(state === GameState.MISE || state === GameState.CONFIG).toBe(true);
  });

  it("bet cannot exceed balance or go below one", () => {
    const engine = new GameEngine();
    engine.newGame(2);
    engine.increaseBet();
    engine.increaseBet();
    engine.increaseBet();
    expect(engine.getSnapshot().bet).toBe(2);

    engine.decreaseBet();
    engine.decreaseBet();
    engine.decreaseBet();
    expect(engine.getSnapshot().bet).toBe(1);
  });
});
