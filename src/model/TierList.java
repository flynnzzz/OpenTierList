package model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static model.enums.TierElementStatus.*;
import model.enums.TierStringFormat;
import model.exceptions.ElementNotFoundException;
import model.exceptions.TierNotFoundException;

/**
 * A class representing the concept of tier list.
 * 
 * @author flynnz
 * @version 1.80
 * @since v0.0.0
 */
public class TierList {

	private String name;
	private ListTierElement unranked;
	private List<Tier> tiers;

	public static final String DEFAULT_TIERLIST_NAME = "New Tierlist";
	
	//---------------------------------- Ctors ----------------------------------//
	
	/**
	 * Constructs a {@link TierList} instance.
	 * 
	 * The tier list instance will be constructed with the given Lists of headers
	 * and {@link ListTierElement}s.
	 * 
	 * @param name the tier list's name
	 * @param unranked elements to rank
	 * @param contents contents to put
	 * @throws IllegalArgumentException if name is blank
	 */
	public TierList(String name, ListTierElement unranked, List<Tier> contents) {
		Objects.requireNonNull(name); Objects.requireNonNull(unranked); 
		Objects.requireNonNull(contents);	
		if (name.isBlank()) throw new IllegalArgumentException();
		
		this.name = name;
		this.unranked = unranked;
		this.tiers = contents;
	}
	
	/**
	 * Constructs {@link TierList} instance.
	 * 
	 * The instance will be constructed with a name and the given {@link ListTierElement} to rank;
	 * It's initial 'ranked' contents will be set to empty.
	 * 
	 * @param name the tier list's name
	 * @param unranked elements to rank
	 * @throws IllegalArgumentException if name is blank
	 */
	public TierList(String name, ListTierElement unranked) {
		this(name, unranked, new ArrayList<>());
	}
	
	/**
	 * Constructs a {@link TierList} instance.
	 * 
	 * The instance will be constructed with the given {@link ListTierElement} to rank;
	 * The name will be set to {@link TierList#DEFAULT_TIERLIST_NAME};
	 * It's initial 'ranked' contents will be set to empty
	 * 
	 * @param unranked elements to rank
	 */
	public TierList(ListTierElement unranked) {
		this(DEFAULT_TIERLIST_NAME, unranked);
	}
	
	/**
	 * Constructs a {@link TierList} instance.
	 * 
	 * It's initial 'ranked' contents will be set to empty
	 * {@link ListTierElement} to rank will be set to empty
	 * 
	 * @param name name of the tier list
	 */
	public TierList(String name) {
		this(name, new ListTierElement());
	}
	
	/**
	 * Constructs an empty {@link TierList} instance.
	 * 
	 * The name will be set to {@link TierList#DEFAULT_TIERLIST_NAME};
	 * It's initial 'ranked' contents will be set to empty;
	 * {@link ListTierElement} to rank will be set to empty
	 * 
	 */
	public TierList() {
		this(new ListTierElement());
	}
	

	//---------------------------------- methods ----------------------------------//
	
	//------------------------------ ranking ------------------------------//
	
	
	public boolean rank(TierElement e, int tierIndex) throws TierNotFoundException, ElementNotFoundException {
		this.removeFromUnranked(e);
		return addToTier(tierIndex, e);
	}
	
	public boolean unrank(TierElement e, int tierIndex) throws TierNotFoundException, ElementNotFoundException {
		this.checkElementExistenceInTier(e, tierIndex);
		this.addToUnranked(e);
		return removeFromTier(tierIndex, e);
	} 
	
	public boolean rankInsert(int tierIndex, TierElement e, int insertIndex) throws ElementNotFoundException, TierNotFoundException {
		this.removeFromUnranked(e);
		boolean added = addToTier(tierIndex, e);
		tiers.get(insertIndex).moveTo(insertIndex, e);
		return added;
	}
	
	public boolean unrankInsert(int tierIndex, TierElement e, int insertIndex) throws ElementNotFoundException, TierNotFoundException {
		this.checkElementExistenceInTier(e, tierIndex);
		this.addToUnranked(e);
		boolean removed = removeFromTier(tierIndex, e);
		unranked.moveTo(insertIndex, e);
		return removed;
	}

	
	//------------------------------ editing ------------------------------//

	public boolean addTier(Tier t) { Objects.requireNonNull(t); return tiers.add(t); }
	
	/**
	 * Adds element to the list of unranked elements
	 * @param e element to add
	 * @return e with updated status
	 */
	public void addToUnranked(TierElement e) { unranked.add(e); e.changeTo(UNRANKED); }
	
	public boolean addToTier(int tierIndex, TierElement e) throws TierNotFoundException {
		this.checkTierExistence(tierIndex);
		return tiers.get(tierIndex).add(e);
	}
	
	public boolean removeTier(Tier t) throws TierNotFoundException {
		this.checkTierExistence(tiers.indexOf(t));
		return tiers.remove(t);
	}
	
	public Tier removeTier(int tierIndex) throws TierNotFoundException  {
		this.checkTierExistence(tierIndex);
		return tiers.remove(tierIndex);
	}
	
	public void removeFromUnranked(TierElement e) throws ElementNotFoundException {
		this.checkElementExistence(e, unranked);
		unranked.remove(e);
		e.changeTo(RANKED);
	}
	
	public boolean removeFromTier(int tierIndex, TierElement e) throws TierNotFoundException, ElementNotFoundException {
		this.checkElementExistenceInTier(e, tierIndex);
		if (!tiers.get(tierIndex).remove(e)) 
			throw new ElementNotFoundException();
		return true;
	}
	
	
	//------------------------------ swapping ------------------------------//
	
	public void swapTiers(int a, int b) throws TierNotFoundException { 
		try { Collections.swap(tiers, a, b); }
		catch (IndexOutOfBoundsException e) { throw new TierNotFoundException(); }
	}

	public void swapElements(int tierIndex, TierElement a, TierElement b) throws TierNotFoundException, ElementNotFoundException {
		this.checkElementExistenceInTier(a, tierIndex); 
		this.checkElementExistenceInTier(b, tierIndex);
		Tier t = tiers.get(tierIndex);
		t.swap(a, b);
	}

	public void swapUnranked(TierElement a, TierElement b) throws ElementNotFoundException {
		int ai = checkElementExistence(a, unranked), bi = checkElementExistence(b, unranked);
		Collections.swap(unranked, ai, bi);
	}
	
	//------------------------------ utils ------------------------------//
	
	public int indexOf(Tier t) throws TierNotFoundException { return checkTierExistence(tiers.indexOf(t)); }
	
	public int size() { return this.tiers.size(); }

	//------------------------------ moving ------------------------------//
	
	public boolean moveFromTierToTier(int toTierIndex, TierElement e) throws ElementNotFoundException, TierNotFoundException {
		this.checkTierExistence(toTierIndex);
		int fromTierIndex = findElementTier(e);
		if (tiers.get(fromTierIndex).remove(e)) 
			return tiers.get(toTierIndex).add(e);
		else return false;
	}

	public boolean moveFromTierToTier(int toTierIndex, TierElement e, int toElementIndex) throws TierNotFoundException, IndexOutOfBoundsException {
		this.checkTierExistence(toTierIndex);
		int fromTierIndex = findElementTier(e);
		if (tiers.get(fromTierIndex).remove(e)) {
			tiers.get(toTierIndex).add(e);
			tiers.get(toTierIndex).moveTo(toElementIndex, e);
			return true;
		}
		else return false;
	}
	
	
	//------------------------------ exceptions ------------------------------//
	
	private int checkTierExistence(int tierIndex) throws TierNotFoundException {
		var exception = new TierNotFoundException("Tier at index \"" + tierIndex + "\" not found");
		try {
			if (tierIndex == -1) 
				throw exception;
			else return tierIndex;
		} catch (IndexOutOfBoundsException indexException) { throw exception; }
	}
	
	private int checkElementExistence(TierElement e, ListTierElement ec) throws ElementNotFoundException {
		var exception = new ElementNotFoundException("Element \"" + e + "\" not found in list \"" + ec + "\"");
		try {
			Objects.requireNonNull(e); 
			Objects.requireNonNull(ec);
			int elementIndex = ec.indexOf(e);
			if (elementIndex == -1) 
				throw exception; 
			return elementIndex;
		} catch (NullPointerException | IndexOutOfBoundsException physException ) { 
			throw exception; 
		}
	}
	
	private void checkElementExistenceInTier(TierElement e, int tierIndex) throws ElementNotFoundException, TierNotFoundException {
		this.checkTierExistence(tierIndex);
		this.checkElementExistence(e, tiers.get(tierIndex).getElements());
	}

	private int findElementTier(TierElement e) throws ElementNotFoundException {
		for (Tier tier : tiers) {
			for (TierElement element : tier.getElements()) {
				if (e.equals(element)) return tiers.indexOf(tier);
			}
		}
		throw new ElementNotFoundException();
	}

	
	//---------------------------------- setters and getters ----------------------------------//

	public void setTierListName(String name) throws IllegalArgumentException {
		Objects.requireNonNull(name);
		if (name.isBlank()) 
			throw new IllegalArgumentException("Tier list's name must not be blank");
		this.name = name;
	}
	
	public void setTierName(int tierIndex, String name) {
		var oldColor = tiers.get(tierIndex).getHeader().color();
		setTierHeader(tierIndex, new TierHeader(name, oldColor));
	}
	
	public void setTierColor(int tierIndex, Color color) { 
		var oldName = tiers.get(tierIndex).getHeader().name();
		setTierHeader(tierIndex, new TierHeader(oldName, color));
	}
	
	private void setTierHeader(int tierIndex, TierHeader th) throws TierNotFoundException {
		Tier t = tiers.get(tierIndex);
		t.setName(th.name()); t.setColor(th.color());
	}
	public String getTierListName() { return name; }
	public String getTierName(int tierIndex) { return getTierHeader(tierIndex).name(); }
	public String getTierColor(int tierIndex) { return getTierHeader(tierIndex).name(); }
	private TierHeader getTierHeader(int tierIndex) { return tiers.get(tierIndex).getHeader(); }
	public ListTierElement getUnranked() { return new ListTierElement(unranked); };
	public List<Tier> getTiers() { return List.copyOf(tiers); }
	
	
	//---------------------------------- hashCode, equals and toString ----------------------------------//
	
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
		return this.toString(TierStringFormat.COMPACT);
	}
	
	public String toString(TierStringFormat format) {
		var sb = new StringBuilder();
		sb.append(this.name + System.lineSeparator());
		sb.append(System.lineSeparator());
		for (Tier t : tiers) {
			sb.append(t.toString(format));
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
		}
		
		sb.append("Unranked:" + System.lineSeparator() + unranked.toString());
		return sb.toString();
	}
}
