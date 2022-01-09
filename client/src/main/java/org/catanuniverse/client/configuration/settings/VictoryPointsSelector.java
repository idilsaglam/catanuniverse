/*
	22015094 - Idil Saglam*/
package org.catanuniverse.client.configuration.settings;

import java.util.function.Consumer;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import org.catanuniverse.commons.GameSettings;

class VictoryPointsSelector extends GameSettingsSelectorPanel {

    private final JSlider slider;

    /**
     * Creates a new victory points selector
     *
     * @param labelText The text of the label
     * @param toolTipText The tooltip text
     * @param min The min value of the selector
     * @param max The max value of the selector
     * @param current The current value of the selector
     * @param callback The callback method which will be called each time the selector is updated
     */
    private VictoryPointsSelector(
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
        this.slider.addChangeListener(
                (ChangeEvent ignore) -> {
                    callback.accept(this.slider.getValue());
                });
        this.slider.setToolTipText(toolTipText);
        this.add(this.slider);
    }

    VictoryPointsSelector(Consumer<Integer> callback) {
        this(
                "Max victory points",
                "Set the number of victory points to win the game",
                3,
                15,
                GameSettings.DEFAULT_VICTORY_POINTS,
                callback);
    }

    /**
     * Get the value of the victory points selector slider
     *
     * @return The value of slider
     */
    int getValue() {
        return this.slider.getValue();
    }
}
