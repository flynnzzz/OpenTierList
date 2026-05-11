package model;

public class RankedElement extends Element {
	
	public RankedElement(Element e) {
		super(true, e.getName(), e.getImagePath());
	}
	
	public RankedElement(String name, String imagePath) {
		super(true, name, imagePath);
	}
	
	public RankedElement(String name) {
		super(true, name);
	}
	
	public RankedElement() {
		super(true);
	}
	
	/**
	 * Returns the {@link Element} as a {@link String}
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
