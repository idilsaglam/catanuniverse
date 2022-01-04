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
                // TODO: Initialise tiles with random numbers and random resource types
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
                this.tiles[i][j].addNeighbor(5, this.tiles[i + 1][j]);
                this.tiles[i][j].addNeighbor(4, this.tiles[i + 1][j + 1]);
            }
        }
        for (int i = this.tiles.length - 1; i > half; i--) {
            for (int j = 0; j < this.tiles[i].length; j++) {
                this.tiles[i][j].addNeighbor(1, this.tiles[i - 1][j]);
                this.tiles[i][j].addNeighbor(2, this.tiles[i - 1][j + 1]);
            }
        }
    }
}
