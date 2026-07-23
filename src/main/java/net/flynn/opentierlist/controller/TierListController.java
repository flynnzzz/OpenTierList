package net.flynn.opentierlist.controller;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import net.flynn.opentierlist.model.enums.DefaultTier;
import net.flynn.opentierlist.model.enums.TierStringFormat;
import net.flynn.opentierlist.model.models.TierElement;
import net.flynn.opentierlist.model.models.Tier;
import net.flynn.opentierlist.model.models.TierList;

/**
 * Controller interface for creating and modifying tier lists.
 * 
 * @author flynnz
 * @version 2.0.0
 * @since v0.0.0
 */
public interface TierListController {

  // TODO: update controller docs
  public static TierListController of(TierList tl) {
    Objects.requireNonNull(tl);
    return new StandardTierListController(tl);
  }

  public static TierListController ofDefaultTiers() {
    var tierList = new TierList();
    for (var tier : DefaultTier.values())
      tierList.addTier(tier.value());
    return new StandardTierListController(tierList);
  }

  // ----- ranking ------//

  public void tier(TierElement element, Tier toTier);

  public void tier(TierElement element, Tier toTier, int toIndex);

  public void unTier(TierElement element);

  public void unTier(TierElement element, int toIndex);

  // ----- adding and removing ------//

  public void addTier(Tier tier);

  public void addDefaultTier();

  public void addUnTiered(TierElement element);

  public void removeTier(Tier tier);

  public void removeTierElement(TierElement element);

  public void removeUnTiered(TierElement e);

  // ----- swapping ------//

  public void swapTiers(Tier a, Tier b);

  public void swapTiered(Tier tier, TierElement a, TierElement b);

  public void swapUnTiered(TierElement a, TierElement b);

  // ----- moving ------//

  public void moveTo(TierElement element, Tier toTier);

  public void moveTo(TierElement element, Tier toTier, TierElement toElement);

  public void moveTo(TierElement element, Tier toTier, int toIndex);

  public void moveUnTiered(TierElement element, TierElement toElement);

  public void moveUnTiered(TierElement element, int toIndex);

  public void moveTierTo(Tier from, Tier to);

  public void moveTierTo(Tier from, int toIndex);

  // ----- setters and getters ------//

  public void setTierListName(String name);

  public void setTierName(Tier tier, String name);

  public Optional<Tier> getTierByElement(TierElement element);

  public Optional<TierElement> getElementByHash(String hashCode);

  public Optional<Tier> getTierByHash(String hashCode);

  public List<TierElement> getUnTiered();

  public List<Tier> getTiers();

  public String getTierListName();

  // ----- persistence ------//

  public void saveTierList();

  public void saveTierListTo(Path path);

  public void saveTierListAs(String name);

  // ----- misc ------//

  public String toString();

  public String toString(TierStringFormat format);

  public boolean tierElementExists(TierElement element);

  public boolean tierElementExistsById(Long id);

  public boolean tierExists(Tier tier);
}
