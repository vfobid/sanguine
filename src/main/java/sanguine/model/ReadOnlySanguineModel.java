package sanguine.model;

import java.util.List;

/**
 * A readonly version of the Sanguine model. Contains all observation methods of the model.
 */
public interface ReadOnlySanguineModel {
  /**
   * Returns a copy of the current state of the board.
   *
   * @return a copy of the current state of the board.
   */
  SanguineElement[][] getBoard();

  /**
   * Returns the maximum number of cards in a player's hand that is visible at any moment.
   *
   * @return the maximum number of visible cards in a player's hand.
   */
  int getNumHands();

  /**
   * Returns the sum of all the values of the cards belonging to the specified player in the row.
   *
   * @param row specifies the row on which to preform the calculation.
   * @return the total score for that row, for the specified player
   */
  int sumOfVals(int row, Player player);

  /**
   * Determines if the game is over. The game ends once both players do not place a card back
   * to back.
   *
   * @return true if the game has ended else false.
   */
  boolean isGameOver();

  /**
   * Calculates and returns the winner, as a Player enum. If there is a tie, returns null.
   * The winner is the player with the largest sum of row scores.The row score for a player is the
   * sum of the values of the cards belonging to the player IF that sum is larger than that of the
   * other player's for that row, else it is 0.
   *
   * @return the player of the game: red, blue, or null if there is no winner.
   */
  Player getWinner();

  /**
   * Returns a list of the cards in the hands of the inputted player.
   *
   * @param player the player. Either red or blue.
   * @return a list of the cards  in the hands of the player.
   */
  List<SanguineCard> getHands(Player player);

  /**
   * Returns a list of the deck of the inputted player.
   *
   * @param player the player. Either red or blue.
   * @return a list of the deck of the inputted player.
   */
  List<SanguineCard> getDeck(Player player);

  /**
   * Returns the score of the inputted player, which is the sum of all rows where the sums of the
   * values of the inputted player cards' are larger than that of the opposite player's.
   *
   * @param player the player. Either red or blue.
   * @return the score of the inputted player as an int.
   */
  int getScore(Player player);

  /**
   * Returns the number of rows of the game board.
   *
   * @return the number of rows of game board.
   */
  int getNumRows();

  /**
   * Returns the number of columns of the game board.
   *
   * @return the number of columns of game board.
   */
  int getNumColumns();

  /**
   * Determines who the current player is. Either red or blue.
   *
   * @return the current player as a Player enum.
   */
  Player getCurrPlayer();

  /**
   * Returns truw if the card can be placed in the cell.
   * It can be placed if the number of pawns in the cluster is greater than or equal
   * to the cost of the card.
   *
   * @param card the card to be placed.
   * @param elem the element on the board which the card may replace.
   * @return true if the card can be placed and false if it cannot.
   */
  boolean canBePlaced(SanguineCard card, SanguineElement elem);
}
