package sanguine;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.awt.Point;
import org.junit.Before;
import org.junit.Test;
import sanguine.controller.BasicSanguineController;
import sanguine.model.BasicInfluence;
import sanguine.model.BasicSanguineCard;
import sanguine.model.ModelListener;
import sanguine.model.Player;
import sanguine.model.SanguineCard;
import sanguine.player.HumanPlayer;

public class TestFeaturesListener {

  StringBuilder log;
  MockModelLogsMethods mockModel;
  MockViewLogsMethods mockView;
  BasicSanguineController controller;
  SanguineCard card;

  @Before
  public void setup() {
    log = new StringBuilder();
    mockModel = new MockModelLogsMethods(log);
    mockView = new MockViewLogsMethods(log);
    controller = new BasicSanguineController(mockView, new HumanPlayer(), Player.RED);
    card = new BasicSanguineCard(1, 1, "TestCard",
        new BasicInfluence[5][5], Player.RED);

    controller.playGame(mockModel);
    log.setLength(0);
  }

  @Test
  public void testOnBoardClickWithSelection() {
    controller.onBoardClick(1, 1);

    String out = log.toString();
    assertTrue(out.contains("refreshBoard(" + mockModel + ", java.awt.Point[x=1,y=1])\n"));
    assertTrue(out.contains("refreshDeck(" + mockModel + ", -1)\n"));
  }

  @Test
  public void testOnCardClickWithSelection() {
    controller.onCardClick(2, card);

    String out = log.toString();

    assertTrue(out.contains("refreshBoard(" + mockModel + ", java.awt.Point[x=0,y=0])\n"));
    assertTrue(out.contains("refreshDeck(" + mockModel + ", 2)\n"));

  }

  @Test
  public void testOnConfirmSelection() {
    controller.onCardClick(1, card);
    controller.onBoardClick(2, 1);
    controller.onConfirm();

    String expected =
        "getCurrPlayer()\n"
            + "refreshBoard(" + mockModel + ", java.awt.Point[x=0,y=0])\n"
            + "refreshDeck(" + mockModel + ", 1)\n"
            + "getNumColumns()\n"
            + "getCurrPlayer()\n"
            + "refreshBoard(" + mockModel + ", java.awt.Point[x=2,y=1])\n"
            + "refreshDeck(" + mockModel + ", 1)\n"
            + "getCurrPlayer()\n"
            + "placeCard(1, 2, 0)\n"
            + "refreshBoard(" + mockModel + ", null)\n"
            + "refreshDeck(" + mockModel + ", -1)\n"
            + "switchPlayer()\n";

    assertEquals(expected, log.toString());
  }

  @Test
  public void testOnPass() {
    controller.onCardClick(1, card);
    controller.onBoardClick(2, 3);

    log.setLength(0);
    controller.onPass();

    assertEquals("getCurrPlayer()\n"
        + "switchPlayer()\n", log.toString());
  }

  @Test
  public void testOnTurnSwitchInteractions() {
    log.setLength(0);

    ((ModelListener) controller).onTurnSwitch(Player.RED);

    String expected =
        "getCurrPlayer()\n"
            + "drawCard()\n"
            + "refreshBoard(" + mockModel + ", java.awt.Point[x=0,y=0])\n"
            + "refreshDeck(" + mockModel + ", -1)\n" + "isGameOver()\n";

    assertEquals(expected, log.toString());
  }

  @Test
  public void testOnGameOverTie() {
    log.setLength(0);

    ((ModelListener) controller).onGameOver();

    assertEquals(

        "getWinner()\nshowMessage(Game over. Result: TIE, Game Over)\n",
        log.toString()
    );
  }
}
