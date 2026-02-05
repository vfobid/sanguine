package sanguine.view;

import java.io.File;
import java.util.List;
import sanguine.controller.BasicSanguineController;
import sanguine.controller.SanguineController;
import sanguine.model.BasicSanguineModel;
import sanguine.model.Player;
import sanguine.model.SanguineCard;
import sanguine.model.SanguineElement;
import sanguine.model.SanguineModel;
import sanguine.player.HumanPlayer;
import sanguine.player.PlayerActions;

/**
 * OLD main class of the Sanguine game (refer to SanguineGame.java file).
 * Shows the functions of the model and textual view in
 * an automated game.
 */
public class Sanguine {

  /**
   * Runs a Sanguine game.
   * Compile:
   * javac -d out (Get-ChildItem -Recurse -Filter *.java src\main\java |
   * ForEach-Object { $_.FullName })
   * Run: java -cp out sanguine.Sanguine docs\example.deck
   *
   * @param args command line arguments.
   */
  public static void main(String[] args) {


    //    TracingTurtleModel model = new SmarterTurtle();
    //    IView view = new TurtleGraphicsView();
    //    IController controller = new MVCCommandController(model, view);
    //    controller.go();

    String filePath = args[0];
    File file = new File(filePath);

    if (!file.exists()) {
      System.out.println("Error: File not found at " + filePath);
      return;
    }

    SanguineModel model = new BasicSanguineModel(3, 5);

    IView view = new JFrameView(model, Player.RED);
    PlayerActions player = new HumanPlayer();
    SanguineController controller =
        new BasicSanguineController(view, player, Player.RED);
    List<SanguineCard> redDeck = controller.createDeck(filePath, Player.RED);
    List<SanguineCard> blueDeck = controller.createDeck(filePath, Player.BLUE);

    if (redDeck.isEmpty() || blueDeck.isEmpty()) {
      System.out.println("Error: failed to load deck");
      return;
    }

    if (!model.isValidDeck(redDeck) || !model.isValidDeck(blueDeck)) {
      System.out.println("Error: invalid deck. There must be at least 15 cards.");
      return;
    }

    try {
      model.configureGame(5, redDeck, blueDeck, false);
    } catch (IllegalArgumentException e) {
      System.out.println("Error: can't start game. " + e.getMessage());
      return;
    }

    TextualView textview = new TextualView();
    System.out.println("Game Start: ");
    System.out.println(textview.render(model));
    System.out.println();

    while (!model.isGameOver()) {
      playTurn(model, textview);
    }

    printGameEnd(model, textview);
  }

  /**
   * Executes a turn.
   * Draws a card, trys to place a card, or passes.
   * Prints the action taken and the resulting board.
   *
   * @param model the model of the game.
   * @param textview  the view to render.
   */
  private static void playTurn(SanguineModel model, TextualView textview) {
    String player;
    if (model.getCurrPlayer().equals(Player.RED)) {
      player = "Red";
    } else {
      player = "Blue";
    }

    model.drawCard();

    boolean placed = tryPlaceCard(model);

    if (placed) {
      System.out.println(player + " placed card");
    } else {
      System.out.println(player + " passed");
    }

    System.out.println(textview.render(model));
    System.out.println();

    model.switchPlayer();
  }

  /**
   * Attempts to place a card from the current player's hand.
   * Tries each card at every position on the board until one works.
   *
   * @param model the model of the game.
   * @return true if the card was placed and false if it could not be placed.
   */
  private static boolean tryPlaceCard(SanguineModel model) {
    SanguineElement[][] board = model.getBoard();
    int rows = board.length;
    int cols = board[0].length;

    for (int cardIndex = 0; cardIndex < 10; cardIndex++) {
      for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
          try {
            model.placeCard(cardIndex, row, col);
            return true;
          } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Invalid card placement.");
          }
        }
      }
    }
    return false;
  }

  /**
   * Prints the final game results and includes the winner.
   *
   * @param model the model of the game.
   * @param textview the view to be rendered.
   */
  private static void printGameEnd(SanguineModel model, TextualView textview) {
    System.out.println("Game Over");
    System.out.println("Final Board: ");
    System.out.println(textview.render(model));
    System.out.println();

    Player winner = model.getWinner();
    if (winner == Player.RED) {
      System.out.println("Winner: Red");
    } else if (winner == Player.BLUE) {
      System.out.println("Winner: Blue");
    } else {
      System.out.println("Tie");
    }
  }
}
