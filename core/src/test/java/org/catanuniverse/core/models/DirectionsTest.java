/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.models;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;
import org.catanuniverse.core.exceptions.InvalidDirectionException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class DirectionsTest {

    private static Stream<Arguments> directionToIndex() {
        return Stream.of(
                arguments(Directions.Top, 0, true),
                arguments(Directions.TopRight, 1, true),
                arguments(Directions.BottomRight, 2, true),
                arguments(Directions.Bottom, 3, true),
                arguments(Directions.BottomLeft, 4, true),
                arguments(Directions.TopLeft, 5, true),
                arguments(Directions.TopLeft, 0, false),
                arguments(Directions.TopRight, 1, false),
                arguments(Directions.Right, 2, false),
                arguments(Directions.BottomRight, 3, false),
                arguments(Directions.BottomLeft, 4, false),
                arguments(Directions.Left, 5, false));
    }

    private static Stream<Arguments> directionCouples() {
        return Stream.of(
                arguments(Directions.TopLeft, Directions.BottomRight),
                arguments(Directions.Top, Directions.Bottom),
                arguments(Directions.TopRight, Directions.BottomLeft),
                arguments(Directions.Right, Directions.Left),
                arguments(Directions.BottomRight, Directions.TopLeft),
                arguments(Directions.Bottom, Directions.Top),
                arguments(Directions.BottomLeft, Directions.TopRight),
                arguments(Directions.Left, Directions.Right));
    }

    @ParameterizedTest
    @MethodSource("directionToIndex")
    void computeIndexTest(Directions direction, int expected, boolean isCorner) {
        try {
            assert direction.computeIndex(isCorner) == expected;
        } catch (InvalidDirectionException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @MethodSource("directionCouples")
    void reversedTest(Directions direction, Directions expected) {
        assert direction.reversed().equals(expected);
    }
}
