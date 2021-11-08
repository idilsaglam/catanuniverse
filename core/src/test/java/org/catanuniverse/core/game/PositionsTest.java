/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.catanuniverse.core.exceptions.InvalidPositionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PositionsTest {

    private static Stream<Arguments> positionToIndex() {
        return Stream.of(
                arguments(Positions.TOP, 0, true),
                arguments(Positions.TOP_RIGHT, 1, true),
                arguments(Positions.BOTTOM_RIGHT, 2, true),
                arguments(Positions.BOTTOM, 3, true),
                arguments(Positions.BOTTOM_LEFT, 4, true),
                arguments(Positions.TOP_LEFT, 5, true),
                arguments(Positions.TOP_LEFT, 0, false),
                arguments(Positions.TOP_RIGHT, 1, false),
                arguments(Positions.RIGHT, 2, false),
                arguments(Positions.BOTTOM_RIGHT, 3, false),
                arguments(Positions.BOTTOM_LEFT, 4, false),
                arguments(Positions.LEFT, 5, false));
    }

    private static Stream<Arguments> positionCouples() {
        return Stream.of(
                arguments(Positions.TOP_LEFT, Positions.BOTTOM_RIGHT),
                arguments(Positions.TOP, Positions.BOTTOM),
                arguments(Positions.TOP_RIGHT, Positions.BOTTOM_LEFT),
                arguments(Positions.RIGHT, Positions.LEFT),
                arguments(Positions.BOTTOM_RIGHT, Positions.TOP_LEFT),
                arguments(Positions.BOTTOM, Positions.TOP),
                arguments(Positions.BOTTOM_LEFT, Positions.TOP_RIGHT),
                arguments(Positions.LEFT, Positions.RIGHT));
    }

    @ParameterizedTest
    @MethodSource("positionToIndex")
    @DisplayName("Test index related to positions")
    void computeIndexTest(Positions position, int expected, boolean isCorner) {
        try {
            assert position.computeIndex(isCorner) == expected;
        } catch (InvalidPositionException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @MethodSource("positionCouples")
    @DisplayName("Test reverse position")
    void reversedTest(Positions position, Positions expected) {
        assert position.reversed().equals(expected);
    }
}
