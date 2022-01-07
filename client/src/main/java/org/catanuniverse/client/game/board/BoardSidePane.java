package org.catanuniverse.client.game.board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import org.catanuniverse.core.game.Card;

class BoardSidePane extends JPanel {
    private final Consumer<Card> onCardUsed;
    private final Consumer<Card> onCardStocked;
    private final Dice dice;
    private final Supplier<Boolean> canCardBeDrawn;

    public BoardSidePane(Predicate<Integer> onDiceRolled, Consumer<Card> onCardUsed, Consumer<Card> onCardStocked, Supplier<Boolean> canCardBeDrawn) throws IOException {
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        CardPanel cardPanel = new CardPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        this.dice = new Dice(onDiceRolled);
        this.add(this.dice,gbc);

        gbc.gridx = 1;
        this.add(cardPanel,gbc);

        this.onCardUsed = onCardUsed;
        this.onCardStocked = onCardStocked;
        this.canCardBeDrawn = canCardBeDrawn;

        gbc.gridx = 0;
        gbc.gridy = 1;


    }



    public void setNextButton(boolean show) {
        this.dice.setNextButton(show);
    }

    private class CardPanel extends JPanel implements MouseListener {
        private Card currentCard;
        private final JButton stockButton, useButton;
        private final JPanel buttonContainer, cardContainer;

        public CardPanel() throws IOException {
            JLabel cardLabel;
            this.stockButton = new JButton("Stock");
            this.stockButton.addActionListener(this::stockCard);
            this.useButton = new JButton("Use");
            this.useButton.addActionListener(this::useCard);
            this.buttonContainer = new JPanel();
            this.cardContainer = new JPanel();
            this.cardContainer.setLayout(new GridLayout(1,2));
            this.buttonContainer.setLayout(new GridLayout(2, 1));
            this.buttonContainer.add(this.useButton);
            this.buttonContainer.add(this.stockButton);
            BufferedImage card = ImageIO.read(this.getClass().getResource("/card2.png"));
            Image simg = card.getScaledInstance(200,400,Image.SCALE_SMOOTH);
            cardLabel = new JLabel(new ImageIcon(simg));
            GridBagConstraints gbc = new GridBagConstraints();
            this.setLayout(new GridBagLayout());
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 0.5;
            this.cardContainer.add(cardLabel);
            this.add(this.cardContainer, gbc);
            gbc.gridx++;
            this.add(this.buttonContainer, gbc);
            this.addMouseListener(this);
            this.buttonContainer.setVisible(false);
        }



        @Override
        public void mouseClicked(MouseEvent e) {
            // If for some reason card drawn is blocked nothing will happen
            if (!BoardSidePane.this.canCardBeDrawn.get()) return;
            // If the current card is not yet used or stocked nothing will happen
            if (this.currentCard != null) return;
            // If there's already a card drawn will remove the card
            this.resetDrawnCard();
            this.buttonContainer.setVisible(true);
            JLabel label;
            BufferedImage card = null;
            try {
                int val = this.updateRandomLabel();
                card = ImageIO.read(this.getClass().getResource("/cart"+val+".png"));
                currentCard = Card.fromInt(val);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Image simg = card.getScaledInstance(200,400,Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(simg));

            this.cardContainer.add(label);
            this.revalidate();
            this.repaint();

        }

        /**
         * Remove the drawn card from JPanel
         */
        private void resetDrawnCard() {
            this.currentCard = null;
            this.buttonContainer.setVisible(false);
            if (this.cardContainer.getComponentCount() == 2) {
                this.cardContainer.remove(1);
                this.cardContainer.revalidate();
                this.cardContainer.repaint();
            }
        }

        private void stockCard(ActionEvent e) {
            BoardSidePane.this.onCardStocked.accept(CardPanel.this.currentCard);
            this.resetDrawnCard();
        }

        private void useCard(ActionEvent e) {
            BoardSidePane.this.onCardUsed.accept(CardPanel.this.currentCard);
            CardPanel.this.resetDrawnCard();
        }

        private int updateRandomLabel() {
            Random r = new Random();
            return (r.nextInt(4));
        }



        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
