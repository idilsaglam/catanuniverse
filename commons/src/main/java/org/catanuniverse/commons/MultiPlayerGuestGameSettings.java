/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.commons;

import java.net.URI;

public class MultiPlayerGuestGameSettings extends GameSettings {

    private URI serverAddress;

    /** Create a multi player guest game settings */
    public MultiPlayerGuestGameSettings() {
        super(1, 0);
        this.serverAddress = null;
    }

    @Override
    public boolean isValid() {
        
        return super.isValid() && this.serverAddress != null;
    }

    /**
     * Updates the server address
     *
     * @param serverAddress The new server address to update with
     */
    public void setServerAddress(URI serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * Return the server address
     *
     * @return The URI of the server to connect
     */
    public URI getServerAddress() {
        return this.serverAddress;
    }

    @Override
    public String toString() {
        return String.format("MultiPlayerGuestGameSettings\nServer address %s", this.serverAddress);
    }

    @Override
    public void start() {
        // FIXME: Connect to the given server
    }

    @Override
    public boolean isOnline() {
        return true;
    }

    @Override
    public void completePlayers() {}

    @Override
    public int getNumberOfRequestedPlayers() {
        return 1;
    }
}
