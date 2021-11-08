/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

enum GroundType {
    Forest(Resource.Wood),
    Hill(Resource.Corn),
    Meadow(Resource.Mineral),
    Farm(Resource.Wool),
    Mountain(Resource.Clay),
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
