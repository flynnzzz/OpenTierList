package controller;

import java.util.Objects;

import model.TierElement;
import model.Tier;
import model.TierList;
import model.exceptions.ElementNotFoundException;
import model.exceptions.TierNotFoundException;

/**
 * Main implementation of {@link TierListController}.
 * 
 * @author flynnz
 * @version 1.67
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
	
	@Override
	public void setTierListName(String name) {
		try { tierList.setTierListName(name); }
		catch(NullPointerException | IllegalArgumentException ex) {
				if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
				else System.err.println(IAE_ERROR + ex.toString());
		}
	}
	
	// TODO? rank and unrank variants with String as second parameter -> rank(Element e, String to)
	@Override
	public void rank(TierElement e, int to) {
		try { tierList.rank(e, to); }
		catch(NullPointerException | IllegalArgumentException |
				  TierNotFoundException | ElementNotFoundException ex) {
				if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
				else System.err.println(IAE_ERROR + ex.toString());
		}
	}

	@Override
	public void unrank(TierElement e, int from) {
		try { tierList.unrank(e, from); }
		catch(NullPointerException | IllegalArgumentException |
				TierNotFoundException | ElementNotFoundException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
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

	@Override
	public void swapTiers(int a, int b) {
		try { tierList.swapTiers(a, b); }
		catch(NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
			if (ex instanceof NullPointerException) System.err.println(NPE_ERROR); 
			else System.err.println(IAE_ERROR + ex.toString());
		}
	}

	@Override
	public void swapTierElements(int tierIndex, TierElement a, TierElement b) {
		try { tierList.swapElements(tierIndex, a, b); }
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
}
