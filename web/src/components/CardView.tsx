import { Card, CardColor } from "../game/Card";
import "./CardView.css";

// cartes.png is a 13-col x 4-row sprite sheet, 150x200 per cell.
const SHEET_COLS = 13;
const SHEET_ROWS = 4;

const ROW_BY_COLOR: Record<CardColor, number> = {
  [CardColor.COEUR]: 0,
  [CardColor.CARREAU]: 1,
  [CardColor.PIQUE]: 2,
  [CardColor.TREFLE]: 3,
};

function colFor(value: number): number {
  if (value === 1) return 12; // ace is last column
  return value - 2; // 2..13 -> 0..11
}

interface CardFaceProps {
  card: Card;
  selected?: boolean;
  onClick?: () => void;
}

export function CardFace({ card, selected, onClick }: CardFaceProps) {
  const row = ROW_BY_COLOR[card.color];
  const col = colFor(card.value);
  const style = {
    backgroundPosition: `${(col * 100) / (SHEET_COLS - 1)}% ${(row * 100) / (SHEET_ROWS - 1)}%`,
    backgroundSize: `${SHEET_COLS * 100}% ${SHEET_ROWS * 100}%`,
  };

  return (
    <div
      className={`playing-card face${selected ? " selected" : ""}${onClick ? " clickable" : ""}`}
      style={style}
      onClick={onClick}
      role={onClick ? "button" : undefined}
      aria-pressed={onClick ? selected : undefined}
    />
  );
}

export function CardBack() {
  return <div className="playing-card back" />;
}
