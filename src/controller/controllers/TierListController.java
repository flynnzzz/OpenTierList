package controller.controllers;

import java.util.List;
import java.util.Objects;

import model.enums.TierStringFormat;
import model.models.Tier;
import model.models.Telement;
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

	public void rank(Telement e, Tier toTier);
	
	public void rank(int toIndex, Telement e, Tier toTier);
	
	public void unrank(Telement e);

	public void unrank(int toIndex, Telement e);
	
	public void addTier(Tier t);

	public void addTier();
	
	public void addToUnranked(Telement e);

	public void removeTier(Tier t);

	public void removeFromUnranked(Telement e);
	
	public void swapTiers(Tier a, Tier b);

	public void swapTierElements(Tier tier, Telement a, Telement b);

	public void swapUnrankedElements(Telement a, Telement b);
	
	public void moveTo(Telement e, Tier toTier);

	public void moveTo(Telement e, Tier toTier, Telement toElement);
	
	public void moveTo(Telement e, Tier toTier, int toIndex);

	public void moveUnranked(Telement e, Telement toElement);
	
	public void moveUnranked(Telement e, int toIndex);

	public void moveTierTo(Tier from, Tier to);

	public void moveTierTo(Tier from, int toindex);
	
	public Tier getTierByElement(Telement e);

	public Telement getElementByHash(String hashCode);

	public Tier getTierByHash(String hashCode);
	
	public List<Telement> getUnranked();

	public List<Tier> getTiers();
	
	public String toString();

	public String toString(TierStringFormat format);
}
