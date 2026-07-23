package net.flynn.opentierlist.model.enums;

public enum TieredStatus {
	TIERED(true), UNTIERED(false), METASTABLE;
	
	private boolean value;
	
	private TieredStatus() {}
	private TieredStatus(boolean value) { this.setValue(value); }

	public boolean value() { return value; }

	private void setValue(boolean value) { this.value = value; }
}
