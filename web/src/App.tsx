import { useRef, useState } from "react";
import { CardBack, CardFace } from "./components/CardView";
import { GameEngine, GameState } from "./game/GameEngine";
import "./App.css";

function App() {
  const engineRef = useRef(new GameEngine());
  const [snapshot, setSnapshot] = useState(() => engineRef.current.getSnapshot());
  const [balanceInput, setBalanceInput] = useState("");
  const [showHistory, setShowHistory] = useState(false);

  const refresh = () => setSnapshot(engineRef.current.getSnapshot());

  const startGame = () => {
    const amount = Number.parseInt(balanceInput, 10);
    if (Number.isFinite(amount) && amount > 0) {
      engineRef.current.newGame(amount);
      setBalanceInput("");
      refresh();
    }
  };

  const increaseBet = () => {
    engineRef.current.increaseBet();
    refresh();
  };

  const decreaseBet = () => {
    engineRef.current.decreaseBet();
    refresh();
  };

  const toggleCard = (index: number) => {
    engineRef.current.toggleSelected(index);
    refresh();
  };

  const play = () => {
    engineRef.current.play();
    refresh();
  };

  const messageClass = (() => {
    const m = snapshot.message.toLowerCase();
    if (m.includes("perdu")) return "message lose";
    if (m.includes("gagné") || m.includes("jackpot")) return "message win";
    return "message";
  })();

  const played = snapshot.history.length;
  const wins = snapshot.history.filter((h) => h.isWin).length;
  const losses = played - wins;
  const netTotal = snapshot.history.reduce((sum, h) => sum + h.gainNet, 0);
  const best = snapshot.history.reduce((m, h) => Math.max(m, h.gainNet), 0);

  return (
    <div className="table">
      <div className="rail">
      <header className="banner">
        <h1>♠ ROYAL VIDEO POKER ♠</h1>
        <p className="subtitle">Style casino • KMP shared logic • session tracking</p>
        {snapshot.isJackpot && (
          <div className="jackpot">
            <div className="jackpot-title">🔥 JACKPOT 🔥</div>
            <div className="jackpot-subtitle">Quinte flush royale</div>
          </div>
        )}
      </header>

      <main className="panel">
        <div className="chips">
          <div className="chip">
            <span className="chip-label">Solde</span>
            <span className="chip-value">{snapshot.balance}€</span>
          </div>
          <div className="chip">
            <span className="chip-label">Mise</span>
            <span className="chip-value">{snapshot.bet}€</span>
          </div>
        </div>

        <p className={messageClass}>{snapshot.message}</p>

        {snapshot.lastResultLabel && snapshot.state === GameState.GAIN && (
          <p className="last-result">Dernière combinaison : {snapshot.lastResultLabel}</p>
        )}

        {snapshot.state === GameState.CONFIG ? (
          <div className="config">
            <h2>Choisissez votre solde de départ</h2>
            <input
              type="number"
              inputMode="numeric"
              placeholder="Entrez un montant (€)"
              value={balanceInput}
              onChange={(e) => setBalanceInput(e.target.value)}
              onKeyDown={(e) => e.key === "Enter" && startGame()}
            />
            <button className="btn btn-gold" onClick={startGame}>
              Valider
            </button>
          </div>
        ) : (
          <>
            <div className="felt-strip">
              <div className="cards-row">
                {snapshot.state === GameState.MISE &&
                  Array.from({ length: 5 }).map((_, i) => (
                    <CardBack key={`${snapshot.state}-${i}`} dealIndex={i} />
                  ))}

                {snapshot.state === GameState.CHOIX &&
                  snapshot.cards.map((card, i) => (
                    <CardFace
                      key={`${snapshot.state}-${i}`}
                      card={card}
                      selected={snapshot.selectedIndices.has(i)}
                      onClick={() => toggleCard(i)}
                      dealIndex={i}
                    />
                  ))}

                {snapshot.state === GameState.GAIN &&
                  snapshot.cards.map((card, i) => (
                    <CardFace key={`${snapshot.state}-${i}`} card={card} dealIndex={i} />
                  ))}
              </div>
            </div>

            <div className="controls">
              <div className="history-col">
                <button className="btn btn-dark" onClick={() => setShowHistory((v) => !v)}>
                  {showHistory ? "Masquer" : "Historique"}
                </button>

                {showHistory && (
                  <div className="history-panel">
                    <HistoryLine label="Parties" value={String(played)} />
                    <HistoryLine label="Victoires" value={String(wins)} />
                    <HistoryLine label="Défaites" value={String(losses)} />
                    <HistoryLine
                      label="Gain net"
                      value={`${netTotal >= 0 ? "+" : ""}${netTotal}€`}
                      className={netTotal >= 0 ? "positive" : "negative"}
                    />
                    <HistoryLine label="Best" value={`${best >= 0 ? "+" : ""}${best}€`} className="gold" />

                    <div className="history-list">
                      {snapshot.history.length === 0 ? (
                        <p className="empty">Aucune manche</p>
                      ) : (
                        [...snapshot.history]
                          .slice(-5)
                          .reverse()
                          .map((entry) => (
                            <p key={entry.roundNumber}>
                              M{entry.roundNumber} : {entry.combinaison} (
                              {entry.gainNet >= 0 ? "+" : ""}
                              {entry.gainNet}€)
                            </p>
                          ))
                      )}
                    </div>
                  </div>
                )}
              </div>

              <div className="bet-col">
                <button
                  className="btn btn-blue"
                  onClick={increaseBet}
                  disabled={snapshot.state !== GameState.MISE || snapshot.balance <= 0}
                >
                  +
                </button>
                <button
                  className="btn btn-blue"
                  onClick={decreaseBet}
                  disabled={snapshot.state !== GameState.MISE || snapshot.balance <= 0}
                >
                  −
                </button>
              </div>

              <div className="play-col">
                <button className="btn btn-play" onClick={play}>
                  {snapshot.state === GameState.GAIN ? "Rejouer" : "Jouer"}
                </button>
              </div>
            </div>

            {snapshot.balance <= 0 && (
              <p className="broke">Vous avez perdu tout votre argent.</p>
            )}
          </>
        )}
      </main>
      </div>
    </div>
  );
}

function HistoryLine({
  label,
  value,
  className,
}: {
  label: string;
  value: string;
  className?: string;
}) {
  return (
    <div className="history-line">
      <span className="history-label">{label} : </span>
      <span className={`history-value ${className ?? ""}`}>{value}</span>
    </div>
  );
}

export default App;
