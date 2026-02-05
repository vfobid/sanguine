package sanguine.model;

import java.util.Arrays;

/**
 * Represents a card from the game Sanguine, with a value, cost (the number of pawns required for
 * the card to be placed.
 */
public class BasicSanguineCard implements SanguineCard {

  private int value;
  private int cost;
  private String name;
  final Influence[][] influenceGrid;
  private Player owner;

  /**
   * Constructor for a BasicSanguineCard.
   *
   * @param value         the value of the card.
   * @param cost          the cost of the card, AKA how many pawns need to be on a spot on the board before
   *                      the card can be placed there.
   * @param name          the String name of the card.
   * @param influenceGrid the grid locating where on the board elements can be mutated by this
   *                      card's influences.
   * @param owner         determines the owner of the card: red player or blue player.
   */
  public BasicSanguineCard(int value, int cost, String name, Influence[][] influenceGrid,
                           Player owner) {
    this.value = value;
    this.cost = cost;
    this.name = name;
    this.influenceGrid = influenceGrid;
    this.owner = owner;
  }

  @Override
  public int getValue() {
    return value;
  }

  @Override
  public int getCost() {
    return cost;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Influence[][] getInfluenceGrid() {
    return influenceGrid;
  }

  @Override
  public void applyInfluence(Influence influence, Player owner) {
    //In actual Sanguine cards can be influenced, not yet here.
  }

  @Override
  public Player getOwner() {
    return owner;
  }

  @Override
  public SanguineCard getCopy() {
    return new BasicSanguineCard(this.value, this.cost, this.name, this.influenceGrid,
        this.owner);
  }

  @Override
  public String toString() {
    return this.owner.equals(Player.RED) ? "R" : "B";
  }

  @Override
  public int hashCode() {
    //max cost hash is 21
    int hashCost = this.owner.equals(Player.RED) ? 3 : 7;
    int hashValue = this.owner.equals(Player.RED) ? 23 : 29;
    return name.hashCode() + cost * hashCost + value * hashValue
        + Arrays.deepHashCode(influenceGrid);
  }

  @Override
  public boolean equals(Object obj) {

    if (!(obj instanceof BasicSanguineCard)) {
      return false;
    }
    BasicSanguineCard other = (BasicSanguineCard) obj;

    // Compare simple fields first
    if (this.cost != other.cost
        || this.value != other.value
        || this.owner != other.getOwner()
        || !this.name.equals(other.name)) {
      return false;
    }

    // Compare influenceGrid element by element
    if (this.influenceGrid.length != other.influenceGrid.length) {
      return false;
    }
    for (int row = 0; row < this.influenceGrid.length; row++) {
      if (this.influenceGrid[row].length != other.influenceGrid[row].length) {
        return false;
      }
      for (int col = 0; col < this.influenceGrid[row].length; col++) {
        Influence a = this.influenceGrid[row][col];
        Influence b = other.influenceGrid[row][col];
        if (a == null && b != null || a != null && b == null) {
          return false;
        }
        if (a == null && b == null) {
          continue;
        }
        if (!a.equals(b)) {
          return false;
        }
      }
    }
    return true;
  }
}
