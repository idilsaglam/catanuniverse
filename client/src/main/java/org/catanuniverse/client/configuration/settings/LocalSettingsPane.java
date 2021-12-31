/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.configuration.settings;

import java.util.function.Consumer;
import org.catanuniverse.commons.Difficulty;
import org.catanuniverse.commons.GameSettings;
import org.catanuniverse.commons.LocalGameSettings;

final class LocalSettingsPane extends GameSettingsPane<LocalGameSettings> {

    private final CapacitySelector capacitySelector;
    private final NumberOfAISelector numberOfAISelector;
    private final DifficultySelector difficultySelector;

    protected LocalSettingsPane(Consumer<GameSettings> onGameSettingsChanged) {
        super(onGameSettingsChanged);

        // TODO: Create with default values of sliders
        super.settings =
                new LocalGameSettings(
                        GameSettingsPane.DEFAULT_CAPACITY, GameSettingsPane.DEFAULT_NB_AI);

        this.difficultySelector =
                new DifficultySelector(
                        (Difficulty difficulty) -> {
                            System.out.printf("Difficulty level changed to %s\n", difficulty);
                            super.settings.setDifficulty(difficulty);
                            super.onGameSettingsChanged.accept(super.settings);
                        });

        this.numberOfAISelector =
                new NumberOfAISelector(
                        GameSettingsPane.DEFAULT_NB_AI,
                        (Integer numberOfAI) -> {
                            System.out.printf("Number of AI slider value %d\n", numberOfAI);
                            boolean disable = numberOfAI == 0;
                            if (this.difficultySelector.isSliderEnabled() == disable) {
                                this.difficultySelector.setValue(0);
                                System.out.printf("Difficulty slider will be enabled %b", !disable);
                                this.difficultySelector.setSliderEnabled(!disable);
                                this.difficultySelector.repaintSlider();
                            }
                            super.settings.setNumberOfAI(numberOfAI);
                            super.onGameSettingsChanged.accept(super.settings);
                        });

        this.capacitySelector =
                new CapacitySelector(
                        GameSettingsPane.DEFAULT_CAPACITY,
                        (Integer capacity) -> {
                            System.out.printf("Capacity slider value %d\n", capacity);
                            this.numberOfAISelector.setMaximum(capacity - 1);
                            this.numberOfAISelector.repaintSlider();
                            super.settings.setCapacity(capacity);
                            super.onGameSettingsChanged.accept(super.settings);
                        });
        this.add(this.capacitySelector);
        this.add(this.numberOfAISelector);
        this.add(this.difficultySelector);
    }

    @Override
    boolean isSettingsValid() {
        return true;
    }
}
