/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game.board;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.catanuniverse.core.game.Player;

class TopStatusBar extends JPanel {
    private int nbPlayers;
    private Player[] players;
    static JLabel l;

    public TopStatusBar() throws IOException {
        JPanel j = new JPanel();
        j.setBackground(Color.red);
        j.setBorder(new EmptyBorder(10, 20, 10, 50));

        JLabel avatarLabel1;
        BufferedImage avatar1 = ImageIO.read(this.getClass().getResource("/avatar1.png"));
        Image dimg = avatar1.getScaledInstance(100,100,Image.SCALE_SMOOTH);
        avatarLabel1 = new JLabel(new ImageIcon(dimg));
        j.add(avatarLabel1);

        JPanel all = new JPanel();
        all.setLayout(new GridLayout(0,1));

        JPanel chars = new JPanel();

        JLabel bleLabel;
        BufferedImage ble = ImageIO.read(this.getClass().getResource("/sword.png"));
        Image bimg = ble.getScaledInstance(60,60,Image.SCALE_SMOOTH);
        bleLabel = new JLabel(new ImageIcon(bimg));
        chars.add(bleLabel);

        // create a label to display text
        l = new JLabel();
        // add text to label
        l.setText("8");
        // add label to panel
        chars.add(l);
        // set the size of frame
        chars.setSize(300, 300);


        JLabel sheepLabel;
        BufferedImage sheep = ImageIO.read(this.getClass().getResource("/route.png"));
        Image simg = sheep.getScaledInstance(60,60,Image.SCALE_SMOOTH);
        sheepLabel = new JLabel(new ImageIcon(simg));
        chars.add(sheepLabel);

        // create a label to display text
        l = new JLabel();
        // add text to label
        l.setText("2");
        // add label to panel
        chars.add(l);
        chars.setSize(300, 300);

        JPanel chars2 = new JPanel();

        JLabel mineralLabel;
        BufferedImage mineral = ImageIO.read(this.getClass().getResource("/card.png"));
        Image mimg = mineral.getScaledInstance(60,60,Image.SCALE_SMOOTH);
        mineralLabel = new JLabel(new ImageIcon(mimg));
        chars2.add(mineralLabel);

        // create a label to display text
        l = new JLabel();
        // add text to label
        l.setText("10");
        // add label to panel
        chars2.add(l);
        // set the size of frame
       chars2.setSize(300, 300);

        JLabel woodLabel;
        BufferedImage wood = ImageIO.read(this.getClass().getResource("/wood.png"));
        Image wimg = wood.getScaledInstance(60,60,Image.SCALE_SMOOTH);
        woodLabel = new JLabel(new ImageIcon(wimg));
        chars2.add(woodLabel);


        // create a label to display text
        l = new JLabel();
        // add text to label
        l.setText("0");
        // add label to panel
        chars2.add(l);
        // set the size of frame
        chars2.setSize(300, 300);

        JPanel chars3 = new JPanel();


        JLabel argileLabel;
        BufferedImage argile = ImageIO.read(this.getClass().getResource("/winn.png"));
        Image aimg = argile.getScaledInstance(60,60,Image.SCALE_SMOOTH);
        argileLabel = new JLabel(new ImageIcon(aimg));
        chars3.add(argileLabel);

        // create a label to display text
        l = new JLabel();
        // add text to label
        l.setText("5");
        // add label to panel
        chars3.add(l);
        // set the size of frame
        chars3.setSize(300, 300);


        JPanel j2 = new JPanel();
        j2.setBorder(new EmptyBorder(20, 20, 70, 250));
        j2.setBackground(Color.yellow);
        JLabel avatarLabel2;
        BufferedImage avatar2 = ImageIO.read(this.getClass().getResource("/avatar2.png"));
        Image limg = avatar2.getScaledInstance(100,100,Image.SCALE_SMOOTH);
        avatarLabel2 = new JLabel(new ImageIcon(limg));
        j2.add(avatarLabel2);

        JPanel j3 = new JPanel();
        j3.setBorder(new EmptyBorder(20, 20, 70, 250));
        j3.setBackground(Color.CYAN);
        JLabel avatarLabel3;
        BufferedImage avatar3 = ImageIO.read(this.getClass().getResource("/avatar3.png"));
        Image himg = avatar3.getScaledInstance(100,100,Image.SCALE_SMOOTH);
        avatarLabel3 = new JLabel(new ImageIcon(himg));
        j3.add(avatarLabel3);

        all.add(chars);
        all.add(chars2);
        all.add(chars3);
        add(j);
        j.add(all);
        add(j2);
        add(j3);

    }


    public TopStatusBar(Player[] players, int capacity) {
        this.setLayout(new GridLayout(1, capacity));
        this.players = new Player[capacity];
        for (int i = 0; i < players.length; i++) {
            this.players[i] = players[i];
            this.add(new PlayerContainer(this.players[i]));
            this.nbPlayers++;
        }
    }

    /**
     * Add a new player to the top pane
     *
     * @param p The new player to add
     * @return True if the player added correctly false if not
     */
    public boolean addPlayer(Player p) {
        if (this.nbPlayers == this.players.length - 1) {
            return false;
        }
        this.players[this.nbPlayers] = p;
        this.add(new PlayerContainer(this.players[this.nbPlayers]));
        this.nbPlayers++;
        return true;
    }

    private static class PlayerContainer extends JPanel {
        public PlayerContainer(Player p) {
            /*
            ImageIcon avatar;
            try {
                avatar = new Avatar(p.getUsername());
            } catch (IOException ignore) {
                avatar = new ImageIcon();
            }
            this.add(new JLabel(avatar));
            */
            JLabel label = new JLabel(p.getUsername());
            System.out.printf("Player container created for %s\n", p.getUsername());
            super.add(label);
        }
    }

    private static class PlayerDetailsContainer extends JPanel {}
}
