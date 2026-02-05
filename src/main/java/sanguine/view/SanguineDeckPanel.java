package sanguine.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.model.SanguineCard;

/**
 * Represents an entire hand of a player. This panel simply holds the SanguineCardPanel objects,
 * and does not directly note any mouseClick events itself.
 */
public class SanguineDeckPanel extends JPanel implements DeckPanel {
  ReadOnlySanguineModel model;
  private List<SanguineCard> cards;
  private List<FeaturesListener> listeners;
  //max number of cards visible in a hand
  private int numHands;
  private int selectedIndex = -1;
  private Player player;

  /**
   * Constructs a panel hand of cards, only intializaing the model, and setting up the layout. No
   * cards have be added to the view.
   *
   * @param model the model that will be observed.
   *  @param player the player who this panel displays cards for.
   */
  public SanguineDeckPanel(ReadOnlySanguineModel model, Player player) {
    this.model = model;
    this.player =  player;
    this.listeners = new ArrayList<>();
    setLayout(new FlowLayout(FlowLayout.LEFT));
    setBackground(Color.DARK_GRAY);
  }

  /**
   * Repaints the hands after recieving new information about the game state.
   * Note: MUST refresh deck to initialize card deck for game play view.
   *
   * @param model         the potentially  recently changed model.
   * @param selectedIndex the index of the card that has been clicked. This is -1 if no card was
   *                      clicked since the previous refresh, or if the same card was clicked.
   */
  public void refreshDeck(ReadOnlySanguineModel model, int selectedIndex) {
    removeAll();
    this.model = model;
    this.cards = model.getHands(player);
    this.selectedIndex = selectedIndex;

    for (int index = 0; index < cards.size(); index++) {
      boolean selectedCard = index == selectedIndex;
      add(new SanguineCardPanel(listeners, cards.get(index), index,
          selectedCard));
    }
    revalidate();
    repaint();
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
  }

  /**
   * Sets the subscriber of this panel to the inputted FeaturesListener. The event transmits the
   * card index at which a mouse click occured, from 0 through the maximum number of hand cards
   * visible.
   *
   * @param listener the new subscriber to the mouse events of this panel.
   */
  @Override
  public void setSubscriber(FeaturesListener listener) {
    this.listeners.add(listener);
    //refresh deck so that cards have updated subscriber list
    refreshDeck(model, selectedIndex);
  }
}
