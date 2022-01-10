/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.client.game.board;

import java.awt.*;
import java.awt.geom.Line2D;
import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
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
    private final Consumer<Player> onGameEnd;

    /**
     * Creates a BoardPane with given size and configuration
     *
     * @param size The size of the board pane
     * @param gameSettings The configuration related to the game
     */
    public BoardPane(Dimension size, GameSettings gameSettings, Consumer<Player> onGameEnd)
            throws IOException {
        this.onGameEnd = onGameEnd;
        this.initSizes(size);
        this.defaultBackground = this.getBackground();
        this.robberLabel = new JLabel("Robber activated");
        this.add(this.robberLabel, BorderLayout.AFTER_LAST_LINE);
        final Dimension sideSize = new Dimension(size.width, size.height / 8),
                centerSize = new Dimension(size.width, 3 * size.height / 4);
        this.gameSettings = gameSettings;
        this.topStatusPane = new TopStatusBar(this.gameSettings.getPlayers());
        this.gameBoardPane = new GameBoardPane(centerSize);
        this.boardSidePane =
                new BoardSidePane(
                        this::onDiceRolled,
                        this::onCardUsed,
                        this::onCardStocked,
                        this::canDrawCard,
                        this::onCardDrawn);
        this.gameBoardPane.setOnRobberMoved(this::onRobberMoved);
        this.gameBoardPane.setOnSettlementAdded(this::onSettlementAdded);
        this.gameBoardPane.setOnRoadAdded(this::onRoadAdded);
        this.bottomStatusPane =
                new BottomStatusBar(
                        this.gameSettings.getCurrentPlayer(),
                        this.gameSettings.getCurrentPlayerIndex(),
                        this::onNextPlayerButtonPressed,
                        this::onCardUsed,
                        this::getHarborsOwnedByCurrentPlayer,
                        this::updateStatusBars);
        this.initPanes(size);
        this.desactivateRobber();
        this.boardSidePane.disableDice();
    }

    /**
     * Method that updates knight card on status bar.
     *
     * @param card
     */
    private void onCardDrawn(Card card) {
        if (card == Card.KNIGHT) {
            this.gameSettings.getCurrentPlayer().incrementAchievement(Achievements.KNIGHT, 1);
        }
        this.updateStatusBars();
    }
    /** Method handles the on next button pressed action */
    private void onNextPlayerButtonPressed() throws IOException {

        if (this.gameSettings.isRobberActivated()) return;
        if ((this.gameSettings.getRoundNumber() == 0)
                        && (this.gameSettings.getCurrentPlayer().getNbSettlement() != 1
                                || this.gameSettings.getCurrentPlayer().getNbRoad() != 1)
                || (this.gameSettings.getRoundNumber() == 1)
                        && (this.gameSettings.getCurrentPlayer().getNbSettlement() != 0
                                || this.gameSettings.getCurrentPlayer().getNbRoad() != 0)) {
            // First two rounds, each user should build a road and a settlement
            return;
        }
        if (this.gameSettings.getRoundNumber() > 1 && this.diceValue == null) return;
        this.next();
        this.updateStatusBars();
    }

    /**
     * Verify if a card can be drawn or not
     *
     * @return True if card can be drawn, false if not
     */
    private Boolean canDrawCard() {
        if (this.gameSettings.getRoundNumber() <= 1) return false;
        if (this.gameSettings.getCurrentPlayer().canBuyDeveloppementCard()
                && !this.gameSettings.isRobberActivated()) {
            // A card can be drawn if the current player has enough resources to draw the card and
            // the robber is not activated

            this.gameSettings.getCurrentPlayer().buyDeveloppementCard();

            this.updateStatusBars();
            return true;
        }
        return false;
    }

    /**
     * Verify the given value of the dice
     *
     * @param diceValue The value of the dice
     */
    private boolean onDiceRolled(Integer diceValue) {
        this.diceValue = diceValue;
        if (diceValue != null && diceValue == 7) {
            this.activateRobber();
            return true;
        }

        if (this.gameBoardPane.diceRolled(diceValue)) {
            this.updateStatusBars();
            return true;
        }
        return false;
    }

    /**
     * Current player uses the given card card
     *
     * @param usedCard The card which will be used by the current player
     */
    private void onCardUsed(Card usedCard) {
        if (usedCard == Card.KNIGHT) {
            this.activateRobber();
        }
        usedCard.use(this.gameSettings.getCurrentPlayer());
        this.updateStatusBars();
    }

    /**
     * Current player stock a card
     *
     * @param stockedCard The card to stock
     */
    private void onCardStocked(Card stockedCard) {
        stockedCard.stock(this.gameSettings.getCurrentPlayer());
        this.updateStatusBars();
    }

    /**
     * Verify if the robber can be moved to the given tile
     *
     * @param tile The tile to move the robber
     * @return True if the robber can be moved to the given tile, false if not
     */
    private boolean onRobberMoved(Hextile tile) {

        if (this.gameSettings.isRobberActivated() && tile.getPlayable()) {
            this.playRobber(tile);
            this.gameSettings.setRobberActivated(false);
            this.desactivateRobber();
            tile.setPlayable(false);
            return true;
        }
        return false;
    }

    /**
     * Method verify if a settlement can be added to the given tile with given index
     *
     * @param tile The tile to add the settlement
     * @param settlementIndex The settlement index to add the settlement
     * @return True if a settlement can be added to the given slot false if not
     */
    private boolean onSettlementAdded(Hextile tile, Integer settlementIndex) {

        if (this.gameSettings.isRobberActivated()) return false;
        if ((this.gameSettings.getRoundNumber() == 0
                        && this.gameSettings.getCurrentPlayer().getNbSettlement() != 2)
                | (this.gameSettings.getRoundNumber() == 1
                        && this.gameSettings.getCurrentPlayer().getNbSettlement() != 1))
            return false;

        try {
            if (!tile.canAddSettlement(settlementIndex)) return false;
        } catch (NoSuchSlotException e) {
            e.printStackTrace();
            return false;
        }
        if (this.gameSettings.getCurrentPlayer().canBuildSettlement()) {

            if (tile.getSettlementSlot(settlementIndex) != null) {
                if (tile.getSettlementSlot(settlementIndex) instanceof City) {
                    return false;
                }
                if (tile.getSettlementSlot(settlementIndex).getOwner() == null
                        || !tile.getSettlementSlot(settlementIndex)
                                .getOwner()
                                .equals(this.gameSettings.getCurrentPlayer())) {
                    return false;
                }
                if (!this.gameSettings.getCurrentPlayer().canBuildCity()) return false;
                try {
                    tile.upgradeSettlement(settlementIndex);
                    this.revalidate();
                    this.repaint();
                    this.gameSettings.getCurrentPlayer().buildCity();
                    this.updateStatusBars();
                    return true;
                } catch (NoSuchSlotException ignore) {
                    return false;
                }
            }
            try {
                tile.addSettlement(
                        settlementIndex, new Settlement(this.gameSettings.getCurrentPlayer()));
            } catch (SlotAlreadyTakenException | NoSuchSlotException ignore) {
                return false;
            }
            this.revalidate();
            this.repaint();
            this.gameSettings.getCurrentPlayer().buildSettlement();
            return true;
        }
        return false;
    }

    /** let the AI play */
    private void playAI() throws IOException {
        Random r = new Random();

        if (this.gameSettings.getRoundNumber() > 1) {

            boardSidePane.roll();
            if (this.gameSettings.isRobberActivated()) {

                this.gameBoardPane.getBoard().randomRobber();
                this.desactivateRobber();
                this.gameBoardPane.revalidate();
                this.gameBoardPane.repaint();
                this.updateStatusBars();
                return;
            }
            return;
        }

        if (this.gameSettings.getRoundNumber() < 2) {

            this.gameBoardPane.getBoard().buildRoad(this.gameSettings.getCurrentPlayer());
            this.gameSettings.getCurrentPlayer().buildRoad();
            this.gameBoardPane.getBoard().buildSettlement(this.gameSettings.getCurrentPlayer());
            this.gameBoardPane.revalidate();
            this.gameBoardPane.repaint();
            this.gameSettings.getCurrentPlayer().buildSettlement();
            // this.next();
            this.revalidate();
            this.repaint();
            return;
        }

        if (!this.gameSettings.getCurrentPlayer().canBuildSettlement()
                && !this.gameSettings.getCurrentPlayer().canBuildCity()
                && !this.gameSettings.getCurrentPlayer().canBuildRoad()
                && !this.gameSettings.getCurrentPlayer().canBuyDeveloppementCard()) {

            return;
        }

        if (this.gameSettings.getCurrentPlayer().canBuildCity()) {

            boolean res = r.nextBoolean();
            if (res) {
                this.gameBoardPane.getBoard().addCity(this.gameSettings.getCurrentPlayer());
                this.gameSettings.getCurrentPlayer().buildCity();
                this.updateStatusBars();
                this.playAI();
                return;
            }
        }

        if (this.gameSettings.getCurrentPlayer().canBuildRoad()) {

            boolean res = r.nextBoolean();
            if (res) {
                this.gameBoardPane.getBoard().buildRoad(this.gameSettings.getCurrentPlayer());
                this.gameSettings.getCurrentPlayer().buildRoad();
                this.updateStatusBars();
                this.playAI();
                return;
            }
        }
        if (this.gameSettings.getCurrentPlayer().canBuildSettlement()) {

            boolean res = r.nextBoolean();
            if (res) {
                this.gameBoardPane.getBoard().buildSettlement(this.gameSettings.getCurrentPlayer());
                this.gameSettings.getCurrentPlayer().buildSettlement();
                this.playAI();
                return;
            }
        }
        if (this.gameSettings.getCurrentPlayer().canBuyDeveloppementCard()) {

            boolean res = r.nextBoolean();
            if (res) {
                this.boardSidePane.drawCard();
                // this.gameSettings.getCurrentPlayer().buyDeveloppementCard();
                boolean res2 = r.nextBoolean();
                if (res2) {
                    this.boardSidePane.stockCard();
                } else {
                    this.boardSidePane.useCard();
                }
                this.playAI();
            }
        }
    }

    /**
     * Method handles onRoadAdded event
     *
     * @param tile The tile on which the road is added
     * @param roadIndex The index of the road slot to add
     * @param lineModel The line modelised in 2D line
     * @return True if the road can be added
     */
    private boolean onRoadAdded(Hextile tile, Integer roadIndex, Line2D lineModel) {
        if (this.gameSettings.isRobberActivated()) return false;
        if ((this.gameSettings.getRoundNumber() == 0
                        && this.gameSettings.getCurrentPlayer().getNbRoad() != 2)
                | (this.gameSettings.getRoundNumber() == 1
                        && this.gameSettings.getCurrentPlayer().getNbRoad() != 1)) return false;

        if (this.gameSettings.getCurrentPlayer().canBuildRoad()) {
            Road r = new Road(this.gameSettings.getCurrentPlayer());
            if (tile.getRoadSlot(roadIndex) != null) return false;
            try {
                if (!tile.canAddRoad(roadIndex, r)) return false;
            } catch (NoSuchSlotException e) {
                e.printStackTrace();
                return false;
            }
            try {
                tile.addRoad(roadIndex, r);
                this.revalidate();
                this.repaint();
                this.gameSettings.getCurrentPlayer().buildRoad();
                this.updateStatusBars();
                this.gameSettings.getCurrentPlayer().addRoad(lineModel.getP1(), lineModel.getP2());
                this.gameSettings
                        .getCurrentPlayer()
                        .setAchievement(
                                Achievements.ROAD,
                                this.gameSettings.getCurrentPlayer().longestRoad());
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
                bottomSize = new Dimension(size.width, 3 * size.height / 16),
                centerSize = new Dimension(size.width, 10 * size.height / 16);

        this.topStatusPane.setSize(topSize);
        this.topStatusPane.setPreferredSize(topSize);
        // this.gameBoardPane.setSize(centerSize);
        // this.gameBoardPane.setPreferredSize(centerSize);
        this.bottomStatusPane.setSize(bottomSize);
        this.bottomStatusPane.setPreferredSize(bottomSize);

        this.gameBoardPane.setMinimumSize(
                new Dimension(30 * centerSize.width / 100, centerSize.height));
        this.boardSidePane.setMaximumSize(
                new Dimension(70 * centerSize.width / 100, centerSize.height));

        this.gameBoardPane.setMaximumSize(
                new Dimension(50 * centerSize.width / 100, centerSize.height));
        this.boardSidePane.setMinimumSize(
                new Dimension(50 * centerSize.width / 100, centerSize.height));

        this.gameBoardPane.setPreferredSize(
                new Dimension(30 * centerSize.width / 100, centerSize.height));
        this.boardSidePane.setPreferredSize(
                new Dimension(70 * centerSize.width / 100, centerSize.height));

        JSplitPane centerPanel =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.gameBoardPane, this.boardSidePane);

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

    /** Passes to the next player */
    private void next() throws IOException {

        this.gameSettings.next();

        if (gameSettings.getCurrentPlayer().isAI()) {

            playAI();

            this.next();
        }
        this.bottomStatusPane.setCurrentPlayer(
                this.gameSettings.getCurrentPlayer(),
                this.gameSettings.getCurrentPlayerIndex() + 1);
        this.topStatusPane.updatePlayerCard();
        // this.bottomStatusPane.revalidate();
        // this.bottomStatusPane.repaint();
        if (this.gameSettings.getRoundNumber() > 1) {
            this.diceValue = null;
            this.boardSidePane.resetDice();
        }
        this.boardSidePane.revalidate();
        this.boardSidePane.repaint();
    }

    /** Updates top and bottom status bars */
    private void updateStatusBars() {
        if (this.gameSettings.isGameEnd()) {
            this.onGameEnd.accept(this.gameSettings.getCurrentPlayer());
        }
        this.topStatusPane.updatePlayerCard();
        this.bottomStatusPane.update();
        this.boardSidePane.revalidate();
        this.boardSidePane.repaint();
    }

    /** Deactivates robber */
    private void desactivateRobber() {
        this.gameSettings.setRobberActivated(false);
        this.setBackground(this.defaultBackground);
        this.robberLabel.setVisible(false);
    }

    /** Activate robber mode */
    private void activateRobber() {
        this.setBackground(Color.RED);
        this.boardSidePane.disableDice();
        this.gameSettings.setRobberActivated(true);
        this.robberLabel.setVisible(true);
    }

    /**
     * Method plays robber
     *
     * @param tile The hextile that the robber is placed
     */
    private void playRobber(Hextile tile) {
        Set<Player> targetPlayers =
                tile.getSettlementOwners().stream()
                        .filter((Player p) -> p.getRessourceNumber() != 0)
                        .collect(Collectors.toSet());
        if (targetPlayers.size() == 0) return;
        Random r = new Random();
        int index = r.nextInt(targetPlayers.size());
        Player targetPlayer = null;
        for (Player p : targetPlayers) {
            if (index == 0) {
                targetPlayer = p;
                break;
            }
            index--;
        }
        if (targetPlayer == null) return;
        Resource resource = targetPlayer.getRandomResource();
        this.gameSettings.getCurrentPlayer().updateResource(resource, 1);
        targetPlayer.updateResource(resource, -1);
        this.updateStatusBars();
        this.revalidate();
        this.repaint();
    }

    /**
     * Get the harbors owned by the current user
     *
     * @return The list of harbors owned by the current user
     */
    Set<Harbor> getHarborsOwnedByCurrentPlayer() {
        Set<Harbor> harbors =
                this.gameBoardPane
                        .getBoard()
                        .getHarborsOfPlayer(this.gameSettings.getCurrentPlayer());

        return harbors;
    }
}
