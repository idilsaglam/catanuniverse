/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.configuration;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import javax.swing.JPanel;
import org.catanuniverse.client.configuration.settings.SettingsPane;
import org.catanuniverse.commons.GameSettings;
import org.catanuniverse.commons.GameType;
import org.catanuniverse.commons.LocalGameSettings;
import org.catanuniverse.core.game.Player;

class ConfigurationForm extends JPanel {

    private final PlayersInputContainer playersInputContainer;
    private final GameTypeSelector gameTypeSelector;
    private final SettingsPane settingsPane;
    private final StartGameButton startButton;
    private ConfigurationFormController controller;
    private Consumer<GameSettings> onSaved;

    public ConfigurationForm() {
        this(null);
    }

    public ConfigurationForm(Consumer<GameSettings> onSaved) {
        this.onSaved = onSaved;
        GridBagConstraints constraints = new GridBagConstraints();
        this.controller = new ConfigurationFormController(this);
        this.setLayout(new GridBagLayout());
        this.playersInputContainer = new PlayersInputContainer(0);

        this.settingsPane = new SettingsPane(null);
        this.startButton = new StartGameButton();

        this.startButton.addActionListener(controller::startButtonListener);

        this.playersInputContainer.setOnPlayersUpdated(controller::playersInputContainerUpdatedListener);

        this.settingsPane.setOnGameSettingsChanged(controller::gameSettingsPaneListener);

        this.gameTypeSelector =
            new GameTypeSelector(controller::gameTypeSelectedListener);

        constraints.gridx = 0;
        constraints.gridy = 0;
        this.add(this.playersInputContainer, constraints);
        constraints.gridy = 1;
        this.add(this.gameTypeSelector, constraints);
        constraints.gridy = 2;
        this.add(this.settingsPane, constraints);
        constraints.gridy = 3;
        this.add(this.startButton, constraints);
    }

    /**
     * Update the on configuration saved callback
     *
     * @param callback The callback function to add
     */
    public void setOnSaved(Consumer<GameSettings> callback) {
        this.onSaved = callback;
    }

    SettingsPane getSettingsPane() {
      return this.settingsPane;
    }

    StartGameButton getStartButton() {
      return this.startButton;
    }

    PlayersInputContainer getPlayersInputContainer() {
      return this.playersInputContainer;
    }
}
