package sanguine.model;

public interface ModelListener {

  /**
   * Reacts after the model changes whose turn it is (either red or blue).
   *
   * @param player the new current player.
   */
  void onTurnSwitch(Player player);

  /**
   * Reacts after the model determines the game is over.
   */
  void onGameOver();
}
