/*
	Idil Saglam
	Abdulrahim Toto
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

    protected GameSettingsPane(Consumer<GameSettings> onGameSettingsChanged) {
        this.onGameSettingsChanged = onGameSettingsChanged;
        this.setLayout(new GridLayout(0, 1));
    }

    protected void add(JLabel label, JComponent component) {
        this.add(label);
        this.add(component);
    }

    protected void setOnGameSettingsChanged(Consumer<GameSettings> onGameSettingsChanged) {
        this.onGameSettingsChanged = onGameSettingsChanged;
    }

    protected GameSettings getSettings() {
        return this.settings;
    }

    abstract boolean isSettingsValid();
}
