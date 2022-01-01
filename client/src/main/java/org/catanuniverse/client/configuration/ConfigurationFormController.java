/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.configuration;

import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import org.catanuniverse.commons.GameSettings;
import org.catanuniverse.commons.GameType;
import org.catanuniverse.commons.LocalGameSettings;
import org.catanuniverse.commons.MultiPlayerGuestGameSettings;
import org.catanuniverse.commons.MultiPlayerHostGameSettings;
import org.catanuniverse.core.game.Player;

class ConfigurationFormController {

    private final ConfigurationForm configurationForm;
    private GameSettings settings;
    private Consumer<GameSettings> onSavedCallback;

    public ConfigurationFormController(
            ConfigurationForm configurationForm, Consumer<GameSettings> onSaved) {
        this.configurationForm = configurationForm;
        this.onSavedCallback = onSaved;
    }

    public ConfigurationFormController(ConfigurationForm configurationForm) {
        this(configurationForm, null);
    }

    void setOnSavedCallback(Consumer<GameSettings> onSavedCallback) {
        this.onSavedCallback = onSavedCallback;
    }

    void startButtonListener(ActionEvent event) {
        System.out.printf(
                "Start button clicked. Current client configuration %s\n",
                this.settings.toString());
        this.callCallback();
    }

    void playersInputContainerUpdatedListener(Player[] players) {
        this.settings.setPlayers(players);
        this.updateStartButtonEnabled();
    }

    void gameTypeSelectedListener(GameType type) {
        System.out.printf("Selected game type %s\n", type);
        this.configurationForm.getSettingsPane().changeGameType(type);
        this.configurationForm.getStartButton().setGameType(type);
        this.updateSettings(type);
    }

    void gameSettingsPaneListener(GameSettings settings) {
        System.out.println("Game settings changed");
        if (settings instanceof LocalGameSettings) {
            this.configurationForm
                    .getPlayersInputContainer()
                    .setNumberOfPlayers(settings.getNumberOfRequestedPlayers());
        }
        this.updateSettings(settings);
    }

    private void updateSettings(GameType type) {
        GameSettings oldSettings = this.settings;
        this.settings =
                switch (type) {
                    case LOCAL -> new LocalGameSettings(
                            GameSettings.DEFAULT_CAPACITY, GameSettings.DEFAULT_NUMBER_OF_AI);
                    case MP_GUEST -> new MultiPlayerGuestGameSettings();
                    case MP_HOST -> new MultiPlayerHostGameSettings(
                            GameSettings.DEFAULT_CAPACITY, GameSettings.DEFAULT_NUMBER_OF_AI);
                };
        if (oldSettings != null) {
            this.settings.setPlayers(oldSettings.getPlayers());
        }
        this.updateStartButtonEnabled();
        this.updatePlayersInputContainer();
    }

    private void updateSettings(GameSettings settings) {
        if (settings instanceof MultiPlayerGuestGameSettings) {
            System.out.println("Multi player guest settings detected");
            System.out.println(((MultiPlayerGuestGameSettings) settings).getServerAddress());
        }
        this.settings = this.settings.merge(settings);
        if (this.settings instanceof MultiPlayerGuestGameSettings) {
            System.out.println("Current settings are now multi player host game settings");
            System.out.println(((MultiPlayerGuestGameSettings) this.settings).getServerAddress());
        }
        this.updateStartButtonEnabled();
        this.updatePlayersInputContainer();
    }

    private void updatePlayersInputContainer() {
        this.configurationForm
                .getPlayersInputContainer()
                .setNumberOfPlayers(this.settings.getNumberOfRequestedPlayers());
        this.configurationForm.getPlayersInputContainer().revalidate();
        this.configurationForm.getPlayersInputContainer().repaint();
    }

    private void updateStartButtonEnabled() {
        System.out.printf("Settings are valid ? %b\n", this.settings.isValid());
        this.configurationForm.getStartButton().setEnabled(this.settings.isValid());
        this.configurationForm.getStartButton().revalidate();
        this.configurationForm.getStartButton().repaint();
    }

    private void callCallback() {
        if (this.onSavedCallback == null) {
            return;
        }
        this.onSavedCallback.accept(this.settings);
    }
}
