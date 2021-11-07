/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.models;

public class Road extends Pin {

    protected static final Directions[] supportedDirections =
            new Directions[] {
                Directions.TopLeft,
                Directions.TopRight,
                Directions.Right,
                Directions.BottomRight,
                Directions.BottomLeft,
                Directions.Left
            };
    /**
     * Create a new Road object owned by the given player
     *
     * @param owner The owner of the road pion
     */
    public Road(Player owner) {
        super(owner);
    }
}
