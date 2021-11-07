/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.core.models;

import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Objects;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class GroupTypeTest {

    private static Stream<Arguments> groundTypeResourceCouples() {
        return Stream.of(
                arguments(GroundType.Forest, Resource.Wood),
                arguments(GroundType.Farm, Resource.Corn),
                arguments(GroundType.Mountain, Resource.Mineral),
                arguments(GroundType.Meadow, Resource.Wool),
                arguments(GroundType.Hill, Resource.Clay),
                arguments(GroundType.Desert, null));
    }

    @ParameterizedTest
    @MethodSource("groundTypeResourceCouples")
    void producesTest(GroundType g, Resource r) {
        assert Objects.equals(g.produces(), r);
    }
}
