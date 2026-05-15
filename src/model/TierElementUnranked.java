package model;

public class TierElementUnranked extends TierElement {
	
	public TierElementUnranked(TierElement e) {
		super(false, e.getName(), e.getImagePath());
	}
	
	public TierElementUnranked(String name, String imagePath) {
		super(false, name, imagePath);
	}
	
	public TierElementUnranked(String name) {
		super(false, name);
	}
	
	public TierElementUnranked() {
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
