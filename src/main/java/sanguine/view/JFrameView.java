package sanguine.view;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;

/**
 * The implementation of IView. This class renders the view for the user.
 */
public class JFrameView extends JFrame implements IView {

  private final JPanel boardPanel;
  private final JPanel deckPanel;
  private final List<FeaturesListener> allListeners;

  /**
   * Constructs a JFrameView object. During this time it sets the panel sizes, and adds a
   * keyListener to this component. Enter confirms a move and Space passes the turn.
   *
   * @param model  the readonly the model will be reading information from.
   * @param player the player who this view displays content for.
   */
  public JFrameView(ReadOnlySanguineModel model, Player player) {
    setTitle(player == Player.RED ? "Red Player" : "Blue Player");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1000, 800);

    //panels
    boardPanel = new SanguineBoardPanel(model);
    deckPanel = new SanguineDeckPanel(model, player);
    allListeners = new ArrayList<>();

    boardPanel.setPreferredSize(new Dimension(1000, 600));
    deckPanel.setPreferredSize(new Dimension(1000, 200));

    // vertical split: board top, deck bottom
    JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, boardPanel, deckPanel);
    split.setResizeWeight(0.75); // 75% board 25% deck
    split.setDividerLocation(0.75);
    split.setEnabled(true);

    add(split);

    addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (allListeners != null) {
          for (FeaturesListener listener : allListeners) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
              listener.onConfirm();
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
              listener.onPass();
            }
          }
        }
      }
    });
    setFocusable(true);
  }

  @Override
  public void refreshBoard(ReadOnlySanguineModel model, Point selectedCell) {
    ((SanguineBoardPanel) boardPanel).refreshBoard(model, selectedCell);
  }

  @Override
  public void refreshDeck(ReadOnlySanguineModel model, int selectedCard) {
    ((SanguineDeckPanel) deckPanel).refreshDeck(model, selectedCard);
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }

  @Override
  public void subscribe(FeaturesListener listener) {
    this.allListeners.add(listener);
    ((SanguineBoardPanel) boardPanel).setSubscriber(listener);
    ((SanguineDeckPanel) deckPanel).setSubscriber(listener);
  }

  @Override
  public void showMessage(String message, String title) {
    JOptionPane.showInternalMessageDialog(null, message,
        title,
        Objects.equals(title, "Error") ? JOptionPane.ERROR_MESSAGE :
            JOptionPane.INFORMATION_MESSAGE);
  }
}

