package opentierlist.persistence;

public class ResourceHolder {
	
	private final static String defaultTelementIcon = "/default_icon.jpeg";
	
	// for testing only
	// TODO: eventually remove
	private final static String defaultImagesFolder = "/external/images/";
	
	// placeholders
	private final static String editButtonIcon = "/edit_icon_resized.png";
	private final static String addElementButtonIcon = "/add_element_icon.png";
	private final static String addTierButtonIcon = "/add_tier_icon.png";
	
	public static String getDefaultTelementIcon() {
		return ResourceHolder.defaultTelementIcon;
	}
	
	public static String getAddelementbuttonicon() {
		return ResourceHolder.addElementButtonIcon;
	}

	public static String getAddtierbuttonicon() {
		return ResourceHolder.addTierButtonIcon;
	}

	public static String getDefaultImagesFolder() {
		return ResourceHolder.defaultImagesFolder;
	}
	
	public static String getEditButtonIcon() {
		return ResourceHolder.editButtonIcon;
	}
}
