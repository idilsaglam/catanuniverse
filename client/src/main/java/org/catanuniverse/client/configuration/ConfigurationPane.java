/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.client.configuration;

import java.awt.BorderLayout;
import java.util.function.Consumer;
import javax.swing.JPanel;
import org.catanuniverse.commons.ClientConfiguration;

public class ConfigurationPane extends JPanel {

    public ConfigurationPane(Consumer<ClientConfiguration> callback) {
        ConfigurationForm configurationForm = new ConfigurationForm();
        configurationForm.setOnConfigurationSaved(callback);
        this.add(configurationForm, BorderLayout.CENTER);
    }
}
