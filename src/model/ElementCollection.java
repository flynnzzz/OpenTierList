package model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Aggregation of {@link Element} instances, is {@link Iterable}.
 * 
 * Wrapper class for a {@link List} of {@link Element} ,
 * it's provided with wrappers for {@link List} methods and additional useful functions. 
 * It's main purpose is to be inherited by {@link Tier}
 * 
 * @author flynnz
 * @version 0.00
 * @since v0.0.0
 */

public class ElementCollection implements Iterable<Element>{
	
	private List<Element> elements;
	
	
	/***********************************************************************
	 * 							Constructors
	 **********************************************************************/
	
	/**
	 * Constructs a new {@link ElementCollection} object with the given list of {@link Element}
	 * 
	 * @param elements the list to encapsulate, must not be null or empty
	 * @throws NullPointerException if elements is null
	 * @throws IllegalArgumentException if elements is empty
	 */
	public ElementCollection(List<Element> elements) {
		if (elements == null) throw new NullPointerException(
				"The passed parameter must not be null");
		if (!(elements instanceof List<Element>)) throw new IllegalArgumentException(
				"The passed parameter must not be an instance of List<Element>");
		if (elements.isEmpty()) throw new IllegalArgumentException(
				"The passed list of elements must not be empty");
		
		this.elements = elements;
	}
	/**
	 * Constructs a new empty {@link ElementCollection} object
	 */
	public ElementCollection() {
		this.elements = new LinkedList<Element>();
	}
	
	
	/***********************************************************************
	 * 							Class methods
	 **********************************************************************/

	/**
	 * Swaps two given elements inside the collection
	 * 
	 * @param a fist element, must not be null
	 * @param b second element, must not be null
	 * @return true if the swap was successfull
	 */
	public boolean swap(Element a, Element b) {
		if (a == null) throw new NullPointerException("the parameter 'a' must not be null");
		if (b == null) throw new NullPointerException("the parameter 'b' must not be null");
		
		int idxA = elements.indexOf(a), idxB = elements.indexOf(b);
		if (idxA == -1 || idxB == -1)
			return false;
		elements.set(idxA, b);
		elements.set(idxB, a);
		return true;
	}
	
	/**
	 * Adds an {@link Element} to the collection.
	 * 
	 * @param e {@link Element} to add
	 * @return true if addition was successful
	 * @see List#add(Object)
	 */
	public boolean addElement(Element e) { 
		if (e == null) throw new NullPointerException();
		return elements.add(e); 
	}
	
	/**
	 * Removes an {@link Element} from the collection.
	 *
	 * @param e {@link Element} to remove
	 * @return true if removal was successful
	 * @see List#remove(Object)
	 */
	public boolean removeElement(Element e) { 
		if (e == null) throw new NullPointerException();
		return elements.remove(e); 
	}
	
	/**
	 * Returns the collection's size.
	 * 
	 * @return the collection's size
	 * @see List#size()
	 */
	public int size() { return elements.size(); }
	
	/**
	 * Returns the index of the given element.
	 *
	 * @param e {@link Element} to find index for
	 * @return index
	 * @see List#size()
	 */
	public int indexOf(Element e) {
		if (e == null) throw new NullPointerException();
		return elements.indexOf(e); 
	}
	
	/**
	 * Returns {@link Element} given the index.
	 * 
	 * @param idx index of {@link Element} to get
	 * @return {@link Element} to get
	 * @see List#get(int)
	 */
	public Element get(int idx) { return elements.get(idx); }
	
	/**
	 * Returns the copy of the {@link ElementCollection}
	 * 
	 * @return a copy of the current instance
	 */
	public ElementCollection clone() { return new ElementCollection(elements); }

	/**
	 * iterator function.
	 * 
	 * @see List#iterator()
	 */
	@Override
	public Iterator<Element> iterator() {
		return elements.iterator();
	}
	
	/***********************************************************************
	 * 					hashCode, equals and toString
	 **********************************************************************/
	
	/**
	 * hashCode function.
	 * 
	 * @see Objects#hashCode()
	 */
	@Override
	public int hashCode() { return Objects.hash(elements); }

	/**
	 * equals function.
	 * 
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof ElementCollection other)) { return false; }
		return Objects.equals(elements, other.elements);
	}

	/**
	 * Returns the {@link ElementCollection} as {@link String}
	 * 
	 * Format:
	 * 	"Elements: 
	 * 	[
	 * 		element1,
	 * 		element2,
	 * 		...		
	 * 	]".
	 * 
	 * @return {@link String}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(System.lineSeparator());
		for (Element e : elements) {	
			sb.append("\t");
			sb.append(e); 
			sb.append(",");
			sb.append(System.lineSeparator());
		}
		sb.append("]");
		return sb.toString();
	}
}
