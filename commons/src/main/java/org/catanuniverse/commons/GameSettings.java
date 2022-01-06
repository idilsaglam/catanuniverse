/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.commons;

import org.catanuniverse.core.game.Player;

public abstract class GameSettings {
    public static int DEFAULT_CAPACITY = 4, DEFAULT_NUMBER_OF_AI = 0;

    protected int capacity;
    protected int numberOfAI;
    protected Difficulty difficulty;
    protected Player[] players;
    protected int currentPlayerIndex = 0;

    public GameSettings() {
        this.capacity = -1;
        this.numberOfAI = -1;
        this.difficulty = null;
        this.players = new Player[0];
    }

    public GameSettings(int capacity, int numberOfAI, Difficulty difficulty) {
        if (numberOfAI >= capacity)
            throw new IllegalArgumentException(
                    "There should be at least one non AI player in the room");
        if ((numberOfAI == 0 && difficulty != null)) {
            throw new IllegalArgumentException(
                    "Difficulty is required only if there's AI players in the room");
        }
        if (numberOfAI != 0 && difficulty == null) difficulty = Difficulty.EASY;
        this.capacity = capacity;
        this.difficulty = difficulty;
        this.numberOfAI = numberOfAI;
        this.players = new Player[this.capacity];
    }

    public GameSettings(int capacity) {
        this(capacity, 0, null);
    }

    public GameSettings(int capacity, int numberOfAI) {
        this(capacity, numberOfAI, numberOfAI == 0 ? null : Difficulty.EASY);
    }

    public void next() {
        this.currentPlayerIndex = (this.currentPlayerIndex + 1)%this.players.length;
    }

    public Player getCurrentPlayer() {
        return this.players[currentPlayerIndex];
    }

    /**
     * Get the room capacity for the current game settings
     *
     * @return The current game capacity
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Get the array of players
     *
     * @return The array of players
     */
    public Player[] getPlayers() {
        return this.players;
    }

    /**
     * Get the number of requested players
     *
     * @return The number of requested players
     */
    public abstract int getNumberOfRequestedPlayers();

    /**
     * update the capacity of the current game room with given capacity
     *
     * @param capacity The new capacity of the game room
     */
    public void setCapacity(int capacity) {
        if (capacity != 3 && capacity != 4) {
            throw new IllegalArgumentException("Game room capacity can be either 3 or 4");
        }
        this.capacity = capacity;
        Player[] oldPlayers = this.players;
        this.players = new Player[this.capacity];
        System.out.printf(
                "Capacity %d Requested players number %d old players capacity %d\n",
                this.capacity, this.getNumberOfRequestedPlayers(), oldPlayers.length);
        System.arraycopy(
                oldPlayers, 0, this.players, 0, Math.min(oldPlayers.length, this.players.length));
    }

    /**
     * Updates the number of AI in the current game room
     *
     * @param numberOfAI The number of AIs in the current game room
     */
    public void setNumberOfAI(int numberOfAI) {
        if (numberOfAI >= this.capacity) {
            throw new IllegalArgumentException("All room should contains at least one real player");
        }
        this.numberOfAI = numberOfAI;
        if (this.numberOfAI != 0 && this.difficulty == null) {
            this.setDifficulty(Difficulty.EASY);
        }
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    /**
     * Updates the difficulty of the AI players in the game room
     *
     * @param difficulty The difficulty of the AI players in the game room
     */
    public void setDifficulty(Difficulty difficulty) {
        if (this.numberOfAI == 0) {
            return;
        }
        this.difficulty = difficulty;
    }

    /**
     * Get the number of AI players in the current game room
     *
     * @return The number of AI players in the current game room
     */
    public int getNumberOfAI() {
        return this.numberOfAI;
    }

    /**
     * Get the difficulty of the game room
     *
     * @return The difficulty of the game room
     */
    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    /**
     * Get the number of non-ai players in the current game room
     *
     * @return The number of non ai players
     */
    @Deprecated
    public int getNumberOfRealPlayers() {
        return this.capacity - this.numberOfAI;
    }

    /**
     * Verify if the game settings are valid or not
     *
     * @return True if game settings are valid, false if not
     */
    public boolean isValid() {
        System.out.printf("Requested players are valid %b\n", this.areRequestedPlayersValid());
        return (this.areRequestedPlayersValid() && this.capacity != -1 && this.areAIsValid());
    }

    @Override
    public String toString() {
        return String.format(
                "Capacity %d\n"
                        + " Number of AI players%d\n"
                        + "Number of real players%d\n"
                        + "Difficulty %s\n"
                        + "Number of requested players%d",
                this.capacity,
                this.numberOfAI,
                this.getNumberOfRealPlayers(),
                this.getDifficulty() == null ? "N/A" : this.getDifficulty().toString(),
                this.getNumberOfRequestedPlayers());
    }

    /**
     * Merge an other game settings instance to the current game settings instance by keeping the
     * same players
     *
     * @param settings The other game settings instance
     * @return The result GameSettings object
     */
    public GameSettings merge(GameSettings settings) {
        GameSettings result = null;
        if (settings instanceof LocalGameSettings) {
            result = new LocalGameSettings(settings.getCapacity(), settings.getNumberOfAI());
        }
        if (settings instanceof MultiPlayerHostGameSettings) {
            result =
                    new MultiPlayerHostGameSettings(
                            settings.getCapacity(), settings.getNumberOfAI());
            ((MultiPlayerHostGameSettings) result)
                    .setPortNumber(((MultiPlayerHostGameSettings) settings).getPortNumber());
        }
        if (settings instanceof MultiPlayerGuestGameSettings) {
            result = new MultiPlayerGuestGameSettings();
            ((MultiPlayerGuestGameSettings) result)
                    .setServerAddress(((MultiPlayerGuestGameSettings) settings).getServerAddress());
        }
        if (result == null) {
            throw new RuntimeException(
                    "Merge failed, game settings to merge should be instance of LocalGameSettings,"
                            + " MultiPlayerHostGameSettings or MultiPlayerGuestGameSettings");
        }
        result.setDifficulty(settings.getDifficulty());
        result.setPlayers(this.players);
        return result;
    }

    public abstract void start();

    public abstract boolean isOnline();

    public abstract void completePlayers();
    /**
     * Verify if requested players are valid
     *
     * @return True if requested players are valid, false if not
     */
    private boolean areRequestedPlayersValid() {
        if (this.getNumberOfRequestedPlayers() == -1
                || this.players == null
                || this.getNumberOfRequestedPlayers() > this.capacity
                || this.getNumberOfRequestedPlayers() + this.numberOfAI > this.capacity) {
            System.out.printf(
                    "Number of requested players %d players null ? %b capacity %d  number of AI"
                            + " %d\n",
                    this.getNumberOfRequestedPlayers(),
                    this.players == null,
                    this.capacity,
                    this.numberOfAI);
            return false;
        }
        for (Player p : this.players) {
            if (p == null || !p.isValid()) {
                System.out.println("Found an invalid player");
                return false;
            }
        }
        return true;
    }

    /**
     * Verify if AIs are valid or not
     *
     * @return True if AIs are valid, false if not
     */
    private boolean areAIsValid() {
        return (this.numberOfAI != -1
                && ((this.numberOfAI == 0 && this.difficulty == null)
                        || (this.numberOfAI > 0 && this.difficulty != null)));
    }

    /**
     * Get the current player index
     * @return The index of the current player
     */
    public int getCurrentPlayerIndex() {
        return this.currentPlayerIndex;
    }
}
