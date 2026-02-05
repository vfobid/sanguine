package sanguine.model;

/**
 * Implementation of the SanguinePawnCluster. Represents a number of pawns from 1-3 than can
 * occupy a space on a board of Sanguine.
 */
public class BasicSanguinePawnCluster implements SanguinePawnCluster {
  private int numPawns;
  private Player owner;

  /**
   * Constructor for BasicSanguinePawnCluster.
   *
   * @param size  the number of pawns the cluster will have.
   * @param owner determines who the cluster belongs to: red or blue owner.
   */
  public BasicSanguinePawnCluster(PawnClusterSize size, Player owner) {
    setNumPawns(size);
    this.owner = owner;
  }

  @Override
  public int getNumPawns() {
    return numPawns;
  }

  @Override
  public void setNumPawns(PawnClusterSize size) {
    switch (size) {
      case PawnClusterSize.ONE:
        numPawns = 1;
        break;
      case PawnClusterSize.TWO:
        numPawns = 2;
        break;
      case PawnClusterSize.THREE:
        numPawns = 3;
        break;
    }
  }

  @Override
  public Player getOwner() {
    return this.owner;
  }

  @Override
  public SanguinePawnCluster getCopy() {
    int numPawns = getNumPawns();
    PawnClusterSize size = PawnClusterSize.ONE;
    switch (numPawns) {
      case 1:
        size = PawnClusterSize.ONE;
        break;
      case 2:
        size = PawnClusterSize.TWO;
        break;
      default:
        size = PawnClusterSize.THREE;
        break;
    }
    return new BasicSanguinePawnCluster(size, owner);
  }

  @Override
  public void switchPawnClusterOwner() {
    this.owner = this.owner.equals(Player.RED) ? Player.BLUE : Player.RED;
  }

  /**
   * Applies the influence to the cluster. It can increase the pawn number by the number specified
   * by the influence, and convert the pawn to the current player's ownership at the influence's
   * discretion.
   *
   * @param influence the influence to be applied.
   * @param owner     the owner of the card which is influencing this element.
   */
  @Override
  public void applyInfluence(Influence influence, Player owner) {
    if (this.owner != owner) {
      this.owner = owner;
    } else {
      this.numPawns += influence.numPawnIncrease();
      if (this.numPawns > 3) {
        numPawns = 3;
      }
    }
  }

  @Override
  public String toString() {
    return getNumPawns() + "";
  }

  @Override
  public int hashCode() {
    int hash = this.owner.equals(Player.RED) ? 3 : 7;
    return hash * getNumPawns();
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof BasicSanguinePawnCluster && obj.hashCode() == hashCode();
  }

}
