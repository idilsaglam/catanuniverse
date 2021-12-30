/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.client.configuration;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import javax.swing.JPanel;
import org.catanuniverse.client.configuration.settings.SettingsPane;
import org.catanuniverse.commons.ClientConfiguration;
import org.catanuniverse.commons.GameSettings;
import org.catanuniverse.commons.GameType;
import org.catanuniverse.commons.LocalGameSettings;
import org.catanuniverse.core.game.Player;

class ConfigurationForm extends JPanel {

    private final PlayersInputContainer playersInputContainer;
    private final GameTypeSelector gameTypeSelector;
    private final SettingsPane settingsPane;
    private final StartGameButton startButton;
    private final ClientConfiguration configuration;
    private Consumer<ClientConfiguration> onConfigurationSaved;

    public ConfigurationForm() {
        GridBagConstraints constraints = new GridBagConstraints();

        this.setLayout(new GridBagLayout());
        this.configuration = new ClientConfiguration();
        this.playersInputContainer = new PlayersInputContainer(0);

        this.settingsPane = new SettingsPane(null);
        this.startButton = new StartGameButton();

        this.startButton.addActionListener((ActionEvent ignore) -> {
          if (ConfigurationForm.this.onConfigurationSaved == null) return;
          ConfigurationForm.this.onConfigurationSaved.accept(ConfigurationForm.this.configuration);
        });

        this.playersInputContainer.setOnPlayersUpdated(
                (Boolean playersValid) -> {
                    System.out.printf("Players input container is valid %b\n", playersValid);
                    ConfigurationForm.this.playersInputContainerChanged(playersValid);
                    this.updateClientConfiguration();
                });

        this.settingsPane.setOnGameSettingsChanged(
                (GameSettings settings) -> {
                    System.out.println("Game settings changed");
                    this.startButton.setEnabled(this.settingsPane.isSettingsValid());
                    if (settings instanceof LocalGameSettings) {
                        LocalGameSettings localGameSettings = (LocalGameSettings) settings;
                        int nbP =
                                localGameSettings.getCapacity() - localGameSettings.getNumberOfAI();
                        System.out.printf(
                                "Local game settings changed the number of valid players is %d\n",
                                nbP);
                        this.playersInputContainer.setNumberOfPlayers(nbP);
                    }
                    this.updateClientConfiguration(settings);
                });

        this.gameTypeSelector =
                new GameTypeSelector(
                        (GameType type) -> {
                            System.out.printf("Selected game type %s\n", type);
                            // TODO: Update client configuration object
                            this.settingsPane.changeGameType(type);
                            this.startButton.setGameType(type);
                            this.startButton.setEnabled(this.isConfigurationValid());
                            if (type == GameType.LOCAL) {
                                this.playersInputContainer.setNumberOfPlayers(4);
                            } else {
                                this.playersInputContainer.setNumberOfPlayers(1);
                            }
                            this.updateClientConfiguration();
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

  /**
   * Update the on configuration saved callback
   * @param callback The callback function to add
   */
  public void setOnConfigurationSaved(Consumer<ClientConfiguration> callback) {
      this.onConfigurationSaved = callback;
    }

  /**
   * Checks if configuration is valid
   * @return True if the configuration in the configuration pane is valid
   */
  private boolean isConfigurationValid() {
        return this.playersInputContainer.arePlayersValid() && this.settingsPane.isSettingsValid();
    }

  /**
   * Enables the start button if given players and settings are valid
   * @param playersAreValid Boolean indicating that if players are valid
   */
  private void playersInputContainerChanged(boolean playersAreValid) {
        this.startButton.setEnabled(playersAreValid && this.settingsPane.isSettingsValid());
        this.startButton.repaint();
    }

  /**
   * Updates client configuration with players and settings
   */
  private void updateClientConfiguration() {
        this.configuration.setPlayers(this.playersInputContainer.getPlayers());
        this.configuration.setGameSettings(this.settingsPane.getSettings());
    }

  /**
   * Update client configuration with given settings
   * @param settings The settings to update the client configuration
   */
  private void updateClientConfiguration(GameSettings settings) {
        this.configuration.setPlayers(this.playersInputContainer.getPlayers());
        this.configuration.setGameSettings(settings);
    }

  /**
   * Updates the client configuration with given array of players
   * @param players An array of players to update the client configuration
   */
  private void updateClientConfiguration(Player[] players) {
        this.configuration.setPlayers(players);
        this.configuration.setGameSettings(this.settingsPane.getSettings());
    }

  /**
   * Updates the client configuration with given settings and array of players
   * @param players An array of players
   * @param settings New settings to update client configuration
   */
    private void updateClientConfiguration(Player[] players, GameSettings settings) {
        this.configuration.setPlayers(players);
        this.configuration.setGameSettings(settings);
    }
}
