package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
		Objects.requireNonNull(name); Objects.requireNonNull(unranked);
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
		Objects.requireNonNull(unranked);
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
		Objects.requireNonNull(name); Objects.requireNonNull(unranked); 
		Objects.requireNonNull(headers); Objects.requireNonNull(tierListRows);
		
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
	 * 							Class methods
	 **********************************************************************/
	
	/**
	 * Adds a tier to the {@link TierList}.
	 * 
	 * @param t tier to add
	 * @return @see Map#put(Object, Object)
	 */
	public ElementCollection addTier(Tier t) {
		Objects.requireNonNull(t);
		return contents.put(t.getHeader(), t);
	}
	
	/**
	 * Removes a tier to the {@link TierList}.
	 * 
	 * @param t tier to remove
	 * @return @see Map#remove(Object)
	 */
	public ElementCollection removeTier(Tier t) {
		Objects.requireNonNull(t);
		return contents.remove(t.getHeader());
	}
	
	public boolean addUnranked(Element e) {
		Objects.requireNonNull(e);
		return unranked.addElement(e);
	}
	
	public boolean addTo(Element e, TierHeader to) {
		Objects.requireNonNull(e); Objects.requireNonNull(to);
		if (contents.containsKey(to)) {
			contents.get(to).addElement(e);
			return true;
		}
		return false;
	}
	
	public boolean removeFrom(Element e, TierHeader from) {
		Objects.requireNonNull(e); Objects.requireNonNull(from);
		if (contents.containsKey(from)) {
			contents.get(from).removeElement(e);
			return true;
		}
		return false;
	}
	
	/***********************************************************************
	 * 						Setters and getters
	 **********************************************************************/

	public String getName() {
		return name;
	}
	public void setName(String name) {
		Objects.requireNonNull(name);
		if (name.isBlank()) throw new IllegalArgumentException("Tier list name must not be blank");
		this.name = name;
	}
	public Map<TierHeader, ElementCollection> getContents() {
		return contents;
	}
	public void setContents(Map<TierHeader, ElementCollection> contents) {
		Objects.requireNonNull(contents);
		this.contents = contents;
	}
	public ElementCollection getUnranked() {
		return unranked;
	}
	public void setUnranked(ElementCollection unranked) {
		Objects.requireNonNull(unranked);
		this.unranked = unranked;
	}

}
