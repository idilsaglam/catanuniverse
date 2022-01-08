package org.catanuniverse.core.game;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

public enum Card {
  BLE2,
  MINERAL1,
  VP1,
  VP2;

  /**
   * Player use a card
   * @param p Player
   */
  public void use(Player p) {
/*    (Integer cardNumber)->{
      if(cardNumber == 0){
        gameSettings.getCurrentPlayer().updateResource(Resource.Corn,2);
      }
      if (cardNumber == 1){
        gameSettings.getCurrentPlayer().addVictoryPoint(2);
      }
      if(cardNumber == 2){
        gameSettings.getCurrentPlayer().addVictoryPoint(1);
      }
      if(cardNumber == 3){
        gameSettings.getCurrentPlayer().updateResource(Resource.Mineral,1);
      }
      System.out.println(cardNumber);
    }*/
    switch (this) {
      case BLE2 -> {
      }
      case MINERAL1 -> {
      }
      case VP1 -> {
      }
      case VP2 -> {
      }
    }
  }

  /**
   * Returns a Card from a given integer value
   * @param index The index of the card
   * @return The card corresponding the given index
   */
  public static Card fromInt(int index) {
    return switch(index) {
      case 0 -> BLE2;
      case 1 -> VP2;
      case 2 -> VP1;
      default -> MINERAL1;
    };
  }

  public void stock(Player currentPlayer) {
    currentPlayer.incrementUserCards(this, 1);
  }

  /**
   * Get the buffered image related to the Achievement
   * @return The BufferedImage related to the achievement
   * @throws IOException If the Image file does not exists
   */
  public Image getImage(int width, int height, int hints) throws IOException {
    System.out.printf("Current achievement %s\n", this.toString());
    String imagePath = String.format("/%s.png", switch(this) {
      case VP1 -> "cart2";
      case VP2 -> "cart1";
      case BLE2 -> "cart0";
      case MINERAL1 -> "cart3";
    });
    System.out.printf("Card getImage image path %s\n", imagePath);
    return ImageIO.read(this.getClass().getResource(imagePath)).getScaledInstance(width, height, hints);
  }

  public Image getImage() throws IOException {
    return this.getImage(64,100,Image.SCALE_SMOOTH);
  }
}
