package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ElementCollection {
	List<Element> elements;
	
	public ElementCollection(List<Element> elements) {
		this.elements = elements;
	}
	
	public ElementCollection() {
		this.elements = new ArrayList<Element>();
	}
	
	public List<Element> getElements() {
		if (!elements.isEmpty())
			return List.copyOf(elements);
		return new ArrayList<Element>();
	}
	
	public boolean addElement(Element e) {
		if (elements.indexOf(e) == -1) {
			elements.add(e); 
			return true;
		}
		return false;
	}
	
	public boolean removeElement(Element e) {
		if (elements.indexOf(e) != -1)
			return elements.remove(e);
		return false;
	}
	
	public int size() {
		return elements.size();
	}
	
	public boolean swap(Element a, Element b) {
		int idxA = elements.indexOf(a), idxB = elements.indexOf(b);
		if (idxA < 0 || idxB < 0)
			return false;
		elements.set(idxA, b);
		elements.set(idxB, a);
		return true;
	}
	
	public ElementCollection clone() {
		return new ElementCollection(elements);
	}
	
	public int indexOf(Element e) {
		return elements.indexOf(e);
	}
	
	public Element get(int idx) {
		return elements.get(idx);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(elements);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ElementCollection)) {
			return false;
		}
		ElementCollection other = (ElementCollection) obj;
		return Objects.equals(elements, other.elements);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Elements: \n["); sb.append(System.lineSeparator());
		for (int i = 0; i < elements.size(); i++) {	
			sb.append("\t");
			sb.append(elements.get(i)); 
			sb.append(",");
			sb.append(System.lineSeparator());
		}
		sb.append("]");
		return sb.toString();
	}
}
