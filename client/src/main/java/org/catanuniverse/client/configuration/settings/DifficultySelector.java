/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.configuration.settings;

import java.util.Hashtable;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import org.catanuniverse.commons.Difficulty;

class DifficultySelector extends GameSettingsSelectorPanel {

    private final JSlider slider;

    /**
     * Creates a difficulty selector with given parameters
     *
     * @param labelText The label text of the difficulty selector
     * @param toolTipText The tooltip text of the selector
     * @param min The minimum selectable value
     * @param max The maximum selectable value
     * @param current The current value of the selector
     * @param callback The callback function which will be called each time the selector is updated
     */
    DifficultySelector(
            String labelText,
            String toolTipText,
            int min,
            int max,
            int current,
            Consumer<Difficulty> callback) {
        super(labelText, toolTipText);
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        Difficulty[] difficulties = Difficulty.values();
        for (int i = 0; i < difficulties.length; i++) {
            labels.put(i, new JLabel(difficulties[i].toString()));
        }
        this.slider = new JSlider(JSlider.HORIZONTAL, min, max, current);
        this.slider.addChangeListener(
                (ChangeEvent ignore) -> {
                    callback.accept(Difficulty.fromInteger(this.slider.getValue()));
                });
        this.slider.setLabelTable(labels);
        this.slider.setMajorTickSpacing(1);
        this.slider.setMinorTickSpacing(0);
        this.slider.setPaintLabels(true);
        this.slider.setPaintTicks(true);
        this.slider.setEnabled(false);
        this.add(this.slider);
    }

    /**
     * Creates a difficulty selector with only the callback function
     *
     * @param callback The callback function that will be called each time the selector is updated
     */
    DifficultySelector(Consumer<Difficulty> callback) {
        this(0, callback);
    }

    /**
     * Creates a difficulty selector with current value and the callback function
     *
     * @param currentValue The current value of the selector
     * @param callback The callback function that will be called each time the selector is updated
     */
    DifficultySelector(int currentValue, Consumer<Difficulty> callback) {
        this(
                "Choose the difficulty of the game",
                "Set the difficulty of the AI players",
                0,
                Difficulty.values().length - 1,
                currentValue,
                callback);
    }

    /**
     * Updates the value of the selector
     *
     * @param n The new value of the selector
     */
    void setValue(int n) {
        this.slider.setValue(n);
    }

    /**
     * Sets if the slider is enabled or not
     *
     * @param v A boolean indicating that if the slider is enabled or not
     */
    void setSliderEnabled(boolean v) {
        this.slider.setEnabled(v);
    }

    /** Reapints the slider */
    void repaintSlider() {
        this.slider.repaint();
    }

    /**
     * Verify if the slider is enabled or not
     *
     * @return True if the slider is enabled, false if not
     */
    boolean isSliderEnabled() {
        return this.slider.isEnabled();
    }
}
