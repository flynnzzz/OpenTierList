package model;

import static model.enums.TierElementStatus.*;

public class TierElementRanked extends TierElement {
	
	public TierElementRanked(TierElement e) {
		super(RANKED, e.getName(), e.getImagePath());
	}
	
	public TierElementRanked(String name, String imagePath) {
		super(RANKED, name, imagePath);
	}
	
	public TierElementRanked(String name) {
		super(RANKED, name);
	}
	
	public TierElementRanked() {
		super(RANKED);
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
