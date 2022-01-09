/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.commons;

import org.catanuniverse.core.game.Player;

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
     * @param maxVictoryPoints The maximum of the victory points of the game room
     */
    public LocalGameSettings(int capacity, int nbOfAI, int maxVictoryPoints) {
        super(capacity, nbOfAI, maxVictoryPoints);
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

    @Override
    public void completePlayers() {
        System.out.printf(
            "Room capacity %d\nNumber of requested players: %d\nNumber of AIs: %d\n",
            super.getCapacity(),
            this.getNumberOfRequestedPlayers(),
            this.getNumberOfAI()
        );
        Player[] oldPlayers = this.players;
        this.players = new Player[this.getCapacity()];
        System.arraycopy(oldPlayers, 0, this.players, 0, oldPlayers.length);
        for (int i = 0; i<getNumberOfAI(); i++) {
            this.players[i + this.getNumberOfRequestedPlayers()] = new Player();
        }
    }
}
