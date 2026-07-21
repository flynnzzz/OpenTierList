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
  public void rank(Telement e, Tier toTier) {
    try {
      tierList.rank(e, toTier);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void rank(int toIndex, Telement e, Tier toTier) {
    try {
      tierList.rankInsert(e, toTier, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void unrank(Telement e) {
    try {
      tierList.unrank(e);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void unrank(int toIndex, Telement e) {
    try {
      tierList.unrankInsert(e, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  // ------------------------------ editing ------------------------------//

  @Override
  public void setTierListName(String name) {
    try {
      tierList.setTierListName(name);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void setTierName(Tier tier, String name) {
    try {
      tierList.setTierName(tierList.indexOf(tier), name);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void addTier(Tier t) {
    try {
      tierList.addTier(t);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void addDefaultTier() {
    tierList.addTier(new Tier());
  }

  @Override
  public void addToUnranked(Telement e) {
    try {
      tierList.addToUnranked(e);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void removeTier(Tier t) {
    try {
      tierList.removeTier(t);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void removeTelement(Telement e) {
    try {
      var tier = getTierByElement(e);
      if (e.isRanked() && tier.isPresent())
        tierList.removeFromTier(tier.get(), e);
      else if (!e.isRanked()) {
        tierList.removeFromUnranked(e);
      } else
        throw new TierNotFoundException("could not remove Telement: " + e);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void removeFromUnranked(Telement e) {
    try {
      tierList.removeFromUnranked(e);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  // ------------------------------ swapping ------------------------------//

  @Override
  public void swapTiers(Tier a, Tier b) {
    try {
      tierList.swapTiers(a, b);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void swapTelements(Tier tier, Telement a, Telement b) {
    try {
      tierList.swapElements(tier, a, b);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void swapUnranked(Telement a, Telement b) {
    try {
      tierList.swapUnranked(a, b);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  // ------------------------------ moving ------------------------------//

  @Override
  public void moveTo(Telement e, Tier toTier) {
    try {
      tierList.moveToTier(toTier, e);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void moveTo(Telement e, Tier toTier, Telement toElement) {
    try {
      int toIndex = toTier.getElements().indexOf(toElement);
      tierList.moveToTier(toTier, e, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void moveTo(Telement e, Tier toTier, int toIndex) {
    try {
      tierList.moveToTier(toTier, e, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void moveUnranked(Telement e, Telement toElement) {
    try {
      int toIndex = tierList.getUnranked().indexOf(toElement);
      tierList.moveUnranked(e, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void moveUnranked(Telement e, int toIndex) {
    try {
      tierList.moveUnranked(e, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void moveTierTo(Tier from, Tier to) {
    try {
      tierList.moveTierTo(from, to);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  @Override
  public void moveTierTo(Tier from, int toIndex) {
    try {
      tierList.moveTierTo(from, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
    try {
      tierList.moveTierTo(from, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }
  }

  // ------------------------------ persistence ------------------------------//

  @Override
  public void saveTierList() {
    try {
      dataHandler.save(savePath.toFile(), tierList);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
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
  public List<Telement> getUnranked() {
    return tierList.getUnranked();
  }

  @Override
  public List<Tier> getTiers() {
    return tierList.getTiers();
  }

  @Override
  public Optional<Tier> getTierByElement(Telement e) {
    Optional<Tier> element = Optional.empty();

    try {
      element = tierList.getTiers().stream()
          .filter(t -> t.contains(e))
          .findFirst();

    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
    }

    return element;
  }

  @Override
  public Optional<Telement> getElementByHash(String hashCode) {
    Optional<Telement> telement = Optional.empty();

    try {

      telement = Stream
          .concat(tierList.getTiers().stream().flatMap(t -> t.getElements().stream()), tierList.getUnranked().stream())
          .filter(e -> String.valueOf(e.hashCode()).equals(hashCode))
          .findFirst();

      if (telement.isEmpty())
        throw new TelementNotFoundException();
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      printStackTrace(ex);
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
      printStackTrace(ex);
    }

    return tier;
  }

  @Override
  public boolean exists(Telement telement) {

    return Stream
        .concat(
            tierList.getTiers()
                .stream()
                .flatMap(t -> t.getElements().stream()),
            tierList.getUnranked()
                .stream())
        .anyMatch(e -> e.equals(telement));

  }

  private void printStackTrace(Exception ex) {
    if (ex instanceof NullPointerException)
      System.err.println("--- Aborting operation: NullPointerException ---" + System.lineSeparator() + ex);
    else
      System.err.println("--- Aborting operation: ---"
          + System.lineSeparator()
          + "\tIllegalArgumentException: unknown error."
          + System.lineSeparator());
  }
}
