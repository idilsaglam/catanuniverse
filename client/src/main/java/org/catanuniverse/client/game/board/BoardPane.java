/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game.board;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.catanuniverse.commons.GameSettings;

public class BoardPane extends JPanel {

    private final GameSettings gameSettings;
    private final JPanel topStatusPane, gameBoardPane, bottomStatusPane;

    /**
     * Creates a BoardPane with given size and configuration
     *
     * @param size The size of the board pane
     * @param gameSettings The configuration related to the game
     */
    public BoardPane(Dimension size, GameSettings gameSettings) {
        this.initSizes(size);
        this.gameSettings = gameSettings;
        this.topStatusPane =
                new TopStatusBar(
                        gameSettings.getPlayers(), gameSettings.getCapacity());
        this.gameBoardPane = new GameBoardPane();
        this.bottomStatusPane = new BottomStatusBar();
        this.initPanes(size);
    }

    /** Initialise panes and add them to the current JPanel */
    private void initPanes(Dimension size) {
        final Dimension sideSize = new Dimension(size.width, size.height / 8),
                centerSize = new Dimension(size.width, 3 * size.height / 4);
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
