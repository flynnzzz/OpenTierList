package net.flynn.opentierlist.model.models;

import static net.flynn.opentierlist.model.enums.TieredStatus.TIERED;
import static net.flynn.opentierlist.model.enums.TieredStatus.UNTIERED;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.flynn.opentierlist.model.enums.TierStringFormat;
import net.flynn.opentierlist.model.enums.TieredStatus;
import net.flynn.opentierlist.model.exceptions.TierElementNotFoundException;
import net.flynn.opentierlist.model.exceptions.TierNotFoundException;

/**
 * A class representing the concept of a tier list
 * 
 * @author flynnz
 * @version 2.50
 * @since v0.0.0
 */
public class TierList {

  private String tierListName;
  private final List<Tier> tiers;
  private final List<TierElement> unTiered;

  public static final String DEFAULT_TIERLIST_NAME = "New Tierlist";

  // ----- Ctors -----//

  /**
   * Constructs a {@link TierList} instance
   * <p>
   * The tier list instance will be constructed with the given parameters
   * 
   * @param tierListName the tier list's name
   * @param unTiered     elements to rank
   * @param tiers        preset tiers
   * @throws IllegalArgumentException if name is blank
   */
  public TierList(String tierListName, List<TierElement> unTiered, List<Tier> tiers) throws IllegalArgumentException {
    this.tierListName = Objects.requireNonNull(tierListName);
    this.unTiered = Objects.requireNonNull(unTiered);
    this.tiers = Objects.requireNonNull(tiers);
    if (tierListName.isBlank())
      throw new IllegalArgumentException("--- TierList name cannot be blank ---");
  }

  /**
   * Constructs {@link TierList} instance
   * <p>
   * The tier list instance will be constructed with the given parameters
   * 
   * @param tierListName the tier list's name
   * @param unTiered     elements to rank
   * @throws IllegalArgumentException if name is blank
   */
  public TierList(String tierListName, List<TierElement> unTiered) throws IllegalArgumentException {
    this(tierListName, unTiered, new ArrayList<>());
  }

  /**
   * Constructs a {@link TierList} instance
   * <p>
   * The tier list instance will be constructed with the given elements to rank
   * 
   * @param unTiered elements to rank
   */
  public TierList(List<TierElement> unTiered) {
    this(DEFAULT_TIERLIST_NAME, unTiered);
  }

  /**
   * Constructs a {@link TierList} instance
   * <p>
   * The tier list instance will be constructed with the given name
   * 
   * @param tierListName name of the tier list
   * @throws IllegalArgumentException if name is blank
   */
  public TierList(String tierListName) throws IllegalArgumentException {
    this(tierListName, new ArrayList<>());
  }

  /**
   * Constructs an empty {@link TierList} instance
   */
  public TierList() {
    this(new ArrayList<>());
  }

  @JsonCreator
  public TierList(
      @JsonProperty("tiers") List<Tier> tiers,
      @JsonProperty("unTiered") List<TierElement> unTiered) {
    this.tiers = tiers;
    this.unTiered = unTiered;
  }

  // ----- ranking -----//

  /**
   * Ranks a {@link TierElement}
   * 
   * @param unTiered  element to rank
   * @param tierIndex tier to rank to
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   * @throws IllegalArgumentException     if the element is already tiered
   */
  public void tier(TierElement unTiered, int tierIndex) throws TierNotFoundException, TierElementNotFoundException {

    if (unTiered.getStatus() != TieredStatus.UNTIERED)
      throw new IllegalArgumentException("--- Cannot tier: " + unTiered + " as it's already tiered ---");

    removeElement(unTiered);
    addElement(unTiered, tiers.get(tierIndex));

    unTiered.changeTo(TieredStatus.TIERED);
  }

  /**
   * Ranks a {@link TierElement}
   *
   * @param unTiered element to rank
   * @param tier     tier to rank to
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   * @throws IllegalArgumentException     if the element is already tiered
   */
  public void tier(TierElement unTiered, Tier tier) throws TierNotFoundException, TierElementNotFoundException {

    if (unTiered.getStatus() != TieredStatus.UNTIERED)
      throw new IllegalArgumentException("--- Cannot tier: " + unTiered + " as it's already tiered ---");
    if (tier.equalsTier(Tier.UNTIERED))
      return;

    removeElement(unTiered);
    addElement(unTiered, tier);

    unTiered.changeTo(TieredStatus.TIERED);
  }

  /**
   * Ranks a {@link TierElement} to a specified position
   *
   * @param unTiered element to rank
   * @param tier     tier to rank to
   * @param position destination
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   * @throws IllegalArgumentException     if the element is already tiered
   */
  public void tierInsert(TierElement unTiered, Tier tier, TierElement position)
      throws TierNotFoundException, TierElementNotFoundException {
    tier(unTiered, tier);
    tier.move(unTiered, position);
  }

  /**
   * Ranks a {@link TierElement} to a specified position
   *
   * @param unTiered element to rank
   * @param tier     tier to rank to
   * @param index    destination index
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   * @throws IllegalArgumentException     if the element is already tiered
   */
  public void tierInsert(TierElement unTiered, Tier tier, int index)
      throws TierNotFoundException, TierElementNotFoundException {
    tier(unTiered, tier);
    tier.move(unTiered, index);
  }

  /**
   * Unranks a {@link TierElement}
   *
   * @param tiered element to unrank
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   * @throws IllegalArgumentException     if the element is already untiered
   */
  public void unTier(TierElement tiered) throws TierNotFoundException, TierElementNotFoundException {

    if (tiered.getStatus() != TieredStatus.TIERED)
      throw new IllegalArgumentException("--- Cannot untier: " + tiered + " as it's already untiered ---");

    removeElement(tiered);
    addElement(tiered, Tier.UNTIERED);

    tiered.changeTo(UNTIERED);
  }

  /**
   * Unranks a {@link TierElement} to a specified position
   *
   * @param tiered   element to rank
   * @param position destination
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   * @throws IllegalArgumentException     if the element is already untiered
   */
  public void unTierInsert(TierElement tiered, TierElement position)
      throws TierNotFoundException, TierElementNotFoundException {
    unTier(tiered);
    moveElement(tiered, Tier.UNTIERED, position);
  }

  /**
   * Unranks a {@link TierElement} to a specified position
   *
   * @param tiered element to rank
   * @param index  destination index
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   * @throws IllegalArgumentException     if the element is already untiered
   */
  public void unTierInsert(TierElement tiered, int index)
      throws TierNotFoundException, TierElementNotFoundException {
    unTier(tiered);
    moveElement(tiered, Tier.UNTIERED, index);
  }

  // ----- editing -----//

  public void addTier(Tier tier) {
    tiers.add(tier);
  }

  public void addElement(TierElement element, Tier toTier) {
    if (toTier.equalsTier(Tier.UNTIERED)) {
      unTiered.add(element);
      return;
    }

    if (tiers.indexOf(toTier) == -1)
      throw new TierNotFoundException(
          "--- Could not add " + element + " to tier: " + toTier + " as it doesn't exists ---");

    toTier.add(element);
  }

  public void addAllElements(Set<TierElement> elements, Tier toTier) {
    elements.forEach(e -> addElement(e, toTier));
  }

  public void addElement(TierElement element, Tier toTier, TierElement position) {
    addElement(element, toTier);
    toTier.move(element, toTier.getTiered().indexOf(position));
  }

  public void addElement(TierElement element, Tier toTier, int index) {
    addElement(element, toTier);
    toTier.move(element, index);
  }

  public void removeTier(int index) throws TierNotFoundException {
    try {
      tiers.remove(index);

    } catch (IndexOutOfBoundsException _) {
      throw new TierNotFoundException("--- Indexes out of bounds for removal, max = " + tiersQuantity() + " ---");

    } catch (UnsupportedOperationException _) {
      throw new TierNotFoundException("--- Unsupported removel operation ---");
    }
  }

  public void removeTier(Tier tier) throws TierNotFoundException {
    try {
      tiers.remove(tier);

    } catch (IndexOutOfBoundsException _) {
      throw new TierNotFoundException("--- Indexes out of bounds for removal, max = " + tiersQuantity() + " ---");

    } catch (UnsupportedOperationException _) {
      throw new TierNotFoundException("--- Unsupported removel operation ---");
    }
  }

  public void removeElement(TierElement element) {

    if (unTiered.contains(element)) {
      unTiered.remove(element);
      return;
    }

    var potentialTier = tiers.stream()
        .filter(t -> t.contains(element))
        .findFirst();

    if (potentialTier.isPresent())
      potentialTier.get().remove(element);
  }

  public void removeAllElements(Set<TierElement> elements) {
    elements.forEach(this::removeElement);
  }
  // ----- swapping -----//

  public void swapTiers(int src, int dest) throws TierNotFoundException {
    try {
      Collections.swap(tiers, src, dest);
    } catch (IndexOutOfBoundsException _) {
      throw new TierNotFoundException("--- Indexes out of bounds for swapping, max = " + tiersQuantity() + " ---");
    }
  }

  public void swapTiers(Tier src, Tier dest) throws TierNotFoundException {
    try {
      swapTiers(indexOf(src), indexOf(dest));
    } catch (IndexOutOfBoundsException _) {
      throw new TierNotFoundException("--- Indexes out of bounds for swapping, max = " + tiersQuantity() + " ---");
    }
  }

  public void swapElements(TierElement src, TierElement dest) throws TierElementNotFoundException {

    Tier srcTier = tierByElement(src), destTier = tierByElement(dest);
    int srcIndex = srcTier.indexOf(src), destIndex = destTier.indexOf(dest);

    removeAllElements(Set.of(src, dest));

    srcTier.add(dest);
    srcTier.move(dest, srcIndex);

    destTier.add(src);
    destTier.move(src, destIndex);
  }

  // ----- utils -----//

  /**
   * Returns the index of a tier
   * 
   * @param tier to search the index for
   * @return the tier's index
   * @throws TierNotFoundException if tier doesn't exist
   */
  public int indexOf(Tier tier) throws TierNotFoundException {
    final var i = tiers.indexOf(tier);
    if (i == -1)
      throw new TierNotFoundException("--- Could not find index of tier: " + tier.toString(TierStringFormat.COMPACT));
    return i;
  }

  /**
   * Returns the index of a tier
   * 
   * @param element to search the index for
   * @return the element's index within it's tier
   * @throws TierNotFoundException if tier doesn't exist
   */
  public int indexOf(TierElement element) throws TierNotFoundException {
    final var i = tiers.stream()
        .filter(t -> t.contains(element))
        .map(t -> t.getTiered().indexOf(element))
        .findFirst();
    if (i.isEmpty() || i.get() == -1)
      throw new TierNotFoundException(
          "--- Could not find index of tier: " + element.toString(TierStringFormat.COMPACT));
    return i.get();
  }

  private Tier tierByElement(TierElement element) throws TierElementNotFoundException {

    if (unTiered.contains(element))
      return Tier.UNTIERED;

    var matching = tiers.stream()
        .filter(tier -> tier.contains(element))
        .findFirst();

    if (matching.isEmpty())
      throw new TierElementNotFoundException("--- No tier contains element: " + element + " ---");

    return matching.get();
  }

  public int tiersQuantity() {
    return tiers.size();
  }

  public boolean contains(TierElement element) {

    return Stream
        .concat(
            getTiers()
                .stream()
                .flatMap(t -> t.getTiered().stream()),

            getUnTiered()
                .stream())
        .anyMatch(e -> e.equals(element));

  }

  public boolean contains(Tier tier) {
    return tiers.contains(tier);
  }

  // ----- moving -----//

  public void moveTier(Tier from, Tier to) throws TierNotFoundException {

    int fromIndex, toIndex;
    if ((fromIndex = tiers.indexOf(from)) == -1)
      throw new TierNotFoundException("--- Index out of bounds 'from': " + fromIndex + " ---");
    if ((toIndex = tiers.indexOf(to)) == -1)
      throw new TierNotFoundException("--- Index out of bounds 'to': " + toIndex + " ---");

    tiers.remove(fromIndex);
    tiers.add(toIndex, from);
  }

  public void moveTier(Tier from, int toIndex) throws TierNotFoundException {

    int fromIndex;
    if ((fromIndex = tiers.indexOf(from)) == -1)
      throw new TierNotFoundException("--- Index out of bounds 'from': " + fromIndex + " ---");

    tiers.remove(fromIndex);
    tiers.add(toIndex, from);
  }

  public void moveElement(TierElement element, Tier toTier) {
    if (!this.contains(element))
      throw new TierElementNotFoundException("--- Element to move: " + element + " not found ---");

    if (!this.contains(toTier))
      throw new TierNotFoundException("--- Tier to move to: " + toTier + " not found ---");

    removeElement(element);
    addElement(element, toTier);
  }

  public void moveElement(TierElement element, Tier toTier, TierElement position) {
    if (!this.contains(position))
      throw new TierElementNotFoundException("--- Position to move to: " + position + " not found ---");

    moveElement(element, toTier);
    toTier.move(element, toTier.indexOf(position));
  }

  public void moveElement(TierElement element, Tier toTier, int index) {
    if (toTier.elementsCount() <= index)
      throw new TierElementNotFoundException("--- Index to move to: " + index + " not found ---");

    moveElement(element, toTier);
    toTier.move(element, index);
  }

  // ----- setters and getters -----//

  public void setTierListName(String name) throws IllegalArgumentException {
    this.tierListName = Objects.requireNonNull(name);
    if (name.isBlank())
      throw new IllegalArgumentException("--- The tier list's name cannot be blank ---");
  }

  public void setTierName(int tierIndex, String name) {
    var oldColor = tiers.get(tierIndex).getColor();
    setTierHeader(tierIndex, new TierHeader(name, oldColor));
  }

  public void setTierColor(int tierIndex, String color) {
    var oldName = tiers.get(tierIndex).getName();
    setTierHeader(tierIndex, new TierHeader(oldName, color));
  }

  private void setTierHeader(int tierIndex, TierHeader tierHeader) throws TierNotFoundException {
    Objects.requireNonNull(tierHeader);
    var tier = tiers.get(tierIndex);
    tier.setName(tierHeader.name());
    tier.setColor(tierHeader.color().toString());
  }

  public String getTierListName() {
    return tierListName;
  }

  public String getTierName(int tierIndex) {
    return tiers.get(tierIndex).getName();
  }

  public String getTierColor(int tierIndex) {
    return tiers.get(tierIndex).getColor();
  }

  public List<TierElement> getUnTiered() {
    return List.copyOf(unTiered);
  }

  public List<Tier> getTiers() {
    return List.copyOf(tiers);
  }

  // ----- hashCode, equals and toString -----//

  @Override
  public int hashCode() {
    return Objects.hash(tiers, tierListName, unTiered);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof TierList other)) {
      return false;
    }
    return Objects.equals(tiers, other.tiers) && Objects.equals(tierListName, other.tierListName)
        && Objects.equals(unTiered, other.unTiered);
  }

  @Override
  public String toString() {
    return this.toString(TierStringFormat.COMPACT);
  }

  public String toString(TierStringFormat format) {
    var sb = new StringBuilder();
    sb.append(this.tierListName).append(System.lineSeparator());
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

  @Deprecated
  public void removeUnTiered(TierElement element) throws TierElementNotFoundException {
    verifyElementExistence(element, unTiered);
    unTiered.remove(element);
    element.changeTo(TIERED);
  }

  @Deprecated
  public void removeFromTier(int tierIndex, TierElement element)
      throws TierNotFoundException, TierElementNotFoundException {
    verifyElementExistenceInTier(element, tierIndex);
    if (!tiers.get(tierIndex).remove(element))
      throw new TierElementNotFoundException();
  }

  @Deprecated
  public void removeFromTier(Tier tier, TierElement element)
      throws TierNotFoundException, TierElementNotFoundException {
    verifyElementExistenceInTier(element, tier);
    if (!tier.remove(element))
      throw new TierElementNotFoundException();
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
  @Deprecated
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
  @Deprecated
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
  @Deprecated
  public void swapUnTiered(TierElement a, TierElement b) throws TierElementNotFoundException {

    int index1 = verifyElementExistence(a, unTiered),
        index2 = verifyElementExistence(b, unTiered);

    Collections.swap(unTiered, index1, index2);
  }

  @Deprecated
  public void addUnTiered(TierElement element) {
    unTiered.add(element);
    element.changeTo(UNTIERED);
  }

  @Deprecated
  public void addUnTiered(TierElement element, int index) {
    unTiered.add(index, element);
    element.changeTo(UNTIERED);
  }

  @Deprecated
  public void addToTier(int tierIndex, TierElement element) throws TierNotFoundException {
    verifyTierExistence(tierIndex);
    element.changeTo(TIERED);
    tiers.get(tierIndex).add(element);
  }

  @Deprecated
  public void addToTier(Tier tier, TierElement element) throws TierNotFoundException {
    verifyTierExistence(tier);
    element.changeTo(TIERED);
    tier.add(element);
  }

  @Deprecated
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

  @Deprecated
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

  @Deprecated
  private int verifyElementExistence(TierElement element, List<TierElement> inList)
      throws TierElementNotFoundException {
    var exception = new TierElementNotFoundException(
        "Element \"" + element + "\" not found in list \"" + inList + "\"");
    try {
      int elementIndex = inList.indexOf(element);
      if (elementIndex == -1)
        throw exception;
      return elementIndex;
    } catch (IndexOutOfBoundsException physException) {
      throw exception;
    }
  }

  @Deprecated
  private void verifyElementExistenceInTier(TierElement element, int tierIndex)
      throws TierElementNotFoundException, TierNotFoundException {
    verifyTierExistence(tierIndex);
    verifyElementExistence(element, tiers.get(tierIndex).getTiered());
  }

  @Deprecated
  private void verifyElementExistenceInTier(TierElement element, Tier tier)
      throws TierElementNotFoundException, TierNotFoundException {
    verifyTierExistence(tier);
    verifyElementExistence(element, tier.getTiered());
  }

  @Deprecated
  public void moveUnTiered(TierElement element, int toElementIndex)
      throws TierElementNotFoundException, TierNotFoundException {

    verifyElementExistence(element, unTiered);

    unTiered.remove(element);
    unTiered.add(toElementIndex, element);
  }

  /**
   * Moves elements between tiers
   * 
   * @param element     element to move
   * @param toTierIndex tier destination index
   * @throws TierNotFoundException        if tier doesn't exist
   * @throws TierElementNotFoundException if tier element doesn't exist
   */
  @Deprecated
  public void moveToTier(TierElement element, int toTierIndex)
      throws TierElementNotFoundException, TierNotFoundException {

    verifyTierExistence(toTierIndex);

    int fromTierIndex = tiers.indexOf(tierByElement(element));
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
  @Deprecated
  public void moveToTier(TierElement element, Tier toTier) throws TierElementNotFoundException, TierNotFoundException {

    verifyTierExistence(toTier);

    if (tierByElement(element).remove(element)) {
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
  @Deprecated
  public void moveToTier(TierElement element, int toTierIndex, int toElementIndex)
      throws TierElementNotFoundException, TierNotFoundException {

    verifyTierExistence(toTierIndex);

    int fromTierIndex = tiers.indexOf(tierByElement(element));
    if (tiers.get(fromTierIndex).remove(element)) {
      tiers.get(toTierIndex).add(element);
      tiers.get(toTierIndex).move(element, toElementIndex);
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
  @Deprecated
  public void moveToTier(TierElement element, Tier toTier, int toElementIndex)
      throws TierElementNotFoundException, TierNotFoundException {

    verifyTierExistence(toTier);

    int fromTierIndex = tiers.indexOf(tierByElement(element));
    if (tiers.get(fromTierIndex).remove(element)) {
      toTier.add(element);
      toTier.move(element, toElementIndex);
    }
  }
}
