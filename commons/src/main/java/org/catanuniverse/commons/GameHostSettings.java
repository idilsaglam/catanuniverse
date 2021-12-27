/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.commons;

public abstract class GameHostSettings extends GameSettings {

    protected int capacity;

    protected int numberOfAI;
    protected Difficulty difficulty;

    GameHostSettings() {
        this(-1, 0, null);
    }

    GameHostSettings(int capacity, int numberOfAI, Difficulty difficulty) {
        this.capacity = capacity;
        this.numberOfAI = numberOfAI;
        this.difficulty = difficulty;
    }

    GameHostSettings(int capacity) {
        this(capacity, 0, null);
    }

    GameHostSettings(int capacity, int nbOfAI) {
        this(capacity, nbOfAI, null);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setNumberOfAI(int numberOfAI) {
        this.numberOfAI = numberOfAI;
    }

    public void setAiDifficulty(Difficulty aiDifficulty) {
        this.difficulty = aiDifficulty;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public int getNumberOfAI() {
        return this.numberOfAI;
    }

    public Difficulty getAiDifficulty() {
        return this.difficulty;
    }

    abstract void start();
}
