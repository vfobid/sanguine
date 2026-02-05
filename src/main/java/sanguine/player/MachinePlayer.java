package sanguine.player;

import java.util.ArrayList;
import java.util.List;
import sanguine.model.Player;
import sanguine.model.ReadOnlySanguineModel;
import sanguine.model.SanguineCard;
import sanguine.strategy.Move;
import sanguine.strategy.SanguineStrategy;
import sanguine.view.FeaturesListener;

/**
 * Represents a machine player in the game.
 * Uses a strategy to compute and execute moves when it is notified that it is its turn.
 */
public class MachinePlayer implements PlayerActions {

  private final SanguineStrategy strategy;
  private final Player playerColor;
  private final List<FeaturesListener> listeners;

  /**
   * Creates a MachinePlayer with the given strategy and color.
   *
   * @param strategy the strategy to choose moves.
   * @param playerColor the color of this player.
   * @throws IllegalArgumentException if strategy or playerColor is null.
   */
  public MachinePlayer(SanguineStrategy strategy, Player playerColor) {
    if (strategy == null || playerColor == null) {
      throw new IllegalArgumentException("Strategy and player color cannot be null");
    }
    this.strategy = strategy;
    this.playerColor = playerColor;
    this.listeners = new ArrayList<>();
  }

  @Override
  public void subscribe(FeaturesListener listener) {
    if (listener != null) {
      this.listeners.add(listener);
    }
  }

  @Override
  public void notifyTurn(ReadOnlySanguineModel model) {
    List<Move> moves = strategy.chooseMoves(model, playerColor);

    if (!moves.isEmpty()) {
      Move move = moves.getFirst();
      List<SanguineCard> hand = model.getHands(playerColor);

      if (move.getCardInd() >= hand.size()) {
        for (FeaturesListener listener : listeners) {
          listener.onPass();
        }
        return;
      }

      SanguineCard card = hand.get(move.getCardInd());

      for (FeaturesListener listener : listeners) {
        listener.onCardClick(move.getCardInd(), card);
        listener.onBoardClick(move.getRow(), move.getCol() + 1);
        listener.onConfirm();
      }
    } else {
      for (FeaturesListener listener : listeners) {
        listener.onPass();
      }
    }
  }
}
