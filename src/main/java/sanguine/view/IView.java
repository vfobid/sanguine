package sanguine.view;

import java.awt.Point;
import sanguine.model.ReadOnlySanguineModel;

/**
 * View of the Sanguine game. The view includes two main panels: the game board including the board
 * grid + score columns on either side, and the current player's hand. Within the hand, each of the
 * cards are also represented by a panel.
 */
public interface IView {

  /**
   * Refresh the board view to reflect any changes in the game state.
   *
   * @param model        the newly changed model.
   * @param selectedCell the coordinates for the cell selected in the view. This is null if none
   *                     were selected, or if the same cell was selected twice.
   */
  void refreshBoard(ReadOnlySanguineModel model, Point selectedCell);

  /**
   * Refresh the deck view to reflect any changes in the game state.
   *
   * @param model        the newly changed model.
   * @param selectedCard the index of the card that has been selected in the hands deck. This is -1
   *                     if none was selected, or if the same index was selected twice.
   */
  void refreshDeck(ReadOnlySanguineModel model, int selectedCard);

  /**
   * Make the view visible to start the game session.
   */
  void makeVisible();

  /**
   * Subscribes a featuresListener to each of the publishers in the frame. (The board and cards
   * within the hand).
   *
   * @param listener the subscriber.
   */
  void subscribe(FeaturesListener listener);

  /**
   * Displays a message to the view with the given message and title.
   *
   * @param message the message to display.
   * @param title   the title to display.
   */
  void showMessage(String message, String title);
}
