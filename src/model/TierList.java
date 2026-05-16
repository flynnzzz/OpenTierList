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
 * @version 1.42
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
	
	public boolean rank(TierElementUnranked e, int tierIndex) throws IllegalArgumentException {
		if (addToTier(tierIndex, e) && removeFromUnranked(e)) return true;
		else throw new IllegalArgumentException();
	}
	
	public boolean unrank(TierElementRanked e, int tierIndex) throws IllegalArgumentException {
		checkElementExistenceInTier(e, tierIndex);
		if (removeFromTier(tierIndex, e) && addToUnranked(e)) return true;
		else throw new IllegalArgumentException();
	}
	
	public boolean addTier(Tier t) { Objects.requireNonNull(t); return tiers.add(t); }
	
	public boolean addToUnranked(TierElement e) { e = e.changeTo(UNRANKED); return unranked.add(e); }
	
	public boolean addToTier(int tierIndex, TierElement e) throws TierNotFoundException {
		checkTierExistence(tiers.get(tierIndex));
		return tiers.get(tierIndex).add(e);
	}
	
	public boolean removeTier(Tier t) throws TierNotFoundException {
		checkTierExistence(t);
		return tiers.remove(t);
	}
	
	public Tier removeTier(int tierIndex) throws TierNotFoundException  {
		checkTierExistence(tiers.get(tierIndex));
		return tiers.remove(tierIndex);
	}
	
	public boolean removeFromUnranked(TierElement e) throws ElementNotFoundException {
		checkElementExistence(e, unranked);
		e = e.changeTo(RANKED);
		return unranked.remove(e);
	}
	
	public boolean removeFromTier(int tierIndex, TierElement e) throws TierNotFoundException,
																	   ElementNotFoundException
	{
		checkTierExistence(tiers.get(tierIndex));
		if (!tiers.get(tierIndex).remove(e)) throw new ElementNotFoundException();
		return true;
	}
	
	public void swapTiers(int a, int b) throws IndexOutOfBoundsException { Collections.swap(tiers, a, b); }

	public void swapTierElements(int tierIndex, TierElement a, TierElement b) 
												throws TierNotFoundException, ElementNotFoundException 
	{
		Tier t = tiers.get(tierIndex);
		checkElementExistenceInTier(a, tierIndex); checkElementExistenceInTier(b, tierIndex);
		
		t.swap(a, b);
	}

	public void swapUnrankedElements(TierElement a, TierElement b) throws ElementNotFoundException {
		int ai = checkElementExistence(a, unranked), bi = checkElementExistence(b, unranked);
		Collections.swap(unranked, ai, bi);
	}
	
	public int indexOf(Tier t) { return checkTierExistence(t); }
	
	public int size() { return this.tiers.size(); }

	public boolean moveToTier(int tierIndex, TierElement e) throws ElementNotFoundException, 
																   TierNotFoundException
	{
		checkTierExistence(tiers.get(tierIndex));
		int originalTierIndex = findElement(e);
		if (tiers.get(originalTierIndex ).remove(e)) return tiers.get(tierIndex).add(e);
		else return false;
	}
	
	private int checkTierExistence(Tier t) throws TierNotFoundException {
		Objects.requireNonNull(t);
		int idx = tiers.indexOf(t);
		if (idx == -1) throw new TierNotFoundException();
		return idx;
	}
	
	private int checkElementExistence(TierElement e, ListTierElement ec) throws ElementNotFoundException {
		Objects.requireNonNull(e); Objects.requireNonNull(ec);
		int idx = ec.indexOf(e);
		if (idx == -1) throw new ElementNotFoundException();
		return idx;
	}
	
	private void checkElementExistenceInTier(TierElement e, int tierIndex) throws ElementNotFoundException,
																				  TierNotFoundException 
	{
		checkTierExistence(tiers.get(tierIndex));
		checkElementExistence(e, tiers.get(tierIndex).getElements());
	}

	private int findElement(TierElement e) throws ElementNotFoundException {
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
		if (name.isBlank()) throw new IllegalArgumentException("Tier list's name must not be blank");
		this.name = name;
	}
	
	public void setTierName(int tierIndex, String name) { 
		setTierHeader(tierIndex, new TierHeader(name, tiers.get(tierIndex).getHeader().color()));
	}
	
	public void setTierColor(int tierIndex, Color color) { 
		setTierHeader(tierIndex, new TierHeader(tiers.get(tierIndex).getHeader().name(), color));
	}
	
	private void setTierHeader(int tierIndex, TierHeader th) throws IndexOutOfBoundsException {
		tiers.get(tierIndex).setName(th.name());
		tiers.get(tierIndex).setColor(th.color());
	}
	public String getTierListName() { return name; }
	public String getTierName(int tierIndex) { return getTierHeader(tierIndex).name(); }
	public String getTierColor(int tierIndex) { return getTierHeader(tierIndex).name(); }
	private TierHeader getTierHeader(int tierIndex) { return tiers.get(tierIndex).getHeader(); }
	public ListTierElement getUnranked() { return new ListTierElement(unranked); };
	
	
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
