package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class representing the concept of tier list.
 * 
 * Container of a {@link Map} with keys: {@link TierHeader} and values: {@link ElementCollection}
 * 
 * @author flynnz
 * @version 0.00
 * @since v0.0.0
 */
public class TierList {

	private String name;
	private ElementCollection unranked;
	private Map<TierHeader, ElementCollection> contents;

	/**
	 * The default name is set to {@link String} "New Tierlist"
	 */
	public static final String DEFAULT_TIERLIST_NAME = "New Tierlist";
	
	/***********************************************************************
	 * 							Constructors
	 **********************************************************************/
	
	/**
	 * Constructs {@link TierList} instance.
	 * 
	 * The instance will be constructed with a name and the given Elements to rank;
	 * It's initial 'ranked' contents will be set to empty.
	 * 
	 * @param name the tier list's name
	 * @param unranked elements to rank
	 * @throws IllegalArgumentException if name is blank
	 */
	public TierList(String name, ElementCollection unranked) {
		if (name == null) throw new NullPointerException();
		if (unranked == null) throw new NullPointerException();
		if (name.isBlank()) throw new IllegalArgumentException();
		
		this.name = name;
		this.unranked = unranked;
		this.contents = new HashMap<>();
	}
	
	/**
	 * Constructs a {@link TierList} instance.
	 * 
	 * The instance will be constructed with the given Elements to rank;
	 * The name will be set to {@link TierList#DEFAULT_TIERLIST_NAME};
	 * It's initial 'ranked' contents will be set to empty
	 * 
	 * @param unranked elements to rank
	 */
	public TierList(ElementCollection unranked) {
		if (name == null) throw new NullPointerException();
		if (unranked == null) throw new NullPointerException();
		if (name.isBlank()) throw new IllegalArgumentException();
		
		this.name = DEFAULT_TIERLIST_NAME;
		this.unranked = unranked;
		this.contents = new HashMap<>();
	}
	
	/**
	 * Constructs an empty {@link TierList} instance.
	 * 
	 * The name will be set to {@link TierList#DEFAULT_TIERLIST_NAME};
	 * It's initial contents will be set to empty;
	 * Elements to rank will be set to empty
	 * 
	 */
	public TierList() {
		if (name == null) throw new NullPointerException();
		if (unranked == null) throw new NullPointerException();
		if (name.isBlank()) throw new IllegalArgumentException();
		
		this.name = DEFAULT_TIERLIST_NAME;
		this.unranked = new ElementCollection();
		this.contents = new HashMap<>();
	}
	
	/**
	 * Constructs a {@link TierList} instance.
	 * 
	 * The tier list instance will be constructed with the given Lists of headers
	 * and {@link ElementCollection}s.
	 * 
	 * @param name the tier list's name
	 * @param unranked elements to rank
	 * @param headers {@link List} of {@link TierHeader}
	 * @param tierListRows {@link List} of {@link ElementCollection} representing the tier list's rows
	 * @throws IllegalArgumentException if name is blank
	 * @throws IllegalArgumentException if the size of the headers list is smaller than rows list's
	 */
	public TierList(String name, ElementCollection unranked, 
			List<TierHeader> headers, List<ElementCollection> tierListRows) {
		if (name == null) throw new NullPointerException();
		if (unranked == null) throw new NullPointerException();
		if (headers == null) throw new NullPointerException();
		if (tierListRows == null) throw new NullPointerException();
		
		if (name.isBlank()) throw new IllegalArgumentException();
		if (headers.size() < tierListRows.size()) throw new IllegalArgumentException(
				"the size of 'headers' must be bigger than the size of 'tierListRows");
		
		this.name = name;
		this.unranked = unranked;
		this.contents = new HashMap<>();
		
		int i = 0; ElementCollection ec;
		for (TierHeader th : headers) {
			ec = tierListRows.get(i);				
			contents.put(th, ec);
			i++;
		}
	}
	
	
	/***********************************************************************
	 * 						Setters and getters
	 **********************************************************************/

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<TierHeader, ElementCollection> getContents() {
		return contents;
	}
	public void setContents(Map<TierHeader, ElementCollection> contents) {
		this.contents = contents;
	}
	public ElementCollection getUnranked() {
		return unranked;
	}
	public void setUnranked(ElementCollection unranked) {
		this.unranked = unranked;
	}

}
