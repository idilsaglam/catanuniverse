/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.core.models;

import org.catanuniverse.core.exceptions.InvalidDirectionException;
import org.catanuniverse.core.exceptions.SlotAlreadyTakenException;

public class Tile {

    private final static int NB_SIDES = 6;

    private final int id;
    private final GroundType type;
    private final Resource resource;
    private final Tile[] neighbors;
    private final Road[] roadSlots;
    private final Colony[] colonySlots;

    /**
     * Create a new instance of Tile object
     *
     * @param id The id of the Tile
     * @param type The type of the Tile
     */
    public Tile(int id, GroundType type) {
        this.id = id;
        this.type = type;
        this.resource = this.type.produces();
        // Neighbors are counted from the top left on the clockwise
        this.neighbors = new Tile[Tile.NB_SIDES];
        // Road slots are the same as the neighbors, counted from the top left on the clockwise
        this.roadSlots = new Road[Tile.NB_SIDES];
        // Colony slots are corners, counted from the top on the clockwise
        this.colonySlots = new Colony[Tile.NB_SIDES];
    }

    /**
     * Return the resource produced by the current Tile
     *
     * @return The resource produced by the current tile
     */
    public Resource produce() {
        return this.resource;
    }

    /**
     * Returns the neighbor of the Tile on the given direction
     * @param direction The direction of the neighbor
     * @return The neighbor Tile
     * @throws InvalidDirectionException if the direction is not valid
     */
    public Tile getNeighbor(Directions direction) throws InvalidDirectionException {
        return this.neighbors[direction.computeIndex(false)];
    }

    /**
     * Returns the road slot on the given direction
     * @param direction The direction of the road slot
     * @return The content of the road slot
     * @throws InvalidDirectionException If direction is not valid
     */
    public Road getRoadSlot(Directions direction) throws InvalidDirectionException {
        return this.roadSlots[direction.computeIndex(false)];
    }

    /**
     * Returns the colony slot on the given direction
     * @param direction The direction to check for the colony slot
     * @return The content of the colony slot
     * @throws InvalidDirectionException If the given direction is not valid
     */
    public Colony getColonySlot(Directions direction) throws InvalidDirectionException {
        return this.colonySlots[direction.computeIndex(true)];
    }

    /**
     * Insert a road on the given direction if possible
     *
     * @param direction The direction to insert the road
     * @param road The road to insert
     * @throws InvalidDirectionException if the direction is not valid
     * @throws SlotAlreadyTakenException if the given slot is already occupied by another user
     */
    public void insertRoad(Directions direction, Road road)
            throws InvalidDirectionException, SlotAlreadyTakenException {
        // Calculate the index of the roadSlots
        int index = direction.computeIndex(false);
        if (this.roadSlots[index] == null) {
            // We can insert only if the slot is empty
            this.roadSlots[index] = road;
            return;
        }
        throw new SlotAlreadyTakenException();
    }

    /**
     * Inserts a colony on the given direction if possible
     *
     * @param direction The direction to insert the colony
     * @param colony The colony to insert
     */
    public void insertColony(Directions direction, Colony colony)
            throws InvalidDirectionException, SlotAlreadyTakenException {
        // Calculate the index of the colonySlots
        int index = direction.computeIndex(true);
        if (this.colonySlots[index] == null) {
            // We can insert only if the slot is null
            this.colonySlots[index] = colony;
            return;
        }
        throw new SlotAlreadyTakenException();
    }

    /**
     * Function inserts a new neighbor on the given direction
     *
     * @param direction The direction to add the neighbor
     * @param neighbor The neighbor tile to add
     * @throws InvalidDirectionException If the direction is not valid
     * @throws SlotAlreadyTakenException If there's already a neighbor on the given direction
     */
    public void insertNeighbor(Directions direction, Tile neighbor)
            throws InvalidDirectionException, SlotAlreadyTakenException {
        /*
          This can be tricky: The compute index uses the supported directions array of the road by default.
          As the supported directions for neighbors are the same as the roads,
          we'll just pass null for pin and compute the index.
        */
        int index = direction.computeIndex(false);
        if (this.neighbors[index] == null) {
            // We can insert only if the neighbor slots on the given index is empty
            this.neighbors[index] = neighbor;
            // When we add a neighbor, the current Tile will automatically become the neighbor too.
            neighbor.neighbors[direction.reversed().computeIndex(false)] = this;
        }
        throw new SlotAlreadyTakenException();
    }
}
