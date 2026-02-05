package sanguine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import sanguine.controller.BasicSanguineController;
import sanguine.controller.SanguineController;
import sanguine.model.BasicInfluence;
import sanguine.model.BasicSanguineCard;
import sanguine.model.BasicSanguineModel;
import sanguine.model.BasicSanguinePawnCluster;
import sanguine.model.Influence;
import sanguine.model.PawnClusterSize;
import sanguine.model.Player;
import sanguine.model.SanguineCard;
import sanguine.model.SanguineElement;
import sanguine.model.SanguineModel;
import sanguine.model.SanguinePawnCluster;
import sanguine.player.HumanPlayer;
import sanguine.view.JFrameView;

/**
 * Tests methods of a SanguineModel.
 */
public class TestSanguineModel {
  SanguineController controller;

  public TestSanguineModel() throws Exception {
    controller =
        new BasicSanguineController(new JFrameView(
            new BasicSanguineModel(3, 5), Player.RED),
            new HumanPlayer(), Player.RED);
  }

  // DECK VALIDATION
  @Test
  public void testIsValidDeckAllCardsValidReturnsTrue() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    assertTrue(model.isValidDeck(deck));
  }

  @Test
  public void testIsValidDeckContainsNullCardReturnsFalse() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    deck.remove(0);
    deck.add(null);
    assertTrue(!model.isValidDeck(deck));
  }

  @Test
  public void testIsValidDeckTooFewCardsReturnsFalse() {
    SanguineModel model = new BasicSanguineModel(3, 7);
    List<SanguineCard> deck = controller.createDeck("docs\\initial_example.deck", Player.RED);
    assertTrue(!model.isValidDeck(deck));
  }

  @Test
  public void testIsValidDeckMoreThanTwoCopiesReturnsFalse() {
    Influence[][] securityGrid = {
        {null, null, null, null, null},
        {null, null, new BasicInfluence(1), null, null},
        {null, new BasicInfluence(1), null,
            new BasicInfluence(1), null},
        {null, null, new BasicInfluence(1), null, null},
        {null, null, null, null, null}
    };
    BasicSanguineCard security =
        new BasicSanguineCard(1, 1, "Security", securityGrid, Player.RED);

    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    deck.add(security);
    assertTrue(!model.isValidDeck(deck));
  }

  // GAME START
  @Test
  public void testStartGameValidInputsInitializesBoardAndHands() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);
    List<SanguineCard> correctHands = new ArrayList<>();
    Influence[][] securityGrid = {
        {null, null, null, null, null},
        {null, null, new BasicInfluence(1), null, null},
        {null, new BasicInfluence(1), null,
            new BasicInfluence(1), null},
        {null, null, new BasicInfluence(1), null, null},
        {null, null, null, null, null}
    };
    BasicSanguineCard security =
        new BasicSanguineCard(1, 1, "Security", securityGrid, Player.RED);
    correctHands.add(security);
    correctHands.add(security);

    Influence[][] queenGrid = {
        {null, null, new BasicInfluence(1), null, null},
        {null, null, null, null, null},
        {null, null, null, null, null},
        {null, null, null, null, null},
        {null, null, new BasicInfluence(1), null, null}
    };
    correctHands.add(new BasicSanguineCard(1, 1, "Queen", queenGrid, Player.RED));

    SanguineElement[] row =
        {new BasicSanguinePawnCluster(PawnClusterSize.ONE, Player.RED), null, null, null,
            new BasicSanguinePawnCluster(PawnClusterSize.ONE, Player.BLUE)};
    SanguineElement[][] correctBoard = {row, row, row};
    for (int i = 0; i < correctBoard.length; i++) {
      assertTrue(correctHands.get(i).equals(model.getHands(Player.RED).get(i)));
    }

    for (int i = 0; i < model.getBoard().length; i++) {
      for (int j = 0; j < model.getBoard()[0].length; j++) {
        assertTrue(model.getBoard()[i][j] == null && correctBoard[i][j] == null
            || model.getBoard()[i][j].equals(correctBoard[i][j]));
      }
    }
  }

  @Test
  public void testStartGameNegativeNumHandsThrowsIllegalArgumentException() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    assertThrows(IllegalArgumentException.class,
        () -> model.configureGame(-1, deck, deck, false));
  }

  @Test
  public void testStartGameNumHandsTooLargeThrowsIllegalArgumentException() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    assertThrows(IllegalArgumentException.class, () -> model.configureGame(100, deck, deck, false));
  }

  @Test
  public void testSumOfValsValidRowReturnsCorrectSum() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);
    model.placeCard(0, 0, 0);
    model.placeCard(0, 0, 1);
    assertEquals(2, model.sumOfVals(0, Player.RED));
  }

  @Test
  public void testIsGameOverInitialStateReturnsFalse() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);
    assertFalse(model.isGameOver());
  }

  @Test
  public void testIsGameOverAfterConsecutivePassesReturnsTrue() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);
    model.switchPlayer();
    model.switchPlayer();
    assertTrue(model.isGameOver());
  }

  @Test
  public void testGetWinnerRedHasHigherScoreReturns1() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);

    model.placeCard(0, 0, 0);
    model.switchPlayer();
    model.switchPlayer();
    assertEquals(Player.RED, model.getWinner());
  }

  @Test
  public void testGetWinnerBlueHasHigherScoreReturns0() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.BLUE);
    model.configureGame(3, deck, deck, false);

    model.switchPlayer();
    model.placeCard(0, 0, 4);
    model.switchPlayer();
    model.switchPlayer();
    assertEquals(Player.BLUE, model.getWinner());
  }

  @Test
  public void testGetWinnerTiedScoresReturnsMinus1() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    List<SanguineCard> deck2 = controller.createDeck("docs\\example.deck", Player.BLUE);

    model.configureGame(3, deck, deck2, false);
    model.placeCard(0, 0, 0);
    model.switchPlayer();
    model.placeCard(0, 0, 4);
    assertEquals(null, model.getWinner());
  }

  @Test
  public void testPlaceCardValidPositionPlacesCardOnBoard() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);
    Influence[][] securityGrid = {
        {null, null, null, null, null},
        {null, null, new BasicInfluence(1), null, null},
        {null, new BasicInfluence(1), null,
            new BasicInfluence(1), null},
        {null, null, new BasicInfluence(1), null, null},
        {null, null, null, null, null}
    };
    BasicSanguineCard security =
        new BasicSanguineCard(1, 1, "Security", securityGrid, Player.RED);

    model.placeCard(0, 0, 0);
    assertEquals(security, model.getBoard()[0][0]);
  }

  @Test
  public void testPlaceCardInvalidPositionThrowsException() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);
    assertThrows(IllegalArgumentException.class, () -> model.placeCard(2, -1, 0));
  }

  @Test
  public void testPlaceCardInvalidHandThrowsException() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);
    assertThrows(IllegalArgumentException.class, () -> model.placeCard(3, 0, 0));
  }

  @Test
  public void testBluePlayerCardsInfluenceBoardReflectsColumns() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\initial_example.deck", Player.RED);
    List<SanguineCard> deck2 = controller.createDeck("docs\\initial_example.deck", Player.BLUE);

    model.configureGame(3, deck, deck2, false);
    model.switchPlayer();
    Influence[][] securityGrid = {
        {new BasicInfluence(1), null, null, null, null},
        {null, null, null, null, null},
        {null, null, null, null, null},
        {null, null, null, null, null},
        {new BasicInfluence(1), null, null, null, null}
    };
    for (int row = 0; row < 3; row++) {
      for (int col = 0; col < 5; col++) {
        if (securityGrid[row][col] != null) {
          assertTrue(
              securityGrid[row][col].equals(
                  model.getDeck(Player.BLUE).get(2).getInfluenceGrid()[row][col]));
        }
      }
    }
  }

  @Test
  public void testDrawCardRedPlayerDrawsFromRedPileAndBlueDrawsFromBluePile() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);
    SanguineCard topRed = model.getDeck(Player.RED).get(0);
    model.drawCard();
    SanguineCard drawn = model.getHands(Player.RED).get(model.getHands(Player.RED).size() - 1);
    assertTrue(topRed.equals(drawn));
    model.switchPlayer();
    SanguineCard topBlue = model.getDeck(Player.BLUE).get(0);
    model.drawCard();
    SanguineCard drawn2 = model.getHands(Player.BLUE).get(model.getHands(Player.BLUE).size() - 1);
    assertEquals(topBlue, drawn2);
  }

  @Test
  public void testSwitchPlayerRedToBlueSwitchesCurrentPlayer() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);
    assertTrue(model.getCurrPlayer().equals(Player.RED));
    model.switchPlayer();
    assertFalse(model.getCurrPlayer().equals(Player.RED));
  }

  @Test
  public void testSwitchPlayerBlueToRedSwitchesCurrentPlayer() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);
    model.switchPlayer();
    assertFalse(model.getCurrPlayer().equals(Player.RED));
    model.switchPlayer();
    assertTrue(model.getCurrPlayer().equals(Player.RED));
  }

  @Test
  public void testIsCurrPlayerRedWhenRedTurnReturnsTrue() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);
    assertTrue(model.getCurrPlayer().equals(Player.RED));
  }

  @Test
  public void testIsCurrPlayerRedWhenBlueTurnReturnsFalse() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);
    model.switchPlayer();
    assertFalse(model.getCurrPlayer().equals(Player.RED));
  }

  // CHECK GETBOARD IS NOT MUTABLE
  @Test
  public void testGetBoardReturnsNewCopy() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);
    SanguineElement[][] copyBoard = model.getBoard();
    copyBoard[0][0] = new BasicSanguinePawnCluster(PawnClusterSize.TWO, Player.RED);
    assertTrue(!copyBoard[0][0].equals(model.getBoard()[0][0]));
  }

  @Test
  public void testPlaceCardInfluenceChangesPawnOwnershipToCardOwner() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    List<SanguineCard> deck2 = controller.createDeck("docs\\example.deck", Player.BLUE);
    model.configureGame(3, deck, deck2, false);
    SanguineElement[][] before = model.getBoard();
    model.placeCard(0, 0, 0);
    model.switchPlayer();
    model.placeCard(0, 0, 4);
    model.switchPlayer();
    model.placeCard(0, 0, 1);
    model.switchPlayer();
    model.placeCard(0, 0, 3);
    assertTrue(!model.getBoard()[0][2].getOwner().equals(Player.RED));
  }

  @Test
  public void testPlaceCardInfluenceAddsOnePawnToInfluencedClusters() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);
    SanguineElement[][] before = model.getBoard();
    model.placeCard(0, 0, 0);
    SanguineElement[][] after = model.getBoard();
    for (int i = 0; i < after.length; i++) {
      for (int j = 0; j < after[0].length; j++) {
        if (after[i][j] instanceof SanguinePawnCluster
            && before[i][j] instanceof SanguinePawnCluster) {
          int beforePawns = ((SanguinePawnCluster) before[i][j]).getNumPawns();
          int afterPawns = ((SanguinePawnCluster) after[i][j]).getNumPawns();
          if (afterPawns != beforePawns) {
            assertEquals(beforePawns + 1, afterPawns);
          }
        }
      }
    }
  }
}
