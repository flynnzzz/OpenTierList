package controller;

import java.util.Objects;
import java.util.Optional;

import model.TierElement;
import model.TierElementRanked;
import model.TierElementUnranked;
import model.Tier;
import model.TierList;

/**
 * Controller interface for creating and modifying tier lists.
 * 
 * @author flynnz
 * @version 1.17
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

	public void rank(TierElementUnranked e, int to);
	
	public void unrank(TierElementRanked e, int from);
	
	public void addTier(Tier t);
	
	public Optional<TierElement> addToUnranked(TierElement e);

	public void removeTier(Tier t);

	public Optional<TierElement> removeFromUnranked(TierElement e);
	
	public void swapTiers(int a, int b);

	public void swapTierElements(int tierIndex, TierElement a, TierElement b);

	public void swapUnrankedElements(TierElement a, TierElement b);
	
}
