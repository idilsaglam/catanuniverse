package org.catanuniverse.client.game.board;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.catanuniverse.core.game.Achievements;
import org.catanuniverse.core.game.Player;

class PlayerCard extends JPanel {

  private final Player player;

  PlayerCard(Player player, int index) throws IOException {
    this.player = player;
    this.setBackground(Color.PINK);
    JLabel avatarLabel;
    PlayerAchievementsContainer achievementsContainer = new PlayerAchievementsContainer();
    String avatarURL = String.format("/avatar%d.png", index);
    System.out.printf("Avatar URL %s\n", avatarURL);
    BufferedImage bufferedAvatarImage = ImageIO.read(this.getClass().getResource(avatarURL));
    Image scaledImage = bufferedAvatarImage.getScaledInstance(240,240,Image.SCALE_SMOOTH);
    avatarLabel = new JLabel(new ImageIcon(scaledImage));
    JLabel username = new JLabel();
    username.setText(player.getUsername());
    this.setLayout(new GridLayout(1, 2));
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

  private class PlayerAchievementsContainer extends JPanel {

    PlayerAchievementsContainer() throws IOException {
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      this.setLayout(new GridBagLayout());
      for (int i = 0; i< Achievements.values().length; i++) {
        Achievements a = Achievements.values()[i];
        gbc.gridy = i / 2;
        gbc.weightx = 2;
        if (i == Achievements.values().length - 1 ) {
          gbc.gridwidth = 2;
          gbc.gridx = 1;
          this.add(new JLabel(new ImageIcon(a.getImage())), gbc);
          gbc.gridx = 2;
          this.add(new JLabel(""+PlayerCard.this.player.getAchievement(a)), gbc);
          continue;
        }
        gbc.gridx = (gbc.gridx+1)%4;
        this.add(new JLabel(new ImageIcon(a.getImage())), gbc);
        gbc.gridx++;
        this.add(new JLabel(""+PlayerCard.this.player.getAchievement(a)), gbc);
      }
    }
  }



}
