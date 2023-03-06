package es.juntadeandalucia.ctpda.gestionpdt.service.core.exception;

import java.util.Collections;
import java.util.List;

/**
 * Esta excepción se puede elevar normalmente con throw new ValidacionException([mensaje]).
 * También permite indicar varios mensajes de validación de una vez. En este caso 
 * el parámetro mensaje está pensado para usarse como cabecera del popup donde se muestren dichos mensajes. 
 * @author rafael.cano
 *
 */

public class ValidacionException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ValidacionException(String message) {
		super(message, Collections.emptyList());		
	}
	
	public ValidacionException(String message, List<String> errors) {
		super(message, errors);
	}

	public ValidacionException(Throwable e) {
		super(e);
	}

}
