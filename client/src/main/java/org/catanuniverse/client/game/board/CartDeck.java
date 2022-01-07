package org.catanuniverse.client.game.board;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.catanuniverse.core.game.Card;
import org.catanuniverse.core.game.Player;

public class CartDeck extends JPanel {

  private final HashMap<Card, CardDeckItem> cardsDeckItems= new HashMap<>();
  private Player currentPlayer;

  public CartDeck(Player currentPlayer) throws IOException {
    this.currentPlayer = currentPlayer;
    this.setBackground(Color.CYAN);
    CardDeckItem cdi;
    for (Card card: Card.values()) {
        cdi = new CardDeckItem((card));
        this.cardsDeckItems.put(card, cdi);
       this.add(cdi);
    }
  }

  /**
   * Changes the current player of the card deck
   * @param currentPlayer The nex current player
   */
  public void setCurrentPlayer(Player currentPlayer) {
    this.currentPlayer = currentPlayer;
    this.updateCards();
  }

  public void updateCards() {
    for (CardDeckItem cardDeckItem: cardsDeckItems.values()) {
      cardDeckItem.update();
    }
  }

  private class CardDeckItem extends JPanel implements MouseListener {

    private final Card card;

    private final JLabel cardCounterLabel;

    public CardDeckItem(Card card) throws IOException {
      this.card = card;
      this.cardCounterLabel = new JLabel();
      this.add(new JLabel(new ImageIcon(card.getImage())));
      this.update();
      this.add(this.cardCounterLabel);
      this.addMouseListener(this);
    }

    /**
     * Update the current card's counter label for current user
     */
    public void update() {
      this.cardCounterLabel.setText("" + CartDeck.this.currentPlayer.getUserCards().get(this.card));
      this.cardCounterLabel.revalidate();
      this.cardCounterLabel.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      System.out.println("Clicked on card");
      System.out.println(this.card);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
  }


}
