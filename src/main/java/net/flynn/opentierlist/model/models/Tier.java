package net.flynn.opentierlist.model.models;

import net.flynn.opentierlist.model.enums.TierStringFormat;
import net.flynn.opentierlist.model.exceptions.TierElementNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javafx.scene.paint.Color;

/**
 * Class representing the concept of a 'Tier'
 * 
 * @author flynnz
 * @version 2.25
 * @since v0.0.0
 */
public class Tier {

  public static final String DEFAULT_TIER_NAME = "New Tier";
  public static final Color DEFAULT_TIER_COLOR = Color.GRAY;

  private TierHeader header;
  private final List<TierElement> tiered;

  private static long NEXT_ID = 1;
  private final long id;

  // ----- Ctors -----//

  private Tier(TierHeader header, List<TierElement> tiered) {
    Objects.requireNonNull(header);
    Objects.requireNonNull(tiered);
    if (header.name().isBlank())
      throw new IllegalArgumentException();

    this.header = header;
    this.tiered = tiered;
    this.id = NEXT_ID++;
  }

  /**
   * Constructs a new {@link Tier} object with the given parameters.
   * 
   * @param name     tier name
   * @param color    tier {@link Color}
   * @param tiered list to associate to this tier
   * @throws IllegalArgumentException if the header's name is blank
   */
  public Tier(String name, Color color, List<TierElement> tiered) {
    this(new TierHeader(name, color), tiered);
  }

  /**
   * Constructs a new empty {@link Tier} object with the given parameters.
   * 
   * @param name  tier name
   * @param color tier {@link Color}
   * @throws IllegalArgumentException if name is blank
   */
  public Tier(String name, Color color) {
    this(new TierHeader(name, color), new ArrayList<TierElement>());
  }

  /**
   * Constructs a new empty {@link Tier} object with given name.
   * 
   * @param name tier name
   * @throws IllegalArgumentException if name is blank
   */
  public Tier(String name) {
    this(new TierHeader(name, DEFAULT_TIER_COLOR), new ArrayList<TierElement>());
  }

  /**
   * Constructs a new empty {@link Tier} object
   */
  public Tier() {
    this(DEFAULT_TIER_NAME);
  }

  // ----- methods -----//

  /**
   * Adds an element to the tier instance
   * 
   * @param element element to add
   * @return true if successfull
   */
  public boolean add(TierElement element) {
    return tiered.add(element);
  }

  public boolean remove(TierElement element) throws TierElementNotFoundException {
    if (!tiered.remove(element))
      throw new TierElementNotFoundException();
    else
      return true;
  }

  public TierElement remove(int i) throws TierElementNotFoundException {
    try {
      return tiered.remove(i);
    } catch (IndexOutOfBoundsException e) {
      throw new TierElementNotFoundException();
    }
  }

  public void swap(TierElement a, TierElement b) throws TierElementNotFoundException {
    try {
      swap(tiered.indexOf(a), tiered.indexOf(b));
    } catch (IndexOutOfBoundsException e) {
      throw new TierElementNotFoundException();
    }

  }

  public void swap(int a, int b) throws TierElementNotFoundException {
    try {
      Collections.swap(tiered, a, b);
    } catch (IndexOutOfBoundsException e) {
      throw new TierElementNotFoundException();
    }
  }

  public boolean contains(TierElement element) {
    return tiered.contains(element);
  }

  /**
   * Moves an element to a certain index, automatically shifting all the others
   * 
   * @param toIndex destination index
   * @param element element to move
   * @throws TierElementNotFoundException if element is not found
   */
  public void moveTo(TierElement element, int toIndex) throws TierElementNotFoundException {
    if (!tiered.contains(element) || toIndex > tiered.size())
      throw new TierElementNotFoundException();

    tiered.remove(element);
    tiered.add(toIndex, element);
  }

  public Tier copy() {
    return new Tier(header.name(), header.color());
  }

  // ----- setters and getters -----//

  public void setName(String name) throws IllegalArgumentException {
    Objects.requireNonNull(name);
    if (name.isBlank())
      throw new IllegalArgumentException();
    setHeader(new TierHeader(name, this.header.color()));
  }

  public void setColor(String color) throws IllegalArgumentException {
    Objects.requireNonNull(color);
    if (color.isBlank())
      throw new IllegalArgumentException("--- Color string must not me blank ---");
    setHeader(new TierHeader(getName(), Color.valueOf(color)));
  }

  private void setHeader(TierHeader header) {
    Objects.requireNonNull(header);
    this.header = header;
  }

  private TierHeader getHeader() {
    return new TierHeader(header.name(), header.color());
  }

  public String getName() {
    return getHeader().name();
  }

  public String getColor() {
    return getHeader().color().toString();
  }

  /**
   * *Read only*
   * 
   * @return this tier instance's elements
   */
  public List<TierElement> getTiered() {
    return List.copyOf(tiered);
  }

  // ----- hashCode, equals and toString -----//
  @Override
  public int hashCode() {
    return Objects.hash(tiered, header, id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Tier other)) {
      return false;
    }
      return Objects.equals(tiered, other.tiered)
        && Objects.equals(header, other.header)
        && Objects.equals(id, other.id);
  }

  private String toStringElements(List<TierElement> elements) {
    var sb = new StringBuilder();
    sb.append("[ ");
    for (TierElement e : elements) {
      sb.append(e);
      if (!elements.getLast().equals(e))
        sb.append(", ");
      else
        sb.append(".");
    }
    sb.append(" ]");
    return sb.toString();
  }

  @Override
  public String toString() {
    return toStringCompact();
  }

  /**
   * Returns the {@link Tier} as {@link String} with the specified
   * {@link TierStringFormat}
   * 
   * Format {@link TierStringFormat#EXTENDED}:
   * "header name:
   * [
   * element1,
   * element2,
   * ...
   * ]"
   */
  public String toString(TierStringFormat format) {
    return switch (format) {
      case EXTENDED -> toStringExtended();
      case COMPACT -> toStringCompact();
      default -> toString();
    };
  }

  private String toStringCompact() {
      return getHeader().name() + ":" + System.lineSeparator() +
              toStringElements(getTiered());
  }

  private String toStringExtended() {
    var sb = new StringBuilder();
    sb.append(getHeader().name()).append(":").append(System.lineSeparator());
    sb.append("[");
    sb.append(System.lineSeparator());
    for (TierElement e : tiered) {
      sb.append("\t");
      sb.append(e);
      if (!tiered.getLast().equals(e))
        sb.append(",");
      else
        sb.append(".");
      sb.append(System.lineSeparator());
    }
    sb.append("]");
    return sb.toString();
  }
}
