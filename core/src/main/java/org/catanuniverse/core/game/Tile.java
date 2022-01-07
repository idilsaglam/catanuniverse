/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

import java.util.ArrayList;
import java.util.List;
import org.catanuniverse.core.exceptions.NoSuchSlotException;
import org.catanuniverse.core.exceptions.SlotAlreadyTakenException;
import org.catanuniverse.core.exceptions.TileTypeNotSupportedException;

abstract class Tile {
    private static int TILE_COUNTER = 0;
    protected final int id;
    protected final int uid;
    protected final GroundType type;
    protected final Resource resource;
    protected final Tile[] neighbors;
    protected final Road[] roadSlots;
    protected final Harbor[] harbors;
    protected final Settlement[] settlementSlots;
    protected boolean playable;

    /**
     * Create a new instance of Tile object
     *
     * @param id The id of the Tile
     * @param type The type of the Tile
     * @param nbSides The number of sides that the tile has
     */
    protected Tile(int id, GroundType type, int nbSides) {
        Tile.TILE_COUNTER++;
        this.uid = Tile.TILE_COUNTER;
        this.id = id;
        this.type = type;
        this.resource = this.type.produces();
        // Neighbors are counted from the top left on the clockwise
        this.neighbors = new Tile[nbSides];
        // Harbor slots are the same as the neighbor slots
        this.harbors = new Harbor[nbSides];
        // Road slots are the same as the neighbors, counted from the top left on the clockwise
        this.roadSlots = new Road[nbSides];
        // Settlement slots are corners, counted from the top on the clockwise
        this.settlementSlots = new Settlement[nbSides];
        this.playable = this.getGroundType().produces() != null;
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
     * Update the playable status of tile
     * @param playable The value of playable attribute
     */
    public void setPlayable(boolean playable) {
        this.playable = playable;
    }


    /**
     * Gets if the current tile is playable or not
     * @return True if the current tile is playable false if not
     */
    public boolean getPlayable() {
        return this.playable;
    }

    /**
     * Returns the neighbor of the Tile on the given position
     *
     * @param index The index of the neighbor slot
     * @return The neighbor Tile
     * @throws NoSuchSlotException If there's no slots for the given index
     */
    protected Tile getNeighbor(int index) throws NoSuchSlotException {
        this.isSlotExists(index);
        return this.neighbors[index];
    }

    /**
     * Returns the given harbor slot
     *
     * @param index The index of the Harbor slot
     * @return The content of the harbor slot
     * @throws NoSuchSlotException If there's not slot matching with the given index
     */
    public Harbor getHarbor(int index) throws NoSuchSlotException {
        this.isSlotExists(index);
        return this.harbors[index];
    }

    /**
     * Checks if the current tile and given neighbours are playable. A tile is not playable if its ground type and its neighbors ground type are all water.
     * @param indexes The array of indexes
     * @return True if playable false if not
     * @throws NoSuchSlotException If there's an index which does not exists
     */
    boolean playable(int[] indexes) throws NoSuchSlotException {
        if (this.getGroundType() != GroundType.Water) return true;
        for (int index : indexes) {
            if (this.getNeighbor(index) != null && this.getNeighbor(index).getGroundType() != GroundType.Water) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the road slot on the given index
     *
     * @param index The index of the road slot
     * @return The content of the road slot
     * @throws NoSuchSlotException If there's no slot for the given index
     */
    protected Road getRoadSlot(int index) throws NoSuchSlotException {
        this.isSlotExists(index);
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
        this.isSlotExists(index);
        return this.settlementSlots[index];
    }

    /**
     * Return the resource type
     *
     * @return The resource of the tile
     */
    protected GroundType getGroundType() {
        return this.type;
    }

    /**
     * Get the id of the tile
     *
     * @return the id of the tile
     */
    protected int getId() {
        return this.id;
    }

    /**
     * Adds a road on the given position if possible
     *
     * @param index The index of the road slot
     * @param road The road to insert
     * @throws SlotAlreadyTakenException if the given slot is already occupied by another user
     * @throws NoSuchSlotException If there's no slot for the given index
     */
    public void addRoad(int index, Road road)
            throws SlotAlreadyTakenException, NoSuchSlotException {
        this.isSlotExists(index);
        if (this.playable(new int[]{index})) {
            if (this.roadSlots[index] == null) {
                // We can insert only if the slot is empty
                this.roadSlots[index] = road;
                int complementaryIndex = this.complementaryIndex(index);
            /*
               Road slots are the same as the neighbor slots
               when we add road, we need to add the complementary road for our neighbor too
            */
                if (this.neighbors[index].roadSlots[complementaryIndex] == null) {
                    this.neighbors[index].roadSlots[complementaryIndex] = road;
                    return;
                }
            }
            throw new SlotAlreadyTakenException();
        }
    }

    /**
     * Adds a settlement on the given position if possible
     *
     * @param index The index of the settlement slot
     * @param settlement The settlement to insert
     * @throws SlotAlreadyTakenException If there's already a settlement on the given slot
     * @throws NoSuchSlotException If there's no slots matching the given index
     */
    protected abstract void addSettlement(int index, Settlement settlement)
            throws SlotAlreadyTakenException, NoSuchSlotException;

    /**
     * Upgrades the settlement in the given slot to a City
     * @param index The index of the settlement slot
     * @throws NoSuchSlotException There's no slot with the given index
     */
    public void upgradeSettlement(int index) throws NoSuchSlotException {
        if (this.settlementSlots[index] != null && !(this.settlementSlots[index] instanceof City)) {
            City settlement = new City(this.settlementSlots[index].getOwner());
            int compIndex = complementaryIndex(index);
            // We can insert only if the slot is null
            this.settlementSlots[index] = settlement;
            if (this.neighbors[index] != null) {

                System.out
                    .printf("Will add neighbor id: %d slot %d\n", this.neighbors[index].getId(),
                        compIndex);
                this.neighbors[index].settlementSlots[(compIndex + 1)%this.neighbors.length] = settlement;
            }
            index = (index + this.neighbors.length - 1) % this.neighbors.length;
            compIndex = complementaryIndex(index);
            if (this.neighbors[index] != null) {
                this.neighbors[index].settlementSlots[compIndex] = settlement;
            }
        }
    }

    /**
     * Get the index of the nearest neighbor which is not water
     * @return The index of nearest neighbor which is not water
     */
    public Integer getNearestNeighbor() {
        for (int i = 0; i<this.neighbors.length; i++) {
            if (this.neighbors[i] != null && this.neighbors[i].getGroundType() != GroundType.Water) {
                return i;
            }
        }
        return null;
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

        this.isSlotExists(index);

        if (!neighbor.getClass().equals(this.getClass())) {
            // The neighbor and the current class should has the same class name
            throw new TileTypeNotSupportedException();
        }
        if (this.neighbors[index] == null && this.harbors[index] == null) {
            // We can insert only if the neighbor slots on the given index is empty
            this.neighbors[index] = neighbor;
            // When we add a neighbor, the current Tile will automatically become the neighbor too.
            neighbor.neighbors[this.complementaryIndex(index)] = this;
            return;
        }
        throw new SlotAlreadyTakenException();
    }

    /**
     * Add harbor to the given slot
     *
     * @param index The index of the slot
     * @param harbor The harbor object to add
     * @throws NoSuchSlotException If there's no slot matching with the given index
     */
    protected void addHarbor(int index, Harbor harbor)
            throws NoSuchSlotException {
        this.isSlotExists(index);
        if ((this.getGroundType() == GroundType.Water || this.neighbors[index].getGroundType() == GroundType.Water) && (this.neighbors[index] != null && this.harbors[index] == null)) {
            this.harbors[index] = harbor;
            this.neighbors[index].harbors[complementaryIndex(index)] = harbor;
        }
    }

    public void addHarbor(Harbor h) throws NoSuchSlotException {
        this.addHarbor(this.getNearestNeighbor(), h);
    }

    /**
     * Returns the list of Tiles on the intersection of the given corner
     *
     * @param index The corner index
     * @return The list of tiles intersecting with the given corner with the current Tile
     */
    protected List<Tile> getInsersectingNeighbors(int index) throws NoSuchSlotException {
        this.isSlotExists(index);
        /*
           A corner is the intersection of 2 sides.
           The lower bounding side is the side with the same index and the upper bounding side
           is the side with (index + 1) % nbSides.
           For example:
               On a hextile:
                   Corner index 0 will be bounded by sides 0 and 1 and
                   corner 5 is bounded by sides 5 and 0.
               On a square:
                   Corner index 0 is bounded by sides 0 and 1 and
                   corner 3 is bounded by sides 3 and 0
        */
        Tile neighbor = this.neighbors[index];
        int bound = -1;
        if (neighbor == null) {
            // if the lower bound side is empty we'll look for the upper bound
            index = (index + 1) % this.neighbors.length;
            neighbor = this.neighbors[index];
            bound = 1;
        }
        if (neighbor == null) {
            /*
               For instance we do not need to handle the case that 2 tiles are intersecting
               on a same corner with no common neighbors
            */
            return new ArrayList<>();
        }
        List<Tile> result = new ArrayList<>();
        do {
            result.add(neighbor);
            index =
                    (this.complementaryIndex(index) + bound + this.neighbors.length)
                            % this.neighbors.length;
            neighbor = neighbor.neighbors[index];
            bound *= -1;
        } while (neighbor != null && !neighbor.equals(this));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Tile) {
            return ((Tile) o).uid == this.uid;
        }
        return false;
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
        this.isSlotExists(index);
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

    /**
     * Throws an exception if there's no slot matching with the given index
     *
     * @param index The index of the slot
     * @throws NoSuchSlotException If there's no slot matching with the given index
     */
    void isSlotExists(int index) throws NoSuchSlotException {
        if (index < 0 || index >= this.neighbors.length) {
            throw new NoSuchSlotException();
        }
    }

    public abstract void sendResources();

    /**
     * Checks if current tile has harbor or not
     * @return True if current tile has harbor or false
     */
    public boolean hasHarbor() {
        for (Harbor h: this.harbors) {
            if (h == null) continue;
            return true;
        }
        return false;
    }

    /**
     * Checks if a settlement can added to the given index of the given tile
     * @param index The index of the settlement slot
     * @return True if a settlement can be added to the given slot false if not
     */
    public abstract boolean canAddSettlement(int index) throws NoSuchSlotException;

    /**
     * Get the harbor index from the current Tile
     * @return The index of the harbor or null
     */
    public Integer getHarborIndex(){
        for (int i = 0; i<this.harbors.length; i++) {
            if (this.harbors[i] == null) continue;
            return i;
        }
        return null;
    }

    /**
     * Checks if a road can added with given index or not
     * @param roadIndex The index of the road slot
     * @return True if a road can added to the given slot, or not
     */
    public boolean canAddRoad(Integer roadIndex, Road road) throws NoSuchSlotException {
        if ((this.roadSlots[roadIndex] == null) && (this.getGroundType() != GroundType.Water) || (this.neighbors[roadIndex] != null && this.neighbors[roadIndex].getGroundType() != GroundType.Water)) {
            final int roadPlayerUid = road.getOwner().uid;
            final int cindex = complementaryIndex(roadIndex);
            return !(
                (
                    this.roadSlots[(roadIndex + 1) % this.roadSlots.length] != null &&
                        this.roadSlots[(roadIndex + 1) % this.roadSlots.length].getOwner().uid != roadPlayerUid
                ) ||
                    (
                        this.roadSlots[(roadIndex - 1 + this.roadSlots.length) % this.roadSlots.length] != null &&
                            this.roadSlots[(roadIndex - 1 + this.roadSlots.length) % this.roadSlots.length].getOwner().uid != roadPlayerUid
                    ) ||
                    (
                        this.neighbors[roadIndex].roadSlots[(cindex + 1) % this.neighbors[roadIndex].roadSlots.length] != null &&
                            this.neighbors[roadIndex].roadSlots[(cindex + 1) % this.neighbors[roadIndex].roadSlots.length].getOwner().uid != roadPlayerUid
                    ) ||
                    (
                        this.neighbors[roadIndex].roadSlots[(cindex - 1 + this.neighbors[roadIndex].roadSlots.length) % this.neighbors[roadIndex].roadSlots.length] != null &&
                            this.neighbors[roadIndex].roadSlots[(cindex - 1 + this.neighbors[roadIndex].roadSlots.length) % this.neighbors[roadIndex].roadSlots.length].getOwner().uid != roadPlayerUid
                    )
            );

        }
        return false;
    }

    protected boolean hasSettlementsOnBothSidesIntersection(int cornerIndex) {
        return this.settlementSlots[cornerIndex] == null && this.settlementSlots[(cornerIndex + 1) % this.settlementSlots.length] == null && this.settlementSlots[(cornerIndex - 1 + this.settlementSlots.length) % this.settlementSlots.length] == null;
    }
}
