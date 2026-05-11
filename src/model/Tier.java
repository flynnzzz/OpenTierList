package model;

import java.awt.Color;
import java.util.Collections;
import java.util.Objects;

/**
 * Class representing the concept of a 'Tier'.
 * 
 * Each Tier contains a {@link TierHeader} and a collection of {@link Element}
 * 
 * @author flynnz
 * @version 1.00
 * @since v0.0.0
 */
public class Tier {
	
	/**
	 * The default name is set to {@link String} "New Tier"
	 */
	public static final String DEFAULT_TIER_NAME = "New Tier";
	/**
	 * The default color is set to {@link Color#gray}
	 */
	public static final Color DEFAULT_TIER_COLOR = Color.gray;
	
	private TierHeader header;
	private ElementCollection elements;
	
	/***********************************************************************
	 * 							Constructors
	 **********************************************************************/
	
	/**
	 * Constructs a new {@link Tier} object with the given {@link ElementCollection}.
	 * 
	 * @param header {@link TierHeader} 
	 * @param elements collection to add
	 * @throws IllegalArgumentException if @param ranked is empty
	 */
	public Tier(TierHeader header, ElementCollection elements) {
		Objects.requireNonNull(header); Objects.requireNonNull(elements);
		if (elements.isEmpty()) throw new IllegalArgumentException();
		
		this.header = header;
		this.elements = elements;
	}	
	
	/**
	 * Constructs a new empty {@link Tier} object with given {@link TierHeader}.
	 * 
	 * @param th {@link TierHeader}
	 */
	public Tier(TierHeader header) { 
		Objects.requireNonNull(header);

		this.header = header;
		this.elements = new ElementCollection();
	}
	
	/**
	 * Constructs a new empty {@link Tier} object
	 */
	public Tier() { 
		this.header = new TierHeader(DEFAULT_TIER_NAME, DEFAULT_TIER_COLOR);
		this.elements = new ElementCollection();  
	}
	
	/***********************************************************************
	 * 							Class methods
	 **********************************************************************/
	
	public boolean add(Element e) {
		return this.getElements().add(e);
	}
	
	public Element add(Element e, int i) {
		return this.getElements().set(i, e);
	}
	
	public boolean remove(Element e) {
		return this.getElements().remove(e);
	}
	
	public Element remove(int i) {
		return this.getElements().remove(i);
	}
	
	public void swap(Element a, Element b) {
		int ia = getElements().indexOf(a),
			ib = getElements().indexOf(b);
		
		Collections.swap(getElements(), ia, ib);
	}
	
	public void swap(int a, int b) {
		Collections.swap(getElements(), a, b);
	}
	
	/***********************************************************************
	 * 						Setters and getters
	 **********************************************************************/
	
	public void setHeader(TierHeader header) { 
		Objects.requireNonNull(header);
		this.header = header;
	}
	public TierHeader getHeader() { return header; }
	
	public ElementCollection getElements() { return elements; }


	/***********************************************************************
	 * 					hashCode, equals and toString
	 **********************************************************************/
	
	@Override
	public int hashCode() {
		return Objects.hash(elements, header);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Tier)) {
			return false;
		}
		Tier other = (Tier) obj;
		return Objects.equals(elements, other.elements) && Objects.equals(header, other.header);
	}

	@Override
	/**
	 * Returns the {@link Tier} as {@link String}
	 * 
	 * Format:
	 * 	"header name:
	 * 	[
	 * 		element1,
	 * 		element2,
	 * 		...		
	 * 	]".
	 * 
	 * @return {@link String}
	 */
	public String toString() {
		 return getHeader().name() + ":" + System.lineSeparator() 
		 + getElements().toString();
	}
}
