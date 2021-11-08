package org.catanuniverse.core.game;

import org.catanuniverse.core.exceptions.InvalidPositionException;
import org.catanuniverse.core.exceptions.SlotAlreadyTakenException;

final class Hextile extends Tile{

  /**
   * Create a new instance of Tile object
   *
   * @param id   The id of the Tile
   * @param type The type of the Tile
   */
  protected Hextile(int id, GroundType type) {
    super(id, type, 6);
  }

  /**
   * Return the resource produced by the current Tile
   *
   * @return The resource produced by the current tile
   */
  protected Resource produce() {
    return this.resource;
  }

  /**
   * Returns the neighbor of the Tile on the given position
   *
   * @param position The position of the neighbor
   * @return The neighbor Tile
   * @throws InvalidPositionException if the position is not valid
   */
  protected Tile getNeighbor(Positions position) throws InvalidPositionException {
    return this.neighbors[position.computeIndex(false)];
  }

  /**
   * Returns the road slot on the given position
   *
   * @param position The position of the road slot
   * @return The content of the road slot
   * @throws InvalidPositionException If position is not valid
   */
  protected Road getRoadSlot(Positions position) throws InvalidPositionException {
    return this.roadSlots[position.computeIndex(false)];
  }

  /**
   * Returns the settlement slot on the given position
   *
   * @param position The position to check for the settlement slot
   * @return The content of the settlement slot
   * @throws InvalidPositionException If the given position is not valid
   */
  protected Settlement getSettlementSlot(Positions position) throws InvalidPositionException {
    return this.settlementSlots[position.computeIndex(true)];
  }

  /**
   * Adds a road on the given position if possible
   *
   * @param position The position to insert the road
   * @param road The road to insert
   * @throws InvalidPositionException if the position is not valid
   * @throws SlotAlreadyTakenException if the given slot is already occupied by another user
   */
  protected void addRoad(Positions position, Road road)
      throws InvalidPositionException, SlotAlreadyTakenException {
    // Calculate the index of the roadSlots
    int index = position.computeIndex(false);
    if (this.roadSlots[index] == null) {
      // We can insert only if the slot is empty
      this.roadSlots[index] = road;
      return;
    }
    throw new SlotAlreadyTakenException();
  }

  /**
   * Adds a settlement on the given position if possible
   *
   * @param position The position to insert the settlement
   * @param settlement The settlement to insert
   */
  protected void addSettlement(Positions position, Settlement settlement)
      throws InvalidPositionException, SlotAlreadyTakenException {
    // Calculate the index of the settlementSlots
    int index = position.computeIndex(true);
    if (this.settlementSlots[index] == null) {
      // We can insert only if the slot is null
      this.settlementSlots[index] = settlement;
      return;
    }
    throw new SlotAlreadyTakenException();
  }

  /**
   * Function add a new neighbor on the given position
   *
   * @param position The position to add the neighbor
   * @param neighbor The neighbor tile to add
   * @throws InvalidPositionException If the position is not valid
   * @throws SlotAlreadyTakenException If there's already a neighbor on the given position
   */
  protected void addNeighbor(Positions position, Tile neighbor)
      throws InvalidPositionException, SlotAlreadyTakenException {
        /*
          This can be tricky: The compute index uses the supported directions array of the road by default.
          As the supported directions for neighbors are the same as the roads,
          we'll just pass null for pin and compute the index.
        */
    int index = position.computeIndex(false);
    if (this.neighbors[index] == null) {
      // We can insert only if the neighbor slots on the given index is empty
      this.neighbors[index] = neighbor;
      // When we add a neighbor, the current Tile will automatically become the neighbor too.
      neighbor.neighbors[position.reversed().computeIndex(false)] = this;
      return;
    }
    throw new SlotAlreadyTakenException();
  }
}
