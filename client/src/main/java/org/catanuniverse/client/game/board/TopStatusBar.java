/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game.board;

import java.awt.GridLayout;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.catanuniverse.core.game.Player;

class TopStatusBar extends JPanel {

    private int nbPlayers;
    private final Player[] players;

    public TopStatusBar(Player[] players, int capacity) {
        this.setLayout(new GridLayout(1, capacity));
        this.players = new Player[capacity];
        for (int i = 0; i < players.length; i++) {
            this.players[i] = players[i];
            this.add(new PlayerContainer(this.players[i]));
            this.nbPlayers++;
        }
    }

    /**
     * Add a new player to the top pane
     *
     * @param p The new player to add
     * @return True if the player added correctly false if not
     */
    public boolean addPlayer(Player p) {
        if (this.nbPlayers == this.players.length - 1) {
            return false;
        }
        this.players[this.nbPlayers] = p;
        this.add(new PlayerContainer(this.players[this.nbPlayers]));
        this.nbPlayers++;
        return true;
    }

    private static class PlayerContainer extends JPanel {
        public PlayerContainer(Player p) {
            ImageIcon avatar;
            try {
                avatar = new Avatar(p.getUsername());
            } catch (IOException ignore) {
                avatar = new ImageIcon();
            }
            this.add(new JLabel(avatar));
        }
    }

    private static class PlayerDetailsContainer extends JPanel {}
}
