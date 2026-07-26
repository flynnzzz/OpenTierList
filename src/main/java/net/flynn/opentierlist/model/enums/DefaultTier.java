package net.flynn.opentierlist.model.enums;

import javafx.scene.paint.Color;
import net.flynn.opentierlist.model.models.Tier;

/**
 * 
 * @version 1.15
 * @since v1.2.5
 */
public enum DefaultTier {
  // temporary colors
  S(Color.ORANGERED.toString()),
  A(Color.ORANGE.toString()),
  B(Color.YELLOW.toString()),
  C(Color.GREENYELLOW.toString()),
  D(Color.LIGHTBLUE.toString()),
  E(Color.ALICEBLUE.toString()),
  F(Color.GRAY.toString());

  private final Tier value;

  DefaultTier(String color) {
    this.value = new Tier(name(), color);
  }

  public Tier value() {
    return value;
  }
}
