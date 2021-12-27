/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.client.configuration;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import javax.swing.JPanel;
import org.catanuniverse.client.configuration.settings.SettingsPane;
import org.catanuniverse.commons.ClientConfiguration;
import org.catanuniverse.commons.GameSettings;
import org.catanuniverse.commons.GameType;
import org.catanuniverse.commons.LocalGameSettings;

class ConfigurationForm extends JPanel {

    private final PlayersInputContainer playersInputContainer;
    private final GameTypeSelector gameTypeSelector;
    private final SettingsPane settingsPane;
    private final Consumer<ClientConfiguration> onConfigurationSaved;

    private final StartGameButton startButton;

    public ConfigurationForm(Consumer<ClientConfiguration> onConfigurationSaved) {
        GridBagConstraints constraints = new GridBagConstraints();

        this.setLayout(new GridBagLayout());
        this.onConfigurationSaved = onConfigurationSaved;
        this.playersInputContainer = new PlayersInputContainer(0);

        this.settingsPane = new SettingsPane(null);
        this.startButton = new StartGameButton();

        this.settingsPane.setOnGameSettingsChanged(
                (GameSettings settings) -> {
                    System.out.println("Game settings changed");
                    this.startButton.setEnabled(this.settingsPane.isSettingsValid());
                    if (settings instanceof LocalGameSettings) {
                        LocalGameSettings localGameSettings = (LocalGameSettings)settings;
                        int nbP = localGameSettings.getCapacity() - localGameSettings.getNumberOfAI();
                      System.out.printf("Local game settings changed the number of valid players is %d\n", nbP);
                        this.playersInputContainer.setNumberOfPlayers(nbP);
                    }
                });

        this.gameTypeSelector =
                new GameTypeSelector(
                        (GameType type) -> {
                            System.out.printf("Selected game type %s\n", type);
                            // TODO: Update client configuration object
                            this.settingsPane.changeGameType(type);
                            this.startButton.setGameType(type);
                            this.startButton.setEnabled(this.settingsPane.isSettingsValid());
                            if (type == GameType.LOCAL) {
                                this.playersInputContainer.setNumberOfPlayers(4);
                            } else {
                                this.playersInputContainer.setNumberOfPlayers(1);
                            }
                        });

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
}
