package model.enums;

// TODO: change TierElement status from boolean to TierElementStatus
public enum TierElementStatus {
	RANKED(true), NOTRANKED(false);
	
	private boolean value;
	
	private TierElementStatus(boolean value) { this.setValue(value); }

	public boolean value() { return value; }

	private void setValue(boolean value) { this.value = value; }
}
