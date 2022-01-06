package org.catanuniverse.client.game.board;

import java.util.function.Consumer;
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
    private BufferedImage image;
    private BufferedImage image2;
    private JLabel imageLabel;
    private  JLabel imageLabel2;
    private final Predicate<Integer> onDiceRolled;
    public Dice(Predicate<Integer> onDiceRolled) {
        this.onDiceRolled = onDiceRolled;
        image = new BufferedImage(100,100, BufferedImage.TYPE_INT_RGB);
        image2 = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);

        this.setSize(300, 3160);
        this.setPreferredSize(new Dimension(400, 600));

        this.setLayout(null);

        imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setBounds(200,100, 100,100);
        this.add(imageLabel);

        imageLabel2 = new JLabel(new ImageIcon(image2));
        imageLabel2.setBounds(100,100,100,100);
        this.add(imageLabel2);

        this.add(getRollBtn(),BorderLayout.EAST);
        displayDie();
        displayDie2();
    }


    private JButton getRollBtn() {

        if (rollBtn == null) {
            rollBtn = new JButton();
            rollBtn.setBounds(new Rectangle(31, 27, 114, 31));
            rollBtn.setFont(new Font("Verdana", Font.BOLD, 14));
            rollBtn.setForeground(new Color(153, 153, 0));
            rollBtn.setMnemonic(KeyEvent.VK_ENTER);
            rollBtn.setText(Dice.BUTTON_TEXT);
            rollBtn.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    displayDie2();
                    displayDie();
                    roll();
                }
            });
        }
        return rollBtn;
    }


    void displayDie(){
        //dieValueTF.setText(String.valueOf(die1));
        Graphics g = image.getGraphics();
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
            imageLabel.repaint();
        } finally {
            g.dispose();
        }

    }

    void displayDie2(){
        //dieValueTF2.setText(String.valueOf(die2));
        Graphics g = image2.getGraphics();
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
            imageLabel2.repaint();
        } finally {
            g.dispose();
        }

    }

    void roll() {
        if (this.rollBtn.getText().equals(Dice.BUTTON_TEXT)) {
            System.out.println("WILL ROLL DICE");
            dice1 = rand.nextInt(6) + 1;
            dice2 = rand.nextInt(6) + 1;
            if (this.onDiceRolled != null) {
                if (this.onDiceRolled.test(dice1 + dice2)) {
                    return;
                }
                roll();
            }
            return;
        }
        this.onDiceRolled.test(null);
    }


    public void setNextButton(boolean show) {
        this.rollBtn.setText(show ? "NEXT": Dice.BUTTON_TEXT);
        this.rollBtn.revalidate();
        this.rollBtn.repaint();
    }
}
