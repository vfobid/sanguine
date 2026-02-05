package sanguine.model;

/**
 * Represents an influence that can be placed on a Sanguine board element.
 * Returns values for Sanguine Card's influence details.
 */
public interface Influence {

  /**
   * Returns the number of pawns by which the pawn cluster will increase.
   *
   * @return the number of pawns by which the pawn cluster will increase.
   */
  int numPawnIncrease();

  @Override
  boolean equals(Object obj);

  @Override
  int hashCode();
}
