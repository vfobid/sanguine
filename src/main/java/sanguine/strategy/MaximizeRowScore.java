package sanguine.strategy;

import java.util.ArrayList;
import java.util.List;
import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.model.SanguineCard;
import sanguine.model.SanguineElement;

/**
 * The maximize row score strategy chooses a move that allows the current player to win
 * a certain row by making their row score higher than their opponent's.
 * Rows are looked at from top to bottom.
 * If there is no move that can win a row, then an empty list is returned.
 */
public class MaximizeRowScore implements SanguineStrategy {

  @Override
  public List<Move> chooseMoves(ReadOnlySanguineModel model, Player player) {
    Player opponent;
    if (player == Player.RED) {
      opponent = Player.BLUE;
    } else {
      opponent = Player.RED;
    }

    int numRows = model.getNumRows();
    int numCols = model.getNumColumns();
    List<SanguineCard> hand = model.getHands(player);

    for (int row = 0; row < numRows; row++) {
      int currentPlayerScore = model.sumOfVals(row, player);
      int opponentScore = model.sumOfVals(row, opponent);

      if (currentPlayerScore <= opponentScore) {
        List<Move> winningMoves = findWinningMoves(model, hand, row,
            numCols, currentPlayerScore, opponentScore);

        if (!winningMoves.isEmpty()) {
          return winningMoves;
        }
      }
    }
    return new ArrayList<>();
  }

  /**
   * Finds moves that would help the player win the given row.
   *
   * @param model              the model of the game.
   * @param hand               the player's hand.
   * @param row                the row to look at to win.
   * @param numCols            the number of columns on the board.
   * @param currentPlayerScore the current score of the player in the row.
   * @param opponentScore      the score of the opponent in the row.
   * @return a list of moves that could win the row which is empty if there are no moves.
   */
  private List<Move> findWinningMoves(ReadOnlySanguineModel model, List<SanguineCard> hand,
                                      int row, int numCols, int currentPlayerScore,
                                      int opponentScore) {
    List<Move> winningMoves = new ArrayList<>();

    for (int cardInd = 0; cardInd < hand.size(); cardInd++) {
      SanguineCard card = hand.get(cardInd);

      for (int col = 0; col < numCols; col++) {
        if (isValidMove(model, card, row, col)) {
          int newScore = currentPlayerScore + card.getValue();
          if (newScore > opponentScore) {
            winningMoves.add(new Move(cardInd, row, col));
            return winningMoves;
          }
        }
      }
    }
    return winningMoves;
  }

  /**
   * Checks if placing a card at a position is valid.
   *
   * @param model the model of the game.
   * @param card  the card that needs to be placed.
   * @param row   the row coordinate.
   * @param col   the column coordinate.
   * @return true if the move is possible and false if it is not.
   */
  private boolean isValidMove(ReadOnlySanguineModel model, SanguineCard card,
                              int row, int col) {
    try {
      SanguineElement element = model.getBoard()[row][col];
      return model.canBePlaced(card, element);
    } catch (Exception e) {
      return false;
    }
  }
}
