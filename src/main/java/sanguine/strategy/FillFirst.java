package sanguine.strategy;

import java.util.ArrayList;
import java.util.List;
import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.model.SanguineCard;

/**
 * The fill first strategy chooses the first valid card and location combination.
 * It iterates through the cards in the hand, then through the board positions.
 */
public class FillFirst implements SanguineStrategy {

  @Override
  public List<Move> chooseMoves(ReadOnlySanguineModel model, Player player) {
    List<Move> moves = new ArrayList<>();
    List<SanguineCard> hand = model.getHands(player);
    int numRows = model.getNumRows();
    int numCols = model.getNumColumns();

    for (int cardInd = 0; cardInd < hand.size(); cardInd++) {
      SanguineCard card = hand.get(cardInd);

      for (int row = 0; row < numRows; row++) {
        for (int col = 0; col < numCols; col++) {
          if (isValidMove(model, card, row, col)) {
            moves.add(new Move(cardInd, row, col));
            return moves;
          }
        }
      }
    }
    return moves;
  }

  /**
   * Checks if placing a card at a position is valid.
   *
   * @param model the model of the game.
   * @param card  the card to be placed.
   * @param row   the row coordinate.
   * @param col   the column coordinate.
   * @return true if the move is possible and false if it is not.
   */
  private boolean isValidMove(ReadOnlySanguineModel model, SanguineCard card,
                              int row, int col) {
    try {
      return model.canBePlaced(card, model.getBoard()[row][col]);
    } catch (Exception e) {
      return false;
    }
  }
}
