package sanguine.model;

/**
 * Represents a cluster of pawns that can exist on a Sanguine board spot. Can be 1 - 3 pawns.
 */
public interface SanguinePawnCluster extends SanguineElement {

  /**
   * Returns the number of pawns in the cluster.
   *
   * @return the number of pawns in the pawn cluster.
   */
  int getNumPawns();

  /**
   * Sets the number of pawns to the specified size.
   *
   * @param size the number of pawns in the cluster.
   */
  void setNumPawns(PawnClusterSize size);

  /**
   * Sets the pawn cluster to belong to the red player if it currently belongs to the blue player.
   * If it currently belongs to the red player, sets the pawn cluster to belong to the blue player.
   */
  void switchPawnClusterOwner();

  /**
   * Returns a new copy of the cluster, not the original reference.
   *
   * @return a copy of the SanguinePawnCluster.
   */
  SanguinePawnCluster getCopy();
}
