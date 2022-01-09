/*
	22015094 - Idil Saglam*/
package org.catanuniverse.core.game;

abstract class Pin {
    protected final Player owner;

    protected Pin(Player owner) {
        this.owner = owner;
    }

    /**
     * Get the owner of the pin
     *
     * @return The player who owns the current pin
     */
    public Player getOwner() {
        return this.owner;
    }
}
