package exceptions;

/**
 * Custom Exception class
 * 
 * @version 0.00
 * @since v1.0.0
 */
public class ElementNotFoundException extends IndexOutOfBoundsException {

	private static final long serialVersionUID = -6509042509524524415L;
	
	public ElementNotFoundException(String message) {
		super(message);
	}
	
	public ElementNotFoundException() {
		super();
	}
}
