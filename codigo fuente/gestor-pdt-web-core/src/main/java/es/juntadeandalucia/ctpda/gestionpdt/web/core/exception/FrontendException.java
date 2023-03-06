package es.juntadeandalucia.ctpda.gestionpdt.web.core.exception;

import java.util.List;

import es.juntadeandalucia.ctpda.gestionpdt.service.core.exception.BaseException;

/**
 * Excepción de frontend base.
 * Se recomienda que las propias de negocio hereden de esta. 
 * */
public class FrontendException extends BaseException{

	/**
	 * serial uid
	 */
	private static final long serialVersionUID = 186348L;

	public FrontendException(String message, List<String> errors) {
		super(message, errors);
	}

	public FrontendException(Throwable e) {
		super(e);
	}

}
