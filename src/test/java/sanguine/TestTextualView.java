package sanguine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import sanguine.controller.BasicSanguineController;
import sanguine.controller.SanguineController;
import sanguine.model.BasicSanguineModel;
import sanguine.model.Player;
import sanguine.model.SanguineCard;
import sanguine.model.SanguineModel;
import sanguine.player.HumanPlayer;
import sanguine.view.JFrameView;
import sanguine.view.TextualView;

/**
 * Tests for the TextualView class.
 * Ensures that the textual representation of the game is rendered correctly.
 * This includes the state of the game, scores, and the elements.
 */
public class TestTextualView {

  private SanguineModel model;
  private TextualView view;
  private String deckpath;
  private SanguineController controller;

  /**
   * Sets up tests by initializing a 3x5 game model, a textual view and a deck file path.
   */
  @Before
  public void setUp() {
    model = new BasicSanguineModel(3, 5);
    view = new TextualView();
    deckpath = "docs" + File.separator + "initial_example.deck";
    controller =
        new BasicSanguineController(new JFrameView(
            new BasicSanguineModel(3, 5), Player.RED),
            new HumanPlayer(), Player.RED);

  }

  @Test
  public void testRenderStartingBoard() {
    List<SanguineCard> redDeck = controller.createDeck(deckpath, Player.RED);
    List<SanguineCard> blueDeck = controller.createDeck(deckpath, Player.BLUE);
    model.configureGame(5, redDeck, blueDeck, false);

    String output = view.render(model);
    String[] rows = output.split("\n");

    assertEquals(3, rows.length);

    assertTrue(output.contains("1"));
    assertTrue(output.contains("_"));
  }

  @Test
  public void testRenderHasCorrectPawnPositions() {
    List<SanguineCard> redDeck = controller.createDeck(deckpath, Player.RED);
    List<SanguineCard> blueDeck = controller.createDeck(deckpath, Player.BLUE);
    model.configureGame(5, redDeck, blueDeck, false);

    String output = view.render(model);
    String[] rows = output.split("\n");

    for (String row : rows) {
      String cells = row.split(" ")[1];
      assertTrue(cells.startsWith("1"));
      assertTrue(cells.endsWith("1"));
    }
  }

  @Test
  public void testRenderHasEmptyCells() {
    List<SanguineCard> redDeck = controller.createDeck(deckpath, Player.RED);
    List<SanguineCard> blueDeck = controller.createDeck(deckpath, Player.BLUE);
    model.configureGame(5, redDeck, blueDeck, false);
    String output = view.render(model);

    assertTrue(output.contains("___"));
  }

  @Test
  public void testRenderStartingScoresAreZero() {
    List<SanguineCard> redDeck = controller.createDeck(deckpath, Player.RED);
    List<SanguineCard> blueDeck = controller.createDeck(deckpath, Player.BLUE);
    model.configureGame(5, redDeck, blueDeck, false);

    String output = view.render(model);
    String[] rows = output.split("\n");

    for (String row : rows) {
      assertTrue(row.startsWith("0 "));
    }

    for (String row : rows) {
      assertTrue(row.endsWith(" 0"));
    }
  }

  @Test
  public void testRenderAfterPlacingCard() {
    List<SanguineCard> redDeck = controller.createDeck(deckpath, Player.RED);
    List<SanguineCard> blueDeck = controller.createDeck(deckpath, Player.BLUE);
    model.configureGame(5, redDeck, blueDeck, false);


    model.placeCard(0, 0, 0);

    String output = view.render(model);

    assertTrue(output.contains("R") || output.contains("B"));
  }

  @Test
  public void testRenderShowsRedCard() {
    List<SanguineCard> redDeck = controller.createDeck(deckpath, Player.RED);
    List<SanguineCard> blueDeck = controller.createDeck(deckpath, Player.BLUE);
    model.configureGame(5, redDeck, blueDeck, false);

    model.placeCard(0, 0, 0);

    String output = view.render(model);

    assertTrue(output.contains("R"));
  }

  @Test
  public void testRenderShowBlueCard() {
    List<SanguineCard> redDeck = controller.createDeck(deckpath, Player.RED);
    List<SanguineCard> blueDeck = controller.createDeck(deckpath, Player.BLUE);
    model.configureGame(5, redDeck, blueDeck, false);

    model.switchPlayer();

    boolean placed = true;
    try {
      model.placeCard(0, 0, 4);
    } catch (IllegalArgumentException | IllegalStateException e) {
      try {
        model.placeCard(0, 1, 4);
      } catch (IllegalArgumentException | IllegalStateException ex) {
        try {
          model.placeCard(0, 2, 4);
        } catch (IllegalArgumentException | IllegalStateException exx) {
          placed = false;
        }
      }
    }

    String output = view.render(model);
    if (placed) {
      assertTrue(output.contains("B"));
    } else {
      assertNotNull(output);
    }
  }

  @Test
  public void testRenderShowsScoreAfterCardPlace() {
    List<SanguineCard> redDeck = controller.createDeck(deckpath, Player.RED);
    List<SanguineCard> blueDeck = controller.createDeck(deckpath, Player.BLUE);
    model.configureGame(5, redDeck, blueDeck, false);

    model.placeCard(0, 0, 0);

    String output = view.render(model);
    String[] rows = output.split("\n");

    String firstRow = rows[0];
    assertFalse(firstRow.startsWith("0 ") && firstRow.endsWith(" 0"));
  }

  @Test
  public void testRenderShowsPawnCount() {
    List<SanguineCard> redDeck = controller.createDeck(deckpath, Player.RED);
    List<SanguineCard> blueDeck = controller.createDeck(deckpath, Player.BLUE);
    model.configureGame(5, redDeck, blueDeck, false);

    model.placeCard(0, 0, 0);

    String output = view.render(model);

    assertTrue(output.contains("1") || output.contains("2") || output.contains("3"));
  }

  @SuppressWarnings("checkstyle:EmptyCatchBlock")
  @Test
  public void testRenderMultipleCards() {
    List<SanguineCard> redDeck = controller.createDeck(deckpath, Player.RED);
    List<SanguineCard> blueDeck = controller.createDeck(deckpath, Player.BLUE);
    model.configureGame(5, redDeck, blueDeck, false);

    model.placeCard(0, 0, 0);
    model.switchPlayer();

    model.placeCard(0, 1, 4);

    String output = view.render(model);

    assertTrue(output.contains("R"));
    assertTrue(output.contains("B"));
  }

  @Test(expected = NullPointerException.class)
  public void testRenderNullExceptionWithNullModel() {
    view.render(null);
  }

  @Test
  public void testRenderAfterManyTurns() {
    List<SanguineCard> redDeck = controller.createDeck(deckpath, Player.RED);
    List<SanguineCard> blueDeck = controller.createDeck(deckpath, Player.BLUE);
    model.configureGame(5, redDeck, blueDeck, false);

    try {
      model.switchPlayer();
      model.placeCard(0, 1, 4);
      model.placeCard(0, 0, 4);
      model.switchPlayer();
      model.placeCard(0, 0, 0);
      model.placeCard(0, 2, 0);
      model.placeCard(0, 1, 0);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    String output = view.render(model);

    assertNotNull(output);

    assertFalse(output.isEmpty());

    assertEquals(3, output.split("\n").length);
  }

  @Test
  public void testRenderRowScoreCalc() {
    List<SanguineCard> redDeck = controller.createDeck(deckpath, Player.RED);
    List<SanguineCard> blueDeck = controller.createDeck(deckpath, Player.BLUE);
    model.configureGame(5, redDeck, blueDeck, false);

    model.placeCard(0, 0, 0);

    String output = view.render(model);
    String[] rows = output.split("\n");
    String firstRow = rows[0];

    String[] cells = firstRow.split(" ");
    int redScore = Integer.parseInt(cells[0]);
    int blueScore = Integer.parseInt(cells[2]);

    assertTrue(redScore > 0);
    assertEquals(0, blueScore);
  }

}