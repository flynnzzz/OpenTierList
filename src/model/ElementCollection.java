package model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Extension of {@link ArrayList}.
 * 
 * @author flynnz
 * @version 1.00
 * @since v0.0.0
 */

public class ElementCollection extends ArrayList<Element> {
	
	private static final long serialVersionUID = -386249859978810016L;

	/**
	 * Constructs a new {@link ElementCollection} object with the given {@link Collection} of {@link Element}
	 * 
	 * @param elements collection to initialize instance
	 */
	public ElementCollection(Collection<Element> elements) {
		super(elements);
	}
	/**
	 * Constructs a new empty {@link ElementCollection} object
	 */
	public ElementCollection() {
		super();
	}
	
	/**
	 * Returns the {@link ElementCollection} as {@link String}
	 * 
	 * Format:
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
		for (Element e : this) {	
			sb.append("\t");
			sb.append(e); 
			if (!this.getLast().equals(e))
				sb.append(",");
			else
				sb.append(".");
			sb.append(System.lineSeparator());
		}
		sb.append("]");
		return sb.toString();
	}
}
