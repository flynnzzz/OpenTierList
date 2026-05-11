package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import exceptions.ElementNotFoundException;
import exceptions.TierNotFoundException;

/**
 * A class representing the concept of tier list.
 * 
 * Container of a {@link Map} with keys: {@link TierHeader} and values: {@link ElementCollection}
 * 
 * @author flynnz
 * @version 1.00
 * @since v0.0.0
 */
public class TierList {

	private String name;
	private ElementCollection unranked;
	private List<Tier> tiers;

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
	 * The instance will be constructed with a name and the given {@link ElementCollection} to rank;
	 * It's initial 'ranked' contents will be set to empty.
	 * 
	 * @param name the tier list's name
	 * @param unranked elements to rank
	 * @throws IllegalArgumentException if name is blank
	 */
	public TierList(String name, ElementCollection unranked) {
		Objects.requireNonNull(name); Objects.requireNonNull(unranked);
		if (name.isBlank()) throw new IllegalArgumentException("Tierlist name must not be blank");
		
		this.name = name;
		this.unranked = unranked;
		this.tiers = new ArrayList<>();
	}
	
	/**
	 * Constructs a {@link TierList} instance.
	 * 
	 * The instance will be constructed with the given {@link ElementCollection} to rank;
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
		this.tiers = new ArrayList<>();
	}
	
	/**
	 * Constructs an empty {@link TierList} instance.
	 * 
	 * The name will be set to {@link TierList#DEFAULT_TIERLIST_NAME};
	 * It's initial 'ranked' contents will be set to empty;
	 * {@link ElementCollection} to rank will be set to empty
	 * 
	 */
	public TierList() {
		this.name = DEFAULT_TIERLIST_NAME;
		this.unranked = new ElementCollection();
		this.tiers = new ArrayList<>();
	}
	
	/**
	 * Constructs a {@link TierList} instance.
	 * 
	 * The tier list instance will be constructed with the given Lists of headers
	 * and {@link ElementCollection}s.
	 * 
	 * @param name the tier list's name
	 * @param unranked elements to rank
	 * @param contents contents to put
	 * @throws IllegalArgumentException if name is blank
	 */
	public TierList(String name, ElementCollection unranked, List<Tier> contents) {
		Objects.requireNonNull(name); Objects.requireNonNull(unranked); 
		Objects.requireNonNull(contents);	

		if (name.isBlank()) throw new IllegalArgumentException();
		
		this.name = name;
		this.unranked = unranked;
		this.tiers = contents;
	}
	
	/***********************************************************************
	 * 							Class methods
	 **********************************************************************/
	
	/**
	 * Adds a tier to the {@link TierList}.
	 * 
	 * @param t tier to add
	 * @return @see List#add(Object)
	 */
	public boolean addTier(Tier t) {
		Objects.requireNonNull(t);
		return tiers.add(t);
	}
	
	/**
	 * Removes a tier to the {@link TierList}.
	 * 
	 * @param t tier to remove
	 * @return @see List#remove(Object)
	 * @throws TierNotFoundException if @param t is not in the Tierlist
	 */
	public boolean removeTier(Tier t) throws TierNotFoundException {
		Objects.requireNonNull(t);
		checkTierExistence(t);
		
		return tiers.remove(t);
	}
	
	public Tier removeTier(int i) throws IndexOutOfBoundsException {
		return tiers.remove(i);
	}
	
	public boolean addToUnranked(Element e) {
		Objects.requireNonNull(e);

		e = e.changeTo(false);
		return unranked.add(e);
	}
	
	public boolean removeFromUnranked(Element e) throws ElementNotFoundException {
		Objects.requireNonNull(e);
		checkElementExistence(e, unranked);
		
		e.changeTo(true);
		return unranked.remove(e);
	}
	
	public Element removeFromUnranked(int i) throws IndexOutOfBoundsException {
		return unranked.remove(i);
	}
	
	public boolean addToTier(Tier to, Element e) throws TierNotFoundException {
		Objects.requireNonNull(e); Objects.requireNonNull(to);
		
		int idx = checkTierExistence(to);
		
		return tiers.get(idx).add(e);
	}
	
	public boolean removeFromTier(Tier from, Element e) throws TierNotFoundException, ElementNotFoundException{
		Objects.requireNonNull(e); Objects.requireNonNull(from);
		
		int idx = checkTierExistence(from);
		
		if (!tiers.get(idx).remove(e)) throw new ElementNotFoundException();
		return true;
	}
	
	public Element removeFromTier(Tier from, int i) throws TierNotFoundException, IndexOutOfBoundsException {
		Objects.requireNonNull(from);
		
		int idx = checkTierExistence(from);
		
		return tiers.get(idx).remove(i);
	}
	
	public void swapTiers(int a, int b) throws IndexOutOfBoundsException {
		Collections.swap(tiers, a, b);
	}

	public void swapTierElements(Tier t, Element a, Element b) throws TierNotFoundException, ElementNotFoundException {
		int idx = checkTierExistence(t);
		checkElementExistence(a, t.getElements()); 
		checkElementExistence(b, t.getElements());
		
		tiers.get(idx).swap(a, b);
	}

	public void swapUnrankedElements(Element a, Element b) throws ElementNotFoundException {
		int ai = checkElementExistence(a, unranked), bi = checkElementExistence(b, unranked);
		
		Collections.swap(unranked, ai, bi);
	}
	
	/**
	 * Checks the presence of a Tier within the TierList
	 * 
	 * @param t Tier to check
	 * @return index of the Tier in the TierList
	 * @throws TierNotFoundException if no match found
	 */
	private int checkTierExistence(Tier t) throws TierNotFoundException {
		int idx = tiers.indexOf(t);
		if (idx == -1) throw new TierNotFoundException();
		return idx;
	}
	
	private int checkElementExistence(Element e, ElementCollection ec) throws ElementNotFoundException {
		int idx = ec.indexOf(e);
		if (idx == -1) throw new ElementNotFoundException();
		return idx;
	}
	
	
	/***********************************************************************
	 * 						Setters and getters
	 **********************************************************************/

	public void setName(String name) {
		Objects.requireNonNull(name);
		if (name.isBlank()) throw new IllegalArgumentException("Tier list name must not be blank");
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setTierHeader(Tier t, TierHeader th) throws TierNotFoundException {
		int idx = checkTierExistence(t);
		
		tiers.get(idx).setHeader(th);
	}
	
	public void getTierHeader(Tier t) throws TierNotFoundException {
		int idx = checkTierExistence(t);
		
		tiers.get(idx).getHeader();
	}
	
	
	/***********************************************************************
	 * 					hashCode, equals and toString
	 **********************************************************************/
	
	@Override
	public int hashCode() {
		return Objects.hash(tiers, name, unranked);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TierList)) {
			return false;
		}
		TierList other = (TierList) obj;
		return Objects.equals(tiers, other.tiers) && Objects.equals(name, other.name)
				&& Objects.equals(unranked, other.unranked);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.name + System.lineSeparator());
		sb.append(System.lineSeparator());
		for (Tier t : tiers) {
			sb.append(t.toString());
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
		}
		
		sb.append("Unranked:" + System.lineSeparator() + unranked.toString());
		return sb.toString();
	}
}
