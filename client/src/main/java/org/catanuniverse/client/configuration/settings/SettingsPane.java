/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.client.configuration.settings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.catanuniverse.commons.GameSettings;
import org.catanuniverse.commons.GameType;

public class SettingsPane extends JPanel {

    private GameType gameType;

    private Consumer<GameSettings> onGameSettingsChanged;

    /**
     * Creates a settings pane with a given game type
     *
     * @param gameType The game type related to the settings pane
     */
    public SettingsPane(GameType gameType) {
        this.gameType = gameType;
        this.setLayout(new GridLayout(1, 0));
        this.build();
    }

    /**
     * Updates the game type related to the setting pane
     *
     * @param gameType The new game type
     */
    public void changeGameType(GameType gameType) {
        this.gameType = gameType;
        this.build();
    }

    /**
     * Updates the onGameSettingsChanged listener in the settings pane
     *
     * @param onGameSettingsChanged The new onGameSettingsChanged listener
     */
    public void setOnGameSettingsChanged(Consumer<GameSettings> onGameSettingsChanged) {
        this.onGameSettingsChanged = onGameSettingsChanged;
        if (this.getComponentCount() == 1 && this.getComponent(0) instanceof GameSettingsPane) {
            GameSettingsPane<? extends GameSettings> pane =
                    (GameSettingsPane<? extends GameSettings>) this.getComponent(0);
            pane.setOnGameSettingsChanged(onGameSettingsChanged);
        }
    }

    /**
     * Verify if settings are valid
     *
     * @return True if settings related to the settings pane is valid
     */
    public boolean isSettingsValid() {
        if (this.getComponentCount() == 1 && this.getComponent(0) instanceof GameSettingsPane) {
            System.out.println("HERE");
            return ((GameSettingsPane<? extends GameSettings>) this.getComponent(0))
                    .isSettingsValid();
        }
        return false;
    }

    /**
     * Returns the game settings related to te settings pane
     *
     * @return Game settings related to the settings pane
     */
    public GameSettings getSettings() {
        if (this.getComponentCount() == 1) {
            GameSettingsPane<? extends GameSettings> settingsPane =
                    (GameSettingsPane<? extends GameSettings>) this.getComponent(0);
            return settingsPane.getSettings();
        }
        return null;
    }

    /** Build view components and add them to the current container */
    private void build() {
        // Check if there's a settings component created previously
        if (this.getComponentCount() == 1) {
            // Remove the second component which is the settings after the title
            this.remove(0);
            this.revalidate();
            this.repaint();
        }
        System.out.printf("Game type %s\n", this.gameType);
        this.add(
                this.gameType == null
                        ? this.buildSelectGameTypeMessage()
                        : switch (this.gameType) {
                            case LOCAL -> new LocalSettingsPane(this.onGameSettingsChanged);
                            case MP_GUEST -> new MPGuestSettingsPane(this.onGameSettingsChanged);
                            case MP_HOST -> new MPHostSettingsPane(this.onGameSettingsChanged);
                        },
                BorderLayout.CENTER);
        System.out.printf("Repainting with %d components\n", this.getComponentCount());
        this.revalidate();
        this.repaint();
    }

    /** Build view elements for the select game type message */
    private JPanel buildSelectGameTypeMessage() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Game type is required. Please select the type of the game");
        label.setForeground(Color.RED);
        panel.add(label);
        return panel;
    }
}
