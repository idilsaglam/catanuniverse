/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.client.game;

import java.awt.Dimension;
import java.io.IOException;
import java.util.function.Consumer;
import javax.swing.JSplitPane;
import org.catanuniverse.client.game.board.BoardPane;
import org.catanuniverse.client.game.chat.ChatPane;
import org.catanuniverse.commons.GameSettings;
import org.catanuniverse.core.game.Player;

public class GameBoard extends JSplitPane {

    private final BoardPane board;
    private final ChatPane chat;

    /**
     * Creates a GameBoard with given size and gameSettings
     *
     * @param size The size of the game board panel
     * @param gameSettings The settings of the game
     */
    public GameBoard(Dimension size, GameSettings gameSettings, Consumer<Player> onGameEnd)
            throws IOException {
        super();
        if (gameSettings.isOnline()) {
            this.board =
                    new BoardPane(
                            new Dimension(3 * size.width / 4, size.height),
                            gameSettings,
                            onGameEnd);
            this.chat = new ChatPane(new Dimension(size.width / 4, size.height));
            super.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            super.setLeftComponent(this.board);
            super.setRightComponent(this.chat);
            return;
        }
        this.board = new BoardPane(size, gameSettings, onGameEnd);
        this.chat = null;
        super.setLeftComponent(this.board);
    }
}
