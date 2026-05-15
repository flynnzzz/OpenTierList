package model;

import java.awt.Color;
import java.util.Collections;
import java.util.Objects;

import model.enums.TierStringFormat;

/**
 * Class representing the concept of a 'Tier'.
 * 
 * Each Tier contains a {@link TierHeader} and a collection of {@link TierElement}
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
	private TierElementList elements;
	
	//---------------------------------- Ctors ----------------------------------//
	
	/**
	 * Constructs a new {@link Tier} object with the given {@link TierElementList}.
	 * 
	 * @param header {@link TierHeader} 
	 * @param elements collection to add
	 * @throws IllegalArgumentException if @param ranked is empty
	 */
	public Tier(TierHeader header, TierElementList elements) {
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
		this.elements = new TierElementList();
	}
	
	/**
	 * Constructs a new empty {@link Tier} object
	 */
	public Tier() { 
		this.header = new TierHeader(DEFAULT_TIER_NAME, DEFAULT_TIER_COLOR);
		this.elements = new TierElementList();  
	}
	
	//---------------------------------- methods  ----------------------------------//
	
	public boolean add(TierElement e) {
		return elements.add(e);
	}
	
	public TierElement add(TierElement e, int i) throws IndexOutOfBoundsException {
		return elements.set(i, e);
	}
	
	public boolean remove(TierElement e) {
		return elements.remove(e);
	}
	
	public TierElement remove(int i) throws IndexOutOfBoundsException {
		return elements.remove(i);
	}
	
	public void swap(TierElement a, TierElement b) throws IndexOutOfBoundsException {
		int ia = elements.indexOf(a),
			ib = elements.indexOf(b);
		
		Collections.swap(elements, ia, ib);
	}
	
	public void swap(int a, int b) throws IndexOutOfBoundsException {
		Collections.swap(elements, a, b);
	}
	
	
	//---------------------------------- setters and getters ----------------------------------//	
	
	public void setHeader(TierHeader header) { 
		Objects.requireNonNull(header);
		this.header = header;
	}
	public TierHeader getHeader() { return new TierHeader(header.name(), header.color()); }
	
	public TierElementList getElements() { return new TierElementList(elements); }


	//---------------------------------- hashCode, equals and toString ----------------------------------//
	
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
	 * 	"header name: [ element1, element2, ... ]"
	 * 
	 * @return {@link String}
	 */
	public String toString() {
		 return toStringCompact();
	}
	
	/**
	 * Returns the {@link Tier} as {@link String} with the specified {@link TierStringFormat}
	 * 
	 * Format {@link TierStringFormat#EXTENDED}:
	 * 	"header name:
	 * 	[
	 * 		element1,
	 * 		element2,
	 * 		...		
	 * 	]"
	 * 
	 * Format {@link TierStringFormat#COMPACT}:
	 * 	"header name: [ element1, element2, ... ]"
	 * 
	 * @return {@link String}
	 */
	public String toString(TierStringFormat format) {
		switch (format) {
			case EXTENDED: return toStringExtended();
			default: return toString();
		}
	}
	
	private String toStringCompact() {
		var sb = new StringBuilder();
		sb.append(getHeader().name() + ":" + System.lineSeparator());
		sb.append(getElements().toString());
		return sb.toString();
	}
	
	private String toStringExtended() {
		var sb = new StringBuilder();
		sb.append(getHeader().name() + ":" + System.lineSeparator());
		sb.append("[");
		sb.append(System.lineSeparator());
		for (TierElement e : elements) {	
			sb.append("\t");
			sb.append(e); 
			if (!elements.getLast().equals(e))
				sb.append(",");
			else
				sb.append(".");
			sb.append(System.lineSeparator());
		}
		sb.append("]");
		return sb.toString();
	}
}

