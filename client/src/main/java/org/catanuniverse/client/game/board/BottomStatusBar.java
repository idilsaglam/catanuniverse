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
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout());
        formPanel.add(new Exchange((HashMap<Resource, Integer> resourcesToExchange, Resource resourceToReceive) -> {
            // TODO: BUNU DA CLASSIN ICINE BIR FONKSIYON OLARAK YAZAK
        },
            // TODO: HARBOR LAZIM
            this.currentPlayer.getResources(),
            3
        ));
        formPanel.add(porteRadioButtons());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        this.add(formPanel, gbc);
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

    public JPanel porteCheckBox1(){
        JPanel f = new JPanel();
        JCheckBox checkBoxCorn = new JCheckBox("Corn");
        JCheckBox checkBoxHill = new JCheckBox("Hill");
        JCheckBox checkBoxFarm = new JCheckBox("Farm");
        JCheckBox checkBoxMeadow = new JCheckBox("Meadow");
        JCheckBox checkBoxMountain = new JCheckBox("Mountain");

        f.add(checkBoxCorn);
        f.add(checkBoxHill);
        f.add(checkBoxFarm);
        f.add(checkBoxMeadow);
        f.add(checkBoxMountain);

        f.setLayout(new GridLayout(0, 1));
        return f;
    }


    public JPanel porteCheckBox(){
        JPanel panel = new JPanel();
        final JLabel label = new JLabel();
        label.setHorizontalAlignment(JLabel.CENTER);

        SpinnerModel cornModel = new SpinnerNumberModel(
            2, //valeur initiale
            0, //valeur minimum
            20, //valeur maximum
            1 //pas
        );

        SpinnerModel hillModel = new SpinnerNumberModel(
            2, //valeur initiale
            0, //valeur minimum
            20, //valeur maximum
            1 //pas
        );

        SpinnerModel farmModel = new SpinnerNumberModel(
            2, //valeur initiale
            0, //valeur minimum
            20, //valeur maximum
            1 //pas
        );

        SpinnerModel meadowModel = new SpinnerNumberModel(
            2, //valeur initiale
            0, //valeur minimum
            20, //valeur maximum
            1 //pas
        );

        SpinnerModel mountainModel = new SpinnerNumberModel(
            2, //valeur initiale
            0, //valeur minimum
            20, //valeur maximum
            1 //pas
        );


        JSpinner cornSpinner = new JSpinner(cornModel);
        JSpinner hillSpinner = new JSpinner(hillModel);
        JSpinner farmSpinner = new JSpinner(farmModel);
        JSpinner mountainSpinner = new JSpinner(mountainModel);
        JSpinner meadowSpinner = new JSpinner(meadowModel);


        panel.add(label);
        panel.add(cornSpinner);
        panel.add(hillSpinner);
        panel.add(farmSpinner);
        panel.add(mountainSpinner);
        panel.add(meadowSpinner);

        panel.setLayout(new GridLayout(2, 1));

        cornSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                label.setText("Valeur de Corn : " + ((JSpinner)e.getSource()).getValue());
            }
        });

        hillSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                label.setText("Valeur de Hill : " + ((JSpinner)e.getSource()).getValue());
            }
        });

        farmSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                label.setText("Valeur de Farm : " + ((JSpinner)e.getSource()).getValue());
            }
        });

        mountainSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                label.setText("Valeur de mountain : " + ((JSpinner)e.getSource()).getValue());
            }
        });

        meadowSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                label.setText("Valeur de Meadow : " + ((JSpinner)e.getSource()).getValue());
            }
        });


        return panel;
    }

    public JPanel porteRadioButtons(){

        JPanel panel = new JPanel();

        JRadioButton jRadioButtonCorn = new JRadioButton();
        JRadioButton jRadioButtonHill = new JRadioButton();
        JRadioButton jRadioButtonFarm = new JRadioButton();
        JRadioButton jRadioButtonMeadow = new JRadioButton();
        JRadioButton jRadioButtonMountain = new JRadioButton();

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
