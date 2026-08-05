package net.flynn.opentierlist.controller;

import java.io.File;
import java.nio.file.Files;
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
import net.flynn.opentierlist.ui.manual.SPTiered;

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

  /**
   * Constructor that creates a controller for {@link TierList}.
   * <p>
   * Instantiates an {@link TierList} with the given parameter
   *
   * @param tierList parameter to pass
   */
  public StandardTierListController(TierList tierList) {
    this.tierList = tierList;
    this.dataHandler = new DataHandler();
  }

  @Override
  public void tier(TierElement unTiered, Tier toTier) {
    try {
      tierList.tier(unTiered, toTier);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'tier' method ---");
    }
  }

  @Override
  public void tier(TierElement unTiered, Tier toTier, TierElement position) {
    try {
      tierList.tierInsert(unTiered, toTier, position);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'tierInsert' method ---");
    }
  }

  @Override
  public void tier(TierElement unTiered, Tier toTier, int toIndex) {
    try {
      tierList.tierInsert(unTiered, toTier, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'tierInsert' method ---");
    }
  }

  @Override
  public void unTier(TierElement tiered) {
    try {
      tierList.unTier(tiered);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'unTier' method ---");
    }
  }

  @Override
  public void unTier(TierElement tiered, TierElement position) {
    try {
      tierList.unTierInsert(tiered, position);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'unTierInsert' method ---");
    }
  }

  @Override
  public void unTier(TierElement tiered, int toIndex) {
    try {
      tierList.unTierInsert(tiered, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'unTierInsert' method ---");
    }
  }

  @Override
  public void setTierList(TierList tierList) {
    try {
      this.tierList = tierList;
    } catch (NullPointerException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'setTierList' method ---");
    }
  }

  @Override
  public void setTierListName(String name) {
    try {
      tierList.setTierListName(name);
    } catch (NullPointerException | IllegalArgumentException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'setTierListName' method ---");
    }
  }

  @Override
  public void setTierName(Tier tier, String name) {
    try {
      tierList.setTierName(tierList.indexOf(tier), name);
    } catch (NullPointerException | IllegalArgumentException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'setTierName' method ---");
    }
  }

  @Override
  public void addTier(Tier tier) {
    try {
      tierList.addTier(tier);
    } catch (NullPointerException | IllegalArgumentException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'addTier' method ---");
    }
  }

  @Override
  public void addDefaultTier() {
    try {
      tierList.addTier(new Tier());
    } catch (NullPointerException | IllegalArgumentException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'addDefaultTier' method ---");
    }
  }

  @Override
  public void addUnTiered(TierElement element) {
    try {
      tierList.addElement(element, Tier.UNTIERED);
    } catch (NullPointerException | IllegalArgumentException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'addUnTiered' method ---");
    }
  }

  @Override
  public void removeTier(Tier tier) {
    try {
      tierList.removeTier(tier);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'deleteTier' method ---");
    }
  }

  @Override
  public void removeElement(TierElement element) {
    try {
      tierList.removeElement(element);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'deleteTierElement' method ---");
    }
  }

  @Override
  public void moveElement(TierElement element, Tier toTier) {
    try {
      tierList.moveElement(element, toTier);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'moveElement' method ---");
    }
  }

  @Override
  public void insertElement(TierElement element, Tier toTier, TierElement position) {
    try {
      tierList.insertElement(element, toTier, position);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'moveElement' method ---");
    }
  }

  @Override
  public void insertElement(TierElement element, Tier toTier, int index) {
    try {
      tierList.insertElement(element, toTier, index);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'moveElement' method ---");
    }
  }

  @Override
  public void moveTier(Tier from, Tier to) {
    try {
      tierList.moveTier(from, to);
    } catch (
            NullPointerException
            | IllegalArgumentException
            | IndexOutOfBoundsException
            | UnsupportedOperationException ex
    ) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'moveTier' method ---");
    }
  }

  @Override
  public void moveTier(Tier from, int toIndex) {
    try {
      tierList.moveTier(from, toIndex);
    } catch (
             NullPointerException
             | IllegalArgumentException
             | IndexOutOfBoundsException
             | UnsupportedOperationException ex
    ) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'moveTier' method ---");
    }
  }

  @Override
  public boolean saveTierList() {
    try {
      final Path defaultPath = Path.of(System.getProperty("user.home"), "Documents", "OpenTierList");

      if (!Files.exists(defaultPath)) {
        if (!defaultPath.toFile().mkdir()) {
          System.err.println(
                  "[ERROR] --- Could not create folder 'OpenTierList' in " + System.getProperty("user.home") + "/Documents ---"
          );
          return false;
        }
      }

      saveTierList(defaultPath.resolve(getTierListName() + ".tson"));
      return true;

    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'saveTierList' method ---");
      return false;
    }
  }

  @Override
  public boolean saveTierList(Path path) {
    try {

      if (!path.toString().endsWith(".tson"))
        path = Path.of(path + ".tson");

      dataHandler.save(path.toFile(), tierList);
      return true;
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'saveTierList' method ---");
      return false;
    }
  }

  @Override
  public boolean exportTierList(SPTiered node) {
    final Path defaultPath = Path.of(System.getProperty("user.home"), "Pictures", "OpenTierList");

    if (!Files.exists(defaultPath)) {
      if (!defaultPath.toFile().mkdir()) {
        System.err.println(
                "[ERROR] --- Could not create folder 'OpenTierList' in " + System.getProperty("user.home") + "/Pictures ---"
        );
        return false;
      }
    }

    exportTierList(node, defaultPath.resolve(getTierListName() + ".png"));
    return true;
  }

  @Override
  public boolean exportTierList(SPTiered node, Path path) {
    try {

      if (!path.toString().endsWith(".png"))
        path = Path.of(path + ".png");

      dataHandler.export(path, node);
      return true;
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'saveTierList' method ---");
      return false;
    }
  }

  @Deprecated @Override
  public void saveTierListAs(String name) throws UnsupportedOperationException {
    throw new UnsupportedOperationException("[ERROR] --- Deprecated method used ---");
  }

  @Override
  public Optional<TierList> loadTierList(File file) {
    return dataHandler.load(file);
  }

  @Override
  public String toString() {
    return tierList.toString();
  }

  @Override
  public String toString(TierStringFormat format) {
    return tierList.toString(format);
  }

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

      if (element.isEmpty() && getUnTiered().contains(e))
        element = Optional.of(Tier.UNTIERED);

    } catch (NullPointerException | IllegalArgumentException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'getTierByElement' method ---");
    }

    return element;
  }

  @Override
  public Optional<TierElement> getElementByHash(String hashCode) {
    Optional<TierElement> element = Optional.empty();

    try {

      element = Stream
              .concat(
                      tierList.getTiers().stream()
                              .flatMap(t -> t.getTiered().stream()),
                      tierList.getUnTiered().stream()
              )
              .filter(e -> String.valueOf(e.hashCode()).equals(hashCode))
              .findFirst();

      if (element.isEmpty())
        throw new TierElementNotFoundException();
    } catch (NullPointerException | IllegalArgumentException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'getElementByHash' method ---");
    }

    return element;
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
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'getTierByHash' method ---");
    }

    return tier;
  }

  @Override
  public boolean elementExists(Integer hash) {

    return Stream
        .concat(
            tierList.getTiers()
                .stream()
                .flatMap(t -> t.getTiered().stream()),
            tierList.getUnTiered()
                .stream())
        .anyMatch(e -> Objects.equals(e.hashCode(), hash));

  }

  @Override
  @Deprecated
  public void swapTiered(Tier tier, TierElement a, TierElement b) {
    try {
      tierList.swapTiered(tier, a, b);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'swapTiered' method ---");
    }
  }

  @Override
  @Deprecated
  public void swapUnTiered(TierElement a, TierElement b) {
    try {
      tierList.swapUnTiered(a, b);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'swapUnTiered' method ---");
    }
  }

  @Override
  @Deprecated
  public void appendTiered(TierElement tiered, Tier toTier) {
    try {
      tierList.moveToTier(tiered, toTier);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'appendTiered' method ---");
    }
  }

  @Override
  @Deprecated
  public void moveTiered(TierElement tiered, Tier toTier, TierElement toElement) {
    try {
      int toIndex = toTier.getTiered().indexOf(toElement);
      tierList.moveToTier(tiered, toTier, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'moveTiered' method ---");
    }
  }

  @Override
  @Deprecated
  public void moveTiered(TierElement tiered, Tier toTier, int toIndex) {
    try {
      tierList.moveToTier(tiered, toTier, toIndex);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'moveTiered' method ---");
    }
  }

  @Override
  @Deprecated
  public void moveUnTiered(TierElement unTiered, TierElement toElement) { }

  @Override
  @Deprecated
  public void moveUnTiered(TierElement unTiered, int toIndex) { }

  @Override
  @Deprecated
  public void deleteUnTiered(TierElement unTiered) {
    try {
      tierList.removeUnTiered(unTiered);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'deleteUnTiered' method ---");
    }
  }

  @Override
  @Deprecated
  public void swapTiers(Tier a, Tier b) {
    try {
      tierList.swapTiers(a, b);
    } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException ex) {
      System.err.println("[ERROR] --- " + ex.getClass() + ": in 'swapTiers' method ---");
    }
  }

}
