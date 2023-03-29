package es.juntadeandalucia.ctpda.gestionpdt.model.core;

import java.io.Serializable;

/**
 * El Interface IDTO.
 */

public interface EntidadBasica extends Serializable{

	/**
	 * Obtiene el id.
	 *
	 * @return Devuelve el id.
	 */
	Long getId(); //probamos con serializable a ver si nos da más margen
	
	/** Obtiene el id de una entidad forma null-safe.
	 * 
	 * @return Devuelve el id de la entidad e, o null si e = null.
	 */
	static Long getIdEntidad(EntidadBasica e) {
		return e != null? e.getId() : null;		
	}

}
