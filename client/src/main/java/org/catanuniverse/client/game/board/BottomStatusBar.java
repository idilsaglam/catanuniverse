/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.client.game.board;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.swing.*;
import org.catanuniverse.core.game.Card;
import org.catanuniverse.core.game.Harbor;
import org.catanuniverse.core.game.Player;
import org.catanuniverse.core.game.Resource;
import org.catanuniverse.core.utils.EmptyCallback;

class BottomStatusBar extends JPanel {
    static JLabel l;
    private Player currentPlayer;
    private java.util.List<ResourceCard> resourceCards;
    private PlayerCard playerCard;
    private CartDeck cartDeck;
    private EmptyCallback onNextButtonClicked, onExchangeCompleted;
    private final Supplier<Set<Harbor>> getCurrentPlayerHarbors;
    private Exchange exchangePanel;
    private Set<Harbor> harbors;

    public BottomStatusBar(
            Player currentPlayer,
            int playerIndex,
            EmptyCallback onNextButtonPressed,
            Consumer<Card> onCardUsed,
            Supplier<Set<Harbor>> harborSupplier,
            EmptyCallback onExchangeCompleted)
            throws IOException {
        this.onExchangeCompleted = onExchangeCompleted;
        this.harbors = harborSupplier.get();
        this.getCurrentPlayerHarbors = harborSupplier;
        this.currentPlayer = currentPlayer;
        this.cartDeck = new CartDeck(this.currentPlayer, onCardUsed);
        this.resourceCards = new ArrayList<ResourceCard>();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        this.playerCard = new PlayerCard(currentPlayer, playerIndex + 1, 64, 64);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        this.exchangePanel =
                new Exchange(
                        this::onExchangeConfirmed, this.currentPlayer.getResources(), this.harbors);
        this.add(this.exchangePanel, gbc);
        gbc.gridx = 1;
        this.add(this.playerCard, gbc);
        gbc.gridx = 2;
        this.add(this.getResourcesRow(), gbc);
        gbc.gridx = 3;
        this.add(cartDeck, gbc);
        gbc.gridx = 4;
        this.add(nextPlayerButton(), gbc);
        gbc.gridx = 5;

        this.onNextButtonClicked = onNextButtonPressed;
    }

    /**
     * Handles the exchange confirmation event from exchange form
     *
     * @param resourcesToExchange The hash map of resources that current user will lose
     * @param resourceToReceive The resource type and the resource value that current user receives
     */
    private void onExchangeConfirmed(
            HashMap<Resource, Integer> resourcesToExchange,
            AbstractMap.Entry<Resource, Integer> resourceToReceive) {
        // Current user will lose resources in the hashmap
        for (Map.Entry<Resource, Integer> e : resourcesToExchange.entrySet()) {
            this.currentPlayer.updateResource(e.getKey(), -1 * e.getValue());
        }
        // Current user will receive resource and value from Entry
        this.currentPlayer.updateResource(resourceToReceive.getKey(), resourceToReceive.getValue());
        try {
            this.onExchangeCompleted.call();
        } catch (IOException ignore) {
        }
    }

    public JButton nextPlayerButton() {
        JButton button = new JButton();
        button.setText("Next player");
        button.addActionListener(
                new ActionListener() {
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
        for (Resource r : Resource.values()) {
            resourceCard = new ResourceCard(r);
            this.resourceCards.add(resourceCard);
            result.add(resourceCard);
        }
        return result;
    }

    public void updateResources() {
        this.resourceCards.forEach(
                (ResourceCard card) -> {
                    card.updateResource();
                    card.revalidate();
                    card.repaint();
                });
    }

    /** Updates the harbors with the given harbor list supplier */
    public void updateExchange() {
        
        
        this.harbors = this.getCurrentPlayerHarbors.get();
        
        this.harbors = this.getCurrentPlayerHarbors.get();
        
        this.exchangePanel.update(this.currentPlayer.getResources(), this.harbors);
        this.exchangePanel.revalidate();
        this.exchangePanel.repaint();
    }

    /** Update all sub-components in the bottom status bar */
    public void update() {
        this.updateResources();
        this.updateUserCards();
        this.updateExchange();
    }

    /**
     * Update the current player with a new one
     *
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
         *
         * @param resource The type of resource
         * @throws IOException when t
         */
        ResourceCard(Resource resource) throws IOException {
            this.resource = resource;
            this.countLabel =
                    new JLabel("" + BottomStatusBar.this.currentPlayer.getResource(resource));
            this.imageLabel = new JLabel();
            this.imageLabel.setIcon(new ImageIcon(resource.getImage()));
            this.add(imageLabel);
            this.add(countLabel);
        }

        /** Updates the resources */
        private void updateResource() {
            this.countLabel.setText("" + BottomStatusBar.this.currentPlayer.getResource(resource));
            this.countLabel.revalidate();
            this.countLabel.repaint();
        }
    }
}
