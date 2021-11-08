/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.models;

public class Settlement extends Pin {

    protected static final Positions[] SUPPORTED_DIRECTIONS =
            new Positions[] {
                Positions.TOP,
                Positions.TOP_RIGHT,
                Positions.BOTTOM_RIGHT,
                Positions.BOTTOM,
                Positions.BOTTOM_LEFT,
                Positions.TOP_LEFT
            };

    /**
     * Creates a new Settlement pin owned by the given user
     *
     * @param owner The owner of the Settlement
     */
    public Settlement(Player owner) {
        super(owner);
    }
}
