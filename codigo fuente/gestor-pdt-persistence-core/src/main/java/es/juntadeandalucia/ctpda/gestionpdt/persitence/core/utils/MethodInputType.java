package es.juntadeandalucia.ctpda.gestionpdt.persitence.core.utils;

import lombok.Builder;
import lombok.Data;


/**
 * The type Method input type.
 */

@Data
@Builder
public class MethodInputType {

	/** The method name. */
	private String methodName;
	
	/** The input type. */
	private Class<?> inputType;
}
