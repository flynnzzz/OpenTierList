package net.flynn.opentierlist.model.exceptions;

/**
 * Custom Exception class
 * 
 * @version 0.00
 * @since v1.0.0
 */
public class TelementNotFoundException extends IndexOutOfBoundsException {

	private static final long serialVersionUID = -6509042509524524415L;
	
	public TelementNotFoundException(String message) {
		super(message);
	}
	
	public TelementNotFoundException() {
		super();
	}
}
