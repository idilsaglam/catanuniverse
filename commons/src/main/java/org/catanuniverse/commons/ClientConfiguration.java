/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.commons;

import org.catanuniverse.core.models.Player;

public class ClientConfiguration {

    private GameSettings gameSettings;
    private Player[] players;

    /**
     * Creates a new client configuration with all attributes null
     */
    public ClientConfiguration() {
        this.gameSettings = null;
        this.players = null;
    }

    /**
     * Get the players array in the current configuration
     * @return The array of Player object in the current configuration
     */
    public Player[] getPlayers() {
        return this.players;
    }

    /**
     * Get the current game settings
     * @return Game settings of the current configuration
     */
    public GameSettings getGameSettings() { return this.gameSettings; }

    /**
     * Update player array with a new one
     * @param players New players array to update the current players array
     */
    public void setPlayers(Player[] players) {
        this.players = players;
    }

    /**
     * Update game settings with new one
     * @param settings New game settings
     */
    public void setGameSettings(GameSettings settings) {
        this.gameSettings = settings;
    }

    /**
     * Checks if the current configuration is an online game or not
     * @return True if the current client configuration is for an online game, false if not
     */
    public boolean isOnline() {
        return ((this.gameSettings instanceof MultiPlayerHostGameSettings) || (this.gameSettings instanceof MultiPlayerGuestGameSettings));
    }
}
