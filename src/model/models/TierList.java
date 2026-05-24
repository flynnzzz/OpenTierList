package model.models;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static model.enums.TierElementStatus.*;
import model.enums.TierStringFormat;
import model.exceptions.ElementNotFoundException;
import model.exceptions.TierNotFoundException;

/**
 * A class representing the concept of a tier list
 * 
 * @author flynnz
 * @version 2.50
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
	 * The tier list instance will be constructed with the given lists parameters.
	 * 
	 * @param name the tier list's name
	 * @param unranked elements to rank
	 * @param tiers tiers to associate 
	 * @throws IllegalArgumentException if name is blank
	 */
	public TierList(String name, ListTierElement unranked, List<Tier> tiers) {
		Objects.requireNonNull(name); Objects.requireNonNull(unranked); 
		Objects.requireNonNull(tiers);	
		if (name.isBlank()) throw new IllegalArgumentException();
		
		this.name = name;
		this.unranked = unranked;
		this.tiers = tiers;
	}
	
	/**
	 * Constructs {@link TierList} instance.
	 * 
	 * The tier list instance will be constructed with the given lists parameters.
	 * 
	 * It's initial 'ranked' contents will be set to empty.
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
	 * The tier list instance will be constructed with the given lists parameters.
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
	 * @throws IllegalArgumentException if name is blank
	 */
	public TierList(String name) {
		this(name, new ListTierElement());
	}  
	
	/**
	 * Constructs an empty {@link TierList} instance.
	 */
	public TierList() {
		this(new ListTierElement());
	}
	

	//---------------------------------- methods ----------------------------------//
	
	//------------------------------ ranking ------------------------------//
	
	/**
	 * Ranks a {@link TierElement}
	 * 
	 * @param e element to rank
	 * @param toTierIndex tier to rank to
	 * @return true if successfull
	 * @throws TierNotFoundException
	 * @throws ElementNotFoundException
	 */
	public boolean rank(TierElement e, int toTierIndex) throws TierNotFoundException, ElementNotFoundException {
		this.removeFromUnranked(e);
		return addToTier(toTierIndex, e);
	}
	
	/**
	 * Ranks a {@link TierElement}
	 * 
	 * @param e element to rank
	 * @param toTier tier to rank to
	 * @return true if successfull
	 * @throws TierNotFoundException
	 * @throws ElementNotFoundException
	 */
	public boolean rank(TierElement e, Tier toTier) throws TierNotFoundException, ElementNotFoundException {
		this.removeFromUnranked(e);
		return addToTier(toTier, e);
	}
	
	/**
	 * Unranks a {@link TierElement}
	 *  
	 * @param e element to unrank
	 * @param fromTierIndex tier to unrank from
	 * @return true if successfull
	 * @throws TierNotFoundException
	 * @throws ElementNotFoundException
	 */
	public boolean unrank(TierElement e, int fromTierIndex) throws TierNotFoundException, ElementNotFoundException {
		this.checkElementExistenceInTier(e, fromTierIndex);
		this.addToUnranked(e);
		return removeFromTier(fromTierIndex, e);
	}
	
	/**
	 * Unranks a {@link TierElement}
	 *  
	 * @param e element to unrank
	 * @param fromTier tier to unrank from
	 * @return true if successfull
	 * @throws TierNotFoundException
	 * @throws ElementNotFoundException
	 */
	public boolean unrank(TierElement e, Tier fromTier) throws TierNotFoundException, ElementNotFoundException {
		this.checkElementExistenceInTier(e, fromTier);
		this.addToUnranked(e);
		return removeFromTier(fromTier, e);
	} 
	
	/**
	 * Ranks a {@link TierElement} to a specified position
	 * 
	 * @param e element to rank
	 * @param toTierIndex tier to rank to
	 * @param insertIndex destination index 
	 * @return true if successfull
	 * @throws TierNotFoundException
	 * @throws ElementNotFoundException
	 */
	public boolean rankInsert(int toTierIndex, TierElement e, int insertIndex) throws TierNotFoundException, ElementNotFoundException {
		this.removeFromUnranked(e);
		boolean added = addToTier(toTierIndex, e);
		tiers.get(insertIndex).moveTo(insertIndex, e);
		return added;
	}
	
	/**
	 * Ranks a {@link TierElement} to a specified position
	 * 
	 * @param e element to rank
	 * @param toTier tier to rank to
	 * @param insertIndex destination index 
	 * @return true if successfull
	 * @throws TierNotFoundException
	 * @throws ElementNotFoundException
	 */
	public boolean rankInsert(Tier toTier, TierElement e, int insertIndex) throws TierNotFoundException, ElementNotFoundException {
		this.removeFromUnranked(e);
		boolean added = addToTier(toTier, e);
		toTier.moveTo(insertIndex, e);
		return added;
	}
	
	/**
	 * Unanks a {@link TierElement} to a specified position
	 * 
	 * @param e element to rank
	 * @param fromTierIndex tier to unrank from
	 * @param insertIndex destination index 
	 * @return true if successfull
	 * @throws TierNotFoundException
	 * @throws ElementNotFoundException
	 */
	public boolean unrankInsert(int fromTierIndex, TierElement e, int insertIndex) throws TierNotFoundException, ElementNotFoundException {
		this.checkElementExistenceInTier(e, fromTierIndex);
		this.addToUnranked(e);
		boolean removed = removeFromTier(fromTierIndex, e);
		unranked.moveTo(insertIndex, e);
		return removed;
	}
	
	/**
	 * Unanks a {@link TierElement} to a specified position
	 * 
	 * @param e element to rank
	 * @param fromTier tier to unrank from
	 * @param insertIndex destination index 
	 * @return true if successfull
	 * @throws TierNotFoundException
	 * @throws ElementNotFoundException
	 */
	public boolean unrankInsert(Tier fromTier, TierElement e, int insertIndex) throws TierNotFoundException, ElementNotFoundException {
		this.checkElementExistenceInTier(e, fromTier);
		this.addToUnranked(e);
		boolean removed = removeFromTier(fromTier, e);
		unranked.moveTo(insertIndex, e);
		return removed;
	}

	
	//------------------------------ editing ------------------------------//

	public boolean addTier(Tier t) { Objects.requireNonNull(t); return tiers.add(t); }
	
	/**
	 * Adds an element to the list of unranked elements
	 * @param e element to add
	 */
	public void addToUnranked(TierElement e) { unranked.add(e); e.changeTo(UNRANKED); }
	
	public boolean addToTier(int tierIndex, TierElement e) throws TierNotFoundException {
		this.checkTierExistence(tierIndex);
		return tiers.get(tierIndex).add(e);
	}
	
	public boolean addToTier(Tier tier, TierElement e) throws TierNotFoundException {
		this.checkTierExistence(tier);
		return tier.add(e);
	}
	
	public Tier removeTier(int tierIndex) throws TierNotFoundException  {
		this.checkTierExistence(tierIndex);
		return tiers.remove(tierIndex);
	}

	public boolean removeTier(Tier t) throws TierNotFoundException {
		this.checkTierExistence(tiers.indexOf(t));
		return tiers.remove(t);
	}
	
	/**
	 * Removes an element from the list of unranked elements
	 * @param e element to add
	 */
	public void removeFromUnranked(TierElement e) throws ElementNotFoundException {
		this.checkElementExistence(e, unranked);
		unranked.remove(e);
		e.changeTo(RANKED);
	}
	
	public boolean removeFromTier(int tierIndex, TierElement e) throws TierNotFoundException, ElementNotFoundException {
		this.checkElementExistenceInTier(e, tierIndex);
		if (!tiers.get(tierIndex).remove(e)) 
			throw new ElementNotFoundException();
		else return true;
	}
	
	public boolean removeFromTier(Tier tier, TierElement e) throws TierNotFoundException, ElementNotFoundException {
		this.checkElementExistenceInTier(e, tier);
		if (!tier.remove(e)) 
			throw new ElementNotFoundException();
		else return true;
	}
	
	
	//------------------------------ swapping ------------------------------//
	
	public void swapTiers(int a, int b) throws TierNotFoundException { 
		try { Collections.swap(tiers, a, b); }
		catch (IndexOutOfBoundsException e) { throw new TierNotFoundException(); }
	}
	
	public void swapTiers(Tier a, Tier b) throws TierNotFoundException { 
		try { Collections.swap(tiers, tiers.indexOf(a), tiers.indexOf(b)); }
		catch (IndexOutOfBoundsException e) { throw new TierNotFoundException(); }
	}

	/**
	 * Swaps two elements from within a tier
	 * 
	 * @param tierIndex destination index
	 * @param a first element
	 * @param b second element
	 * @throws TierNotFoundException
	 * @throws ElementNotFoundException
	 */
	public void swapElements(int tierIndex, TierElement a, TierElement b) throws TierNotFoundException, ElementNotFoundException {
		this.checkElementExistenceInTier(a, tierIndex); 
		this.checkElementExistenceInTier(b, tierIndex);
		Tier t = tiers.get(tierIndex);
		t.swap(a, b);
	}
	
	/**
	 * Swaps two elements from within a tier
	 * 
	 * @param tier destination
	 * @param a first element
	 * @param b second element
	 * @throws TierNotFoundException
	 * @throws ElementNotFoundException
	 */
	public void swapElements(Tier tier, TierElement a, TierElement b) throws TierNotFoundException, ElementNotFoundException {
		this.checkElementExistenceInTier(a, tier); 
		this.checkElementExistenceInTier(b, tier);
		tier.swap(a, b);
	}
	
	/**
	 * Swaps two elements from the unranked list
	 * 
	 * @param a first element
	 * @param b second element
	 * @throws ElementNotFoundException
	 */
	public void swapUnranked(TierElement a, TierElement b) throws ElementNotFoundException {
		int ai = checkElementExistence(a, unranked), bi = checkElementExistence(b, unranked);
		Collections.swap(unranked, ai, bi);
	}
	
	//------------------------------ utils ------------------------------//

	/**
	 * Returns the index of a tier
	 * 
	 * @param t tier to search the index for
	 * @return the tier's index
	 * @throws TierNotFoundException
	 */
	public int indexOf(Tier t) throws TierNotFoundException { return checkTierExistence(tiers.indexOf(t)); }
	
	public int size() { return this.tiers.size(); }

	
	//------------------------------ moving ------------------------------//
	
	/**
	 * Moves elements between tiers
	 * 
	 * @param toTierIndex tier destination index
	 * @param e element to move
	 * @return true if successfull
	 * @throws ElementNotFoundException
	 * @throws TierNotFoundException
	 */
	public boolean moveFromTierToTier(int toTierIndex, TierElement e) throws ElementNotFoundException, TierNotFoundException {
		this.checkTierExistence(toTierIndex);
		int fromTierIndex = findElementTier(e);
		if (tiers.get(fromTierIndex).remove(e)) 
			return tiers.get(toTierIndex).add(e);
		else return false;
	}
	
	/**
	 * Moves elements between tiers
	 * 
	 * @param toTier tier destination index
	 * @param e element to move
	 * @return true if successfull
	 * @throws ElementNotFoundException
	 * @throws TierNotFoundException
	 */
	public boolean moveFromTierToTier(Tier toTier, TierElement e) throws ElementNotFoundException, TierNotFoundException {
		this.checkTierExistence(toTier);
		if (toTier.remove(e)) 
			return toTier.add(e);
		else return false;
	}

	/**
	 * Moves elements between tiers to a specified position
	 * 
	 * @param toTierIndex tier destination index
	 * @param e element to move
	 * @param toElementIndex destination position index
	 * @return true if successfull
	 * @throws ElementNotFoundException
	 * @throws TierNotFoundException
	 */
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
	
	/**
	 * Moves elements between tiers to a specified position
	 * 
	 * @param toTier tier destination index
	 * @param e element to move
	 * @param toElementIndex destination position index
	 * @return true if successfull
	 * @throws ElementNotFoundException
	 * @throws TierNotFoundException
	 */
	public boolean moveFromTierToTier(Tier toTier, TierElement e, int toElementIndex) throws TierNotFoundException, IndexOutOfBoundsException {
		this.checkTierExistence(toTier);
		if (toTier.remove(e)) {
			toTier.add(e);
			toTier.moveTo(toElementIndex, e);
			return true;
		}
		else return false;
	}
	
	public TierElement moveUnranked(TierElement e, int toElementIndex) throws TierNotFoundException, IndexOutOfBoundsException {
		this.checkElementExistence(e, unranked);
		return unranked.moveTo(toElementIndex, e);
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
	
	private int checkTierExistence(Tier tier) throws TierNotFoundException {
		var exception = new TierNotFoundException("Tier \"" + tier + "\" not found");
		int tierIndex = tiers.indexOf(tier);
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
	
	private void checkElementExistenceInTier(TierElement e, Tier tier) throws ElementNotFoundException, TierNotFoundException {
		this.checkTierExistence(tier);
		this.checkElementExistence(e, tier.getElements());
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
	public List<TierElement> getUnranked() { return List.copyOf(unranked); };
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
