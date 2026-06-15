package controller.controllers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import model.enums.TierStringFormat;
import model.exceptions.ElementNotFoundException;
import model.exceptions.TierNotFoundException;
import model.models.Tier;
import model.models.TierElement;
import model.models.TierList;

/**
 * Main implementation of {@link TierListController}.
 * 
 * @author flynnz
 * @version 1.96
 * @since v0.0.0
 */ 
public class StandardTierListController implements TierListController {
	
	private TierList tierList;
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
	public StandardTierListController() { this(new TierList()); }

	
	//---------------------------------- methods ----------------------------------//
	
	//------------------------------ ranking ------------------------------//

	@Override
	public void rank(TierElement e, Tier toTier) {
		try { tierList.rank(e, toTier); }
		catch(NullPointerException | IllegalArgumentException |
				  TierNotFoundException | ElementNotFoundException ex) {
				if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
				else System.err.println(IAE_ERROR + ex.toString());
		}
	}
	
	@Override
	public void rank(int toIndex, TierElement e, Tier toTier) {
		try { tierList.rankInsert(toTier, e, toIndex); }
		catch(NullPointerException | IllegalArgumentException |
				TierNotFoundException | ElementNotFoundException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}
	
	@Override
	public void unrank(TierElement e) {
		try { tierList.unrank(e); }
		catch(NullPointerException | IllegalArgumentException |
				TierNotFoundException | ElementNotFoundException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}
	
	@Override
	public void unrank(int toIndex, TierElement e) {
		try { tierList.unrankInsert(e, toIndex); }
		catch(NullPointerException | IllegalArgumentException |
				TierNotFoundException | ElementNotFoundException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}


	//------------------------------ editing ------------------------------//
	
	@Override
	public void setTierListName(String name) {
		try { tierList.setTierListName(name); }
		catch(NullPointerException | IllegalArgumentException ex) {
				if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
				else {
					System.err.println(IAE_ERROR + ex.toString()); 
					tierList.setTierListName(TierList.DEFAULT_TIERLIST_NAME);	
				}
		}
	}
	
	@Override
	public void setTierName(Tier tier, String name) {
		try { tierList.setTierName(tierList.indexOf(tier), name); }
		catch(NullPointerException | IllegalArgumentException ex) {
				if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
				else {
					System.err.println(IAE_ERROR + ex.toString()); 
					tierList.setTierName(tierList.indexOf(tier), Tier.DEFAULT_TIER_NAME);	
				}
		}
	}
	@Override
	public void addTier(Tier t) {
		try { tierList.addTier(t); }
		catch(NullPointerException | IllegalArgumentException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}
	
	@Override
	public void addToUnranked(TierElement e) {
		try { tierList.addToUnranked(e); }
		catch(NullPointerException | IllegalArgumentException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}
	
	@Override
	public void removeTier(Tier t) {
		try { tierList.removeTier(t); }  
		catch(NullPointerException | IllegalArgumentException | TierNotFoundException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}

	@Override
	public void removeFromUnranked(TierElement e) {
		try { tierList.removeFromUnranked(e); }
		catch(NullPointerException | IllegalArgumentException | ElementNotFoundException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}

	
	//------------------------------ swapping ------------------------------//

	@Override
	public void swapTiers(Tier a, Tier b) {
		try { tierList.swapTiers(a, b); }
		catch(NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}

	@Override
	public void swapTierElements(Tier tier, TierElement a, TierElement b) {
		try { tierList.swapElements(tier, a, b); }
		catch(NullPointerException | IllegalArgumentException |
				TierNotFoundException | ElementNotFoundException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}

	@Override
	public void swapUnrankedElements(TierElement a, TierElement b) {
		try { tierList.swapUnranked(a, b); }
		catch(NullPointerException | IllegalArgumentException | ElementNotFoundException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}
	

	//------------------------------ moving ------------------------------//
	
	@Override
	public void moveTo(TierElement e, Tier toTier) {
		try {
			tierList.moveToTier(toTier, e);
		}
		catch(NullPointerException | IllegalArgumentException |
				TierNotFoundException | ElementNotFoundException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}
	
	@Override
	public void moveTo(TierElement e, Tier toTier, TierElement toElement) {
		try {
			int toIndex = toTier.getElements().indexOf(toElement);
			tierList.moveToTier(toTier, e, toIndex);
		}
		catch(NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}
	
	@Override
	public void moveTo(TierElement e, Tier toTier, int toIndex) {
		try {
			tierList.moveToTier(toTier, e, toIndex);
		}
		catch(NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}

	@Override
	public void moveUnranked(TierElement e, TierElement toElement) {
		try {
			int toIndex = tierList.getUnranked().indexOf(toElement);
			tierList.moveUnranked(e, toIndex);
		}
		catch(NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}

	@Override
	public void moveUnranked(TierElement e, int toIndex) {
		try {
			tierList.moveUnranked(e, toIndex);
		}
		catch(NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}

	@Override
	public void moveTierTo(Tier from, Tier to) {
		try {
			tierList.moveTierTo(from, to);
		}
		catch(NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}
	
	
	//------------------------------ toString ------------------------------//

	@Override
	public String toString() {
		return tierList.toString();
	}

	@Override
	public String toString(TierStringFormat format) {
		return tierList.toString(format);
	}

	
	//------------------------------ getters ------------------------------//
	
	@Override
	public List<TierElement> getUnranked() {
		return tierList.getUnranked();
	}
	
	@Override
	public List<Tier> getTiers() {
		return tierList.getTiers();
	}

	@Override
	public Tier getTierByElement(TierElement e) {
		try {
			List<Tier> filtered = 
					 tierList.getTiers().stream()
					.filter( tier -> (tier.getElements().indexOf(e) != -1) )
					.collect(Collectors.toList());
			if (filtered.size() > 1 || filtered.size() == 0)
				throw new ElementNotFoundException();
			else return filtered.get(0);
		}
		catch(NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
		return null;
	}

	@Override
	public TierElement getElementByHash(String hashCode) {
		try {
			for (var tier : tierList.getTiers())
				for (var element : tier.getElements()) 
					if (Integer.valueOf(element.hashCode()).toString().equals(hashCode))
						return element;
			throw new ElementNotFoundException();
		} catch (NullPointerException | IllegalArgumentException | ElementNotFoundException ex) {
			for (var unranked : tierList.getUnranked()) {
				if (Integer.valueOf(unranked.hashCode()).toString().equals(hashCode))
					return unranked;
			}
		}
		return null;
	}

	@Override
	public Tier getTierByHash(String hashCode) {
		try {
			for (var tier : tierList.getTiers())
					if (Integer.valueOf(tier.hashCode()).toString().equals(hashCode))
						return tier;
			throw new TierNotFoundException();
		} catch (NullPointerException | IllegalArgumentException | TierNotFoundException ex) {
			System.err.println("getTierByHash failed");	
		}
		return null;
	}
}
