package model.enums;

import java.awt.Color;

import model.models.Tier;
import model.models.TierHeader;

public enum DefaultTier {
    S(Color.RED),
    A(Color.ORANGE),
    B(Color.YELLOW),
    C(Color.GREEN),
    D(Color.BLUE),
    E(Color.CYAN),
    F(Color.GRAY);

    private Tier value;

    DefaultTier(Color color) {
        this.value = new Tier(new TierHeader(name(), color));
    }

    public Tier value() { return value; }
}