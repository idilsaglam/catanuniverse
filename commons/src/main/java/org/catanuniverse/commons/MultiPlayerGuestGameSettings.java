/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.commons;

import java.net.URI;

public class MultiPlayerGuestGameSettings extends GameSettings {

    private URI serverAddress;

    public MultiPlayerGuestGameSettings() {
        this.serverAddress = null;
    }

    public void setServerAddress(URI serverAddress) {
        this.serverAddress = serverAddress;
    }

    public URI getServerAddress() {
        return this.serverAddress;
    }

    public void connect() {
        // FIXME: Connect to the given server
    }
}
