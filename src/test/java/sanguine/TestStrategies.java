package sanguine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import sanguine.controller.BasicSanguineController;
import sanguine.controller.SanguineController;
import sanguine.model.BasicSanguineCard;
import sanguine.model.BasicSanguineModel;
import sanguine.model.Influence;
import sanguine.model.Player;
import sanguine.model.SanguineCard;
import sanguine.model.SanguineModel;
import sanguine.player.HumanPlayer;
import sanguine.strategy.FillFirst;
import sanguine.strategy.MaximizeRowScore;
import sanguine.strategy.Move;
import sanguine.strategy.SanguineStrategy;
import sanguine.view.JFrameView;

/**
 * Tests the Sanguine strategies.
 */
public class TestStrategies {
  private Influence[][] emptyInfluence;
  private SanguineController controller;

  /**
   * Sets up tests by creating an empty influence grid.
   */
  @Before
  public void setUp() {
    emptyInfluence = new Influence[5][5];
    controller =
        new BasicSanguineController(new JFrameView(new BasicSanguineModel(3, 5), Player.RED),
            new HumanPlayer(), Player.RED);

  }

  @Test
  public void testFillFirstChoosesFirstValidMove() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(3, deck, deck, false);

    SanguineStrategy strategy = new FillFirst();
    List<Move> moves = strategy.chooseMoves(model, Player.RED);

    assertEquals(1, moves.size());
    assertEquals(0, moves.getFirst().getCardInd());
    assertEquals(0, moves.getFirst().getRow());
    assertEquals(0, moves.getFirst().getCol());
  }

  @Test
  public void testFillFirstSkipsBlockedSpots() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.deck", Player.RED);
    model.configureGame(5, deck, deck, false);

    model.placeCard(0, 0, 0);
    model.switchPlayer();
    model.drawCard();

    SanguineStrategy strategy = new FillFirst();
    List<Move> moves = strategy.chooseMoves(model, Player.BLUE);

    assertEquals(1, moves.size());
    assertFalse(moves.getFirst().getRow() == 0 && moves.getFirst().getCol() == 0);
  }

  @Test
  public void testMaximizeRowScoreWinsLosingRow() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> redDeck = controller.createDeck("docs\\example.deck", Player.RED);
    List<SanguineCard> blueDeck = controller.createDeck("docs\\example.deck", Player.BLUE);
    model.configureGame(3, redDeck, blueDeck, false);

    model.switchPlayer();
    model.placeCard(0, 0, 4);

    model.switchPlayer();
    SanguineStrategy strategy = new MaximizeRowScore();
    List<Move> moves = strategy.chooseMoves(model, Player.RED);

    assertEquals(1, moves.size());
    assertTrue(moves.getFirst().getRow() >= 0);
    assertTrue(moves.getFirst().getRow() < 3);
  }

  @Test
  public void testStrategiesProduceDifferentMoves() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> redDeck = controller.createDeck("docs\\example.deck", Player.RED);
    List<SanguineCard> blueDeck = controller.createDeck("docs\\example.deck", Player.BLUE);
    model.configureGame(3, redDeck, blueDeck, false);

    model.switchPlayer();
    model.placeCard(2, 2, 4);
    model.switchPlayer();

    SanguineStrategy fillFirst = new FillFirst();
    SanguineStrategy maxRow = new MaximizeRowScore();

    List<Move> fillFirstMoves = fillFirst.chooseMoves(model, Player.RED);
    List<Move> maxRowMoves = maxRow.chooseMoves(model, Player.RED);

    assertFalse(fillFirstMoves.isEmpty());
    assertFalse(maxRowMoves.isEmpty());
  }

  @Test
  public void testFillFirstCallsGetHands() {
    Appendable log = new StringBuilder();
    MockSanguineModel model = new MockSanguineModel(log, new ArrayList<>(), 0, 0);
    SanguineStrategy strategy = new FillFirst();

    strategy.chooseMoves(model, Player.RED);

    try (FileWriter writer = new FileWriter("strategy-transcript-first.txt")) {
      writer.write(log.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }

    assertTrue(log.toString().contains("getHands RED"));
  }

  @Test
  public void testFillFirstCallsGetBoard() {
    Appendable log = new StringBuilder();

    SanguineCard card = new BasicSanguineCard(1, 1, "Card", emptyInfluence, Player.RED);
    List<SanguineCard> hand = new ArrayList<>();
    hand.add(card);

    MockSanguineModel model = new MockSanguineModel(log, hand, 3, 5);
    SanguineStrategy strategy = new FillFirst();

    strategy.chooseMoves(model, Player.RED);

    assertTrue(log.toString().contains("getBoard"));
  }

  @Test
  public void testFillFirstCallsGetNumRows() {
    Appendable log = new StringBuilder();
    MockSanguineModel model = new MockSanguineModel(log, new ArrayList<>(), 0, 0);
    SanguineStrategy strategy = new FillFirst();

    strategy.chooseMoves(model, Player.RED);

    assertTrue(log.toString().contains("getNumRows"));
  }

  @Test
  public void testFillFirstCallGetNumColumns() {
    Appendable log = new StringBuilder();
    MockSanguineModel model = new MockSanguineModel(log, new ArrayList<>(), 0, 0);
    SanguineStrategy strategy = new FillFirst();

    strategy.chooseMoves(model, Player.RED);

    assertTrue(log.toString().contains("getNumColumns"));
  }

  @Test
  public void testMaximizeRowScoreCallsGetHands() {
    Appendable log = new StringBuilder();
    MockSanguineModel model = new MockSanguineModel(log, new ArrayList<>(), 0, 0);
    SanguineStrategy strategy = new MaximizeRowScore();

    strategy.chooseMoves(model, Player.RED);

    try (FileWriter writer = new FileWriter("strategy-transcript-score.txt")) {
      writer.write(log.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }

    assertTrue(log.toString().contains("getHands RED"));
  }

  @Test
  public void testMaximizeRowScoreCalsSumOfVals() {
    Appendable log = new StringBuilder();
    MockSanguineModel model = new MockSanguineModel(log, new ArrayList<>(), 3, 5);
    SanguineStrategy strategy = new MaximizeRowScore();

    strategy.chooseMoves(model, Player.RED);

    assertTrue(log.toString().contains("sumOfVals"));
  }

  @Test
  public void testMaximizeRowScoreCallsGetNumRows() {
    Appendable log = new StringBuilder();
    MockSanguineModel model = new MockSanguineModel(log, new ArrayList<>(), 0, 0);
    SanguineStrategy strategy = new MaximizeRowScore();

    strategy.chooseMoves(model, Player.RED);

    assertTrue(log.toString().contains("getNumRows"));
  }

  @Test
  public void testMaximizeRowScoreChecksBothPlayers() {
    Appendable log = new StringBuilder();
    MockSanguineModel model = new MockSanguineModel(log, new ArrayList<>(), 3, 5);
    SanguineStrategy strategy = new MaximizeRowScore();

    strategy.chooseMoves(model, Player.RED);
    String output = log.toString();

    assertTrue(output.contains("RED"));
    assertTrue(output.contains("BLUE"));
  }

  @Test
  public void testMaximizeRowScoreForcedRowOne() {
    Appendable log = new StringBuilder();

    SanguineCard card1 = new BasicSanguineCard(2, 1, "Card1", emptyInfluence, Player.RED);
    SanguineCard card2 = new BasicSanguineCard(3, 1, "Card2", emptyInfluence, Player.RED);
    List<SanguineCard> hand = new ArrayList<>();
    hand.add(card1);
    hand.add(card2);

    MockSanguineModel model = new MockSanguineModel(log, hand, 3, 5);

    model.setRowScore(0, Player.RED, 0);
    model.setRowScore(0, Player.BLUE, 10);
    model.setRowScore(1, Player.RED, 0);
    model.setRowScore(1, Player.BLUE, 2);
    model.setRowScore(2, Player.RED, 0);
    model.setRowScore(2, Player.BLUE, 10);

    model.setMove(1, 1, 2, true);

    SanguineStrategy strategy = new MaximizeRowScore();
    List<Move> moves = strategy.chooseMoves(model, Player.RED);

    assertFalse(moves.isEmpty());
    assertEquals(1, moves.getFirst().getCardInd());
    assertEquals(1, moves.getFirst().getRow());
  }

  @Test
  public void testFillFirstForcedSpecificPosition() {
    Appendable log = new StringBuilder();

    SanguineCard card = new BasicSanguineCard(1, 0, "Card", emptyInfluence, Player.RED);
    List<SanguineCard> hand = new ArrayList<>();
    hand.add(card);

    MockSanguineModel model = new MockSanguineModel(log, hand, 2, 3);

    for (int row = 0; row < 2; row++) {
      for (int col = 0; col < 3; col++) {
        model.setMove(0, row, col, true);
      }
    }

    SanguineStrategy strategy = new FillFirst();
    List<Move> moves = strategy.chooseMoves(model, Player.RED);

    assertFalse(moves.isEmpty());
    assertEquals(0, moves.getFirst().getCardInd());
    assertEquals(0, moves.getFirst().getRow());
    assertEquals(0, moves.getFirst().getCol());
  }

  @Test
  public void testMaximizeRowScoreSkipsUnwinnableRows() {
    Appendable log = new StringBuilder();

    SanguineCard card = new BasicSanguineCard(1, 1, "Card", emptyInfluence, Player.RED);
    List<SanguineCard> hand = new ArrayList<>();
    hand.add(card);

    MockSanguineModel model = new MockSanguineModel(log, hand, 3, 5);

    model.setRowScore(0, Player.RED, 0);
    model.setRowScore(0, Player.BLUE, 10);
    model.setRowScore(1, Player.RED, 0);
    model.setRowScore(1, Player.BLUE, 10);
    model.setRowScore(2, Player.RED, 0);
    model.setRowScore(2, Player.BLUE, 10);

    SanguineStrategy strategy = new MaximizeRowScore();
    List<Move> moves = strategy.chooseMoves(model, Player.RED);

    assertTrue(moves.isEmpty());
  }

  @Test
  public void testMaximizeRowScoreChecksRowOneBeforeRowTwo() {
    Appendable log = new StringBuilder();

    SanguineCard card = new BasicSanguineCard(5, 1, "Card", emptyInfluence, Player.RED);
    List<SanguineCard> hand = new ArrayList<>();
    hand.add(card);

    MockSanguineModel model = new MockSanguineModel(log, hand, 3, 5);

    model.setRowScore(0, Player.RED, 5);
    model.setRowScore(0, Player.BLUE, 1);
    model.setRowScore(1, Player.RED, 0);
    model.setRowScore(1, Player.BLUE, 2);
    model.setRowScore(2, Player.RED, 0);
    model.setRowScore(2, Player.BLUE, 2);

    model.setMove(0, 1, 0, true);
    model.setMove(0, 2, 0, true);

    SanguineStrategy strategy = new MaximizeRowScore();
    List<Move> moves = strategy.chooseMoves(model, Player.RED);

    assertFalse(moves.isEmpty());
    assertEquals(1, moves.getFirst().getRow());
  }
}
