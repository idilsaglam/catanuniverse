/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.commons;

public class LocalGameSettings extends GameSettings {


    /**
     * Create a local game settings with given capcity
     *
     * @param capacity The capacity of the local game room
     */
    public LocalGameSettings(int capacity) {
        super(capacity, capacity);
    }

    /**
     * create a local game settings with given capacity and number of Ais
     *
     * @param capacity the capacity of the game room
     * @param nbOfAI The number of AIs in the game room
     */
    public LocalGameSettings(int capacity, int nbOfAI) {
        super(capacity,  nbOfAI);
    }

    @Override
    public int getNumberOfRequestedPlayers() {
        return this.capacity - this.numberOfAI;
    }

    @Override
    public String toString() {
        return String.format("LocalGameSettings\n%s", super.toString());
    }

    @Override
    public void start() {
        // TODO: Start local game
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public boolean isValid() {
        return super.isValid();
    }
}
