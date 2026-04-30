package model;

import java.util.List;
import java.util.Map;

/**
 * A class representing the concept of tier list.
 * 
 * Container of a {@link Map} with keys: {@link TierHeader} and values: {@link ElementCollection}
 * 
 * @author flynnz
 * @version 0.00
 * @since v0.0.0
 */
public class TierList {
	private String name;
	
	private Map<TierHeader, ElementCollection> contents;
	private ElementCollection unranked;
	
	public TierList(String name, List<Tier> tiers, ElementCollection unranked) {}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<TierHeader, ElementCollection> getContents() {
		return contents;
	}

	public void setContents(Map<TierHeader, ElementCollection> contents) {
		this.contents = contents;
	}

	public ElementCollection getUnranked() {
		return unranked;
	}

	public void setUnranked(ElementCollection unranked) {
		this.unranked = unranked;
	}

}
