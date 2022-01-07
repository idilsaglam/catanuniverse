package org.catanuniverse.core.game;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

public enum Achievements {
  // TODO: Change names
  SWORD,
  CARD,
  ROAD,
  WOOD,
  TROPHY;

  /**
   * Get the buffered image related to the Achievement
   * @return The BufferedImage related to the achievement
   * @throws IOException If the Image file does not exists
   */
  public Image getImage(int width, int height, int hints) throws IOException {
    System.out.printf("Current achievement %s\n", this.toString());
    return ImageIO.read(this.getClass().getResource(switch (this) {
      case SWORD -> "/sword.png";
      case CARD -> "/card.png";
      case WOOD -> "/wood.png";
      case ROAD -> "/route.png";
      case TROPHY -> "/winn.png";
    })).getScaledInstance(width, height, hints);
  }

  public Image getImage() throws IOException {
    return this.getImage(32,32, Image.SCALE_SMOOTH);
  }
}
