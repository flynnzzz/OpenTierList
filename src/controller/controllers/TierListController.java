package controller.controllers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import model.enums.TierStringFormat;
import model.models.Tier;
import model.models.Telement;
import model.models.TierList;

/**
 * Controller interface for creating and modifying tier lists.
 * 
 * @author flynnz
 * @version 2.0.0
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
	

	//----- ranking ------//
	
	public void rank(Telement e, Tier toTier);
	
	public void rank(int toIndex, Telement e, Tier toTier);
	
	public void unrank(Telement e);

	public void unrank(int toIndex, Telement e);

	
	//----- adding and removing ------//

	public void addTier(Tier t);

	public void addTier();
	
	public void addToUnranked(Telement e);

	public void removeTier(Tier t);

	public void removeFromUnranked(Telement e);
	
	
	//----- swapping ------//
	
	public void swapTiers(Tier a, Tier b);

	public void swapTelements(Tier tier, Telement a, Telement b);

	public void swapUnranked(Telement a, Telement b);
	
	
	//----- moving ------//

	public void moveTo(Telement e, Tier toTier);

	public void moveTo(Telement e, Tier toTier, Telement toElement);
	
	public void moveTo(Telement e, Tier toTier, int toIndex);

	public void moveUnranked(Telement e, Telement toElement);
	
	public void moveUnranked(Telement e, int toIndex);

	public void moveTierTo(Tier from, Tier to);

	public void moveTierTo(Tier from, int toindex);
	
	
	//----- setters and getters ------//
	
	public void setTierListName(String name); 

	public void setTierName(Tier tier, String name);
	
	public Optional<Tier> getTierByElement(Telement e);

	public Optional<Telement> getElementByHash(String hashCode);

	public Optional<Tier> getTierByHash(String hashCode);
	
	public List<Telement> getUnranked();

	public List<Tier> getTiers();

	public String getTierListName();
	

	//----- misc ------//

	public String toString();

	public String toString(TierStringFormat format);
}
