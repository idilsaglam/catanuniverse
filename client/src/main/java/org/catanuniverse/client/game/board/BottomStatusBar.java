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
import javax.swing.*;
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
    public BottomStatusBar(Player currentPlayer, int playerIndex,EmptyCallback e) throws IOException {

        this.currentPlayer = currentPlayer;
        this.cartDeck = new CartDeck(this.currentPlayer);
        this.resourceCards = new ArrayList<ResourceCard>();
        // TODO: Update grid layout
        GridBagConstraints gbc= new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        this.playerCard = new PlayerCard(currentPlayer, playerIndex+1,64,64);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        this.add(porteRadioButtons(), gbc);
        gbc.gridx = 1;
        this.add(this.playerCard, gbc);
        gbc.gridx = 2;
        this.add(this.getResourcesRow(), gbc);
        gbc.gridx = 3;
        this.add(cartDeck, gbc);
        gbc.gridx = 4;
        this.add(nextPlayerButton(), gbc);
        this.onNextButtonClicked = e;

    }


    public JPanel porteRadioButtons(){

        JPanel panel = new JPanel();

        JRadioButton jRadioButtonCorn = new JRadioButton();
        JRadioButton jRadioButtonHill = new JRadioButton();
        JRadioButton jRadioButtonFarm = new JRadioButton();
        JRadioButton jRadioButtonMeadow = new JRadioButton();
        JRadioButton jRadioButtonMountain = new JRadioButton();
        JRadioButton jRadioButtonHarbor = new JRadioButton();

        JButton jButton = new JButton();
        jButton.setText("Harbor");
        ButtonGroup g1 = new ButtonGroup();


        jRadioButtonCorn.setText("Corn");
        jRadioButtonFarm.setText("Farm");
        jRadioButtonHill.setText("Hill");
        jRadioButtonMeadow.setText("Meadow");
        jRadioButtonMountain.setText("Mountain");


        this.add(jRadioButtonCorn);
        this.add(jRadioButtonFarm);
        this.add(jRadioButtonHill);
        this.add(jRadioButtonMeadow);
        this.add(jRadioButtonMountain);

        this.add(jButton);


        g1.add(jRadioButtonCorn);
        g1.add(jRadioButtonHill);
        g1.add(jRadioButtonMeadow);
        g1.add(jRadioButtonMountain);
        g1.add(jRadioButtonFarm);

        panel.add(jRadioButtonCorn);
        panel.add(jRadioButtonHill);
        panel.add(jRadioButtonMeadow);
        panel.add(jRadioButtonMountain);
        panel.add(jRadioButtonFarm);
        panel.add(jButton);
        panel.setLayout(new GridLayout(0, 2));
        panel.setBackground(Color.red);
        return panel;

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
        result.setLayout(new GridLayout(1, 0));
        ResourceCard resourceCard;
        for (Resource r: Resource.values()) {
            resourceCard = new ResourceCard(r);
            this.resourceCards.add(resourceCard);
            result.add(resourceCard);
        }
        result.setBackground(Color.GREEN);
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
