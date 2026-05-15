package model;

import java.util.Objects;

/**
 * Class representing a single {@link TierList} "entry"
 * 
 * @author flynnz
 * @version 1.00
 * @since v0.0.0
 */
public class TierElement {
	private boolean ranked;
	private String name, imagePath;

	public static final String DEFAULT_ELEMENT_NAME = "element";
	public static final String DEFAULT_ELEMENT_IMAGE_PATH = "NONE";
	
	
	/***********************************************************************
	 * 							Constructors
	 **********************************************************************/
	
	/**
	 * Constructs a {@link TierList} entry given the following parameters.
	 * 
	 * All parameters must not be null:
	 * @param isRanked boolean representing state
	 * @param name the entry's name
	 * @param imagePath path to the entry image
	 * 
	 * @throws IllegalArgumentException if either name or image path are blank
	 */

	public TierElement(boolean isRanked,  String name, String imagePath) throws IllegalArgumentException {
		Objects.requireNonNull(name); Objects.requireNonNull(imagePath);
		if (name.isBlank()) throw new IllegalArgumentException();
		if (imagePath.isBlank()) throw new IllegalArgumentException();
		
		this.ranked = isRanked;
		this.name = name;
		this.imagePath = imagePath;
	}
	/**
	 * Constructs a {@link TierList} entry given the following parameters.
	 * 
	 * All parameters must not be null:
	 * @param isRanked boolean representing state
	 * @param name the entry's name
	 * 
	 * The element's image path will be set to {@link TierElement#DEFAULT_ELEMENT_IMAGE_PATH}
	 * @throws IllegalArgumentException if name is blank
	 */
	public TierElement(boolean isRanked,  String name) throws IllegalArgumentException {
		Objects.requireNonNull(name);
		if (name.isBlank()) throw new IllegalArgumentException();
		this.ranked = isRanked;
		this.name = name;
		this.imagePath = DEFAULT_ELEMENT_IMAGE_PATH;
	}
	/**
	 * Constructs a {@link TierList} entry given the following parameters.
	 * 
	 * All parameters must not be null:
	 * @param isRanked boolean representing state
	 * 
	 * The element's name will be set to {@link TierElement#DEFAULT_ELEMENT_NAME}
	 * and the image path will be set to {@link TierElement#DEFAULT_ELEMENT_IMAGE_PATH}
	 */
	public TierElement(boolean isRanked) {
		this.ranked = isRanked;
		this.name = DEFAULT_ELEMENT_NAME;
		this.imagePath = DEFAULT_ELEMENT_IMAGE_PATH;
	}
	
	
	/***********************************************************************
	 * 						Setters and getters
	 **********************************************************************/
	
	/**
	 * Returns whether or not the {@link TierElement} was ranked
	 * 
	 * @return true if the {@link TierElement} was ranked
	 */
	public boolean isRanked() { return ranked; }
	
	/**
	 * Set the {@link TierElement} to ranked or unranked
	 * 
	 * @param ranked value to set
	 * 
	 * @return new Element with updated status
	 */
	public TierElement changeTo(boolean ranked) { 
		if (ranked)
			return new TierElementRanked(this);
		else
			return new TierElementUnranked(this);
	}
	
	/**
	 * Sets the {@link TierElement} name.
	 * 
	 * @param name value to set, must not be null
	 * @throws IllegalArgumentException if name is blank 
	 */
	public void setName(String name) throws IllegalArgumentException { 
		Objects.requireNonNull(name);
		if (name.isBlank()) throw new IllegalArgumentException();
		this.name = name; 
	}
	/**
	 * Returns the function name
	 * 
	 * @return name the {@link TierElement} name
	 */
	public String getName() { return name; }
	
	/**
	 * Sets the image path of the {@link TierElement}
	 * 
	 * @param imagePath value to set, must not be null
	 * @throws IllegalArgumentException if image path is blank
	 */
	public void setImagePath(String imagePath) throws IllegalArgumentException {
		Objects.requireNonNull(name);
		if (imagePath.isBlank()) throw new IllegalArgumentException();
		this.imagePath = imagePath; 
	}
	
	/**
	 * Returns the image path of the {@link TierElement}
	 * 
	 * @return imagePath the {@link TierElement} image path
	 */
	public String getImagePath() { return imagePath; }

	
	/***********************************************************************
	 * 					hashCode, equals and toString
	 **********************************************************************/
	
	/**
	 * hashCode function
	 * 
	 * @see Objects#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(imagePath, name, ranked);
	}
	
	/**
	 * equals function
	 * 
	 *  @see Objects#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof TierElement other)) { return false; }
		return Objects.equals(imagePath, other.imagePath) && Objects.equals(name, other.name) && ranked == other.ranked;
	}
	
	/**
	 * Returns the {@link TierElement} as a {@link String}
	 * 
	 * Format:
	 * 	"name: [not] ranked".
	 * 
	 * @return {@link String}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		sb.append(name + ": ");
		if (!ranked)
			sb.append("not ");
		sb.append("ranked");

		return sb.toString();
	}
}
