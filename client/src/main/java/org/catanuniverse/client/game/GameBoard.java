/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game;

import java.awt.Dimension;
import javax.swing.JSplitPane;
import org.catanuniverse.client.game.board.BoardPane;
import org.catanuniverse.client.game.chat.ChatPane;
import org.catanuniverse.commons.GameSettings;

public class GameBoard extends JSplitPane {

    private final BoardPane board;
    private final ChatPane chat;

    /**
     * Creates a GameBoard with given size and gameSettings
     *
     * @param size The size of the game board panel
     * @param gameSettings The settings of the game
     */
    public GameBoard(Dimension size, GameSettings gameSettings) {
        super();
        if (gameSettings.isOnline()) {
            this.board =
                    new BoardPane(new Dimension(3 * size.width / 4, size.height), gameSettings);
            this.chat = new ChatPane(new Dimension(size.width / 4, size.height));
            super.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            super.setLeftComponent(this.board);
            super.setRightComponent(this.chat);
            return;
        }
        this.board = new BoardPane(size, gameSettings);
        this.chat = null;
        super.setLeftComponent(this.board);
    }
}
