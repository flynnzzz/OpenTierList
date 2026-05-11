package model;

public class UnrankedElement extends Element {
	
	public UnrankedElement(Element e) {
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
