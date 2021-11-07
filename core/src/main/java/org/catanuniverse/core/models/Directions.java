/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.core.models;

import org.catanuniverse.core.exceptions.InvalidDirectionException;

public enum Directions {
    TopLeft,
    Top,
    TopRight,
    Right,
    BottomRight,
    Bottom,
    BottomLeft,
    Left;

    /**
     * Get the reversed directions
     *
     * @return The reversed directions
     */
    public Directions reversed() {
        return switch (this) {
            case TopLeft -> BottomRight;
            case Top -> Bottom;
            case TopRight -> BottomLeft;
            case Right -> Left;
            case BottomRight -> TopLeft;
            case Bottom -> Top;
            case BottomLeft -> TopRight;
            case Left -> Right;
        };
    }

    /**
     * Function computes the index related to the given direction
     *
     * @param isCorner If true the index will be calculated for corners
     * @return The computed index for the given direction and given pin
     * @throws InvalidDirectionException If the direction not supported by the given pin
     */
    protected int computeIndex(boolean isCorner) throws InvalidDirectionException {
        Directions[] supportedDirections =
                (isCorner ? Colony.supportedDirections : Road.supportedDirections);
        for (int i = 0; i < supportedDirections.length; i++) {
            if (supportedDirections[i].equals(this)) {
                return i;
            }
        }
        throw new InvalidDirectionException();
    }
}
