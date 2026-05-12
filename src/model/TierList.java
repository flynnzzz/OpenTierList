package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import enums.TierStringFormat;
import exceptions.ElementNotFoundException;
import exceptions.TierNotFoundException;

/**
 * A class representing the concept of tier list.
 * 
 * Container of a {@link Map} with keys: {@link TierHeader} and values: {@link TierElementList}
 * 
 * @author flynnz
 * @version 1.00
 * @since v0.0.0
 */
public class TierList {

	private String name;
	private TierElementList unranked;
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
	 * The instance will be constructed with a name and the given {@link TierElementList} to rank;
	 * It's initial 'ranked' contents will be set to empty.
	 * 
	 * @param name the tier list's name
	 * @param unranked elements to rank
	 * @throws IllegalArgumentException if name is blank
	 */
	public TierList(String name, TierElementList unranked) {
		Objects.requireNonNull(name); Objects.requireNonNull(unranked);
		if (name.isBlank()) throw new IllegalArgumentException("Tierlist name must not be blank");
		
		this.name = name;
		this.unranked = unranked;
		this.tiers = new ArrayList<>();
	}
	
	/**
	 * Constructs a {@link TierList} instance.
	 * 
	 * The instance will be constructed with the given {@link TierElementList} to rank;
	 * The name will be set to {@link TierList#DEFAULT_TIERLIST_NAME};
	 * It's initial 'ranked' contents will be set to empty
	 * 
	 * @param unranked elements to rank
	 */
	public TierList(TierElementList unranked) {
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
	 * {@link TierElementList} to rank will be set to empty
	 * 
	 */
	public TierList() {
		this.name = DEFAULT_TIERLIST_NAME;
		this.unranked = new TierElementList();
		this.tiers = new ArrayList<>();
	}
	
	/**
	 * Constructs a {@link TierList} instance.
	 * 
	 * The tier list instance will be constructed with the given Lists of headers
	 * and {@link TierElementList}s.
	 * 
	 * @param name the tier list's name
	 * @param unranked elements to rank
	 * @param contents contents to put
	 * @throws IllegalArgumentException if name is blank
	 */
	public TierList(String name, TierElementList unranked, List<Tier> contents) {
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
	//TODO: delete?
	public boolean removeTier(Tier t) throws TierNotFoundException {
		Objects.requireNonNull(t);
		checkTierExistence(t);
		
		return tiers.remove(t);
	}
	
	public Tier removeTier(int i) throws TierNotFoundException  {
		Tier t = tiers.get(i);
		checkTierExistence(t);
		
		return tiers.remove(i);
	}
	
	public boolean addToUnranked(TierElement e) {
		Objects.requireNonNull(e);

		e = e.changeTo(false);
		return unranked.add(e);
	}
	
	public boolean removeFromUnranked(TierElement e) throws ElementNotFoundException {
		Objects.requireNonNull(e);
		checkElementExistence(e, unranked);
		
		e = e.changeTo(true);
		return unranked.remove(e);
	}
	
	public TierElement removeFromUnranked(int i) throws ElementNotFoundException {
		TierElement e = unranked.get(i);
		checkElementExistence(e, unranked);
		
		e = e.changeTo(true);
		return unranked.remove(i);
	}
	
	// TODO: change param Tier to int (index)
	public boolean addToTier(Tier to, TierElement e) throws TierNotFoundException {
		Objects.requireNonNull(e); Objects.requireNonNull(to);
		
		int idx = checkTierExistence(to);
		
		return tiers.get(idx).add(e);
	}
	
	// TODO: change param Tier to int (index)
	public boolean removeFromTier(Tier from, TierElement e) throws TierNotFoundException, ElementNotFoundException{
		Objects.requireNonNull(e); Objects.requireNonNull(from);
		
		int idx = checkTierExistence(from);
		
		if (!tiers.get(idx).remove(e)) throw new ElementNotFoundException();
		return true;
	}
	
	// TODO: change param Tier to int (index)
	public TierElement removeFromTier(Tier from, int i) throws TierNotFoundException, IndexOutOfBoundsException {
		Objects.requireNonNull(from);
		
		int idx = checkTierExistence(from);
		
		return tiers.get(idx).remove(i);
	}
	
	public void swapTiers(int a, int b) throws IndexOutOfBoundsException {
		Collections.swap(tiers, a, b);
	}

	// TODO: change param Tier to int (index)
	public void swapTierElements(Tier t, TierElement a, TierElement b) throws TierNotFoundException, ElementNotFoundException {
		int idx = checkTierExistence(t);
		checkElementExistence(a, t.getElements()); 
		checkElementExistence(b, t.getElements());
		
		tiers.get(idx).swap(a, b);
	}

	public void swapUnrankedElements(TierElement a, TierElement b) throws ElementNotFoundException {
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
	
	private int checkElementExistence(TierElement e, TierElementList ec) throws ElementNotFoundException {
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
	
	public void setTierHeader(int tierIndex, TierHeader th) throws IndexOutOfBoundsException {
		tiers.get(tierIndex).setHeader(th);
	}
	
	public void getTierHeader(int tierIndex) throws IndexOutOfBoundsException {
		tiers.get(tierIndex).getHeader();
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
		var sb = new StringBuilder();
		sb.append(this.name + System.lineSeparator());
		sb.append(System.lineSeparator());
		for (Tier t : tiers) {
			if (!t.getElements().isEmpty()) {
				sb.append(t.toString());
				sb.append(System.lineSeparator());
				sb.append(System.lineSeparator());
			}
		}
		
		sb.append("Unranked:" + System.lineSeparator() + unranked.toString());
		return sb.toString();
	}
	
	public String toString(TierStringFormat format) {
		var sb = new StringBuilder();
		sb.append(this.name + System.lineSeparator());
		sb.append(System.lineSeparator());
		for (Tier t : tiers) {
			if (!t.getElements().isEmpty()) {
				sb.append(t.toString(format));
				sb.append(System.lineSeparator());
				sb.append(System.lineSeparator());
			}
		}
		
		sb.append("Unranked:" + System.lineSeparator() + unranked.toString());
		return sb.toString();
	}
}
