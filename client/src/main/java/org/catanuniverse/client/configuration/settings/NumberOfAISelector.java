/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.configuration.settings;

import java.util.function.Consumer;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

class NumberOfAISelector extends GameSettingsSelectorPanel {
    private final JSlider slider;

    /**
     * Creates a new Number of AI selector
     *
     * @param labelText The text of the label of the selector
     * @param toolTipText The text of the tooltip
     * @param min The minimum selectable value
     * @param max The maximum selectable value
     * @param current The current value of the selector
     * @param callback The callback function that will be called each time the selector is updated
     */
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

    /**
     * Creates a new number of AI selector with a callback function
     *
     * @param callback The callback function that will be called each time the selector isupdated
     */
    NumberOfAISelector(Consumer<Integer> callback) {
        this(0, callback);
    }

    /**
     * Creates a new number of AI selector with current value and callback function
     *
     * @param currentValue The current value of the selector
     * @param callback The callback function that will be called each time the selector is updated
     */
    NumberOfAISelector(int currentValue, Consumer<Integer> callback) {
        this(
                "Select the number of AIs in the room",
                "Set the number of AI players in the current game room",
                0,
                3,
                currentValue,
                callback);
    }

    /**
     * Updates the maximum selectable value of the selector
     *
     * @param n The new maximum selectable value
     */
    protected void setMaximum(int n) {
        this.slider.setMaximum(n);
        if (this.slider.getValue() > this.slider.getMaximum()) {
            this.slider.setValue(this.slider.getMaximum());
        }
    }

    /** Repaint the slider */
    protected void repaintSlider() {
        this.slider.repaint();
    }
}
