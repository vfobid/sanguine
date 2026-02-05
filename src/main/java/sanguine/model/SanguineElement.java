package sanguine.model;

/**
 * Represents any element on a Sanguine board. Can be null, a card, or 1-3 pawns.
 */
public interface SanguineElement {
  @Override
  int hashCode();

  @Override
  boolean equals(Object obj);

  @Override
  String toString();

  /**
   * Applies the influence to the element. This mutates the contents of the element.
   *
   * @param influence the influence to be applied.
   * @param owner     the owner of the card which is influencing the element.
   */
  void applyInfluence(Influence influence, Player owner);

  /**
   * Returns who the element belongs to: the red player or blue player.
   *
   * @return a Player enum that is either red or blue, corresponding to the owner of the card.
   */
  Player getOwner();

  /**
   * Returns a new object with identical parameters of the SanguineElement.
   *
   * @return a copy of the object.
   */
  SanguineElement getCopy();
}
