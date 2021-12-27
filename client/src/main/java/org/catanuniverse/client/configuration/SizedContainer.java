/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.client.configuration;

import java.awt.Dimension;
import javax.swing.JPanel;

abstract class SizedContainer extends JPanel {

    SizedContainer(Dimension size) {
        if (size != null) {
            super.setMaximumSize(size);
        }
    }

    SizedContainer() {
        this(null);
    }
}
