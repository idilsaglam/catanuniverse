/*
	22015094 - Idil Saglam*/
package org.catanuniverse.core.game;

import java.awt.Image;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public enum Resource {
    Wood,
    Clay,
    Wool,
    Corn,
    Mineral;

    /**
     * Get the Image for the current resource
     *
     * @param width Image width
     * @param height Image height
     * @param scale Image scale constant
     * @return The image object for the current resource value
     * @throws IOException If the image file not found
     */
    public Image getImage(int width, int height, int scale) throws IOException {
        String imagePath =
                String.format(
                        "/%s.png",
                        switch (this) {
                            case Wood -> "wood";
                            case Corn -> "ble";
                            case Clay -> "argile";
                            case Mineral -> "mineral";
                            case Wool -> "sheep";
                        });
        return ImageIO.read(this.getClass().getResource(imagePath))
                .getScaledInstance(width, height, scale);
    }

    public Image getImage() throws IOException {
        return this.getImage(32, 32, Image.SCALE_SMOOTH);
    }

    /**
     * Get a random resource or null
     *
     * @return A random resource or null
     */
    public static Resource random() {
        Random r = new Random();
        int i = r.nextInt(Resource.values().length + 1);
        if (i < Resource.values().length) return Resource.values()[i];
        return null;
    }
}
