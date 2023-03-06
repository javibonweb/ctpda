package es.juntadeandalucia.ctpda.gestionpdt.service.core.exception;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * The Class BaseException.
 */
public class BaseException extends IOException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * The Errors.
	 */
	private final List<String> errors;

	/**
	 * Instantiates a new cinta backend exception.
	 *
	 * @param message the message
	 * @param errors  the errors
	 */
	public BaseException(String message, List<String> errors) {
		super(message);
		this.errors = errors;
	}
	
	/**
	 * Instantiates a new cinta backend exception.
	 *
	 * @param message the message
	 * @param errors  the errors
	 */
	public BaseException(String message) {
		super(message);
		this.errors = Collections.emptyList();
	}

	/**
	 * Instantiates a new base exception.
	 *
	 * @param e the e
	 */
	public BaseException(Throwable e) {
		super(e);
		this.errors = Collections.emptyList();
	}

	/**
	 * Gets errors.
	 *
	 * @return the errors
	 */
	public List<String> getErrors() {
		return errors;
	}

}
