/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.client;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import org.catanuniverse.client.configuration.ConfigurationPane;
import org.catanuniverse.client.game.GameBoard;
import org.catanuniverse.commons.GameSettings;
import org.catanuniverse.commons.LocalGameSettings;
import org.catanuniverse.core.game.Player;

public class MainFrame extends JFrame {

    private GameSettings gameSettings;
    private final ConfigurationPane configurationPane;
    private Dimension size;
    private Point position;
    private Rectangle defaultSize;

    public MainFrame() {
        this.init();
        this.configurationPane = new ConfigurationPane(this::setGameSettings);

        this.build();
        super.setVisible(true);
    }

    /** Initialize the main frame's properties */
    private void init() {
        super.setTitle("Catanuniverse");
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.initSizes();
    }

    /** Initialize the position and the size of the current frame */
    private void initSizes() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // Initialise the dimension of the screen
        this.size = new Dimension(screenSize.width / 2, screenSize.height / 2);
        // The frame should not be smaller than this size
        super.setMinimumSize(this.size);
        // Set the position attribute of the frame
        this.position = new Point(screenSize.width / 4, screenSize.height / 4);
        // the maximum size of the screen should not be bigger than the screen size
        super.setMaximumSize(screenSize);
        // Set the JFrame bound with the given position and the given size
        defaultSize =
                new Rectangle(this.position.x, this.position.y, this.size.width, this.size.height);
        super.setBounds(defaultSize);
    }

    /** Maximise the current frame for the entire screen */
    private void maximise() {
        // Get the current screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // Update the position. The position should be top left corner of the screen
        this.position = new Point((screenSize.width) / 2, (screenSize.height) / 2);
        // The frame size should be the screen size
        this.size = screenSize;
        // Update the bounds of the current frame
        super.setBounds(0, 0, screenSize.width, screenSize.height);
        // Update the minimum frame size
        super.setMinimumSize(this.size);
        // Update the maximum frame size
        super.setMaximumSize(this.size);
    }

    /**
     * Updates the configuration of the current game
     *
     * @param gameSettings The configuration object to update with
     */
    private void setGameSettings(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
        this.setVisible(false);
        this.maximise();
        this.gameSettings.completePlayers();
        try {
            this.loadGameBoard();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Load the game board after the configuration */
    private void loadGameBoard() throws IOException {
        if (this.gameSettings instanceof LocalGameSettings) {
            JWindow window = new JWindow();
            window.setBounds(defaultSize);
            window.setVisible(true);
            GameBoard gameBoard =
                    new GameBoard(this.getSafeAreaSize(), this.gameSettings, this::onGameEnd);
            window.setVisible(false);
            this.setVisible(true);
            window.dispose();
            super.setContentPane(gameBoard);
            super.revalidate();
            super.repaint();
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Due to time restrictions, this feature is under active development",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
        /*        // Create a dialog
        JDialog d = new JDialog(this, "Dialog box");
        // Create a label
        JLabel l = new JLabel("This is a dialog box.");
        // Add the label to the dialog box
        d.add(l);
        // Set the size of the dialog box
        d.setSize(200, 100);
        // Set the visibility of the dialog box
        d.setVisible(true);*/
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

    /**
     * Method handles the end of game callback
     *
     * @param player The winner
     */
    private void onGameEnd(Player player) {
        this.setVisible(false);
        JOptionPane.showMessageDialog(
                this,
                String.format("%s won!", player.getUsername()),
                "End of game",
                JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    /** Adds the current screen to the contentPane with current configuration state */
    private void build() {
        if (this.gameSettings == null) {
            // If the configuration is null, we need to add configuration pane to the screen
            super.setContentPane(this.configurationPane);
        }
        // configuration
    }
}
