import { describe, expect, it } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import App from "./App";

async function startGame(balance = "100") {
  const user = userEvent.setup();
  render(<App />);
  await user.type(screen.getByPlaceholderText("Entrez un montant (€)"), balance);
  await user.click(screen.getByRole("button", { name: "Valider" }));
  return user;
}

describe("App", () => {
  it("shows the config screen on first load", () => {
    render(<App />);
    expect(screen.getByText("Choisissez votre solde de départ")).toBeInTheDocument();
    expect(screen.getByPlaceholderText("Entrez un montant (€)")).toBeInTheDocument();
  });

  it("typed balance digits appear in the input", async () => {
    const user = userEvent.setup();
    render(<App />);
    const input = screen.getByPlaceholderText("Entrez un montant (€)");
    await user.type(input, "150");
    expect(input).toHaveValue(150);
  });

  it("starting a game shows the bet screen with card backs", async () => {
    await startGame("100");

    expect(screen.getByText("100€")).toBeInTheDocument();
    expect(screen.getByText("Appuie sur Jouer pour commencer")).toBeInTheDocument();
    expect(document.querySelectorAll(".playing-card.back")).toHaveLength(5);
  });

  it("dealing reveals selectable face-up cards and deducts the bet", async () => {
    const user = userEvent.setup();
    await startGame("100");

    await user.click(screen.getByRole("button", { name: "Jouer" }));

    expect(screen.getByText("99€")).toBeInTheDocument();
    expect(screen.getByText("Choisissez les cartes à changer")).toBeInTheDocument();
    expect(document.querySelectorAll(".playing-card.face")).toHaveLength(5);
    expect(document.querySelectorAll('.playing-card.face[role="button"]')).toHaveLength(5);
  });

  it("resolving a round shows a result and relabels the button Rejouer", async () => {
    const user = userEvent.setup();
    await startGame("100");

    await user.click(screen.getByRole("button", { name: "Jouer" }));
    await user.click(screen.getByRole("button", { name: "Jouer" }));

    expect(screen.getByRole("button", { name: "Rejouer" })).toBeInTheDocument();
  });

  it("history panel tracks played rounds", async () => {
    const user = userEvent.setup();
    await startGame("100");

    await user.click(screen.getByRole("button", { name: "Jouer" }));
    await user.click(screen.getByRole("button", { name: "Jouer" }));
    await user.click(screen.getByRole("button", { name: "Historique" }));

    const partiesLabel = screen.getByText(/Parties/);
    expect(partiesLabel.closest(".history-line")).toHaveTextContent("Parties : 1");
  });
});
