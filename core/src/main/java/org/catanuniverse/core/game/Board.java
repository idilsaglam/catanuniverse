/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.core.game;

import java.util.*;
import java.util.function.Supplier;
import org.catanuniverse.core.exceptions.NoSuchSlotException;
import org.catanuniverse.core.exceptions.SlotAlreadyTakenException;
import org.catanuniverse.core.exceptions.TileTypeNotSupportedException;

public class Board {

    // The tile on the top left corner of the board
    private final Hextile[][] tiles;

    public Board(int size)
            throws NoSuchSlotException, SlotAlreadyTakenException, TileTypeNotSupportedException {
        this.tiles = new Hextile[size][];
        final int half = size / 2;
        int rowLength;
        for (int row = 0; row < half; row++) {
            rowLength = size - Math.abs(row - half);
            this.tiles[row] = new Hextile[rowLength];
            this.tiles[size - row - 1] = new Hextile[rowLength];
        }
        this.tiles[half] = new Hextile[size];
        this.initTiles();
        this.initNeighbors();
    }

    /**
     * Method returns harbor if player can exchange their resources
     *
     * @param player The player to check
     * @return Harbor owned by the given player
     */
    public java.util.Set<Harbor> getHarborsOfPlayer(Player player) {
        // resouces && settlement
        Hextile tile;
        Harbor harbor;
        Resource harborResource;
        Set<Harbor> harbors = new HashSet<>();
        for (int row = 0; row < this.tiles.length; row++) {
            for (int column = 0; column < this.tiles[row].length; column++) {
                if (((row == 1)
                                || (row == this.tiles.length - 2)
                                || (column == 0)
                                || (column == this.tiles[row].length - 2))
                        && this.tiles[row][column].hasHarbor()) {
                    int harborIndex = this.tiles[row][column].getHarborIndex();
                    tile = this.tiles[row][column];
                    if ((tile.getSettlementSlot(harborIndex) != null
                                    && tile.getSettlementSlot(harborIndex).getOwner().uid
                                            == player.uid)
                            || (tile.getSettlementSlot((harborIndex + 1) % Hextile.NB_SIDES) != null
                                    && tile.getSettlementSlot((harborIndex + 1) % Hextile.NB_SIDES)
                                                    .getOwner()
                                                    .uid
                                            == player.uid)) {

                        try {
                            harbor = tile.getHarbor(tile.getHarborIndex());
                            harborResource = harbor.getResource();
                            if (harborResource == null) {
                                harbors.add(harbor);
                                continue;
                            }
                            harbors.add(harbor);
                        } catch (NoSuchSlotException ignore) {
                        }
                    }
                }
            }
        }
        return harbors;
    }

    /**
     * Get the hextile in a specific row and column
     *
     * @param row The row
     * @param column The column
     * @return The hextile in the given row and column
     * @throws IndexOutOfBoundsException if there's no slot for given row and column
     */
    public Hextile get(int row, int column) throws IndexOutOfBoundsException {
        return this.tiles[row][column];
    }

    /**
     * Return matrix of hextiles
     *
     * @return two dimension array of hextiles
     */
    public Hextile[][] getHextiles() {
        return this.tiles;
    }

    /** Initialise board tiles with random resources and random ids */
    private void initTiles() {
        boolean desertExists = false;
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[i].length; j++) {

                if ((i == 0)
                        || (i == this.tiles.length - 1)
                        || (j == 0)
                        || (j == this.tiles[i].length - 1)) {
                    this.tiles[i][j] = new Hextile(-1, GroundType.Water);
                    continue;
                }
                GroundType groundType = GroundType.random(desertExists);
                if (groundType == GroundType.Desert) {
                    desertExists = true;
                    this.tiles[i][j] = new Hextile(7, GroundType.Desert);
                    continue;
                }
                Supplier<Integer> generateLabelId =
                        () -> {
                            Random r = new Random();
                            int result;
                            do {
                                result = r.nextInt(12) + 1;
                            } while (result == 7);
                            return result;
                        };
                this.tiles[i][j] = new Hextile(generateLabelId.get(), groundType);
            }
        }
    }

    /** Initialise neighbors for each tile of the board */
    private void initNeighbors()
            throws NoSuchSlotException, SlotAlreadyTakenException, TileTypeNotSupportedException {
        final int half = this.tiles.length / 2;
        for (int i = 0; i < half; i++) {
            for (int j = 0; j < this.tiles[i].length; j++) {
                this.tiles[i][j].addNeighbor(4, this.tiles[i + 1][j]);
                this.tiles[i][j].addNeighbor(3, this.tiles[i + 1][j + 1]);
            }
        }
        for (int i = this.tiles.length - 1; i > half; i--) {
            for (int j = 0; j < this.tiles[i].length; j++) {
                this.tiles[i][j].addNeighbor(0, this.tiles[i - 1][j]);
                this.tiles[i][j].addNeighbor(1, this.tiles[i - 1][j + 1]);
            }
        }
        for (Hextile[] tile : this.tiles) {
            for (int j = 0; j < tile.length - 1; j++) {
                tile[j].addNeighbor(2, tile[j + 1]);
            }
        }
        for (int i = 0; i < this.tiles.length; i++) {
            if (i == 0 || i == this.tiles.length - 1) {
                this.tiles[i][0].addHarbor(Harbor.random());
                this.tiles[i][2].addHarbor(Harbor.random());
            }
            this.tiles[i][(i % 2 == 0) ? 0 : this.tiles[i].length - 1].addHarbor(Harbor.random());
        }
    }

    /**
     * Checks if a tile with given number exists or not
     *
     * @param diceResult The number to check
     * @return True if a tile with given number exists or false
     */
    public boolean isTileExists(Integer diceResult) {
        for (Hextile[] tile : this.tiles) {
            for (Hextile hextile : tile) {
                if (hextile.getId() == diceResult) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Send related resources to players who have settlements or cities on this tile
     *
     * @param diceResult The number of tile
     */
    public void sendResourcesToPlayers(Integer diceResult) {

        for (Hextile[] row : this.tiles) {
            for (Hextile innerTile : row) {
                if (innerTile.getId() == diceResult && innerTile.playable) {
                    innerTile.sendResources();
                }
            }
        }
    }

    /** Make all playable except for water and desert */
    public void resetRobber() {
        for (Hextile[] row : this.tiles) {
            for (Hextile tile : row) {
                if (tile.getGroundType() == GroundType.Water) continue;
                tile.setPlayable(true);
            }
        }
    }

    /**
     * Add a city on behalf of the given player
     *
     * @param p The player who adds the city
     */
    public void addCity(Player p) {
        List<Settlement> settlements;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                for (int settlementSlot = 0; i < Hextile.NB_SIDES; settlementSlot++) {
                    if (this.tiles[i][j].getSettlementSlot(settlementSlot) != null
                            && this.tiles[i][j].getSettlementSlot(settlementSlot).getOwner().uid
                                    == p.uid) {
                        try {
                            this.tiles[i][j].upgradeSettlement(settlementSlot);
                            return;
                        } catch (NoSuchSlotException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * Make build a road to the first eligible slot the given player
     *
     * @param currentPlayer The player to build the road
     */
    public void buildRoad(Player currentPlayer) {
        Road roadToAdd = new Road(currentPlayer);
        for (Hextile[] tile : this.tiles) {
            for (Hextile hextile : tile) {
                Integer roadSlot = hextile.getFirstEligibleRoadSlot(roadToAdd);
                if (roadSlot == null) continue;
                try {
                    hextile.addRoad(roadSlot, roadToAdd);
                } catch (SlotAlreadyTakenException | NoSuchSlotException ignore) {
                    continue;
                }
                return;
            }
        }
    }

    /**
     * Make the given player build a settlement to the first eligible place in the board
     *
     * @param player The player who will build the settlement on the board
     */
    public void buildSettlement(Player player) {
        Settlement settlement = new Settlement(player);
        for (Hextile[] row : this.tiles) {
            for (Hextile tile : row) {
                Integer settlementSlot = tile.getFirstEligibleSettlementSlot(settlement);
                if (settlementSlot == null) continue;
                try {
                    tile.addSettlement(settlementSlot, settlement);
                } catch (SlotAlreadyTakenException | NoSuchSlotException ignore) {
                    continue;
                }
                return;
            }
        }
    }

    /**
     * Find the robber in the board
     *
     * @return The Hextile with the robber
     */
    private Hextile getRobber() {
        for (Hextile[] row : this.tiles) {
            for (Hextile tile : row) {
                if (!tile.getPlayable() && tile.isNotWater()) {
                    return tile;
                }
            }
        }
        return null;
    }

    /** Move the robber to a random tile */
    public void randomRobber() {
        Hextile initialTile = this.getRobber();
        Random r = new Random();
        int i, j;
        do {
            i = r.nextInt(this.tiles.length - 2) + 1;
            j = r.nextInt(this.tiles[i].length - 2) + 1;
        } while (!this.tiles[i][j].playable && this.tiles[i][j].isNotWater());
        this.tiles[i][j].setPlayable(false);
        if (initialTile == null) return;
        initialTile.setPlayable(true);
    }
}
