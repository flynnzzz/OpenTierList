package model;

import java.awt.Color;
import java.util.Objects;

public class Tier {
	String name, DEFAULT_NAME = "New Tier";
	Color color, DEFAULT_COLOR = new Color(128, 128, 128);
	RankedElements rankedElements;
	
	public Tier(String name, Color color, RankedElements rankedElements) {
		this.name = name;
		this.color = color;
		this.rankedElements = rankedElements;
	}
	
	public Tier(String name, Color color) {
		this.name = name;
		this.color = color;
		this.rankedElements = new RankedElements();
	}
	
	public Tier(String name) {
		this.name = name;
		this.color = DEFAULT_COLOR;
		this.rankedElements = new RankedElements();
	}
	
	public Tier() {
		this.name = DEFAULT_NAME;;
		this.color = DEFAULT_COLOR;
		this.rankedElements = new RankedElements();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public ElementCollection getRankedElements() {
		return rankedElements.clone();
	}
	
	public boolean addElement(Element e) {
		boolean success = rankedElements.addElement(e);
		if (success)
			e.setRanked(true);
		return success;
	}
	
	public boolean removeElement(Element e) { 
		boolean success = rankedElements.removeElement(e);
		if (success)
			e.setRanked(false);
		return success;
	}
	
	public boolean swapElements(Element a, Element b) {
		return rankedElements.swap(a, b);
	}
	
	public int indexOf(Element e) {
		return rankedElements.indexOf(e);
	}
	
	public Element get(int idx) {
		return rankedElements.get(idx);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(color, name, rankedElements);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Tier)) {
			return false;
		}
		Tier other = (Tier) obj;
		return Objects.equals(color, other.color) && Objects.equals(name, other.name)
				&& Objects.equals(rankedElements, other.rankedElements);
	}

	@Override
	public String toString() {
		 return "Tier: " + name + System.lineSeparator() +
				 rankedElements.toString();
	}
}
