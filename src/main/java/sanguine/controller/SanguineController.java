package sanguine.controller;

import java.util.List;
import sanguine.model.Player;
import sanguine.model.SanguineCard;
import sanguine.model.SanguineModel;

/**
 * Stub controller for the Sanguine view. Has the necessary methods for starting up the view,
 * and the gamePlay methods will be implemented later.
 */
public interface SanguineController {

  /**
   * Creates a deck for the desired player using input from a file.
   *
   * @param filePath the String path to the deck of cards for the player.
   * @param owner    the owner of the cards to be made.
   * @return a List of all the cards in the deck for the player.
   */
  List<SanguineCard> createDeck(String filePath, Player owner);


  /**
   * Sets up a game of Sanguine using the given inputs. Subscribes to the view and model, and makes
   * the view visible for the user to see.
   *
   * @param model        the SanguineModel being used for gameplay.
   */
  void playGame(SanguineModel model);

  /**
   * Refreshes the publishers this controller is subscribed to. Used after mutating the model.
   */
  void refreshAll();

}
