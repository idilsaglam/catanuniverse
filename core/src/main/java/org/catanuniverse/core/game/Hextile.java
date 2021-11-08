/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

final class Hextile extends Tile {
    private static final int NB_SIDES = 6;
    /**
     * Create a new instance of Tile object
     *
     * @param id The id of the Tile
     * @param type The type of the Tile
     */
    protected Hextile(int id, GroundType type) {
        super(id, type, Hextile.NB_SIDES);
    }
}
