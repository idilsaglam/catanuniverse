/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.client.game.board;

import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

class Avatar extends ImageIcon {

    public Avatar(String username) throws IOException {
        this(username, Sprite.IDENTICON);
    }

    /**
     * Creates an avatar with given parameters
     *
     * @param username The username to generate the avatar
     * @param sprite The sprite for the avatar image
     * @throws IOException If the URL format is incorrect or there's a problem reading the image
     */
    private Avatar(String username, Sprite sprite) throws IOException {
        super(
                ImageIO.read(
                        new URL(
                                String.format(
                                        "https://avatars.dicebear.com/api/%s/%s.svg\n",
                                        sprite.toString(), username))));
    }

    private enum Sprite {
        MALE,
        FEMALE,
        HUMAN,
        IDENTICON,
        INITIALS,
        BOTTTS,
        AVATAARS,
        JDENTICON,
        GRIDY,
        MICAH;

        /**
         * Converts a Sprite value to a string to use in the avatar url
         *
         * @return String related to the Sprite value
         */
        public String toString() {
            return switch (this) {
                case MALE -> "male";
                case FEMALE -> "female";
                case HUMAN -> "human";
                case IDENTICON -> "identicon";
                case INITIALS -> "initials";
                case BOTTTS -> "botts";
                case AVATAARS -> "avataars";
                case JDENTICON -> "jdenticaon";
                case GRIDY -> "gridy";
                case MICAH -> "micah";
            };
        }
    }
}
