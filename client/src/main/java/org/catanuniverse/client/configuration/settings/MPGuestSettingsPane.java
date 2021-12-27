/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.client.configuration.settings;

import java.net.URI;
import java.util.function.Consumer;
import org.catanuniverse.commons.GameSettings;
import org.catanuniverse.commons.MultiPlayerGuestGameSettings;

final class MPGuestSettingsPane extends GameSettingsPane<MultiPlayerGuestGameSettings> {

    private ServerAddressSelector serverAddressSelector;

    protected MPGuestSettingsPane(Consumer<GameSettings> onGameSettingsChanged) {
        super(onGameSettingsChanged);
        super.settings = new MultiPlayerGuestGameSettings();
        this.serverAddressSelector =
                new ServerAddressSelector(
                        (URI uri) -> {
                            System.out.println("Server address changed");
                            super.settings.setServerAddress(uri);
                            super.onGameSettingsChanged.accept(super.settings);
                        });
        this.add(this.serverAddressSelector);
    }

    @Override
    boolean isSettingsValid() {
        return this.serverAddressSelector.isServerAddressValid();
    }
}
