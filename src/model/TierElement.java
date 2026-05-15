package model;

import java.util.Objects;

import model.enums.TierElementStatus;

/**
 * Class representing a single {@link TierList} "entry"
 * 
 * @author flynnz
 * @version 1.20
 * @since v0.0.0
 */
public class TierElement {
	private TierElementStatus status;
	private String name, imagePath;

	public static final String DEFAULT_ELEMENT_NAME = "element";
	public static final String DEFAULT_ELEMENT_IMAGE_PATH = "NONE";
	
	
	//---------------------------------- Ctors ----------------------------------//	
	
	/**
	 * Constructs a {@link TierList} entry given the following parameters.
	 * 
	 * All parameters must not be null:
	 * @param status enum representing state
	 * @param name the entry's name
	 * @param imagePath path to the entry image
	 * 
	 * @throws IllegalArgumentException if either name or image path are blank
	 */

	public TierElement(TierElementStatus status,  String name, String imagePath) throws IllegalArgumentException {
		Objects.requireNonNull(name); Objects.requireNonNull(imagePath);
		if (name.isBlank()) throw new IllegalArgumentException();
		if (imagePath.isBlank()) throw new IllegalArgumentException();
		
		this.status = status;
		this.name = name;
		this.imagePath = imagePath;
	}
	
	/**
	 * Constructs a {@link TierList} entry given the following parameters.
	 * 
	 * All parameters must not be null:
	 * @param status enum representing state
	 * @param name the entry's name
	 * 
	 * The element's image path will be set to {@link TierElement#DEFAULT_ELEMENT_IMAGE_PATH}
	 * @throws IllegalArgumentException if name is blank
	 */
	public TierElement(TierElementStatus status,  String name) throws IllegalArgumentException {
		Objects.requireNonNull(name);
		if (name.isBlank()) throw new IllegalArgumentException();
		this.status = status;
		this.name = name;
		this.imagePath = DEFAULT_ELEMENT_IMAGE_PATH;
	}
	
	/**
	 * Constructs a {@link TierList} entry given the following parameters.
	 * 
	 * All parameters must not be null:
	 * @param status boolean representing state
	 * 
	 * The element's name will be set to {@link TierElement#DEFAULT_ELEMENT_NAME}
	 * and the image path will be set to {@link TierElement#DEFAULT_ELEMENT_IMAGE_PATH}
	 */
	public TierElement(TierElementStatus status) {
		this.status = status;
		this.name = DEFAULT_ELEMENT_NAME;
		this.imagePath = DEFAULT_ELEMENT_IMAGE_PATH;
	}
	
	
	//---------------------------------- setters and getters ----------------------------------//	
	
	public TierElementStatus status() { return status; }
	
	public boolean isRanked() { return status.value(); }
	
	public TierElement changeTo(TierElementStatus status) { 
		if (status.equals(TierElementStatus.RANKED)) return new TierElementRanked(this);
		else return new TierElementUnranked(this);
	}
	
	public void setName(String name) throws IllegalArgumentException { 
		Objects.requireNonNull(name);
		if (name.isBlank()) throw new IllegalArgumentException();
		this.name = name; 
	}

	public String getName() { return name; }
	
	public void setImagePath(String imagePath) throws IllegalArgumentException {
		Objects.requireNonNull(name);
		if (imagePath.isBlank()) throw new IllegalArgumentException();
		this.imagePath = imagePath; 
	}
	
	public String getImagePath() { return imagePath; }

	
	//---------------------------------- hashCode, equals and toString ----------------------------------//	
	
	@Override
	public int hashCode() {
		return Objects.hash(imagePath, name, status);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof TierElement other)) { return false; }
		return Objects.equals(imagePath, other.imagePath) && Objects.equals(name, other.name) && status == other.status;
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
		if (status.equals(TierElementStatus.UNRANKED))
			sb.append("not ");
		sb.append("ranked");

		return sb.toString();
	}
}
