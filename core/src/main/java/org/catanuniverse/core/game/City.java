/*
	22015094 - Idil Saglam*/
package org.catanuniverse.core.game;

public class City extends Settlement {

    public City(Player owner) {
        super(owner);
    }

    @Override
    public void sendResource(Resource r) {
        this.owner.updateResource(r, 2);
    }
}
