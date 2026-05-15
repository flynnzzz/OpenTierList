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

public class TierElementList extends ArrayList<TierElement> {
	
	private static final long serialVersionUID = -386249859978810016L;

	/**
	 * Constructs a new {@link TierElementList} object with the given {@link Collection} of {@link TierElement}
	 * 
	 * @param elements collection to initialize instance
	 */
	public TierElementList(Collection<TierElement> elements) {
		super(elements);
	}
	/**
	 * Constructs a new empty {@link TierElementList} object
	 */
	public TierElementList() {
		super();
	}
	
	/**
	 * Returns the {@link TierElementList} as {@link String}
	 * 
	 * Format:
	 * 	[ element1, element2, ...]".
	 * 
	 * @return {@link String}
	 */
	@Override
	public String toString() {
		var sb = new StringBuilder();
		sb.append("[ ");
		for (TierElement e : this) {	
			sb.append(e); 
			if (!this.getLast().equals(e)) 
				sb.append(", ");
			else
				sb.append(".");
		}
		sb.append(" ]");
		return sb.toString();
	}
}
