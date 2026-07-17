package opentierlist.model.models;

import javafx.scene.paint.Color;

import static opentierlist.model.enums.TelementStatus.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import opentierlist.model.enums.TierStringFormat;
import opentierlist.model.exceptions.TelementNotFoundException;
import opentierlist.model.exceptions.TierNotFoundException;

/**
 * A class representing the concept of a tier list
 * 
 * @author flynnz
 * @version 2.50
 * @since v0.0.0
 */
public class TierList {

	private String name;
	private List<Telement> unranked;
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
	 * @param tiers preset tiers
	 * @throws IllegalArgumentException if name is blank
	 */
	public TierList(String name, List<Telement> unranked, List<Tier> tiers) throws IllegalArgumentException {
		Objects.requireNonNull(name); 
		Objects.requireNonNull(unranked); 
		Objects.requireNonNull(tiers);	
		if (name.isBlank()) 
			throw new IllegalArgumentException();
		
		this.name = name;
		this.unranked = unranked;
		this.tiers = tiers;
	}
	
	/**
	 * Constructs {@link TierList} instance.
	 * 
	 * The tier list instance will be constructed with the given lists parameters.
	 * 
	 * @param name the tier list's name
	 * @param unranked elements to rank
	 * @throws IllegalArgumentException if name is blank
	 */
	public TierList(String name, List<Telement> unranked) throws IllegalArgumentException {
		this(name, unranked, new ArrayList<>());
	}
	
	/**
	 * Constructs a {@link TierList} instance.
	 * 
	 * The tier list instance will be constructed with the given elements to rank.
	 * 
	 * @param unranked elements to rank
	 */
	public TierList(List<Telement> unranked) {
		this(DEFAULT_TIERLIST_NAME, unranked);
	}
	
	/**
	 * Constructs a {@link TierList} instance.
	 * 
	 * The tier list instance will be constructed with the given name.
	 * 
	 * @param name name of the tier list
	 * @throws IllegalArgumentException if name is blank
	 */
	public TierList(String name) throws IllegalArgumentException {
		this(name, new ArrayList<Telement>());
	}  
	
	/**
	 * Constructs an empty {@link TierList} instance.
	 */
	public TierList() {
		this(new ArrayList<Telement>());
	}
	

	//---------------------------------- methods ----------------------------------//
	
	//------------------------------ ranking ------------------------------//
	
	/**
	 * Ranks a {@link Telement}
	 * 
	 * @param element element to rank
	 * @param toTierIndex tier to rank to
	 * @return true if successfull
	 * @throws TierNotFoundException
	 * @throws TelementNotFoundException
	 */
	public boolean rank(Telement element, int toTierIndex) throws TierNotFoundException, TelementNotFoundException {
		removeFromUnranked(element);
		return addToTier(toTierIndex, element);
	}
	
	/**
	 * Ranks a {@link Telement}
	 * 
	 * @param element element to rank
	 * @param toTier tier to rank to
	 * @return true if successfull
	 * @throws TierNotFoundException
	 * @throws TelementNotFoundException
	 */
	public boolean rank(Telement element, Tier toTier) throws TierNotFoundException, TelementNotFoundException {
		removeFromUnranked(element);
		return addToTier(toTier, element);
	}
	
	/**
	 * Unranks a {@link Telement}
	 *  
	 * @param element element to unrank
	 * @return true if successfull
	 * @throws TierNotFoundException
	 * @throws TelementNotFoundException
	 */
	public boolean unrank(Telement element) throws TierNotFoundException, TelementNotFoundException {
		Tier fromTier = findTierByElement(element);
		verifyElementExistenceInTier(element, fromTier);
		addToUnranked(element);
		element.changeTo(UNRANKED);
		return removeFromTier(fromTier, element);
	}
	
	/**
	 * Ranks a {@link Telement} to a specified position
	 * 
	 * @param element element to rank
	 * @param toTierIndex tier to rank to
	 * @param insertIndex destination index 
	 * @return true if successfull
	 * @throws TierNotFoundException
	 * @throws TelementNotFoundException
	 */
	public boolean rankInsert(Telement element, int toTierIndex, int insertIndex) throws TierNotFoundException, TelementNotFoundException {
		removeFromUnranked(element);
		boolean added = addToTier(toTierIndex, element);
		tiers.get(insertIndex).moveTo(insertIndex, element);
		return added;
	}
	
	/**
	 * Ranks a {@link Telement} to a specified position
	 * 
	 * @param element element to rank
	 * @param toTier tier to rank to
	 * @param insertIndex destination index 
	 * @return true if successfull
	 * @throws TierNotFoundException
	 * @throws TelementNotFoundException
	 */
	public boolean rankInsert(Telement element, Tier toTier, int insertIndex) throws TierNotFoundException, TelementNotFoundException {
		removeFromUnranked(element);
		boolean added = addToTier(toTier, element);
		toTier.moveTo(insertIndex, element);
		return added;
	}
	
	/**
	 * Unanks a {@link Telement} to a specified position
	 * 
	 * @param element element to rank
	 * @param insertIndex destination index 
	 * @return true if successfull
	 * @throws TierNotFoundException
	 * @throws TelementNotFoundException
	 */
	public boolean unrankInsert(Telement element, int insertIndex) throws TierNotFoundException, TelementNotFoundException {
		Tier fromTier = findTierByElement(element);
		verifyElementExistenceInTier(element, fromTier);
		addToUnranked(element, insertIndex);
		return removeFromTier(fromTier, element);
	}

	
	//------------------------------ editing ------------------------------//

	public boolean addTier(Tier tier) { Objects.requireNonNull(tier); return tiers.add(tier); }
	
	public void addToUnranked(Telement element) { unranked.add(element); element.changeTo(UNRANKED); }

	public void addToUnranked(Telement element, int index) { unranked.add(index, element); element.changeTo(UNRANKED); }
	
	public boolean addToTier(int tierIndex, Telement element) throws TierNotFoundException {
		verifyTierExistence(tierIndex);
		element.changeTo(RANKED);
		return tiers.get(tierIndex).add(element);
	}
	
	public boolean addToTier(Tier tier, Telement element) throws TierNotFoundException {
		checkTierExistence(tier);
		element.changeTo(RANKED);
		return tier.add(element);
	}
	
	public Tier removeTier(int tierIndex) throws TierNotFoundException  {
		verifyTierExistence(tierIndex);
		return tiers.remove(tierIndex);
	}

	public boolean removeTier(Tier tier) throws TierNotFoundException {
		verifyTierExistence(tiers.indexOf(tier));
		return tiers.remove(tier);
	}
	
	public void removeFromUnranked(Telement element) throws TelementNotFoundException {
		verifyElementExistence(element, unranked);
		unranked.remove(element);
		element.changeTo(RANKED);
	}
	
	public boolean removeFromTier(int tierIndex, Telement element) throws TierNotFoundException, TelementNotFoundException {
		verifyElementExistenceInTier(element, tierIndex);
		if (!tiers.get(tierIndex).remove(element)) 
			throw new TelementNotFoundException();
		else return true;
	}
	
	public boolean removeFromTier(Tier tier, Telement element) throws TierNotFoundException, TelementNotFoundException {
		verifyElementExistenceInTier(element, tier);
		if (!tier.remove(element)) 
			throw new TelementNotFoundException();
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
	 * @throws TelementNotFoundException
	 */
	public void swapElements(int tierIndex, Telement a, Telement b) throws TierNotFoundException, TelementNotFoundException {
		verifyElementExistenceInTier(a, tierIndex); 
		verifyElementExistenceInTier(b, tierIndex);
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
	 * @throws TelementNotFoundException
	 */
	public void swapElements(Tier tier, Telement a, Telement b) throws TierNotFoundException, TelementNotFoundException {
		verifyElementExistenceInTier(a, tier); 
		verifyElementExistenceInTier(b, tier);
		tier.swap(a, b);
	}
	
	/**
	 * Swaps two elements from the unranked list
	 * 
	 * @param a first element
	 * @param b second element
	 * @throws TelementNotFoundException
	 */
	public void swapUnranked(Telement a, Telement b) throws TelementNotFoundException {
		int ai = verifyElementExistence(a, unranked), bi = verifyElementExistence(b, unranked);
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
	public int indexOf(Tier t) throws TierNotFoundException { return verifyTierExistence(tiers.indexOf(t)); }
	
	public int size() { return tiers.size(); }

	
	//------------------------------ moving ------------------------------//
	
	/**
	 * Moves elements between tiers
	 * 
	 * @param toTierIndex tier destination index
	 * @param e element to move
	 * @return true if successfull
	 * @throws TelementNotFoundException
	 * @throws TierNotFoundException
	 */
	public boolean moveToTier(int toTierIndex, Telement e) throws TelementNotFoundException, TierNotFoundException {
		verifyTierExistence(toTierIndex);
		int fromTierIndex = findTierIndexByElement(e);
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
	 * @throws TelementNotFoundException
	 * @throws TierNotFoundException
	 */
	public boolean moveToTier(Tier toTier, Telement e) throws TelementNotFoundException, TierNotFoundException {
		checkTierExistence(toTier);
		if (findTierByElement(e).remove(e)) 
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
	 * @throws TelementNotFoundException
	 * @throws TierNotFoundException
	 */
	public boolean moveToTier(int toTierIndex, Telement e, int toElementIndex) 
			throws TelementNotFoundException, TierNotFoundException {
		verifyTierExistence(toTierIndex);
		int fromTierIndex = findTierIndexByElement(e);
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
	 * @throws TelementNotFoundException
	 * @throws TierNotFoundException
	 */
	public boolean moveToTier(Tier toTier, Telement e, int toElementIndex) 
			throws TelementNotFoundException, TierNotFoundException {
		checkTierExistence(toTier);
		int fromTierIndex = findTierIndexByElement(e);
		if (tiers.get(fromTierIndex).remove(e)) {
			toTier.add(e);
			toTier.moveTo(toElementIndex, e);
			return true;
		}
		else return false;
	}
	
	public void moveUnranked(Telement e, int toElementIndex) throws TelementNotFoundException, TierNotFoundException {
		verifyElementExistence(e, unranked);
		unranked.remove(e);
		unranked.add(toElementIndex, e);
	}

	public void moveTierTo(Tier from, Tier to) throws TierNotFoundException {
		int indexFrom = checkTierExistence(from),
			indexTo = checkTierExistence(to);
		
		tiers.remove(indexFrom);
		tiers.add(indexTo, from);
	}
	
	public void moveTierTo(Tier from, int toIndex) throws TierNotFoundException {
		int indexFrom = checkTierExistence(from);		
		tiers.remove(indexFrom);
		tiers.add(toIndex, from);
	}
	
	//------------------------------ exceptions ------------------------------//
	
	public boolean contains(Telement element) {
		return tiers.stream().anyMatch(tier -> tier.contains(element)) 
				|| unranked.contains(element);
	}
	
	public boolean contains(Tier tier) { return tiers.contains(tier); }
	
	private int verifyTierExistence(int tierIndex) throws TierNotFoundException {
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
	
	private int verifyElementExistence(Telement e, List<Telement> ec) throws TelementNotFoundException {
		var exception = new TelementNotFoundException("Element \"" + e + "\" not found in list \"" + ec + "\"");
		Objects.requireNonNull(e); Objects.requireNonNull(ec);
		try {
			int elementIndex = ec.indexOf(e);
			if (elementIndex == -1) 
				throw exception; 
			return elementIndex;
		} catch (IndexOutOfBoundsException physException ) { 
			throw exception; 
		}
	}
	
	private void verifyElementExistenceInTier(Telement e, int tierIndex) throws TelementNotFoundException, TierNotFoundException {
		verifyTierExistence(tierIndex);
		verifyElementExistence(e, tiers.get(tierIndex).getElements());
	}
	
	private void verifyElementExistenceInTier(Telement e, Tier tier) throws TelementNotFoundException, TierNotFoundException {
		checkTierExistence(tier);
		verifyElementExistence(e, tier.getElements());
	}

	/*
	for (Tier tier : tiers) {
		for (Telement element : tier.getElements()) {
			if (e.equals(element)) return tier;
		}
	}
	*/
	private Tier findTierByElement(Telement e) throws TelementNotFoundException {
		
		var matching = tiers.stream()
				.filter( tier -> tier.contains(e))
				.collect(Collectors.toList());
		
		if (matching.size() != 1) throw new TelementNotFoundException();
		return matching.getFirst();
	}
	
	private int findTierIndexByElement(Telement e) throws TelementNotFoundException {
		return tiers.indexOf(findTierByElement(e));
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
		Objects.requireNonNull(th);
		Objects.requireNonNull(th.name());
		Objects.requireNonNull(th.color());
		
		Tier t = tiers.get(tierIndex);
		t.setName(th.name()); t.setColor(th.color());
	}
	public String getTierListName() { return name; }
	
	public String getTierName(int tierIndex) { return getTierHeader(tierIndex).name(); }
	
	public String getTierColor(int tierIndex) { return getTierHeader(tierIndex).name(); }
	
	private TierHeader getTierHeader(int tierIndex) { return tiers.get(tierIndex).getHeader(); }
	
	public List<Telement> getUnranked() { return List.copyOf(unranked); };
	
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
		
		tiers.stream()
		.map( tier -> tier.toString(format) )
		.forEach( tierString ->  {
			sb.append(tierString);
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
		});
		
		sb.append("Unranked:" + System.lineSeparator() + unranked.toString());
		return sb.toString();
	}
}
