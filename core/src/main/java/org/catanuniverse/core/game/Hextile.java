/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.core.game;

import org.catanuniverse.core.exceptions.NoSuchSlotException;
import org.catanuniverse.core.exceptions.SlotAlreadyTakenException;

public final class Hextile extends Tile {

    public static final int NB_SIDES = 6;

    /**
     * Create a new instance of Tile object
     *
     * @param id The id of the Tile
     * @param type The type of the Tile
     */
    public Hextile(int id, GroundType type) {
        super(id, type, Hextile.NB_SIDES);
    }

    @Override
    public GroundType getGroundType() {
        return super.getGroundType();
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public Hextile getNeighbor(int index) {
        try {
            return (Hextile) super.getNeighbor(index);
        } catch (NoSuchSlotException e) {
            return null;
        }
    }

    @Override
    public Settlement getSettlementSlot(int index) {
        try {
            return super.getSettlementSlot(index);
        } catch (NoSuchSlotException ignore) {
            return null;
        }
    }

    @Override
    public Road getRoadSlot(int index) {
        try {
            return super.getRoadSlot(index);
        } catch (NoSuchSlotException ignore) {
            return null;
        }
    }

    @Override
    public void addSettlement(int index, Settlement settlement)
            throws SlotAlreadyTakenException, NoSuchSlotException {
        {
            this.isSlotExists(index);
            if (this.playable(
                    new int[] {
                        index, (index + this.neighbors.length - 1) % this.neighbors.length
                    })) {
                if (this.settlementSlots[index] == null) {
                    int compIndex = complementaryIndex(index);
                    // We can insert only if the slot is null
                    this.settlementSlots[index] = settlement;
                    if (this.neighbors[index] != null) {

                        this.neighbors[index]
                                        .settlementSlots[(compIndex + 1) % this.neighbors.length] =
                                settlement;
                    }
                    index = (index + this.neighbors.length - 1) % this.neighbors.length;
                    compIndex = complementaryIndex(index);
                    if (this.neighbors[index] != null) {
                        this.neighbors[index].settlementSlots[compIndex] = settlement;
                    }
                    return;
                }
                throw new SlotAlreadyTakenException();
            }
        }
    }

    @Override
    public void sendResources() {
        if (!this.playable) return;
        Resource r = this.getGroundType().produces();
        if (r == null) return;
        for (Settlement s : this.settlementSlots) {
            if (s == null) continue;
            s.sendResource(r);
        }
    }

    @Override
    public boolean canAddSettlement(int index) throws NoSuchSlotException {
        if (this.playable(
                new int[] {index, (index + this.neighbors.length - 1) % this.neighbors.length})) {

            boolean result = this.hasSettlementsOnBothSidesIntersection(index);

            if (!result) return false;
            int compIndex = complementaryIndex(index);
            result =
                    this.neighbors[index] != null
                            && this.neighbors[index].hasSettlementsOnBothSidesIntersection(
                                    (compIndex + 1) % this.neighbors.length);

            if (!result) return false;
            index = (index + this.neighbors.length - 1) % this.neighbors.length;
            compIndex = complementaryIndex(index);
            result =
                    this.neighbors[compIndex] != null
                            && this.neighbors[compIndex].hasSettlementsOnBothSidesIntersection(
                                    index);

            return result;
        }
        return false;
    }

    @Override
    public Integer getFirstEligibleSettlementSlot(Settlement settlement) {
        for (int i = 0; i < this.settlementSlots.length; i++) {
            try {
                if (this.canAddSettlement(i)) {
                    return i;
                }
            } catch (NoSuchSlotException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return String.format(
                "Hextile id: %d, ground type %s Is playable? %b",
                this.uid, this.getGroundType(), this.playable);
    }
}
