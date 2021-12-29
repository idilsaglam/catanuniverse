package org.catanuniverse.client.game.board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
import org.catanuniverse.commons.ClientConfiguration;

public class BoardPane extends JPanel {

  private final ClientConfiguration configuration;
  private JPanel topStatusPane, gameBoardPane, bottomStatusPane;

  public BoardPane(Dimension size, ClientConfiguration configuration) {
    this.initSizes(size);
    // TODO: Check if we need to update the layout
    this.configuration = configuration;
    this.initPanes();
  }


  /**
   * Initialise panes
   */
  private void initPanes() {
    this.topStatusPane = new TopStatusBar();
    this.gameBoardPane = new GameBoardPane();
    this.bottomStatusPane = new BottomStatusBar();
    super.add(this.topStatusPane, BorderLayout.PAGE_START);
    super.add(this.gameBoardPane, BorderLayout.CENTER);
    super.add(this.bottomStatusPane, BorderLayout.PAGE_END);
  }

  /**
   * Set size related properties
   * @param size The given size object
   */
  private void initSizes(Dimension size) {
    super.setPreferredSize(size);
    super.setSize(size);
    super.setMinimumSize(size);
  }

}
