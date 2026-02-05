package sanguine.player;

import sanguine.model.ReadOnlySanguineModel;
import sanguine.view.FeaturesListener;

/**
 * Represents a human player in the game.
 * Human players make moves using the view, so this does nothing when it is their turn.
 */
public class HumanPlayer implements PlayerActions {

  /**
   * Creates a HumanPlayer.
   */
  public HumanPlayer() {

  }

  @Override
  public void subscribe(FeaturesListener listener) {

  }

  @Override
  public void notifyTurn(ReadOnlySanguineModel model) {

  }
}
