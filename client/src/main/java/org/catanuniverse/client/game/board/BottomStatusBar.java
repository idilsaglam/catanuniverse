/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game.board;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.catanuniverse.core.game.Card;
import org.catanuniverse.core.game.Player;
import org.catanuniverse.core.game.Resource;
import org.catanuniverse.core.utils.EmptyCallback;

class BottomStatusBar extends JPanel {
    static JLabel l;
    private Player currentPlayer;
    private java.util.List<ResourceCard> resourceCards;
    private PlayerCard playerCard;
    private CartDeck cartDeck;
    private EmptyCallback onNextButtonClicked;
    public BottomStatusBar(Player currentPlayer, int playerIndex,EmptyCallback onNextButtonPressed, Consumer<Card> onCardUsed) throws IOException {
        this.currentPlayer = currentPlayer;
        this.cartDeck = new CartDeck(this.currentPlayer, onCardUsed);
        this.resourceCards = new ArrayList<ResourceCard>();
        GridBagConstraints gbc= new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        this.playerCard = new PlayerCard(currentPlayer, playerIndex+1,64,64);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        this.add(new Exchange((HashMap<Resource, Integer> resourcesToExchange, Resource resourceToReceive) -> {
            // TODO: BUNU DA CLASSIN ICINE BIR FONKSIYON OLARAK YAZAK
        },
                // TODO: HARBOR LAZIM
                this.currentPlayer.getResources(),
                3
        ), gbc);
        gbc.gridx = 1;
        this.add(this.playerCard, gbc);
        gbc.gridx = 2;
        this.add(this.getResourcesRow(), gbc);
        gbc.gridx = 3;
        this.add(cartDeck, gbc);
        gbc.gridx = 4;
        this.add(nextPlayerButton(), gbc);
        gbc.gridx=5;

        this.onNextButtonClicked = onNextButtonPressed;

    }





    public JButton nextPlayerButton(){
        JButton button = new JButton();
        button.setText("Next player");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    onNextButtonClicked.call();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        return button;
    }

    public JPanel getResourcesRow() throws IOException {
        JPanel result = new JPanel();
        result.setLayout(new GridLayout(2, 0));
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
        this.cartDeck.setCurrentPlayer(this.currentPlayer);
        this.updateResources();
        this.playerCard.revalidate();
        this.playerCard.repaint();
    }

    public void updateUserCards() {
        this.cartDeck.updateCards();
    }


    private class ResourceCard extends JPanel {
        private final Resource resource;
        private final JLabel imageLabel, countLabel;
        /**
         * Creates a resource card for given resource type
         * @param resource The type of resource
         * @throws IOException when t
         */
        ResourceCard(Resource resource) throws IOException {
            this.resource = resource;
            this.countLabel = new JLabel(""+BottomStatusBar.this.currentPlayer.getResource(resource));
            this.imageLabel = new JLabel();
            this.imageLabel.setIcon(new ImageIcon(resource.getImage()));
            this.add(imageLabel);
            this.add(countLabel);
        }

        /**
         * Updates the resources
         */
        private void updateResource() {
            this.countLabel.setText(""+BottomStatusBar.this.currentPlayer.getResource(resource));
            this.countLabel.revalidate();
            this.countLabel.repaint();
        }
    }


}
