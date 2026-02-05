package sanguine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.model.SanguineCard;
import sanguine.model.SanguineElement;

/**
 * Mock clas that records a transcript of the method calls to verify
 * the strategy classes.
 */
public class MockSanguineModel implements ReadOnlySanguineModel {
  private final Appendable log;
  private final List<SanguineCard> hand;
  private final int numRows;
  private final int numCols;
  private final Map<String, Integer> rowScores;
  private final Map<String, Boolean> legalMoves;

  /**
   * Creates a mock model that logs the method calls to the appendable.
   * It can be used to log method calls or for configured tests.
   *
   * @param log     the appendable where the method calls are logged.
   * @param hand    the hand to return
   * @param numRows the number of rows.
   * @param numCols the number of cols.
   */
  public MockSanguineModel(Appendable log, List<SanguineCard> hand,
                           int numRows, int numCols) {
    this.log = log;
    this.hand = new ArrayList<>(hand);
    this.numRows = numRows;
    this.numCols = numCols;
    this.rowScores = new HashMap<>();
    this.legalMoves = new HashMap<>();
  }

  /**
   * Configures the row score for a specific row and player.
   *
   * @param row    the index of the row.
   * @param player the player.
   * @param score  the score to return.
   */
  public void setRowScore(int row, Player player, int score) {
    rowScores.put(row + "-" + player, score);
  }

  /**
   * Configures if a move is legal at a specific position.
   *
   * @param cardInd the index of the card.
   * @param row     the row.
   * @param col     the column.
   * @param isLegal checks if the move is legal or not.
   */
  public void setMove(int cardInd, int row, int col, boolean isLegal) {
    legalMoves.put(cardInd + "-" + row + "-" + col, isLegal);
  }

  @Override
  public SanguineElement[][] getBoard() {
    try {
      log.append("getBoard").append(System.lineSeparator());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new SanguineElement[numRows][numCols];
  }

  @Override
  public int getNumHands() {
    try {
      log.append("getNumHands").append(System.lineSeparator());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return hand.size();
  }

  @Override
  public int sumOfVals(int row, Player player) {
    try {
      log.append("sumOfVals").append(String.valueOf(row)).append(" player")
          .append(player.toString()).append(System.lineSeparator());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    String key = row + "-" + player;
    if (rowScores.containsKey(key)) {
      return rowScores.get(key);
    }
    return 0;
  }

  @Override
  public boolean isGameOver() {
    try {
      log.append("isGameOver").append(System.lineSeparator());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return false;
  }

  @Override
  public Player getWinner() {
    try {
      log.append("getWinner").append(System.lineSeparator());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public List<SanguineCard> getHands(Player player) {
    try {
      log.append("getHands ").append(player.toString()).append(System.lineSeparator());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new ArrayList<>(hand);
  }

  @Override
  public List<SanguineCard> getDeck(Player player) {
    try {
      log.append("getDeck ").append(player.toString()).append(System.lineSeparator());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new ArrayList<>();
  }

  @Override
  public int getScore(Player player) {
    try {
      log.append("getScore ").append(player.toString()).append(System.lineSeparator());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return 0;
  }

  @Override
  public int getNumRows() {
    try {
      log.append("getNumRows").append(System.lineSeparator());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return numRows;
  }

  @Override
  public int getNumColumns() {
    try {
      log.append("getNumColumns").append(System.lineSeparator());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return numCols;
  }

  @Override
  public Player getCurrPlayer() {
    try {
      log.append("getCurrPlayer").append(System.lineSeparator());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return Player.RED;
  }

  @Override
  public boolean canBePlaced(SanguineCard card, SanguineElement elem) {
    int cardIndex = hand.indexOf(card);
    try {
      log.append("canBePlaced card").append(String.valueOf(cardIndex))
          .append(System.lineSeparator());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    for (String key : legalMoves.keySet()) {
      if (key.startsWith(cardIndex + "-")) {
        Boolean val = legalMoves.get(key);
        if (val != null) {
          return val;
        }
      }
    }
    return false;
  }
}
