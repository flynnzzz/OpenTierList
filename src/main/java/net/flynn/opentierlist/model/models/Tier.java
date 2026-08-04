package net.flynn.opentierlist.model.models;

import net.flynn.opentierlist.model.enums.TierStringFormat;
import net.flynn.opentierlist.model.exceptions.TierElementNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
  public static final String DEFAULT_TIER_COLOR = Color.GRAY.toString();
  public static final Tier UNTIERED = new Tier("__UNTIERED__", "#ffffff");

  private TierHeader header;
  protected final List<TierElement> tiered;

  private static long NEXT_ID = 1;
  private final long id;

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
   * @param name   tier name
   * @param color  tier {@link Color}
   * @param tiered list to associate to this tier
   * @throws IllegalArgumentException if the header's name is blank
   */
  public Tier(String name, String color, List<TierElement> tiered) {
    this(new TierHeader(name, color), tiered);
  }

  /**
   * Constructs a new empty {@link Tier} object with the given parameters.
   * 
   * @param name  tier name
   * @param color tier {@link Color}
   * @throws IllegalArgumentException if name is blank
   */
  public Tier(String name, String color) {
    this(new TierHeader(name, color), new ArrayList<>());
  }

  /**
   * Constructs a new empty {@link Tier} object with given name.
   * 
   * @param name tier name
   * @throws IllegalArgumentException if name is blank
   */
  public Tier(String name) {
    this(new TierHeader(name, DEFAULT_TIER_COLOR), new ArrayList<>());
  }

  /**
   * Constructs a new empty {@link Tier} object
   */
  public Tier() {
    this(DEFAULT_TIER_NAME);
  }

  @JsonCreator
  public Tier(
      @JsonProperty("name") String name,
      @JsonProperty("color") String color,
      @JsonProperty("id") long id,
      @JsonProperty("tiered") List<TierElement> tiered) {
    setHeader(new TierHeader(name, color));
    this.id = id;
    this.tiered = tiered;
  }

  // ----- methods -----//

  /**
   * Adds an element to the tier instance
   *
   * @param element element to add
   * @return true if successful
   */
  public boolean add(TierElement element) {
    return tiered.add(element);
  }

  // TODO: add details to exceptions thrown
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

  public void swap(TierElement src, TierElement dest) throws TierElementNotFoundException {
    try {
      swap(tiered.indexOf(src), tiered.indexOf(dest));
    } catch (IndexOutOfBoundsException e) {
      throw new TierElementNotFoundException();
    }

  }

  public void swap(int src, int dest) throws TierElementNotFoundException {
    try {
      Collections.swap(tiered, src, dest);
    } catch (IndexOutOfBoundsException e) {
      throw new TierElementNotFoundException();
    }
  }

  public boolean contains(TierElement element) {
    return tiered.contains(element);
  }

  public int elementsCount() {
    return tiered.size();
  }

  /**
   * Moves an element to a certain position, automatically shifting all the others
   * 
   * @param src  element to move
   * @param dest destination
   * @throws TierElementNotFoundException if element is not found
   */
  public void move(TierElement src, TierElement dest) throws TierElementNotFoundException {
    if (!tiered.contains(src))
      throw new TierElementNotFoundException("--- Element to move not found: " + src + " ---");
    if (!tiered.contains(dest))
      throw new TierElementNotFoundException("--- Element to move not found: " + src + " ---");
    final var destIndex = indexOf(dest);
    tiered.remove(src);
    tiered.add(destIndex, src);
  }

  /**
   * Moves an element to a certain index, automatically shifting all the others
   * 
   * @param element element to move
   * @param toIndex destination index
   * @throws TierElementNotFoundException if element is not found
   */
  public void move(TierElement element, int toIndex) throws TierElementNotFoundException {
    if (!this.contains(element))
      throw new TierElementNotFoundException("--- Element to move not found: " + element + " ---");

    if (toIndex > tiered.size())
      throw new TierElementNotFoundException("--- Index to move to is out of bounds: " + toIndex + " ---");

    tiered.remove(element);
    tiered.add(toIndex, element);
  }

  public Tier copy() {
    return new Tier(header.name(), header.color(), tiered);
  }

  public int indexOf(TierElement element) {
    return tiered.indexOf(element);
  }

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
    setHeader(new TierHeader(getName(), color));
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
    return getHeader().color();
  }

  /**
   * *Read only*
   * 
   * @return this tier instance's elements
   */
  public List<TierElement> getTiered() {
    return List.copyOf(tiered);
  }

  public TierElement get(int i) { return tiered.get(i); }

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

  /**
   * Equals but ignoring instance difference
   *
   * @param tier tier to compare to
   * @return true if names and colors match
   */
  public boolean equalsTier(Tier tier) {
    if (this == tier) {
      return true;
    }
    if (!(tier instanceof Tier other)) {
      return false;
    }
    return Objects.equals(header, other.header);
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
   * \[
   * element1,
   * element2,
   * ...
   * \]
   * "
   */
  public String toString(TierStringFormat format) {
    return switch (format) {
      case EXTENDED -> toStringExtended();
      case COMPACT -> toStringCompact();
    };
  }

  private String toStringCompact() {
    return getHeader().name() + ": " + toStringElements(getTiered());
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
