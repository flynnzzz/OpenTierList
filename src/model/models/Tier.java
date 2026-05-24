package model.models;

import javafx.scene.paint.Color;
import java.util.Collections;
import java.util.Objects;

import model.enums.TierStringFormat;
import model.exceptions.ElementNotFoundException;

/**
 * Class representing the concept of a 'Tier'
 * 
 * @author flynnz
 * @version 2.00
 * @since v0.0.0
 */
public class Tier {
	
	public static final String DEFAULT_TIER_NAME = "New Tier";
	public static final Color DEFAULT_TIER_COLOR = Color.GRAY;
	
	private TierHeader header;
	private ListTierElement elements;
	
	private static long NEXT_ID = 1;
	private final long id;
	
	//---------------------------------- Ctors ----------------------------------//
	
	/**
	 * Constructs a new {@link Tier} object with the given parameters.
	 * 
	 * @param header {@link TierHeader} 
	 * @param elements list to associate
	 * @throws IllegalArgumentException if the header's name is blank
	 */
	public Tier(TierHeader header, ListTierElement elements) {
		Objects.requireNonNull(header); Objects.requireNonNull(elements);
		if (header.name().isBlank()) throw new IllegalArgumentException(
				"TierHeader's name parameter must not be blank");
		
		this.header = header;
		this.elements = elements;
		this.id = NEXT_ID++;
	}	
	
	/**
	 * Constructs a new empty {@link Tier} object with given {@link TierHeader}.
	 * 
	 * @param header {@link TierHeader}
	 * @throws IllegalArgumentException if the header's name is blank
	 */
	public Tier(TierHeader header) { 
		this(header, new ListTierElement());
	}
	
	/**
	 * Constructs a new empty {@link Tier} object with given {@link String}.
	 * 
	 * @param name tier name
	 * @throws IllegalArgumentException if name is blank
	 */
	public Tier(String name) { 
		this(new TierHeader(name, DEFAULT_TIER_COLOR), new ListTierElement());
	}
	
	/**
	 * Constructs a new empty {@link Tier} object
	 * 
	 * the Tier's name will be set to {@link Tier#DEFAULT_TIER_NAME}
	 * the Tier's color will be set to {@link Tier#DEFAULT_TIER_COLOR}
	 */
	public Tier() { 
		this(new TierHeader(DEFAULT_TIER_NAME, DEFAULT_TIER_COLOR));
	}
	
	//---------------------------------- methods  ----------------------------------//
	
	/**
	 * Adds an element to the tier
	 * 
	 * @param e element to add
	 * @return true if successfull
	 * @throws IllegalArgumentException if element is a duplicate
	 */
	public boolean add(TierElement e) throws IllegalArgumentException {
		if (elements.contains(e)) throw new IllegalArgumentException("Tier already contains this element: " + e);
		else return elements.add(e);
	}
	
	/**
	 * Moves an element to a certain index, automatically shifts all the others
	 * 
	 * @param to destination index 
	 * @param e element to move
	 * @return {@link TierElement} previously at the specified location
	 * @throws IndexOutOfBoundsException
	 * @throws ElementNotFoundException
	 */
	public TierElement moveTo(int to, TierElement e) throws IndexOutOfBoundsException, ElementNotFoundException 
	{ return elements.moveTo(to, e); }
	
	public boolean remove(TierElement e) throws ElementNotFoundException {
		if (!elements.remove(e)) throw new ElementNotFoundException();
		else return true;
	}
	
	public TierElement remove(int i) throws IndexOutOfBoundsException { return elements.remove(i); }

	public void swap(TierElement a, TierElement b) throws IndexOutOfBoundsException {
		swap(elements.indexOf(a), elements.indexOf(b));
	}
	
	private void swap(int a, int b) throws IndexOutOfBoundsException { Collections.swap(elements, a, b); }
	
	
	//---------------------------------- setters and getters ----------------------------------//	
	
	public void setName(String name) throws IllegalArgumentException {
		Objects.requireNonNull(name);
		if (name.isBlank()) throw new IllegalArgumentException();
		setHeader(new TierHeader(name, this.header.color()));
	}
	
	public void setColor(Color color) throws IllegalArgumentException {
		Objects.requireNonNull(color); setHeader(new TierHeader(this.header.name(), color)); 
	}
	
	private void setHeader(TierHeader header) { Objects.requireNonNull(header); this.header = header; }
	
	public TierHeader getHeader() { return new TierHeader(this.header.name(), this.header.color()); }
	public ListTierElement getElements() { return new ListTierElement(elements); }


	//---------------------------------- hashCode, equals and toString ----------------------------------//
	
	@Override
	public int hashCode() {
		return Objects.hash(elements, header, id);
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
		return Objects.equals(elements, other.elements) 
				&& Objects.equals(header, other.header)
				&& Objects.equals(id, other.id);
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

