package sanguine.model;

import java.util.List;

/**
 * Implements the ReadOnlySanguineModel interface, while using a SanguineModel delegate
 * to implement the read-only methods. Protects the model from being cast to Sanguine model and
 * mutated when passed in to the view in the main method.
 */
public class ViewModel implements ReadOnlySanguineModel {
  SanguineModel adaptee;

  /**
   * Initializes the delegate using the inputted model.
   *
   * @param model the Sanguine model to be used for method implementation.
   */
  public ViewModel(SanguineModel model) {
    adaptee = model;
  }

  @Override
  public SanguineElement[][] getBoard() {
    return adaptee.getBoard();
  }

  @Override
  public int getNumHands() {
    return adaptee.getNumHands();
  }

  @Override
  public int sumOfVals(int row, Player player) {
    return adaptee.sumOfVals(row, player);
  }

  @Override
  public boolean isGameOver() {
    return adaptee.isGameOver();
  }

  @Override
  public Player getWinner() {
    return adaptee.getWinner();
  }

  @Override
  public List<SanguineCard> getHands(Player player) {
    return adaptee.getHands(player);
  }

  @Override
  public List<SanguineCard> getDeck(Player player) {
    return adaptee.getDeck(player);
  }

  @Override
  public int getScore(Player player) {
    return adaptee.getScore(player);
  }

  @Override
  public int getNumRows() {
    return adaptee.getNumRows();
  }

  @Override
  public int getNumColumns() {
    return adaptee.getNumColumns();
  }

  @Override
  public Player getCurrPlayer() {
    return adaptee.getCurrPlayer();
  }

  @Override
  public boolean canBePlaced(SanguineCard card, SanguineElement elem) {
    return adaptee.canBePlaced(card, elem);
  }
}
