package net.flynn.opentierlist.controller;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import net.flynn.opentierlist.model.enums.TierStringFormat;
import net.flynn.opentierlist.model.models.Tier;
import net.flynn.opentierlist.model.models.TierElement;
import net.flynn.opentierlist.model.models.TierList;
import net.flynn.opentierlist.ui.manual.SPTiered;

/**
 * Controller interface for creating and modifying tier lists.
 * 
 * @author flynnz
 * @version 2.7.2
 * @since v0.0.0
 */
public interface TierListController {

  /**
   * Controller factory for a tier list
   *
   * @param tl tier list to control
   * @return {@link TierListController}
   */
  static TierListController of(TierList tl) {
    Objects.requireNonNull(tl);
    return new StandardTierListController(tl);
  }

  /**
   * Controller factory for a default tier list
   * 
   * @return {@link TierListController}
   */
  static TierListController ofDefaultTiers() {
    return new StandardTierListController(TierList.ofDefaultTiers());
  }

  /**
   * Rank an untiered element
   *
   * @param unTiered element to rank
   * @param toTier   destination
   */
  void tier(TierElement unTiered, Tier toTier);

  /**
   * Rank an untiered element to a specified position
   *
   * @param unTiered element to rank
   * @param toTier   destination
   * @param position destination position
   */
  void tier(TierElement unTiered, Tier toTier, TierElement position);

  /**
   * Rank an untiered element to a specified position
   *
   * @param unTiered element to rank
   * @param toTier   destination
   * @param toIndex  position
   */
  void tier(TierElement unTiered, Tier toTier, int toIndex);

  /**
   * Un-rank a tiered element
   *
   * @param tiered element to un-rank
   */
  void unTier(TierElement tiered);

  /**
   * Un-rank a tiered element to a specified position
   *
   * @param tiered   element to un-rank
   * @param position destination position
   */
  void unTier(TierElement tiered, TierElement position);

  /**
   * Un-rank a tiered element to a specified position
   *
   * @param tiered  element to un-rank
   * @param toIndex position
   */
  void unTier(TierElement tiered, int toIndex);

  /**
   * Add a tier to the tier list
   *
   * @param tier tier to add
   */
  void addTier(Tier tier);

  /**
   * Add a default tier to the tier list
   */
  void addDefaultTier();

  /**
   * Add an unranked element to the tier list
   *
   * @param unTiered element to add
   */
  void addUnTiered(TierElement unTiered);

  /**
   * Remove a tier from the tier list
   *
   * @param tier tier to delete
   */
  void removeTier(Tier tier);

  /**
   * Remove an element from the tier list
   *
   * @param element to delete
   */
  void removeElement(TierElement element);

  /**
   * Move and append a ranked element to a specified tier
   *
   * @param tiered element to append
   * @param toTier destination
   */
  void moveElement(TierElement tiered, Tier toTier);

  /**
   * Move and a ranked element to a specified tier
   *
   * @param element  to move
   * @param toTier   destination
   * @param position to move to
   */
  void moveElement(TierElement element, Tier toTier, TierElement position);

  /**
   * Move and a ranked element to a specified tier
   *
   * @param element to move
   * @param toTier  destination
   * @param index   to move to
   */
  void moveElement(TierElement element, Tier toTier, int index);

  /**
   * Move a tier to a destination
   *
   * @param from source
   * @param to   destination
   */
  void moveTier(Tier from, Tier to);

  /**
   * Move a tier to a destination
   *
   * @param from    source
   * @param toIndex destination
   */
  void moveTier(Tier from, int toIndex);

  void setTierList(TierList tierList);

  void setTierListName(String name);

  void setTierName(Tier tier, String name);

  Optional<Tier> getTierByElement(TierElement element);

  Optional<TierElement> getElementByHash(String hashCode);

  Optional<Tier> getTierByHash(String hashCode);

  List<TierElement> getUnTiered();

  List<Tier> getTiers();

  String getTierListName();

  void saveTierList();

  void saveTierList(Path path);

  void exportTierList(SPTiered node);

  void exportTierList(SPTiered node, Path path);

  @Deprecated void saveTierListAs(String name);

  Optional<TierList> loadTierList(File file);

  String toString();

  String toString(TierStringFormat format);

  boolean elementExists(Long id);

  /**
   * Remove an unranked element from the tier list
   *
   * @param unTiered element to delete
   */
  @Deprecated
  void deleteUnTiered(TierElement unTiered);

  /**
   * Swaps the positions of two tiers
   *
   * @param a first tier
   * @param b second tier
   */
  @Deprecated
  void swapTiers(Tier a, Tier b);

  /**
   * Swaps the positions of two elements from a tier
   *
   * @param tier source of the elements to swap
   * @param a    first element
   * @param b    second element
   */
  @Deprecated
  void swapTiered(Tier tier, TierElement a, TierElement b);

  /**
   * Swaps the positions of two unranked elements
   *
   * @param a first element
   * @param b second element
   */
  @Deprecated
  void swapUnTiered(TierElement a, TierElement b);

  /**
   * Move and append a ranked element to a specified tier
   *
   * @param tiered element to append
   * @param toTier destination
   */
  @Deprecated
  void appendTiered(TierElement tiered, Tier toTier);

  /**
   * Move a ranked element to a specified tier and position
   *
   * @param tiered    element to append
   * @param toTier    destination
   * @param toElement position
   */
  @Deprecated
  void moveTiered(TierElement tiered, Tier toTier, TierElement toElement);

  /**
   * Move a ranked element to a specified tier and position
   *
   * @param tiered  element to append
   * @param toTier  destination
   * @param toIndex position
   */
  @Deprecated
  void moveTiered(TierElement tiered, Tier toTier, int toIndex);

  /**
   * Move an unranked element to a specified position
   *
   * @param unTiered  element to append
   * @param toElement position
   */
  @Deprecated
  void moveUnTiered(TierElement unTiered, TierElement toElement);

  /**
   * Move an unranked element to a specified position
   *
   * @param unTiered element to append
   * @param toIndex  position
   */
  @Deprecated
  void moveUnTiered(TierElement unTiered, int toIndex);

}
