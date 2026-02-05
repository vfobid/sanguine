package sanguine.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the Sanguine model interface. Includes methods for Sanguine gameplay.
 */
public class BasicSanguineModel implements SanguineModel {

  private List<SanguineCard> redDeck;
  private List<SanguineCard> blueDeck;
  //INVARIANT: the number of rows of the board is always positive and the number of columns odd.
  private final SanguineElement[][] board;
  private List<SanguineCard> redHand;
  private List<SanguineCard> blueHand;
  private Player currPlayer;
  //The board a turn before the current.
  private SanguineElement[][] prevBoard;
  //The number of times the board has remained the same while the turn has changed
  private int noChangeCount;
  private final int numRows;
  private final int numCols;
  private int numHands;
  private final List<ModelListener> listeners;

  /**
   * Constructs a BasicSanguineModel object. the
   *
   * @param numRows the number of rows the board will have.
   * @param numCols the number of columns the board will have.
   */
  public BasicSanguineModel(int numRows, int numCols) {
    //INVARIANT: the number of rows of the board is always positive and the number of columns odd.
    if (numRows <= 0) {
      throw new IllegalArgumentException("Number of rows must be greater than 0");
    }
    if (numCols <= 0 || numCols % 2 == 0) {
      throw new IllegalArgumentException("Number of columns must be greater than 0 and odd.");
    }
    this.numRows = numRows;
    this.numCols = numCols;
    this.board = new SanguineElement[numRows][numCols];
    for (int i = 0; i < numRows; i++) {
      board[i][0] = new BasicSanguinePawnCluster(PawnClusterSize.ONE, Player.RED);
      board[i][numCols - 1] = new BasicSanguinePawnCluster(PawnClusterSize.ONE, Player.BLUE);
    }
    prevBoard = getBoard();
    this.listeners = new ArrayList<>();
  }

  @Override
  public boolean isValidDeck(List<SanguineCard> deck) {

    if (deck.size() < getNumColumns() * getNumRows()) {
      return false;
    }
    int copyCount = 0;
    for (int comparingCardIndex = 0; comparingCardIndex < deck.size(); comparingCardIndex++) {
      for (SanguineCard sanguineCard : deck) {
        if (deck.get(comparingCardIndex) == null || sanguineCard == null) {
          return false;
        }
        if (deck.get(comparingCardIndex).equals(sanguineCard)) {
          copyCount++;
        }
        if (copyCount > 2) {
          return false;
        }
      }
      copyCount = 0;
    }
    return true;
  }

  @Override
  public void configureGame(int numHands, List<SanguineCard> redDeck, List<SanguineCard> blueDeck,
                            boolean shuffle)
      throws IllegalArgumentException {
    this.numHands = numHands;
    this.currPlayer = Player.RED;
    this.redDeck = new ArrayList<>(redDeck);
    this.blueDeck = new ArrayList<>(blueDeck);

    if (shuffle) {
      Collections.shuffle(this.redDeck);
      Collections.shuffle(this.blueDeck);
    }

    if (!(isValidDeck(redDeck)
        && isValidDeck(blueDeck)
        && 3 * numHands <= redDeck.size() && 3 * numHands <= blueDeck.size())) {
      throw new IllegalArgumentException("Invalid deck and number of hands");
    }

    this.redHand = new ArrayList<>(this.redDeck.subList(0, numHands));
    this.blueHand = new ArrayList<>(this.blueDeck.subList(0, numHands));
    this.redDeck = new ArrayList<>(this.redDeck.subList(numHands, redDeck.size()));
    this.blueDeck = new ArrayList<>(this.blueDeck.subList(numHands, blueDeck.size()));
  }

  @Override
  public void startGame() {
    for (ModelListener listener : listeners) {
      listener.onTurnSwitch(currPlayer);
    }
  }

  @Override
  public SanguineElement[][] getBoard() {
    SanguineElement[][] copyBoard = new SanguineElement[this.board.length][this.board[0].length];
    for (int row = 0; row < this.board.length; row++) {
      for (int col = 0; col < this.board[0].length; col++) {
        if (this.board[row][col] == null) {
          copyBoard[row][col] = null;
        } else {
          //INVARIANT: the number of rows of the board is always positive and the number of columns
          //odd.
          copyBoard[row][col] = this.board[row][col].getCopy();
        }
      }
    }
    return copyBoard;
  }

  @Override
  public int getNumHands() {
    return numHands;
  }

  @Override
  public int sumOfVals(int row, Player player) {
    int sum = 0;
    for (int col = 0; col < this.board[row].length; col++) {
      if (board[row][col] != null && board[row][col] instanceof BasicSanguineCard
          && board[row][col].getOwner().equals(player)) {
        sum += ((BasicSanguineCard) board[row][col]).getValue();
      }
    }
    return sum;
  }

  @Override
  public boolean isGameOver() {
    if (noChangeCount >= 2) {
      return true;
    }
    if (!(redDeck.isEmpty() && blueDeck.isEmpty()
        && redHand.isEmpty() && blueHand.isEmpty())) {
      return false;
    }
    for (SanguineElement[] sanguineElements : this.board) {
      for (SanguineElement sanguineElement : sanguineElements) {
        if (sanguineElement == null) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public Player getWinner() {
    int redSum = getScore(Player.RED);
    int blueSum = getScore(Player.BLUE);

    Player winner;
    if (redSum == blueSum) {
      winner = null;
    } else if (redSum > blueSum) {
      winner = Player.RED;
    } else {
      winner = Player.BLUE;
    }
    return winner;
  }

  @Override
  public void placeCard(int handIndex, int row, int col) throws IllegalArgumentException {

    int length = this.currPlayer == Player.RED ? this.redHand.size() : this.blueHand.size();
    if (handIndex > length - 1) {
      throw new IllegalArgumentException("Hand index out of bounds");
    }
    if (row < 0 || col < 0 || row >= this.board.length || col >= this.board[0].length) {
      throw new IllegalArgumentException("Invalid row/col");
    }
    SanguineCard card =
        this.currPlayer == Player.RED ? redHand.get(handIndex) : blueHand.get(handIndex);
    if (canBePlaced(card, this.board[row][col])) {
      this.board[row][col] = card;
      if (this.currPlayer == Player.RED) {
        redHand.remove(handIndex);
      } else {
        blueHand.remove(handIndex);
      }
    } else {
      throw new IllegalStateException("Card placement is not legal.");
    }
    //traverse board and see which any influences on influence grid fall on the board
    //influence grid places card at row 2 column 2 on the 5x5 grid (index)
    int rowOffset = 2 - row;
    int colOffset = 2 - col;
    for (int boardRow = 0; boardRow < this.board.length; boardRow++) {
      for (int boardCol = 0; boardCol < this.board[0].length; boardCol++) {
        if (!(boardRow == row && boardCol == col)) { //skip card being placed
          if (boardRow + rowOffset >= 0 && boardRow + rowOffset < card.getInfluenceGrid().length
              && boardCol + colOffset >= 0
              && boardCol + colOffset < card.getInfluenceGrid()[0].length) {
            Influence infl = card.getInfluenceGrid()[boardRow + rowOffset][boardCol + colOffset];
            if (infl != null) {
              applyInfluence(infl, boardRow, boardCol);
            }
          }
        }
      }
    }
  }

  @Override
  public boolean canBePlaced(SanguineCard card, SanguineElement el) {
    if (el instanceof SanguineCard) {
      return false;
    }
    if (el instanceof SanguinePawnCluster) {
      return ((SanguinePawnCluster) el).getNumPawns() >= card.getCost()
          && card.getOwner() == ((SanguinePawnCluster) el).getOwner();
    }
    return card.getCost() == 0;
  }

  @Override
  public void drawCard() {
    if (currPlayer == Player.RED) {
      if (!redDeck.isEmpty()) {
        this.redHand.add(redDeck.removeFirst());
      } else {
        throw new IllegalStateException("Deck is empty");
      }
    } else {
      if (!blueDeck.isEmpty()) {
        this.blueHand.add(blueDeck.removeFirst());
      } else {
        throw new IllegalStateException("Deck is empty");
      }
    }
  }

  @Override
  public List<SanguineCard> getHands(Player player) {
    return player == Player.RED ? new ArrayList<>(redHand) : new ArrayList<>(blueHand);
  }

  @Override
  public List<SanguineCard> getDeck(Player player) {
    return player == Player.RED ? new ArrayList<>(redDeck) : new ArrayList<>(blueDeck);
  }

  @Override
  public int getScore(Player player) {
    int sum = 0;
    for (int row = 0; row < this.board.length; row++) {
      sum += sumOfVals(row, player);
    }
    return sum;
  }

  @Override
  public void switchPlayer() throws IllegalStateException {
    if (Arrays.deepToString(prevBoard).equals(Arrays.deepToString(board))) {
      noChangeCount++;
    } else {
      //ensure once 2 turns skipped it stays above 2.
      if (noChangeCount < 2) {
        noChangeCount = 0;
      }
    }
    currPlayer = this.currPlayer == Player.RED ? Player.BLUE : Player.RED;
    prevBoard = getBoard();
    for (ModelListener listener : listeners) {
      listener.onTurnSwitch(currPlayer);
    }
  }

  @Override
  public void subscribe(ModelListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public void endGame() {
    for (ModelListener listener : this.listeners) {
      listener.onGameOver();
    }
  }

  @Override
  public int getNumRows() {
    return numRows;
  }

  @Override
  public int getNumColumns() {
    return numCols;
  }

  @Override
  public void applyInfluence(Influence infl, int row, int col) {
    if (this.board[row][col] == null) {
      PawnClusterSize size;
      if (infl.numPawnIncrease() == 1) {
        size = PawnClusterSize.ONE;
      } else if (infl.numPawnIncrease() == 2) {
        size = PawnClusterSize.TWO;
      } else if (infl.numPawnIncrease() >= 3) {
        size = PawnClusterSize.THREE;
      } else {
        return;
      }
      this.board[row][col] = new BasicSanguinePawnCluster(size, currPlayer);
    } else {
      if (this.board[row][col] instanceof BasicSanguinePawnCluster) {
        if (((BasicSanguinePawnCluster) this.board[row][col]).getNumPawns()
            + infl.numPawnIncrease() < 0) {
          this.board[row][col] = null;
        } else {
          this.board[row][col].applyInfluence(infl, currPlayer);
        }
      }
    }
  }


  @Override
  public Player getCurrPlayer() {
    return currPlayer;
  }
}
