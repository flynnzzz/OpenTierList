package net.flynn.opentierlist.controller;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import net.flynn.opentierlist.model.enums.*;
import net.flynn.opentierlist.model.exceptions.*;
import net.flynn.opentierlist.model.models.*;
import net.flynn.opentierlist.persistence.DataHandler;

/**
 * Main implementation of {@link TierListController}.
 * 
 * @author flynnz
 * @version 2.0.0
 * @since v0.0.0
 */
public class StandardTierListController implements TierListController {

  private TierList tierList;

  private DataHandler dataHandler;
  private String fileName;
  private Path savePath;

  // ---------------------------------- Ctors ----------------------------------//

  /**
   * Constructor that creates a controller for {@link TierList}.
   * 
   * Instanciates an {@link TierList} with the given parameter
   * 
   * @param tierList parameter to pass
   */
  public StandardTierListController(TierList tierList) {
    this.tierList = tierList;
    this.fileName = getTierListName() + ".json";
    this.savePath = Path.of(System.getProperty("user.home"), "Documents").resolve(fileName);
    this.dataHandler = new DataHandler();
  }

  /**
   * Constructor that creates a controller for {@link TierList}.
   * 
   * Instanciates an empty {@link TierList}
   */
  public StandardTierListController() {
    this(new TierList());
  }

  // ------------------------------- methods -------------------------------//

  // ------------------------------ ranking ------------------------------//

  @Override
  public void rank(TierElement element, Tier toTier) {
    try {
      tierList.rank(element, toTier);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void rank(TierElement element, Tier toTier, int toIndex) {
    try {
      tierList.rankInsert(element, toTier, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void unrank(TierElement element) {
    try {
      tierList.unrank(element);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void unrank(TierElement element, int toIndex) {
    try {
      tierList.unrankInsert(element, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  // ------------------------------ editing ------------------------------//

  @Override
  public void setTierListName(String name) {
    try {
      tierList.setTierListName(name);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void setTierName(Tier tier, String name) {
    try {
      tierList.setTierName(tierList.indexOf(tier), name);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void addTier(Tier tier) {
    try {
      tierList.addTier(tier);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void addDefaultTier() {
    tierList.addTier(new Tier());
  }

  @Override
  public void addToUnranked(TierElement element) {
    try {
      tierList.addToUnranked(element);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void removeTier(Tier tier) {
    try {
      tierList.removeTier(tier);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void removeTelement(TierElement element) {
    try {
      var tier = getTierByElement(element);
      if (element.isRanked() && tier.isPresent())
        tierList.removeFromTier(tier.get(), element);
      else if (!element.isRanked()) {
        tierList.removeFromUnranked(element);
      } else
        throw new TierNotFoundException("could not remove Telement: " + element);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void removeFromUnranked(TierElement element) {
    try {
      tierList.removeFromUnranked(element);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  // ------------------------------ swapping ------------------------------//

  @Override
  public void swapTiers(Tier a, Tier b) {
    try {
      tierList.swapTiers(a, b);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void swapTelements(Tier tier, TierElement a, TierElement b) {
    try {
      tierList.swapElements(tier, a, b);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void swapUnranked(TierElement a, TierElement b) {
    try {
      tierList.swapUnranked(a, b);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  // ------------------------------ moving ------------------------------//

  @Override
  public void moveTo(TierElement element, Tier toTier) {
    try {
      tierList.moveToTier(element, toTier);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void moveTo(TierElement element, Tier toTier, TierElement toElement) {
    try {
      int toIndex = toTier.getElements().indexOf(toElement);
      tierList.moveToTier(element, toTier, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void moveTo(TierElement element, Tier toTier, int toIndex) {
    try {
      tierList.moveToTier(element, toTier, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void moveUnranked(TierElement element, TierElement toElement) {
    try {
      int toIndex = tierList.getUnranked().indexOf(toElement);
      tierList.moveUnranked(element, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void moveUnranked(TierElement element, int toIndex) {
    try {
      tierList.moveUnranked(element, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void moveTierTo(Tier from, Tier to) {
    try {
      tierList.moveTierTo(from, to);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void moveTierTo(Tier from, int toIndex) {
    try {
      tierList.moveTierTo(from, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
    try {
      tierList.moveTierTo(from, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  // ------------------------------ persistence ------------------------------//

  @Override
  public void saveTierList() {
    try {
      dataHandler.save(savePath.toFile(), tierList);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void saveTierListTo(Path path) {
    savePath = path;
    saveTierList();
  }

  @Override
  public void saveTierListAs(String name) {
    fileName = name;
    savePath = savePath.getParent().resolve(fileName);
    saveTierList();
  }

  // ------------------------------ misc ------------------------------//

  @Override
  public String toString() {
    return tierList.toString();
  }

  @Override
  public String toString(TierStringFormat format) {
    return tierList.toString(format);
  }

  // ------------------------------ getters ------------------------------//

  @Override
  public String getTierListName() {
    return tierList.getTierListName();
  }

  @Override
  public List<TierElement> getUnranked() {
    return tierList.getUnranked();
  }

  @Override
  public List<Tier> getTiers() {
    return tierList.getTiers();
  }

  @Override
  public Optional<Tier> getTierByElement(TierElement e) {
    Optional<Tier> element = Optional.empty();

    try {
      element = tierList.getTiers().stream()
          .filter(t -> t.contains(e))
          .findFirst();

    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }

    return element;
  }

  @Override
  public Optional<TierElement> getElementByHash(String hashCode) {
    Optional<TierElement> telement = Optional.empty();

    try {

      telement = Stream
          .concat(tierList.getTiers().stream().flatMap(t -> t.getElements().stream()), tierList.getUnranked().stream())
          .filter(e -> String.valueOf(e.hashCode()).equals(hashCode))
          .findFirst();

      if (telement.isEmpty())
        throw new TelementNotFoundException();
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }

    return telement;
  }

  @Override
  public Optional<Tier> getTierByHash(String hashCode) {
    Optional<Tier> tier = Optional.empty();

    try {

      tier = tierList.getTiers().stream()
          .filter(t -> String.valueOf(t.hashCode()).contains(hashCode))
          .findFirst();

      if (tier.isEmpty())
        throw new TierNotFoundException();
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }

    return tier;
  }

  @Override
  public boolean telementExists(TierElement element) {

    return Stream
        .concat(
            tierList.getTiers()
                .stream()
                .flatMap(t -> t.getElements().stream()),
            tierList.getUnranked()
                .stream())
        .anyMatch(e -> e.equals(element));

  }

  @Override
  public boolean telementExistsById(Long id) {

    return Stream
        .concat(
            tierList.getTiers()
                .stream()
                .flatMap(t -> t.getElements().stream()),
            tierList.getUnranked()
                .stream())
        .anyMatch(e -> e.getId() == id);

  }

  @Override
  public boolean tierExists(Tier tier) {

    return getTiers().stream().anyMatch(t -> t.equals(tier));

  }
}
