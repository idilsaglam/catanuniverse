/*
	Idil Saglam
	Abdulrahim Toto
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

    public boolean isSettingsValid() {
        if (this.getComponentCount() == 1 && this.getComponent(0) instanceof GameSettingsPane) {
            System.out.println("HERE");
            return ((GameSettingsPane<? extends GameSettings>) this.getComponent(0))
                    .isSettingsValid();
        }
        return false;
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