/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

import java.util.Random;
import org.catanuniverse.core.exceptions.InvalidPositionException;
import org.catanuniverse.core.exceptions.SlotAlreadyTakenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class HextileTest {

    private Tile tile, neighbor;

    @BeforeEach
    void initTile() {
        Random rnd = new Random();
        tile = new Hextile(rnd.nextInt(10), GroundType.Desert);
        neighbor = new Hextile(rnd.nextInt(10), GroundType.Farm);
    }

    @ParameterizedTest
    @DisplayName("Test adding neighbor to an empty slot")
    @EnumSource(
            value = Positions.class,
            names = {"LEFT", "TOP_LEFT", "TOP_RIGHT", "RIGHT", "BOTTOM_RIGHT", "BOTTOM_LEFT"})
    void addNeighborTest(Positions position) {
        try {
            assert tile.getNeighbor(position) == null;
            assert neighbor.getNeighbor(position.reversed()) == null;
        } catch (InvalidPositionException e) {
            assert false;
        }
        try {
            tile.addNeighbor(position, neighbor);
        } catch (InvalidPositionException | SlotAlreadyTakenException e) {
            assert false;
        }
        try {
            assert tile.getNeighbor(position).equals(neighbor);
            assert neighbor.getNeighbor(position.reversed()).equals(tile);
        } catch (InvalidPositionException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Test adding multiple neighbors to the same slot")
    @EnumSource(
            value = Positions.class,
            names = {"LEFT", "TOP_LEFT", "TOP_RIGHT", "RIGHT", "BOTTOM_RIGHT", "BOTTOM_LEFT"})
    void addMultipleNeighborsToSamePositionTest(Positions position) {
        try {
            tile.addNeighbor(position, neighbor);
        } catch (InvalidPositionException | SlotAlreadyTakenException e) {
            assert false;
        }
        try {
            tile.addNeighbor(position, neighbor);
            assert false;
        } catch (InvalidPositionException e) {
            assert false;
        } catch (SlotAlreadyTakenException e) {
            assert true;
        }
    }

    @ParameterizedTest
    @DisplayName("Adding a neighbor to an invalid position")
    @EnumSource(
            value = Positions.class,
            names = {"TOP", "BOTTOM"})
    void addNeighborInvalidPositionTest(Positions position) {
        try {
            tile.addNeighbor(position, neighbor);
            assert false;
        } catch (InvalidPositionException e) {
            assert true;
        } catch (SlotAlreadyTakenException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Adding a new road to an empty slot")
    @EnumSource(
            value = Positions.class,
            names = {"LEFT", "TOP_LEFT", "TOP_RIGHT", "RIGHT", "BOTTOM_RIGHT", "BOTTOM_LEFT"})
    void addRoadTest(Positions position) {
        try {
            tile.addRoad(position, new Road(null));
            assert tile.getRoadSlot(position) != null;
        } catch (InvalidPositionException | SlotAlreadyTakenException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Adding two roads to the same slot")
    @EnumSource(
            value = Positions.class,
            names = {"LEFT", "TOP_LEFT", "TOP_RIGHT", "RIGHT", "BOTTOM_RIGHT", "BOTTOM_LEFT"})
    void addRoadNotEmptySlotTest() {
        Road r = new Road(null);
        try {
            tile.addRoad(Positions.LEFT, r);
        } catch (InvalidPositionException | SlotAlreadyTakenException e) {
            assert false;
        }
        try {
            tile.addRoad(Positions.LEFT, r);
            assert false;
        } catch (InvalidPositionException e) {
            assert false;
        } catch (SlotAlreadyTakenException e) {
            assert true;
        }
    }

    @ParameterizedTest
    @DisplayName("Testing to add a road on an invalid position")
    @EnumSource(
            value = Positions.class,
            names = {"TOP", "BOTTOM"})
    void addRoadInvalidPosition(Positions position) {
        try {
            tile.addRoad(position, new Road(null));
            assert false;
        } catch (InvalidPositionException e) {
            assert true;
        } catch (SlotAlreadyTakenException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Adding a settlement to a non empty slot")
    @EnumSource(
            value = Positions.class,
            names = {"TOP_LEFT", "TOP", "TOP_RIGHT", "BOTTOM_RIGHT", "BOTTOM", "BOTTOM_LEFT"})
    void addSettlementTest(Positions position) {
        try {
            tile.addSettlement(position, new Settlement(null));
            assert tile.getSettlementSlot(position) != null;
        } catch (InvalidPositionException | SlotAlreadyTakenException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Adding two colonies to a same slot")
    @EnumSource(
            value = Positions.class,
            names = {"TOP_LEFT", "TOP", "TOP_RIGHT", "BOTTOM_RIGHT", "BOTTOM", "BOTTOM_LEFT"})
    void addSettlementNotEmptySlot(Positions position) {
        Settlement c = new Settlement(null);
        try {
            tile.addSettlement(position, c);
        } catch (InvalidPositionException | SlotAlreadyTakenException e) {
            assert false;
        }
        try {
            tile.addSettlement(position, c);
            assert false;
        } catch (InvalidPositionException e) {
            assert false;
        } catch (SlotAlreadyTakenException e) {
            assert true;
        }
    }

    @ParameterizedTest
    @DisplayName("Add settlement to an invalid position")
    @EnumSource(
            value = Positions.class,
            names = {"LEFT", "RIGHT"})
    void addSettlementInvalidPosition(Positions position) {
        try {
            tile.addSettlement(position, new Settlement(null));
            assert false;
        } catch (InvalidPositionException e) {
            assert true;
        } catch (SlotAlreadyTakenException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Test all possible positions of a newly created tile's neighbors")
    @EnumSource(
            value = Positions.class,
            names = {"LEFT", "TOP_LEFT", "TOP_RIGHT", "RIGHT", "BOTTOM_RIGHT", "BOTTOM_LEFT"})
    void getNeighborTest(Positions p) {
        try {
            assert tile.getNeighbor(p) == null;
        } catch (InvalidPositionException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Get neighbors from invalid positions")
    @EnumSource(
            value = Positions.class,
            names = {"TOP", "BOTTOM"})
    void getNeighborInvalidPositionTest(Positions position) {
        try {
            tile.getNeighbor(position);
            assert false;
        } catch (InvalidPositionException e) {
            assert true;
        }
    }

    @ParameterizedTest
    @DisplayName("Test all possible positions possible for road slots")
    @EnumSource(
            value = Positions.class,
            names = {"TOP_LEFT", "TOP_RIGHT", "RIGHT", "BOTTOM_RIGHT", "BOTTOM_LEFT", "LEFT"})
    void getRoadTest(Positions position) {
        try {
            assert tile.getRoadSlot(position) == null;
        } catch (InvalidPositionException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Test invalid positions for road slots")
    @EnumSource(
            value = Positions.class,
            names = {"TOP", "BOTTOM"})
    void getRoadInvalidPositionsTest(Positions position) {
        try {
            tile.getRoadSlot(position);
            assert false;
        } catch (InvalidPositionException e) {
            assert true;
        }
    }

    @ParameterizedTest
    @DisplayName("Test all possible positions for settlement slots")
    @EnumSource(
            value = Positions.class,
            names = {"TOP_LEFT", "TOP", "TOP_RIGHT", "BOTTOM_RIGHT", "BOTTOM", "BOTTOM_LEFT"})
    void getSettlementTest(Positions position) {
        try {
            assert tile.getSettlementSlot(position) == null;
        } catch (InvalidPositionException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Testing to access an invalid settlement slot")
    @EnumSource(
            value = Positions.class,
            names = {"LEFT", "RIGHT"})
    void getSettlementInvalidPositionsTest(Positions position) {
        try {
            tile.getSettlementSlot(position);
            assert false;
        } catch (InvalidPositionException e) {
            assert true;
        }
    }
}
