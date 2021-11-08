/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

public enum GroundType {
    Forest,
    Hill,
    Meadow,
    Farm,
    Mountain,
    Desert;

    /**
     * Returns the Resources produced by this ground type
     *
     * @return The resource produced by this ground type
     */
    public Resource produces() {
        return switch (this) {
            case Forest -> Resource.Wood;
            case Farm -> Resource.Corn;
            case Mountain -> Resource.Mineral;
            case Meadow -> Resource.Wool;
            case Hill -> Resource.Clay;
            default -> null;
        };
    }
}
