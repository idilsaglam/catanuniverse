/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.client.configuration.settings;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

abstract class GameSettingsSelectorPanel extends JPanel {

    protected GameSettingsSelectorPanel(String labelText, String toolTipText) {
        JLabel label = new JLabel(labelText);
        this.setToolTipText(toolTipText);
        this.setLayout(new GridLayout(1, 2));
        this.add(label);
    }
}
