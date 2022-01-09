/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.client.configuration;

import java.awt.BorderLayout;
import java.util.function.Consumer;
import javax.swing.JPanel;
import org.catanuniverse.commons.GameSettings;

public class ConfigurationPane extends JPanel {

    /**
     * Create a configuration pane with a callback function
     *
     * @param callback A callback function which will be ran when the configuration is saved
     */
    public ConfigurationPane(Consumer<GameSettings> callback) {
        ConfigurationForm configurationForm = new ConfigurationForm();
        configurationForm.setOnSaved(callback);
        this.add(configurationForm, BorderLayout.CENTER);
    }
}
