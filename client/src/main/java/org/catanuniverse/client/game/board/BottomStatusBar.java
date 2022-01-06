/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game.board;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import org.catanuniverse.core.game.Player;
import org.catanuniverse.core.game.Resource;

class BottomStatusBar extends JPanel {
    // label to display text
    static JLabel l;
    private Player currentPlayer;
    private java.util.List<ResourceCard> resourceCards;
    public BottomStatusBar(Player currentPlayer) throws IOException {
        this.currentPlayer = currentPlayer;
        this.resourceCards = new ArrayList<ResourceCard>();
        // TODO: Update grid layout
        this.setLayout(new GridLayout());
        this.add(this.getResourcesRow());
    }

    public JPanel getResourcesRow() throws IOException {
        JPanel result = new JPanel();
        result.setLayout(new GridLayout(0, 2));
        ResourceCard resourceCard;
        for (Resource r: Resource.values()) {
            resourceCard = new ResourceCard(r);
            this.resourceCards.add(resourceCard);
            this.add(resourceCard);
        }
        return result;
    }

    public void updateResources() {
        this.resourceCards.forEach((ResourceCard card) -> {
            card.revalidate();
            card.repaint();
        });
    }

    /**
     * Update the current player with a new one
     * @param currentPlayer The new current player
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;

    }

    private class ResourceCard extends JPanel {
        ResourceCard(Resource resource) throws IOException {
            JLabel label = new JLabel(""+BottomStatusBar.this.currentPlayer.getResource(resource));
            JLabel imageLabel = new JLabel(new ImageIcon(resource.getImage()));
            this.add(label);
            this.add(imageLabel);
        }
    }

}
