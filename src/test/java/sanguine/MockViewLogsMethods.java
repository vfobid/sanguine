package sanguine;

import java.awt.Point;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.view.FeaturesListener;
import sanguine.view.IView;

/**
 * Mock view that logs which methods are invoked.
 */
public class MockViewLogsMethods implements IView {

  private final StringBuilder log;

  /**
   * Constructs a mock view.
   *
   * @param log the StringBuilder to which the view will append its information.
   */
  public MockViewLogsMethods(StringBuilder log) {
    this.log = log;
  }

  @Override
  public void refreshBoard(ReadOnlySanguineModel model, Point selectedCell) {
    log.append("refreshBoard(").append(model).append(", ").append(selectedCell).append(")\n");
  }

  @Override
  public void refreshDeck(ReadOnlySanguineModel model, int selectedCard) {
    log.append("refreshDeck(").append(model).append(", ").append(selectedCard).append(")\n");
  }

  @Override
  public void makeVisible() {
    log.append("makeVisible()\n");
  }

  @Override
  public void subscribe(FeaturesListener listener) {
    log.append("subscribe(").append(listener).append(")\n");
  }

  @Override
  public void showMessage(String message, String title) {
    log.append("showMessage(").append(message).append(", ").append(title).append(")\n");
  }
}
