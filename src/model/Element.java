package model;

import java.util.Objects;

/**
 * Class representing a single {@link TierList} "entry"
 * 
 * @author flynnz
 * @version 0.00
 * @since v0.0.0
 */
public class Element {
	private boolean ranked;
	private String name, imagePath;
	/**
	 * The default name is set to "element"
	 */
	public static final String DEFAULT_ELEMENT_NAME = "element";
	/**
	 * The default image path is set to "NONE"
	 */
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
	 */
	public Element(boolean isRanked,  String name, String imagePath) {
		if (name == null) throw new NullPointerException("Element name must not be null");
		if (imagePath == null) throw new NullPointerException(
				"Element image path must not be null");
		
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
	 * The element's image path will be set to {@link Element#DEFAULT_ELEMENT_IMAGE_PATH}
	 */
	public Element(boolean isRanked,  String name) {
		Objects.requireNonNull(name);
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
	 * The element's name will be set to {@link Element#DEFAULT_ELEMENT_NAME}
	 * and the image path will be set to {@link Element#DEFAULT_ELEMENT_IMAGE_PATH}
	 */
	public Element(boolean isRanked) {
		this.ranked = isRanked;
		this.name = DEFAULT_ELEMENT_NAME;
		this.imagePath = DEFAULT_ELEMENT_IMAGE_PATH;
	}
	
	
	/***********************************************************************
	 * 						Setters and getters
	 **********************************************************************/
	
	/**
	 * Returns whether or not the {@link Element} was ranked
	 * 
	 * @return true if the {@link Element} was ranked
	 */
	public boolean isRanked() { return ranked; }
	/**
	 * Set the {@link Element} to ranked or not
	 * 
	 * @param ranked value to set
	 */
	public void setRanked(boolean ranked) { this.ranked = ranked; }
	/**
	 * Returns the function name
	 * 
	 * @return name the {@link Element} name
	 */
	public String getName() { return name; }
	/**
	 * Sets the {@link Element} name.
	 * 
	 * @param name value to set, must not be null
	 */
	public void setName(String name) { 
		if (name == null) throw new NullPointerException("Name to set must not be null");
		this.name = name; 
	}
	/**
	 * Returns the image path of the {@link Element}
	 * 
	 * @return imagePath the {@link Element} image path
	 */
	public String getImagePath() { return imagePath; }
	/**
	 * Sets the image path of the {@link Element}
	 * 
	 * @param imagePath value to set, must not be null
	 */
	public void setImagePath(String imagePath) {
		if (imagePath == null) throw new NullPointerException(
				"Image path to set must not be null");
		this.imagePath = imagePath; 
	}

	
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
		if (!(obj instanceof Element other)) { return false; }
		return Objects.equals(imagePath, other.imagePath) && Objects.equals(name, other.name) && ranked == other.ranked;
	}
	
	/**
	 * Returns the {@link Element} as a {@link String}
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
