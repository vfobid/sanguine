package sanguine;

import org.junit.Before;
import org.junit.Test;

import sanguine.MockModelLogsMethods;
import sanguine.MockViewLogsMethods;
import sanguine.controller.BasicSanguineController;
import sanguine.controller.SanguineController;
import sanguine.model.BasicInfluence;
import sanguine.model.BasicSanguineCard;
import sanguine.model.BasicSanguineModel;
import sanguine.model.Influence;
import sanguine.model.Player;
import sanguine.model.SanguineCard;
import sanguine.model.SanguineModel;
import sanguine.player.HumanPlayer;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestBasicSanguineController {

  StringBuilder log;
  MockModelLogsMethods mockModel;
  MockViewLogsMethods mockView;
  SanguineController controller;

  /**
   * Sets up the tests with a log, mock model, mock view, and controller.
   */
  @Before
  public void setup() {
    log = new StringBuilder();
    mockModel = new MockModelLogsMethods(log);
    mockView = new MockViewLogsMethods(log);
    controller = new BasicSanguineController(mockView, new HumanPlayer(), Player.RED);
  }


  //PRIVATE HELPERS
  private BasicSanguineCard makeSecurity(Player owner) {
    Influence[][] g = {
        {null, null, null, null, null},
        {null, null, new BasicInfluence(1), null, null},
        {null, new BasicInfluence(1), null, new BasicInfluence(1), null},
        {null, null, new BasicInfluence(1), null, null},
        {null, null, null, null, null}
    };
    return new BasicSanguineCard(1, 1, "Security", g, owner);
  }

  private BasicSanguineCard makeQueen(Player owner) {
    Influence[][] g = {
        {null, null, new BasicInfluence(1), null, null},
        {null, null, null, null, null},
        {null, null, null, null, null},
        {null, null, null, null, null},
        {null, null, new BasicInfluence(1), null, null}
    };
    return new BasicSanguineCard(1, 1, "Queen", g, owner);
  }

  private BasicSanguineCard makeCrab(Player owner) {
    Influence[][] g = {
        {null, null, null, null, null},
        {null, null, new BasicInfluence(1), null, null},
        {null, new BasicInfluence(1), null, new BasicInfluence(1), null},
        {null, null, null, null, null},
        {null, null, null, null, null}
    };
    return new BasicSanguineCard(1, 1, "Crab", g, owner);
  }

  private BasicSanguineCard makeWheel(Player owner) {
    Influence[][] g = {
        {null, null, null, null, new BasicInfluence(1)},
        {null, null, null, null, null},
        {null, null, null, null, null},
        {null, null, null, null, null},
        {null, null, null, null, new BasicInfluence(1)}
    };
    return new BasicSanguineCard(1, 1, "Wheel", g, owner);
  }

  private BasicSanguineCard makeFlame(Player owner) {
    Influence[][] g = {
        {null, null, null, null, null},
        {null, new BasicInfluence(1), new BasicInfluence(1), null, null},
        {null, new BasicInfluence(1), null, null, null},
        {null, new BasicInfluence(1), new BasicInfluence(1), null, null},
        {null, null, null, null, null}
    };
    return new BasicSanguineCard(3, 1, "Flame", g, owner);
  }

  private BasicSanguineCard makeChocomog(Player owner) {
    Influence[][] g = {
        {null, null, null, null, null},
        {null, null, new BasicInfluence(1), null, null},
        {null, null, null, new BasicInfluence(1), null},
        {null, null, new BasicInfluence(1), null, null},
        {null, null, null, null, null}
    };
    return new BasicSanguineCard(1, 1, "Chocomog", g, owner);
  }

  private BasicSanguineCard makeGrenade(Player owner) {
    Influence[][] g = {
        {null, null, null, null, null},
        {null, null, null, null, null},
        {null, null, null, null, new BasicInfluence(1)},
        {null, null, null, null, null},
        {null, null, null, null, null}
    };
    return new BasicSanguineCard(1, 2, "Grenade", g, owner);
  }

  private BasicSanguineCard makeSweeper(Player owner) {
    Influence[][] g = {
        {null, null, null, null, null},
        {null, null, new BasicInfluence(1), new BasicInfluence(1), null},
        {null, null, null, null, null},
        {null, null, new BasicInfluence(1), new BasicInfluence(1), null},
        {null, null, null, null, null}
    };
    return new BasicSanguineCard(2, 2, "Sweeper", g, owner);
  }

  private BasicSanguineCard makeQuetz(Player owner) {
    Influence[][] g = {
        {null, null, new BasicInfluence(1), null, null},
        {null, null, null, new BasicInfluence(1), null},
        {null, null, null, null, null},
        {null, null, null, new BasicInfluence(1), null},
        {null, null, new BasicInfluence(1), null, null}
    };
    return new BasicSanguineCard(3, 2, "Quetz", g, owner);
  }

  private BasicSanguineCard makeBig(Player owner) {
    Influence[][] g = {
        {null, null, new BasicInfluence(1), null, null},
        {null, new BasicInfluence(1), null, new BasicInfluence(1), null},
        {new BasicInfluence(1), null, null, null, new BasicInfluence(1)},
        {null, new BasicInfluence(1), null, new BasicInfluence(1), null},
        {null, null, new BasicInfluence(1), null, null}
    };
    return new BasicSanguineCard(5, 3, "Big", g, owner);
  }

  private BasicSanguineCard makeRider(Player owner) {
    Influence[][] g = {
        {null, null, null, null, null},
        {null, null, null, null, null},
        {null, null, null, new BasicInfluence(1), null},
        {null, new BasicInfluence(1), new BasicInfluence(1), new BasicInfluence(1), null},
        {null, null, null, null, null}
    };
    return new BasicSanguineCard(5, 3, "Rider", g, owner);
  }

  @Test
  public void testCreateRedDeckAndBlueDeckValidFilePathReturnsDeck() {
    List<SanguineCard> expected = new ArrayList<>();

    expected.add(makeSecurity(Player.RED));
    expected.add(makeSecurity(Player.RED));
    expected.add(makeQueen(Player.RED));
    expected.add(makeCrab(Player.RED));
    expected.add(makeCrab(Player.RED));
    expected.add(makeWheel(Player.RED));
    expected.add(makeFlame(Player.RED));
    expected.add(makeFlame(Player.RED));
    expected.add(makeChocomog(Player.RED));
    expected.add(makeGrenade(Player.RED));
    expected.add(makeSweeper(Player.RED));
    expected.add(makeSweeper(Player.RED));
    expected.add(makeQuetz(Player.RED));
    expected.add(makeBig(Player.RED));
    expected.add(makeRider(Player.RED));

    List<SanguineCard> actual =
        controller.createDeck("docs/initial_example.deck", Player.RED);

    for (int i = 0; i < actual.size(); i++) {
      assertTrue(expected.get(i).equals(actual.get(i)));
    }
  }


  @Test
  public void testPlayGame() {
    controller.playGame(mockModel);

    String expectedLog =
        "subscribe(" + controller + ")\n"
            + "subscribe(" + controller + ")\n"
            + "drawCard()\n"
            + "refreshBoard(" + mockModel + ", java.awt.Point[x=0,y=0])\n"
            + "refreshDeck(" + mockModel + ", -1)\n"
            + "makeVisible()\n";

    assertEquals(expectedLog, log.toString());
  }

  @Test
  public void testRefreshAll() {
    controller.playGame(mockModel);
    controller.refreshAll();

    String expectedLog =
        "subscribe(" + controller + ")\n"
            + "subscribe(" + controller + ")\n"
            + "drawCard()\n"
            + "refreshBoard(" + mockModel + ", java.awt.Point[x=0,y=0])\n"
            + "refreshDeck(" + mockModel + ", -1)\n"
            + "makeVisible()\n"
            + "refreshBoard(" + mockModel + ", java.awt.Point[x=0,y=0])\n"
            + "refreshDeck(" + mockModel + ", -1)\n";

    assertEquals(expectedLog, log.toString());
  }

  @Test
  public void testCreateRedDeckAndBlueDeckInvalidFilePathEndsRunAndDoesntThrowException() {
    SanguineModel model = new BasicSanguineModel(3, 5);
    List<SanguineCard> deck = controller.createDeck("docs\\example.decks", Player.RED);
    List<SanguineCard> deck2 = controller.createDeck("docs\\example.decks", Player.BLUE);
  }
}
