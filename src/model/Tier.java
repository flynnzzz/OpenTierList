package model;

import java.awt.Color;
import java.util.Collections;
import java.util.Objects;

import model.enums.TierStringFormat;
import model.exceptions.ElementNotFoundException;

/**
 * Class representing the concept of a 'Tier'.
 * 
 * Each Tier contains a {@link TierHeader} and a collection of {@link TierElement}
 * 
 * @author flynnz
 * @version 1.30
 * @since v0.0.0
 */
public class Tier {
	
	public static final String DEFAULT_TIER_NAME = "New Tier";
	public static final Color DEFAULT_TIER_COLOR = Color.gray;
	
	private TierHeader header;
	private ListTierElement elements;
	
	//---------------------------------- Ctors ----------------------------------//
	
	/**
	 * Constructs a new {@link Tier} object with the given {@link ListTierElement}.
	 * 
	 * @param header {@link TierHeader} 
	 * @param elements collection to add
	 * @throws IllegalArgumentException if @param ranked is empty
	 */
	public Tier(TierHeader header, ListTierElement elements) {
		Objects.requireNonNull(header); Objects.requireNonNull(elements);
		
		this.header = header;
		this.elements = elements;
	}	
	
	/**
	 * Constructs a new empty {@link Tier} object with given {@link TierHeader}.
	 * 
	 * @param th {@link TierHeader}
	 */
	public Tier(TierHeader header) { 
		this(header, new ListTierElement());
	}
	
	/**
	 * Constructs a new empty {@link Tier} object
	 */
	public Tier() { 
		this(new TierHeader(DEFAULT_TIER_NAME, DEFAULT_TIER_COLOR));
	}
	
	//---------------------------------- methods  ----------------------------------//
	
	public boolean add(TierElement e) throws IllegalArgumentException {
		if (elements.contains(e)) throw new IllegalArgumentException("Tier already contains this element: " + e);
		else return elements.add(e);
	}
	
	public TierElement moveTo(int to, TierElement e) throws IndexOutOfBoundsException, ElementNotFoundException {
		if (!elements.contains(e)) throw new ElementNotFoundException();
		
		if (to < elements.indexOf(e)) {
			for (int i = elements.indexOf(e); i > to; i--) {
				elements.set(i, elements.get(i - 1));
			}
		}
		else if (to > elements.indexOf(e)) {
			for (int i = elements.indexOf(e); i < to; i++) {
				elements.set(i, elements.get(i + 1));
			}
		}
		
		return elements.set(to, e);
	}
	
	public boolean remove(TierElement e) throws ElementNotFoundException {
		if (!elements.remove(e)) throw new ElementNotFoundException();
		else return true;
	}
	
	public TierElement remove(int i) throws IndexOutOfBoundsException {
		return elements.remove(i);
	}
	
	public void swap(TierElement a, TierElement b) throws IndexOutOfBoundsException {
		swap(elements.indexOf(a), elements.indexOf(b));
	}
	
	private void swap(int a, int b) throws IndexOutOfBoundsException {
		Collections.swap(elements, a, b);
	}
	
	
	//---------------------------------- setters and getters ----------------------------------//	
	
	public void setName(String name) throws IllegalArgumentException {
		Objects.requireNonNull(name);
		if (name.isBlank()) throw new IllegalArgumentException();
		setHeader(new TierHeader(name, this.header.color()));
	}
	
	public void setColor(Color color) throws IllegalArgumentException {
		Objects.requireNonNull(color);
		setHeader(new TierHeader(this.header.name(), color));
	}
	
	private void setHeader(TierHeader header) { 
		Objects.requireNonNull(header);
		this.header = header;
	}
	
	public TierHeader getHeader() { return new TierHeader(this.header.name(), this.header.color()); }
	public ListTierElement getElements() { return new ListTierElement(elements); }


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

