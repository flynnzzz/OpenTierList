package model;

import java.awt.Color;

/**
 * Record class used by the {@link Tier} class
 * 
 * @param name String representing a {@link Tier} header name
 * @param color {@link Color}
 */
public record TierHeader(String name, Color color) implements Comparable<TierHeader> {

	@Override
	public int compareTo(TierHeader o) {
		return name.compareTo(o.name());
	}
}
