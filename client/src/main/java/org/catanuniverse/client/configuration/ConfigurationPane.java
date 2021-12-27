/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.client.configuration;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.function.Consumer;
import javax.swing.JPanel;
import org.catanuniverse.commons.ClientConfiguration;

public class ConfigurationPane extends JPanel {

    private final Consumer<ClientConfiguration> onConfigure;

    public ConfigurationPane(Dimension size, Consumer<ClientConfiguration> onConfigure) {
        this.onConfigure = onConfigure;
        this.add(new ConfigurationForm(this.onConfigure), BorderLayout.CENTER);
    }

    public ConfigurationPane(Consumer<ClientConfiguration> onConfigure) {
        this(null, onConfigure);
    }
}
