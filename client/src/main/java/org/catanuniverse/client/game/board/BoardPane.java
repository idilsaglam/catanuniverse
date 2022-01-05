/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game.board;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JPanel;
import org.catanuniverse.commons.GameSettings;
import org.catanuniverse.core.exceptions.NoSuchSlotException;
import org.catanuniverse.core.exceptions.SlotAlreadyTakenException;
import org.catanuniverse.core.game.Hextile;
import org.catanuniverse.core.game.Player;
import org.catanuniverse.core.game.Road;
import org.catanuniverse.core.game.Settlement;

public class BoardPane extends JPanel {

    private final GameSettings gameSettings;
    private final TopStatusBar topStatusPane;
    private final GameBoardPane gameBoardPane;
    private final BottomStatusBar bottomStatusPane;

    /**
     * Creates a BoardPane with given size and configuration
     *
     * @param size The size of the board pane
     * @param gameSettings The configuration related to the game
     */
    public BoardPane(Dimension size, GameSettings gameSettings) throws IOException {
        this.initSizes(size);
        final Dimension sideSize = new Dimension(size.width, size.height / 8),
                centerSize = new Dimension(size.width, 3 * size.height / 4);
        this.gameSettings = gameSettings;
        this.topStatusPane =
                new TopStatusBar();
        this.gameBoardPane = new GameBoardPane(centerSize);
        this.gameBoardPane.setOnSettlementAdded((Hextile tile, Integer settlementIndex) -> {
            if (tile.getSettlementSlot(settlementIndex) != null) return false;
            tile.addSettlement(settlementIndex, new Settlement(new Player("Test")));
            return true;
        });
        this.gameBoardPane.setOnRoadAdded((Hextile tile, Integer roadIndex) -> {
            if (tile.getRoadSlot(roadIndex) != null) return false;
            try {
                tile.addRoad(roadIndex, new Road(new Player("Test")));
                return true;
            } catch (SlotAlreadyTakenException | NoSuchSlotException ignore) {
                return false;
            }
        });
        this.bottomStatusPane = new BottomStatusBar();
        this.initPanes(size);
    }

    /** Initialise panes and add them to the current JPanel */
    private void initPanes(Dimension size) {
        final Dimension sideSize = new Dimension(size.width, size.height / 4),
                centerSize = new Dimension(size.width,  size.height / 2);
        this.topStatusPane.setSize(sideSize);
        this.topStatusPane.setPreferredSize(sideSize);
        this.gameBoardPane.setSize(centerSize);
        this.gameBoardPane.setPreferredSize(centerSize);
        this.bottomStatusPane.setSize(sideSize);
        this.bottomStatusPane.setPreferredSize(sideSize);
        super.add(this.topStatusPane, BorderLayout.PAGE_START);
        super.add(this.gameBoardPane, BorderLayout.CENTER);
        super.add(this.bottomStatusPane, BorderLayout.PAGE_END);
    }

    /**
     * Set size related properties
     *
     * @param size The given size object
     */
    private void initSizes(Dimension size) {
        super.setPreferredSize(size);
        super.setSize(size);
        super.setMinimumSize(size);
    }
}
