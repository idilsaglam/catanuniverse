package org.catanuniverse.client.game.board;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

class BoardSidePane extends JPanel {

    public BoardSidePane() throws IOException {
        this.setBackground(Color.MAGENTA);
        JLabel cardLabel;
        BufferedImage card = ImageIO.read(this.getClass().getResource("/card2.png"));
        Image simg = card.getScaledInstance(60,60,Image.SCALE_SMOOTH);
        cardLabel = new JLabel(new ImageIcon(simg));
        this.add(cardLabel);
    }

    @Override
    public void setSize(Dimension size) {
        super.setSize(size);
        super.setMinimumSize(size);
        super.setPreferredSize(size);
    }
}
