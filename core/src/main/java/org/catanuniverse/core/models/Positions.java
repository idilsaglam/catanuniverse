/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.models;

import org.catanuniverse.core.exceptions.InvalidPositionException;

public enum Positions {
    TOP_LEFT,
    TOP,
    TOP_RIGHT,
    RIGHT,
    BOTTOM_RIGHT,
    BOTTOM,
    BOTTOM_LEFT,
    LEFT;

    /**
     * Get the reversed directions
     *
     * @return The reversed directions
     */
    public Positions reversed() {
        return switch (this) {
            case TOP_LEFT -> BOTTOM_RIGHT;
            case TOP -> BOTTOM;
            case TOP_RIGHT -> BOTTOM_LEFT;
            case RIGHT -> LEFT;
            case BOTTOM_RIGHT -> TOP_LEFT;
            case BOTTOM -> TOP;
            case BOTTOM_LEFT -> TOP_RIGHT;
            case LEFT -> RIGHT;
        };
    }

    /**
     * Function computes the index related to the given direction
     *
     * @param isCorner If true the index will be calculated for corners
     * @return The computed index for the given direction and given pin
     * @throws InvalidPositionException If the direction not supported by the given pin
     */
    protected int computeIndex(boolean isCorner) throws InvalidPositionException {
        Positions[] supportedDirections =
                (isCorner ? Settlement.SUPPORTED_DIRECTIONS : Road.SUPPORTED_DIRECTIONS);
        for (int i = 0; i < supportedDirections.length; i++) {
            if (supportedDirections[i].equals(this)) {
                return i;
            }
        }
        throw new InvalidPositionException();
    }
}
