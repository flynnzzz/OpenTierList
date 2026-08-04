package net.flynn.opentierlist.model.enums;

import net.flynn.opentierlist.model.models.Tier;
import net.flynn.opentierlist.ui.ConfigHolder;

/**
 * 
 * @version 1.15
 * @since v1.2.5
 */
public enum DefaultTier {
  S(ConfigHolder.DEFAULT_S_COLOR),
  A(ConfigHolder.DEFAULT_A_COLOR),
  B(ConfigHolder.DEFAULT_B_COLOR),
  C(ConfigHolder.DEFAULT_C_COLOR),
  D(ConfigHolder.DEFAULT_D_COLOR),
  E(ConfigHolder.DEFAULT_E_COLOR),
  F(ConfigHolder.DEFAULT_F_COLOR);

  private final Tier value;

  DefaultTier(String color) {
    this.value = new Tier(name(), color);
  }

  public Tier value() {
    return value;
  }
}
