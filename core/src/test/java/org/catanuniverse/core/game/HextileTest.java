/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.core.game;

import java.util.List;
import java.util.Random;
import org.catanuniverse.core.exceptions.NoSuchSlotException;
import org.catanuniverse.core.exceptions.SlotAlreadyTakenException;
import org.catanuniverse.core.exceptions.TileTypeNotSupportedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class HextileTest {

    private Hextile tile, neighbor, neighbor1, neighbor2;
    private Harbor harbor;

    @BeforeEach
    void initTile() {
        Random rnd = new Random();
        tile = new Hextile(rnd.nextInt(10), GroundType.Desert);
        neighbor = new Hextile(rnd.nextInt(10), GroundType.Farm);
        neighbor1 = new Hextile(rnd.nextInt(10), GroundType.Forest);
        neighbor2 = new Hextile(rnd.nextInt(10), GroundType.Hill);
        harbor = new Harbor(3);
    }

    @ParameterizedTest
    @DisplayName("Test adding neighbor to an empty slot")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void addNeighborTest(int index) {
        try {
            assert tile.getNeighbor(index) == null;
            assert neighbor.getNeighbor(tile.complementaryIndex(index)) == null;
        } catch (NoSuchSlotException e) {
            assert false;
        }
        try {
            tile.addNeighbor(index, neighbor);
        } catch (NoSuchSlotException
                | SlotAlreadyTakenException
                | TileTypeNotSupportedException e) {
            assert false;
        }
        try {
            assert tile.getNeighbor(index).equals(neighbor);
            assert neighbor.getNeighbor(tile.complementaryIndex(index)).equals(tile);
        } catch (NoSuchSlotException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Test adding multiple neighbors to the same slot")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void addMultipleNeighborsToSamePositionTest(int index) {
        try {
            tile.addNeighbor(index, neighbor);
        } catch (NoSuchSlotException
                | SlotAlreadyTakenException
                | TileTypeNotSupportedException e) {
            assert false;
        }
        try {
            tile.addNeighbor(index, neighbor);
            assert false;
        } catch (NoSuchSlotException | TileTypeNotSupportedException e) {
            assert false;
        } catch (SlotAlreadyTakenException e) {
            assert true;
        }
    }

    @ParameterizedTest
    @DisplayName("Adding a neighbor to an invalid position")
    @ValueSource(ints = {-1, 6})
    void addNeighborInvalidPositionTest(int index) {
        try {
            tile.addNeighbor(index, neighbor);
            assert false;
        } catch (NoSuchSlotException e) {
            assert true;
        } catch (SlotAlreadyTakenException | TileTypeNotSupportedException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Adding a neighbor to a slot occupied by an harbor")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void addNeighborOnAHarbor(int index) {
        try {
            tile.addHarbor(index, harbor);
        } catch (NoSuchSlotException | SlotAlreadyTakenException e) {
            assert false;
        }
        try {
            tile.addNeighbor(index, neighbor);
            assert false;
        } catch (SlotAlreadyTakenException e) {
            assert true;
        } catch (NoSuchSlotException | TileTypeNotSupportedException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Adding an harbor on an empty slot")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void addHarborTest(int index) {
        try {
            tile.addHarbor(index, harbor);
            assert tile.getHarbor(index).equals(harbor);
        } catch (SlotAlreadyTakenException | NoSuchSlotException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Adding an harbor on a inexisting slot")
    @ValueSource(ints = {-1, 6})
    void addHarborToInvalidSlot(int index) {
        try {
            tile.addHarbor(index, harbor);
            assert false;
        } catch (NoSuchSlotException e) {
            assert true;
        } catch (SlotAlreadyTakenException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Adding multiple neighbors to the same slot")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void addMultipleHarborsToTheSameSlot(int index) {
        try {
            tile.addHarbor(index, harbor);
        } catch (SlotAlreadyTakenException | NoSuchSlotException e) {
            assert false;
        }
        try {
            tile.addHarbor(index, harbor);
            assert false;
        } catch (SlotAlreadyTakenException e) {
            assert true;
        } catch (NoSuchSlotException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Add harbor on a neighbor")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void addHarborOnANeighbor(int index) {
        try {
            tile.addNeighbor(index, neighbor);
        } catch (NoSuchSlotException
                | SlotAlreadyTakenException
                | TileTypeNotSupportedException e) {
            assert false;
        }
        try {
            tile.addHarbor(index, harbor);
            assert false;
        } catch (NoSuchSlotException e) {
            assert false;
        } catch (SlotAlreadyTakenException e) {
            assert true;
        }
    }

    private static class Square extends Tile {
        /**
         * Create a new instance of Tile object
         *
         * @param id The id of the Tile
         * @param type The type of the Tile
         */
        protected Square(int id, GroundType type) {
            super(id, type, 4);
        }
    }

    @ParameterizedTest
    @DisplayName("Adding another tile type with hextile")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void addInvalidTileType(int index) {
        try {
            tile.addNeighbor(index, new Square(4, GroundType.Desert));
            assert false;
        } catch (NoSuchSlotException | SlotAlreadyTakenException e) {
            assert false;
        } catch (TileTypeNotSupportedException e) {
            assert true;
        }
    }

    @ParameterizedTest
    @DisplayName("Adding a new road to an empty slot")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void addRoadTest(int position) {
        try {
            tile.addRoad(position, new Road(null));
            assert tile.getRoadSlot(position) != null;
        } catch (NoSuchSlotException | SlotAlreadyTakenException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Adding a road to the intersection with the neighbor")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void addRoadIntersectionWithNeighborTest(int position) {
        Road r = new Road(null);
        try {
            tile.addNeighbor(position, neighbor);
            tile.addRoad(position, r);
            assert tile.getRoadSlot(position).equals(r);
            assert neighbor.getRoadSlot(tile.complementaryIndex(position)).equals(r);
        } catch (NoSuchSlotException
                | SlotAlreadyTakenException
                | TileTypeNotSupportedException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Get intersecting neighbors with 2 neighbors")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void getIntersectingNeighborsWith2Neighbors(int index) {
        int baseIndex = index;
        try {
            tile.addNeighbor(index, neighbor);
            index = (tile.complementaryIndex(index) - 1) % 6;
            neighbor.addNeighbor(index, neighbor1);
            index = (tile.complementaryIndex(index) + 1) % 6;
            assert baseIndex == index;
            List<Tile> intersection = tile.getInsersectingNeighbors(index);
            assert intersection.size() == 2;
            assert intersection.get(0).equals(neighbor);
            assert intersection.get(1).equals(neighbor1);
        } catch (NoSuchSlotException
                | SlotAlreadyTakenException
                | TileTypeNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @DisplayName("Get intersecting neighbors with only lower bound neighbor")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void getIntersectingNeighborsWithLowerNeighbor(int index) {
        try {
            tile.addNeighbor(index, neighbor);
            List<Tile> intersection = tile.getInsersectingNeighbors(index);
            assert intersection.size() == 1;
            assert intersection.get(0).equals(neighbor);
        } catch (NoSuchSlotException
                | SlotAlreadyTakenException
                | TileTypeNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @DisplayName("Get intersecting neighbors with only upper bound neighbor")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void getIntersectingNeighborsWithUpperNeighbor(int index) {
        try {
            index++;
            tile.addNeighbor(index, neighbor);
            List<Tile> intersection = tile.getInsersectingNeighbors(index);
            assert intersection.size() == 1;
            assert intersection.get(0).equals(neighbor);
        } catch (NoSuchSlotException
                | SlotAlreadyTakenException
                | TileTypeNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @DisplayName("Adding two roads to the same slot")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void addRoadNotEmptySlotTest(int position) {
        Road r = new Road(null);
        try {
            tile.addRoad(position, r);
        } catch (NoSuchSlotException | SlotAlreadyTakenException e) {
            assert false;
        }
        try {
            tile.addRoad(position, r);
            assert false;
        } catch (NoSuchSlotException e) {
            assert false;
        } catch (SlotAlreadyTakenException e) {
            assert true;
        }
    }

    @ParameterizedTest
    @DisplayName("Testing to add a road on an invalid position")
    @ValueSource(ints = {-1, 6})
    void addRoadInvalidPosition(int position) {
        try {
            tile.addRoad(position, new Road(null));
            assert false;
        } catch (NoSuchSlotException e) {
            assert true;
        } catch (SlotAlreadyTakenException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Adding a settlement to a non empty slot")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void addSettlementTest(int position) {
        try {
            tile.addSettlement(position, new Settlement(null));
            assert tile.getSettlementSlot(position) != null;
        } catch (NoSuchSlotException | SlotAlreadyTakenException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Adding two colonies to a same slot")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void addSettlementNotEmptySlot(int position) {
        Settlement c = new Settlement(null);
        try {
            tile.addSettlement(position, c);
        } catch (NoSuchSlotException | SlotAlreadyTakenException e) {
            assert false;
        }
        try {
            tile.addSettlement(position, c);
            assert false;
        } catch (NoSuchSlotException e) {
            assert false;
        } catch (SlotAlreadyTakenException e) {
            assert true;
        }
    }

    @ParameterizedTest
    @DisplayName("Add settlement to an invalid position")
    @ValueSource(ints = {-1, 6})
    void addSettlementInvalidPosition(int position) {
        try {
            tile.addSettlement(position, new Settlement(null));
            assert false;
        } catch (NoSuchSlotException e) {
            assert true;
        } catch (SlotAlreadyTakenException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Test all possible positions of a newly created tile's harbor slots")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void getHarborTest(int index) {
        try {
            assert tile.getHarbor(index) == null;
        } catch (NoSuchSlotException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Test invalid positions for harbor slots")
    @ValueSource(ints = {-1, 6})
    void getHarborInvalidPositionsTest(int index) {
        try {
            tile.getHarbor(index);
            assert false;
        } catch (NoSuchSlotException e) {
            assert true;
        }
    }

    @ParameterizedTest
    @DisplayName("Test all possible positions of a newly created tile's neighbors")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void getNeighborTest(int p) {
        try {
            assert tile.getNeighbor(p) == null;
        } catch (NoSuchSlotException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Get neighbors from invalid positions")
    @ValueSource(ints = {-1, 6})
    void getNeighborInvalidPositionTest(int position) {
        try {
            tile.getNeighbor(position);
            assert false;
        } catch (NoSuchSlotException e) {
            assert true;
        }
    }

    @ParameterizedTest
    @DisplayName("Test all possible positions possible for road slots")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void getRoadTest(int position) {
        try {
            assert tile.getRoadSlot(position) == null;
        } catch (NoSuchSlotException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Test invalid positions for road slots")
    @ValueSource(ints = {-1, 6})
    void getRoadInvalidPositionsTest(int position) {
        try {
            tile.getRoadSlot(position);
            assert false;
        } catch (NoSuchSlotException e) {
            assert true;
        }
    }

    @ParameterizedTest
    @DisplayName("Test all possible positions for settlement slots")
    @ValueSource(ints = {0, 1, 2, 3, 4, 5})
    void getSettlementTest(int position) {
        try {
            assert tile.getSettlementSlot(position) == null;
        } catch (NoSuchSlotException e) {
            assert false;
        }
    }

    @ParameterizedTest
    @DisplayName("Testing to access an invalid settlement slot")
    @ValueSource(ints = {-1, 6})
    void getSettlementInvalidPositionsTest(int position) {
        try {
            tile.getSettlementSlot(position);
            assert false;
        } catch (NoSuchSlotException e) {
            assert true;
        }
    }
}
