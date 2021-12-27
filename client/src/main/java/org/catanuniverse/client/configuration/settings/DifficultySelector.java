/*
	Idil Saglam
	Abdulrahim Toto
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

    DifficultySelector(Consumer<Difficulty> callback) {
        this(0, callback);
    }

    DifficultySelector(int currentValue, Consumer<Difficulty> callback) {
        this(
                "Choose the difficulty of the game",
                "Set the difficulty of the AI players",
                0,
                Difficulty.values().length - 1,
                currentValue,
                callback);
    }

    void setValue(int n) {
        this.slider.setValue(n);
    }

    void setSliderEnabled(boolean v) {
        this.slider.setEnabled(v);
    }

    void repaintSlider() {
        this.slider.repaint();
    }

    boolean isSliderEnabled() {
        return this.slider.isEnabled();
    }
}
