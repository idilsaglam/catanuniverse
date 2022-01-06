/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;


import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

public enum Resource {
    Wood,
    Clay,
    Wool,
    Corn,
    Mineral;

    public Image getImage(int width, int height, int scale) throws IOException {
        return ImageIO.read(this.getClass().getResource(String.format("/%s.png",switch (this) {
            case Wood -> "wood";
            case Corn -> "corn";
            case Clay -> "argile";
            case Mineral -> "mineral";
            case Wool -> "sheep";
        }))).getScaledInstance(width, height, scale);
    }

    public Image getImage() throws IOException {
        return this.getImage(60, 60, Image.SCALE_SMOOTH);
    }
}
