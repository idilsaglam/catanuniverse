package org.catanuniverse.core.utils;

import java.awt.Color;
import java.util.Random;

public interface Helpers {
  Random r = new Random();

  static Color randomColor() {
    return new Color(r.nextInt(256), r.nextInt(256), r.nextInt(256));
  }

}
