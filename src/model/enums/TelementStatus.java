package model.enums;

public enum TelementStatus {
	RANKED(true), UNRANKED(false), METASTABLE; 
	
	private boolean value;
	
	private TelementStatus() {}
	private TelementStatus(boolean value) { this.setValue(value); }

	public boolean value() { return value; }

	private void setValue(boolean value) { this.value = value; }
}
