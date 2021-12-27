/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.client.configuration;

import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.catanuniverse.core.models.Player;

class PlayersInputContainer extends JPanel {

    private final PlayerInputContainer[] players;
    private int numberOfPlayers;

    PlayersInputContainer(String labelText, int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.players = new PlayerInputContainer[4];
    }

    PlayersInputContainer(int numberOfPlayers) {
        this("Create players", numberOfPlayers);
    }

    void setNumberOfPlayers(int numberOfPlayers) {
        System.out.printf("Players input container set number of players function called with %d\n", numberOfPlayers);
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
                this.players[old + i] = new PlayerInputContainer(old + i);
                this.add(this.players[old + i]);
            }
        }
        super.revalidate();
        super.repaint();
    }

    Player[] getPlayers() {
        Player[] result = new Player[this.numberOfPlayers];
        for (int i = 0; i < this.numberOfPlayers; i++) {
            result[i] = this.players[i].getPlayer();
        }
        return result;
    }

    private static class PlayerInputContainer extends JPanel {

        private final JTextField usernameField;
        private final JLabel errorMessageLabel;
        PlayerInputContainer(String labelText) {
            JLabel label = new JLabel(labelText);

            this.usernameField = new JTextField(10);
            this.errorMessageLabel = new JLabel();
            this.errorMessageLabel.setVisible(false);
            this.errorMessageLabel.setForeground(Color.RED);

            this.setLayout(new GridLayout(3, 1));
            this.add(label);
            this.usernameField.setInputVerifier(new InputVerifier() {
                @Override
                public boolean verify(JComponent input) {
                    String text = ((JTextField) input).getText();
                    boolean valid = text.matches("^[a-zA-Z0-9]{4,10}$");
                    PlayerInputContainer.this.errorMessageLabel.setVisible(!valid);
                    if (valid) {
                        PlayerInputContainer.this.errorMessageLabel.repaint();
                        return true;
                    }
                    PlayerInputContainer.this.errorMessageLabel.setText(String.format("text %s is not valid.", text));
                    PlayerInputContainer.this.errorMessageLabel.repaint();
                    return false;
                }
            });
            this.add(this.usernameField);
            this.add(this.errorMessageLabel);
        }

        boolean isPlayerValid() {
            return this.usernameField.isValid();
        }

        PlayerInputContainer(int playerIndex) {
            this(String.format("Player %d", playerIndex + 1));
        }

        Player getPlayer() {
            return null;
        }
    }
}
