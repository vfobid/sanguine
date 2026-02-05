package sanguine.strategy;

import java.util.List;
import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;

/**
 * This interface represents a strategy for choosing moves in the game.
 * It must analyze the state of the game and suggest the best move for a player.
 */
public interface SanguineStrategy {

  /**
   * Chooses the best move for the given player based on the strategy.
   * If multiple moves are just as good, all of them are given.
   *
   * @param model  the state of the game which is read only.
   * @param player the player to choose a move for.
   * @return a list of equally good moves which is empty if there are no valid moves.
   */
  List<Move> chooseMoves(ReadOnlySanguineModel model, Player player);
}
