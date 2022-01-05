/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

import org.catanuniverse.core.exceptions.NoSuchSlotException;

public final class Hextile extends Tile {

    private static final int NB_SIDES = 6;

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
    public void addSettlement(int index, Settlement settlement) {

    }

}
