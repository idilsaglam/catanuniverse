/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game.board;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.catanuniverse.core.game.Player;

class TopStatusBar extends JPanel {

    private Player[] players;
    private PlayerCard[] playerCards;
    static JLabel l;

    public TopStatusBar(Player[] players) throws IOException {
        this.setLayout(new GridLayout(1, players.length));
        this.players = players;
        this.playerCards = new PlayerCard[players.length];
        for (int i = 0; i < players.length; i++) {
            this.playerCards[i] = new PlayerCard(this.players[i], i+1);
            this.add(this.playerCards[i]);
        }
    }

    /**
     * Updates player cards
     */
    public void updatePlayerCard() {
        for (PlayerCard playerCard: this.playerCards) {
            playerCard.updateAchievements();
        }
    }

}
