package org.catanuniverse.client.game.board;

import java.awt.event.ActionEvent;
import java.util.function.Predicate;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

class Dice extends JPanel {
    private static final long serialVersionUID = 1L;
    static Random rand = new Random();  //  @jve:decl-index=0:
    private int dice1 = 1;
    private int dice2 = 1;
    private JButton rollBtn = null;
    private static final String BUTTON_TEXT = "Roll dice!";
    private final BufferedImage diceImage1, diceImage2;
    private final JLabel diceLabel1, diceLabel2;
    private final JButton rollButton;
    private final Predicate<Integer> onDiceRolled;
    private final JPanel diceContainer;

    public Dice(Predicate<Integer> onDiceRolled) {
        this.onDiceRolled = onDiceRolled;

        this.diceImage1 = new BufferedImage(100,100, BufferedImage.TYPE_INT_RGB);
        this.diceImage2 = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
        this.rollButton = new JButton(Dice.BUTTON_TEXT);
        this.rollButton.addActionListener((ActionEvent ignore) -> {
            roll();
        });
        this.diceContainer = new JPanel();
        this.diceContainer.setLayout(new GridLayout(2, 1));
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());

        diceLabel1 = new JLabel(new ImageIcon(diceImage1));
        diceLabel2 = new JLabel(new ImageIcon(diceImage2));

        this.diceContainer.add(diceLabel1);
        this.diceContainer.add(diceLabel2);

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(diceContainer, gbc);
        gbc.gridy++;
        this.add(this.rollButton, gbc);
        this.reset();
    }




    void displayDie(){
        //dieValueTF.setText(String.valueOf(die1));
        Graphics g = diceImage1.getGraphics();
        try {
            g.setColor(Color.WHITE);
            g.fillRect(0,0, 100,100);
            g.setColor(Color.BLACK);
            g.drawRect(0,0, 100,100);
            switch (dice1) {
                case 1:
                    g.fillOval(50,50, 10,10);
                    break;
                case 3:
                    g.fillOval(20,20, 10,10);
                    g.fillOval(50,50, 10,10);
                    g.fillOval(80,80, 10,10);
                    break;
                case 5:
                    g.fillOval(50,50, 10,10);
                    // Fall through.
                case 4:
                    g.fillOval(80,20, 10,10);
                    g.fillOval(20,80, 10,10);
                    // Fall through.
                case 2:
                    g.fillOval(20,20, 10,10);
                    g.fillOval(80,80, 10,10);
                    break;
                case 6:
                    g.fillOval(20,20, 10,10);
                    g.fillOval(20,50, 10,10);
                    g.fillOval(80,20, 10,10);
                    g.fillOval(20,80, 10,10);
                    g.fillOval(80,50, 10,10);
                    g.fillOval(80,80, 10,10);
                    break;
            }
            diceLabel1.repaint();
        } finally {
            g.dispose();
        }

    }

    void displayDie2(){
        //dieValueTF2.setText(String.valueOf(die2));
        Graphics g = diceImage2.getGraphics();
        try {
            g.setColor(Color.WHITE);
            g.fillRect(0,0, 100,100);
            g.setColor(Color.BLACK);
            g.drawRect(0,0, 100,100);
            switch (dice2) {
                case 1:
                    g.fillOval(50,50, 10,10);
                    break;
                case 3:
                    g.fillOval(20,20, 10,10);
                    g.fillOval(50,50, 10,10);
                    g.fillOval(80,80, 10,10);
                    break;
                case 5:
                    g.fillOval(50,50, 10,10);
                    // Fall through.
                case 4:
                    g.fillOval(80,20, 10,10);
                    g.fillOval(20,80, 10,10);
                    // Fall through.
                case 2:
                    g.fillOval(20,20, 10,10);
                    g.fillOval(80,80, 10,10);
                    break;
                case 6:
                    g.fillOval(20,20, 10,10);
                    g.fillOval(20,50, 10,10);
                    g.fillOval(80,20, 10,10);
                    g.fillOval(20,80, 10,10);
                    g.fillOval(80,50, 10,10);
                    g.fillOval(80,80, 10,10);
                    break;
            }
            diceLabel2.repaint();
        } finally {
            g.dispose();
        }

    }

    void roll() {
            dice1 = rand.nextInt(6) + 1;
            dice2 = rand.nextInt(6) + 1;
            this.displayDie();
            this.displayDie2();
            this.diceLabel2.revalidate();
            this.diceLabel2.repaint();
            this.diceLabel1.revalidate();
            this.diceLabel1.repaint();
            if (this.onDiceRolled != null) {
                if (this.onDiceRolled.test(dice1 + dice2)) {
                    this.rollButton.setVisible(false);
                    this.diceContainer.setVisible(true);
                    return;
                }
                roll();
            }

    }

    public void setDisabled() {
        this.rollButton.setVisible(true);
        this.rollButton.setEnabled(false);
        this.diceContainer.setVisible(false);
    }

    public void reset() {
        this.rollButton.setVisible(true);
        this.rollButton.setEnabled(true);
        this.diceContainer.setVisible(false);
    }
}
