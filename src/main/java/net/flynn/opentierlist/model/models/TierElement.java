package net.flynn.opentierlist.model.models;

import java.io.FileNotFoundException;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.flynn.opentierlist.model.enums.*;

/**
 * Class representing a single {@link Tier} entry.
 * <p>
 *
 * @author flynnz
 * @version 2.75
 * @since v0.0.0
 */
public class TierElement {
  private TieredStatus status;
  private String name;
  private ImagePath imagePath;

  private static long NEXT_ID = 1;
  private final long id;

  public static final String DEFAULT_ELEMENT_NAME = "element";

  // ----- Ctors -----//

  private TierElement(TieredStatus status, String name, ImagePath imagePath) throws IllegalArgumentException {
    Objects.requireNonNull(name);
    Objects.requireNonNull(imagePath);
    if (name.isBlank())
      throw new IllegalArgumentException();

    this.status = status;
    this.name = name;
    this.id = NEXT_ID++;
    this.imagePath = imagePath;
  }

  public TierElement(String name, ImagePath imagePath) throws IllegalArgumentException {
    this(TieredStatus.UNTIERED, name, imagePath);
  }

  /**
   * Constructs a {@link TierList} entry given the following parameters.
   * 
   * @param status    enum representing state
   * @param name      the entry's name
   * @param imagePath path to the entry image
   * 
   * @throws IllegalArgumentException if either name or image path are blank
   */
  public TierElement(TieredStatus status, String name, String imagePath)
      throws IllegalArgumentException, FileNotFoundException {
    if (imagePath.isBlank())
      throw new IllegalArgumentException();
    this(status, name, ImagePath.of(imagePath));
  }

  /**
   * Constructs a {@link TierList} entry given the following parameters.
   * 
   * @param name      the entry's name
   * @param imagePath the entry's image path
   * 
   * @throws IllegalArgumentException if either name or path are blank
   */
  public TierElement(String name, String imagePath) throws IllegalArgumentException, FileNotFoundException {
    this(TieredStatus.UNTIERED, name, imagePath);
  }

  /**
   * Constructs a {@link TierList} entry given only the name.
   * 
   * @param name the entry's name
   * 
   * @throws IllegalArgumentException if name is blank
   */
  public TierElement(String name) throws IllegalArgumentException, FileNotFoundException {
    this(TieredStatus.UNTIERED, name, ImagePath.defaultResource());
  }

  /**
   * Constructs a 'default' {@link TierList} entry.
   */
  public TierElement() throws FileNotFoundException {
    this(DEFAULT_ELEMENT_NAME);
  }

  // ----- setters and getters -----//

  public TieredStatus status() {
    return status;
  }

  @JsonIgnore
  public boolean isTiered() {
    return status.value();
  }

  /**
   * Method to mutate this {@link TierElement} instance's status
   * 
   * @param status to change to
   */
  public void changeTo(TieredStatus status) {
    Objects.requireNonNull(status);
    this.status = status;
  }

  public void setName(String name) throws IllegalArgumentException {
    Objects.requireNonNull(name);
    if (name.isBlank())
      throw new IllegalArgumentException();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setImageUrl(String imageUri) throws FileNotFoundException {
    this.imagePath = ImagePath.of(imageUri);
  }

  public String getImageUri() {
    return this.imagePath.getUri();
  }

  public Long getId() {
    return this.id;
  }

  public void updateImagePath() throws FileNotFoundException {
    this.imagePath = imagePath.exists() ? imagePath : ImagePath.defaultResource();
  }

  // ----- hashCode, equals and toString ----- //

  @Override
  public int hashCode() {
    return Objects.hash(imagePath, name, status, id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof TierElement other)) {
      return false;
    }
    return Objects.equals(imagePath, other.imagePath)
        && Objects.equals(name, other.name)
        && Objects.equals(id, other.id)
        && status == other.status;
  }

  /**
   * Returns the {@link TierElement} as a {@link String}
   * 
   * Format:
   * "TierElementName".
   * 
   * @return {@link String}
   */
  @Override
  public String toString() {
    return toString(TierStringFormat.COMPACT);
  }

  /**
   * Returns the {@link TierElement} as a {@link String}
   * 
   * Format COMPACT:
   * "name".
   * <p>
   * Format EXTENDED:
   * "name\n
   * status\n
   * imagePath".
   * 
   * @return {@link String}
   */
  public String toString(TierStringFormat format) {
    String res = null;

    switch (format) {
      case TierStringFormat.EXTENDED -> res = getName() + System.lineSeparator()
          + status + System.lineSeparator()
          + imagePath.getUri();
      case TierStringFormat.COMPACT -> res = getName();
    }
    return res;
  }
}
