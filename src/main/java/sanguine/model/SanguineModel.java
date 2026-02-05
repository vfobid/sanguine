package sanguine.model;

import java.util.List;

/**
 * Represents a model for the game Sanguine. Contains methods for initializing and starting a game,
 * along with methods for mutating the board during gameplay.
 */
public interface SanguineModel extends ReadOnlySanguineModel {

  /**
   * Determines if the inputted deck is valid. A deck is valid if no cards are null, there are
   * enough cards in the deck to fill every cell on the board, and there is at most 2 copies of any
   * given card.
   *
   * @param deck the deck to evaluate.
   * @return true if the deck is valid, else false.
   */
  boolean isValidDeck(List<SanguineCard> deck);

  /**
   * Starts game by dealing cards to both players from their respective piles, and initializes
   * board with the first and last columns containing 1 pawn in each row.
   *
   * @param numHands the number of cards in each player's hand. Must be <= 1/3 of their deck.
   * @param redDeck  the deck of cards for the red player.
   * @param blueDeck the deck of cards for the blue player.
   * @param shuffle  a boolean that is true if the decks should be shuffled, else false.
   * @throws IllegalArgumentException if the number of hands is negative or >= 1/3 of the deck.
   */
  void configureGame(int numHands, List<SanguineCard> redDeck, List<SanguineCard> blueDeck,
                     boolean shuffle)
      throws IllegalArgumentException;

  /**
   * Starts the game by notifying listeners that the game has started and it is
   * the first player's turn.
   * This is called after all controllers have subscribed to the model.
   */
  void startGame();

  /**
   * Places the specified card in the specified row and column. Applies card influence to affected
   * areas. A card can only legally be placed if the destination has enough pawns to cover the cost
   * of the card.
   *
   * @param handIndex the index for the card within the hand of either player to be placed.
   * @param row       the row to place the card in.
   * @param col       the column to place the card in.
   * @throws IllegalArgumentException if the handIndex is out of bounds of the current player's
   *                                  hand.
   * @throws IllegalStateException    if the card cannot legally be placed.
   */
  void placeCard(int handIndex, int row, int col)
      throws IllegalArgumentException, IllegalStateException;

  /**
   * Applies the influence of the SanguineCard on the affected cell of the board. The cell
   * is specified by the row and col parameters. If the influence removed all pawns from a cell,
   * the cell becomes null.
   *
   * @param infl the influence that will affect other cards.
   * @param row  the row of the cell.
   * @param col  the column of the cell.
   */
  void applyInfluence(Influence infl, int row, int col);

  /**
   * Draws a card from the red pile if the current player is the red player and adds it to the red
   * player's hand, else draws a card from the blue pile and adds it to blue player's hand.If there
   * are no cards left in the deck to draw, returns false, else returns true.
   *
   * @throws IllegalStateException if there are no more cards left in the deck to draw.
   */
  void drawCard() throws IllegalStateException;

  /**
   * Switches the current player to the other player. For example, if the current player is the red
   * player, it becomes the blue player.
   */
  void switchPlayer();

  /**
   * Subscribes a listener to this model.
   *
   * @param listener the new subscriber to the model.
   */
  void subscribe(ModelListener listener);

  /**
   * Ends the game.
   */
  void endGame();
}
