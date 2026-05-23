package model.models;

import java.awt.Color;

/**
 * Record class used by {@link Tier}
 * 
 * @param name string representing a {@link Tier}'s name
 * @param color a {@link Tier}'s {@link Color}
 */
public record TierHeader(String name, Color color) implements Comparable<TierHeader> {

	@Override
	public int compareTo(TierHeader o) {
		return name.compareTo(o.name());
	}
}
