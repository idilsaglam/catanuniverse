/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.commons;

public class MultiPlayerHostGameSettings extends GameSettings {

    private int portNumber;

    public MultiPlayerHostGameSettings() {
        super();
        this.portNumber = -1;
    }

    public MultiPlayerHostGameSettings(int capacity, int numberOfAI, int portNumber) {
        super(capacity, numberOfAI);
        this.portNumber = portNumber;
    }

    @Override
    public int getNumberOfRequestedPlayers() {
        return 1;
    }

    public MultiPlayerHostGameSettings(int capacity, int numberOfAI) {
        this(capacity, numberOfAI, -1);
    }

    @Override
    public boolean isValid() {
        return super.isValid() && this.portNumber != -1;
    }

    public int getPortNumber() {
        return this.portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    @Override
    public void start() {
        // FIXME: Complete
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public void completePlayers() {

    }

    @Override
    public String toString() {
        return String.format(
                "MultiPlayerHostGameSettings\nPort number: %d\n%s",
                this.portNumber, super.toString());
    }
}
