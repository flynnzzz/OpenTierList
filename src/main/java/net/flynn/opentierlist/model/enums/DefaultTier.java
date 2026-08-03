package net.flynn.opentierlist.model.enums;

import net.flynn.opentierlist.model.models.Tier;
import net.flynn.opentierlist.ui.manual.UISettings;

/**
 * 
 * @version 1.15
 * @since v1.2.5
 */
public enum DefaultTier {
  S(UISettings.DEFAULT_S_COLOR),
  A(UISettings.DEFAULT_A_COLOR),
  B(UISettings.DEFAULT_B_COLOR),
  C(UISettings.DEFAULT_C_COLOR),
  D(UISettings.DEFAULT_D_COLOR),
  E(UISettings.DEFAULT_E_COLOR),
  F(UISettings.DEFAULT_F_COLOR);

  private final Tier value;

  DefaultTier(String color) {
    this.value = new Tier(name(), color);
  }

  public Tier value() {
    return value;
  }
}
