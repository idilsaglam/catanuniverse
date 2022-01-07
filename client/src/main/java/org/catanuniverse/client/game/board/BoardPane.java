/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game.board;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;

import org.catanuniverse.commons.GameSettings;
import org.catanuniverse.core.exceptions.NoSuchSlotException;
import org.catanuniverse.core.exceptions.SlotAlreadyTakenException;
import org.catanuniverse.core.game.*;

public class BoardPane extends JPanel {

    private final GameSettings gameSettings;
    private final TopStatusBar topStatusPane;
    private final GameBoardPane gameBoardPane;
    private final BottomStatusBar bottomStatusPane;
    private final BoardSidePane boardSidePane;
    private final JLabel robberLabel;
    private final Color defaultBackground;
    /**
     * Creates a BoardPane with given size and configuration
     *
     * @param size The size of the board pane
     * @param gameSettings The configuration related to the game
     */
    public BoardPane(Dimension size, GameSettings gameSettings) throws IOException {
        this.initSizes(size);
        this.defaultBackground = this.getBackground();
        this.robberLabel = new JLabel("Robber activated");
        this.robberLabel.setVisible(false);
        this.add(this.robberLabel,BorderLayout.AFTER_LAST_LINE);

        final Dimension sideSize = new Dimension(size.width, size.height / 8),
                centerSize = new Dimension(size.width, 3 * size.height / 4);
        this.gameSettings = gameSettings;
        this.topStatusPane =
                new TopStatusBar(this.gameSettings.getPlayers());
        this.gameBoardPane = new GameBoardPane(centerSize);
        this.boardSidePane = new BoardSidePane((Integer diceValue) -> {

            if(diceValue != null && diceValue== 7){this.activateRobber(); return true;}

            System.out.printf("DICE VALUE %d\n", diceValue);
            if (this.gameBoardPane.diceRolled(diceValue)) {
                this.updateStatusBars();
                return true;
            }
            return false;
        },
            (Card usedCard) -> {
                usedCard.use(this.gameSettings.getCurrentPlayer());
            },
            (Card stockedCard) -> {
                stockedCard.stock(this.gameSettings.getCurrentPlayer());
                this.updateStatusBars();
            });
        this.gameBoardPane.setOnRobberMoved((Hextile tile) -> {
            System.out.printf("Robber moved tile id %d\n", tile.getId());
            if (this.gameSettings.isRobberActivated() && tile.getPlayable()) {
                this.gameSettings.setRobberActivated(false);
                this.desactivateRobber();
                tile.setPlayable(false);
                return true;
            }
            return false;
        });
        this.gameBoardPane.setOnSettlementAdded((Hextile tile, Integer settlementIndex) -> {
            if (this.gameSettings.isRobberActivated()) return false;
            if (this.gameSettings.getCurrentPlayer().isAI()) {
                return false;
            }
            try {
                if (!tile.canAddSettlement(settlementIndex)) return false;
            } catch (NoSuchSlotException e) {
                e.printStackTrace();
                return false;
            }
            if (this.gameSettings.getCurrentPlayer().canBuildSettlement()) {
                System.out.printf("Game board pane on settlement added to %d\n", settlementIndex);
                if (tile.getSettlementSlot(settlementIndex) != null) {
                    if (tile.getSettlementSlot(settlementIndex) instanceof City) {
                        return false;
                    }
                    if (tile.getSettlementSlot(settlementIndex).getOwner() == null || !tile.getSettlementSlot(settlementIndex).getOwner().equals(this.gameSettings.getCurrentPlayer())) {
                        return false;
                    }
                    if (!this.gameSettings.getCurrentPlayer().canBuildCity()) return false;
                    try {
                        tile.upgradeSettlement(settlementIndex);
                        this.revalidate();
                        this.repaint();
                        this.gameSettings.getCurrentPlayer().buildCity();
                        return true;
                    } catch (NoSuchSlotException ignore) {
                        return false;
                    }
                }
                try {
                    tile.addSettlement(settlementIndex, new Settlement(this.gameSettings.getCurrentPlayer()));
                } catch (SlotAlreadyTakenException|NoSuchSlotException ignore) {
                     return false;
                }
                this.revalidate();
                this.repaint();
                this.gameSettings.getCurrentPlayer().buildSettlement();
                return true;
            }
            return false;
        });
        this.gameBoardPane.setOnRoadAdded((Hextile tile, Integer roadIndex) -> {
            if (this.gameSettings.isRobberActivated()) return false;
            if (gameSettings.getCurrentPlayer().isAI()) {
                // TODO: Make AI play
                return false;
            }
            if (this.gameSettings.getCurrentPlayer().canBuildRoad()) {

            if (tile.getRoadSlot(roadIndex) != null) return false;
            if (!tile.canAddRoad(roadIndex)) return false;
            try {
                tile.addRoad(roadIndex, new Road(this.gameSettings.getCurrentPlayer()));
                this.revalidate();
                this.repaint();
                this.gameSettings.getCurrentPlayer().buildRoad();
                return true;
                } catch (SlotAlreadyTakenException | NoSuchSlotException ignore) {
                    return false;
                }
            }
            return false;
        });
        this.bottomStatusPane = new BottomStatusBar(
            this.gameSettings.getCurrentPlayer(),
            this.gameSettings.getCurrentPlayerIndex(),
            this::next
        );
        this.initPanes(size);
    }

    /** Initialise panes and add them to the current JPanel */
    private void initPanes(Dimension size) {
        final Dimension topSize = new Dimension(size.width, size.height / 8),
                bottomSize = new Dimension(size.width, size.height / 8),
                centerSize = new Dimension(size.width,  10* size.height / 16);

        this.topStatusPane.setSize(topSize);
        this.topStatusPane.setPreferredSize(topSize);
        //this.gameBoardPane.setSize(centerSize);
        //this.gameBoardPane.setPreferredSize(centerSize);
        this.bottomStatusPane.setSize(bottomSize);
        this.bottomStatusPane.setPreferredSize(bottomSize);

        this.gameBoardPane.setMinimumSize(new Dimension( 30*centerSize.width / 100, centerSize.height));
        this.boardSidePane.setMaximumSize(new Dimension(70*centerSize.width / 100, centerSize.height));

        this.gameBoardPane.setMaximumSize(new Dimension( 50*centerSize.width / 100, centerSize.height));
        this.boardSidePane.setMinimumSize(new Dimension(50*centerSize.width / 100, centerSize.height));

        this.gameBoardPane.setPreferredSize(new Dimension(30*centerSize.width / 100, centerSize.height));
        this.boardSidePane.setPreferredSize(new Dimension(70*centerSize.width / 100, centerSize.height));

        JSplitPane centerPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.gameBoardPane, this.boardSidePane);

        centerPanel.setSize(centerSize);
        centerPanel.setPreferredSize(centerSize);

        super.add(this.topStatusPane, BorderLayout.PAGE_START);
        super.add(centerPanel, BorderLayout.CENTER);
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

    private void showNextButton(boolean show) {
        this.boardSidePane.setNextButton(show);
    }

    /**
     * Passes to the next player
     * @throws IOException
     */
    private void next() throws IOException {
        System.out.printf("Current player username %s Current player index %d\n", this.gameSettings.getCurrentPlayer().getUsername(), this.gameSettings.getCurrentPlayerIndex());
        this.gameSettings.next();
        System.out.printf("Current player username %s Current player index %d\n", this.gameSettings.getCurrentPlayer().getUsername(), this.gameSettings.getCurrentPlayerIndex());
        this.bottomStatusPane.setCurrentPlayer(this.gameSettings.getCurrentPlayer(), this.gameSettings.getCurrentPlayerIndex()+1);
        this.topStatusPane.updatePlayerCard();
        //this.bottomStatusPane.revalidate();
        //this.bottomStatusPane.repaint();
        this.showNextButton(false);

    }

    /**
     * Updates top and bottom status bars
     */
    private void updateStatusBars() {
        this.bottomStatusPane.updateResources();
        this.topStatusPane.updatePlayerCard();
        this.bottomStatusPane.updateUserCards();
    }

    private void desactivateRobber() {
        this.setBackground(this.defaultBackground);
        this.robberLabel.setVisible(false);
    }

    /**
     * Activate robber mode
     */
    private void activateRobber(){
        this.setBackground(Color.RED);
        this.gameSettings.setRobberActivated(true);
        this.robberLabel.setVisible(true);
        int nbCardsForThief;
        for(int i=0; i<gameSettings.getPlayers().length; i++){
            if (gameSettings.getPlayers()[i].getRessourceNumber() <= 7) {
                continue;
            }
            nbCardsForThief = gameSettings.getPlayers()[i].getRessourceNumber()/2;
            for (Resource r: Resource.values()) {
                if(nbCardsForThief == 0){
                    continue;
                }
                if(this.gameSettings.getCurrentPlayer().getResource(r) > nbCardsForThief){
                    this.gameSettings.getCurrentPlayer().updateResource(r, nbCardsForThief * -1);
                    nbCardsForThief = 0;
                    continue;
                }
                if(this.gameSettings.getCurrentPlayer().getResource(r) <= nbCardsForThief){
                    nbCardsForThief -= this.gameSettings.getCurrentPlayer().getResource(r);
                    this.gameSettings.getCurrentPlayer().updateResource(r, this.gameSettings.getCurrentPlayer().getResource(r) * -1);
                }
            }

        }

        this.revalidate();
        this.repaint();
    }

}
