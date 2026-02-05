package sanguine.view;

import java.awt.Point;
import sanguine.model.ReadOnlySanguineModel;

/**
 * Interface for the board panel in Sanguine. Includes information from the game board.
 */
public interface BoardPanel {
  /**
   * Method for repainting the panel. To be used after the controller has made changes to the view.
   *
   * @param model        the recently changed view.
   * @param selectedCell the cell selected between the last mouse event and the present.
   */
  void refreshBoard(ReadOnlySanguineModel model, Point selectedCell);

  /**
   * Adds the listener to the list of subscribers of this panel.
   *
   * @param listener the new subscriber to the mouse events of this panel.
   */
  void setSubscriber(FeaturesListener listener);
}
