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
    S(Color.ORANGERED),
    A(Color.ORANGE),
    B(Color.YELLOW),
    C(Color.GREENYELLOW),
    D(Color.LIGHTBLUE),
    E(Color.ALICEBLUE),
    F(Color.GRAY);

    private Tier value;

    DefaultTier(Color color) {
        this.value = new Tier(name(), color);
    }

    public Tier value() { return value; }
}
