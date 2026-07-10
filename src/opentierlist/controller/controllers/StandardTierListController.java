package opentierlist.controller.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import opentierlist.model.enums.DefaultTier;
import opentierlist.model.enums.TierStringFormat;
import opentierlist.model.exceptions.TelementNotFoundException;
import opentierlist.model.exceptions.TierNotFoundException;
import opentierlist.model.models.Telement;
import opentierlist.model.models.Tier;
import opentierlist.model.models.TierList;

/**
 * Main implementation of {@link TierListController}.
 * 
 * @author flynnz
 * @version 2.0.0
 * @since v0.0.0
 */ 
public class StandardTierListController implements TierListController {
	
	private TierList tierList;
	
	// probably useless might delete later 
	private final static String NPE_ERROR = System.lineSeparator()
			+ "Aborting operation for:"
			+ System.lineSeparator()
			+ "\tNullPointerException"
			+ System.lineSeparator();
	private final static String IAE_ERROR = System.lineSeparator()
			+ "Aborting operation:"
			+ System.lineSeparator()
			+ "\tIllegalArgumentException: unknown error."
			+ System.lineSeparator();
	
	//---------------------------------- Ctors ----------------------------------//
	
	/**
	 * Constructor that creates a controller for {@link TierList}.
	 * 
	 * Instanciates an {@link TierList} with the given parameter
	 * 
	 * @param tierList parameter to pass 
	 */
	public StandardTierListController(TierList tierList) { Objects.requireNonNull(tierList); this.tierList = tierList; }
	
	/**
	 * Constructor that creates a controller for {@link TierList}.
	 * 
	 * Instanciates an empty {@link TierList}
	 */
	public StandardTierListController() { this.tierList = defaultTierList(); }

	
	//---------------------------------- methods ----------------------------------//
	
	//------------------------------ ranking ------------------------------//

	@Override
	public void rank(Telement e, Tier toTier) {
		try { tierList.rank(e, toTier); }
		catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}
	
	@Override
	public void rank(int toIndex, Telement e, Tier toTier) {
		try { tierList.rankInsert(e, toTier,  toIndex); }
		catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}
	
	@Override
	public void unrank(Telement e) {
		try { tierList.unrank(e); }
		catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}
	
	@Override
	public void unrank(int toIndex, Telement e) {
		try { tierList.unrankInsert(e, toIndex); }
		catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}


	//------------------------------ editing ------------------------------//
	
	@Override
	public void setTierListName(String name) {
		try { tierList.setTierListName(name); }
		catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}
	
	@Override
	public void setTierName(Tier tier, String name) {
		try { tierList.setTierName(tierList.indexOf(tier), name); }
		catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}
	
	@Override
	public void addTier(Tier t) {
		try { tierList.addTier(t); }
		catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}
	
	@Override
	public void addTier() {
		tierList.addTier(new Tier());
	}
	
	@Override
	public void addToUnranked(Telement e) {
		try { tierList.addToUnranked(e); }
		catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}
	
	@Override
	public void removeTier(Tier t) {
		try { tierList.removeTier(t); }  
		catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}

	@Override
	public void removeFromUnranked(Telement e) {
		try { tierList.removeFromUnranked(e); }
		catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}

	
	//------------------------------ swapping ------------------------------//

	@Override
	public void swapTiers(Tier a, Tier b) {
		try { tierList.swapTiers(a, b); }
		catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}

	@Override
	public void swapTelements(Tier tier, Telement a, Telement b) {
		try { tierList.swapElements(tier, a, b); }
		catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}

	@Override
	public void swapUnranked(Telement a, Telement b) {
		try { tierList.swapUnranked(a, b); }
		catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}
	

	//------------------------------ moving ------------------------------//
	
	@Override
	public void moveTo(Telement e, Tier toTier) {
		try {
			tierList.moveToTier(toTier, e);
		} catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}
	
	@Override
	public void moveTo(Telement e, Tier toTier, Telement toElement) {
		try {
			int toIndex = toTier.getElements().indexOf(toElement);
			tierList.moveToTier(toTier, e, toIndex);
		} catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}
	
	@Override
	public void moveTo(Telement e, Tier toTier, int toIndex) {
		try {
			tierList.moveToTier(toTier, e, toIndex);
		} catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}

	@Override
	public void moveUnranked(Telement e, Telement toElement) {
		try {
			int toIndex = tierList.getUnranked().indexOf(toElement);
			tierList.moveUnranked(e, toIndex);
		} catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}

	@Override
	public void moveUnranked(Telement e, int toIndex) {
		try {
			tierList.moveUnranked(e, toIndex);
		} catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}

	@Override
	public void moveTierTo(Tier from, Tier to) {
		try {
			tierList.moveTierTo(from, to);
		} catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}
	
	@Override
	public void moveTierTo(Tier from, int toIndex) {
		try {
			tierList.moveTierTo(from, toIndex);
		} catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
	}
	
	
	//------------------------------ misc ------------------------------//

	@Override
	public String toString() {
		return tierList.toString();
	}

	@Override
	public String toString(TierStringFormat format) {
		return tierList.toString(format);
	}
	
	@Override
	public TierList defaultTierList() {
		var tierList = new TierList();
		for (DefaultTier tier : DefaultTier.values())
			tierList.addTier(tier.value());
		return tierList;
	}
	
	//------------------------------ getters ------------------------------//
	
	@Override
	public String getTierListName() {
		return tierList.getTierListName();
	}
	
	@Override
	public List<Telement> getUnranked() {
		return tierList.getUnranked();
	}
	
	@Override
	public List<Tier> getTiers() {
		return tierList.getTiers();
	}

	@Override
	public Optional<Tier> getTierByElement(Telement e) {
		Optional<Tier> element = Optional.empty();
		
		try {
			element = 
			tierList.getTiers().stream()
			.filter(t -> t.contains(e))
			.findFirst();
			
			if (element.isEmpty()) throw new TelementNotFoundException();
		} catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
		
		return element;
	}

	@Override
	public Optional<Telement> getElementByHash(String hashCode) {
		Optional<Telement> telement = Optional.empty();
		
		try {
			// firstly, search in tiers
			telement =
			tierList.getTiers().stream()
			.flatMap( t -> t.getElements().stream())
			.filter(e -> String.valueOf(e.hashCode()).equals(hashCode)).findFirst();
			
			if (telement.isEmpty()) {
				// if not found, seach in unranked
				telement = 
				tierList.getUnranked().stream()
				.filter(e -> String.valueOf(e.hashCode()).equals(hashCode))
				.findFirst();
			}
			
			if (telement.isEmpty()) throw new TelementNotFoundException();	
		} catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
		
		return telement;
	}

	@Override
	public Optional<Tier> getTierByHash(String hashCode) {
		Optional<Tier> tier = Optional.empty();
		
		try {
			tier = 
			tierList.getTiers().stream()
			.filter( t -> String.valueOf(t.hashCode()).contains(hashCode))
			.findFirst();

			if (tier.isEmpty()) throw new TierNotFoundException();
		} catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) { printStackTrace(ex); }
		
		return tier;
	}
	
	private void printStackTrace(Exception ex) {
		if (ex instanceof NullPointerException) 
			System.err.println(NPE_ERROR + System.lineSeparator() + ex); 
		else
			System.err.println(IAE_ERROR + System.lineSeparator() + ex);
	}
}
