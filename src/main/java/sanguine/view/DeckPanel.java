package sanguine.view;

import sanguine.model.ReadOnlySanguineModel;

/**
 * Interface for the deck panel in a game of Sanguine. Holds all the cards in this section,
 * separating them from the board.
 */
public interface DeckPanel {
  /**
   * Repaints the hands after recieving new information about the game state.
   * Note: MUST refresh deck to initialize card deck for game play view.
   *
   * @param model         the potentially  recently changed model.
   * @param selectedIndex the index of the card that has been clicked.
   */
  void refreshDeck(ReadOnlySanguineModel model, int selectedIndex);

  /**
   * Adds the inputted FeaturesListener to the list of subscribers of this panel to.
   *
   * @param listener the new subscriber to the mouse events of this panel.
   */
  void setSubscriber(FeaturesListener listener);
}
