package controller;

import java.util.Objects;

import model.TierElement;
import model.Tier;
import model.TierList;

/**
 * Main implementation of {@link TierListController}.
 * 
 * @author flynnz
 * @version 1.67
 * @since v0.0.0
 */
public class StandardTierListController implements TierListController {
	
	private TierList tl;
	private final static String NPE_ERROR = System.lineSeparator()
											+ "Aborting operation:"
											+ System.lineSeparator()
											+ "\tNullPointerException encountered.";
	
	//---------------------------------- Ctors ----------------------------------//
	
	/**
	 * Constructor that creates a controller for {@link TierList}.
	 * 
	 * Instanciates an {@link TierList} with the given parameter
	 * 
	 * @param tl parameter to pass 
	 */
	public StandardTierListController(TierList tl) { Objects.requireNonNull(tl); this.tl = tl; }
	
	/**
	 * Constructor that creates a controller for {@link TierList}.
	 * 
	 * Instanciates an empty {@link TierList}
	 */
	public StandardTierListController() { this(new TierList()); }
	

	
	//---------------------------------- methods ----------------------------------//
	
	@Override
	public void setTierListName(String name) {
		try { tl.setTierListName(name); }
		catch(IllegalArgumentException e) { }
	}
	
	@Override
	public void rank(TierElement e, int to) {

	}
	
	// TODO rank and unrank variants with String as second parameter -> rank(Element e, String to)

	@Override
	public void unrank(TierElement e, int from) {

	}
	
	@Override
	public void addTier(Tier t) {
		try { tl.addTier(t); }
		catch(NullPointerException npe) { System.err.println(NPE_ERROR); }
	}
	
	@Override
	public void addToUnranked(TierElement e) {
		try {
			tl.addToUnranked(e);
		}
		catch(NullPointerException npe) {
			System.err.println("Addition to unranked failed.");
		}
	}

	@Override
	public void addToTier(TierElement e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void removeTier(Tier t) {
		try {
			tl.removeTier(t);
		}
		catch(NullPointerException npe) {
			System.err.println("Tier removal failed.");
		}
	}

	@Override
	public void removeFromUnranked(TierElement e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFromTier(int tierIndex, TierElement e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swapTiers(int a, int b) {
		tl.swapTiers(a, b);
	}

	@Override
	public void swapTierElements(int tierIndex, TierElement a, TierElement b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swapUnrankedElements(TierElement a, TierElement b) {
		// TODO Auto-generated method stub
		
	}
}
