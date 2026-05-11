package controller;

import java.util.Objects;

import model.Element;
import model.Tier;
import model.TierList;

/**
 * Controller interface for creating and modifying tier lists.
 * 
 * @author flynnz
 * @version 0.00
 * @since v0.0.0
 */
public interface TierListController {
	
	public static TierListController of(TierList tl) {
		Objects.requireNonNull(tl);
		return new StandardTierListController(tl);
	}
	
	public static TierListController of() {
		return new StandardTierListController();
	}
	
	public void setTierListName(String name); 
	
	public void addTier(Tier t);
	
	public void removeTier(Tier t);
	
	public void addUnranked(Element e);
	
	public void rank(Element e, Tier to);

	public void unrank(Element e, Tier from);
	
	public void swapTiers(int a, int b);

	public void swapElements(Tier t, Element a, Element b);

	public void swapUnrankedElements(Element a, Element b);
	
}
