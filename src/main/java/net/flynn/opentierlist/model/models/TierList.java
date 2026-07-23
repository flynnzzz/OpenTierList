package net.flynn.opentierlist.model.models;

import net.flynn.opentierlist.model.exceptions.*;
import static net.flynn.opentierlist.model.enums.TieredStatus.*;
import net.flynn.opentierlist.model.enums.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javafx.scene.paint.Color;

/**
 * A class representing the concept of a tier list
 * 
 * @author flynnz
 * @version 2.50
 * @since v0.0.0
 */
public class TierList {

  private String name;
  private final List<Tier> tiers;
  private final List<TierElement> unTiered;

  public static final String DEFAULT_TIERLIST_NAME = "New Tierlist";

  // ----- Ctors -----//

  /**
   * Constructs a {@link TierList} instance.
   * <p>
   * The tier list instance will be constructed with the given lists parameters.
   * 
   * @param name     the tier list's name
   * @param unTiered elements to rank
   * @param tiers    preset tiers
   * @throws IllegalArgumentException if name is blank
   */
  public TierList(String name, List<TierElement> unTiered, List<Tier> tiers) throws IllegalArgumentException {
    Objects.requireNonNull(name);
    Objects.requireNonNull(unTiered);
    Objects.requireNonNull(tiers);
    if (name.isBlank())
      throw new IllegalArgumentException("--- TierList name cannot be blank ---");

    this.name = name;
    this.unTiered = unTiered;
    this.tiers = tiers;
  }

  /**
   * Constructs {@link TierList} instance.
   * <p>
   * The tier list instance will be constructed with the given lists parameters.
   * 
   * @param name     the tier list's name
   * @param unTiered elements to rank
   * @throws IllegalArgumentException if name is blank
   */
  public TierList(String name, List<TierElement> unTiered) throws IllegalArgumentException {
    this(name, unTiered, new ArrayList<>());
  }

  /**
   * Constructs a {@link TierList} instance.
   * <p>
   * The tier list instance will be constructed with the given elements to rank.
   * 
   * @param unTiered elements to rank
   */
  public TierList(List<TierElement> unTiered) {
    this(DEFAULT_TIERLIST_NAME, unTiered);
  }

  /**
   * Constructs a {@link TierList} instance.
   * <p>
   * The tier list instance will be constructed with the given name.
   * 
   * @param name name of the tier list
   * @throws IllegalArgumentException if name is blank
   */
  public TierList(String name) throws IllegalArgumentException {
    this(name, new ArrayList<TierElement>());
  }

  /**
   * Constructs an empty {@link TierList} instance.
   */
  public TierList() {
    this(new ArrayList<TierElement>());
  }

  // ----- ranking -----//

  /**
   * Ranks a {@link TierElement}
   * 
   * @param element     element to rank
   * @param toTierIndex tier to rank to
   * @throws TierNotFoundException if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   */
  public void tier(TierElement element, int toTierIndex) throws TierNotFoundException, TierElementNotFoundException {
    removeUnTiered(element);
    addToTier(toTierIndex, element);
  }

  /**
   * Ranks a {@link TierElement}
   *
   * @param element element to rank
   * @param toTier  tier to rank to
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   */
  public void tier(TierElement element, Tier toTier) throws TierNotFoundException, TierElementNotFoundException {
    removeUnTiered(element);
      addToTier(toTier, element);
  }

  /**
   * Unranks a {@link TierElement}
   *
   * @param element element to unrank
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   */
  public void unTier(TierElement element) throws TierNotFoundException, TierElementNotFoundException {
    Tier fromTier = findTierByElement(element);

    verifyElementExistenceInTier(element, fromTier);

    addUnTiered(element);
    element.changeTo(UNTIERED);
      removeFromTier(fromTier, element);
  }

  /**
   * Ranks a {@link TierElement} to a specified position
   * 
   * @param element     element to rank
   * @param toTierIndex tier to rank to
   * @param insertIndex destination index
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   */
  public void tierInsert(TierElement element, int toTierIndex, int insertIndex)
      throws TierNotFoundException, TierElementNotFoundException {

    removeUnTiered(element);

    addToTier(toTierIndex, element);
    tiers.get(insertIndex).moveTo(element, insertIndex);
  }

  /**
   * Ranks a {@link TierElement} to a specified position
   *
   * @param element     element to rank
   * @param toTier      tier to rank to
   * @param insertIndex destination index
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   */
  public void tierInsert(TierElement element, Tier toTier, int insertIndex)
      throws TierNotFoundException, TierElementNotFoundException {
    removeUnTiered(element);

    boolean added = addToTier(toTier, element);

    toTier.moveTo(element, insertIndex);
  }

  /**
   * Unranks a {@link TierElement} to a specified position
   *
   * @param element     element to rank
   * @param insertIndex destination index
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   */
  public void unTierInsert(TierElement element, int insertIndex)
      throws TierNotFoundException, TierElementNotFoundException {
    Tier fromTier = findTierByElement(element);

    verifyElementExistenceInTier(element, fromTier);

    addUnTiered(element, insertIndex);
    removeFromTier(fromTier, element);
  }

  // ----- editing -----//

  public void addTier(Tier tier) {
      tiers.add(tier);
  }

  public void addUnTiered(TierElement element) {
    unTiered.add(element);
    element.changeTo(UNTIERED);
  }

  public void addUnTiered(TierElement element, int index) {
    unTiered.add(index, element);
    element.changeTo(UNTIERED);
  }

  public void addToTier(int tierIndex, TierElement element) throws TierNotFoundException {

    verifyTierExistence(tierIndex);

    element.changeTo(TIERED);
      tiers.get(tierIndex).add(element);
  }

  public boolean addToTier(Tier tier, TierElement element) throws TierNotFoundException {

    verifyTierExistence(tier);

    element.changeTo(TIERED);
    return tier.add(element);
  }

  public Tier removeTier(int tierIndex) throws TierNotFoundException {

    verifyTierExistence(tierIndex);

    return tiers.remove(tierIndex);
  }

  public void removeTier(Tier tier) throws TierNotFoundException {

    verifyTierExistence(tiers.indexOf(tier));

      tiers.remove(tier);
  }

  public void removeUnTiered(TierElement element) throws TierElementNotFoundException {

    verifyElementExistence(element, unTiered);

    unTiered.remove(element);
    element.changeTo(TIERED);
  }

  public void removeFromTier(int tierIndex, TierElement element)
      throws TierNotFoundException, TierElementNotFoundException {

    verifyElementExistenceInTier(element, tierIndex);

    if (!tiers.get(tierIndex).remove(element))
      throw new TierElementNotFoundException();
  }

  public void removeFromTier(Tier tier, TierElement element)
      throws TierNotFoundException, TierElementNotFoundException {

    verifyElementExistenceInTier(element, tier);

    if (!tier.remove(element))
      throw new TierElementNotFoundException();
  }

  // ----- swapping -----//

  public void swapTiers(int a, int b) throws TierNotFoundException {
    try {
      Collections.swap(tiers, a, b);
    } catch (IndexOutOfBoundsException e) {
      throw new TierNotFoundException();
    }
  }

  public void swapTiers(Tier a, Tier b) throws TierNotFoundException {
    try {
      Collections.swap(tiers, tiers.indexOf(a), tiers.indexOf(b));
    } catch (IndexOutOfBoundsException e) {
      throw new TierNotFoundException();
    }
  }

  /**
   * Swaps two elements from within a tier
   * 
   * @param tierIndex destination index
   * @param a         first element
   * @param b         second element
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   */
  public void swapTiered(int tierIndex, TierElement a, TierElement b)
      throws TierNotFoundException, TierElementNotFoundException {

    verifyElementExistenceInTier(a, tierIndex);
    verifyElementExistenceInTier(b, tierIndex);

    var tier = tiers.get(tierIndex);
    tier.swap(a, b);
  }

  /**
   * Swaps two elements from within a tier
   * 
   * @param tier destination
   * @param a    first element
   * @param b    second element
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   */
  public void swapTiered(Tier tier, TierElement a, TierElement b)
      throws TierNotFoundException, TierElementNotFoundException {

    verifyElementExistenceInTier(a, tier);
    verifyElementExistenceInTier(b, tier);

    tier.swap(a, b);
  }

  /**
   * Swaps two elements from the unTiered elements list
   * 
   * @param a first element
   * @param b second element
   * @throws TierElementNotFoundException if tier element doesn't exist
   */
  public void swapUnTiered(TierElement a, TierElement b) throws TierElementNotFoundException {

    int index1 = verifyElementExistence(a, unTiered),
        index2 = verifyElementExistence(b, unTiered);

    Collections.swap(unTiered, index1, index2);
  }

  // ----- utils -----//

  /**
   * Returns the index of a tier
   * 
   * @param tier tier to search the index for
   * @return the tier's index
   * @throws TierNotFoundException        if tier doesn't exist
   */
  public int indexOf(Tier tier) throws TierNotFoundException {
    return verifyTierExistence(tiers.indexOf(tier));
  }

  public int tiersQuantity() {
    return tiers.size();
  }

  // ----- moving -----//

  /**
   * Moves elements between tiers
   * 
   * @param element     element to move
   * @param toTierIndex tier destination index
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   */
  public void moveToTier(TierElement element, int toTierIndex)
      throws TierElementNotFoundException, TierNotFoundException {

    verifyTierExistence(toTierIndex);

    int fromTierIndex = findTierIndexByElement(element);
    if (tiers.get(fromTierIndex).remove(element))
      tiers.get(toTierIndex).add(element);
  }

  /**
   * Moves elements between tiers
   *
   * @param element element to move
   * @param toTier  tier destination index
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   */
  public void moveToTier(TierElement element, Tier toTier) throws TierElementNotFoundException, TierNotFoundException {

    verifyTierExistence(toTier);

    if (findTierByElement(element).remove(element)) {
        toTier.add(element);
    }
  }

  /**
   * Moves elements between tiers to a specified position
   * 
   * @param element        element to move
   * @param toTierIndex    tier destination index
   * @param toElementIndex destination position index
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   */
  public void moveToTier(TierElement element, int toTierIndex, int toElementIndex)
      throws TierElementNotFoundException, TierNotFoundException {

    verifyTierExistence(toTierIndex);

    int fromTierIndex = findTierIndexByElement(element);
    if (tiers.get(fromTierIndex).remove(element)) {
      tiers.get(toTierIndex).add(element);
      tiers.get(toTierIndex).moveTo(element, toElementIndex);
    }
  }

  /**
   * Moves elements between tiers to a specified position
   *
   * @param element        element to move
   * @param toTier         tier destination index
   * @param toElementIndex destination position index
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   */
  public void moveToTier(TierElement element, Tier toTier, int toElementIndex)
      throws TierElementNotFoundException, TierNotFoundException {

    verifyTierExistence(toTier);

    int fromTierIndex = findTierIndexByElement(element);
    if (tiers.get(fromTierIndex).remove(element)) {
      toTier.add(element);
      toTier.moveTo(element, toElementIndex);
    }
  }

  public void moveUnTiered(TierElement element, int toElementIndex)
      throws TierElementNotFoundException, TierNotFoundException {

    verifyElementExistence(element, unTiered);

    unTiered.remove(element);
    unTiered.add(toElementIndex, element);
  }

  public void moveTierTo(Tier from, Tier to) throws TierNotFoundException {

    int indexFrom = verifyTierExistence(from),
        indexTo = verifyTierExistence(to);

    tiers.remove(indexFrom);
    tiers.add(indexTo, from);
  }

  public void moveTierTo(Tier from, int toIndex) throws TierNotFoundException {

    int indexFrom = verifyTierExistence(from);

    tiers.remove(indexFrom);
    tiers.add(toIndex, from);
  }

  // ----- exceptions -----//

  private int verifyTierExistence(int tierIndex) throws TierNotFoundException {
    var exception = new TierNotFoundException("Tier at index \"" + tierIndex + "\" not found");
    try {
      if (tierIndex == -1)
        throw exception;
      else
        return tierIndex;
    } catch (IndexOutOfBoundsException indexException) {
      throw exception;
    }
  }

  private int verifyTierExistence(Tier tier) throws TierNotFoundException {
    var exception = new TierNotFoundException("Tier \"" + tier + "\" not found");
    int tierIndex = tiers.indexOf(tier);
    try {
      if (tierIndex == -1)
        throw exception;
      else
        return tierIndex;
    } catch (IndexOutOfBoundsException indexException) {
      throw exception;
    }
  }

  private int verifyElementExistence(TierElement element, List<TierElement> inList) throws TierElementNotFoundException {
    var exception = new TierElementNotFoundException("Element \"" + element + "\" not found in list \"" + inList + "\"");
    try {
      int elementIndex = inList.indexOf(element);
      if (elementIndex == -1)
        throw exception;
      return elementIndex;
    } catch (IndexOutOfBoundsException physException) {
      throw exception;
    }
  }

  private void verifyElementExistenceInTier(TierElement element, int tierIndex)
      throws TierElementNotFoundException, TierNotFoundException {
    verifyTierExistence(tierIndex);
    verifyElementExistence(element, tiers.get(tierIndex).getTiered());
  }

  private void verifyElementExistenceInTier(TierElement element, Tier tier)
      throws TierElementNotFoundException, TierNotFoundException {
    verifyTierExistence(tier);
    verifyElementExistence(element, tier.getTiered());
  }

  private Tier findTierByElement(TierElement element) throws TierElementNotFoundException {

    var matching = tiers.stream()
        .filter(tier -> tier.contains(element))
        .toList();

    if (matching.size() != 1)
      throw new TierElementNotFoundException();
    return matching.getFirst();
  }

  private int findTierIndexByElement(TierElement element) throws TierElementNotFoundException {
    return tiers.indexOf(findTierByElement(element));
  }

  // ----- setters and getters -----//

  public void setTierListName(String name) throws IllegalArgumentException {
    Objects.requireNonNull(name);
    if (name.isBlank())
      throw new IllegalArgumentException("Tier list's name must not be blank");
    this.name = name;
  }

  public void setTierName(int tierIndex, String name) {
    var oldColor = tiers.get(tierIndex).getColor();
    setTierHeader(tierIndex, new TierHeader(name, Color.valueOf(oldColor)));
  }

  public void setTierColor(int tierIndex, Color color) {
    var oldName = tiers.get(tierIndex).getName();
    setTierHeader(tierIndex, new TierHeader(oldName, color));
  }

  private void setTierHeader(int tierIndex, TierHeader th) throws TierNotFoundException {
    Objects.requireNonNull(th);
    Objects.requireNonNull(th.name());
    Objects.requireNonNull(th.color());

    Tier t = tiers.get(tierIndex);
    t.setName(th.name());
    t.setColor(th.color().toString());
  }

  public String getTierListName() {
    return name;
  }

  public String getTierName(int tierIndex) {
    return tiers.get(tierIndex).getName();
  }

  public String getTierColor(int tierIndex) {
    return tiers.get(tierIndex).getColor();
  }

  public List<TierElement> getUnTiered() {
    return List.copyOf(unTiered);
  };

  public List<Tier> getTiers() {
    return List.copyOf(tiers);
  }

  // ----- hashCode, equals and toString -----//

  @Override
  public int hashCode() {
    return Objects.hash(tiers, name, unTiered);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof TierList other)) {
      return false;
    }
      return Objects.equals(tiers, other.tiers) && Objects.equals(name, other.name)
        && Objects.equals(unTiered, other.unTiered);
  }

  @Override
  public String toString() {
    return this.toString(TierStringFormat.COMPACT);
  }

  public String toString(TierStringFormat format) {
    var sb = new StringBuilder();
    sb.append(this.name).append(System.lineSeparator());
    sb.append(System.lineSeparator());

    tiers.stream()
        .map(tier -> tier.toString(format))
        .forEach(tierString -> {
          sb.append(tierString);
          sb.append(System.lineSeparator());
          sb.append(System.lineSeparator());
        });

    sb.append("Unranked:").append(System.lineSeparator()).append(unTiered.toString());
    return sb.toString();
  }

  public boolean contains(TierElement element) {
    return tiers.stream()
        .anyMatch(tier -> tier.contains(element))
        || unTiered.contains(element);
  }

  public boolean contains(Tier tier) {
    return tiers.contains(tier);
  }
}
