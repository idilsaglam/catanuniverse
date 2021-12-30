/*
	Binôme 35
	22015094 - Idil Saglam
	 - Abderrahim Arous
*/
package org.catanuniverse.core.game;

public class Player {
  private String username;

  public Player(String username) {
      this.username = username;
  }

  public Player() {
    this("");
  }

  public String getUsername() {
    return this.username;
  }
}
