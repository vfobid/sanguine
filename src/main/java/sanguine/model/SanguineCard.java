package sanguine.model;

/**
 * Represents a Sanguine playing card. Each card has a score, cost, and name.
 */
public interface SanguineCard extends SanguineElement {

  /**
   * Accessor method for the value of the Sanguine card representation. The value is used
   * to determine the player's overall scores.
   *
   * @return the value of the card.
   */
  int getValue();

  /**
   * Accessor method for the cost of the Sanguine card representation. The cost
   * is the number of pawns needed to place the card on a spot on the board.
   *
   * @return the cost of the card as an integer
   */
  int getCost();

  /**
   * Accessor method for the name of the Sanguine card representation.
   *
   * @return the name of the card as a String.
   */
  String getName();

  /**
   * Returns the 5x5 influence grid of the card. A null element means there no influence on that
   * spot if the board, and the presence of an Influence object in an element means the influence
   * is applied to that element.
   *
   * @return the influence grid of the card.
   */
  public Influence[][] getInfluenceGrid();

  @Override
  SanguineCard getCopy();
}
