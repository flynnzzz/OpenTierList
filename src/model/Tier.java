package model;

import java.awt.Color;
import java.util.List;
import java.util.Objects;

public class Tier extends ElementCollection {
	
	private String DEFAULT_NAME = "New Tier";
	private Color DEFAULT_COLOR = Color.gray;
	
	private TierHeader header;
	
	public Tier(String name, Color color, List<Element> ranked) {
		super(ranked);
		this.header = new TierHeader(name, color);
	}	
	public Tier(String name, Color color) { super(); this.header = new TierHeader(name, color); }
	public Tier(String name) { super(); this.header = new TierHeader(name, DEFAULT_COLOR); }
	public Tier() { super(); this.header = new TierHeader(DEFAULT_NAME, DEFAULT_COLOR);  }
	
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
		result = prime * result + Objects.hash(DEFAULT_COLOR, DEFAULT_NAME, header);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (!super.equals(obj)) { return false; }
		
		if (!(obj instanceof Tier other)) { return false; }
		return Objects.equals(DEFAULT_COLOR, other.DEFAULT_COLOR) && Objects.equals(DEFAULT_NAME, other.DEFAULT_NAME)
				&& Objects.equals(header, other.header);
	}

	@Override
	public String toString() {
		 return "Tier: " + header.name() + System.lineSeparator() +
				 super.toString();
	}
}
