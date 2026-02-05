package sanguine.view;

import sanguine.model.BasicSanguineCard;
import sanguine.model.BasicSanguinePawnCluster;
import sanguine.model.Player;
import sanguine.model.SanguineElement;
import sanguine.model.SanguineModel;

/**
 * A textual view for the game.
 * Includes methods to render the game board and scores to display
 * the information in String format.
 * Empty cells: "_"
 * Pawns: 1-3 (number of pawns)
 * Red player cards: "R"
 * Blue player cards: "B"
 *
 */
public class TextualView {

  /**
   * Renders the current state of the game to represent it as a string.
   * Each row of the board if rendered as a separate line with row scores.
   *
   * @param model the model of the game which can't be null.
   * @return A string representation of the game board with scores.
   * @throws NullPointerException if the model is null.
   */
  public String render(SanguineModel model) {
    SanguineElement[][] board = model.getBoard();
    StringBuilder output = new StringBuilder();

    for (int row = 0; row < board.length; row++) {
      output.append(renderRow(model, board, row));
      if (row < board.length - 1) {
        output.append("\n");
      }
    }
    return output.toString();
  }

  /**
   * Renders a row of the game board with scores.
   * Calculates the row scores for the players.
   *
   * @param model the model being rendered.
   * @param board 2d array representing the board.
   * @param row   the index of the row to render.
   * @return a string showing the row with scores.
   */
  private String renderRow(SanguineModel model, SanguineElement[][] board, int row) {
    int redScore = model.sumOfVals(row, Player.RED);
    int blueScore = model.sumOfVals(row, Player.BLUE);

    StringBuilder rowString = new StringBuilder();
    rowString.append(redScore).append(" ");

    for (int col = 0; col < board[row].length; col++) {
      rowString.append(renderCell(board[row][col]));
    }

    rowString.append(" ").append(blueScore);
    return rowString.toString();
  }

  /**
   * Renders a single cell in the board.
   * Converts the element to a string representation.
   * If it is an empty cell: _
   * If it is a pawn cluster: 1-3 (number of pawns).
   * If it is a card: R for red and B for blue.
   *
   * @param element the board element
   * @return a character string representing the cell.
   */
  private String renderCell(SanguineElement element) {
    if (element == null) {
      return "_";
    } else if (element instanceof BasicSanguinePawnCluster) {
      return String.valueOf(((BasicSanguinePawnCluster) element).getNumPawns());
    } else if (element instanceof BasicSanguineCard card) {
      return card.toString();
    }
    return "_";
  }
}
