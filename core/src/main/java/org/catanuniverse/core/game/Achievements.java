/*
	22015094 - Idil Saglam*/
package org.catanuniverse.core.game;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

public enum Achievements {
    KNIGHT,
    DEVCARD,
    ROAD,
    RECARD,
    TROPHY;

    /**
     * Get the buffered image related to the Achievement
     *
     * @return The BufferedImage related to the achievement
     * @throws IOException If the Image file does not exists
     */
    public Image getImage(int width, int height, int hints) throws IOException {
        System.out.printf("Current achievement %s\n", this.toString());
        return ImageIO.read(
                        this.getClass()
                                .getResource(
                                        switch (this) {
                                            case KNIGHT -> "/sword.png";
                                            case DEVCARD -> "/card.png";
                                            case RECARD -> "/card2.png";
                                            case ROAD -> "/route.png";
                                            case TROPHY -> "/winn.png";
                                        }))
                .getScaledInstance(width, height, hints);
    }

    /**
     * Get the tooltip test
     *
     * @return The tooltip text for the current achievement
     */
    public String getTooltipText() {
        return switch (this) {
            case KNIGHT -> "Knight card";
            case DEVCARD -> "Development card";
            case ROAD -> "Maximum road ";
            case RECARD -> "Resources card";
            case TROPHY -> "Victory point";
        };
    }

    public Image getImage() throws IOException {
        return this.getImage(32, 32, Image.SCALE_SMOOTH);
    }
}
