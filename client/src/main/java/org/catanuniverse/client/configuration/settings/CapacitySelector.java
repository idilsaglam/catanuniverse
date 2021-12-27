/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.client.configuration.settings;

import java.util.function.Consumer;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

class CapacitySelector extends GameSettingsSelectorPanel {
    private final JSlider slider;

    private CapacitySelector(
            String labelText,
            String toolTipText,
            int min,
            int max,
            int current,
            Consumer<Integer> callback) {
        super(labelText, toolTipText);
        System.out.printf("Min %d Max %d Current %d\n", min, max, current);
        this.slider = new JSlider(JSlider.HORIZONTAL, min, max, current);
        this.slider.setMinorTickSpacing(0);
        this.slider.setMajorTickSpacing(1);
        this.slider.setPaintLabels(true);
        this.slider.setPaintTicks(true);
        this.slider.addChangeListener(
                (ChangeEvent ignore) -> {
                    callback.accept(this.slider.getValue());
                });
        this.slider.setToolTipText(toolTipText);
        this.add(this.slider);
    }

    CapacitySelector(Consumer<Integer> callback) {
        this(4, callback);
    }

    CapacitySelector(int currentValue, Consumer<Integer> callback) {
        this(
                "Select the room capacity",
                "Set the game room's capacity",
                3,
                4,
                currentValue,
                callback);
    }

    int getValue() {
        return this.slider.getValue();
    }

    void setValue(int value) {
        this.slider.setValue(value);
    }
}
