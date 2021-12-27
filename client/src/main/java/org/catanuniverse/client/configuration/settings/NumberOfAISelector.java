/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.client.configuration.settings;

import java.util.function.Consumer;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

class NumberOfAISelector extends GameSettingsSelectorPanel {
    private final JSlider slider;

    private NumberOfAISelector(
            String labelText,
            String toolTipText,
            int min,
            int max,
            int current,
            Consumer<Integer> callback) {
        super(labelText, toolTipText);
        this.slider = new JSlider(JSlider.HORIZONTAL, min, max, current);
        this.slider.setMinorTickSpacing(0);
        this.slider.setMajorTickSpacing(1);
        this.slider.setPaintLabels(true);
        this.slider.setPaintTicks(true);
        this.slider.setToolTipText(toolTipText);
        this.slider.addChangeListener(
                (ChangeEvent ignore) -> {
                    callback.accept(this.slider.getValue());
                });
        this.add(this.slider);
    }

    NumberOfAISelector(Consumer<Integer> callback) {
        this(0, callback);
    }

    NumberOfAISelector(int currentValue, Consumer<Integer> callback) {
        this(
                "Select the number of AIs in the room",
                "Set the number of AI players in the current game room",
                0,
                3,
                currentValue,
                callback);
    }

    protected void setMaximum(int n) {
        this.slider.setMaximum(n);
        if (this.slider.getValue() > this.slider.getMaximum()) {
            this.slider.setValue(this.slider.getMaximum());
        }
    }

    protected void repaintSlider() {
        this.slider.repaint();
    }
}
