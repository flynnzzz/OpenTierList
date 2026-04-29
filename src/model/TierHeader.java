package model;

import java.awt.Color;

/**
 * Data class used by the {@link Tier} class
 * 
 * @param name String representing a {@link Tier} header name
 * @param color {@link java.awt.Color}
 */
public record TierHeader(String name, Color color) {}
