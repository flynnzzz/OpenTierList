package net.flynn.opentierlist.controller;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import net.flynn.opentierlist.model.enums.TierStringFormat;
import net.flynn.opentierlist.model.exceptions.TierElementNotFoundException;
import net.flynn.opentierlist.model.exceptions.TierNotFoundException;
import net.flynn.opentierlist.model.models.Tier;
import net.flynn.opentierlist.model.models.TierElement;
import net.flynn.opentierlist.model.models.TierList;
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

  private final DataHandler dataHandler;
  private String fileName;
  private Path savePath;

  // ---------------------------------- Ctors ----------------------------------//

  /**
   * Constructor that creates a controller for {@link TierList}.
   * <p>
   * Instantiates an {@link TierList} with the given parameter
   * 
   * @param tierList parameter to pass
   */
  public StandardTierListController(TierList tierList) {
    this.tierList = tierList;
    this.fileName = getTierListName() + ".tson";
    this.savePath = Path.of(System.getProperty("user.home"), "Documents").resolve(fileName);
    this.dataHandler = new DataHandler();
  }

  /**
   * Constructor that creates a controller for {@link TierList}.
   * <p>
   * Instantiates an empty {@link TierList}
   */
  public StandardTierListController() {
    this(new TierList());
  }

  // ------------------------------- methods -------------------------------//

  // ------------------------------ ranking ------------------------------//

  @Override
  public void tier(TierElement element, Tier toTier) {
    try {
      tierList.tier(element, toTier);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void tier(TierElement element, Tier toTier, int toIndex) {
    try {
      tierList.tierInsert(element, toTier, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void unTier(TierElement element) {
    try {
      tierList.unTier(element);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void unTier(TierElement element, int toIndex) {
    try {
      tierList.unTierInsert(element, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  // ------------------------------ editing ------------------------------//

  @Override
  public void setTierList(TierList tierList) {
    try {
      this.tierList = tierList;
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

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
  public void addUnTiered(TierElement element) {
    try {
      tierList.addUnTiered(element);
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
  public void removeTierElement(TierElement element) {
    try {
      var tier = getTierByElement(element);
      if (element.isTiered() && tier.isPresent())
        tierList.removeFromTier(tier.get(), element);
      else if (!element.isTiered()) {
        tierList.removeUnTiered(element);
      } else
        throw new TierNotFoundException("could not remove Telement: " + element);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void removeUnTiered(TierElement element) {
    try {
      tierList.removeUnTiered(element);
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
  public void swapTiered(Tier tier, TierElement a, TierElement b) {
    try {
      tierList.swapTiered(tier, a, b);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void swapUnTiered(TierElement a, TierElement b) {
    try {
      tierList.swapUnTiered(a, b);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  // ------------------------------ moving ------------------------------//

  @Override
  public void moveTiered(TierElement element, Tier toTier) {
    try {
      tierList.moveToTier(element, toTier);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void moveTiered(TierElement element, Tier toTier, TierElement toElement) {
    try {
      int toIndex = toTier.getTiered().indexOf(toElement);
      tierList.moveToTier(element, toTier, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void moveTiered(TierElement element, Tier toTier, int toIndex) {
    try {
      tierList.moveToTier(element, toTier, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void moveUnTiered(TierElement element, TierElement toElement) {
    try {
      int toIndex = tierList.getUnTiered().indexOf(toElement);
      tierList.moveUnTiered(element, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void moveUnTiered(TierElement element, int toIndex) {
    try {
      tierList.moveUnTiered(element, toIndex);
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
      fileName = getTierListName() + ".tson";
      savePath = Path.of(System.getProperty("user.home"), "Documents").resolve(fileName);
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

  @Override
  public Optional<TierList> loadTierList(File file) {
    return dataHandler.load(file);
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
  public List<TierElement> getUnTiered() {
    return tierList.getUnTiered();
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
          .concat(tierList.getTiers().stream().flatMap(t -> t.getTiered().stream()), tierList.getUnTiered().stream())
          .filter(e -> String.valueOf(e.hashCode()).equals(hashCode))
          .findFirst();

      if (telement.isEmpty())
        throw new TierElementNotFoundException();
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
  public boolean tierElementExists(TierElement element) {

    return Stream
        .concat(
            tierList.getTiers()
                .stream()
                .flatMap(t -> t.getTiered().stream()),
            tierList.getUnTiered()
                .stream())
        .anyMatch(e -> e.equals(element));

  }

  @Override
  public boolean tierElementExistsById(Long id) {

    return Stream
        .concat(
            tierList.getTiers()
                .stream()
                .flatMap(t -> t.getTiered().stream()),
            tierList.getUnTiered()
                .stream())
        .anyMatch(e -> Objects.equals(e.getId(), id));

  }

  @Override
  public boolean tierExists(Tier tier) {

    return getTiers().stream().anyMatch(t -> t.equals(tier));

  }
}
