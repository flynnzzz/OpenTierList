package model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ElementCollection {
	private List<Element> elements;
	
	public ElementCollection(List<Element> elements) {
		this.elements = elements;
	}
	
	public ElementCollection() {
		this.elements = new LinkedList<Element>();
	}
	
	// wrappers for LinkedList methods
	public boolean addElement(Element e) { return elements.add(e); }
	public boolean removeElement(Element e) { return elements.remove(e); }
	public int size() { return elements.size(); }
	public int indexOf(Element e) { return elements.indexOf(e); }
	public Element get(int idx) { return elements.get(idx); }
	
	public ElementCollection clone() { return new ElementCollection(elements); }

	public boolean swap(Element a, Element b) {
		int idxA = elements.indexOf(a), idxB = elements.indexOf(b);
		if (idxA < 0 || idxB < 0)
			return false;
		elements.set(idxA, b);
		elements.set(idxB, a);
		return true;
	}

	public List<Element> getElements() { return List.copyOf(elements); }		
	
	@Override
	public int hashCode() { return Objects.hash(elements); }

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!(obj instanceof ElementCollection other)) { return false; }
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
