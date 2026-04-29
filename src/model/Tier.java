package model;

import java.awt.Color;
import java.util.List;
import java.util.Objects;

/**
 * Extention class of {@link ElementCollection}
 * 
 * Aggregation of {@link Element} , with the added extention of 
 * a {@link TierHeader} 
 * 
 * @author flynnz
 * @version 0.00
 * @since v0.0.0
 */
public class Tier extends ElementCollection {
	
	public static final String DEFAULT_NAME = "New Tier";
	public static final Color DEFAULT_COLOR = Color.gray;
	
	private TierHeader header;
	
	/**
	 * Constructs a new {@link Tier} object with the given list of {@link Element}
	 * 
	 * All parameters must not be null:
	 * @param name {@link Tier} header name as {@link String}
	 * @param color {@link Tier} color as {@link java.awt.Color}
	 * @param elements the list to encapsulate
	 * @throws NullPointerException if either parameter is null
	 * @throws IllegalArgumentException if elements is empty
	 */
	public Tier(String name, Color color, List<Element> ranked) {
		if (name == null) throw new NullPointerException("Tier name must not be null");
		if (color == null) throw new NullPointerException("Tier color must not be null");
		
		super(ranked);
		this.header = new TierHeader(name, color);
	}	
	/**
	 * Constructs a new empty {@link Tier} object
	 * 
	 * All parameters must not be null:
	 * @param name {@link Tier} header name as {@link String}
	 * @param color {@link Tier} color as {@link java.awt.Color}
	 * 
	 * @throws NullPointerException if either parameter is null
	 */
	public Tier(String name, Color color) { super(); this.header = new TierHeader(name, color); }
	/**
	 * Constructs a new empty {@link Tier} object
	 * 
	 * @param name {@link Tier} header name as {@link String}
	 * @throws NullPointerException if the passed parameter is null
	 */
	public Tier(String name) { super(); this.header = new TierHeader(name, DEFAULT_COLOR); }
	/**
	 * Constructs a new empty {@link Tier} object
	 */
	public Tier() { super(); this.header = new TierHeader(DEFAULT_NAME, DEFAULT_COLOR);  }
	
	// Getters and setters:
	public String getName() { return header.name(); }
	public void setName(String name) { this.header = new TierHeader(name, this.getColor());}
	public Color getColor() { return header.color(); }
	public void setColor(Color color) { this.header = new TierHeader(this.getName(), color);}
	public TierHeader getHeader() { return this.header; }
	public void setHeader(TierHeader header) { this.header = header; }
	
	/**
	 * @see Objects#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(DEFAULT_COLOR, DEFAULT_NAME, header);
		return result;
	}

	/**
	 *  @see Objects#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!super.equals(obj)) { return false; }
		
		if (!(obj instanceof Tier other)) { return false; }
		return Objects.equals(header, other.header);
	}

	@Override
	/**
	 * Returns the {@link Tier} as {@link String}
	 * 
	 * Format:
	 * 	"Tier: header name
	 * 	Elements: 
	 * 	[
	 * 		element1,
	 * 		element2,
	 * 		...		
	 * 	]"
	 * 
	 * @return {@link String}
	 */
	public String toString() {
		 return "Tier: " + header.name() + System.lineSeparator() +
				 super.toString();
	}
}
