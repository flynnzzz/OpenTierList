package model;

import java.awt.Color;
import java.util.List;
import java.util.Objects;

/**
 * Extention class of {@link ElementCollection}.
 * 
 * Aggregation of {@link Element} , with the added extention of 
 * a {@link TierHeader}
 * 
 * @author flynnz
 * @version 0.00
 * @since v0.0.0
 */
public class Tier extends ElementCollection {
	
	/**
	 * The default name is set to {@link String} "New Tier"
	 */
	public static final String DEFAULT_TIER_NAME = "New Tier";
	/**
	 * The default color is set to {@link Color#gray}
	 */
	public static final Color DEFAULT_TIER_COLOR = Color.gray;
	private TierHeader header;
	
	
	/***********************************************************************
	 * 							Constructors
	 **********************************************************************/
	
	/**
	 * Constructs a new {@link Tier} object with the given list of {@link Element}.
	 * 
	 * All parameters must not be null:
	 * @param name {@link Tier} header name as {@link String}
	 * @param color {@link Tier} color as {@link java.awt.Color}
	 * @param ranked the list to encapsulate
	 * @throws IllegalArgumentException if the list passed in is empty
	 */
	public Tier(String name, Color color, List<Element> ranked) {
		if (name == null) throw new NullPointerException("Tier name must not be null");
		if (color == null) throw new NullPointerException("Tier color must not be null");
		
		super(ranked);
		this.header = new TierHeader(name, color);
	}	
	/**
	 * Constructs a new empty {@link Tier} object with name and color.
	 * 
	 * All parameters must not be null:
	 * @param name {@link Tier} header name as {@link String}
	 * @param color {@link Tier} color as {@link java.awt.Color}
	 */
	public Tier(String name, Color color) { super(); this.header = new TierHeader(name, color); }
	
	/**
	 * Constructs a new empty {@link Tier} object with specified name.
	 * 
	 * @param name {@link Tier} header name as {@link String}
	 */
	public Tier(String name) { super(); this.header = new TierHeader(name, DEFAULT_TIER_COLOR); }
	
	/**
	 * Constructs a new empty {@link Tier} object
	 */
	public Tier() { super(); this.header = new TierHeader(DEFAULT_TIER_NAME, DEFAULT_TIER_COLOR);  }
	
	
	/***********************************************************************
	 * 						Setters and getters
	 **********************************************************************/
	
	public String getName() { return header.name(); }
	public void setName(String name) {
		if (name == null) throw new NullPointerException();
		this.header = new TierHeader(name, this.getColor());
	}
	
	public Color getColor() { return header.color(); }
	public void setColor(Color color) { 
		if (color == null) throw new NullPointerException();
		this.header = new TierHeader(this.getName(), color);
	}
	
	public TierHeader getHeader() { return this.header; }
	public void setHeader(TierHeader header) { 
		if (header == null) throw new NullPointerException();
		this.header = header; 
	}

	
	/***********************************************************************
	 * 					hashCode, equals and toString
	 **********************************************************************/
	
	/**
	 * hashCode function
	 * 
	 * @see Objects#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(DEFAULT_TIER_COLOR, DEFAULT_TIER_NAME, header);
		return result;
	}

	/**
	 * equals function
	 * 
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
	 * 	]".
	 * 
	 * @return {@link String}
	 */
	public String toString() {
		 return "Tier: " + header.name() + System.lineSeparator() +
				 super.toString();
	}
}
