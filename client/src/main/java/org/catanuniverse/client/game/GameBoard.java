package org.catanuniverse.client.game;

import java.awt.Dimension;
import javax.swing.JSplitPane;
import org.catanuniverse.client.game.board.BoardPane;
import org.catanuniverse.client.game.chat.ChatPane;
import org.catanuniverse.commons.ClientConfiguration;

public class GameBoard extends JSplitPane {

  private final BoardPane board;
  private final ChatPane chat;

  public GameBoard(Dimension size, ClientConfiguration configuration) {
    super();
    if (configuration.isOnline()) {
      this.board = new BoardPane(
          new Dimension(
              3 * size.width / 4,
              size.height
          ),
          configuration);
      this.chat = new ChatPane(new Dimension(
          size.width / 4,
          size.height
      ));
      super.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
      super.setLeftComponent(this.board);
      super.setRightComponent(this.chat);
      return;
    }
    this.board = new BoardPane(size, configuration);
    this.chat = null;
    super.setLeftComponent(this.board);
  }


}
