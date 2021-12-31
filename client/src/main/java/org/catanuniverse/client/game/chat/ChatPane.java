/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
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
        // TODO: Complete with the chat pane definition
        // TODO: On the top there should be a title
        // TODO: In the middle there should be a scroll pane
        // TODO: On the botton there should be a new message text box
    }
}
