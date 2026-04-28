package model;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Tier extends ElementCollection {
	
	private String DEFAULT_NAME = "New Tier";
	private Color DEFAULT_COLOR = Color.gray;
	
	private TierHeader header;
	private List<Element> ranked;
	
	public Tier(String name, Color color, List<Element> ranked) {
		this.header = new TierHeader(name, color);
		super(ranked);
	}	
	public Tier(String name, Color color) { this.header = new TierHeader(name, color); this.ranked = new LinkedList<>(); }
	public Tier(String name) { this.header = new TierHeader(name, DEFAULT_COLOR); this.ranked = new LinkedList<>(); }
	public Tier() { this.header = new TierHeader(DEFAULT_NAME, DEFAULT_COLOR); this.ranked = new LinkedList<>(); }
	
	// getters and setters
	public String getName() { return header.name(); }
	public void setName(String name) { this.header = new TierHeader(name, this.getColor());}
	public Color getColor() { return header.color(); }
	public void setColor(Color color) { this.header = new TierHeader(this.getName(), color);}
	public TierHeader getHeader() { return this.header; }
	public void setHeader(TierHeader header) { this.header = header; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(DEFAULT_COLOR, DEFAULT_NAME, header, ranked);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!super.equals(obj)) { return false; }
		
		if (!(obj instanceof Tier other)) { return false; }
		return Objects.equals(DEFAULT_COLOR, other.DEFAULT_COLOR) && Objects.equals(DEFAULT_NAME, other.DEFAULT_NAME)
				&& Objects.equals(header, other.header) && Objects.equals(ranked, other.ranked);
	}

	@Override
	public String toString() {
		 return "Tier: " + header.name() + System.lineSeparator() +
				 ranked.toString();
	}
}
