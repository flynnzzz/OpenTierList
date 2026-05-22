package controller;

import java.util.Objects;

import model.TierElement;
import model.Tier;
import model.TierList;
import model.enums.TierStringFormat;

/**
 * Controller interface for creating and modifying tier lists.
 * 
 * @author flynnz
 * @version 1.17
 * @since v0.0.0
 */
public interface TierListController {
	
	//TODO: update controller docs
	public static TierListController of(TierList tl) {
		Objects.requireNonNull(tl);
		return new StandardTierListController(tl);
	}
	
	public static TierListController of() {
		return new StandardTierListController();
	}
	
	public void setTierListName(String name); 

	public void rank(TierElement e, int toTier);
	
	public void rank(int toIndex, TierElement e, int toTier);
	
	public void unrank(TierElement e, int fromTier);

	public void unrank(int toIndex, TierElement e, int fromTier);
	
	public void addTier(Tier t);
	
	public void addToUnranked(TierElement e);

	public void removeTier(Tier t);

	public void removeFromUnranked(TierElement e);
	
	public void swapTiers(int a, int b);

	public void swapTierElements(int tierIndex, TierElement a, TierElement b);

	public void swapUnrankedElements(TierElement a, TierElement b);
	
	public void moveTo(TierElement e, int toTier);

	public void moveTo(TierElement e, int toTier, int toIndex);
	
	public String toString();

	public String toString(TierStringFormat format);
}
