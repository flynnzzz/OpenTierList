package model.exceptions;

/**
 * Custom Exception class
 * 
 * @version 0.00
 * @since v1.0.0
 */
public class TierNotFoundException extends IndexOutOfBoundsException {

	private static final long serialVersionUID = 1827696283991396826L;

	public TierNotFoundException(String message) {
		super(message);
	}
	
	public TierNotFoundException() {
		super();
	}
}
