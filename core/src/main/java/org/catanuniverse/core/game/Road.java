/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

public class Road extends Pin {

    protected static final Positions[] SUPPORTED_DIRECTIONS =
            new Positions[] {
                Positions.TOP_LEFT,
                Positions.TOP_RIGHT,
                Positions.RIGHT,
                Positions.BOTTOM_RIGHT,
                Positions.BOTTOM_LEFT,
                Positions.LEFT
            };
    /**
     * Create a new Road object owned by the given player
     *
     * @param owner The owner of the road pion
     */
    protected Road(Player owner) {
        super(owner);
    }
}
