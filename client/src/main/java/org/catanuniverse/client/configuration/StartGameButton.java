/*
	22015094 - Idil Saglam
*/
package org.catanuniverse.client.configuration;

import javax.swing.JButton;
import org.catanuniverse.commons.GameType;

class StartGameButton extends JButton {

    private GameType gameType;

    /** Creates a new Start game button */
    StartGameButton() {
        this.gameType = null;
        super.setVisible(false);
    }

    /**
     * Update the text in the start button with the game type
     *
     * @param gameType The new game type to update with
     */
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
