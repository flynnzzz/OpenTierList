package model;

public class TierElementRanked extends TierElement {
	
	public TierElementRanked(TierElement e) {
		super(true, e.getName(), e.getImagePath());
	}
	
	public TierElementRanked(String name, String imagePath) {
		super(true, name, imagePath);
	}
	
	public TierElementRanked(String name) {
		super(true, name);
	}
	
	public TierElementRanked() {
		super(true);
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
