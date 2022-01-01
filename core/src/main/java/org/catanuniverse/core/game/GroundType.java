/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

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
}
