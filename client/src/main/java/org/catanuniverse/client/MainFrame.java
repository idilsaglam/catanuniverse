/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import org.catanuniverse.client.configuration.ConfigurationPane;
import org.catanuniverse.commons.ClientConfiguration;

public class MainFrame extends JFrame {

    private ClientConfiguration configuration;
    private final ConfigurationPane configurationPane;

    public MainFrame() {
        this.init();
        this.configurationPane = new ConfigurationPane(this::setConfiguration);
        this.build();
        super.setVisible(true);
    }

    /** Initialize the main frame's properties */
    private void init() {
        super.setTitle("Catanuniverse");
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // TODO: Change the minimum size
        super.setMinimumSize(new Dimension(200, 200));
        super.setBounds(
                screenSize.width / 4,
                screenSize.height / 4,
                screenSize.width / 2,
                screenSize.height / 2);
        super.setMaximumSize(screenSize);
    }

    /**
     * Updates the configuration of the current game
     *
     * @param configuration The configuration object to update with
     */
    private void setConfiguration(ClientConfiguration configuration) {
        this.configuration = configuration;
        System.out.println("Configuration updated");
        // TODO: Change contentPane with game pane with given configuration
    }

    /**
     * Returns the usable screen size of the main frame
     *
     * @return Dimensions of the usable screen size
     */
    private Dimension getSafeAreaSize() {
        return new Dimension(
                super.getWidth() - super.getInsets().right - super.getInsets().left,
                super.getHeight() - super.getInsets().top - super.getInsets().bottom);
    }

    /** Adds the current screen to the contentPane with current configuration state */
    private void build() {
        if (this.configuration == null) {
            // If the configuration is null, we need to add configuration pane to the screen
            super.setContentPane(this.configurationPane);
        }
        // TODO: If the configuration is not null, we need to compute the screen with given
        // configuration
    }
}
