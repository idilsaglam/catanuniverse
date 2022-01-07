package org.catanuniverse.client.game.board;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.catanuniverse.core.game.Achievements;
import org.catanuniverse.core.game.Card;
import org.catanuniverse.core.game.Player;

class PlayerCard extends JPanel {
  private Player player;
  private final JLabel avatarLabel;
  private final JLabel username;
  private final int widthPlayer, heightPlayer;
  private final PlayerAchievementsContainer achievementsContainer;

  PlayerCard(Player player, int index, int imageW, int imageH) throws IOException {
    this.heightPlayer = imageH;
    this.widthPlayer = imageW;
    System.out.printf("PlayerCard constructor called with index %d\n", index);
    this.player = player;
    this.avatarLabel = new JLabel();
    this.username = new JLabel();
    this.achievementsContainer = new PlayerAchievementsContainer();


    GridBagConstraints gbc= new GridBagConstraints();
    this.setLayout(new GridBagLayout());
    this.setPlayer(player, index);
    JPanel avatarPanel = new JPanel();
    avatarPanel.setLayout(new GridBagLayout());
    gbc.gridx = 0;
    gbc.gridy = 0;
    avatarPanel.add(avatarLabel, gbc);
    gbc.gridx = 0;
    gbc.gridy = 1;
    avatarPanel.add(username, gbc);

    gbc.gridx = 0;
    gbc.gridy = 0;
    this.add(avatarPanel, gbc);
    gbc.gridx = 1;
    gbc.gridy = 0;
    this.add(achievementsContainer, gbc);

  }
  PlayerCard(Player player, int index) throws IOException {
    this(player, index, 64, 64);
  }

  public void setPlayer(Player p, int i) throws IOException {
    this.player = p;
    String avatarURL = String.format("/avatar%d.png", i);
    System.out.printf("Avatar URL %s\n", avatarURL);
    BufferedImage bufferedAvatarImage = ImageIO.read(this.getClass().getResource(avatarURL));
    Image scaledImage = bufferedAvatarImage.getScaledInstance(this.widthPlayer,this.heightPlayer,Image.SCALE_SMOOTH);
    avatarLabel.setIcon(new ImageIcon(scaledImage));
    username.setText(this.player.getUsername());
    this.setBorder(BorderFactory.createMatteBorder(
            0, 25, 0, 0, player.getColor()));
    this.achievementsContainer.updateAchievements();
    this.achievementsContainer.revalidate();
    this.achievementsContainer.repaint();
  }

  /**
   * Updates the achievement container of the player
   */
  public void updateAchievements() {
    this.achievementsContainer.updateAchievements();
  }

  private class PlayerAchievementsContainer extends JPanel {

    private final Map<Achievements, JLabel> achievementsJLabelMap = new HashMap<>(), achievementsJLabelImageMap = new HashMap<>();
    PlayerAchievementsContainer() throws IOException {
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      this.setLayout(new GridBagLayout());
      for (Achievements a: Achievements.values()) {
        this.achievementsJLabelMap.put(a, new JLabel());
        this.achievementsJLabelImageMap.put(a, new JLabel(new ImageIcon(a.getImage())));
      }
      this.updateAchievements();
      for (Achievements a: Achievements.values()) {
        this.add(this.achievementsJLabelImageMap.get(a));
        this.add(this.achievementsJLabelMap.get(a));
      }
    }

    public void updateAchievements() {
      for (Achievements a: Achievements.values()) {
      System.out.printf("Achievements card update achievements methods called. Username %s Achievement name %s Achievement value %d\n", PlayerCard.this.player.getUsername(), a, PlayerCard.this.player.getAchievement(a));
        this.achievementsJLabelMap.get(a).setText(""+PlayerCard.this.player.getAchievement(a));
        this.achievementsJLabelMap.get(a).revalidate();
        this.achievementsJLabelMap.get(a).repaint();
      }
    }

  }



}
