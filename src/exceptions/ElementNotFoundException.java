package exceptions;

public class ElementNotFoundException extends IndexOutOfBoundsException {

	private static final long serialVersionUID = -6509042509524524415L;
	
	public ElementNotFoundException(String message) {
		super(message);
	}
	
	public ElementNotFoundException() {
		super();
	}
}
