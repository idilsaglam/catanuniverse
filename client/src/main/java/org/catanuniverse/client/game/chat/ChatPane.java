/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.client.game.chat;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

public class ChatPane extends JPanel {

    public ChatPane(Dimension size) {
        super.setMinimumSize(size);
        super.setSize(size);
        super.setPreferredSize(size);
        super.setBackground(Color.LIGHT_GRAY);
    }
}
