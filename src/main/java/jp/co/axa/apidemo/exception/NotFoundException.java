package jp.co.axa.apidemo.exception;

public class NotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFoundException(Long id) {
        super("Could not find employee with id " + id + ".");
    }
}
