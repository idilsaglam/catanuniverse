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
    private Integer diceValue;
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
                new TopStatusBar(this.gameSettings.getPlayers());
        this.gameBoardPane = new GameBoardPane(centerSize);
        this.boardSidePane = new BoardSidePane((Integer diceValue) -> {

            if(diceValue != null && diceValue== 7){this.thief(); return true;}

            System.out.printf("DICE VALUE %d\n", diceValue);
            return this.gameBoardPane.diceRolled(diceValue);
        },
            (Integer cardNumber)->{
                if(cardNumber == 0){
                    gameSettings.getCurrentPlayer().updateResource(Resource.Corn,2);
                }
                if (cardNumber == 1){
                    gameSettings.getCurrentPlayer().addVictoryPoint(2);
                }
                if(cardNumber == 2){
                    gameSettings.getCurrentPlayer().addVictoryPoint(1);
                }
                if(cardNumber == 3){
                    gameSettings.getCurrentPlayer().updateResource(Resource.Mineral,1);
                }
                System.out.println(cardNumber);
            });
        this.gameBoardPane.setOnSettlementAdded((Hextile tile, Integer settlementIndex) -> {
            if (this.gameSettings.getCurrentPlayer().isAI()) {
                return false;
            }
            if (this.gameSettings.getCurrentPlayer().canBuildSettlement()) {
                System.out.printf("Game board pane on settlement added to %d\n", settlementIndex);
                if (tile.getSettlementSlot(settlementIndex) != null) {
                    if (tile.getSettlementSlot(settlementIndex) instanceof City) {
                        return false;
                    }
                    if (!this.gameSettings.getCurrentPlayer().canBuildCity()) return false;
                    try {
                        tile.upgradeSettlement(settlementIndex);
                        this.revalidate();
                        this.repaint();
                        this.gameSettings.getCurrentPlayer().buildCity();
                        this.next();
                        return true;
                    } catch (NoSuchSlotException | IOException e) {
                        return false;
                    }
                }
                tile.addSettlement(settlementIndex, new Settlement(this.gameSettings.getCurrentPlayer()));
                this.revalidate();
                this.repaint();
                this.gameSettings.getCurrentPlayer().buildSettlement();
                try {
                    this.next();
                } catch (IOException e) {
                    return false;
                }
                return true;
            }
            return false;
        });
        this.gameBoardPane.setOnRoadAdded((Hextile tile, Integer roadIndex) -> {
            if (gameSettings.getCurrentPlayer().isAI()) {
                return false;
            }
            if (this.gameSettings.getCurrentPlayer().canBuildRoad()) {

            if (tile.getRoadSlot(roadIndex) != null) return false;

            try {
                tile.addRoad(roadIndex, new Road(this.gameSettings.getCurrentPlayer()));
                this.revalidate();
                this.repaint();
                this.gameSettings.getCurrentPlayer().buildRoad();
                this.next();
                return true;
                } catch (SlotAlreadyTakenException | NoSuchSlotException | IOException ignore) {
                    return false;
                }
            }
            return false;
        });
        this.bottomStatusPane = new BottomStatusBar(this.gameSettings.getCurrentPlayer(), this.gameSettings.getCurrentPlayerIndex());
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

        this.gameBoardPane.setMinimumSize(new Dimension( 2*centerSize.width / 8, centerSize.height));
        this.boardSidePane.setMaximumSize(new Dimension(6*centerSize.width /8, centerSize.height));

        this.gameBoardPane.setMaximumSize(new Dimension( 3*centerSize.width / 8, centerSize.height));
        this.boardSidePane.setMinimumSize(new Dimension(5*centerSize.width /8, centerSize.height));

        this.gameBoardPane.setPreferredSize(new Dimension(2*centerSize.width / 8, centerSize.height));
        this.boardSidePane.setPreferredSize(new Dimension(6*centerSize.width / 8, centerSize.height));

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

    private void next() throws IOException {
        this.diceValue = null;
        System.out.printf("Current player username %s Current player index %d\n", this.gameSettings.getCurrentPlayer().getUsername(), this.gameSettings.getCurrentPlayerIndex());
        this.gameSettings.next();
        System.out.printf("Current player username %s Current player index %d\n", this.gameSettings.getCurrentPlayer().getUsername(), this.gameSettings.getCurrentPlayerIndex());
        this.bottomStatusPane.setCurrentPlayer(this.gameSettings.getCurrentPlayer(), this.gameSettings.getCurrentPlayerIndex()+1);
        //this.bottomStatusPane.revalidate();
        //this.bottomStatusPane.repaint();
        this.showNextButton(false);

    }


    public void thief(){
        this.setBackground(Color.RED);
        this.add(new Label("Il y a une voleur!!!"),BorderLayout.AFTER_LAST_LINE);
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
