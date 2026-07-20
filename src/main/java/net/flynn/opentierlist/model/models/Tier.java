package net.flynn.opentierlist.model.models;

import net.flynn.opentierlist.model.enums.TierStringFormat;
import net.flynn.opentierlist.model.exceptions.TelementNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javafx.scene.paint.Color;

/**
 * Class representing the concept of a 'Tier'
 * 
 * @author flynnz
 * @version 2.25
 * @since v0.0.0
 */
public class Tier {
	
	public static final String DEFAULT_TIER_NAME = "New Tier";
	public static final Color DEFAULT_TIER_COLOR = Color.GRAY;
	
	private TierHeader header;
	private List<Telement> elements;
	
	private static long NEXT_ID = 1;
	private final long id;
	
	//---------------------------------- Ctors ----------------------------------//
	
	private Tier(TierHeader header, List<Telement> elements) {
		Objects.requireNonNull(header);
		Objects.requireNonNull(elements);
		if (header.name().isBlank()) throw new IllegalArgumentException();
		
		this.header = header;
		this.elements = elements;
		this.id = NEXT_ID++;
	}	
	
	/**
	 * Constructs a new {@link Tier} object with the given parameters.
	 * 
	 * @param name tier name
	 * @param color tier {@link Color}
	 * @param elements list to associate to this tier
	 * @throws IllegalArgumentException if the header's name is blank
	 */	
	public Tier(String name, Color color, List<Telement> elements) { 
		this(new TierHeader(name, color), elements);
	}
	
	/**
	 * Constructs a new empty {@link Tier} object with the given parameters.
	 * 
	 * @param name tier name
	 * @param color tier {@link Color}
	 * @throws IllegalArgumentException if name is blank
	 */
	public Tier(String name, Color color) { 
		this(new TierHeader(name, color), new ArrayList<Telement>());
	}
	
	/**
	 * Constructs a new empty {@link Tier} object with given name.
	 * 
	 * @param name tier name
	 * @throws IllegalArgumentException if name is blank
	 */
	public Tier(String name) { 
		this(new TierHeader(name, DEFAULT_TIER_COLOR), new ArrayList<Telement>());
	}
	
	/**
	 * Constructs a new empty {@link Tier} object
	 */
	public Tier() { 
		this(DEFAULT_TIER_NAME);
	}
	
	//---------------------------------- methods  ----------------------------------//
	
	/**
	 * Adds an element to the tier instance
	 * 
	 * @param element element to add
	 * @return true if successfull
	 */
	public boolean add(Telement element) {
		return elements.add(element);
	}
	
	public boolean remove(Telement element) throws TelementNotFoundException {
		if (!elements.remove(element)) throw new TelementNotFoundException();
		else return true;
	}
	
	public Telement remove(int i) throws TelementNotFoundException {
		try {
			return elements.remove(i); 
		} catch(IndexOutOfBoundsException e) {
			throw new TelementNotFoundException();
		}
		
	}

	public void swap(Telement a, Telement b) throws TelementNotFoundException {
		try {
			swap(elements.indexOf(a), elements.indexOf(b));
		} catch(IndexOutOfBoundsException e) {
			throw new TelementNotFoundException();
		}
		
	}
	
	public void swap(int a, int b) throws TelementNotFoundException { 
		try {
			Collections.swap(elements, a, b); 	
		} catch(IndexOutOfBoundsException e) {
			throw new TelementNotFoundException();
		}
	}
	
	public boolean contains(Telement element) {
		return elements.contains(element);
	}
	
	/**
	 * Moves an element to a certain index, automatically shifting all the others
	 * 
	 * @param to destination index 
	 * @param element element to move
	 * @throws TelementNotFoundException
	 */
	public void moveTo(int to, Telement element) throws TelementNotFoundException {
		if (!elements.contains(element) || to > elements.size()) 
			throw new TelementNotFoundException();

		elements.remove(elements.indexOf(element));
		elements.add(to, element);
	}
	
	public Tier copy() {
		return new Tier(header.name(), header.color());
	}
	
	//---------------------------------- setters and getters ----------------------------------//	
	
	public void setName(String name) throws IllegalArgumentException {
		Objects.requireNonNull(name);
		if (name.isBlank()) 
			throw new IllegalArgumentException();
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
	
	private TierHeader getHeader() { return new TierHeader(this.header.name(), this.header.color()); }
	
	public String getName() { return getHeader().name(); }

	public Color getColor() { return getHeader().color(); }
	
	/**
	 * *Read only*
	 * @return this tier instance's elements
	 */
	public List<Telement> getElements() { return List.copyOf(elements); }


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
	
	private String toStringElements(List<Telement> elements) {
		var sb = new StringBuilder();
		sb.append("[ ");
		for (Telement e : elements) {	
			sb.append(e); 
			if (!elements.getLast().equals(e)) 
				sb.append(", ");
			else
				sb.append(".");
		}
		sb.append(" ]");
		return sb.toString();
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
		return switch (format) {
			case EXTENDED -> toStringExtended();
			default -> toString();
		};
	}
	
	private String toStringCompact() {
		var sb = new StringBuilder();
		sb.append(getHeader().name() + ":" + System.lineSeparator());
		sb.append(toStringElements(getElements()));
		return sb.toString();
	}
	
	private String toStringExtended() {
		var sb = new StringBuilder();
		sb.append(getHeader().name() + ":" + System.lineSeparator());
		sb.append("[");
		sb.append(System.lineSeparator());
		for (Telement e : elements) {	
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

