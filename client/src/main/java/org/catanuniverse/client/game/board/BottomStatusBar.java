/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game.board;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import org.catanuniverse.core.game.Player;
import org.catanuniverse.core.game.Resource;

class BottomStatusBar extends JPanel {
    static JLabel l;
    private Player currentPlayer;
    private java.util.List<ResourceCard> resourceCards;
    private PlayerCard playerCard;
    public BottomStatusBar(Player currentPlayer, int playerIndex) throws IOException {
        this.currentPlayer = currentPlayer;
        this.resourceCards = new ArrayList<ResourceCard>();
        // TODO: Update grid layout
        this.setLayout(new GridLayout(1,2));
        this.playerCard = new PlayerCard(currentPlayer, playerIndex+1);
        this.add(this.playerCard);
        this.add(this.getResourcesRow());
    }

    public JPanel getResourcesRow() throws IOException {
        JPanel result = new JPanel();
        result.setLayout(new GridLayout(0, 3));
        ResourceCard resourceCard;
        for (Resource r: Resource.values()) {
            resourceCard = new ResourceCard(r);
            this.resourceCards.add(resourceCard);
            result.add(resourceCard);
        }
        return result;
    }

    public void updateResources() {
        this.resourceCards.forEach((ResourceCard card) -> {
            card.updateResource();
            card.revalidate();
            card.repaint();
        });
    }

    /**
     * Update the current player with a new one
     * @param currentPlayer The new current player
     */
    public void setCurrentPlayer(Player currentPlayer, int currentPlayerIndex) throws IOException {
        this.currentPlayer = currentPlayer;
        this.playerCard.setPlayer(currentPlayer, currentPlayerIndex);
        this.playerCard.revalidate();
        this.playerCard.repaint();
    }


    private class ResourceCard extends JPanel {
        private final Resource resource;
        private final JLabel imageLabel, countLabel;
        ResourceCard(Resource resource) throws IOException {
            this.resource = resource;
            this.countLabel = new JLabel(""+BottomStatusBar.this.currentPlayer.getResource(resource));
            this.imageLabel = new JLabel();
            this.imageLabel.setIcon(new ImageIcon(resource.getImage()));
            this.add(countLabel);
            this.add(imageLabel);
        }

        private void updateResource() {
            this.countLabel.setText(""+BottomStatusBar.this.currentPlayer.getResource(resource));
        }
    }

}
