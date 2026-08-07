package net.flynn.opentierlist.model.enums;

public enum TieredStatus {
	TIERED(true), UNTIERED(false);
	
	private boolean value;

	TieredStatus(boolean value) { this.setValue(value); }

	public boolean value() { return value; }

	private void setValue(boolean value) { this.value = value; }
}
