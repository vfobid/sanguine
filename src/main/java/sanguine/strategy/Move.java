package sanguine.strategy;

/**
 * This class represents a possible move in Sanguine: playing a specific card at
 * a specific location.
 */
public class Move {
  private final int cardInd;
  private final int row;
  private final int col;

  /**
   * Creates a Move.
   *
   * @param cardInd the index of the card in the player's hand
   * @param row     the row on the board.
   * @param col     the column on the board.
   */
  public Move(int cardInd, int row, int col) {
    this.cardInd = cardInd;
    this.row = row;
    this.col = col;
  }

  /**
   * Gets the card index.
   *
   * @return the card index in the hand.
   */
  public int getCardInd() {
    return cardInd;
  }

  /**
   * Gets the row on the board.
   *
   * @return The row coordinate.
   */
  public int getRow() {
    return row;
  }

  /**
   * Gets the column on the board.
   *
   * @return The column coordinate.
   */
  public int getCol() {
    return col;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Move move)) {
      return false;
    }

    return cardInd == move.cardInd && row == move.row && col == move.col;
  }

  @Override
  public int hashCode() {
    return cardInd * 1000 + row * 100 + col;
  }

  @Override
  public String toString() {
    return "Move{card=" + cardInd + " , row=" + row + " , col=" + col + "}";
  }

}
