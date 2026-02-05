package sanguine.view;

import sanguine.model.SanguineCard;

/**
 * Represents subscriber to a publisher. The two panels that will be observed by this
 * are the game board, as well as the cards in the current player's hand
 */
public interface FeaturesListener {

  /**
   * Reacts after there is a mouse click on the board, including the score columns.
   *
   * @param row the row where the mouse click occured, within the number of rows in the board.
   * @param col the column where the mouse click occured, within the number of columns in the board.
   */
  void onBoardClick(int row, int col);

  /**
   * Reacts after there is a mouse click on a card in the deck.
   *
   * @param index the index of the clicked card within the deck.
   * @param card  the SanguineCard object--the actual card that was clicked.
   */
  void onCardClick(int index, SanguineCard card);

  /**
   * Reacts after a key press indicating confirmation of the move.
   */
  void onConfirm();

  /**
   * Reacts after a key press indicating the player wants to pass.
   */
  void onPass();
}

