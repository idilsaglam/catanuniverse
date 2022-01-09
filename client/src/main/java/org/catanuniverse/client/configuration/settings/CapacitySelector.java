/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.client.configuration.settings;

import java.util.function.Consumer;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

class CapacitySelector extends GameSettingsSelectorPanel {
    private final JSlider slider;

    /**
     * Creates a new capacity selector
     *
     * @param labelText The label text on the capacity selector
     * @param toolTipText The tooltip text of the capacity selector
     * @param min The minimum selectable value
     * @param max The maximum selectable value
     * @param current The current value of the selector
     * @param callback The callback function which will be called each time the selector is updated
     */
    private CapacitySelector(
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
        this.slider.addChangeListener(
                (ChangeEvent ignore) -> {
                    callback.accept(this.slider.getValue());
                });
        this.slider.setToolTipText(toolTipText);
        this.add(this.slider);
    }

    /**
     * Creates a capacity selector with only the callback function
     *
     * @param callback The callback function of the capacity selector
     */
    CapacitySelector(Consumer<Integer> callback) {
        this(4, callback);
    }

    /**
     * Creates a capacity selector with current value and callback function
     *
     * @param currentValue The current value of the capacity selector
     * @param callback The callback function which will be called each time the selector is updated
     */
    CapacitySelector(int currentValue, Consumer<Integer> callback) {
        this(
                "Select the room capacity",
                "Set the game room's capacity",
                3,
                4,
                currentValue,
                callback);
    }

    /**
     * Return the value of the capacity selector
     *
     * @return The value of the capacity selector
     */
    int getValue() {
        return this.slider.getValue();
    }

    /**
     * Updates the value of the capacity selector
     *
     * @param value The new value of the selector to update with
     */
    void setValue(int value) {
        this.slider.setValue(value);
    }
}
