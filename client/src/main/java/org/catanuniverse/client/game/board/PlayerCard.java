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
import org.catanuniverse.core.game.Player;

class PlayerCard extends JPanel {

  private final Player player;
  private final JLabel avatarLabel;
  private final JLabel username;
private final PlayerAchievementsContainer achievementsContainer;

  PlayerCard(Player player, int index) throws IOException {
    System.out.printf("PlayerCard constructor called with index %d\n", index);
    this.player = player;
    this.setBackground(Color.PINK);
    this.avatarLabel = new JLabel();
    this.username = new JLabel();
    this.achievementsContainer = new PlayerAchievementsContainer();


    this.setLayout(new GridLayout(1, 2));
    this.setPlayer(player, index);
    JPanel avatarPanel = new JPanel();
    GridBagConstraints gbc= new GridBagConstraints();
    avatarPanel.setLayout(new GridBagLayout());
    gbc.gridx = 0;
    gbc.gridy = 0;
    avatarPanel.add(avatarLabel, gbc);
    gbc.gridy = 1;
    avatarPanel.add(username, gbc);

    this.add(avatarPanel);
    this.add(achievementsContainer);

  }

  public void setPlayer(Player p, int i) throws IOException {
    String avatarURL = String.format("/avatar%d.png", i);
    System.out.printf("Avatar URL %s\n", avatarURL);
    BufferedImage bufferedAvatarImage = ImageIO.read(this.getClass().getResource(avatarURL));
    Image scaledImage = bufferedAvatarImage.getScaledInstance(190,190,Image.SCALE_SMOOTH);
    avatarLabel.setIcon(new ImageIcon(scaledImage));
    username.setText(p.getUsername());
    this.achievementsContainer.updateAchievements();
    this.achievementsContainer.revalidate();
    this.achievementsContainer.repaint();
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
        this.achievementsJLabelMap.get(a).setText(""+PlayerCard.this.player.getAchievement(a));
      }
    }
  }



}
