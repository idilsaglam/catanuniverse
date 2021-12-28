/*
	Idil Saglam
	Abdulrahim Toto
*/
package org.catanuniverse.client.configuration;

import javax.swing.JButton;
import org.catanuniverse.commons.GameType;

class StartGameButton extends JButton {

    private GameType gameType;

    StartGameButton() {
        this.gameType = null;
        super.setVisible(false);
    }

    void setGameType(GameType gameType) {
        this.gameType = gameType;
        if (this.gameType == null) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
        this.setText(
                switch (this.gameType) {
                    case MP_HOST -> "Start server";
                    case MP_GUEST -> "Join the game";
                    case LOCAL -> "Start game";
                });
        this.setEnabled(false);
    }
}
