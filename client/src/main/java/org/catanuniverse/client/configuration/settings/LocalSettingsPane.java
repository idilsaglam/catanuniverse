/*
	22015094 - Idil Saglam
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
    private final VictoryPointsSelector victoryPointsSelector;

    protected LocalSettingsPane(Consumer<GameSettings> onGameSettingsChanged) {
        super(onGameSettingsChanged);
        super.settings =
                new LocalGameSettings(
                        GameSettingsPane.DEFAULT_CAPACITY,
                        GameSettingsPane.DEFAULT_NB_AI,
                        GameSettings.DEFAULT_VICTORY_POINTS);

        this.difficultySelector = new DifficultySelector(this::onDifficultySelectorUpdated);

        this.numberOfAISelector =
                new NumberOfAISelector(GameSettingsPane.DEFAULT_NB_AI, this::onAISelectorUpdated);

        this.capacitySelector =
                new CapacitySelector(
                        GameSettingsPane.DEFAULT_CAPACITY, this::onCapacitySelectorUpdated);
        this.victoryPointsSelector = new VictoryPointsSelector(this::onVictoryPointsUpdated);
        this.add(this.capacitySelector);
        this.add(this.numberOfAISelector);
        this.add(this.difficultySelector);
        this.add(this.victoryPointsSelector);
    }

    @Override
    boolean isSettingsValid() {
        return true;
    }

    /**
     * Handles the update of the victory points
     *
     * @param victoryPoints The number of victory points
     */
    private void onVictoryPointsUpdated(Integer victoryPoints) {

        super.settings.setVictoryPoints(victoryPoints);
        super.onGameSettingsChanged.accept(super.settings);
    }

    /**
     * Handles the update of the difficulty selector
     *
     * @param difficulty The difficulty value in the difficulty selector
     */
    private void onDifficultySelectorUpdated(Difficulty difficulty) {

        super.settings.setDifficulty(difficulty);
        super.onGameSettingsChanged.accept(super.settings);
    }

    /**
     * Handles the capacity selector update event
     *
     * @param capacity The value of the capacity selector
     */
    private void onCapacitySelectorUpdated(Integer capacity) {

        this.numberOfAISelector.setMaximum(capacity - 1);
        this.numberOfAISelector.repaintSlider();
        super.settings.setCapacity(capacity);
        super.onGameSettingsChanged.accept(super.settings);
    }

    /**
     * Handles the AI selector updated event
     *
     * @param numberOfAI The number of AIs selected in AI selector
     */
    private void onAISelectorUpdated(Integer numberOfAI) {

        boolean disable = numberOfAI == 0;
        if (this.difficultySelector.isSliderEnabled() == disable) {
            this.difficultySelector.setValue(0);

            this.difficultySelector.setSliderEnabled(!disable);
            this.difficultySelector.repaintSlider();
        }
        super.settings.setNumberOfAI(numberOfAI);
        super.onGameSettingsChanged.accept(super.settings);
    }
}
