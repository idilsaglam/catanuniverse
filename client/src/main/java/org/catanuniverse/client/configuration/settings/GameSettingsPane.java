/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.client.configuration.settings;

import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.catanuniverse.commons.GameSettings;

abstract class GameSettingsPane<T extends GameSettings> extends JPanel {
    protected T settings;
    protected Consumer<GameSettings> onGameSettingsChanged;
    protected static final int DEFAULT_CAPACITY = 4, DEFAULT_NB_AI = 0;

    /**
     * Creates a new game settings pane with the given callback function
     *
     * @param onGameSettingsChanged The callback function that will be called each time the game
     *     settings are changed
     */
    protected GameSettingsPane(Consumer<GameSettings> onGameSettingsChanged) {
        this.onGameSettingsChanged = onGameSettingsChanged;
        this.setLayout(new GridLayout(0, 1));
    }

    /**
     * Adds a label and component to the current pane
     *
     * @param label the label object to add
     * @param component The component to add
     */
    protected void add(JLabel label, JComponent component) {
        this.add(label);
        this.add(component);
    }

    /**
     * Updates the callback function
     *
     * @param onGameSettingsChanged The new callback function to update with
     */
    protected void setOnGameSettingsChanged(Consumer<GameSettings> onGameSettingsChanged) {
        this.onGameSettingsChanged = onGameSettingsChanged;
    }

    /**
     * Returns the game settings related to the settings pane
     *
     * @return The game setting related to the settings pane
     */
    protected GameSettings getSettings() {
        return this.settings;
    }

    /**
     * Verify that if settings are valid
     *
     * @return True if settings are valid, false if not
     */
    abstract boolean isSettingsValid();
}
