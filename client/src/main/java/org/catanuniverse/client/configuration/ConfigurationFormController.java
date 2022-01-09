/*
	22015094 - Idil Saglam
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

    /**
     * Creates a new configuration form controller
     *
     * @param configurationForm The configuration form
     * @param onSaved The onSaved callback which will be called when configuration is saved
     */
    public ConfigurationFormController(
            ConfigurationForm configurationForm, Consumer<GameSettings> onSaved) {
        this.configurationForm = configurationForm;
        this.onSavedCallback = onSaved;
    }

    /**
     * Creates a new configuration form controller
     *
     * @param configurationForm The configuration form related to the controller
     */
    public ConfigurationFormController(ConfigurationForm configurationForm) {
        this(configurationForm, null);
    }

    /**
     * Updates the on saved callback function
     *
     * @param onSavedCallback The new callback function
     */
    void setOnSavedCallback(Consumer<GameSettings> onSavedCallback) {
        this.onSavedCallback = onSavedCallback;
    }

    /**
     * Handles the start button click event
     *
     * @param event The action event related to the click on the button click
     */
    void startButtonListener(ActionEvent event) {

        this.callCallback();
    }

    /**
     * Listener function to handle each time the players input container is updated
     *
     * @param players The array of players from the players input container
     */
    void playersInputContainerUpdatedListener(Player[] players) {
        this.settings.setPlayers(players);
        this.updateStartButtonEnabled();
    }

    /**
     * Change listener for the game type
     *
     * @param type The new game type
     */
    void gameTypeSelectedListener(GameType type) {

        this.configurationForm.getSettingsPane().changeGameType(type);
        this.configurationForm.getStartButton().setGameType(type);
        this.updateSettings(type);
    }

    /**
     * listener for the game settings pane
     *
     * @param settings The updated game settings
     */
    void gameSettingsPaneListener(GameSettings settings) {

        if (settings instanceof LocalGameSettings) {
            this.configurationForm
                    .getPlayersInputContainer()
                    .setNumberOfPlayers(settings.getNumberOfRequestedPlayers());
        }
        this.updateSettings(settings);
    }

    /**
     * Update game settings for the given game type
     *
     * @param type The new game type
     */
    private void updateSettings(GameType type) {
        GameSettings oldSettings = this.settings;
        this.settings =
                switch (type) {
                    case LOCAL -> new LocalGameSettings(
                            GameSettings.DEFAULT_CAPACITY,
                            GameSettings.DEFAULT_NUMBER_OF_AI,
                            GameSettings.DEFAULT_VICTORY_POINTS);
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

    /**
     * Update game settings with an other game settings
     *
     * @param settings The new game settings
     */
    private void updateSettings(GameSettings settings) {
        if (settings instanceof MultiPlayerGuestGameSettings) {}

        this.settings = this.settings.merge(settings);
        if (this.settings instanceof MultiPlayerGuestGameSettings) {}

        this.updateStartButtonEnabled();
        this.updatePlayersInputContainer();
    }

    /** Updates players input container */
    private void updatePlayersInputContainer() {
        this.configurationForm
                .getPlayersInputContainer()
                .setNumberOfPlayers(this.settings.getNumberOfRequestedPlayers());
        this.configurationForm.getPlayersInputContainer().revalidate();
        this.configurationForm.getPlayersInputContainer().repaint();
    }

    /** Updates the enabled property of the start button */
    private void updateStartButtonEnabled() {

        this.configurationForm.getStartButton().setEnabled(this.settings.isValid());
        this.configurationForm.getStartButton().revalidate();
        this.configurationForm.getStartButton().repaint();
    }

    /** Function calls the onSaved callback with correct parameters */
    private void callCallback() {
        if (this.onSavedCallback == null) {
            return;
        }
        this.onSavedCallback.accept(this.settings);
    }
}
