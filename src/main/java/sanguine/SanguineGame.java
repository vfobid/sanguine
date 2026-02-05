package sanguine;

import sanguine.controller.BasicSanguineController;
import sanguine.controller.SanguineController;
import sanguine.model.BasicSanguineModel;
import sanguine.model.Player;
import sanguine.model.SanguineModel;
import sanguine.player.HumanPlayer;
import sanguine.player.MachinePlayer;
import sanguine.player.PlayerActions;
import sanguine.strategy.FillFirst;
import sanguine.strategy.MaximizeRowScore;
import sanguine.view.IView;
import sanguine.view.JFrameView;

/**
 * Class for initializing the GUI of Sanguine, using a stub controller.
 */
public final class SanguineGame {
  /**
   * Starts a game of Sanguine.
   * There should be 6 command-line arguments:
   * the first is the number of rows on the board
   * the second is the number of columns on the board
   * the third is the path to the file for Red's deck
   * the fourth is the path to the file for Blue's deck
   * fifth and sixth describe each of the players (Red first, then Blue) and their strategies,
   *    for example, `"human"`, `"strategy1"`, `"strategy2"`, and `"strategy3"`.
   * Example input:
   * 3 5 docs\\example.deck docs\\example.deck human strategy1
   *
   * @param args arguments.
   */
  public static void main(String[] args) {
  

    if (args.length != 6) {
      System.err.println("Usage: java SanguineGame <numRows> <numCols> "
          + "<redDeckPath> <blueDeckPath> <redPlayerType> <bluePlayerType>");
      System.err.println("Player types: human, strategy1, strategy2");
      return;
    }

    try {
      int numRows = Integer.parseInt(args[0]);
      int numCols = Integer.parseInt(args[1]);
      String redDeckPath = args[2];
      String blueDeckPath = args[3];
      String redPlayerType = args[4];
      String bluePlayerType = args[5];

      int numHands = 5;
      boolean shuffle = false;

      SanguineModel model = new BasicSanguineModel(numRows, numCols);

      IView viewPlayer1 = new JFrameView(model, Player.RED);
      IView viewPlayer2 = new JFrameView(model, Player.BLUE);

      PlayerActions player1 = createPlayer(redPlayerType, Player.RED);
      PlayerActions player2 = createPlayer(bluePlayerType, Player.BLUE);

      SanguineController controller1 = new BasicSanguineController(viewPlayer1,
          player1, Player.RED);
      SanguineController controller2 = new BasicSanguineController(viewPlayer2,
          player2, Player.BLUE);

      model.configureGame(numHands,
          controller1.createDeck(redDeckPath, Player.RED),
          controller2.createDeck(blueDeckPath, Player.BLUE),
          shuffle);

      controller1.playGame(model);
      controller2.playGame(model);
    } catch (NumberFormatException e) {
      System.err.println("Error: Rows and columns must be integers");
    } catch (IllegalArgumentException e) {
      System.err.println("Error: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Error starting game: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static PlayerActions createPlayer(String type, Player playerColor) {
    return switch (type.toLowerCase()) {
      case "human" -> new HumanPlayer();
      case "strategy1" -> new MachinePlayer(new FillFirst(), playerColor);
      case "strategy2" -> new MachinePlayer(new MaximizeRowScore(), playerColor);
      default -> throw new IllegalArgumentException(
          "Unknown player type: " + type + ". Use 'human', 'strategy1', or 'strategy2'");
    };
  }
}