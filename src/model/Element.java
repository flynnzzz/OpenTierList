package model;

import java.util.Objects;

public class Element {
	private boolean ranked;
	private String name;
	private String imagePath;

	public Element(boolean isRanked,  String name, String imagePath) {
		this.ranked = isRanked;
		this.name = name;
		this.imagePath = imagePath;
	}
	
	// getters and setters
	public boolean isRanked() { return ranked; }
	public void setRanked(boolean ranked) { this.ranked = ranked; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getImagePath() { return imagePath; }
	public void setImagePath(String imagePath) { this.imagePath = imagePath; }
	
	@Override
	public int hashCode() {
		return Objects.hash(imagePath, name, ranked);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof Element other)) { return false; }
		return Objects.equals(imagePath, other.imagePath) && Objects.equals(name, other.name) && ranked == other.ranked;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(); 
		sb.append(name + ": ");
		if (!ranked)
			sb.append("not ");
		sb.append("ranked");

		return sb.toString();
	}
}
