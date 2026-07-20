package net.flynn.opentierlist.model.models;

import java.util.Objects;
import net.flynn.opentierlist.model.enums.*;

/**
 * Class representing a single {@link Tier} entry.
 * 
 * Stands for 'Tier element'.
 * 
 * @author flynnz
 * @version 2.25
 * @since v0.0.0
 */
public class Telement {
	private TelementStatus status;
	private String name;
	private ImagePath imagePath;
	
	private static long NEXT_ID = 1;
	private final long id;
	
	public static final String DEFAULT_ELEMENT_NAME = "element";
	
	//---------------------------------- Ctors ----------------------------------//	
	
	private Telement(TelementStatus status,  String name, ImagePath imagePath) throws IllegalArgumentException {
		Objects.requireNonNull(name); 
		Objects.requireNonNull(imagePath);
		if (name.isBlank()) throw new IllegalArgumentException();
		
		this.status = status;
		this.name = name;
		this.id = NEXT_ID++;
		this.imagePath = imagePath;
	}
	
	public Telement(String name, ImagePath imagePath) throws IllegalArgumentException {
		this(TelementStatus.UNRANKED, name , imagePath);
	}
	
	/**
	 * Constructs a {@link TierList} entry given the following parameters.
	 * 
	 * @param status enum representing state
	 * @param name the entry's name
	 * @param imagePath path to the entry image
	 * 
	 * @throws IllegalArgumentException if either name or image path are blank
	 */

	public Telement(TelementStatus status,  String name, String imagePath) throws IllegalArgumentException {
		if (imagePath.isBlank()) throw new IllegalArgumentException();
		this(status, name, ImagePath.of(imagePath));
	}
	
	/**
	 * Constructs a {@link TierList} entry given the following parameters.
	 * 
	 * @param name the entry's name
	 * @param imagePath the entry's image path
	 * 
	 * @throws IllegalArgumentException if either name or path are blank
	 */
	public Telement(String name, String imagePath) throws IllegalArgumentException {
		this(TelementStatus.UNRANKED, name, imagePath);
	}
	
	/**
	 * Constructs a {@link TierList} entry given only the name.
	 * 
	 * @param name the entry's name
	 * 
	 * @throws IllegalArgumentException if path is blank
	 */
	public Telement(String name) throws IllegalArgumentException {
		this(TelementStatus.UNRANKED, name, ImagePath.defaultResource());
	}
	
	/**
	 * Constructs a 'default' {@link TierList} entry.
	 */
	public Telement() {
		this(DEFAULT_ELEMENT_NAME);
	}
	
	
	//---------------------------------- setters and getters ----------------------------------//	
	
	public TelementStatus status() { return status; }
	
	public boolean isRanked() { return status.value(); }
	
	
	/**
	 * Method to mutate this {@link Telement} instance's status
	 * 
	 * @param status to change to
	 */
	public void changeTo(TelementStatus status) { 
		Objects.requireNonNull(status);
		this.status = status;
	}
	
	public void setName(String name) throws IllegalArgumentException { 
		Objects.requireNonNull(status); 
		if (name.isBlank()) throw new IllegalArgumentException();
		this.name = name; 
	}

	public String getName() { return name; }
	
	public String getImageUrl() { 
		return this.imagePath.getUrl(); 
	}
	
	public void updateImagePath() {
		this.imagePath = imagePath.exists() ? imagePath : ImagePath.defaultResource();
	}

	
	//---------------------------------- hashCode, equals and toString ----------------------------------//	
	
	@Override
	public int hashCode() {
		return Objects.hash(imagePath, name, status, id);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof Telement other)) { return false; }
		return Objects.equals(imagePath, other.imagePath) 
				&& Objects.equals(name, other.name) 
				&& Objects.equals(id, other.id)
				&& status == other.status;
	}
	
	/**
	 * Returns the {@link Telement} as a {@link String}
	 * 
	 * Format:
	 * 	"TierElementName".
	 * 
	 * @return {@link String}
	 */
	@Override
	public String toString() {
		return toString(TierStringFormat.COMPACT);
	}
	
	/**
	 * Returns the {@link Telement} as a {@link String}
	 * 
	 * Format COMPACT:
	 * 	"name".
	 * 
	 * Format EXTENDED:
	 * 	"name\n
	 * 	 status\n
	 * 	 imagePath".
	 * 
	 * @return {@link String}
	 */
	public String toString(TierStringFormat format) {
		String res = null;
		
		switch(format) {
		case TierStringFormat.EXTENDED -> res = getName() + System.lineSeparator() 
											+ status + System.lineSeparator() 
											+ imagePath;
		case TierStringFormat.COMPACT -> res = getName();
		default -> res = getName();
		}
		return res;
	}
}
