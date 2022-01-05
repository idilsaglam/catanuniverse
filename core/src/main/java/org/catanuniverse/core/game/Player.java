/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

import java.awt.Color;
import java.util.HashMap;
import org.catanuniverse.core.utils.Helpers;

public class Player {
    private static int COUNTER = 0;
    private String username;
    private Color color;
    private final HashMap<Achievements, Integer> achievements;
    public final int uid;
    /**
     * Create a player with the given username
     *
     * @param username The username of the player to create
     */
    public Player(String username) {
        this.username = username;
        this.color = Helpers.randomColor();
        this.achievements = new HashMap<>();
        this.uid = Player.COUNTER++;
        System.out.println("PLAYER UID " + this.uid);
        for (Achievements a: Achievements.values()) {
            this.achievements.put(a, 0);
        }
    }

    /** Create a player instance with an empty username */
    public Player() {
        this(String.format("Auto %d", (int) (Math.random() * 10)));
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

    /**
     * Get the color of the player
     *
     * @return The color related to the current player
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Return the value of the given achivements of the current player
     * @param achivement The achivement to get
     * @return The value of the achivement of the current player
     */
    public Integer getAchievement(Achievements achivement) {
        return this.achievements.get(achivement);
    }

    /**
     * Increase given achievement value with given value
     * @param achievement The achievement to update
     * @param value Value to increase
     */
    public void incrementAchievement(Achievements achievement, int value) {
        this.achievements.put(achievement, this.achievements.get(achievement)+value);
    }

}
