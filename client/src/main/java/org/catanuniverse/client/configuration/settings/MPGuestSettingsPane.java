/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.client.configuration.settings;

import java.net.URI;
import java.util.function.Consumer;
import org.catanuniverse.commons.GameSettings;
import org.catanuniverse.commons.MultiPlayerGuestGameSettings;

final class MPGuestSettingsPane extends GameSettingsPane<MultiPlayerGuestGameSettings> {

    private ServerAddressSelector serverAddressSelector;

    /**
     * Creates a new settings pane for multi player guest settings
     *
     * @param onGameSettingsChanged The callback function that called each time the settings are
     *     updated
     */
    protected MPGuestSettingsPane(Consumer<GameSettings> onGameSettingsChanged) {
        super(onGameSettingsChanged);
        super.settings = new MultiPlayerGuestGameSettings();
        this.serverAddressSelector =
                new ServerAddressSelector(
                        (URI uri) -> {
                            
                            super.settings.setServerAddress(uri);
                            super.onGameSettingsChanged.accept(super.settings);
                        });
        this.add(this.serverAddressSelector);
    }

    /**
     * Verify if given settings are valid
     *
     * @return True if the given server address is valid
     */
    @Override
    boolean isSettingsValid() {
        return this.serverAddressSelector.isServerAddressValid();
    }
}
