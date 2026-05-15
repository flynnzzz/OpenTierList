package model;

import static model.enums.TierElementStatus.*;

public class TierElementUnranked extends TierElement {
	
	public TierElementUnranked(TierElement e) {
		super(UNRANKED, e.getName(), e.getImagePath());
	}
	
	public TierElementUnranked(String name, String imagePath) {
		super(UNRANKED, name, imagePath);
	}
	
	public TierElementUnranked(String name) {
		super(UNRANKED, name);
	}
	
	public TierElementUnranked() {
		super(UNRANKED);
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
