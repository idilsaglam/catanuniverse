/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

import java.util.Random;
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
                    // TODO: Initialise water tile
                    this.tiles[i][j] = new Hextile(-1, GroundType.Water);
                    continue;
                }
                GroundType groundType = GroundType.random(desertExists);
                if (groundType == GroundType.Desert) {
                    desertExists = true;
                    this.tiles[i][j] = new Hextile(7, GroundType.Desert);
                    continue;
                }
                Random r = new Random();
                this.tiles[i][j] = new Hextile(r.nextInt(12) + 1, groundType);

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
        for (int i = 0; i<this.tiles.length; i++) {
            if (i == 0 || i == this.tiles.length -1) {
                this.tiles[i][0].addHarbor(Harbor.random());
                this.tiles[i][2].addHarbor(Harbor.random());
            }
            this.tiles[i][(i%2 == 0) ? 0 : this.tiles[i].length-1].addHarbor(Harbor.random());
        }
    }

    /**
     * Checks if a tile with given number exists or not
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
     * @param diceResult The number of tile
     */
    public void sendResourcesToPlayers(Integer diceResult) {
        for (Hextile[] row : this.tiles) {
            for (Hextile innerTile: row) {
                if (innerTile.getId() == diceResult && innerTile.playable) {
                    innerTile.sendResources();
                }
            }
        }
    }
}
