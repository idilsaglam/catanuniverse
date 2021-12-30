/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.client.configuration;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;
import javax.swing.ButtonGroup;
import org.catanuniverse.commons.GameType;

class GameTypeSelector extends SizedContainer {

    private final ButtonGroup buttonGroup;

    private final RadioButton hostServer, joinServer, localGame;

    private Consumer<GameType> onGameTypeSelcted;

    GameTypeSelector(Dimension size, Consumer<GameType> onGameTypeSelcted) {

        super(size);
        this.onGameTypeSelcted = onGameTypeSelcted;
        // JLabel label = new JLabel(labelText == null ? "Game type" : labelText);

        this.buttonGroup = new ButtonGroup();

        this.hostServer =
                new RadioButton(
                        "Create an online server",
                        "User your local machine to host the online server");
        this.joinServer =
                new RadioButton(
                        "Join an online server",
                        "Join to an existing online server with server ip address");
        this.localGame =
                new RadioButton(
                        "Create a local game",
                        "Create a single-player or multi-player game on your machine");

        this.buttonGroup.add(this.hostServer);
        this.buttonGroup.add(this.joinServer);
        this.buttonGroup.add(this.localGame);

        this.hostServer.addActionListener(this::callback);
        this.joinServer.addActionListener(this::callback);
        this.localGame.addActionListener(this::callback);

        super.add(this.hostServer);
        super.add(this.joinServer);
        super.add(this.localGame);
        super.setLayout(new GridLayout(1, 3));
    }

    /**
     * Updates the on game type selected callback
     * @param callback The new callback function
     */
    public void onGameTypeSelected(Consumer<GameType> callback) {
        this.onGameTypeSelected = callback;
    }


    /**
     * Handles the radio button call back with related game type
     *
     * @param e ActionEvent from the radio button click
     */
    private void callback(ActionEvent e) {
        if (this.localGame.isSelected()) {
            this.onGameTypeSelcted.accept(GameType.LOCAL);
            return;
        }
        if (this.joinServer.isSelected()) {
            this.onGameTypeSelcted.accept(GameType.MP_GUEST);
            return;
        }
        if (this.hostServer.isSelected()) {
            this.onGameTypeSelcted.accept(GameType.MP_HOST);
            return;
        }
    }
}
