package net.flynn.opentierlist.model.models;

import java.io.FileNotFoundException;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import net.flynn.opentierlist.model.enums.TierStringFormat;
import java.net.URI;
import java.net.URISyntaxException;

import net.flynn.opentierlist.model.enums.TieredStatus;

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
  private String elementName;
  private ImagePath imagePath;

  private static long NEXT_ID = 1;
  private final long id;

  public static final String DEFAULT_ELEMENT_NAME = "element";

  // ----- Ctors -----//

  private TierElement(TieredStatus status, String elementName, ImagePath imagePath) throws IllegalArgumentException {
    Objects.requireNonNull(elementName);
    Objects.requireNonNull(imagePath);
    if (elementName.isBlank())
      throw new IllegalArgumentException();

    this.status = status;
    this.elementName = elementName;
    this.id = NEXT_ID++;
    this.imagePath = imagePath;
  }

  private TierElement(String elementName, ImagePath imagePath) throws IllegalArgumentException {
    this(TieredStatus.UNTIERED, elementName, imagePath);
  }

  /**
   * Constructs a {@link TierList} entry given the following parameters.
   * 
   * @param status    enum representing state
   * @param elementName      the entry's name
   * @param uri path to the entry image
   * 
   * @throws IllegalArgumentException if either name or image path are blank
   */
  public TierElement(TieredStatus status, String elementName, String uri)
      throws IllegalArgumentException, FileNotFoundException {
    if (uri.isBlank())
      throw new IllegalArgumentException();
    this(status, elementName, ImagePath.of(uri));
  }

  /**
   * Constructs a {@link TierList} entry given the following parameters.
   * 
   * @param elementName      the entry's name
   * @param uri the entry's image path
   * 
   * @throws IllegalArgumentException if either name or path are blank
   */
  public TierElement(String elementName, String uri) throws IllegalArgumentException, FileNotFoundException {
    this(TieredStatus.UNTIERED, elementName, uri);
  }

  /**
   * Constructs a {@link TierList} entry given only the name.
   * 
   * @param elementName the entry's name
   * 
   * @throws IllegalArgumentException if name is blank
   */
  public TierElement(String elementName) throws IllegalArgumentException, FileNotFoundException {
    this(TieredStatus.UNTIERED, elementName, ImagePath.defaultResource());
  }

  /**
   * Constructs a 'default' {@link TierList} entry.
   */
  public TierElement() throws FileNotFoundException {
    this(DEFAULT_ELEMENT_NAME);
  }

  @JsonCreator
  public TierElement(
      @JsonProperty("name") String elementName,
      @JsonProperty("status") TieredStatus status,
      @JsonProperty("id") long id,
      @JsonProperty("imageUri") String imageUri) {
    this.elementName = elementName;
    this.status = status;
    this.id = id;
    try {
      this.imagePath = ImagePath.of(new URI(imageUri));
    } catch (URISyntaxException e) {
      this.imagePath = ImagePath.defaultResource();
    }
  }

  // ----- setters and getters -----//

  public TieredStatus getStatus() {
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

  public void setElementName(String elementName) throws IllegalArgumentException {
    Objects.requireNonNull(elementName);
    if (elementName.isBlank())
      throw new IllegalArgumentException();
    this.elementName = elementName;
  }

  public String getElementName() {
    return elementName;
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
    return Objects.hash(imagePath, elementName, status, id);
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
        && Objects.equals(elementName, other.elementName)
        && Objects.equals(id, other.id)
        && status == other.status;
  }

  /**
   * Equals but ignoring instance difference
   *
   * @param tierElement tier element to compare to
   * @return true if names, resource paths and statuses match
   */
  public boolean equalsTier(TierElement tierElement) {
    if (this == tierElement) {
      return true;
    }
    if (!(tierElement instanceof TierElement other)) {
      return false;
    }
    return Objects.equals(imagePath, other.imagePath)
            && Objects.equals(elementName, other.elementName)
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
      case TierStringFormat.EXTENDED -> res = getElementName() + System.lineSeparator()
          + status + System.lineSeparator()
          + imagePath.getUri();
      case TierStringFormat.COMPACT -> res = getElementName();
    }
    return res;
  }
}
