package model;

public class UnrankedElement extends TierElement {
	
	public UnrankedElement(TierElement e) {
		super(false, e.getName(), e.getImagePath());
	}
	
	public UnrankedElement(String name, String imagePath) {
		super(false, name, imagePath);
	}
	
	public UnrankedElement(String name) {
		super(false, name);
	}
	
	public UnrankedElement() {
		super(false);
	}
	
	/**
	 * Returns the {@link TierElement} as a {@link String}
	 * 
	 * Format:
	 * 	"name"
	 * 
	 * @return {@link String}
	 */
	@Override
	public String toString() {
		return getName();
	}
}
