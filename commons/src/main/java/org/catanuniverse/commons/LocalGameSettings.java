/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.commons;

public class LocalGameSettings extends GameHostSettings {

    public LocalGameSettings(int capacity) {
        super(capacity);
    }

    public LocalGameSettings(int capacity, int nbOfAI) {
        super(capacity, nbOfAI);
    }

    @Override
    public void start() {}
}
