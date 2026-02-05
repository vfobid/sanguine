package sanguine.model;

/**
 * Holds values for Sanguine Card's influence details. Includes values for pawn increase and
 */
public class BasicInfluence implements Influence {
  private int numPawnIncrease;

  /**
   * Constructor for a BasicInfluence object.
   *
   * @param numPawnIncrease the number by which the pawns will increase if influenced.
   */
  public BasicInfluence(int numPawnIncrease) {
    this.numPawnIncrease = numPawnIncrease;
  }

  @Override
  public int numPawnIncrease() {
    return numPawnIncrease;
  }

  @Override
  public int hashCode() {
    return 31 * Integer.hashCode(numPawnIncrease);
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof BasicInfluence &&
        ((BasicInfluence) o).numPawnIncrease == this.numPawnIncrease;
  }
}
