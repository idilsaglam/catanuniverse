package org.catanuniverse.client.game.board;

import java.util.function.Consumer;
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
    private JTextField valueTF = null;
    private JTextField valueTF2 = null;
    private JLabel label = null;
    private JLabel label2 = null;
    private int top = 50, left = 100;

    private BufferedImage image;
    private BufferedImage image2;
    private JLabel imageLabel;
    private  JLabel imageLabel2;
    private final Consumer<Integer> onDiceRolled;
    public Dice(Consumer<Integer> onDiceRolled) {
        this.onDiceRolled = onDiceRolled;
        image = new BufferedImage(100,100, BufferedImage.TYPE_INT_RGB);
        image2 = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);

        this.setSize(300, 3160);
        this.setPreferredSize(new Dimension(400, 600));


        label = new JLabel();
        label.setBounds(new Rectangle(147, 28, 67, 34));
        label.setText("Die Value:");

        label2 = new JLabel();
        label2.setBounds(new Rectangle(300, 28, 67, 34));
        label2.setText("Die Value 2:");

        this.setLayout(null);

        /*this.add(getDieValueTF());
        this.add(getDieValueTF2());
        this.add(dieLabel);
        this.add(dieLabel2);*/

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
            rollBtn.setText("Roll Die!");
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

    private JTextField getValueTF() {

        if (valueTF == null) {
            valueTF = new JTextField();
            valueTF.setBounds(new Rectangle(217, 32, 26, 26));
            //dieValueTF.setHorizontalAlignment(JTextField.CENTER);
        }
        return valueTF;
    }

    private JTextField getValueTF2() {

        if (valueTF2 == null) {
            valueTF2 = new JTextField();
            valueTF2.setBounds(new Rectangle(500, 32, 26, 26));
            valueTF.setHorizontalAlignment(JTextField.CENTER);
        }
        return valueTF2;
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
        dice1 = rand.nextInt(6) + 1;
        dice2 = rand.nextInt(6) + 1;
        if (this.onDiceRolled != null) {
            this.onDiceRolled.accept(dice1+dice2);
        }
    }


}
