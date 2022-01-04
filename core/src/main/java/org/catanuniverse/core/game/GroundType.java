/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

import java.util.Random;

public enum GroundType {
    Forest(Resource.Wood),
    Hill(Resource.Clay),
    Meadow(Resource.Wool),
    Farm(Resource.Corn),
    Mountain(Resource.Mineral),
    // TODO: Update the resource with water or something else
    Water(null),
    Desert(null);

    private final Resource resource;

    GroundType(Resource resource) {
        this.resource = resource;
    }

    /**
     * Returns the Resources produced by this ground type
     *
     * @return The resource produced by this ground type
     */
    protected Resource produces() {
        return this.resource;
    }

    public int getColor() {
        return switch (this) {
            case Water -> 0x5D9BC2;
            case Forest -> 0xFEC437;
            case Hill -> 0xB3C92F;
            case Meadow -> 0x19DCCD;
            case Farm -> 0xCD7730;
            case Mountain -> 0xF2D2A9;
            case Desert -> 0x4B7922;
        };
    }
    // Black color code 0x000000
    public static GroundType random(boolean desertExists) {
        Random r = new Random();
        return switch (r.nextInt(7)) {
            case 0 -> GroundType.Forest;
            case 1 -> GroundType.Hill;
            case 2 -> GroundType.Meadow;
            case 3 -> GroundType.Farm;
            case 4 -> GroundType.Mountain;
            // TODO: There should be only one desert
            default -> desertExists ? random(true) : GroundType.Desert;
        };
    }
}
