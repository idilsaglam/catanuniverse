package org.catanuniverse.client.game.board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import java.util.function.Predicate;
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
    private Consumer<Card> onCardUsed;
    private Consumer<Card> onCardStocked;
    private final Dice dice;
    private JButton stockBtn = null;
    private JButton useBtn = null;

    public BoardSidePane(Predicate<Integer> onDiceRolled, Consumer<Card> onCardUsed, Consumer<Card> onCardStocked) throws IOException {
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        CardPanel cardPanel = new CardPanel();
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.gridy = 0;
        this.dice = new Dice(onDiceRolled);
        this.add(this.dice,gbc);

        gbc.gridx = 1;
        this.add(cardPanel,gbc);
        this.onCardUsed = onCardUsed;
        this.onCardStocked = onCardStocked;

        this.add(cardPanel.stockButton());
        this.add(cardPanel.useButton());

    }




    public int updateRandomLabel() {
        Random r = new Random();
        return (r.nextInt(4));
    }

    public void setNextButton(boolean show) {
        this.dice.setNextButton(show);
    }

    private class CardPanel extends JPanel implements MouseListener {
        Card currentCard;

        public CardPanel() throws IOException {
            JLabel cardLabel;
            BufferedImage card = ImageIO.read(this.getClass().getResource("/card2.png"));
            Image simg = card.getScaledInstance(200,400,Image.SCALE_SMOOTH);
            cardLabel = new JLabel(new ImageIcon(simg));
            this.add(cardLabel,BorderLayout.CENTER);
            this.addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (this.getComponentCount() == 2) {
                this.remove(1);
                this.revalidate();
                this.repaint();
            }
            JLabel label;
            BufferedImage card = null;
            try {
                int val = BoardSidePane.this.updateRandomLabel();
                card = ImageIO.read(this.getClass().getResource("/cart"+val+".png"));
                currentCard = Card.fromInt(val);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Image simg = card.getScaledInstance(200,400,Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(simg));

            this.add(label,BorderLayout.CENTER);
            this.revalidate();
            this.repaint();

        }

        /**
         * Creates stock card button
         * @return The stock card button
         */
        public JButton stockButton(){
            JButton button = new JButton();
            button.setText("Stock");
            button.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        BoardSidePane.this.onCardStocked.accept(CardPanel.this.currentCard);
                    }
                });
           return button;
        }

        /**
         * Creates the use card button
         * @return The use card button
         */
        public JButton useButton(){
            JButton button = new JButton();
            button.setText("Use");
            button.addActionListener(new java.awt.event.ActionListener(){
                    public void actionPerformed(java.awt.event.ActionEvent e){
                        BoardSidePane.this.onCardUsed.accept(CardPanel.this.currentCard);
                    }
                });
          return button;
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
