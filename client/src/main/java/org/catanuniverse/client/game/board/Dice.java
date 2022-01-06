package org.catanuniverse.client.game.board;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

class Dice extends JPanel {
    private static final long serialVersionUID = 1L;
    static Random rand = new Random();  //  @jve:decl-index=0:
    private int die1 = 1;
    private int die2 = 1;
    private JButton rollBtn = null;
    private JTextField dieValueTF = null;
    private JTextField dieValueTF2 = null;
    private JLabel dieLabel = null;
    private JLabel dieLabel2 = null;
    private int top = 50, left = 100;

    private BufferedImage dieImage;
    private BufferedImage dieImage2;
    private JLabel dieImageLabel;
    private  JLabel dieImageLabel2;
    public Dice() {

        dieImage = new BufferedImage(100,100, BufferedImage.TYPE_INT_RGB);
        dieImage2 = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);

        this.setSize(300, 3160);
        this.setPreferredSize(new Dimension(400, 600));


        dieLabel = new JLabel();
        dieLabel.setBounds(new Rectangle(147, 28, 67, 34));
        dieLabel.setText("Die Value:");

        dieLabel2 = new JLabel();
        dieLabel2.setBounds(new Rectangle(300, 28, 67, 34));
        dieLabel2.setText("Die Value 2:");

        this.setLayout(null);

        /*this.add(getDieValueTF());
        this.add(getDieValueTF2());
        this.add(dieLabel);
        this.add(dieLabel2);*/

        dieImageLabel = new JLabel(new ImageIcon(dieImage));
        dieImageLabel.setBounds(200,100, 100,100);
        this.add(dieImageLabel);

        dieImageLabel2 = new JLabel(new ImageIcon(dieImage2));
        dieImageLabel2.setBounds(100,100,100,100);
        this.add(dieImageLabel2);

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

    private JTextField getDieValueTF() {

        if (dieValueTF == null) {
            dieValueTF = new JTextField();
            dieValueTF.setBounds(new Rectangle(217, 32, 26, 26));
            //dieValueTF.setHorizontalAlignment(JTextField.CENTER);
        }
        return dieValueTF;
    }

    private JTextField getDieValueTF2() {

        if (dieValueTF2 == null) {
            dieValueTF2 = new JTextField();
            dieValueTF2.setBounds(new Rectangle(500, 32, 26, 26));
            dieValueTF.setHorizontalAlignment(JTextField.CENTER);
        }
        return dieValueTF2;
    }

    void displayDie(){
        //dieValueTF.setText(String.valueOf(die1));
        Graphics g = dieImage.getGraphics();
        try {
            g.setColor(Color.WHITE);
            g.fillRect(0,0, 100,100);
            g.setColor(Color.BLACK);
            g.drawRect(0,0, 100,100);
            switch (die1) {
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
            dieImageLabel.repaint();
        } finally {
            g.dispose();
        }

    }

    void displayDie2(){
        //dieValueTF2.setText(String.valueOf(die2));
        Graphics g = dieImage2.getGraphics();
        try {
            g.setColor(Color.WHITE);
            g.fillRect(0,0, 100,100);
            g.setColor(Color.BLACK);
            g.drawRect(0,0, 100,100);
            switch (die2) {
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
            dieImageLabel2.repaint();
        } finally {
            g.dispose();
        }

    }

    void roll() {
        die1 = rand.nextInt(6) + 1;
        die2 = rand.nextInt(6) + 1;
    }


}
