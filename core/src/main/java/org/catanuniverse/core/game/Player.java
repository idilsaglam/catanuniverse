/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

public class Player {
    private String username;

    /**
     * Create a player with the given username
     *
     * @param username The username of the player to create
     */
    public Player(String username) {
        this.username = username;
    }

    /** Create a player instance with an empty username */
    public Player() {
        this("");
    }

    /**
     * Get the username of the current player
     *
     * @return The username of the current player
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Verify if the given player is valid or not
     *
     * @return True if the given player is valid, false if not
     */
    public boolean isValid() {
        return this.username.matches("^[a-zA-Z0-9]{4,10}$");
    }
}
