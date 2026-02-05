package sanguine.controller;

import java.awt.Point;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import sanguine.model.BasicInfluence;
import sanguine.model.BasicSanguineCard;
import sanguine.model.FileReader;
import sanguine.model.Influence;
import sanguine.model.ModelListener;
import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.model.SanguineCard;
import sanguine.model.SanguineModel;
import sanguine.player.PlayerActions;
import sanguine.view.FeaturesListener;
import sanguine.view.IView;

/**
 * Basic implementation of a Sanguine controller. This constroller is able to present a
 * view of the type IView that represents a game of Sanguine. Currently, it only handles
 * events from the view, but will later also mutate the model and intake user input. It implements
 * the FeaturesListener interface so that it can be a subscriber to the view, and recieve updates
 * on events happening in the window.
 */
public class BasicSanguineController implements SanguineController, FeaturesListener,
    ModelListener {
  private SanguineModel model;
  private IView view;
  //the index of the card that has been selected according to the publisher.
  private int selectedCard;
  private Point selectedCell;
  private Player playerColor;
  private PlayerActions player;

  /**
   * Constructor for a controller. Intakes a view and initializes variables except for the model.
   *
   * @param view the view to be used in gamePlay.
   * @param player the player this controller will manage
   * @param playerColor the color of the player : red or blue
   */
  public BasicSanguineController(IView view, PlayerActions player, Player playerColor) {
    if (view == null || player == null || playerColor == null) {
      throw new IllegalArgumentException("View, player and player color cannot be null");
    }
    this.view = view;
    this.player = player;
    this.playerColor = playerColor;
    this.selectedCell = new Point();
    this.selectedCard = -1;
  }

  @Override
  public List<SanguineCard> createDeck(String filePath, Player owner) {
    List<String> lines = FileReader.getLines(filePath);
    List<SanguineCard> deck = new ArrayList<>();

    String cardName = null;
    int cardCost = -1;
    int cardValue = -1;
    //loop through each line of document
    Influence[][] currInfluence = new Influence[5][5];

    for (int fileRow = 0; fileRow < lines.size(); fileRow++) {
      if (fileRow % 6 == 0) {
        String[] parts = lines.get(fileRow).split(" ");
        cardName = parts[0];
        cardCost = Integer.parseInt(parts[1]);
        cardValue = Integer.parseInt(parts[2]);
      } else {
        int rowInGrid = (fileRow % 6) - 1;
        String line = lines.get(fileRow);
        for (int col = 0; col < line.length(); col++) {
          int gridCol = accurateColumn(col, owner);
          currInfluence[rowInGrid][gridCol] =
              line.charAt(col) == 'I' ? new BasicInfluence(1) : null;
        }
      }
      // After 6 lines, create card
      if (fileRow % 6 == 5) {
        Influence[][] influenceArray = new Influence[5][5];
        for (int i = 0; i < 5; i++) {
          influenceArray[i] = currInfluence[i].clone();
        }
        deck.add(new BasicSanguineCard(cardValue, cardCost, cardName, influenceArray, owner));
      }
    }

    return deck;
  }

  /**
   * Provides the reversed column index if it is the blue player's card being created,
   * else it returns the same index. Helper method for creating influence grid.
   *
   * @param col   the column index.
   * @param owner the owner of the element being placed.
   * @return the accurate column depending on whom the player is.
   */
  private int accurateColumn(int col, Player owner) {
    if (!(owner == Player.RED)) {
      return 5 - col - 1;
    }
    return col;
  }

  @Override
  public void playGame(SanguineModel model) {
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null");
    }
    this.model = model;

    this.model.subscribe(this);
    view.subscribe(this);
    player.subscribe(this);

    if (this.playerColor == Player.RED) {
      try {
        model.drawCard();
      } catch (IllegalStateException e) {
      }
    }
    refreshAll();
    view.makeVisible();
  }

  @Override
  public void onBoardClick(int row, int col) {
    if (col > 0 && col <= model.getNumColumns()) {
      if (this.playerColor != model.getCurrPlayer()) {
        return;
      }
      System.out.println("Board cell: ( " + row + " , " + col + " )");
      boolean sameSelectedCell = false;
      if (this.selectedCell != null) {
        sameSelectedCell = this.selectedCell.x == row && this.selectedCell.y == col;
      }
      this.selectedCell = sameSelectedCell ? null : new Point(row, col);
      refreshAll();
    }
  }


  @Override
  public void onCardClick(int index, SanguineCard card) {
    if (model.getCurrPlayer() != this.playerColor) {
      return;
    }

    String owner = card.getOwner() == Player.BLUE ? "Blue" : "Red";
    System.out.println("Hand index: " + index + "     Card owner: " + owner);
    boolean sameSelectedCard = this.selectedCard == index;
    this.selectedCard = sameSelectedCard ? -1 : index;
    refreshAll();
  }

  @Override
  public void onConfirm() {
    if (this.playerColor != model.getCurrPlayer()) {
      return;
    }

    if (selectedCell != null && selectedCard != -1) {
      System.out.println("Key press: CONFIRM");
      try {
        this.model.placeCard(this.selectedCard, this.selectedCell.x, this.selectedCell.y - 1);
        selectedCell = null;
        selectedCard = -1;
        refreshAll();
        model.switchPlayer();
      } catch (IllegalArgumentException | IllegalStateException e) {
        view.showMessage(e.getMessage(), "Error");
      } catch (ConcurrentModificationException e) {
      }
    } else if (selectedCell == null || selectedCard == -1) {
      view.showMessage("Select a cell and card", "Invalid selection");
    }
  }

  @Override
  public void onPass() {
    if (this.playerColor != model.getCurrPlayer()) {
      return;
    }

    try {
      System.out.println("Key press: PASS");
      selectedCell = null;
      selectedCard = -1;
      model.switchPlayer();
    } catch (ConcurrentModificationException e) {
    }
  }

  @Override
  public void refreshAll() {
    view.refreshBoard((ReadOnlySanguineModel) model, selectedCell);
    view.refreshDeck((ReadOnlySanguineModel) model, selectedCard);
  }

  @Override
  public void onTurnSwitch(Player player) {
    if (model.getCurrPlayer() == this.playerColor) {
      try {
        model.drawCard();
      } catch (IllegalStateException e) {
      } catch (ConcurrentModificationException e) {
      }

      refreshAll();
      this.player.notifyTurn((ReadOnlySanguineModel) model);

      if (model.isGameOver()) {
        model.endGame();
      }
    }
  }

  @Override
  public void onGameOver() {
    Player winner = model.getWinner();
    if (winner == null) {
      view.showMessage("Game over. Result: TIE", "Game Over");
    } else if (winner == Player.RED) {
      view.showMessage(
          "Game over. Winner: Player RED, with score, " + model.getScore(Player.RED),
          "Game Over");
    } else {
      view.showMessage(
          "Game over. Winner: Player BLUE, with score, " + model.getScore(Player.BLUE),
          "Game Over");
    }
  }
}
