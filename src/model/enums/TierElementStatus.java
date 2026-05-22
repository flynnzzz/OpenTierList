package model.enums;

public enum TierElementStatus {
	RANKED(true), UNRANKED(false), METASTABLE; 
	
	private boolean value;
	
	private TierElementStatus() {}
	private TierElementStatus(boolean value) { this.setValue(value); }

	public boolean value() { return value; }

	private void setValue(boolean value) { this.value = value; }
}
