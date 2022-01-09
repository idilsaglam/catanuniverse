/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.client.configuration;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import org.catanuniverse.core.game.Player;
import org.catanuniverse.core.utils.EmptyCallback;

class PlayersInputContainer extends JPanel {

    private final PlayerInputContainer[] players;
    private int numberOfPlayers;
    private Consumer<Player[]> onPlayersUpdated;

    /**
     * Creates a new PlayersInputContainer
     *
     * @param labelText Label text
     * @param numberOfPlayers The number of players
     */
    PlayersInputContainer(String labelText, int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.players = new PlayerInputContainer[4];
    }

    /**
     * Creates a new PlayerInputContainer
     *
     * @param numberOfPlayers The number of players
     */
    PlayersInputContainer(int numberOfPlayers) {
        this("Create players", numberOfPlayers);
    }

    /**
     * Updates the number of players
     *
     * @param numberOfPlayers The number of players to update with
     */
    void setNumberOfPlayers(int numberOfPlayers) {
        System.out.printf(
                "Players input container set number of players function called with %d\n",
                numberOfPlayers);
        int old = this.numberOfPlayers;
        int delta = numberOfPlayers - this.numberOfPlayers;
        this.numberOfPlayers = numberOfPlayers;
        this.setLayout(new GridLayout(1, this.numberOfPlayers));
        if (delta < 0) {
            for (int i = 0; i < Math.abs(delta); i++) {
                super.remove(super.getComponentCount() - 1);
            }
        }
        if (delta > 0) {
            for (int i = 0; i < delta; i++) {
                PlayerInputContainer playerContainerToAdd = new PlayerInputContainer(old + i);
                playerContainerToAdd.setOnFocusLost(
                        (String playerName) -> {
                            boolean isUnique =
                                    PlayersInputContainer.this.verifyPlayerNameUnique(playerName);
                            if (isUnique) {
                                playerContainerToAdd.hideErrorMessage();
                                return;
                            }
                            playerContainerToAdd.showErrorMessage("Player names must be unique.");
                        });
                playerContainerToAdd.setOnFocusGained(playerContainerToAdd::hideErrorMessage);
                playerContainerToAdd.setOnChanged(
                        (String value) -> {
                            System.out.printf(
                                    "Player input container updated with text %s\n", value);
                            System.out.printf(
                                    "All player names are unique ? %b\n",
                                    PlayersInputContainer.this.verifyAllPlayerNamesAreUnique());
                            if (PlayersInputContainer.this.onPlayersUpdated == null) {
                                return;
                            }
                            PlayersInputContainer.this.onPlayersUpdated.accept(
                                    PlayersInputContainer.this.getPlayers());
                        });
                this.players[old + i] = playerContainerToAdd;
                this.add(this.players[old + i]);
            }
        }
        super.revalidate();
        super.repaint();
    }

    /**
     * Updates the on players updated callback function
     *
     * @param callback The new callback function
     */
    void setOnPlayersUpdated(Consumer<Player[]> callback) {
        this.onPlayersUpdated = callback;
    }

    /**
     * Returns the array of players from the players input
     *
     * @return The array of players
     */
    Player[] getPlayers() {
        Player[] result = new Player[this.numberOfPlayers];
        for (int i = 0; i < this.numberOfPlayers; i++) {
            result[i] = this.players[i].getPlayer();
        }
        return result;
    }

    /**
     * Checks if players in the input cases are valid or not
     *
     * @return True if players are valid, false if not
     */
    boolean arePlayersValid() {
        if (this.numberOfPlayers == 0) {
            System.out.println("No players to validate in arePlayersValid function");
            return false;
        }
        ;
        boolean result = this.players[0].isPlayerValid();
        System.out.printf("Is first player is valid %b\n", result);
        for (int i = 1; i < this.numberOfPlayers && result; i++) {
            result = this.players[i].isPlayerValid();
            System.out.printf("Player in index %d is valid ? %b\n", i, result);
        }
        return this.verifyAllPlayerNamesAreUnique() && result;
    }

    /**
     * Verify if a given player name appears only once in the array of players
     *
     * @param playerName The player name to check with
     * @return True if the given player name is unique, false if not
     */
    private boolean verifyPlayerNameUnique(String playerName) {
        int occ = 0;
        for (int i = 0; i < this.numberOfPlayers; i++) {
            if (this.players[i].getPlayerName().equals(playerName)) {
                occ++;
            }
            if (occ > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verify if all player names are unique or not
     *
     * @return True if all players have unique names
     */
    private boolean verifyAllPlayerNamesAreUnique() {
        Set<String> playerNames = new HashSet<>();
        for (int i = 0; i < this.numberOfPlayers; i++) {
            playerNames.add(this.players[i].getPlayerName());
        }
        return playerNames.size() == this.numberOfPlayers;
    }

    private static class PlayerInputContainer extends JPanel {

        private final JTextField usernameField;
        private final JLabel errorMessageLabel;
        private Consumer<String> onFocusLost, onChanged;
        private EmptyCallback onFocusGained;

        /**
         * Creates a new PlayerInputContainer with given label text
         *
         * @param labelText The label text to create the player input container with
         */
        PlayerInputContainer(String labelText) {
            JLabel label = new JLabel(labelText);
            label.setHorizontalAlignment(0);
            this.usernameField = new JTextField(10);
            this.errorMessageLabel = new JLabel();
            this.errorMessageLabel.setVisible(false);
            this.errorMessageLabel.setForeground(Color.RED);

            this.setLayout(new GridLayout(3, 1));
            this.add(label);
            this.usernameField.setInputVerifier(
                    new InputVerifier() {
                        @Override
                        public boolean verify(JComponent input) {
                            String text = ((JTextField) input).getText();
                            boolean valid = text.matches("^[a-zA-Z0-9]{4,10}$");
                            PlayerInputContainer.this.errorMessageLabel.setVisible(!valid);
                            if (valid) {
                                PlayerInputContainer.this.errorMessageLabel.repaint();
                                return true;
                            }
                            PlayerInputContainer.this.errorMessageLabel.setText(
                                    String.format("text %s is not valid.", text));
                            PlayerInputContainer.this.errorMessageLabel.repaint();
                            return false;
                        }
                    });
            this.usernameField.addFocusListener(
                    new FocusListener() {
                        @Override
                        public void focusGained(FocusEvent e) {
                            try {
                                PlayerInputContainer.this.onFocusGained.call();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }

                        @Override
                        public void focusLost(FocusEvent e) {
                            if (PlayerInputContainer.this.onFocusLost != null) {
                                PlayerInputContainer.this.onFocusLost.accept(
                                        PlayerInputContainer.this.usernameField.getText());
                            }
                        }
                    });
            this.usernameField.addCaretListener(
                    (CaretEvent e) -> {
                        PlayerInputContainer.this.onChanged.accept(
                                PlayerInputContainer.this.usernameField.getText());
                    });
            this.add(this.usernameField);
            this.add(this.errorMessageLabel);
        }

        /**
         * Updates the on focus lost callback function
         *
         * @param callback The new callback function
         */
        void setOnFocusLost(Consumer<String> callback) {
            this.onFocusLost = callback;
        }

        /**
         * Updates the on focus gaines callback function with a new one
         *
         * @param callback The new callback function
         */
        void setOnFocusGained(EmptyCallback callback) {
            this.onFocusGained = callback;
        }

        /**
         * Updates on changed callback function
         *
         * @param callback The new callback function
         */
        void setOnChanged(Consumer<String> callback) {
            this.onChanged = callback;
        }

        /**
         * Return the player name in the given input
         *
         * @return The name of the player on the given input
         */
        String getPlayerName() {
            return this.usernameField.getText();
        }

        /**
         * Verify if the player is valid or not. A player name is valid if it contains upper case,
         * lower case and digit characters and its length is between 3 and 10 inclusive
         *
         * @return True if the player is valid, false if not
         */
        boolean isPlayerValid() {
            System.out.printf(
                    "Is player valid function called. Is error message is visible ? %b\n"
                            + " Is username field valid ? %b\n",
                    this.errorMessageLabel.isVisible(),
                    this.usernameField.getText().matches("^[a-zA-Z0-9]{4,10}$"));
            return !this.errorMessageLabel.isVisible()
                    && this.usernameField.getText().matches("^[a-zA-Z0-9]{4,10}$");
        }

        /**
         * Creates a PlayerInput container with an index
         *
         * @param playerIndex The index of the player
         */
        PlayerInputContainer(int playerIndex) {
            this(String.format("Player %d", playerIndex + 1));
        }

        /**
         * Return the player related to the player input container
         *
         * @return The player related to the player input container
         */
        Player getPlayer() {
            return new Player(this.usernameField.getText());
        }

        /** Hide the error message on the input */
        void hideErrorMessage() {
            this.errorMessageLabel.setVisible(false);
        }

        /**
         * Show error message with a given message
         *
         * @param message The given error message to show
         */
        void showErrorMessage(String message) {
            this.errorMessageLabel.setText(message);
            this.errorMessageLabel.setVisible(true);
        }
    }
}
