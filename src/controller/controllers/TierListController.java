package controller.controllers;

import java.util.List;
import java.util.Objects;

import model.enums.TierStringFormat;
import model.models.Tier;
import model.models.TierElement;
import model.models.TierList;

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

	public void setTierName(Tier tier, String name);

	public void rank(TierElement e, Tier toTier);
	
	public void rank(int toIndex, TierElement e, Tier toTier);
	
	public void unrank(TierElement e);

	public void unrank(int toIndex, TierElement e);
	
	public void addTier(Tier t);

	public void addTier();
	
	public void addToUnranked(TierElement e);

	public void removeTier(Tier t);

	public void removeFromUnranked(TierElement e);
	
	public void swapTiers(Tier a, Tier b);

	public void swapTierElements(Tier tier, TierElement a, TierElement b);

	public void swapUnrankedElements(TierElement a, TierElement b);
	
	public void moveTo(TierElement e, Tier toTier);

	public void moveTo(TierElement e, Tier toTier, TierElement toElement);
	
	public void moveTo(TierElement e, Tier toTier, int toIndex);

	public void moveUnranked(TierElement e, TierElement toElement);
	
	public void moveUnranked(TierElement e, int toIndex);

	public void moveTierTo(Tier from, Tier to);

	public void moveTierTo(Tier from, int toindex);
	
	public Tier getTierByElement(TierElement e);

	public TierElement getElementByHash(String hashCode);

	public Tier getTierByHash(String hashCode);
	
	public List<TierElement> getUnranked();

	public List<Tier> getTiers();
	
	public String toString();

	public String toString(TierStringFormat format);
}
