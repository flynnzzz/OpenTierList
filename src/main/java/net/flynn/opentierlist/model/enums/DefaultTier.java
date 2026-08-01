package net.flynn.opentierlist.model.enums;

import net.flynn.opentierlist.model.models.Tier;
import net.flynn.opentierlist.ui.manual.UISettings;

/**
 * 
 * @version 1.15
 * @since v1.2.5
 */
public enum DefaultTier {
  S(UISettings.DEFAULT_S_COLOR_LIGHT),
  A(UISettings.DEFAULT_A_COLOR_LIGHT),
  B(UISettings.DEFAULT_B_COLOR_LIGHT),
  C(UISettings.DEFAULT_C_COLOR_LIGHT),
  D(UISettings.DEFAULT_D_COLOR_LIGHT),
  E(UISettings.DEFAULT_E_COLOR_LIGHT),
  F(UISettings.DEFAULT_F_COLOR_LIGHT);

  private final Tier value;

  DefaultTier(String color) {
    this.value = new Tier(name(), color);
  }

  public Tier value() {
    return value;
  }
}
