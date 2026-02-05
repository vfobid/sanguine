package sanguine.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.model.SanguineCard;
import sanguine.model.SanguineElement;
import sanguine.model.SanguinePawnCluster;

/**
 * Represents the board of the game of Sanguine. This includes an extra column on either side
 * of the actual playing board, with sums of each player's scores for each row.
 */
public class SanguineBoardPanel extends JPanel implements BoardPanel {

  private ReadOnlySanguineModel model;
  private List<FeaturesListener> allListeners;
  //the cell selected on the GUI if any
  private Point selectedCell;

  /**
   * Constructs a board, initializing the model.
   *
   * @param model the readonly model to be read.
   */
  public SanguineBoardPanel(ReadOnlySanguineModel model) {
    this.model = model;
    this.selectedCell = null;
    this.allListeners = new ArrayList<>();

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent event) {
        int rows = model.getNumRows();
        int cols = model.getNumColumns() + 2;

        int row = (int) (event.getY() * (rows / (double) getHeight()));
        int col = (int) (event.getX() * (cols / (double) getWidth()));

        for (FeaturesListener listener : allListeners) {
          listener.onBoardClick(row, col);
        }
      }
    });
  }

  /**
   * Method for repainting the panel. To be used after the controller has made changes to the view.
   *
   * @param model        the recently changed view.
   * @param selectedCell the cell selected between the last mouse event and the present, represented
   *                     as (row, column), corresponding to cells of the game board, indexed by
   *                     (0,0) starting at the top leftmost column, which is the red player's row 1
   *                     score. This is null if no mouse event occured on the panel or if the event
   *                     occured on the same coordinates.
   */
  @Override
  public void refreshBoard(ReadOnlySanguineModel model, Point selectedCell) {
    this.model = model;
    this.selectedCell = selectedCell;
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    int numRows = model.getNumRows();
    int numColumns = model.getNumColumns() + 2;

    int cellWidth = getWidth() / numColumns;
    int cellHeight = getHeight() / numRows;

    for (int row = 0; row < numRows; row++) {
      for (int col = 0; col < numColumns; col++) {
        int xcoord = col * cellWidth;
        int ycoord = row * cellHeight;

        g.setColor(Color.BLACK);
        g.fillRect(xcoord, ycoord, cellWidth, cellHeight);
        if (col == 0 || col == numColumns - 1) {                 //score rows on either side
          g.setColor(Color.LIGHT_GRAY);
          g.fillRect(xcoord + 2, ycoord + 2, cellWidth - 3, cellHeight - 3);
          g.setColor(Color.BLACK);
          g.setFont(new Font("Arial", Font.BOLD, 20));
          g.drawString(String.valueOf(model.sumOfVals(row, col == 0 ? Player.RED : Player.BLUE)),
              xcoord + cellWidth / 2, ycoord + cellHeight / 2);
        } else {
          if (selectedCell != null && row == this.selectedCell.x //element has been selected
              && col == this.selectedCell.y) {
            g.setColor(Color.CYAN);
            g.fillRect(xcoord, ycoord, cellWidth, cellHeight);
          }
          SanguineElement el = model.getBoard()[row][col - 1];
          g.setColor(Color.DARK_GRAY);
          g.fillRect(xcoord + 2, ycoord + 2, cellWidth - 4, cellHeight - 3);
          if (el instanceof SanguineCard) {                       //determine what element is
            drawSanguineCard(g, (SanguineCard) el, xcoord, ycoord, cellWidth, cellHeight);
          } else if (el instanceof SanguinePawnCluster) {
            drawSanguineCluster(g, (SanguinePawnCluster) el, xcoord, ycoord, cellWidth, cellHeight);
          } else {
          }
        }
      }
    }
  }

  /**
   * Helper method that draws a SanguineCard.
   *
   * @param g          the painting tool.
   * @param el         the element on the board being drawn (will be a SanguineCard).
   * @param xcoord     the topleft x coordinate of the element.
   * @param ycoord     the topleft y coordinate of the element.
   * @param cellWidth  the width the card will be.
   * @param cellHeight the height the card will be.
   */
  private void drawSanguineCard(Graphics g, SanguineCard el, int xcoord, int ycoord,
                                int cellWidth,
                                int cellHeight) {

    g.setColor(el.getOwner() == Player.RED ? Color.RED : Color.BLUE);
    g.drawString(
        String.valueOf("" + el.getValue()),
        xcoord + cellWidth / 2 - 5,
        ycoord + cellHeight / 2 + 5
    );
  }

  /**
   * Helper method that draws a SanguinePawnCluster.
   *
   * @param g          the painting tool.
   * @param el         the element on the board being drawn (will be a SanguinePawnCluster).
   * @param xcoord     the topleft x coordinate of the element.
   * @param ycoord     the topleft y coordinate of the element.
   * @param cellWidth  the width of the cell the element is in.
   * @param cellHeight the height the cell the element is in.
   */
  private void drawSanguineCluster(Graphics g, SanguinePawnCluster el, int xcoord, int ycoord,
                                   int cellWidth,
                                   int cellHeight) {

    SanguinePawnCluster cluster = el;
    int clusterSize = cluster.getNumPawns();

    // background is already filled by caller
    g.setColor(el.getOwner() == Player.RED ? Color.RED : Color.BLUE);

    // Correct centering
    int totalWidth = clusterSize * 36; // 30px circle + 6px spacing
    int xstart = xcoord + (cellWidth - totalWidth) / 2;
    int ystart = ycoord + (cellHeight - 30) / 2; // vertically centered

    for (int i = 0; i < clusterSize; i++) {
      g.fillOval(xstart + i * 36, ystart, 30, 30);
    }
  }

  /**
   * Sets the subscriber of this panel to the inputted FeaturesListener. The event transmits the
   * point at which a mouse click occurred, in the form (row, col), indexed at (0,0), starting with
   * the top left cell where the red player's row 1 score is.
   *
   * @param listener the new subscriber to the mouse events of this panel.
   */
  @Override
  public void setSubscriber(FeaturesListener listener) {
    this.allListeners.add(listener);
  }
}


