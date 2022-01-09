/*
	22015094 - Idil Saglam*/
package org.catanuniverse.client.configuration;

import javax.swing.JRadioButton;

class RadioButton extends JRadioButton {

    RadioButton(String text, String toolTip) {
        super(text);
        super.setToolTipText(toolTip);
    }

    RadioButton(String text) {
        super(text);
    }
}
