/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.commons;

public class MultiPlayerHostGameSettings extends GameHostSettings {

    private int portNumber;

    public MultiPlayerHostGameSettings() {
        super();
        this.portNumber = -1;
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
}
