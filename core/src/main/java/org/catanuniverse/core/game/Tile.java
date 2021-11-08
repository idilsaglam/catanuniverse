/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

import org.catanuniverse.core.exceptions.NoSuchSlotException;
import org.catanuniverse.core.exceptions.SlotAlreadyTakenException;
import org.catanuniverse.core.exceptions.TileTypeNotSupportedException;

abstract class Tile {

    protected final int id;
    protected final GroundType type;
    protected final Resource resource;
    protected final Tile[] neighbors;
    protected final Road[] roadSlots;
    protected final Settlement[] settlementSlots;

    /**
     * Create a new instance of Tile object
     *
     * @param id The id of the Tile
     * @param type The type of the Tile
     * @param nbSides The number of sides that the tile has
     */
    protected Tile(int id, GroundType type, int nbSides) {
        this.id = id;
        this.type = type;
        this.resource = this.type.produces();
        // Neighbors are counted from the top left on the clockwise
        this.neighbors = new Tile[nbSides];
        // Road slots are the same as the neighbors, counted from the top left on the clockwise
        this.roadSlots = new Road[nbSides];
        // Settlement slots are corners, counted from the top on the clockwise
        this.settlementSlots = new Settlement[nbSides];
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
     * @param index The index of the neighbor slot
     * @return The neighbor Tile
     * @throws NoSuchSlotException If there's no slots for the given index
     */
    protected Tile getNeighbor(int index) throws NoSuchSlotException {
        if (index < 0 || index >= this.neighbors.length) {
            throw new NoSuchSlotException();
        }
        return this.neighbors[index];
    }

    /**
     * Returns the road slot on the given index
     *
     * @param index The index of the road slot
     * @return The content of the road slot
     * @throws NoSuchSlotException If there's no slot for the given index
     */
    protected Road getRoadSlot(int index) throws NoSuchSlotException {
        if (index < 0 || index >= this.roadSlots.length) {
            throw new NoSuchSlotException();
        }
        return this.roadSlots[index];
    }

    /**
     * Returns the settlement slot on the given position
     *
     * @param index The index of the settlement slot
     * @return The content of the settlement slot
     * @throws NoSuchSlotException If there's no slot for the given index
     */
    protected Settlement getSettlementSlot(int index) throws NoSuchSlotException {
        if (index < 0 || index >= this.settlementSlots.length) {
            throw new NoSuchSlotException();
        }
        return this.settlementSlots[index];
    }

    /**
     * Adds a road on the given position if possible
     *
     * @param index The index of the road slot
     * @param road The road to insert
     * @throws SlotAlreadyTakenException if the given slot is already occupied by another user
     * @throws NoSuchSlotException If there's no slot for the given index
     */
    protected void addRoad(int index, Road road)
            throws SlotAlreadyTakenException, NoSuchSlotException {
        if (index < 0 || index >= this.roadSlots.length) {
            throw new NoSuchSlotException();
        }
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
     * @param index The index of the settlement slot
     * @param settlement The settlement to insert
     * @throws SlotAlreadyTakenException If there's already a settlement on the given slot
     * @throws NoSuchSlotException If there's no slots matching the given index
     */
    protected void addSettlement(int index, Settlement settlement)
            throws SlotAlreadyTakenException, NoSuchSlotException {
        if (index < 0 || index >= this.settlementSlots.length) {
            throw new NoSuchSlotException();
        }
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
     * @param index The index of the neighbor slot to add the the neighbor
     * @param neighbor The neighbor tile to add
     * @throws NoSuchSlotException If the position is not valid
     * @throws SlotAlreadyTakenException If there's already a neighbor on the given position
     * @throws TileTypeNotSupportedException If the type of the tile does not match with the tile of
     *     the current tile
     */
    protected void addNeighbor(int index, Tile neighbor)
            throws NoSuchSlotException, SlotAlreadyTakenException, TileTypeNotSupportedException {

        if (index < 0 || index >= this.neighbors.length) {
            throw new NoSuchSlotException();
        }

        if (!(neighbor.getClass().equals(this.getClass()))) {
            // The neighbor and the current class should has the same class name
            throw new TileTypeNotSupportedException();
        }
        if (this.neighbors[index] == null) {
            // We can insert only if the neighbor slots on the given index is empty
            this.neighbors[index] = neighbor;
            // When we add a neighbor, the current Tile will automatically become the neighbor too.
            neighbor.neighbors[this.complementaryIndex(index)] = this;
            return;
        }
        throw new SlotAlreadyTakenException();
    }

    /**
     * Returns the complementary index of the given index This will mainly used for adding neighbors
     *
     * @param index The current index
     * @return The complementary index
     * @throws NoSuchSlotException If there's no slots for the given index
     */
    protected int complementaryIndex(int index) throws NoSuchSlotException {
        int max = this.neighbors.length;
        if (index < 0 || index >= max) {
            throw new NoSuchSlotException();
        }
        if (max % 2 == 0) {
            int mid = max / 2;
            if (index < mid) {
                return index + max / 2;
            }
            return index % mid;
        }
        if (index == max - 1) {
            return index;
        }
        return max - 2 - index;
    }
}
