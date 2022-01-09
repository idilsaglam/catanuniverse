/*
	22015094 - Idil Saglam*/
package org.catanuniverse.client.configuration.settings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Consumer;
import org.catanuniverse.commons.Difficulty;
import org.catanuniverse.commons.GameSettings;
import org.catanuniverse.commons.MultiPlayerHostGameSettings;

final class MPHostSettingsPane extends GameSettingsPane<MultiPlayerHostGameSettings> {

    private final CapacitySelector capacitySelector;
    private final NumberOfAISelector numberOfAISelector;
    private DifficultySelector difficultySelector;
    private PortNumberSelector portNumberSelector;

    /**
     * Creates a new settings pane for multi player host settings pane
     *
     * @param onGameSettingsChanged The callback function that will be called each time the settings
     *     are updated
     */
    protected MPHostSettingsPane(Consumer<GameSettings> onGameSettingsChanged) {
        super(onGameSettingsChanged);
        GridBagConstraints gbc = new GridBagConstraints();
        super.setLayout(new GridBagLayout());
        // TODO: Create with default values of sliders
        super.settings =
                new MultiPlayerHostGameSettings(
                        GameSettings.DEFAULT_CAPACITY, GameSettings.DEFAULT_NUMBER_OF_AI);

        this.difficultySelector =
                new DifficultySelector(
                        (Difficulty difficulty) -> {
                            System.out.printf("Difficulty level changed to %s\n", difficulty);
                            super.settings.setDifficulty(difficulty);
                            super.onGameSettingsChanged.accept(super.settings);
                        });

        this.numberOfAISelector =
                new NumberOfAISelector(
                        (Integer numberOfAI) -> {
                            System.out.printf("Number of AI slider value %d\n", numberOfAI);
                            boolean disable = numberOfAI == 0;
                            if (this.difficultySelector.isSliderEnabled() == disable) {
                                this.difficultySelector.setValue(0);
                                System.out.printf("Difficulty slider will be enabled %b", !disable);
                                this.difficultySelector.setSliderEnabled(!disable);
                                this.difficultySelector.repaintSlider();
                                super.settings.setNumberOfAI(numberOfAI);
                                super.onGameSettingsChanged.accept(super.settings);
                            }
                        });

        this.capacitySelector =
                new CapacitySelector(
                        (Integer capacity) -> {
                            System.out.printf("Capacity slider value %d\n", capacity);
                            this.numberOfAISelector.setMaximum(capacity - 1);
                            this.numberOfAISelector.repaintSlider();
                            super.settings.setCapacity(capacity);
                            super.onGameSettingsChanged.accept(super.settings);
                        });

        this.portNumberSelector =
                new PortNumberSelector(
                        (Integer portNumber) -> {
                            System.out.printf("Port number is set to %d\n", portNumber);
                            super.settings.setPortNumber(portNumber);
                            super.onGameSettingsChanged.accept(super.settings);
                        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(this.capacitySelector, gbc);
        gbc.gridy = 1;
        this.add(this.numberOfAISelector, gbc);
        gbc.gridy = 2;

        this.add(this.difficultySelector, gbc);
        gbc.gridy = 3;
        this.add(this.portNumberSelector, gbc);
    }

    /**
     * Verify if settings are valid
     *
     * @return True if the settings are valid, false if not
     */
    boolean isSettingsValid() {
        return this.portNumberSelector.isPortNumberValid();
    }
}
