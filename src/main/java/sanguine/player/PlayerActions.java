package sanguine.player;

import sanguine.model.ReadOnlySanguineModel;
import sanguine.view.FeaturesListener;

/**
 * This represents a player in the Sanguine game.
 * A player can be a human or a machine.
 * Players can subscribe listeners to their actions and be notified when it is their turn.
 */
public interface PlayerActions {

  /**
   * Subscribes a listener to this player's actions.
   *
   * @param listener the listener to be notified of the actions.
   */
  void subscribe(FeaturesListener listener);

  /**
   * Notifies the player that it is their turn to make a move.
   * For humans, this does nothing since the view handles their input.
   * For machines, this calls the strategy to compute and execute a move.
   *
   * @param model the current state of the game.
   */
  void notifyTurn(ReadOnlySanguineModel model);
}
