package org.catanuniverse.client.game.board;

import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Random;

class BoardSidePane extends JPanel {
    private Consumer<Integer> consumer;
    private final Dice dice;
    public BoardSidePane(Predicate<Integer> onDiceRolled, Consumer<Integer> consumer) throws IOException {
        this.setBackground(Color.RED);
        this.setLayout(new GridLayout(2,1));
        CardPanel cardPanel = new CardPanel();
        this.add(cardPanel,BorderLayout.CENTER);
        this.addMouseListener(cardPanel);
        this.dice = new Dice(onDiceRolled);
        this.add(this.dice);
        this.consumer = consumer;
    }

    @Override
    public void setSize(Dimension size) {
        super.setSize(size);
        super.setMinimumSize(size);
        super.setPreferredSize(size);
    }

    public int updateRandomLabel() {
        Random r = new Random();
        return (r.nextInt(4));
    }

    public void setNextButton(boolean show) {
        this.dice.setNextButton(show);
    }

    private class CardPanel extends JPanel implements MouseListener {

        public CardPanel() throws IOException {
            JLabel cardLabel;
            BufferedImage card = ImageIO.read(this.getClass().getResource("/card2.png"));
            Image simg = card.getScaledInstance(200,400,Image.SCALE_SMOOTH);
            cardLabel = new JLabel(new ImageIcon(simg));
            this.add(cardLabel,BorderLayout.CENTER);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (this.getComponentCount() == 2) {
                this.remove(1);
                this.revalidate();
                this.repaint();
            }
            JPanel panel = new JPanel();
            JLabel label;
            BufferedImage card = null;
            try {
                int val = BoardSidePane.this.updateRandomLabel();
                card = ImageIO.read(this.getClass().getResource("/cart"+val+".png"));
                BoardSidePane.this.consumer.accept(val);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Image simg = card.getScaledInstance(200,400,Image.SCALE_SMOOTH);
            label = new JLabel(new ImageIcon(simg));

            this.add(label,BorderLayout.CENTER);
            this.revalidate();
            this.repaint();

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
