package net.flynn.opentierlist.model.models;

import javafx.scene.paint.Color;

/**
 * Self-explanatory
 * 
 * @param name  string representing a {@link Tier}'s name
 * @param color a {@link Tier}'s {@link Color}
 */
public record TierHeader(String name, String color) implements Comparable<TierHeader> {

  @Override
  public int compareTo(TierHeader o) {
    return name.compareTo(o.name());
  }
}
