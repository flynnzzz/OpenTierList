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
  static TierListController of(TierList tl) {
    Objects.requireNonNull(tl);
    return new StandardTierListController(tl);
  }

  static TierListController ofDefaultTiers() {
    var tierList = new TierList();
    for (var tier : DefaultTier.values())
      tierList.addTier(tier.value());
    return new StandardTierListController(tierList);
  }

  // ----- ranking ------//

  void tier(TierElement element, Tier toTier);

  void tier(TierElement element, Tier toTier, int toIndex);

  void unTier(TierElement element);

  void unTier(TierElement element, int toIndex);

  // ----- adding and removing ------//

  void addTier(Tier tier);

  void addDefaultTier();

  void addUnTiered(TierElement element);

  void removeTier(Tier tier);

  void removeTierElement(TierElement element);

  void removeUnTiered(TierElement e);

  // ----- swapping ------//

  void swapTiers(Tier a, Tier b);

  void swapTiered(Tier tier, TierElement a, TierElement b);

  void swapUnTiered(TierElement a, TierElement b);

  // ----- moving ------//

  void moveTiered(TierElement element, Tier toTier);

  void moveTiered(TierElement element, Tier toTier, TierElement toElement);

  void moveTiered(TierElement element, Tier toTier, int toIndex);

  void moveUnTiered(TierElement element, TierElement toElement);

  void moveUnTiered(TierElement element, int toIndex);

  void moveTierTo(Tier from, Tier to);

  void moveTierTo(Tier from, int toIndex);

  // ----- setters and getters ------//

  void setTierListName(String name);

  void setTierName(Tier tier, String name);

  Optional<Tier> getTierByElement(TierElement element);

  Optional<TierElement> getElementByHash(String hashCode);

  Optional<Tier> getTierByHash(String hashCode);

  List<TierElement> getUnTiered();

  List<Tier> getTiers();

  String getTierListName();

  // ----- persistence ------//

  void saveTierList();

  void saveTierListTo(Path path);

  void saveTierListAs(String name);

  // ----- misc ------//

  String toString();

  String toString(TierStringFormat format);

  boolean tierElementExists(TierElement element);

  boolean tierElementExistsById(Long id);

  boolean tierExists(Tier tier);
}
