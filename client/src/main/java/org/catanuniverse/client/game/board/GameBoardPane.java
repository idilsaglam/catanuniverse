/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.client.game.board;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import javax.swing.*;
import org.catanuniverse.core.exceptions.NoSuchSlotException;
import org.catanuniverse.core.exceptions.SlotAlreadyTakenException;
import org.catanuniverse.core.exceptions.TileTypeNotSupportedException;
import org.catanuniverse.core.game.Board;
import org.catanuniverse.core.game.Hextile;
import org.catanuniverse.core.utils.TriPredicate;

class GameBoardPane extends JPanel {

    private static final int SIZE = 7;
    private static final long serialVersionUID = 1L;

    private Font font = new Font("Arial", Font.BOLD, 18);

    private final Dimension size;
    private final Board board;
    private FontMetrics metrics;

    private BiPredicate<Hextile, Integer> onSettlementAdded;
    private TriPredicate<Hextile, Integer, Line2D> onRoadAdded;
    private Predicate<Hextile> onRobberMoved;

    public GameBoardPane(Dimension size) {
        this.size = size;
        try {
            this.board = new Board(GameBoardPane.SIZE);
        } catch (NoSuchSlotException
                | SlotAlreadyTakenException
                | TileTypeNotSupportedException e) {
            e.printStackTrace();
            throw new RuntimeException("Error happened while creating the board pane");
        }
        this.setLayout(null);
        this.setHexagons(7, 35, 0);
    }

    void setOnRoadAdded(TriPredicate<Hextile, Integer, Line2D> onRoadAdded) {
        this.onRoadAdded = onRoadAdded;
    }

    void setOnSettlementAdded(BiPredicate<Hextile, Integer> onSettlementAdded) {
        this.onSettlementAdded = onSettlementAdded;
    }

    /**
     * Updates the onRobberMoved predicate
     *
     * @param onRobberMoved The new onRobberMoved predicate
     */
    void setOnRobberMoved(Predicate<Hextile> onRobberMoved) {
        this.onRobberMoved = onRobberMoved;
    }

    Board getBoard() {
        return this.board;
    }

    private void setHexagons(int s, int radius, int padding) {
        double r3 = Math.sqrt(3);

        Point origin = new Point(size.width / 6, 3 * size.height / 8);
        double ang30 = Math.toRadians(30);
        double xOff = Math.cos(ang30) * (radius + padding);
        double yOff = Math.sin(ang30) * (radius + padding);
        int half = GameBoardPane.SIZE / 2;
        for (int row = 0; row < GameBoardPane.SIZE; row++) {
            int cols = s - Math.abs(row - half);
            for (int column = 0; column < cols; column++) {
                int x = (int) (origin.x + xOff * (column * 2 + 1 - cols));
                int y = (int) (origin.y + yOff * (row - half) * 3);
                HexagonTile tile = new HexagonTile(x, y, radius, this.board.get(row, column));
                tile.setOnAddRoadCallback(this::addRoad);
                tile.setOnAddSettlementCallback(this::addSettlement);
                tile.setOnRobberMoved(this::onRobberMoved);
                this.add(tile);
            }
        }
    }

    /**
     * Method called when the robber moved
     *
     * @param hextile The hextile on which the robber moved
     * @return True if the robber can moved, false if not
     */
    private boolean onRobberMoved(Hextile hextile) {
        if (hextile.getPlayable() && this.onRobberMoved.test(hextile)) {
            this.board.resetRobber();
            hextile.setPlayable(false);
            return true;
        }
        return false;
    }

    private boolean addRoad(Hextile tile, Integer roadSlot, Line2D lineModel) {

        if (this.onRoadAdded == null) {
            return false;
        }
        return this.onRoadAdded.test(tile, roadSlot, lineModel);
    }

    private boolean addSettlement(Hextile tile, Integer settlementSlot) {

        if (this.onSettlementAdded == null) {
            return false;
        }
        return this.onSettlementAdded.test(tile, settlementSlot);
    }

    /**
     * Handles dice roll action
     *
     * @param diceResult The total number on dice
     * @return true if tile exists, false if not
     */
    boolean diceRolled(Integer diceResult) {
        if (this.board.isTileExists(diceResult)) {

            this.board.sendResourcesToPlayers(diceResult);
            return true;
        }
        return false;
    }
}
