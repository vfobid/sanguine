package sanguine.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JPanel;
import sanguine.model.Influence;
import sanguine.model.Player;
import sanguine.model.SanguineCard;


/**
 * Represents a card in the hands of a player (either Player blue or red). Draws the card with
 * information such as the player who owns it, the value, cost, and influence board.
 */
public class SanguineCardPanel extends JPanel implements CardPanel {

  private final SanguineCard card;
  private final int index;
  //reprents if the card was selected betweent he last mouse event and the present.
  private boolean isSelected;

  /**
   * Constructs a SanguineCardPanel, sets the subscriber of this panel to the inputted
   * FeaturesListener. The event transmits the
   * index of the card at which a mouse click occurred, indexed at 0. Note: this occurs when the
   * game is started in the model.
   *
   * @param allListeners the subcscribers to the events occuring in the card (mouse clicks).
   * @param card         the specific card being represented in this panel.
   * @param index        the index of the card from the player's hand.
   * @param isSelected   true if the card has been selected since the last mouse event, else false.
   */
  public SanguineCardPanel(List<FeaturesListener> allListeners, SanguineCard card, int index,
                           boolean isSelected) {
    this.card = card;
    this.index = index;
    this.isSelected = isSelected;

    setPreferredSize(new Dimension(120, 180));

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        for (FeaturesListener listener : allListeners) {
          listener.onCardClick(index, card);
        }
      }
    });
    this.setEnabled(true);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    //border
    g.setColor(isSelected ? Color.CYAN : Color.BLACK);
    g.fillRect(0, 0, getWidth(), getHeight());

    //owner color inside panel
    g.setColor(card.getOwner() == Player.RED ? Color.RED : Color.BLUE);
    g.fillRect(2, 2, getWidth() - 3, getHeight() - 3);

    //text area
    g.setColor(Color.BLACK);
    g.setFont(g.getFont().deriveFont(Font.PLAIN, 12f));
    g.drawString(card.getName(), 5, 15);
    g.drawString("Cost: " + card.getCost(), 5, 30);
    g.drawString("Value: " + card.getValue(), 5, 45);

    int textAreaHeight = 45;

    //grid dimensions
    int gridWidth = getWidth() - 10;
    int gridHeight = getHeight() - textAreaHeight - 10;
    int influenceCellWidth = gridWidth / 5;
    int influenceCellHeight = gridHeight / 5;

    Influence[][] grid = card.getInfluenceGrid();

    int influenceStartY = textAreaHeight + 5;

    for (int row = 0; row < card.getInfluenceGrid().length; row++) {
      for (int col = 0; col < card.getInfluenceGrid()[0].length; col++) {
        int x = 5 + col * influenceCellWidth;
        int y = influenceStartY + row * influenceCellHeight;

        if (row == 2 && col == 2) {
          g.setColor(Color.YELLOW); //center always maincard
        } else if (grid[row][col] != null) {
          g.setColor(Color.CYAN);   //influence present
        } else {
          g.setColor(Color.GRAY);   //empty cell
        }
        g.fillRect(x, y, influenceCellWidth, influenceCellHeight);
        g.setColor(Color.BLACK);
        g.drawRect(x, y, influenceCellWidth, influenceCellHeight);
      }
    }
  }
}

