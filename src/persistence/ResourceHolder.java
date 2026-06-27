package persistence;

public class ResourceHolder {
	private final static String defaultTelementIcon = "/default_icon.jpeg";
	private final static String defaultImagesFolder = "/external/images/";
	private final static String editButtonIcon = "/edit_icon_resized.png";
	
	public static String getDefaultTelementIcon() {
		return ResourceHolder.defaultTelementIcon;
	}
	
	public static String getDefaultImagesFolder() {
		return ResourceHolder.defaultImagesFolder;
	}
	
	public static String getEditButtonIcon() {
		return ResourceHolder.editButtonIcon;
	}
}
