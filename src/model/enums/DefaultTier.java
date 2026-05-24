package model.enums;

import javafx.scene.paint.Color;

import model.models.Tier;
import model.models.TierHeader;

/**
 * 
 * @version 1.11
 * @since v1.2.5
 */
public enum DefaultTier {
    S(Color.ORANGERED),
    A(Color.ORANGE),
    B(Color.YELLOW),
    C(Color.GREENYELLOW),
    D(Color.LIGHTBLUE),
    E(Color.ALICEBLUE),
    F(Color.GRAY);

    private Tier value;

    DefaultTier(Color color) {
        this.value = new Tier(new TierHeader(name(), color));
    }

    public Tier value() { return value; }
}