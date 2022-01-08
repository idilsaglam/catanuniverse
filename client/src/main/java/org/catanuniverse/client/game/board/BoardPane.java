/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game.board;

import java.awt.*;
import java.io.IOException;
import java.util.Random;
import java.util.HashMap;
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
    private Integer diceValue;
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
        this.add(this.robberLabel,BorderLayout.AFTER_LAST_LINE);
        final Dimension sideSize = new Dimension(size.width, size.height / 8),
                centerSize = new Dimension(size.width, 3 * size.height / 4);
        this.gameSettings = gameSettings;
        this.topStatusPane =
                new TopStatusBar(this.gameSettings.getPlayers());
        this.gameBoardPane = new GameBoardPane(centerSize);
        this.boardSidePane = new BoardSidePane(this::onDiceRolled,
            this::onCardUsed,
            this::onCardStocked,
            this::canDrawCard
            );
        this.gameBoardPane.setOnRobberMoved(this::onRobberMoved);
        this.gameBoardPane.setOnSettlementAdded(this::onSettlementAdded);
        this.gameBoardPane.setOnRoadAdded(this::onRoadAdded);
        this.bottomStatusPane = new BottomStatusBar(
            this.gameSettings.getCurrentPlayer(),
            this.gameSettings.getCurrentPlayerIndex(),
            this::onNextPlayerButtonPressed,
            this::onCardUsed,
            this::getHarborsOwnedByCurrentPlayer
        );
        this.initPanes(size);
        this.desactivateRobber();
        this.boardSidePane.disableDice();
    }


    private void onNextPlayerButtonPressed() throws IOException {
        if (this.gameSettings.isRobberActivated()) return;
        if (
            (this.gameSettings.getRoundNumber() == 0) && (this.gameSettings.getCurrentPlayer().getNbSettlement() != 1 || this.gameSettings.getCurrentPlayer().getNbRoad() != 1) ||
                (this.gameSettings.getRoundNumber() == 1) && (this.gameSettings.getCurrentPlayer().getNbSettlement() != 0 || this.gameSettings.getCurrentPlayer().getNbRoad() != 0)
        ) {
            // First two rounds, each user should build a road and a settlement
            return;
        }
        if (
            this.gameSettings.getRoundNumber() > 1 && this.diceValue == null
        ) return;
        this.updateStatusBars();
        this.next();
    }

    /**
     * Verify if a card can be drawn or not
     * @return True if card can be drawn, false if not
     */
    private Boolean canDrawCard() {
        if (this.gameSettings.getRoundNumber() <= 1) return false;
        if (this.gameSettings.getCurrentPlayer().canBuyDeveloppementCard() && !this.gameSettings.isRobberActivated()) {
            // A card can be drawn if the current player has enough resources to draw the card and the robber is not activated
            this.gameSettings.getCurrentPlayer().buyDeveloppementCard();
            return true;
        }
        return false;
    }

    /**
     * Verify the given value of the dice
     * @param diceValue The value of the dice
     */
    private boolean onDiceRolled(Integer diceValue) {
        this.diceValue = diceValue;
        if(diceValue != null && diceValue== 7){this.activateRobber(); return true;}

        System.out.printf("DICE VALUE %d\n", diceValue);
        if (this.gameBoardPane.diceRolled(diceValue)) {
            this.updateStatusBars();
            return true;
        }
        return false;
    }

    /**
     * Current player uses the given card card
     * @param usedCard The card which will be used by the current player
     */
    private void onCardUsed(Card usedCard) {
        usedCard.use(this.gameSettings.getCurrentPlayer());
    }

    /**
     * Current player stock a card
     * @param stockedCard The card to stock
     */
    private void onCardStocked(Card stockedCard) {
        stockedCard.stock(this.gameSettings.getCurrentPlayer());
        this.updateStatusBars();
    }

    /**
     * Verify if the robber can be moved to the given tile
     * @param tile The tile to move the robber
     * @return True if the robber can be moved to the given tile, false if not
     */
    private boolean onRobberMoved(Hextile tile) {
        System.out.printf("Robber moved tile id %d\n", tile.getId());
        if (this.gameSettings.isRobberActivated() && tile.getPlayable()) {
            this.gameSettings.setRobberActivated(false);
            this.desactivateRobber();
            tile.setPlayable(false);
            return true;
        }
        return false;
    }

    /**
     * Method verify if a settlement can be added to the given tile with given index
     * @param tile The tile to add the settlement
     * @param settlementIndex The settlement index to add the settlement
     * @return True if a settlement can be added to the given slot false if not
     */
    private boolean onSettlementAdded(Hextile tile, Integer settlementIndex) {
        if (this.gameSettings.isRobberActivated()) return false;
        if (
            (this.gameSettings.getRoundNumber() == 0 && this.gameSettings.getCurrentPlayer().getNbSettlement() != 2) |
                (this.gameSettings.getRoundNumber() == 1 && this.gameSettings.getCurrentPlayer().getNbSettlement() != 1)
        ) return false;
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
    }

    /**
     * let the AI play
     */
    private void playAI() throws IOException {
        Random r = new Random();

        if(this.gameSettings.getRoundNumber() != 0 || this.gameSettings.getRoundNumber()!=1 ){
            boardSidePane.roll();
        }

        if( !this.gameSettings.getCurrentPlayer().canBuildSettlement() &&
            !this.gameSettings.getCurrentPlayer().canBuildCity() &&
            !this.gameSettings.getCurrentPlayer().canBuildRoad() &&
            !this.gameSettings.getCurrentPlayer().canBuyDeveloppementCard()
        ){
            next();
            return;
        }
        if (this.gameSettings.isRobberActivated()){
            this.gameBoardPane.getBoard().randomRobber();
            this.gameBoardPane.revalidate();
            this.gameBoardPane.repaint();
            this.updateStatusBars();
            this.playAI();
            return;
        }
        if(this.gameSettings.getCurrentPlayer().canBuildCity()){
            boolean res = r.nextBoolean();
            if(res){
                this.gameBoardPane.getBoard().addCity(this.gameSettings.getCurrentPlayer());
                this.gameSettings.getCurrentPlayer().buildCity();
                this.updateStatusBars();
                this.playAI();
                return;
            }
        }

        if(this.gameSettings.getCurrentPlayer().canBuildRoad()){
            boolean res = r.nextBoolean();
            if(res){
                this.gameBoardPane.getBoard().buildRoad(this.gameSettings.getCurrentPlayer());
                this.gameSettings.getCurrentPlayer().buildRoad();
                this.updateStatusBars();
                this.playAI();
                return;
            }
        }
        if(this.gameSettings.getCurrentPlayer().canBuildSettlement()){
            boolean res = r.nextBoolean();
            if(res){
                this.gameBoardPane.getBoard().buildSettlement(this.gameSettings.getCurrentPlayer());
                this.gameSettings.getCurrentPlayer().buildSettlement();
            }
        }
        if(this.gameSettings.getCurrentPlayer().canBuyDeveloppementCard()){
            boolean res = r.nextBoolean();
            if(res){
                this.boardSidePane.drawCard();
                //TODO: Not sure if it is necessary
                //this.gameSettings.getCurrentPlayer().buyDeveloppementCard();
                boolean res2 = r.nextBoolean();
                if(res2){
                    this.boardSidePane.stockCard();
                }else{
                    this.boardSidePane.useCard();
                }
            }
        }
        next();

    }


    /**
     * Method handles onRoadAdded event
     * @param tile The tile on which the road is added
     * @param roadIndex The index of the road slot to add
     * @return True if the road can be added
     */
    private boolean onRoadAdded(Hextile tile, Integer roadIndex) {
            if (this.gameSettings.isRobberActivated()) return false;
        if (
            (this.gameSettings.getRoundNumber() == 0 && this.gameSettings.getCurrentPlayer().getNbRoad() != 2) |
                (this.gameSettings.getRoundNumber() == 1 && this.gameSettings.getCurrentPlayer().getNbRoad() != 1)) return false;

            if (this.gameSettings.getCurrentPlayer().canBuildRoad()) {
                Road r = new Road(this.gameSettings.getCurrentPlayer());
                if (tile.getRoadSlot(roadIndex) != null) return false;
                try {
                    if (!tile.canAddRoad(roadIndex,r)) return false;
                } catch (NoSuchSlotException e) {
                    e.printStackTrace();
                    return false;
                }
                try {
                    tile.addRoad(roadIndex, r);
                    this.revalidate();
                    this.repaint();
                    this.gameSettings.getCurrentPlayer().buildRoad();
                    updateStatusBars();
                    return true;
                } catch (SlotAlreadyTakenException | NoSuchSlotException ignore) {
                    return false;
                }
            }
            return false;
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

    /**
     * Passes to the next player
     * @throws IOException
     */
    private void next() throws IOException {
        this.gameSettings.next();
        System.out.println(gameSettings.getCurrentPlayer().getUsername());
        System.out.println(gameSettings.getCurrentPlayer().isAI());
        if (gameSettings.getCurrentPlayer().isAI()) {
            playAI();
            this.next();
            return;
        }
        this.bottomStatusPane.setCurrentPlayer(this.gameSettings.getCurrentPlayer(), this.gameSettings.getCurrentPlayerIndex()+1);
        this.topStatusPane.updatePlayerCard();
        //this.bottomStatusPane.revalidate();
        //this.bottomStatusPane.repaint();
        if (this.gameSettings.getRoundNumber() > 1) {
            this.diceValue = null;
            this.boardSidePane.resetDice();
        }
        this.boardSidePane.revalidate();
        this.boardSidePane.repaint();
    }

    /**
     * Updates top and bottom status bars
     */
    private void updateStatusBars() {
        this.bottomStatusPane.updateResources();
        this.topStatusPane.updatePlayerCard();
        this.bottomStatusPane.updateUserCards();
        this.bottomStatusPane.updateExchange();
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
        this.boardSidePane.disableDice();
        this.gameSettings.setRobberActivated(true);
        this.robberLabel.setVisible(true);
        int nbCardsForThief;
        for(int i=0; i<gameSettings.getPlayers().length; i++){
            if (gameSettings.getPlayers()[i].getRessourceNumber() <= 7) {
                continue;
            }
            Player player = this.gameSettings.getPlayers()[i];
            // Robber does not impact resources of the current player
            if (player.uid == this.gameSettings.getCurrentPlayer().uid) continue;
            nbCardsForThief = player.getRessourceNumber()/2;
            for (Resource r: Resource.values()) {
                if(nbCardsForThief == 0){
                    break;
                }
                if(player.getResource(r) > nbCardsForThief){
                    player.updateResource(r, nbCardsForThief * -1);
                    nbCardsForThief = 0;
                    continue;
                }
                if(player.getResource(r) <= nbCardsForThief){
                    nbCardsForThief -= player.getResource(r);
                    player.updateResource(r, player.getResource(r) * -1);
                }
            }
        }
        this.updateStatusBars();
        this.revalidate();
        this.repaint();
    }

    /**
     * Get the harbors owned by the current user
     * @return The list of harbors owned by the current user
     */
    java.util.List<Harbor> getHarborsOwnedByCurrentPlayer() {
        return this.gameBoardPane.getBoard().getHarborsOfPlayer(this.gameSettings.getCurrentPlayer());
    }
}
