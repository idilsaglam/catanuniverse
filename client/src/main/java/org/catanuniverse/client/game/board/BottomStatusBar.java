/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game.board;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.html.ImageView;

class BottomStatusBar extends JPanel {
    // label to display text
    static JLabel l;
    int woodNumber, argileNumber,mineralNumber,bleNumber,sheepNumber;
    public BottomStatusBar() throws IOException {

        this.woodNumber= 0;
        this.argileNumber = 0;
        this.bleNumber = 0;
        this.sheepNumber = 0;
        this.mineralNumber = 0;

        this.setLayout(new GridLayout());


        this.add(this.getResourcesRow());

    }

    public JPanel getResourcesRow() throws IOException {
        JPanel result = new JPanel();

        // create a label to display text
        l = new JLabel();
        // add text to label
        l.setText(""+mineralNumber);
        // add label to panel
        result.add(l);

        JLabel mineralLabel;
        BufferedImage mineral = ImageIO.read(this.getClass().getResource("/mineral.png"));
        Image dimg = mineral.getScaledInstance(60,60,Image.SCALE_SMOOTH);
        mineralLabel = new JLabel(new ImageIcon(dimg));
        result.add(mineralLabel);


        // create a label to display text
        l = new JLabel();
        // add text to label
        l.setText(""+sheepNumber);
        // add label to panel
        result.add(l);

        JLabel sheepLabel;
        BufferedImage sheep = ImageIO.read(this.getClass().getResource("/sheep.png"));
        Image simg = sheep.getScaledInstance(60,60,Image.SCALE_SMOOTH);
        sheepLabel = new JLabel(new ImageIcon(simg));
        result.add(sheepLabel);

        // create a label to display text
        l = new JLabel();
        // add text to label
        l.setText(""+argileNumber);
        // add label to panel
        result.add(l);


        JLabel argileLabel;
        BufferedImage argile = ImageIO.read(this.getClass().getResource("/argile.png"));
        Image aimg = argile.getScaledInstance(60,60,Image.SCALE_SMOOTH);
        argileLabel = new JLabel(new ImageIcon(aimg));
        result.add(argileLabel);


        // create a label to display text
        l = new JLabel();
        // add text to label
        l.setText(""+bleNumber);
        // add label to panel
        result.add(l);

        JLabel bleLabel;
        BufferedImage ble = ImageIO.read(this.getClass().getResource("/ble.png"));
        Image bimg = ble.getScaledInstance(60,60,Image.SCALE_SMOOTH);
        bleLabel = new JLabel(new ImageIcon(bimg));
        result.add(bleLabel);



        // create a label to display text
        l = new JLabel();
        // add text to label
        l.setText(""+woodNumber);
        // add label to panel
        result.add(l);


        JLabel woodLabel;
        BufferedImage wood = ImageIO.read(this.getClass().getResource("/wood.png"));
        Image wimg = wood.getScaledInstance(60,60,Image.SCALE_SMOOTH);
        woodLabel = new JLabel(new ImageIcon(wimg));
        result.add(woodLabel);
        return result;
    }


}
