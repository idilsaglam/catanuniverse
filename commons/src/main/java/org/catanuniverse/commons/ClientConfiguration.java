/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.commons;

import org.catanuniverse.core.models.Player;

public class ClientConfiguration {

    private GameSettings gameSettings;
    private Player[] players;

    public ClientConfiguration() {
        this.gameSettings = null;
        this.players = null;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public void setGameSettings(GameSettings settings) {
        this.gameSettings = settings;
    }
}
