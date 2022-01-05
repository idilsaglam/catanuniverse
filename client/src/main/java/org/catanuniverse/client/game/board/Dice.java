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
    private JButton rollBtn = null;
    private JTextField dieValueTF = null;
    private JLabel dieLabel = null;
    private int top = 50, left = 100;

    private BufferedImage dieImage;
    private JLabel dieImageLabel;
    public Dice() {



        dieImage = new BufferedImage(100,100, BufferedImage.TYPE_INT_RGB);
        this.setSize(6370, 3160);
        this.setPreferredSize(new Dimension(400, 200));
        this.setBackground(Color.pink);

        dieLabel = new JLabel();
        dieLabel.setBounds(new Rectangle(147, 28, 60, 20));
        dieLabel.setText("Die Value:");

        this.setLayout(null);

        this.add(getDieValueTF());
        this.add(dieLabel, null);
        dieImageLabel = new JLabel(new ImageIcon(dieImage));
        dieImageLabel.setBounds(200,100, 100,100);
        this.add(dieImageLabel);
        this.setBorder(new EmptyBorder(1000, 2000, 1000, 400));

        this.add(getRollBtn());
        displayDie();
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

    void displayDie(){
        dieValueTF.setText(String.valueOf(die1));
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

    void roll() {
        die1 = rand.nextInt(6) + 1;
    }


}
