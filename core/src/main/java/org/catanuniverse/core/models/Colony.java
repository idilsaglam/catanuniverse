/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.models;

public class Colony extends Pin {

    protected static final Directions[] supportedDirections =
            new Directions[] {
                Directions.Top,
                Directions.TopRight,
                Directions.BottomRight,
                Directions.Bottom,
                Directions.BottomLeft,
                Directions.TopLeft
            };

    /**
     * Creates a new Colony pin owned by the given user
     *
     * @param owner The owner of the Colony
     */
    public Colony(Player owner) {
        super(owner);
    }
}
