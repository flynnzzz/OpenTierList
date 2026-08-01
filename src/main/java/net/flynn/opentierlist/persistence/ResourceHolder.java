package net.flynn.opentierlist.persistence;

public class ResourceHolder {

  private final static String defaultTelementIcon = "/default_icon.jpeg";

  // placeholders
  private final static String editButtonIcon = "/edit_icon_resized.png";
  private final static String addElementButtonIcon = "/add_element_icon.png";
  private final static String addTierButtonIcon = "/add_tier_icon.png";

  public static String getDefaultElementIcon() {
    return ResourceHolder.defaultTelementIcon;
  }

  public static String getAddElementButtonIcon() {
    return ResourceHolder.addElementButtonIcon;
  }

  public static String getAddTierButtonIcon() {
    return ResourceHolder.addTierButtonIcon;
  }

  public static String getEditButtonIcon() {
    return ResourceHolder.editButtonIcon;
  }
}
