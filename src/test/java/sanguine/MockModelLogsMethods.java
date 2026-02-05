package sanguine;

import java.util.List;
import sanguine.model.*;

/**
 * Mock model that logs which methods are used. It returns 5 for the number of hands,
 * 3 for the number of rows, and 5 for the number of columns.
 */
public class MockModelLogsMethods implements SanguineModel {

  private final StringBuilder log;

  /**
   * Constructs a mock view.
   *
   * @param log the StringBuilder to which the model will append its information.
   */
  public MockModelLogsMethods(StringBuilder log) {
    this.log = log;
  }

  @Override
  public boolean isValidDeck(List<SanguineCard> deck) {
    log.append("isValidDeck(").append(deck).append(")\n");
    return false;
  }

  @Override
  public void configureGame(int numHands, List<SanguineCard> redDeck,
                            List<SanguineCard> blueDeck, boolean shuffle) {
    log.append("configureGame(")
        .append(numHands).append(", ")
        .append(redDeck).append(", ")
        .append(blueDeck).append(", ")
        .append(shuffle).append(")\n");
  }

  @Override
  public void startGame() {
    log.append("startGame called\n");
  }

  @Override
  public void placeCard(int handIndex, int row, int col) {
    log.append("placeCard(").append(handIndex).append(", ")
        .append(row).append(", ").append(col).append(")\n");
  }

  @Override
  public void applyInfluence(Influence infl, int row, int col) {
    log.append("applyInfluence(").append(infl).append(", ")
        .append(row).append(", ").append(col).append(")\n");
  }

  @Override
  public void drawCard() {
    log.append("drawCard()\n");
  }

  @Override
  public void switchPlayer() {
    log.append("switchPlayer()\n");
  }

  @Override
  public void subscribe(ModelListener listener) {
    log.append("subscribe(").append(listener).append(")\n");
  }

  @Override
  public void endGame() {
    log.append("endGame()\n");
  }

  @Override
  public SanguineElement[][] getBoard() {
    log.append("getBoard()\n");
    return new SanguineElement[0][0];
  }

  @Override
  public int getNumHands() {
    log.append("getNumHands()\n");
    return 5;
  }

  @Override
  public int sumOfVals(int row, Player player) {
    log.append("sumOfVals(").append(row).append(", ").append(player).append(")\n");
    return 0;
  }

  @Override
  public boolean isGameOver() {
    log.append("isGameOver()\n");
    return false;
  }

  @Override
  public Player getWinner() {
    log.append("getWinner()\n");
    return null;
  }

  @Override
  public List<SanguineCard> getHands(Player player) {
    log.append("getHands(").append(player).append(")\n");
    return List.of();
  }

  @Override
  public List<SanguineCard> getDeck(Player player) {
    log.append("getDeck(").append(player).append(")\n");
    return List.of();
  }

  @Override
  public int getScore(Player player) {
    log.append("getScore(").append(player).append(")\n");
    return 0;
  }

  @Override
  public int getNumRows() {
    log.append("getNumRows()\n");
    return 3;
  }

  @Override
  public int getNumColumns() {
    log.append("getNumColumns()\n");
    return 5;
  }

  @Override
  public Player getCurrPlayer() {
    log.append("getCurrPlayer()\n");
    return Player.RED;
  }

  @Override
  public boolean canBePlaced(SanguineCard card, SanguineElement elem) {
    log.append("canBePlaced(").append(card).append(", ").append(elem).append(")\n");
    return false;
  }
}
