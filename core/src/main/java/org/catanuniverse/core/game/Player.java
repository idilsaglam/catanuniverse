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
    private final HashMap<Resource, Integer> resources;
    public final int uid;
    private final boolean ai;
    private static final int DEFAULT_RESOURCE_VALUE = 100;
    private int nbSettlement,nbRoad;
    /**
     * Create a player with the given username&
     *
     * @param username The username of the player to create
     */
    public Player(String username, boolean isAI) {
        this.username = username;
        this.color = Helpers.randomColor();
        this.achievements = new HashMap<>();
        this.resources = new HashMap<>();

        this.uid = Player.COUNTER++;
        System.out.println("PLAYER UID " + this.uid);
        for (Achievements a: Achievements.values()) {
            this.achievements.put(a, 0);
        }
        for (Resource r: Resource.values()) {
            this.resources.put(r, Player.DEFAULT_RESOURCE_VALUE);
        }
        this.ai = isAI;
        this.nbRoad = 2;
        this.nbSettlement = 2;
    }

    public Player(String username) {
        this(username, false);
    }

    /** Create a player instance with an empty username */
    public Player() {
        this(String.format("Auto %d", (int) (Math.random() * 10)), false);
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

    public boolean isAI() {
        return this.ai;
    }

    /**
     * @return true if user can build a road false if not.
     */
    public boolean canBuildRoad(){
        return this.nbRoad>0||(this.resources.get(Resource.Wood) >= 1 && this.resources.get(Resource.Clay) >=1);
    }

    /**
     * @return true if user can build a settlement false if not.
     */
    public boolean canBuildSettlement(){
        return this.nbRoad>0||(canBuildRoad() && this.resources.get(Resource.Corn) >= 1 && this.resources.get(Resource.Wool)>=1);
    }

    /**
     * @return true if user can build a city false if not.
     */
    public boolean canBuildCity(){
        return (this.resources.get(Resource.Corn) >=2 && this.resources.get(Resource.Mineral) >=3);
    }

    /**
     * @return true if user can buy a developpment card false if not.
     */
    public boolean canBuyDeveloppementCard(){
        return this.resources.get(Resource.Mineral)>=1 && this.resources.get(Resource.Wool)>=1 && this.resources.get(Resource.Corn) >=1;
    }

    /**
     * Function builds a road by decreasing necessary resources.
     */
    public void buildRoad(){
        if(canBuildRoad()){
            if(nbRoad > 0){
                this.nbRoad--;
                return;
            }
            this.resources.put(Resource.Wood,this.resources.get(Resource.Wood)-1);
            this.resources.put(Resource.Clay,this.resources.get(Resource.Clay)-1);
        }
    }

    /**
     * Function builds a settlement by decreasing necessary resources.
     */
    public void buildSettlement(){
        if(canBuildSettlement()){
            if(nbRoad > 0){
                this.nbRoad--;
                return;
            }
            this.resources.put(Resource.Wood,this.resources.get(Resource.Wood)-1);
            this.resources.put(Resource.Wood,this.resources.get(Resource.Clay)-1);
            this.resources.put(Resource.Wood,this.resources.get(Resource.Wool)-1);
            this.resources.put(Resource.Wood,this.resources.get(Resource.Mineral)-1);
        }
    }

    /**
     * Function builds a city by decreasing necessary resources.
     */
    public void buildCity(){
        if(canBuildCity()){
            this.resources.put(Resource.Wood,this.resources.get(Resource.Mineral)-3);
            this.resources.put(Resource.Wood,this.resources.get(Resource.Clay)-2);
        }
    }
    /**
     * Function buys a development card by decreasing necessary resources.
     */
    public void buyDeveloppementCard(){
        if(canBuyDeveloppementCard()){
            this.resources.put(Resource.Wood,this.resources.get(Resource.Wool)-1);
            this.resources.put(Resource.Wood,this.resources.get(Resource.Corn)-1);
            this.resources.put(Resource.Wood,this.resources.get(Resource.Mineral)-1);

        }
    }

    /**
     * Update a resource with given value
     * @param r The resource to update
     * @param i The value to change the resource
     */
    public void updateResource(Resource r, int i) {
        this.resources.put(r, this.resources.get(r) + i);
    }
}
